package entities;

/**
 * Staff entities meant for staff/admin users
 */
public class Staff extends User{
    private static final long serialVersionUID = -7309047941954437648L;
    private String staffNo;

    /**
     * Constructor
     * 
     * @param staffNo     String identifier for the staff user. Must be unique for
     *                    each staff user
     * @param userId      UserID for the staff. Must be unique for each staff user\
     * @param name        Name of the staff user
     * @param gender      Gender of the staff user
     * @param nationality Nationality of the staff user
     */
    public Staff(String staffNo, String userId, String name, String gender, String nationality) {
        super(userId, "staff", name, gender, nationality);
        this.staffNo = staffNo;
    }

    /**
     * Getter for the staff's identification number
     */
    public String getStaffNo() {
        return this.staffNo;
    }
}
