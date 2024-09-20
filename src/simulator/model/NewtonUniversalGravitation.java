package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws{

	private double G;
	
	public NewtonUniversalGravitation(double G) {
		if(G < 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.G = G;
		}
	}
	
	@Override
	public void apply(List<Body> bs) {

		double f = 0.0;
		Vector2D F;
		double distancia = 0.0;
		Vector2D d;
		for(int i= 0; i < bs.size(); i++) {
			for(int j = 0; j < bs.size(); j++) {
				distancia = Math.abs(bs.get(j).getPosition().distanceTo(bs.get(i).getPosition()));
				if(distancia > 0) {
					f = G * ((bs.get(i).getMass()*bs.get(j).getMass())/Math.pow(distancia, 2));
					d = bs.get(j).getPosition().minus(bs.get(i).getPosition());
					d = d.direction();
					F = d.scale(f);
					bs.get(i).addForce(F);
				}
			}
		}
		
	}
	
	public String toString() {
		
		return "Newton’s Universal Gravitation with G=" + G;

	}

}
