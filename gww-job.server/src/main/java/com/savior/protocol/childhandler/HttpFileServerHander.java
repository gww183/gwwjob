package com.savior.protocol.childhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Auther: weiwe
 * @Date: 2019/12/16 15:20
 * @Description:
 */
public class HttpFileServerHander extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String fileUrl;

    public HttpFileServerHander(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.uri();
        final String path = sanitizeUri(uri);
        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(ctx, file);
            } else {
                sendRedirect(ctx, uri + "/");
            }
            return;
        }
        if (!file.isFile()) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "r");
        long fileLength = randomAccessFile.length();
        // TODO 注意这里是DefaultHttpResponse而不是DefaultFullHttpResponse
        HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpHeaderUtil.setContentLength(httpResponse, fileLength);
        setContentType(httpResponse, file);
        if (isKeepAlive(request)) {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(httpResponse);
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 1024), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress:" + progress);
                } else {
                    System.err.println("Transfer progress:" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete.");
            }
        });

        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if(!HttpHeaderUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 校验是否是保持连接的
     *
     * @param request
     * @return
     */
    private boolean isKeepAlive(FullHttpRequest request) {
        return HttpHeaderUtil.isKeepAlive(request);
    }

    /**
     * 设置内容类型
     *
     * @param httpResponse
     * @param file
     */
    private void setContentType(HttpResponse httpResponse, File file) {
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, fileTypeMap.getContentType(file.getPath()));
    }


    /**
     * 重定向
     *
     * @param ctx
     * @param uri
     */
    private void sendRedirect(ChannelHandlerContext ctx, String uri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, uri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendListing(ChannelHandlerContext ctx, File file) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuffer buffer = new StringBuffer();
        String dirPath = file.getPath();
        buffer.append("<!DOCTYPE html>\r\n");
        buffer.append("<html><head><title>");
        buffer.append(dirPath);
        buffer.append("目录：");
        buffer.append("</title></head>\r\n");
        buffer.append("<body>\r\n");
        buffer.append("<h3>");
        buffer.append(dirPath).append("目录:");
        buffer.append("</h3>\r\n");
        buffer.append("<ul>");
        buffer.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        for (File f : file.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            buffer.append("<li>链接：<a href=\"");
            buffer.append(f.getName());
            buffer.append("\">");
            buffer.append(f.getName());
            buffer.append("</a></li>\r\n");
        }
        buffer.append("</ul></body></html>\r\n");
        ByteBuf byteBuf = Unpooled.copiedBuffer(buffer, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 过滤uri
     *
     * @param uri
     * @return
     */
    private String sanitizeUri(String uri) throws Exception {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Exception();
            }
        }
        if (!uri.startsWith(fileUrl)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        uri = uri.replace('/', File.separatorChar);

        return System.getProperty("user.dir") + File.separator + uri;
    }


    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus badRequest) {
        FullHttpResponse httpResponse =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, badRequest,
                        Unpooled.copiedBuffer("Failure:" + badRequest.toString() + "\r\n", CharsetUtil.UTF_8));
        httpResponse.headers().set("CONTENT_TYPE", "text/plain; charset=UTF-8");
        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

}
