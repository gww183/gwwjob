package com.savior.jibx.pojo;

import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jibx.binding.Compile;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;

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
//        compile();
        Order order = new Order();
        order.setOrderNumber(10000);
        order.setShipping(Shipping.DOMESTIC_EXPRESS);
        Address address = new Address();
        address.setCity("北京");
        address.setCountry("中国");
        address.setPostCode("010");
        address.setState("长安街");
        address.setStreet1("万宝路1号");
        order.setBillTo(address);
        Customer customer= new Customer();
        customer.setCustomerNumber(1121212);
        customer.setFistName("张");
        customer.setLastName("三");
        customer.setMiddleNames(Arrays.asList("111", "222", "333"));
        order.setCustomer(customer);
        JIBXTest jibxTest = new JIBXTest();
        String xmlStr = null;
        try {
            xmlStr = jibxTest.encode(order);
            jibxTest.decode(xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void compile() {
        String[] args = new String[2];

        // 打印生成过程的详细信息。可选
        args[0] = "-v";

        // 指定 binding 和 schema 文件的路径。必须
        args[1] = "D:/project_java/gww_job/gww-job.server/src/main/resources/jibx/request_order.xml";

        Compile.main(args);
    }

    public String encode(Object object) throws Exception {
        bindingFactory = BindingDirectory.getFactory(Order.class);
        writer = new StringWriter();
        IMarshallingContext mctx = bindingFactory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(object, charset_name, null, writer);
        String xmlStr = writer.toString();
        writer.close();
        System.out.println(xmlStr);
        return xmlStr;
    }

    public void decode(String xmlStr) throws Exception {
        bindingFactory = BindingDirectory.getFactory(Order.class);
        IUnmarshallingContext ucx = bindingFactory.createUnmarshallingContext();
        StringReader stringReader = new StringReader(xmlStr);
        Order order = (Order) ucx.unmarshalDocument(stringReader);
        System.out.println("客户姓名： " + order.getCustomer().getFistName());
    }

}
