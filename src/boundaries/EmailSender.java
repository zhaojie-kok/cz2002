package boundaries;

/**
 * Interface for sending emails
 */
public interface EmailSender {
    /**
     * Method to send email, to be implemented by concrete classes like NotifSender
     * 
     * @param subject Subject header of email
     * @param body    Body of email
     * @param address Email address of recipient
     */
    public void sendEmail(String subject, String body, String address) throws Exception;
}
