package com.example.trafficlightsimulator;

import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.trafficlightsimulator.model.GreenTrafficLightState;
import com.example.trafficlightsimulator.model.RedTrafficLightState;
import com.example.trafficlightsimulator.model.State;
import com.example.trafficlightsimulator.model.TrafficLight;
import com.example.trafficlightsimulator.model.YellowTrafficLightState;

@RestController
@RequestMapping("/sse")
public class LightController {

	static final SseEmitters emitters = new SseEmitters();
	static Timer timer = new Timer();

	static class Task extends TimerTask {
		@Override
		public void run() {

			// create a scheduled task to run at random delay interval between 3-0 seconds
			int delay = gextNextDelayInterval() * 1000;
			timer.schedule(new Task(), delay);

			// update the traffic light state
			tl.changeState();

			// add the light status to SSE emitter endpoint so it status can be retrieved
			emitters.send(getLightStatusMessage(tl));
		}

	}

	private static TrafficLight tl = new TrafficLight();

	LightController() {

	}

	/*
	 * generate a random interval value for the delay task interval
	 */
	static int gextNextDelayInterval() {
		int min = 3;
		int max = 10;

		// Generate random int value from 3 to 10
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		return random_int;
	}

	/*
	 * Create JSON message object for current light state
	 */
	@SuppressWarnings("unchecked")
	private static JSONObject getLightStatusMessage(TrafficLight tl) {

		JSONObject obj = new JSONObject();

		State currentState = tl.getState();
		if (currentState instanceof RedTrafficLightState) {
			obj.put("currentState", SignalColor.RED);
		} else if (currentState instanceof YellowTrafficLightState) {
			obj.put("currentState", SignalColor.YELLOW);
		} else if (currentState instanceof GreenTrafficLightState) {
			obj.put("currentState", SignalColor.GREEN);
		}

		// System.out.print(obj);

		return obj;
	}

	@PostConstruct
	void init() {
		// start task that updates the light state
		new Task().run();
	}

	/*
	 * Server side events endpoint to get the light status.
	 * 
	 * NOTE: Used a quick and dirty fix to deal with CORS issues accessing the UI
	 * endpoint. Better way probably would have been to write a filter to add to all messages
	 */
	@GetMapping(path = "/light-status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	SseEmitter getLightStatus() {
		return emitters.add();
	}

}