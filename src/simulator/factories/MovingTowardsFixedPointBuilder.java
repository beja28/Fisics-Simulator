package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "moving towards fixed point");
	}
	
	@Override
	protected ForceLaws createInstance(JSONObject data) {
		Vector2D c = null;
		double g;
		ForceLaws fl;
		try {
			c = Vector2D.JSONtoVector2D(data.getJSONArray("c"));
			
		}catch(JSONException j) {
			c = new Vector2D();
		}
		try {
			g = data.getDouble("g");
		}
		catch(JSONException j2) {
			g = 9.81;
		}
		
		fl = new MovingTowardsFixedPoint(c,g);
		
		return fl;
	}
	
	@Override
	public JSONObject getInfo() {
		JSONObject info = new JSONObject();
		JSONObject data = new JSONObject();
		//JSON de DATA
		data.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		data.put("g",  "the length of the acceleration vector (a number)");
		
		//JSON INFO
		info.put("type", "mtfp");
		info.put("desc", "Moving towards a fixed point");
		info.put("data", data);
		return info;
	}

}
