package com.savior;

import com.savior.client.TimeServerClient;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 11:41
 * @Description:
 */
public class ApplicationClientMain {

    public static void main(String[] arg) throws InterruptedException {
        TimeServerClient timeServerClient = new TimeServerClient();
        timeServerClient.connected(8899);
    }

}