package com.chess.Utility;

import java.util.Objects;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Coordinate {
    // values that consists of the coordinate (x, y) to indicate position (rowIdx, colIdx)
    private int x;
    private int y;

    /***
     * Constructor that sets the position for the coordinate
     * @param x x coordinate of the position to set
     * @param y y coordinate of the position to set
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /***
     * Get the x-coordinate of the position
     * @return x-coordinate value of the position
     */
    public int getx() {
        return this.x;
    }

    /***
     * Get the y-coordinate of the position
     * @return y-coordinate value of the position
     */
    public int gety() {
        return this.y;
    }

    // if no need to modify, give -2 as param

    /***
     * Modify the values of the coordinates, set value to be -2 if no need to modify
     * @param x x coordinate of the position to set
     * @param y y coordinate of the position to set
     */
    public void setxy(int x, int y) {
        if (x != -2) {
            this.x = x;
        }
        if (y != -2) {
            this.y = y;
        }
    }

    /***
     * Override the default equal,
     * instead of determining whether the two objects to compare against is the same coordinate,
     * compare if the two objs are coordinates with the same values
     * @param obj obj to compare with 'this'
     * @return boolean to determine whether the obj is Coordinate that has the same value with 'this'
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Coordinate)) return false;

        final Coordinate coordi = (Coordinate)obj;
        return (coordi.getx()) == (this.getx()) && (coordi.gety()) == (this.gety());
    }

    /***
     * Required to modify the  behavior of the method 'equal' for Coordinate
     * @return hashcode of the obj
     */
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
