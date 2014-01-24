package fw.gui.bot;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import fw.extensions.util.GCArray;
import fw.game.model.L2Item;
import fw.game.model.L2PlayerInventory;
import fw.game.model.L2Skill;

public class GuiSkills extends JTable {
	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = {"Icon", "Name", "lvl"};
	private Object[][] data = {};
	private DefaultTableModel tableInvModel;
	private long _lastUpdate = System.currentTimeMillis();
	
	public GuiSkills() {
		super();
		tableInvModel = new DefaultTableModel(data,columnNames);
		setModel(tableInvModel);
		fixWidth(0, 32);
		setRowHeight(32);
		tableInvModel.addRow(new Object[]{awtBotFrame.getGameIcon("none"), "x", 1});	
	}	
	
	public synchronized void updateSkills(GCArray<L2Skill> _skills){
		
		//if(_lastUpdate == _skills.getLastUpdate()) return;
		//_lastUpdate =_skills.getLastUpdate();
		
		DefaultTableModel model = (DefaultTableModel) getModel();
		model.setNumRows(0);	
		for (int i = 0; i < _skills.size(); i++) {
			L2Skill _el = _skills.get(i);
			model.addRow(new Object[]{awtBotFrame.getGameIcon(_el.getIcon()), _el.getName(), _el.getLevel()});
		}
					
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
