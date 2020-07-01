package com.example.wed01.RecyclerView;

public class GraphItem {

    public GraphItem(float humidity, String time) {
        this.humidity = humidity;
        this.time = time;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private float humidity;
    private String time;
}
