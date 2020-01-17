package com.savior;

import com.savior.protocol.HttpXmlProtocolServer;
import com.savior.websocket.WebSocketServer;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 09:52
 * @Description:
 */
public class ApplicationMain {

    public static void main(String[] arg) {
//        HttpProtocolServer httpProtocolServer = new HttpProtocolServer(8899);
//        try {
//            httpProtocolServer.createServer("/");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            httpProtocolServer.closeServer();
//        }
       /* HttpXmlProtocolServer httpXmlProtocolServer = new HttpXmlProtocolServer(8899);
        httpXmlProtocolServer.createServer();*/
        WebSocketServer webSocketServer = new WebSocketServer(8899);
        webSocketServer.connection();
    }

}
