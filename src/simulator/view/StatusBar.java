package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;



class StatusBar extends JPanel implements SimulatorObserver {
	private Controller _ctrl;
	private double actualTime;
	private int nGroups = 0;
	JLabel timeL;
	JLabel groupsL;
	JLabel timeT;
	JLabel groupsT;
	// TODO Añadir los atributos necesarios, si hace falta …
	StatusBar(Controller ctrl) {
	_ctrl = ctrl;
	initGUI();
	// TODO registrar this como observador
	_ctrl.addObserver(this);
	}
	private void initGUI() {
	this.setLayout(new FlowLayout(FlowLayout.LEFT));
	this.setBorder(BorderFactory.createBevelBorder(1));
	// TODO Crear una etiqueta de tiempo y añadirla al panel
	timeL = new JLabel("Time: ");
	timeT = new JLabel();
	add(timeL);
	add(timeT);
	// TODO Crear la etiqueta de número de grupos y añadirla al panel
	groupsL = new JLabel("Groups: ");
	groupsT = new JLabel();
	add(groupsL);
	add(groupsT);
	// TODO Utilizar el siguiente código para añadir un separador vertical
	JSeparator s = new JSeparator(JSeparator.VERTICAL);
	s.setPreferredSize(new Dimension(10, 20));
	this.add(s);
	}
	// TODO el resto de métodos van aquí…
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		actualTime = time;
		timeT.setText(""+actualTime);
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		actualTime = time;
		nGroups = 0;
		timeT.setText(""+actualTime);
		
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		actualTime = time;
		nGroups = groups.size();
		timeT.setText(""+actualTime);
		groupsT.setText(""+nGroups);
		
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		nGroups ++;
		groupsT.setText(""+nGroups);
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}
	}

