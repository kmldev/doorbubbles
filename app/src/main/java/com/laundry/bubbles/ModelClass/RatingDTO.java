package com.laundry.bubbles.ModelClass;

import java.io.Serializable;

public class RatingDTO implements Serializable {
    String average="";

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
