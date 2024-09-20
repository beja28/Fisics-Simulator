package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{
	
	public NoForceBuilder() {
		super("nf", "no force");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		ForceLaws fl = new NoForce();
		return fl;
	}
	
	@Override
	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		JSONObject data = new JSONObject();
		info.put("type", "nf");
		info.put("desc", "No Force");
		info.put("data", data);
		return info;
	}
	

}
