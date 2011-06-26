package aima.core.util.datastructure;

import java.util.Hashtable;
import java.util.List;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class Table<RowHeaderType, ColumnHeaderType, ValueType> {
	private List<RowHeaderType> rowHeaders;
	private List<ColumnHeaderType> columnHeaders;
	private Hashtable<RowHeaderType, Hashtable<ColumnHeaderType, ValueType>> rows;

	/**
	 * Constructs a Table with the specified row and column headers.
	 * 
	 * @param rowHeaders
	 *            a list of row headers
	 * @param columnHeaders
	 *            a list of column headers
	 */
	public Table(List<RowHeaderType> rowHeaders,
			List<ColumnHeaderType> columnHeaders) {

		this.rowHeaders = rowHeaders;
		this.columnHeaders = columnHeaders;
		this.rows = new Hashtable<RowHeaderType, Hashtable<ColumnHeaderType, ValueType>>();
		for (RowHeaderType rowHeader : rowHeaders) {
			rows.put(rowHeader, new Hashtable<ColumnHeaderType, ValueType>());
		}
	}

	/**
	 * Maps the specified row and column to the specified value in the table.
	 * Neither the row nor the column nor the value can be <code>null</code> <br>
	 * The value can be retrieved by calling the <code>get</code> method with a
	 * row and column that is equal to the original row and column.
	 * 
	 * @param r
	 *            the table row
	 * @param c
	 *            the table column
	 * @param v
	 *            the value
	 * 
	 * @throws NullPointerException
	 *             if the row, column, or value is <code>null</code>.
	 */
	public void set(RowHeaderType r, ColumnHeaderType c, ValueType v) {
		rows.get(r).put(c, v);
	}

	/**
	 * Returns the value to which the specified row and column is mapped in this
	 * table.
	 * 
	 * @param r
	 *            a row in the table
	 * @param c
	 *            a column in the table
	 * 
	 * @return the value to which the row and column is mapped in this table;
	 *         <code>null</code> if the row and column is not mapped to any
	 *         values in this table.
	 * 
	 * @throws NullPointerException
	 *             if the row or column is <code>null</code>.
	 */
	public ValueType get(RowHeaderType r, ColumnHeaderType c) {
		Hashtable<ColumnHeaderType, ValueType> rowValues = rows.get(r);
		return rowValues == null ? null : rowValues.get(c);

	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (RowHeaderType r : rowHeaders) {
			for (ColumnHeaderType c : columnHeaders) {
				buf.append(get(r, c).toString());
				buf.append(" ");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	class Row<R> {
		private Hashtable<ColumnHeaderType, ValueType> cells;

		public Row() {

			this.cells = new Hashtable<ColumnHeaderType, ValueType>();
		}

		public Hashtable<ColumnHeaderType, ValueType> cells() {
			return this.cells;
		}

	}

	class Cell<ValueHeaderType> {
		private ValueHeaderType value;

		public Cell() {
			value = null;
		}

		public Cell(ValueHeaderType value) {
			this.value = value;
		}

		public void set(ValueHeaderType value) {
			this.value = value;
		}

		public ValueHeaderType value() {
			return value;
		}

	}
}
