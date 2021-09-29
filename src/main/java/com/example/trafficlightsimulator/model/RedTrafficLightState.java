package com.example.trafficlightsimulator.model;

public class RedTrafficLightState extends State {

    private TrafficLight trafficLight;

    public RedTrafficLightState(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public void handleRequest() {
        System.out.println("Turning traffic light to green…");
        trafficLight.setState(trafficLight.getGreenLightState());
    }

    public String toString() {
        return "Traffic light is red.";
    }
}