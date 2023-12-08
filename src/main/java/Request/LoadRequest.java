package Request;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Container for the request data from the LoadHandler to give to the LoadService.
 */
public class LoadRequest {

    /**
     * Contains all of the users as User objects to be loaded.
     */
    User[] users;
    /**
     * Contains all of the persons as Person objects to be loaded.
     */
    Person[] persons;
    /**
     * Contains all of the events as Event objects to be loaded.
     */
    Event[] events;

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
