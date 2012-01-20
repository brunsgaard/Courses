package game.controller;
/**
 * 
 * @author DIKU OOPD instructors
 *
 * @param <TypeOfValue>
 */
public interface Observer<TypeOfValue>
{
    void update(TypeOfValue value);
}