package fw.gui.bot;

import javax.swing.JPanel;

import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.lwjgl.LWJGLException;

import fw.test.AWTGLRender;
import net.miginfocom.swing.MigLayout;

public class awtBotFrame extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private AWTGLRender _canvas;
	
	public awtBotFrame() {
		super();
		
		JPanel panelAuth = new JPanel();
		panelAuth.setBackground(Color.WHITE);
		panelAuth.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblSrv = new JLabel("HOST:");
		panelAuth.add(lblSrv);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("localhost");
		panelAuth.add(comboBox);
		
		JPanel panelMapInfo = new JPanel();
		
		JPanel panelChat = new JPanel();
		FlowLayout fl_panelChat = (FlowLayout) panelChat.getLayout();
		fl_panelChat.setVgap(1);
		panelChat.setBackground(Color.ORANGE);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelAuth, GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
				.addComponent(panelMapInfo, GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
				.addComponent(panelChat, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelAuth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelMapInfo, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelChat, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
		);
		panelMapInfo.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		try {			
			JPanel panelMap = new JPanel();
			panelMap.setLayout(new GridLayout(2, 1, 0, 0));
			_canvas = new AWTGLRender(panelMap);
			_canvas.setSize(500, 100);
			panelMap.add(_canvas);
			splitPane.setLeftComponent(panelMap);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		JTabbedPane _tabUserInfo = new JTabbedPane();
		_tabUserInfo.addTab("Tab 2", null, new JComboBox(),"Does nothing");
		
		splitPane.setRightComponent(_tabUserInfo);
		splitPane.setContinuousLayout(true);
		panelMapInfo.add(splitPane);
		setLayout(groupLayout);
	}
}
