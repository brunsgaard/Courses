package game.controller;

public interface Observer<TypeOfValue>
{
    void update(TypeOfValue value);
}