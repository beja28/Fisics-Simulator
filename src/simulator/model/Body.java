package simulator.model;


import org.json.JSONObject;
import simulator.misc.Vector2D;

public abstract class Body {

	protected String id;
	protected String gId;
	protected Vector2D v;
	protected Vector2D f;
	protected Vector2D p;
	protected double m;
	
	public Body(String id, String gId, Vector2D p, Vector2D v, double m) {
		
		if(id == null || id.trim().length() <= 0 || gId == null || gId.trim().length() <= 0 || v == null || p == null || m <= 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.id = id;
			this.gId = gId;
			this.v = v;
			this.p = p;
			this.m = m;
			this.f = new Vector2D();
		}
	
	}
	
	public String getId() {
		return id;
	}
	
	public String getgId() {
		return gId;
	}
	
	public Vector2D getVelocity() {
		return v;
	}
	
	public Vector2D getForce() {
		return f;
	}
	
	public Vector2D getPosition() {
		return p;
	}
	
	public double getMass() {
		return m;
	}
	
	public void addForce(Vector2D f) {
		this.f = this.f.plus(f);
	}
	
	void resetForce() {
		this.f = new Vector2D();
	}
	
	abstract void advance(double dt);
	
	public JSONObject getState() {
		JSONObject j = new JSONObject();
		j.put("id", id);
		j.put("m", m);
		j.put("p", p.asJSONArray());
		j.put("v", v.asJSONArray());
		j.put("f", f.asJSONArray());
		return j;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	
	
	
}
