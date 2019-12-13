package com.savior.vo;

/**
 * @Auther: weiwe
 * @Date: 2019/11/29 10:38
 * @Description:
 */
public class ResponInfo implements  java.io.Serializable {

    private static final long serialVersionUid = 1L;

    private int respCode;

    private String message;


    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponInfo[respCode = " + this.getRespCode() + ", message = " + this.getMessage() + "]";
    }
}
