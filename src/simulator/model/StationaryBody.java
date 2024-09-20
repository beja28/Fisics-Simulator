package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body{

	public StationaryBody(String id, String gId, Vector2D p, double m) {
		super(id, gId, p, new Vector2D(), m);
	}

	@Override
	void advance(double dt) {
		
	}

}
