package com.chess.pieces;

import sun.plugin.dom.core.CoreConstants;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by jiayu on 2/5/2017.
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getxy() {
        int[] xyCoordi = {this.x, this.y};
        return xyCoordi;
    }

    public int getx() {
        return this.x;
    }

    public int gety() {
        return this.y;
    }

    // if no need to modify, give -2 as param
    public void setxy(int x, int y) {
        if (x != -2) {
            this.x = x;
        }
        if (y != -2) {
            this.y = y;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Coordinate)) return false;

        final Coordinate coordi = (Coordinate)obj;
        if ( (coordi.getx()) == (this.getx()) && (coordi.gety()) == (this.gety())) {
            return true;
        }
        System.out.println("Testing!not equal" + coordi.getx() + " vs " +  this.getx() + "; " + coordi.gety() + " vs. " + this.getx());
      return false;
//        return (  (coordi.getx()) == (this.getx())
//                && (coordi.gety()) == (this.getx()) );
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y);
//        return new Integer(this.x).hashCode() ^ new Integer(this.y).hashCode();
    }
}
