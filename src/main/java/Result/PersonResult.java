package Result;

import Model.Person;

/**
 * A container for the resulting data from PersonService to return to PersonHandler. Indicates success or failure.
 */
public class PersonResult extends Result {
    /**
     * the username associated with the person.
     */
    String associatedUsername;
    /**
     * The unique ID of the person.
     */
    String personID;
    /**
     * The first name of the person.
     */
    String firstName;
    /**
     * The last name of the person.
     */
    String lastName;
    /**
     * The gender of the person. Either 'm' or 'f'.
     */
    String gender;
    /**
     * The personID of the father of the person, if they exist.
     */
    String fatherID;
    /**
     * The personID of the mother of the person, if they exist.
     */
    String motherID;
    /**
     * The personID of the spouse of the person, if they exist.
     */
    String spouseID;

    /**
     * In the case of a multiple person result, this will hold the data.
     */
    Person[] data;

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public PersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public PersonResult() {};

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
