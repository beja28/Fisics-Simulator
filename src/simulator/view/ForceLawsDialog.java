package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private int _status;
	private int _selectedLawsIndex;
	// TODO en caso de ser necesario, añadir los atributos aquí…
	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observer;
		_ctrl.addObserver(this);
	}
	private void initGUI() {
		
		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setPreferredSize(new Dimension(1000,600));
		setContentPane(mainPanel);
		
		String texto = "<html>Select a force law and provide values for the parameters in the Value column (default values are used for <br> parameters with no value)</html>";
		JLabel lb = new JLabel(texto);
		JPanel panelTexto = new JPanel();
		panelTexto.setLayout(new GridLayout());
		lb.setPreferredSize(new Dimension(mainPanel.getWidth(), 50));
		panelTexto.add(lb);
		mainPanel.add(panelTexto);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel etiquetaF = new JLabel("Force Law: ");	
		JLabel etiquetaG = new JLabel("Group: ");
		panel.add(etiquetaF);
		
		
		// _forceLawsInfo se usará para establecer la información en la tabla
		_forceLawsInfo = _ctrl.getForceLawsInfo();
		
		// TODO crear un JTable que use _dataTableModel, y añadirla al panel
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column == 1) {
					return true;
				}
				return false;
				// TODO hacer editable solo la columna 1
			}
			
			@Override
			public String getColumnName(int column) {
				return _headers[column];
			}
			
			@Override
			public int getColumnCount() {
				return _headers.length;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		JTable tabla = new JTable(_dataTableModel) {
		
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int renderWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(renderWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		
		tabla.setPreferredSize(new Dimension(mainPanel.getWidth(), 400));
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		
		TableColumnModel colModel = tabla.getColumnModel();
		colModel.getColumn(1).setPreferredWidth(25);
		colModel.getColumn(2).setPreferredWidth(400);
		mainPanel.add(new JScrollPane(tabla));
		
		
		_lawsModel = new DefaultComboBoxModel<>();
		// TODO añadir la descripción de todas las leyes de fuerza a _lawsModel
		for(JSONObject law : _forceLawsInfo) {
			_lawsModel.addElement(law.getString("desc"));
		}
		// TODO crear un combobox que use _lawsModel y añadirlo al panel
		JComboBox<String> lawSelection = new JComboBox<String>(_lawsModel);
		lawSelection.addActionListener((e) -> {
			_selectedLawsIndex = lawSelection.getSelectedIndex();
			JSONObject info = _forceLawsInfo.get(_selectedLawsIndex);
			JSONObject data = info.getJSONObject("data");
			_dataTableModel.setRowCount(data.length());
			int i = 0;
			for(String key : data.keySet()) {
				_dataTableModel.setValueAt(key, i, 0);
				_dataTableModel.setValueAt(data.get(key), i, 2);
				i++;
			}
				
		});
		
		panel.add(lawSelection);
		panel.add(etiquetaG);
		_groupsModel = new DefaultComboBoxModel<>();
		// TODO crear un combobox que use _groupsModel y añadirlo al panel
		JComboBox<String> groupSelection = new JComboBox<String>(_groupsModel);
		panel.add(groupSelection);
		mainPanel.add(panel);
		// TODO crear los botones OK y Cancel y añadirlos al panel
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener((e) -> {
			_status = 0;
			setVisible(false);
		});
		
		ok.addActionListener((e) -> {
			JSONObject infoRow = new JSONObject();
			try {
				for(int i = 0; i < _dataTableModel.getRowCount(); i++) {
					Object key = _dataTableModel.getValueAt(i, 0);
					Object value = _dataTableModel.getValueAt(i, 1);
					if(key.equals("c") && _forceLawsInfo.get(_selectedLawsIndex).getString("type").equalsIgnoreCase("mtfp")) {
                        value = new JSONArray((String) value);
                    }
					infoRow.put((String) key, value);
				}
				JSONObject dataInfo = new JSONObject();
				dataInfo.put("data", infoRow);
				dataInfo.put("type", _forceLawsInfo.get(_selectedLawsIndex).getString("type"));
				_ctrl.setForcesLaws(groupSelection.getSelectedItem().toString(), dataInfo);
				_status = 1;
				setVisible(false);
			}catch(Exception e1) {
				Utils.showErrorMsg(e1.getMessage());
			}
		});
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout());
		panelBotones.add(ok);
		panelBotones.add(cancel);
		mainPanel.add(panelBotones);
		
		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}
	public void open() {
		if (_groupsModel.getSize() == 0) {
			_status = 1;
		}
		else {
			// TODO Establecer la posición de la ventana de diálogo de tal manera que se
			// abra en el centro de la ventana principal
			setLocationRelativeTo(null);
			pack();
			setVisible(true);
			_status = 0;
		}
	}
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_groupsModel.removeAllElements();
	}
	
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for(BodiesGroup g: groups.values()) {
			_groupsModel.addElement(g.getId());
		}
		_selectedLawsIndex = 0;
		JSONObject info = _forceLawsInfo.get(_selectedLawsIndex);
		JSONObject data = info.getJSONObject("data");
		_dataTableModel.setRowCount(data.length());
		int i = 0;
		for(String key : data.keySet()) {
			_dataTableModel.setValueAt(key, i, 0);
			_dataTableModel.setValueAt(data.get(key), i, 2);
			i++;
		}
		
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groupsModel.addElement(g.getId());
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