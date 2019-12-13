package com.savior.vo;

import java.io.Serializable;

/**
 * @Auther: weiwe
 * @Date: 2019/11/29 10:26
 * @Description:
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String method;

    private String fieldName;

    private String param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "ClassInfo[name = " + this.getName() + ", method = " + this.getMethod() + ", " +
                "fieldName = " + this.getFieldName() + ", param = " + this.getParam() + "]";
    }
}
