package com.example.trafficlightsimulator.model;

public class TrafficLight {

    private State red;
    private State yellow;
    private State green;
    

    private State currentState;

    public TrafficLight() {
        
        this.red = new RedTrafficLightState(this);
        this.yellow = new YellowTrafficLightState(this);
        this.green = new GreenTrafficLightState(this);
        
        this.currentState = red;
    }

    public void changeState() {
        this.currentState.handleRequest();
    }

    public String toString() {
        return this.currentState.toString();
    }
    
    public State getGreenLightState() {
        return this.green;
    }

    public State getYellowLightState() {
        return this.yellow;
    }

    public State getRedLightState() {
        return this.red;
    }
    

    public void setState(State state) {
        this.currentState = state;
    }
    
    public State getState() {
    	return this.currentState;
    }
}