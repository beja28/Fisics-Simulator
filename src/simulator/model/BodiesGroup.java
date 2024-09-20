package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body>{

	private String id;
	private ForceLaws fl;
	private List<Body> bodies;
	private List<Body> bodiesRO;
	
	public BodiesGroup(String id, ForceLaws fl) {
		if(id.trim().length() <= 0 || fl == null) {
			throw new IllegalArgumentException();
		}
		else {
			this.id = id;
			this.fl = fl;
			bodies = new ArrayList<>();
			bodiesRO = Collections.unmodifiableList(bodies);
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setForceLaws(ForceLaws fl) {
		if(fl == null) {
			throw new IllegalArgumentException();
		}
		else {
			this.fl = fl;
		}
	}
	
	public void addBody(Body b) {
		if(b == null) {
			throw new IllegalArgumentException();
		}
		else {
			boolean found = false;
			int i = 0;
				while(i < bodies.size() && !found) {
					if(bodies.get(i).getId() == b.getId()) {
						found = true;
						throw new IllegalArgumentException();
					}
					else {
						i++;
					}
				}
				if(!found) {
					bodies.add(b);
				}
		}
	}
	
	public void advance(double dt) {
		if(dt <= 0) {
			throw new IllegalArgumentException();
		}
		else {
			for(Body b : bodies) {
				b.resetForce();
			}
			fl.apply(bodies);
			
			for(Body a : bodies) {
				a.advance(dt);
			}
		}
	}
	
	public JSONObject getState() {
		JSONObject j = new JSONObject();
		JSONArray ja = new JSONArray();
		for(Body b : bodies) {
			ja.put(b.getState());
		}
		j.put("id", this.id);
		j.put("bodies", ja);
		return j;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public String getForceLawsInfo() {
		return fl.toString();
		

	}

	@Override
	public Iterator<Body> iterator() {
		return bodiesRO.iterator();
	}
}
