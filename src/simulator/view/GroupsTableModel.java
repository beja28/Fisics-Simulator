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

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {

		private Controller _ctrl;
		String[] _header = { "Id", "Force Laws", "Bodies" };
		List<BodiesGroup> _groups;
		GroupsTableModel(Controller ctrl) {
			_ctrl = ctrl;
		_groups = new ArrayList<>();
		_ctrl.addObserver(this);
		}
		// TODO el resto de métodos van aquí …
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return _groups.size();
		}
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return _header.length;
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			BodiesGroup g = _groups.get(rowIndex);
			if(columnIndex == 0) {
				return g.getId();
			}
			else if( columnIndex == 1) {
				return g.getForceLawsInfo();
			}
			else if( columnIndex == 2) {
				Iterator<Body> i = g.iterator();
				ArrayList<String> bodies = new ArrayList<String>();
				while(i.hasNext()) {
					bodies.add(i.next().getId());	
				}
				return bodies;
			}
			else {
				return null;
			}
		}
		@Override
		public void onAdvance(Map<String, BodiesGroup> groups, double time) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
			// TODO Auto-generated method stub
			_groups.clear();
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
			_groups.add(g);
			fireTableStructureChanged();
		}
		@Override
		public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
			int i = 0;
			boolean encontrado = false;
			
			while(!encontrado && i<_groups.size()){
				if(_groups.get(i).getId().equalsIgnoreCase(b.getgId())){
					encontrado= true;
				}
				else {
				i ++;
				}
			}
			if(!encontrado) {
			_groups.get(i).addBody(b);
			fireTableStructureChanged();
			}
		}
		@Override
		public void onDeltaTimeChanged(double dt) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onForceLawsChanged(BodiesGroup g) {
			fireTableStructureChanged();
			
		}
		
		@Override
		public String getColumnName(int column) {
			
			return _header[column];
		}
		
}
