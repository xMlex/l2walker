package fw.gui.bot;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class L2GuiJTable extends JTable {

	protected static final long serialVersionUID = 1L;
	
	protected DefaultTableModel _tableModel;
	
	public L2GuiJTable() {
		super();
		//setRowHeight(32);
		//fixWidth(0, 32);
	}
	
	public Class<? extends Object> getColumnClass(int column)
    {
        return getValueAt(0, column).getClass();
    }

	protected void fixWidth(final int columnIndex, final int width) {
	    TableColumn column = getColumnModel().getColumn(columnIndex);
	    column.setMinWidth(width);
	    column.setMaxWidth(width);
	    column.setPreferredWidth(width);
	}
}
