package Model;

import java.util.Objects;

/**
 * This class stores the data from a database row in a Java object.
 */
public class Event {
    /**
     * Stores the ID of the event. Cannot be null.
     */
    String eventID;

    /**
     * Stores the username of the user associated with the event. Cannot be null.
     */
    String associatedUsername;

    /**
     * Stores the ID of the person who is associated with the event. Cannot be null.
     */
    String personID;

    /**
     * Stores the latitude of the place where the event took place. Cannot be null.
     */
    float latitude = 0;

    /**
     * Stores the longitude of the place where the event took place. Cannot be null.
     */
    float longitude = 0;

    /**
     * Stores the name of the country where the event took place. Cannot be null.
     */
    String country;

    /**
     * Stores the name of the city where the event took place. Cannot be null.
     */
    String city;

    /**
     * Stores the type of the event. Cannot be null.
     */
    String eventType;

    /**
     * Stores the year when the event occured. Cannot be null.
     */
    int year = 0;

    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Float.compare(event.latitude, latitude) == 0 && Float.compare(event.longitude, longitude) == 0 && year == event.year && eventID.equals(event.eventID) && associatedUsername.equals(event.associatedUsername) && personID.equals(event.personID) && country.equals(event.country) && city.equals(event.city) && eventType.equals(event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
