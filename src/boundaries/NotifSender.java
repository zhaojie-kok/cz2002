package boundaries;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Default class for sending notifications to users.
 * Method of notifying is email by default.
 */
public class NotifSender {

	/**
	 * Method to send notifications via email
	 * 
	 * @param subject Email subject
	 * @param body    Message body
	 * @param address Recipient's email address
	 * @return true if notification was made successfully, false otherwise
	 * @throws MessagingException in event where notification cannot be made, exception is thrown
	 */
	public static boolean sendNotif(String subject, String body, String address) throws MessagingException {
		final String username = "czassignment482"; // to be added
		final String password = "javaisfun"; // to be added

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		boolean success = false;

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(address)); 
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			success = true;

		} catch (MessagingException e) {
			throw new MessagingException("Unable to send notification. Please inform system administrator");
		}

		return success;
	}
}