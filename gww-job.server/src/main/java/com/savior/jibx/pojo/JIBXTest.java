package com.savior.jibx.pojo;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * @Auther: weiwe
 * @Date: 2020/1/13 16:41
 * @Description:
 */
public class JIBXTest {

    IBindingFactory bindingFactory = null;
    StringWriter writer = null;
    StringReader reader = null;
    String charset_name = "UTF-8";
    Charset charset = Charset.forName(charset_name);

    public static void main(String[] arg) {

    }

    public void encode(Class tClass) throws Exception {
        bindingFactory = BindingDirectory.getFactory(tClass);
        writer = new StringWriter();
        IMarshallingContext mctx = bindingFactory.createMarshallingContext();
        mctx.setIndent(2);

    }

}
