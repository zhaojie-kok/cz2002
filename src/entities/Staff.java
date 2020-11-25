package entities;

import exceptions.MissingParametersException;

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
     * @param userId      UserID for the staff. Must be unique for each user
     * @param name        Name of the staff user
     * @param gender      Gender of the staff user
     * @param nationality Nationality of the staff user
     * @param email 	  Email address of staff user
     * @throws MissingParametersException Thrown if any of the above fields are null
     */
    public Staff(String staffNo, String userId, String name, String gender, String nationality, String email)
            throws MissingParametersException {
        super(userId, "staff", name, gender, nationality, email);

        if (staffNo == null) {
            throw new MissingParametersException("Staff Number cannot be null");
        }
        this.staffNo = staffNo;
    }

    /**
     * Getter for the staff's identification number
     * @return Staff identification number
     */
    public String getStaffNo() {
        return this.staffNo;
    }
}
