package soccer.access.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {
	public static void sendMail(String userEmail, String errinfo){
		try {  
	           Properties p = new Properties();   
	            p.put("mail.smtp.auth", "true");  
	            p.put("mail.transport.protocol", "smtp");  
	            p.put("mail.smtp.host", "smtp.163.com");  
	            p.put("mail.smtp.port", "25");  
	            //建立会话  
	            Session session = Session.getInstance(p);  
	            Message msg = new MimeMessage(session); //建立信息  
	   
	            msg.setFrom(new InternetAddress("15365033861@163.com")); //发件人  
	            msg.setRecipient(Message.RecipientType.TO,  
	                             new InternetAddress(userEmail)); //收件人  
	   
	            msg.setSentDate(new Date()); // 发送日期  
	            msg.setSubject("Jscorer程序自动通知"); // 主题  
	            msg.setText(errinfo); //内容  
	            // 邮件服务器进行验证  
	            Transport tran = session.getTransport("smtp");  
	            tran.connect("smtp.163.com", "15365033861", "Shuzui1985");  
	            // bluebit_cn是用户名，Shuzui1985是密码  
	            tran.sendMessage(msg, msg.getAllRecipients()); // 发送  
	            System.out.println("邮件发送成功");  
	   
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }
	            		
	}
	

	public static void sendOrderInfo(String userEmail, String fileName){
		try {
			Properties p = new Properties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.transport.protocol", "smtp");
			p.put("mail.smtp.host", "smtp.126.com");
			p.put("mail.smtp.port", "25");
			// 建立会话
			Session session = Session.getInstance(p);
			Message msg = new MimeMessage(session); // 建立信息

			msg.setFrom(new InternetAddress("communityishare@126.com")); // 发件人
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
					userEmail)); // 收件人

			msg.setSentDate(new Date()); // 发送日期
			msg.setSubject("欢迎您加入社区ishare！！"); // 主题
			msg.setText("订单见附件"); // 内容
			// 邮件服务器进行验证

			MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			FileDataSource fds = new FileDataSource(new File(fileName));
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp2);

			// add the Multipart to the message
			msg.setContent(mp);

			Transport tran = session.getTransport("smtp");
			tran.connect("smtp.126.com", "communityishare", "cx1887356359");
			// bluebit_cn是用户名，xiaohao是密码
			tran.sendMessage(msg, msg.getAllRecipients()); // 发送
			System.out.println("邮件发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
