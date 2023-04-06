import java.util.ArrayList;

/*
There are several thing need to check
First, The unit user inputs format, can not larger than it has
Second, for the attack part, from the place it has and can only attack others territory.
Third, for the move part, the user can only move units to own place and connected.
 */
public abstract class Basic_Checker{

    private final Basic_Checker next;
    public Basic_Checker(Basic_Checker next) {
        this.next = next;
    }
    protected abstract String checkMyRule(Behavior my_behavior, ArrayList <Territory> t);
    public String checkPlacement (Behavior my_behavior, ArrayList <Territory> t) {
        //if we fail our own rule: stop the placement is not legal
        String error_Msg = checkMyRule(my_behavior, t);
        if (error_Msg != null) {
            return error_Msg;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkPlacement(my_behavior, t);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }
}
