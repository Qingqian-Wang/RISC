package com.example.demo;

import java.util.ArrayList;

/*
There are several thing need to check
First, The unit user inputs format, can not larger than it has
Second, for the attack part, from the place it has and can only attack others territory.
Third, for the move part, the user can only move units to own place and connected.

This class is the abstract class of all "checkrules", and it uses the next attribute to build a check chain,
when it is called by other, it will check all the rules.
 */
public abstract class BasicChecker {

    // next, point at the next rule checker class.
    private final BasicChecker next;
    public BasicChecker(BasicChecker next) {
        this.next = next;
    }
    public abstract String checkMyRule(int currentFood, Behavior my_behavior, ArrayList <Territory> t);
    public String checkBehavior (int currentFood, Behavior my_behavior, ArrayList <Territory> t) {
        //if we fail our own rule: stop the placement is not legal
        String error_Msg = checkMyRule(currentFood, my_behavior, t);
        if (error_Msg != null) {
            return error_Msg;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkBehavior(currentFood, my_behavior, t);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }

}
