package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton Universal Gravitation");	
	}
	
	@Override
	protected ForceLaws createInstance(JSONObject data) {
		double G;
		ForceLaws fl;
		try {
			G = data.getDouble("G");
		}catch(JSONException j) {
			G = 6.67E-11;
		}
		fl = new NewtonUniversalGravitation(G);
		
		return fl;
	}
	@Override
	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("G", "the gravitational constant (a number)");
		info.put("type", "nlug");
		info.put("desc", "Newton’s law of universal gravitation");
		info.put("data", data);
		return info;
	}

}
