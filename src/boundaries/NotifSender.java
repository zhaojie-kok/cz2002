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
public class NotifSender implements EmailSender{
	/**
	 * Method to send email to recipient
	 */
	@Override
	public void sendEmail(String subject, String body, String address) throws MessagingException {
		final String username = "czassignment482"; // to be added
		final String password = "javaisfun"; // to be added

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new MessagingException("Unable to send notification. Please inform system administrator");
		}
	}
	/**
	 * Method to send notifications
	 * 
	 * @param subject Notification subject
	 * @param body    Message body
	 * @param contact Recipient's contact
	 * @param mode	  The mode of communication (only supports email currently)
	 * 				  Must be one of following <ol> <li> email </li> </ol>
	 * @return true if notification was made successfully, false otherwise
	 * @throws MessagingException in event where notification cannot be made, exception is thrown
	 */
	public boolean sendNotif(String subject, String body, Object contact, String mode) throws MessagingException {
		if (mode == "email") {
			sendEmail(subject, body, (String) contact);
			return true;
		}

		return false;
	}
}