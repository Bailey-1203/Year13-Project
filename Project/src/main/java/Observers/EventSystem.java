package Observers;

import Observers.Events.Event;
import iso.GameObject;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {
    private static final List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer){
        observers.add(observer);
    }

    public static void notify(GameObject gameObject, Event event) {
        for (Observer obs : observers) {
            obs.onNotify(gameObject,event);
        }
    }
}
