package com.yoad.arkanoid.notifiers;

/**
 * The {@code Counter} class is a simple utility to keep track of a numeric count. It allows
 * increasing, decreasing, and retrieving the current value of the count.
 */
public class Counter {
  // Fields
  private int counter;

  /** Constructs a new Counter initialized to zero. */
  public Counter() {
    this.counter = 0;
  }

  /**
   * Adds the specified number to the current count.
   *
   * @param number the number to add.
   */
  public void increase(int number) {
    counter += number;
  }

  /**
   * Subtracts the specified number from the current count.
   *
   * @param number the number to subtract.
   */
  public void decrease(int number) {
    counter -= number;
  }

  /**
   * Returns the current count value.
   *
   * @return the current count.
   */
  public int getValue() {
    return counter;
  }
}
