package main.java.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class User {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private int userStatus;

    public User() {
    }

    public User(long id, String username, String firstName, String lastName, String email, String password, String phone, int userStatus) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userStatus = userStatus;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public long getUserStatus() {
        return userStatus;
    }

    @Override
    public String toString() {
        return "User: \n" +
                "id =" + this.id + "\n" +
                "Username =" + this.username + "\n" +
                "FirstName =" + this.firstName + "\n" +
                "LastName =" + this.lastName + "\n" +
                "Email = " + this.email + "\n" +
                "Password = " + this.password + "\n" +
                "Phone = " + this.phone + "\n" +
                "User status = " + this.userStatus + "\n";
    }
}
