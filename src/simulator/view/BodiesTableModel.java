package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private Controller _ctrl;
	
	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;
	BodiesTableModel(Controller ctrl) {
		_ctrl = ctrl;
	_bodies = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _bodies.size();
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return _header.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Body b = _bodies.get(rowIndex);
	switch(columnIndex) {
	case 0: return b.getId();
	case 1:	return b.getgId();
	case 2:	return b.getMass();
	case 3:	return b.getVelocity();
	case 4:	return b.getPosition();
	case 5:	return b.getForce();
	default: return null;
	}
	}
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		
		fireTableDataChanged();
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		
		_bodies.clear();
		fireTableStructureChanged();
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for(BodiesGroup bg : groups.values()) {
			onGroupAdded(groups, bg);
		}
		fireTableStructureChanged();
		
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		
		Iterator<Body> i = g.iterator();
		while(i.hasNext()) {
			_bodies.add(i.next());
		}
		
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		_bodies.add(b);
		fireTableStructureChanged();
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getColumnName(int column) {
		return _header[column];
	}

	

}
