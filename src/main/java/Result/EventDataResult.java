package Result;

import Model.Event;

/**
 * This is a class that is used to save an array of events. This is also the type that the Event service returns,
 * because the EventResult class contains fields that cannot be null and thus, in the case where the user requests an
 * event, the GSON cannot omit those fields. The EventResult is the child of this class.
 */
public class EventDataResult extends Result {

    /**
     * An array for storing an array of Event objects
     */
    Event[] data;

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
