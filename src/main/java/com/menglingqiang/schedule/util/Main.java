package com.menglingqiang.schedule.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

public class Main {

  public static void main(String[] args) {

    Properties properties = new Properties();

    properties.put("mail.transport.protocol", "smtp");// 连接协议

    properties.put("mail.smtp.host", "smtp.qq.com");// 主机名

    properties.put("mail.smtp.port", 465);// 端口号

    properties.put("mail.smtp.auth", "true");

    properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用

    properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息

    // 得到回话对象

    Session session = Session.getInstance(properties);

    // 获取邮件对象

    Message message = new MimeMessage(session);

    // 设置发件人邮箱地址

    try {
      message.setFrom(new InternetAddress("1820026438@qq.com"));
      // 设置收件人地址 message.setRecipients( RecipientType.TO, new
      // InternetAddress[] { new InternetAddress("987654321@qq.com") });

      // 设置邮件标题

      message.setSubject("这是第一封Java邮件");

      // 设置邮件内容
      message.setContent("jadskfj", "text/html;charset=UTF-8");
      //message.setText("内容为： 这是第一封java发送来的邮件。");

      // 得到邮差对象

      message.setRecipient(RecipientType.TO, new InternetAddress("1820026438@qq.com"));//目的邮箱
      Transport transport = session.getTransport();

      // 连接自己的邮箱账户
      transport.connect("1820026438@qq.com", "pehvbogobnmmeaec");// 密码为刚才得到的授权码

      transport.sendMessage(message, message.getAllRecipients());
    } catch (AddressException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

}