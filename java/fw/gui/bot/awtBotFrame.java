package fw.gui.bot;

import javax.swing.JPanel;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.lwjgl.LWJGLException;

import fw.game.GameEngine;
import fw.game.GameVisualInterface;
import fw.game.L2Char;
import fw.game.L2Item;
import fw.game.L2PartyChar;
import fw.game.L2Skill;
import fw.game.L2SkillUse;
import fw.game.PlayerChar;
import fw.game.UserChar;
import fw.game.model.CharSelectInfoPackage;
import fw.game.model.L2Player;
import fw.game.model.instances.L2NpcInstance;
import fw.gui.ServerConfig;
import fw.test.AWTGLRender;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class awtBotFrame extends JPanel implements GameVisualInterface {

	private static final long serialVersionUID = 1L;

	private final static String iconDir = "./data/res/icons";
	public final static HashMap<String, ImageIcon> iconList = new HashMap<String, ImageIcon>();

	static {
		iconList.clear();
		File[] files;
		File path = new File(iconDir);
		if (path.isFile()) {
			files = new File[] { path };
		} else {
			files = path.listFiles();
		}
		for (File f : files) {
			// ImageIcon cup = new ImageIcon(f.getPath());
			iconList.put(f.getName().replaceAll(".png", ""),
					new ImageIcon(f.getPath()));
			System.out.println("ICON: " + f.getPath() + " name: "
					+ f.getName().replaceAll(".png", ""));
		}
	}

	private GameEngine _gameEngine;
	private JTabbedPane _tabPane;
	private int _tabPaneIndex;

	private AWTGLRender _canvas;
	private JTextField loginTxt;
	private JPasswordField passwordField;
	private JTextField txtMsg;

	public awtBotFrame(HashMap<String, ServerConfig> srvList,
			JTabbedPane tabPane, int tind) throws LWJGLException {
		super();
		setBackground(Color.WHITE);
		_tabPane = tabPane;
		_tabPaneIndex = tind;
		_gameEngine = new GameEngine(this);

		JPanel panelAuth = new JPanel();
		panelAuth.setBackground(Color.WHITE);
		panelAuth.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));

		JLabel lblSrv = new JLabel("HOST:");
		panelAuth.add(lblSrv);

		final JComboBox<String> comboServers = new JComboBox<String>();

		final JComboBox<Integer> comboCharNum = new JComboBox<Integer>();
		for (int i = 1; i < 8; i++)
			comboCharNum.addItem(i);

		final JComboBox<ServerConfig> comboHosts = new JComboBox<ServerConfig>();
		comboHosts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboServers.removeAll();
				comboServers.addItem(((ServerConfig) comboHosts
						.getSelectedItem()).gameServers[0]);
			}
		});
		for (Entry<String, ServerConfig> srv : srvList.entrySet())
			comboHosts.addItem(srv.getValue());
		comboHosts.setSelectedIndex(0);
		panelAuth.add(comboHosts);

		JPanel panelMapInfo = new JPanel();

		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(Color.ORANGE);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addComponent(panelAuth, GroupLayout.DEFAULT_SIZE, 1105,
						Short.MAX_VALUE)
				.addComponent(panelFooter, Alignment.TRAILING,
						GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addComponent(panelMapInfo,
										GroupLayout.PREFERRED_SIZE, 851,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(244, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addComponent(panelAuth, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panelMapInfo, GroupLayout.PREFERRED_SIZE,
								354, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelFooter, GroupLayout.DEFAULT_SIZE,
								175, Short.MAX_VALUE)));
		panelFooter.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panelChatx = new JPanel();
		panelFooter.add(panelChatx);

		JTabbedPane tabbedChat = new JTabbedPane(JTabbedPane.TOP);
		final JTextArea _xChatMain = new JTextArea();
		_xChatMain.setEditable(false);
		tabbedChat.addTab("Main", null, _xChatMain,"");

		JPanel panelChatSend = new JPanel();
		FlowLayout fl_panelChatSend = (FlowLayout) panelChatSend.getLayout();
		fl_panelChatSend.setAlignment(FlowLayout.LEFT);
		GroupLayout gl_panelChatx = new GroupLayout(panelChatx);
		gl_panelChatx.setHorizontalGroup(gl_panelChatx
				.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedChat, Alignment.TRAILING,
						GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
				.addComponent(panelChatSend, Alignment.TRAILING,
						GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE));
		gl_panelChatx.setVerticalGroup(gl_panelChatx.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panelChatx
						.createSequentialGroup()
						.addComponent(tabbedChat, GroupLayout.PREFERRED_SIZE,
								108, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelChatSend,
								GroupLayout.PREFERRED_SIZE, 36,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(25, Short.MAX_VALUE)));

		JComboBox comboBox = new JComboBox();
		panelChatSend.add(comboBox);

		txtMsg = new JTextField();
		txtMsg.setText("Msg");
		panelChatSend.add(txtMsg);
		txtMsg.setColumns(25);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_xChatMain.setText(txtMsg.getText());
			}
		});
		panelChatSend.add(btnSend);
		panelChatx.setLayout(gl_panelChatx);

		JLabel lblSrv_1 = new JLabel("SRV");
		panelAuth.add(lblSrv_1);

		panelAuth.add(comboServers);

		JLabel lblChar = new JLabel("CHAR");
		panelAuth.add(lblChar);

		panelAuth.add(comboCharNum);

		JLabel lblLogin = new JLabel("Login:");
		panelAuth.add(lblLogin);

		loginTxt = new JTextField();
		loginTxt.setText("mlex");
		panelAuth.add(loginTxt);
		loginTxt.setColumns(8);

		JLabel lblPwd = new JLabel("PWD:");
		panelAuth.add(lblPwd);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		panelAuth.add(passwordField);

		final JButton btnLogin = new JButton("Login");
		final JButton btnLogout = new JButton("LogOut");

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_gameEngine.doLogin(loginTxt.getText(), String
						.valueOf(passwordField.getPassword()),
						((ServerConfig) comboHosts.getSelectedItem()).hostLogin
								.toLowerCase(), ((ServerConfig) comboHosts
								.getSelectedItem()).port,
						((ServerConfig) comboHosts.getSelectedItem()).protocol, /* serv_num */
						1, comboCharNum.getSelectedIndex() + 1);
				btnLogin.setEnabled(false);
				btnLogout.setEnabled(true);
			}
		});
		btnLogin.setIcon(getIcon("connect"));
		panelAuth.add(btnLogin);

		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnLogin.setEnabled(true);
				btnLogout.setEnabled(false);
				try {
					_gameEngine.getLoginConnection().getSocket().disconnect();
					_gameEngine.getGameConnection().getSocket().disconnect();
				} catch (Exception e) {
				}
			}
		});
		btnLogout.setIcon(getIcon("disconnect"));
		panelAuth.add(btnLogout);

		JToggleButton tglbtnEnabled = new JToggleButton("Enabled");
		tglbtnEnabled.setSelectedIcon(getIcon("status_online"));
		tglbtnEnabled.setIcon(getIcon("status_offline"));
		panelAuth.add(tglbtnEnabled);

		groupLayout.setAutoCreateGaps(true);
		panelMapInfo.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel centerBlock = new JPanel();
		// panelMap.setLayout(gl_panelMap);

		JPanel panelMapContainer = new JPanel();

		JPanel panelMap = new JPanel();

		JPanel panelUserStatus = new JPanel();
		panelUserStatus.setBackground(Color.GRAY);
		panelMap.setLayout(new GridLayout(0, 1, 0, 0));
		_canvas = new AWTGLRender(panelMap);
		panelMap.add(_canvas);
		JTabbedPane tabUserInfo = new JTabbedPane();
		tabUserInfo.setTabPlacement(JTabbedPane.BOTTOM);

		tabUserInfo.addTab("Char", null, new JPanel(), "");
		tabUserInfo.setIconAt(0, getIcon("user_green"));

		tabUserInfo.addTab("Party", null, new JPanel(), "");
		tabUserInfo.setIconAt(1, getIcon("group"));

		tabUserInfo.addTab("Inv", null, new JPanel(), "");
		tabUserInfo.setIconAt(2, getIcon("drive_user"));

		tabUserInfo.addTab("Skills", null, new JPanel(), "");
		tabUserInfo.setIconAt(3, getIcon("skills"));

		tabUserInfo.addTab("Buffs", null, new JPanel(), "");
		tabUserInfo.setIconAt(4, getIcon("buffs"));
		// splitMapInfo.setContinuousLayout(false);
		// splitMapInfo.setResizeWeight(0.3);
		panelMapInfo.add(centerBlock);
		GroupLayout gl_centerBlock = new GroupLayout(centerBlock);
		gl_centerBlock.setHorizontalGroup(gl_centerBlock.createParallelGroup(
				Alignment.TRAILING).addGroup(
				Alignment.LEADING,
				gl_centerBlock
						.createSequentialGroup()
						.addComponent(panelMapContainer,
								GroupLayout.PREFERRED_SIZE, 566,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(tabUserInfo, GroupLayout.DEFAULT_SIZE,
								261, Short.MAX_VALUE).addContainerGap()));
		gl_centerBlock
				.setVerticalGroup(gl_centerBlock
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_centerBlock
										.createSequentialGroup()
										.addGroup(
												gl_centerBlock
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																panelMapContainer,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																tabUserInfo,
																GroupLayout.PREFERRED_SIZE,
																354,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		// JPanel panelMap = new JPanel();

		JPanel panelMapTools = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelMapTools.getLayout();
		flowLayout.setVgap(1);
		flowLayout.setHgap(1);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelMapTools.setBackground(SystemColor.control);

		// GroupLayout gl_panelMap = new GroupLayout(panelMap);
		// gl_panelMap.setHonorsVisibility(false);

		JCheckBox chckbxEnabled = new JCheckBox("Enabled");
		panelMapTools.add(chckbxEnabled);

		JCheckBox chckbxRealMap = new JCheckBox("Real Map");
		panelMapTools.add(chckbxRealMap);

		JSlider sliderMapZoom = new JSlider();
		sliderMapZoom.setMinimum(1);
		panelMapTools.add(sliderMapZoom);

		JCheckBox chckbxNames = new JCheckBox("Names");
		panelMapTools.add(chckbxNames);

		JCheckBox chckbxNpc = new JCheckBox("NPC");
		panelMapTools.add(chckbxNpc);

		JCheckBox chckbxMon = new JCheckBox("MON");
		panelMapTools.add(chckbxMon);

		JCheckBox chckbxPlayer = new JCheckBox("Player");
		chckbxPlayer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
			}
		});
		panelMapTools.add(chckbxPlayer);

		JCheckBox chckbxItems = new JCheckBox("Item");
		panelMapTools.add(chckbxItems);

		JCheckBox chckbxBuy = new JCheckBox("Buy");
		panelMapTools.add(chckbxBuy);

		JCheckBox chckbxSell = new JCheckBox("Sell");
		panelMapTools.add(chckbxSell);

		JCheckBox chckbxZRange = new JCheckBox("Z Range");
		panelMapTools.add(chckbxZRange);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(500), null, null,
				new Integer(25)));
		panelMapTools.add(spinner);
		GroupLayout gl_panelMapContainer = new GroupLayout(panelMapContainer);
		gl_panelMapContainer
				.setHorizontalGroup(gl_panelMapContainer
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panelMapContainer
										.createSequentialGroup()
										.addGroup(
												gl_panelMapContainer
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_panelMapContainer
																		.createParallelGroup(
																				Alignment.TRAILING,
																				false)
																		.addComponent(
																				panelMapTools,
																				Alignment.LEADING,
																				0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				panelMap,
																				Alignment.LEADING,
																				GroupLayout.DEFAULT_SIZE,
																				566,
																				Short.MAX_VALUE))
														.addComponent(
																panelUserStatus,
																GroupLayout.DEFAULT_SIZE,
																583,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_panelMapContainer
				.setVerticalGroup(gl_panelMapContainer.createParallelGroup(
						Alignment.LEADING).addGroup(
						gl_panelMapContainer
								.createSequentialGroup()
								.addComponent(panelUserStatus,
										GroupLayout.PREFERRED_SIZE, 80,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panelMap,
										GroupLayout.PREFERRED_SIZE, 207,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panelMapTools,
										GroupLayout.PREFERRED_SIZE, 56,
										GroupLayout.PREFERRED_SIZE).addGap(23)));
		panelMapContainer.setLayout(gl_panelMapContainer);
		centerBlock.setLayout(gl_centerBlock);
		setLayout(groupLayout);
	}

	public static ImageIcon getIcon(String ic) {
		if (iconList.containsKey(ic.toLowerCase()))
			return iconList.get(ic.toLowerCase());
		return null;
	}

	@Override
	public void procSetUserChar(L2Player userChar) {
		_tabPane.setTitleAt(_tabPaneIndex, userChar.getName());

	}

	@Override
	public void putMessage(String msg, int msg_type, boolean bold) {
		System.out.println("MSG: " + msg + " Type: " + msg_type);
	}

	@Override
	public void procLogoutEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestPartyDialog(String requestor, String partyType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestHtmlDialog(String htmlMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestHeroesListDialog(CharSelectInfoPackage[] heroesList,
			int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procPlayerChar(L2Player playerChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procNpcChar(L2NpcInstance npcChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procDeleteL2char(L2Char l2Char) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procDeleteItem(L2Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procPlayerCharClanInfo(PlayerChar playerChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procMyTargetSelected(L2Char targetChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procUpdateTargetcharStatus(L2Char targetChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procUpdateUsercharStatus(UserChar userChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procMyTargetUnselected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void procAddSkill(L2Skill l2Skill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procSelfSkillUse(L2Skill l2Skill, L2SkillUse l2SkillUse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procRemoveSkill(L2Skill l2Skill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procAddPartyChars(L2PartyChar[] l2PartyChars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procUpdatePartyChar(L2PartyChar l2PartyChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procDeletePartyChar(L2PartyChar l2PartyChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procDeleteAllPartyChars() {
		// TODO Auto-generated method stub

	}

	@Override
	public void procAddItems(L2Item[] items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procAddEnvItem(L2Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procUpdateItems(L2Item[] items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procDeletItems(L2Item[] items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procTeleportClear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestTrade(L2Char tradeChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestTradeDialog(L2Item[] items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procSendTradeDone() {
		// TODO Auto-generated method stub

	}

	@Override
	public void procConfirmedTrade() {
		// TODO Auto-generated method stub

	}

	@Override
	public void procTradeAddOwnItem(L2Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procTradeAddOtherItem(L2Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkTradeDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
