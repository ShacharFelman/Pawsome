package com.example.pawsome.model;

import java.util.Locale;
import java.util.Objects;

public class WalkType {

    private long time;
    private int durationInMinutes;
    private boolean poop;
    private boolean pee;
    private double rate;
    private String name;

    public WalkType() {
    }

    public long getTime() {
        return time;
    }

    public WalkType setTime(long time) {
        this.time = time;
        return this;
    }

    public String getName() {
        return name;
    }

    public WalkType setName(String name) {
        this.name = name;
        return this;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public WalkType setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public boolean isPoop() {
        return poop;
    }

    public WalkType setPoop(boolean poop) {
        this.poop = poop;
        return this;
    }

    public boolean isPee() {
        return pee;
    }

    public WalkType setPee(boolean pee) {
        this.pee = pee;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public WalkType setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public WalkType setDuration(int minutes, int hours) {
        this.durationInMinutes = minutes + hours * 60;
        return this;
    }

    public String getDurationAsString() {
        int hours = this.durationInMinutes / 60;
        int minutes = this.durationInMinutes % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalkType walkType = (WalkType) o;
        return Objects.equals(name, walkType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}