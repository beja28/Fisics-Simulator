package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver {
private JFrame _mainWindow;
private Controller _ctrl;
private JToolBar _toolaBar;
private JFileChooser _fc;
private boolean _stopped = true; // utilizado en los botones de run/stop
private JButton _quitButton;
// TODO añade más atributos aquí …
private JButton _fileButton;
private JButton _lawButton;
private JButton _viewerButton;
private JButton _playButton;
private JButton _stopButton;
private JSpinner _stepsSpinner;
private JTextField _dtT;
private JLabel _dtL;
private JLabel _stepsL;
private ForceLawsDialog flD;





ControlPanel(Controller ctrl, JFrame mainWindow) {
_ctrl = ctrl;
_mainWindow = mainWindow;
initGUI();
_ctrl.addObserver(this);
}
private void initGUI() {
setLayout(new BorderLayout());
_toolaBar = new JToolBar();
add(_toolaBar, BorderLayout.PAGE_START);
// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
// _toolaBar.addSeparator() para añadir la línea de separación vertical
// entre las componentes que lo necesiten


//BUTTONS
// QUIT BUTTON
_toolaBar.addSeparator();
_quitButton = new JButton();
_quitButton.setToolTipText("Quit");
_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
_quitButton.addActionListener((e) -> Utils.quit(this));

// FILEBUTTON
_fc = new JFileChooser();
_fileButton = new JButton();
_fileButton.setToolTipText("Load an input file into the simulator");
_fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
_fileButton.addActionListener((e) -> {
if(_fc.showOpenDialog(Utils.getWindow(this)) == JFileChooser.APPROVE_OPTION) {
	_ctrl.reset();
	File file = _fc.getSelectedFile();
	InputStream input = null;
	try {
		input = new FileInputStream(file);
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	_ctrl.loadData(input);
}
});
_toolaBar.add(_fileButton);

//LAW BUTTON
_toolaBar.addSeparator();
_lawButton = new JButton();
_lawButton.setToolTipText("Select force laws for groups");
_lawButton.setIcon(new ImageIcon("resources/icons/physics.png"));
_lawButton.addActionListener((e) -> {
	if(flD == null) {
	flD = new ForceLawsDialog(_mainWindow, _ctrl); //NO SABEMOS QUE JFRAME METERLE
	}
	flD.open();
});
_toolaBar.add(_lawButton);

//VIWER BUTTON
_viewerButton = new JButton();
_viewerButton.setToolTipText("Open viewer window");
_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
_viewerButton.addActionListener((e) -> {
	ViewerWindow viewe = new ViewerWindow(_mainWindow, _ctrl); //NO SABEMOS QUE JFRAME METER
});
_toolaBar.add(_viewerButton);

//PLAY BUTTON
_toolaBar.addSeparator();
_playButton = new JButton();
_playButton.setToolTipText("Run the simulator");
_playButton.setIcon(new ImageIcon("resources/icons/run.png"));
_playButton.addActionListener((e) -> {
	//_quitButton.setEnabled(false);
	_playButton.setEnabled(false);
	_fileButton.setEnabled(false);
	_lawButton.setEnabled(false);
	_viewerButton.setEnabled(false);
	_stopped = false;
	
		Double deltaTime = Double.parseDouble(_dtT.getText());
	
	_ctrl.setDeltaTime(deltaTime);
	int steps = (Integer)_stepsSpinner.getValue();
	run_sim(steps);
});
_toolaBar.add(_playButton);

//STOP BUTTON
_stopButton = new JButton();
_stopButton.setToolTipText("Stop the simulator");
_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
_stopButton.addActionListener((e) -> {
	_stopped = true;
});
_toolaBar.add(_stopButton);
_toolaBar.addSeparator();

//STEPSlABEL
_stepsL = new JLabel("Steps");
_toolaBar.add(_stepsL);


//STEPSSPINNER
_stepsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20000, 100));
_stepsSpinner.setToolTipText("Simulator steps to run");
Dimension d = new Dimension(100, 30);
_stepsSpinner.setPreferredSize(d);
_stepsSpinner.setMaximumSize(d);
_toolaBar.add(_stepsSpinner);

//DELTA TIME LABEL
_dtL = new JLabel("Delta-Time");
_dtL.setToolTipText("Real time (seconds) corresponding to a step");
_toolaBar.add(_dtL);

//TEXTFIELD DELTA-TIME
_dtT = new JTextField();
_dtT.setPreferredSize(d);
_dtT.setMaximumSize(d);
_toolaBar.add(_dtT);
_toolaBar.add(Box.createGlue()); // this aligns the button to the right
_toolaBar.add(_quitButton);


}
@Override
public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	// TODO Auto-generated method stub
	
}
@Override
public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
	// TODO Auto-generated method stub
	
}
@Override
public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
	_dtT.setText("0.0");
	
}
@Override
public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
	// TODO Auto-generated method stub
	
}
@Override
public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	// TODO Auto-generated method stub
	
}
@Override
public void onDeltaTimeChanged(double dt) {
	_dtT.setText(""+dt);
}
@Override
public void onForceLawsChanged(BodiesGroup g) {
	// TODO Auto-generated method stub
	
}
// TODO el resto de métodos van aquí…

private void run_sim(int n) {
	if (n > 0 && !_stopped) {
		try {
			_ctrl.run(1);
		} catch (Exception e) {
			Utils.showErrorMsg(e.getMessage());
			_fileButton.setEnabled(true);
			_lawButton.setEnabled(true);
			_viewerButton.setEnabled(true);
			_playButton.setEnabled(true);
			_stopped = true;
			return;
		}
		SwingUtilities.invokeLater(() -> run_sim(n - 1));
	} else {
		//Utils.showErrorMsg("No es posible correr el programa");
		_playButton.setEnabled(true);
		_fileButton.setEnabled(true);
		_lawButton.setEnabled(true);
		_viewerButton.setEnabled(true);
		_stopped = true;

	}


}

}

