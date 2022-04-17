package Observers.Events;

public class Event {
    public Events event;

    public Event(Events event1) {
        this.event = event1;
    }

    public Event() {
        this.event = Events.UserEvent;
    }
}
