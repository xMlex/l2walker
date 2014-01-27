package fw.gui.bot;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fw.game.model.L2Item;
import fw.game.model.L2PlayerInventory;

public class GuiInventory extends JTable {
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = {"Icon", "Name", "Count","Eqip"};
	private Object[][] data = {};
	private DefaultTableModel tableInvModel;
	private long _lastUpdate = System.currentTimeMillis();
	
	public GuiInventory() {
		super();
		tableInvModel = new DefaultTableModel(data,columnNames);
		setModel(tableInvModel);
		fixWidth(0, 32);
		setRowHeight(32);
	}	
	
	public synchronized void updateInventory(L2PlayerInventory list){
		
		if(_lastUpdate == list.getLastUpdate()) return;
		_lastUpdate =list.getLastUpdate();
		
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.setNumRows(0);	
		for(L2Item _el:list.getObjectList())
			model.addRow(new Object[]{awtBotFrame.getGameIcon(_el.getIcon()), "("+_el.getId()+")"+_el.getName(), _el.getCount(),_el.isEquipped()});		
	}
	
	public Class<? extends Object> getColumnClass(int column)
    {
        return getValueAt(0, column).getClass();
    }
	private void fixWidth(final int columnIndex, final int width) {
	    TableColumn column = getColumnModel().getColumn(columnIndex);
	    column.setMinWidth(width);
	    column.setMaxWidth(width);
	    column.setPreferredWidth(width);
	}
}
