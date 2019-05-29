package com.menglingqiang.schedule.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;


//发送邮件工具类
public class SendUtil {


  private static Session session = getSession();

  public static Session getSession() {
    //
    Properties pro = new Properties();
    // 发送服务器需要身份验证
    pro.setProperty("mail.smtp.auth", "true");
    // 设置邮件服务器主机名
    pro.setProperty("mail.smtp.host", "smtp.qq.com");
    // 发送邮件协议名称
    pro.setProperty("mail.transport.protocol", "smtp");
    pro.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    pro.setProperty("mail.smtp.port", "465");
    pro.setProperty("mail.smtp.ssl.enable", "true");
    // 设置环境信息
    Session session = Session.getInstance(pro, new Authenticator() {
      @Override
      protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

        return new javax.mail.PasswordAuthentication(
            Constant.SENDEMAIL, Constant.SENDPASSWORD);
      }

    });
    //session.setDebug(true);
    return session;
  }

  public static void sendString(String to, String code) {

    session = session == null ? getSession() : session;
    try {
      // 创建邮件对象
      Message msg = new MimeMessage(session);
      msg.setSubject(Constant.SENDTITLE);
      // 设置邮件内容
      msg.setContent(code, "text/html;charset=UTF-8");
      //msg.setText("内容为： 这是第一封java发送来的邮件。");
      // 设置发件人
      msg.setFrom(new InternetAddress(Constant.SENDEMAIL));//从我的邮箱  ,服务器邮箱，这个发件人要和上面的授权的人是一个邮箱
      msg.saveChanges();
      msg.setRecipient(RecipientType.TO, new InternetAddress(to));//目的邮箱
      Transport.send(msg);
      // 关闭连接
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();//发送不成功，用户信息不会插入表中
    }
  }

  public static boolean sendAttach(String to, List<String> filePaths) {
    //目前就是我自己的邮箱
    session = session == null ? getSession() : session;
    // 创建邮件对象
    Message msg = new MimeMessage(session);
    MimeMultipart mmp = new MimeMultipart("mixed");//MIME消息头组合类型是mixed(html+附件)
    try {
      // 设置发件人
      msg.setFrom(new InternetAddress(Constant.SENDEMAIL));//从我的邮箱  ,服务器邮箱，这个发件人要和上面的授权的人是一个邮箱
      msg.setRecipient(RecipientType.TO, new InternetAddress(to));//目的邮箱
      msg.setSubject(Constant.SENDKINDLETITLE);
      msg.setContent("发一波书", "text/html;charset=UTF-8");
      for (String filePath : filePaths) {
        mmp.addBodyPart(getAttachedBodyPart(filePath));
      }
      msg.setContent(mmp);
      msg.saveChanges();
      //自动close开销比较大,用sendMessage比较好,反正也没人用无所谓.
      Transport.send(msg);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

  }

  public static String doHandlerFileName(String filePath) {
    String fileName = filePath;
    if (null != filePath && !"".equals(filePath)) {
      fileName = filePath.substring(filePath.lastIndexOf("//") + 1);
    }
    return fileName;
  }

  public static MimeBodyPart getAttachedBodyPart(String filePath) throws MessagingException,
      UnsupportedEncodingException {
    MimeBodyPart attached = new MimeBodyPart();
    FileDataSource fds = new FileDataSource(filePath);
    attached.setDataHandler(new DataHandler(fds));
    String fileName = doHandlerFileName(filePath);
    attached.setFileName(MimeUtility.encodeWord(fileName));//处理附件文件的中文名问题
    return attached;
  }

  public static void main(String[] args) {
    sendString("1820026438@qq.com", "aasdfdsff");
  }
}
