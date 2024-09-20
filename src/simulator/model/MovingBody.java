package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body{

	private Vector2D a;
	
	public MovingBody(String id, String gId, Vector2D p, Vector2D v, double m) {
		super(id, gId, p, v, m);
	}

	@Override
	void advance(double dt) {
		if(this.m == 0) {
			a = new Vector2D();
		}
		else {
			double x, y;
			Vector2D vAux, aAux;
			//Cálculo de la aceleración
			x = f.getX()/m;
			y = f.getY()/m;
			a = new Vector2D(x,y);
			
			
			//Cálculo de la nueva posición p = p + vt +1/2*a*t^2
			vAux = v.scale(dt);
			aAux = a.scale(0.5);
			aAux= aAux.scale(Math.pow(dt, 2));
			p = p.plus(vAux);
			p = p.plus(aAux);
			
			//Cálculo de la nueva velocidad
			aAux = a.scale(dt);
			v = v.plus(aAux);
		
		}
	}

}
