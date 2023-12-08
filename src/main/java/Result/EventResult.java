package Result;

import Model.Event;

/**
 * A container for the resulting data from EventService to return to EventHandler. Indicates success or failure.
 */
public class EventResult extends EventDataResult {

    /**
     * Username associated with the event.
     */
    String associatedUsername;
    /**
     * Unique ID of the event.
     */
    String eventID;
    /**
     * Unique ID of the person who experienced the event.
     */
    String personID;
    /**
     * Latitude of the location of the event.
     */
    float latitude;
    /**
     * Longitude of the location of the event.
     */
    float longitude;
    /**
     * Name of the country where the event took place.
     */
    String country;
    /**
     * Name of the city where the event took place.
     */
    String city;
    /**
     * The type of event that took place.
     */
    String eventType;
    /**
     * The year when the event took place.
     */
    int year;

    public EventResult(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public EventResult(){};

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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
