package diku.oopd;

public interface Observer<TypeOfValue>
{
    void update(TypeOfValue value);
}