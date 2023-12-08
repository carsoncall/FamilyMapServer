package Model;

import java.util.Objects;

/**
 * This class stores the data from a database row in a Java object.
 */

public class User {
    /**
     * Stores the username of the user. Cannot be null.
     */
    String username;

    /**
     * Stores the password of the user. Cannot be null.
     */
    String password;

    /**
     * Stores the email of the user. Cannot be null.
     */
    String email;

    /**
     *Stores the first name of the user. Cannot be null.
     */
    String firstName;

    /**
     * Stores the last name of the user. Cannot be null.
     */
    String lastName;

    /**
     * Stores the gender of the user in a string that can only be either "m" or "f". Cannot be null.
     */
    String gender;

    /**
     * Stores the person's ID. Cannot be null.
     */
    String personID;

    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getGender(), user.getGender()) && Objects.equals(getPersonID(), user.getPersonID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getFirstName(), getLastName(), getGender(), getPersonID());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }


}
