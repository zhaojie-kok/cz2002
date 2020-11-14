package entities;

public class Staff extends User{
    private String staffNo;

    Staff(String staffNo, String userId, String userType, String name, String gender, String nationality) {
        super(userId, userType, name, gender, nationality);
        this.staffNo = staffNo;
    }

    public String getStaffNo() {
        return this.staffNo;
    }
}
