package com.tensquare.sms.listener;



import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class sendSms {

    @Autowired
    private SmsUtil smsUtil;
    @Value("${aliyun.sms.template_code}")
    private String template_code; //模板编号
    @Value("${aliyun.sms.sign_name}")
    private String sign_name;  //签名


    @RabbitHandler
    public void sendSms(Map<String,String> message){
        String mobile=message.get("mobile");
        String checkcode=message.get("code");
        System.out.println("手机号："+message.get("mobile"));
        System.out.println("验证码："+message.get("code"));
        System.out.println("验证码："+message.get("code"));
     try{
         smsUtil.sendSms(mobile,template_code,sign_name,"{\"checkcode\":\""+checkcode+"\"}");
     }catch (ClientException e){
         e.printStackTrace();
     }
    }

}
