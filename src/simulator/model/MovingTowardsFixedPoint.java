package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws{

	private Vector2D c;
	private double g;
	
	public MovingTowardsFixedPoint(Vector2D c, double g) {
		if(c == null || g <= 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.c = c;
			this.g = g;
		}
	}
	
	
	@Override
	public void apply(List<Body> bs) {
		Vector2D d;
		for(Body b : bs) {
			d = c.minus(b.getPosition());
			d = d.direction();
			b.f = d.scale(g * b.m);
		}
	}
	
	public String toString () {
		return "Moving towards "+c+" with constant acceleration "+g;
	}

}
