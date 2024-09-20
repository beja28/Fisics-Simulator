package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;


public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "stationary body");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		String id, gId;
		Vector2D p, v;
		double m;
		Body b = null;
		id = data.getString("id");
		gId = data.getString("gid");
		p = Vector2D.JSONtoVector2D(data.getJSONArray("p"));
		m = data.getDouble("m");
		
		b = new StationaryBody(id, gId, p, m);
		
		return b;
	}

}
