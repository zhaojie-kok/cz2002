package entities;

import java.io.Serializable;

import exceptions.MissingParametersException;

/**
 * General purpose entity class for all users
 */
public class User implements Serializable{
    private static final long serialVersionUID = 8107694123020271867L;
    protected String userId;
    protected String userType;
    protected String name;
    protected String gender;
    protected String nationality;
    protected String email;

    /**
     * Constructor
     * 
     * @param userId      userID of the user
     * @param userType    Type of user
     * @param name        Name of user
     * @param gender      Gender of user
     * @param nationality Nationality of user
     * @param email       email address of user
     * @throws MissingParametersException thrown if any arguments are missing
     */
    User(String userId, String userType, String name, String gender, String nationality, String email)
            throws MissingParametersException {
        if (userId == null) {
            throw new MissingParametersException("UserID cannot be blank");
        }
        if (userType == null) {
            throw new MissingParametersException("User Type cannot be blank");
        }
        if (name == null) {
            throw new MissingParametersException("User's name cannot be blank");
        }
        if (gender == null) {
            throw new MissingParametersException("User's gender cannot be blank");
        }
        if (nationality == null) {
            throw new MissingParametersException("User's nationality cannot be blank");
        }
        if (email == null) {
            throw new MissingParametersException("User's email cannot be blank");
        }
        this.userId = userId;
        this.userType = userType;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
        this.email = email;
    }

    /**
     * Getter for userID
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Getter for user tye
     */
    public String getUserType() {
        return this.userType;
    }

    /**
     * Getter for user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for user's gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Getter for user's nationality
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * Getter for user's email address
     */
    public String getEmail() {
        return this.email;
    }
}
