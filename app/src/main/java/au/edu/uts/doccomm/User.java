package au.edu.uts.doccomm;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String gender;
    private String password;
    private String dateOfBirth;
    private String phoneNumber;
    private String weight;
    private String height;
    private String medicalCondition;
    private String userType;

    public User() {

    }

    public User(String userId, String firstName, String lastName, String emailAddress, String password, String gender, String phoneNumber,String dateOfBirth, String weight, String height, String medicalCondition, String userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.gender = gender;

        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.height = height;
        this.medicalCondition = medicalCondition;
        this.userType = userType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }
}
