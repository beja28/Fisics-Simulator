package simulator.factories;


import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {
		super("mv_body", "moving body");
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
		v = Vector2D.JSONtoVector2D(data.getJSONArray("v"));
		m = data.getDouble("m");
		b = new MovingBody(id, gId, p, v, m);
		
		return b;
	}

}
