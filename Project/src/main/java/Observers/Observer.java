package Observers;

import Observers.Events.Event;
import iso.GameObject;

public interface Observer {
    void onNotify(GameObject gameObject, Event event);
}
