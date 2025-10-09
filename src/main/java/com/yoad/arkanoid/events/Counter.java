package com.yoad.arkanoid.events;

/**
 * The {@code Counter} class is a simple utility to keep track of a numeric count.
 * It allows increasing, decreasing, and retrieving the current value of the count.
 */
public class Counter {
    //---------- Fields ----------

    private int counter;

    //---------- Constructor & Getters/Setters ----------

    public Counter() { this.counter = 0; }

    public void increase(int number) { counter += number; }

    public void decrease(int number) { counter -= number; }

    public int getValue() { return counter; }
}
