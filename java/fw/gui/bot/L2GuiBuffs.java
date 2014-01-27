package fw.gui.bot;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import fw.game.model.L2Player;
import fw.game.model.L2Skill;

@SuppressWarnings("serial")
public class L2GuiBuffs extends L2GuiJTable {

	private String[] columnNames = { "Icon", "Name", "Left", "LVL" };
	private Object[][] data = {};
	private long _lastUpdate = System.currentTimeMillis();

	public L2GuiBuffs() {
		super();
		_tableModel = new DefaultTableModel(data, columnNames){
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		setModel(_tableModel);
		fixWidth(0, 32);
		setRowHeight(32);
	}

	public synchronized void updateBaffs(L2Player _char) {
		if (_char.getBuffs().getLastUpdate() != _lastUpdate) {

			_tableModel.setNumRows(0);
			for (L2Skill _el : _char.getBuffs().getCopy())
				_tableModel.addRow(new Object[] {
						awtBotFrame.getGameIcon(_el.getIcon()),
						"(" + _el.getSkill_id() + ")" + _el.getName(),
						_el.getBuffLeft(), _el.getLevel() });
			_lastUpdate = _char.getBuffs().getLastUpdate();
		}else{
			//TODO Сделать обновление ячейки со здачением времени			
		}
	}

	public Class<? extends Object> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}
}
