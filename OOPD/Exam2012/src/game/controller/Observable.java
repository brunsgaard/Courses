package game.controller;

import java.util.LinkedList;

public class Observable<TypeOfValue, TypeOfObserver extends Observer<TypeOfValue>>
{
    private LinkedList<TypeOfObserver> observers;

    public void addObserver(TypeOfObserver observer)
    {
        if (this.observers == null)
            this.observers = new LinkedList<TypeOfObserver>();

        this.observers.add(observer);
    }

    public void notifyObservers(TypeOfValue value)
    {
        if (this.observers == null)
            return;

        for (TypeOfObserver observer : this.observers)
            observer.update(value);
    }
}