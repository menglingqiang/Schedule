package com.menglingqiang.schedule.util;

import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;


//发送邮件工具类
public class SendUtil {

	public static void send(String to,String code)
	{
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
        Session session = Session.getInstance(pro,new Authenticator() {
        	@Override
        	protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        		
        		return new javax.mail.PasswordAuthentication(
        				Constant.SENDEMAIL, Constant.SENDPASSWORD);
        	}
        	
		});  
        //session.setDebug(true);  
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
	
	
	public static void main(String[] args)
	{
		send("1820026438@qq.com", "aasdfdsff");
	}
}
