package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	private PhysicsSimulator simulator;
	private /*static*/ Factory<Body> fb;
	private /*static*/ Factory<ForceLaws> ff;
	
	public Controller(PhysicsSimulator simulator, Factory<Body> fb, Factory<ForceLaws>	ff) {
		this.simulator = simulator;
		this.fb = fb;
		this.ff = ff;
	}
	
	public void loadData(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray bodiesGroup = jsonInput.getJSONArray("groups");
		for(int i = 0; i < bodiesGroup.length(); i++) {
			simulator.addGroup(bodiesGroup.getString(i));
		}
		try {
			JSONArray laws = jsonInput.getJSONArray("laws");
			JSONObject li;
			for(int i = 0; i < laws.length(); i++) {
				li = laws.getJSONObject(i);
				simulator.setForceLaws(li.getString("id"), ff.createInstance(li.getJSONObject("laws")));
			}
		}catch(JSONException e) {
			
		}
		
		JSONArray bodies = jsonInput.getJSONArray("bodies");
		JSONObject bi;
		for(int i = 0; i < bodies.length(); i++) {
			bi = bodies.getJSONObject(i);
			simulator.addBody(fb.createInstance(bi));
		}
	}
	
	public void run(int n, OutputStream out) {
		PrintStream p = null;
		if(out == null) {
			p = new PrintStream(System.out);
		}
		else {
			p = new PrintStream(out);
		}
		p.println("{");
		p.println("\"states\": [");
		for(int i = 0; i <= n ; i++) {
			p.println(simulator.getState());
			if(i<n) {
				p.println(",");
			}
			simulator.advance();
		}
		p.println("]");
		p.println("}");
		
		
	}
	//Actualizacion V2
	public void reset() {
		simulator.reset();
	}
	
	public void setDeltaTime(double dt) {
		simulator.setDeltaTime(dt);
	}
	public void addObserver(SimulatorObserver o) {
		simulator.addObserver(o);
	}
	public void removeObserver(SimulatorObserver o) {
		simulator.removeObserver(o);
	}
	 public List<JSONObject> getForceLawsInfo(){
		return ff.getInfo();
	 }
	 public void setForcesLaws(String gId, JSONObject info) {
		 ForceLaws law = ff.createInstance(info);
		 simulator.setForceLaws(gId, law);
	 }
	 public void run(int n) {
		 for(int i=0; i<n ; i++) {
			 simulator.advance();
		 }
	 }
	
	
}
