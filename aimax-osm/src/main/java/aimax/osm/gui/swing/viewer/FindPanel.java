package aimax.osm.gui.swing.viewer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import aimax.osm.data.EntityFinder;
import aimax.osm.data.MapEvent;
import aimax.osm.data.MapEventListener;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/**
 * Panel to control search for entities by name and attribute.
 * 
 * @author Ruediger Lunde
 */
public class FindPanel extends JPanel implements ActionListener,
		ListSelectionListener, MapEventListener {
	private static final long serialVersionUID = 1L;

	MapViewPane view;
	EntityFinder entityFinder;

	private JComboBox<String> typeCombo;
	private JButton findButton;
	private JButton findMoreButton;
	private JButton clearButton;
	private JTextField findField;
	private JLabel resultLabel;
	private JTable resultTable;
	private FindTableModel tableModel;

	List<MapNode> currMarkers;
	List<MapNode> storedMarkers;

	public FindPanel(MapViewPane view) {
		this.view = view;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;

		JLabel specLabel = new JLabel("Search Specification:");
		// specLabel.setHorizontalTextPosition(JLabel.LEFT);
		c.insets = new Insets(2, 2, 0, 2);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		add(specLabel, c);

		findField = new JTextField();
		findField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Simulate pressed find button.
					ActionEvent ae = new ActionEvent(findButton, 0, "");
					actionPerformed(ae);
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		c.insets = new Insets(2, 2, 2, 2);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		add(findField, c);

		typeCombo = new JComboBox<String>(new String[] { "Entities", "Nodes", "Ways",
				"Address (e.g. 'ulm, pritt')" });
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		add(typeCombo, c);

		findButton = new JButton("Find");
		findButton.setToolTipText("Find Entity by Name or Attribute");
		findButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		add(findButton, c);

		findMoreButton = new JButton("Find More");
		findMoreButton.setToolTipText("Continues Search");
		findMoreButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 3;
		add(findMoreButton, c);

		clearButton = new JButton("Clear");
		clearButton.setToolTipText("Clears Search Results");
		clearButton.addActionListener(this);
		c.gridx = 2;
		c.gridy = 3;
		add(clearButton, c);

		resultLabel = new JLabel("Results:");
		c.insets = new Insets(2, 2, 0, 2);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		add(resultLabel, c);

		tableModel = new FindTableModel();
		resultTable = new JTable(tableModel);
		resultTable.getSelectionModel().addListSelectionListener(this);
		resultTable.setDefaultRenderer(Object.class, new NodeRenderer());
		resultTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = resultTable.getSelectedRow();
					MapEntity entity = (MapEntity) resultTable.getValueAt(row,
							0);
					MapViewPane view = FindPanel.this.view;
					view.showMapEntityInfoDialog(entity, view.isDebugModeEnabled());
				}
			}
		});
		JScrollPane resultScroller = new JScrollPane(resultTable);
		resultScroller.setPreferredSize(new Dimension(10, 10));
		c.insets = new Insets(2, 2, 2, 2);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 3;
		add(resultScroller, c);

		currMarkers = new ArrayList<MapNode>();
		storedMarkers = new ArrayList<MapNode>();
		updateResults(false);
		updateEnabledState();
	}

	public void updateResults(boolean clearStoredMarkers) {
		tableModel.clearResults();
		if (entityFinder != null) {
			if (!entityFinder.getResults().isEmpty()) {
				tableModel.addEntities(entityFinder.getResults());
				resultLabel.setText("Search Results:");

			} else if (!entityFinder.getIntermediateResults().isEmpty()) {
				tableModel.addEntities(entityFinder.getIntermediateResults());
				resultLabel.setText("Select from Intermediate Results:");
			} else {
				resultLabel.setText("No Results:");
			}
		} else {
			resultLabel.setText("Results:");
		}
		clearMarkers(clearStoredMarkers);
	}

	private void clearMarkers(boolean includingStored) {
		for (MapNode marker : currMarkers)
			view.getMap().removeMarker(marker);
		currMarkers.clear();
		if (includingStored) {
			for (MapNode marker : storedMarkers)
				view.getMap().removeMarker(marker);
			storedMarkers.clear();
		}
	}

	public void updateEnabledState() {
		boolean canFindMore = entityFinder != null
		&& (entityFinder.canFindMore() || !entityFinder
				.getIntermediateResults().isEmpty()
				&& getSelectedEntities().size() == 1);
		boolean hasResults = entityFinder != null
				&& (!entityFinder.getResults().isEmpty() || !entityFinder
						.getIntermediateResults().isEmpty());
		findMoreButton.setEnabled(canFindMore);
		clearButton.setEnabled(hasResults);
	}

	private Position getPosition(MapEntity entity) {
		if (entity instanceof MapNode) {
			MapNode node = (MapNode) entity;
			return new Position(node.getLat(), node.getLon());
		} else if (entity instanceof MapWay) {
			MapNode node = ((MapWay) entity).getNodes().get(0);
			return new Position(node.getLat(), node.getLon());
		}
		return null;
	}

	private List<MapEntity> getSelectedEntities() {
		List<MapEntity> result = new ArrayList<MapEntity>();
		int[] selIdxs = resultTable.getSelectedRows();
		for (int i = 0; i < selIdxs.length; i++)
			result.add((MapEntity) tableModel.getValueAt(selIdxs[i], 0));
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findButton) {
			entityFinder = view.getMap().getEntityFinder();
			Position pos = view.getCenterPosition();
			String pattern = findField.getText();
			switch (typeCombo.getSelectedIndex()) {
			case 0:
				entityFinder.findEntity(pattern, pos);
				break;
			case 1:
				entityFinder.findNode(pattern, pos);
				break;
			case 2:
				entityFinder.findWay(pattern, pos, null);
				break;
			case 3:
				entityFinder.findAddress(pattern, pos);
				break;
			}
			storedMarkers.addAll(currMarkers);
			currMarkers.clear();
		} else if (e.getSource() == findMoreButton) {
			if (!entityFinder.getIntermediateResults().isEmpty()) {
				List<MapEntity> entities = getSelectedEntities();
				if (entities.size() == 1)
					entityFinder.selectIntermediateResult(entities.get(0));
			}
			entityFinder.findMore();
		} else if (e.getSource() == clearButton) {
			entityFinder = null;
		}
		updateResults(entityFinder == null);
		updateEnabledState();
	}

	/** Creates markers for selected result items. */
	@Override
	public void valueChanged(ListSelectionEvent event) {
		clearMarkers(false);
		for (MapEntity entity : getSelectedEntities()) {
			Position pos = getPosition(entity);
			if (pos != null) {
				currMarkers.add(view.getMap().addMarker(pos.getLat(),
						pos.getLon()));
				view.adjustToCenter(pos.getLat(), pos.getLon());
			}
		}
		updateEnabledState();
	}

	/** Resets find results after map changes. */
	@Override
	public void eventHappened(MapEvent event) {
		if (event.getType() == MapEvent.Type.MAP_CLEARED
				|| event.getType() == MapEvent.Type.MAP_NEW) {
			entityFinder = null;
			updateResults(true);
		}
	}

	class NodeRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public NodeRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (value instanceof MapNode || value instanceof MapWay) {
				MapEntity entity = (MapEntity) value;
				String text = entity.getName() != null ? entity.getName()
						: "ID=" + entity.getId();
				Position pos = entityFinder.getRefPosition();
				if (pos != null) {
					DecimalFormat f1 = new DecimalFormat("#0.00");
					text += " (" + f1.format(pos.getDistKM(entity)) + " km)";
				}
				setValue(text);
			} else {
				super.setValue(value);
			}
		}

	}

	static class FindTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		List<MapEntity> results = new ArrayList<MapEntity>();
		boolean isIntermediate;

		void clearResults() {
			results.clear();
			fireTableDataChanged();
		}

		void addEntities(List<MapEntity> nodes) {
			results.addAll(nodes);
			fireTableDataChanged();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return results.size();
		}

		@Override
		public String getColumnName(int arg0) {
			return "Entity (dist to ref in km)";
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return results.get(rowIndex);
		}
	}
}
