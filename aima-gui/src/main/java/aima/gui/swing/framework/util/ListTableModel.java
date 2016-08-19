package aima.gui.swing.framework.util;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This class extends the {@link AbstractTableModel} for a one dimensional {@link ArrayList} of strings.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
public final class ListTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;

	private final String columnName;
	
	private ArrayList<String> data = new ArrayList<String>();
	
	/**
	 * @param columnName the name of the column in the table.
	 */
	public ListTableModel(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public int getColumnCount() { 
		return 1;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}
	
	@Override
	public String getValueAt(int row, int col) {
		return data.get(row);
	}
	
	@Override
	public String getColumnName(int column) {
		return columnName;
		
	}
	
	/**
	 * Adds a string to the table.
	 * @param string the string to be added.
	 */
	public void add(String string) {
		data.add(string);
		this.fireTableRowsInserted(data.size()-1, data.size()-1);
	}
	
	/**
	 * Removes a value from the table at the given index.
	 * @param index the index of the element to be removed.
	 */
	public void removeValueAt(int index) {
		data.remove(index);
	}
	
	/**
	 * Adds a value to the table at a given index.
	 * @param index the index where the element is to be put in.
	 * @param element the element to add to the table.
	 */
	public void setValueAt(int index, String element) {
		data.set(index, element);
	}
	
	/**
	 * Clears the table completely.
	 */
	public void clear() {
		final int lastRow = data.size() - 1;
		data.clear();
		if (lastRow >= 0) this.fireTableRowsDeleted(0,lastRow);
	}
}
