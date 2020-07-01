package com.example.wed01.RecyclerView;

public class ArduinoItem {

    public ArduinoItem(String _arduinoID) {
        this.arduinoID = _arduinoID;
    }

    public String getArduinoID() {
        return arduinoID;
    }

    public void setArduinoID(String arduinoID) {
        this.arduinoID = arduinoID;

    }

    private String arduinoID;
}
