package aima.core.environment.cellworld.next;

public class CellWorldFactory {

	/**
	 * Create the cell world as defined in Figure 17.1 in AIMA3e.
	 * @return
	 */
	public static CellWorld<Double> createCellWorldForFig17_1() {
		CellWorld<Double> cw = new CellWorld<Double>(4, 3, -0.04);
		
		cw.removeCell(2, 2);
		
		cw.getCellAt(4, 3).setContent(1.0);
		cw.getCellAt(4, 2).setContent(-1.0);
		
		return cw;
	}
}
