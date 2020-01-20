package com.savior;

import com.savior.udp.UDPProtocol;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 11:41
 * @Description:
 */
public class ApplicationClientMain {

    public static void main(String[] arg) throws InterruptedException {
       /* TimeServerClient timeServerClient = new TimeServerClient();
        timeServerClient.connected(8899);*/
//        HttpXmlClient httpXmlClient = new HttpXmlClient(8899);
//        httpXmlClient.connection();
        UDPProtocol udpProtocol = new UDPProtocol();
        udpProtocol.run(8899);
    }

}