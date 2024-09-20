package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver> {

	private double dt;
	private ForceLaws fl;
	private List<String> ids;
	private Map<String, BodiesGroup> map;
	private Map<String, BodiesGroup> mapRO;
	private double actualTime;
	private List<SimulatorObserver> observers; 
	
	public PhysicsSimulator(ForceLaws fl, double dt) {
		if(dt <= 0 || fl == null) {
			throw new IllegalArgumentException();
		}
		else {
			this.dt = dt;
			this.fl = fl;
			ids = new ArrayList<>();
			observers = new ArrayList<>();
			map = new HashMap<String, BodiesGroup>();
			mapRO = Collections.unmodifiableMap(map);
			actualTime = 0.0;
		}
	}
	
	public void advance() {
		for(BodiesGroup bd : map.values()) {
			bd.advance(dt);
		}
		actualTime += dt;
		for(SimulatorObserver o: observers) {
			o.onAdvance(mapRO, actualTime);
		}
	}
	
	public void addGroup(String id) {
		if(map.containsKey(id)) {
			throw new IllegalArgumentException();
		}
		else {
			ids.add(id);
			BodiesGroup group = new BodiesGroup(id, fl); 
			map.put(id, group);
			for(SimulatorObserver o: observers) {
				o.onGroupAdded(mapRO, group);
			}
		}
		
	}
	
	public void addBody(Body b) {
		if(!map.containsKey(b.getgId())) {
			throw new IllegalArgumentException();
		}
		else {
			map.get(b.getgId()).addBody(b);
			for(SimulatorObserver o: observers) {
				o.onBodyAdded(mapRO, b);
			}
		}
	}
	
	public void setForceLaws(String id, ForceLaws fl) {
		 if(!map.containsKey(id)) {
	         throw new IllegalArgumentException();
	     }
	     else {
	         map.get(id).setForceLaws(fl);
	         for(SimulatorObserver o: observers) {
					o.onForceLawsChanged(mapRO.get(id));
				}
	     }
	}
	
	public JSONObject getState() {
		JSONObject j = new JSONObject();
		j.put("time", actualTime);
		JSONArray ja = new JSONArray();
		for(String id : ids) {
			ja.put(map.get(id).getState());
		}
		j.put("groups", ja);
		return j;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public void reset() {
		map.clear();
		ids.clear();
		actualTime =0;
		for(SimulatorObserver o: observers) {
			o.onReset(mapRO, actualTime, dt);
		}
		
	}
	
	public void setDeltaTime(double dt)
	{
		if(dt < 0)
		{
			throw new  IllegalArgumentException();
		}
		else {
			this.dt= dt;
			for(SimulatorObserver o: observers) {
				o.onDeltaTimeChanged(dt);
			}
		}
	}
	
	

	@Override
	public void addObserver(SimulatorObserver o) {

		observers.add(o);
		o.onRegister(mapRO, actualTime, dt);
		
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		
		observers.remove(o);
		
	}
}
