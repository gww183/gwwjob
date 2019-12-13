package com.savior;

import com.savior.server.*;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 09:52
 * @Description:
 */
public class ApplicationMain {

    public static void main(String[] arg) {
        TimeServer timeServer = new TimeServer();
        timeServer.bind(8899);
    }

}
