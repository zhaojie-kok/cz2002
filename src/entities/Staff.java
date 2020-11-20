package entities;

import java.io.Serializable;

public class Staff extends User{
    /**
     *
     */
    private static final long serialVersionUID = -7309047941954437648L;
    private String staffNo;

    public Staff(String staffNo, String userId, String userType, String name, String gender, String nationality) {
        super(userId, userType, name, gender, nationality);
        this.staffNo = staffNo;
    }

    public String getStaffNo() {
        return this.staffNo;
    }
}
