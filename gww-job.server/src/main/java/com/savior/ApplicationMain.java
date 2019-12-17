package com.savior;

import com.savior.protocol.HttpProtocolServer;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 09:52
 * @Description:
 */
public class ApplicationMain {

    public static void main(String[] arg) {
        HttpProtocolServer httpProtocolServer = new HttpProtocolServer(8899);
        try {
            httpProtocolServer.createServer("/");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            httpProtocolServer.closeServer();
        }
    }

}
