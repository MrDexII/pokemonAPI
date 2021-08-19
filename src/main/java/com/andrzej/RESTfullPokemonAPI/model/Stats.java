package com.andrzej.RESTfullPokemonAPI.model;

import org.bson.types.ObjectId;

import java.util.Objects;

public class Stats {

    private int base;
    private int min;
    private int max;

    public Stats() {
    }

    public Stats(int base, int min, int max) {
        this.base = base;
        this.min = min;
        this.max = max;
    }

    public Stats(ObjectId _id, int base, int min, int max) {
        this.base = base;
        this.min = min;
        this.max = max;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "base=" + base +
                ", min=" + min +
                ", max=" + max +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return base == stats.base && min == stats.min && max == stats.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, min, max);
    }
}
