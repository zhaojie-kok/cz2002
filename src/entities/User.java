package entities;

import java.io.Serializable;

public class User implements Serializable{
    protected String userId;
    protected String userType;
    protected String name;
    protected String gender;
    protected String nationality;

    User(String userId, String userType, String name, String gender, String nationality) {
        // TODO: throw exception if any of these fields are null or invalid
        this.userId = userId;
        this.userType = userType;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserType() {
        return this.userType;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getNationality() {
        return this.nationality;
    }
}
