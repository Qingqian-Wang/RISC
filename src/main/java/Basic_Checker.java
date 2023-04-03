/*
There are several thing need to check
First, The unit user inputs format, can not larger than it has
Second, for the attack part, from the place it has and can only attack others territory.
Third, for the move part, the user can only move units to own place and connected.
 */
public abstract class Basic_Checker<T> {
    public Basic_Checker(Basic_Checker<T> next) {
        this.next = next;
    }

    private final Basic_Checker<T> next;
}
