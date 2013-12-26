package fw.gui.mainArea;

import fw.game.GameEngine;
import fw.game.GameVisualInterface;
import fw.game.L2Char;
import fw.game.L2Item;
import fw.game.L2PartyChar;
import fw.game.L2Skill;
import fw.game.L2SkillUse;
import fw.game.NpcChar;
import fw.game.PlayerChar;
import fw.game.UserChar;
import fw.game.clientpackets.Say;
import fw.gui.ServerConfig;
import fw.gui.dialogs.RequestHeroesListDialog;
import fw.gui.dialogs.RequestHtmlDialog;
import fw.gui.dialogs.RequestPartyDaialog;
import fw.gui.dialogs.RequestTradeDialog;
import fw.gui.game_canvas.GameCanvas;
import fw.gui.game_canvas.GameDraw;
import fw.gui.mainArea.efects.VisualEfectsTimer;
import java.util.Iterator;
import javolution.util.FastMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import fw.util.Printer;
import fw.com.swtdesigner.SWTResourceManager;

import fw.connection.game.server.CharSelectionInfo.Heroes;
import fw.dbClasses.DbObjects;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class UserMainArea extends org.eclipse.swt.widgets.Composite implements GameVisualInterface
{
	private Button btnLeaveParty;
	private Button btnInviteParty;
	private Composite cpPartyActions;
	private Composite cpGameTop;
	private CTabItem tbi_skills_actives;
	private CTabFolder envTab;
	private TableColumn tableColumn2;
	private TableColumn tableColumn1;
	private Table gridItems;
	private Composite cpItems;
	private CTabItem tbi_items;
	private StyledText textCombatMessages;
	private InventoryArea inventoryAreaNormal;
	private CTabItem tbi_quest_items;
	private CTabItem tbi_normal_items;
	private CTabFolder tab_inventory;
	private CTabItem tbi_inventory;
	private PartyArea partyArea;
	private SkillArea skillAreaPassive;
	private SkillArea skillAreaActive;
	private CTabItem tbi_skills_passives;
	private CTabFolder tab_skillsTypes;
	private User_life_area user_life_area;
	private TableColumn tableColumn6;
	private Table gridNpcs;
	private Composite cpNpcs;
	private CTabItem tbi_npcs;
	private TableColumn tableColumn5;
	private TableColumn tableColumn4;
	private TableColumn tableColumn3;
	private Table gridMobs;
	private Composite cpMobs;
	private CTabItem tbi_mobs;
	private CTabItem tbi_players;
	private Text edPartyTarget;
	private TargetArea targetArea;
	private Composite cpSkills;
	private CTabItem tbi_skills;
	private GameCanvas gameCanvas;
	private Composite cpParty;
	private Composite cpHeader;
	private SashForm cpArea1;
	private CTabFolder tab_informations;
	private CLabel cLabel3;
	private Text edLogin;
	private CCombo cbCharNum;
	private CLabel cLabel2;
	private CLabel cLabel1;
	private CCombo cbHostNum;
	private CTabItem tbi_party;
	private Table gridPlayers;
	private Composite cpPlayers;
	private CCombo cbChatTarget;
	private CCombo cbChatType;
	private Text edChatSend;
	private StyledText textChat;
	private Composite cpMessages;
	public Button btnLogout;
	private Button btnLogin;
	private Text edPassword;
	private CLabel cLabel4;
	private CCombo cbServer;
	private CTabItem parentTabItem = null;
	private SashForm cpInformations;
	private Composite cpGame;
	public RequestTradeDialog myTradeDialog;
	Colors colors = new Colors();
	FastMap<String, ServerConfig> mapServersConfig = null;

	public final GameEngine gameEngine = new GameEngine(this);
	public final VisualEfectsTimer visualEfectsTimer = new VisualEfectsTimer();
	private Label lClass;
	private Label lClan;
	private Composite composite1;
	private CTabItem tbiPlayer;

	public UserMainArea(Composite composite, int style)
	{
		super(composite, style);
		visualEfectsTimer.start();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			{
				GridLayout thisLayout = new GridLayout();
				thisLayout.makeColumnsEqualWidth = true;
				this.setLayout(thisLayout);
				this.setSize(881, 501);
				{
					cpHeader = new Composite(this, SWT.BORDER);
					cpHeader.setLayout(null);
					GridData cpHeaderLData = new GridData();
					cpHeaderLData.heightHint = 26;
					cpHeaderLData.horizontalAlignment = GridData.FILL;
					cpHeaderLData.horizontalSpan = 0;
					cpHeaderLData.verticalAlignment = GridData.BEGINNING;
					cpHeaderLData.grabExcessHorizontalSpace = true;
					cpHeader.setLayoutData(cpHeaderLData);
					{
						cbServer = new CCombo(cpHeader, SWT.BORDER);
						cbServer.setBounds(8, 2, 111, 21);
						cbServer.setEditable(false);
						cbServer.setBackground(SWTResourceManager.getColor(255, 255, 255));
						cbServer.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								cbServerWidgetSelected(evt);
							}
						});
					}
					{
						cbHostNum = new CCombo(cpHeader, SWT.BORDER);
						cbHostNum.setBounds(159, 2, 50, 21);
						cbHostNum.setEditable(false);
						cbHostNum.setBackground(SWTResourceManager.getColor(255, 255, 255));
					}
					{
						cbCharNum = new CCombo(cpHeader, SWT.BORDER);
						cbCharNum.setBounds(249, 2, 50, 21);
						cbCharNum.setEditable(false);
						cbCharNum.setBackground(SWTResourceManager.getColor(255, 255, 255));
						cbCharNum.add("1");
						cbCharNum.add("2");
						cbCharNum.add("3");
						cbCharNum.add("4");
						cbCharNum.add("5");
						cbCharNum.add("6");
						cbCharNum.add("7");
						cbCharNum.select(0);
					}
					{
						edLogin = new Text(cpHeader, SWT.BORDER);
						edLogin.setBounds(328, 2, 106, 21);
						edLogin.setTextLimit(16);
					}
					{
						edPassword = new Text(cpHeader, SWT.BORDER);
						edPassword.setTextLimit(16);
						edPassword.setBounds(474, 2, 106, 21);
						edPassword.setEchoChar('*');
					}
					{
						cLabel1 = new CLabel(cpHeader, SWT.NONE);
						cLabel1.setText("HOST");
						cLabel1.setBounds(120, 4, 40, 19);
						cLabel1.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
						cLabel1.setAlignment(SWT.RIGHT);
					}
					{
						cLabel2 = new CLabel(cpHeader, SWT.NONE);
						cLabel2.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
						cLabel2.setText("CHAR");
						cLabel2.setBounds(208, 4, 43, 19);
						cLabel2.setAlignment(SWT.RIGHT);
					}
					{
						cLabel3 = new CLabel(cpHeader, SWT.NONE);
						cLabel3.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
						cLabel3.setText("ID:");
						cLabel3.setBounds(301, 4, 28, 19);
						cLabel3.setAlignment(SWT.RIGHT);
					}
					{
						cLabel4 = new CLabel(cpHeader, SWT.NONE);
						cLabel4.setFont(SWTResourceManager.getFont("Tahoma", 8, 1, false, false));
						cLabel4.setText("PWD:");
						cLabel4.setBounds(436, 4, 40, 19);
						cLabel4.setAlignment(SWT.RIGHT);
					}
					{
						btnLogin = new Button(cpHeader, SWT.PUSH | SWT.CENTER);
						btnLogin.setText("Login");
						btnLogin.setBounds(583, 1, 72, 24);
						btnLogin.setAlignment(SWT.UP);
						btnLogin.setImage(SWTResourceManager.getImage("res_images/connect16_h.png"));
						btnLogin.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent evt)
							{
								btnLoginWidgetSelected(evt);
							}
						});
					}
					{
						btnLogout = new Button(cpHeader, SWT.PUSH | SWT.CENTER);
						btnLogout.setText("Logout");
						btnLogout.setBounds(660, 1, 72, 24);
						btnLogout.setEnabled(false);
						btnLogout.setImage(SWTResourceManager.getImage("res_images/disconnect16_h.png"));
						btnLogout.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent evt)
							{
								btnLogoutWidgetSelected(evt);
							}
						});
					}

				}
				{
					cpArea1 = new SashForm(this, SWT.NONE);
					FormLayout cpArea1Layout = new FormLayout();
					GridData cpArea1LData = new GridData();
					cpArea1LData.grabExcessVerticalSpace = true;
					cpArea1LData.grabExcessHorizontalSpace = true;
					cpArea1LData.horizontalAlignment = GridData.FILL;
					cpArea1LData.verticalAlignment = GridData.FILL;
					cpArea1.setLayoutData(cpArea1LData);
					cpArea1.setLayout(cpArea1Layout);
					cpArea1.setBackground(SWTResourceManager.getColor(192, 192, 192));
					{
						cpGame = new Composite(cpArea1, SWT.BORDER);
						GridLayout cpGameLayout = new GridLayout();
						cpGameLayout.makeColumnsEqualWidth = true;
						cpGameLayout.marginHeight = 0;
						cpGameLayout.marginWidth = 0;
						cpGameLayout.verticalSpacing = 0;
						cpGame.setLayout(cpGameLayout);
						FormData cpGameLData = new FormData();
						cpGame.setLayoutData(cpGameLData);
						{
							cpGameTop = new Composite(cpGame, SWT.BORDER);
							GridLayout cpGameTopLayout = new GridLayout();
							cpGameTopLayout.marginHeight = 0;
							cpGameTopLayout.marginWidth = 0;
							cpGameTopLayout.numColumns = 2;
							cpGameTop.setLayout(cpGameTopLayout);
							GridData cpGameTopLData = new GridData();
							cpGameTopLData.grabExcessHorizontalSpace = true;
							cpGameTopLData.horizontalAlignment = GridData.FILL;
							cpGameTopLData.horizontalSpan = 0;
							cpGameTopLData.heightHint = 68;
							cpGameTop.setLayoutData(cpGameTopLData);
							{
								GridData user_life_areaLData = new GridData();
								user_life_areaLData.widthHint = 168;
								user_life_areaLData.verticalAlignment = GridData.FILL;
								user_life_areaLData.grabExcessVerticalSpace = true;
								user_life_area = new User_life_area(cpGameTop, SWT.BORDER);
								user_life_area.setLayoutData(user_life_areaLData);
								user_life_area.setFont(SWTResourceManager.getFont("Segoe UI", 8, 0, false, false));
								user_life_area.addMouseListener(new MouseAdapter() {
									public void mouseDoubleClick(MouseEvent evt) {
										user_life_areaMouseDoubleClick(evt);
									}
								});
							}
							{
								GridData targetAreaLData = new GridData();
								targetAreaLData.horizontalAlignment = GridData.END;
								targetAreaLData.grabExcessVerticalSpace = true;
								targetAreaLData.widthHint = 168;
								targetAreaLData.verticalSpan = 0;
								targetAreaLData.verticalAlignment = GridData.FILL;
								targetAreaLData.grabExcessHorizontalSpace = true;
								targetArea = new TargetArea(cpGameTop, SWT.BORDER);
								targetArea.setLayoutData(targetAreaLData);
								targetArea.addMouseListener(new MouseAdapter()
								{
									public void mouseDoubleClick(MouseEvent evt)
									{
										targetAreaMouseDoubleClick(evt);
									}
								});
								targetArea.addKeyListener(new KeyAdapter()
								{
									public void keyPressed(KeyEvent evt)
									{
										targetAreaKeyPressed(evt);
									}
								});
							}
						}
						{
							GridData gameCanvasLData = new GridData();
							gameCanvasLData.horizontalAlignment = GridData.FILL;
							gameCanvasLData.verticalAlignment = GridData.FILL;
							gameCanvasLData.grabExcessHorizontalSpace = true;
							gameCanvasLData.grabExcessVerticalSpace = true;
							gameCanvas = new GameCanvas(cpGame, SWT.BORDER);
							gameCanvas.setLayoutData(gameCanvasLData);
							gameCanvas.setBackground(SWTResourceManager.getColor(210, 230, 250));
							gameCanvas.setDrawInterface(new GameDraw(gameCanvas));
						}
						{
							cpMessages = new Composite(cpGame, SWT.BORDER);
							GridLayout cpMessagesLayout = new GridLayout();
							cpMessagesLayout.horizontalSpacing = 2;
							cpMessagesLayout.marginWidth = 2;
							cpMessagesLayout.marginHeight = 2;
							cpMessagesLayout.numColumns = 3;
							cpMessages.setLayout(cpMessagesLayout);
							GridData cpMessagesLData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
							cpMessagesLData.heightHint = 200;
							cpMessages.setLayoutData(cpMessagesLData);
							{
								textChat = new StyledText(cpMessages, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
								textChat.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
								final GridData gd_textChat = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
								gd_textChat.widthHint = 186;
								textChat.setLayoutData(gd_textChat);
								textChat.setFont(SWTResourceManager.getFont("Arial", 8, 0, false, false));
								textChat.setEditable(false);
							}
							{
								textCombatMessages = new StyledText(
									cpMessages,
									SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
								textCombatMessages.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
								FillLayout textCombatMessagesLayout = new FillLayout(
									org.eclipse.swt.SWT.HORIZONTAL);
								textCombatMessages.setLayout(textCombatMessagesLayout);
								textCombatMessages.setEditable(false);
								GridData textCombatMessagesLData = new GridData(SWT.RIGHT, SWT.FILL, false, false);
								textCombatMessagesLData.heightHint = 100;
								textCombatMessagesLData.widthHint = 250;
								textCombatMessages.setLayoutData(textCombatMessagesLData);
								textCombatMessages.setFont(SWTResourceManager.getFont("Arial", 8, 0, false, false));
							}
							{
								cbChatType = new CCombo(cpMessages, SWT.READ_ONLY | SWT.BORDER);
								cbChatType.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
								cbChatType.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
								cbChatType.setBackground(SWTResourceManager.getColor(255, 255, 255));
								cbChatType.setEditable(false);
								cbChatType.setItems(Say.chatNames);
								cbChatType.addSelectionListener(new SelectionAdapter()
								{
									public void widgetSelected(SelectionEvent evt)
									{
										cbChatTypeWidgetSelected(evt);
									}
								});
								cbChatType.select(0);
							}
							{
								cbChatTarget = new CCombo(cpMessages, SWT.BORDER);
								cbChatTarget.setEditable(false);
								cbChatTarget.setForeground(SWTResourceManager.getColor(128, 255, 0));
								cbChatTarget.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
								final GridData gd_cbChatTarget = new GridData(SWT.FILL, SWT.BOTTOM, false, false);
								gd_cbChatTarget.widthHint = 196;
								cbChatTarget.setLayoutData(gd_cbChatTarget);
								cbChatTarget.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
								cbChatTarget.setVisible(false);
							}
							new Label(cpMessages, SWT.NONE);
							{
								edChatSend = new Text(cpMessages, SWT.WRAP | SWT.BORDER);
								edChatSend.setFont(SWTResourceManager.getFont("Arial Unicode MS", 9, SWT.NONE));
								final GridData gd_edChatSend = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
								gd_edChatSend.widthHint = 304;
								edChatSend.setLayoutData(gd_edChatSend);
								edChatSend.setTextLimit(255);
								edChatSend.addKeyListener(new KeyAdapter()
								{
									public void keyPressed(KeyEvent evt)
									{
										edChatSendKeyPressed(evt);
									}
								});
							}
						}
					}
					{
						cpInformations = new SashForm(cpArea1, SWT.VERTICAL | SWT.V_SCROLL);
						FormLayout cpInformationsLayout = new FormLayout();
						FormData cpInformationsLData = new FormData(200, 456);
						cpInformations.setLayoutData(cpInformationsLData);
						cpInformations.setLayout(cpInformationsLayout);
						cpInformations.setSize(200, 456);
						{
							tab_informations = new CTabFolder(
								cpInformations,
								SWT.NONE);
							{
								tbiPlayer = new CTabItem(tab_informations, SWT.NONE);
								tbiPlayer.setText("Player");
								tbiPlayer.setImage(SWTResourceManager.getImage("res_images/player.png"));
								{
									composite1 = new Composite(tab_informations, SWT.NONE);
									FormLayout composite1Layout = new FormLayout();
									composite1.setLayout(composite1Layout);
									tbiPlayer.setControl(composite1);
									{
										lClan = new Label(composite1, SWT.NONE);
										FormData lClanLData = new FormData();
										lClanLData.width = 27;
										lClanLData.height = 15;
										lClanLData.left =  new FormAttachment(15, 1000, 0);
										lClanLData.right =  new FormAttachment(132, 1000, 0);
										lClanLData.top =  new FormAttachment(17, 1000, 0);
										lClanLData.bottom =  new FormAttachment(92, 1000, 0);
										lClan.setLayoutData(lClanLData);
										lClan.setText("Clan:");
									}
									{
										lClass = new Label(composite1, SWT.NONE);
										FormData lClassLData = new FormData();
										lClassLData.width = 37;
										lClassLData.height = 17;
										lClassLData.left =  new FormAttachment(15, 1000, 0);
										lClassLData.right =  new FormAttachment(175, 1000, 0);
										lClassLData.top =  new FormAttachment(121, 1000, 0);
										lClassLData.bottom =  new FormAttachment(206, 1000, 0);
										lClass.setLayoutData(lClassLData);
										lClass.setText("Class:");
									}
								}
							}
							{
								tbi_inventory = new CTabItem(
									tab_informations,
									SWT.NONE);
								tbi_inventory.setText("Inventory");
								tbi_inventory.setFont(SWTResourceManager
									.getFont("Tahoma", 8, 1, false, false));
								tbi_inventory.setImage(SWTResourceManager.getImage("res_images/inventory.png"));

								{
									tab_inventory = new CTabFolder(
										tab_informations,
										SWT.NONE);
									tbi_inventory.setControl(tab_inventory);
									{
										tbi_normal_items = new CTabItem(
											tab_inventory,
											SWT.NONE);
										tbi_normal_items.setText("Items");
										{
											inventoryAreaNormal = new InventoryArea(
												tab_inventory,
												SWT.NONE);
											tbi_normal_items
												.setControl(inventoryAreaNormal);
										}
									}
									{
										tbi_quest_items = new CTabItem(
											tab_inventory,
											SWT.NONE);
										tbi_quest_items.setText("Quest");
									}
									tab_inventory.setSelection(0);
								}
							}
							{
								tbi_party = new CTabItem(
									tab_informations,
									SWT.NONE);
								tbi_party.setText("Party");
								tbi_party.setFont(SWTResourceManager.getFont(
									"Tahoma",
									8,
									1,
									false,
									false));
								tbi_party.setImage(SWTResourceManager.getImage("res_images/party.png"));
								{
									cpParty = new Composite(
										tab_informations,
										SWT.NONE);
									GridLayout cpPartyLayout = new GridLayout();
									cpPartyLayout.makeColumnsEqualWidth = true;
									cpPartyLayout.marginWidth = 0;
									cpPartyLayout.marginHeight = 0;
									cpPartyLayout.verticalSpacing = 1;
									cpParty.setLayout(cpPartyLayout);
									tbi_party.setControl(cpParty);
									{
										GridData partyAreaLData = new GridData();
										partyAreaLData.grabExcessVerticalSpace = true;
										partyAreaLData.grabExcessHorizontalSpace = true;
										partyAreaLData.horizontalAlignment = GridData.FILL;
										partyAreaLData.verticalAlignment = GridData.FILL;
										partyArea = new PartyArea(
											cpParty,
											SWT.BORDER);
										partyArea.setLayoutData(partyAreaLData);
									}
									{
										cpPartyActions = new Composite(
											cpParty,
											SWT.BORDER);
										GridData cpPartyActionsLData = new GridData();
										cpPartyActionsLData.heightHint = 26;
										cpPartyActionsLData.horizontalAlignment = GridData.FILL;
										cpPartyActionsLData.grabExcessHorizontalSpace = true;
										cpPartyActionsLData.verticalAlignment = GridData.END;
										cpPartyActions
											.setLayoutData(cpPartyActionsLData);
										cpPartyActions.setLayout(null);
										{
											btnInviteParty = new Button(
												cpPartyActions,
												SWT.PUSH | SWT.CENTER);
											btnInviteParty.setText("Invite");
											btnInviteParty.setBounds(
												118,
												4,
												60,
												20);
											btnInviteParty
												.addSelectionListener(new SelectionAdapter() {
													public void widgetSelected(
														SelectionEvent evt) {
														btnInvitePartyWidgetSelected(evt);
													}
												});
										}
										{
											btnLeaveParty = new Button(
												cpPartyActions,
												SWT.PUSH | SWT.CENTER);
											btnLeaveParty.setText("Leave");
											btnLeaveParty.setBounds(
												183,
												4,
												60,
												20);
											btnLeaveParty
												.addSelectionListener(new SelectionAdapter() {
													public void widgetSelected(
														SelectionEvent evt) {
														btnLeavePartyWidgetSelected(evt);
													}
												});

										}
										{
											edPartyTarget = new Text(
												cpPartyActions,
												SWT.BORDER);
											edPartyTarget.setTextLimit(20);
											edPartyTarget.setBounds(
												1,
												3,
												114,
												21);
											edPartyTarget
												.setFont(SWTResourceManager
													.getFont(
														"Tahoma",
														7,
														0,
														false,
														false));
										}
									}
								}
							}
							{
								tbi_skills = new CTabItem(
									tab_informations,
									SWT.NONE);
								tbi_skills.setText("Skills");
								tbi_skills.setFont(SWTResourceManager.getFont(
									"Tahoma",
									8,
									1,
									false,
									false));
								tbi_skills.setImage(SWTResourceManager.getImage("res_images/skills.png"));
								{
									cpSkills = new Composite(
										tab_informations,
										SWT.NONE);
									FillLayout cpSkillsLayout = new FillLayout(
										org.eclipse.swt.SWT.HORIZONTAL);
									cpSkills.setLayout(cpSkillsLayout);
									tbi_skills.setControl(cpSkills);
									{
										tab_skillsTypes = new CTabFolder(
											cpSkills,
											SWT.NONE);
										{
											tbi_skills_actives = new CTabItem(
												tab_skillsTypes,
												SWT.NONE);
											tbi_skills_actives
												.setText("Actives");
											{
												skillAreaActive = new SkillArea(
													tab_skillsTypes,
													SWT.NONE);
												tbi_skills_actives
													.setControl(skillAreaActive);
												GridLayout skillAreaActiveLayout = new GridLayout();
												skillAreaActiveLayout.makeColumnsEqualWidth = true;
												skillAreaActiveLayout.marginWidth = 0;
												skillAreaActiveLayout.verticalSpacing = 0;
												skillAreaActiveLayout.marginHeight = 0;
												skillAreaActive
													.setLayout(skillAreaActiveLayout);
												skillAreaActive
													.setBackground(SWTResourceManager
														.getColor(210, 230, 250));
												skillAreaActive.setBounds(
													-2,
													-2,
													181,
													142);
											}
										}
										{
											tbi_skills_passives = new CTabItem(
												tab_skillsTypes,
												SWT.NONE);
											tbi_skills_passives
												.setText("Passives");
											{
												skillAreaPassive = new SkillArea(
													tab_skillsTypes,
													SWT.NONE);
												tbi_skills_passives
													.setControl(skillAreaPassive);
												GridLayout skillAreaPassiveLayout = new GridLayout();
												skillAreaPassiveLayout.makeColumnsEqualWidth = true;
												skillAreaPassiveLayout.horizontalSpacing = 0;
												skillAreaPassiveLayout.marginHeight = 0;
												skillAreaPassiveLayout.marginWidth = 0;
												skillAreaPassiveLayout.verticalSpacing = 0;
												skillAreaPassive
													.setLayout(skillAreaPassiveLayout);
												skillAreaPassive
													.setBackground(SWTResourceManager
														.getColor(210, 230, 250));
												skillAreaPassive.setBounds(
													-2,
													-2,
													181,
													142);
											}
										}
										tab_skillsTypes.setSelection(0);
									}
								}
							}
							tab_informations
								.addSelectionListener(new SelectionAdapter() {
									public void widgetSelected(
										SelectionEvent evt) {
										tab_informationsWidgetSelected(evt);
									}
								});
							tab_informations.setBounds(0, 0, 255, 186);
							tab_informations.setBorderVisible(true);
							tab_informations.setSimple(false);
							tab_informations.setSelection(0);
						}
						{
							envTab = new CTabFolder(cpInformations, SWT.NONE);
							{
								tbi_mobs = new CTabItem(envTab, SWT.NONE);
								tbi_mobs.setText("Mons");
								tbi_mobs.setImage(SWTResourceManager.getImage("res_images/mons.png"));
								{
									cpMobs = new Composite(envTab, SWT.NONE);
									tbi_mobs.setControl(cpMobs);
									FillLayout composite1Layout = new FillLayout(
										org.eclipse.swt.SWT.HORIZONTAL);
									cpMobs.setLayout(composite1Layout);
									{
										gridMobs = new Table(cpMobs, SWT.SINGLE
											| SWT.FULL_SELECTION
											| SWT.H_SCROLL
											| SWT.V_SCROLL
											| SWT.BORDER);
										gridMobs.setHeaderVisible(true);
										gridMobs.setLinesVisible(true);
										gridMobs
											.addMouseListener(new MouseAdapter() {
												public void mouseDoubleClick(
													MouseEvent evt) {
													gridMobsMouseDoubleClick(evt);
												}
											});

										{
											tableColumn3 = new TableColumn(
												gridMobs,
												SWT.NONE);
											tableColumn3.setText("Mob name");
											tableColumn3.setWidth(200);

											tableColumn4 = new TableColumn(
												gridMobs,
												SWT.NONE);
											tableColumn4.setText("Mob type");
											tableColumn4.setWidth(120);
										}

									}
								}
							}
							{
								tbi_items = new CTabItem(envTab, SWT.NONE);
								tbi_items.setText("Items");
								tbi_items.setImage(SWTResourceManager.getImage("res_images/items.png"));
								{
									cpItems = new Composite(envTab, SWT.NONE);
									FillLayout cpItemsLayout = new FillLayout(
										org.eclipse.swt.SWT.HORIZONTAL);
									cpItems.setLayout(cpItemsLayout);
									tbi_items.setControl(cpItems);
									{
										gridItems = new Table(cpItems, SWT.SINGLE
												| SWT.FULL_SELECTION
												| SWT.H_SCROLL
												| SWT.V_SCROLL
												| SWT.BORDER);
										gridItems.setHeaderVisible(true);
										gridItems.setLinesVisible(true);
										gridItems
											.addMouseListener(new MouseAdapter() {
												public void mouseDoubleClick(
													MouseEvent evt) {
													gridItemsMouseDoubleClick(evt);
												}
											});
										{
											tableColumn1 = new TableColumn(
												gridItems,
												SWT.NONE);
											tableColumn1.setText("Item");
											tableColumn1.setWidth(150);
										}
										{
											tableColumn2 = new TableColumn(
												gridItems,
												SWT.NONE);
											tableColumn2.setText("Count");
											tableColumn2.setWidth(80);
										}
									}
								}
							}
							{
								tbi_players = new CTabItem(envTab, SWT.NONE);
								tbi_players.setText("Players");
								tbi_players.setImage(SWTResourceManager.getImage("res_images/players.png"));
								{
									cpPlayers = new Composite(
											envTab,
										SWT.NONE);
									tbi_players.setControl(cpPlayers);
									FillLayout cpPlayersLayout = new FillLayout(
										org.eclipse.swt.SWT.HORIZONTAL);
									cpPlayers.setLayout(cpPlayersLayout);
									{
										Menu userpopUpMenu = new Menu(getShell(),SWT.POP_UP);
										MenuItem requestTrade = new MenuItem(userpopUpMenu,SWT.PUSH);
										requestTrade.setText("Request Trade");
										requestTrade.addListener (SWT.Selection, new Listener () {
											public void handleEvent (Event e) {
												procRequestTrade();
											}
										});

										MenuItem evaluate = new MenuItem(userpopUpMenu,SWT.PUSH);
										evaluate.setText("Evaluate");
										evaluate.addListener (SWT.Selection, new Listener () {
											public void handleEvent (Event e) {

											}
										});


										gridPlayers = new Table(
											cpPlayers,
											SWT.SINGLE
												| SWT.FULL_SELECTION
												| SWT.H_SCROLL
												| SWT.V_SCROLL
												| SWT.BORDER);
										gridPlayers.setHeaderVisible(true);
										gridPlayers.setLinesVisible(true);
										gridPlayers.setMenu(userpopUpMenu);
										gridPlayers
											.addMouseListener(new MouseAdapter() {
												public void mouseDoubleClick(
													MouseEvent evt) {
													gridPlayresMouseDoubleClick(evt);
												}
											});
										{
											TableColumn columPlayerName = new TableColumn(
												gridPlayers,
												SWT.NONE);
											columPlayerName
												.setText("Player name");
											columPlayerName.setWidth(120);

											TableColumn columPlayerRace = new TableColumn(
												gridPlayers,
												SWT.NONE);
											columPlayerRace
												.setText("Player Class");
											columPlayerRace.setWidth(120);

											TableColumn columPlayerClan = new TableColumn(
												gridPlayers,
												SWT.NONE);
											columPlayerClan
												.setText("Player Clan");
											columPlayerClan.setWidth(120);
										}
									}
								}
							}
							{
								tbi_npcs = new CTabItem(envTab, SWT.NONE);
								tbi_npcs.setText("NPC");
								tbi_npcs.setImage(SWTResourceManager.getImage("res_images/Delphi.png"));
								{
									cpNpcs = new Composite(envTab, SWT.NONE);
									tbi_npcs.setControl(cpNpcs);
									FillLayout composite1Layout1 = new FillLayout(
										org.eclipse.swt.SWT.HORIZONTAL);
									cpNpcs.setLayout(composite1Layout1);
									{
										gridNpcs = new Table(cpNpcs, SWT.SINGLE
											| SWT.FULL_SELECTION
											| SWT.H_SCROLL
											| SWT.V_SCROLL
											| SWT.BORDER);
										gridNpcs.setHeaderVisible(true);
										gridNpcs.setLinesVisible(true);
										gridNpcs
											.addMouseListener(new MouseAdapter() {
												public void mouseDoubleClick(
													MouseEvent evt) {
													gridNpcsMouseDoubleClick(evt);
												}
											});
										{
											tableColumn5 = new TableColumn(
												gridNpcs,
												SWT.NONE);
											tableColumn5.setText("Npc name");
											tableColumn5.setWidth(200);
										}
										{
											tableColumn6 = new TableColumn(
												gridNpcs,
												SWT.NONE);
											tableColumn6.setText("Npc type");
											tableColumn6.setWidth(120);
										}
									}
								}
							}
							envTab.setSelection(0);
							envTab.setSimple(false);
						}
					}
					cpArea1.setWeights(new int[]
					{
							700, 300
					});
				}
				this.addControlListener(new ControlAdapter()
				{
					public void controlResized(ControlEvent evt)
					{
						rootControlResized(evt);
					}
				});

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void rootControlResized(ControlEvent evt)
	{

	}

	private void btnLoginWidgetSelected(SelectionEvent evt)
	{
		if(edLogin.getText().length() > 0 && edPassword.getText().length() > 0){
			btnLogin.setEnabled(false);
			btnLogout.setEnabled(true);
			ServerConfig serverConfig = mapServersConfig.get(cbServer.getText());
			int serverNum = Integer.parseInt(cbHostNum.getText().trim());
			int charNum = Integer.parseInt(cbCharNum.getText().trim());
			gameEngine.doLogin(edLogin.getText(), edPassword.getText(), serverConfig.hostLogin, serverConfig.port, serverConfig.protocol, serverNum, charNum);
		}
		else{
			 MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.ICON_WARNING);
				 messageBox.setMessage("Please enter username and password");
				 messageBox.open();
		}
	}

	private void putChatMessage(final String msg, final Color color, final boolean bold)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					textChat.setRedraw(false);
					textChat.append(msg);
					textChat.append("\n");

					StyleRange styleRange = new StyleRange();
					styleRange.start = textChat.getCharCount() - msg.length() - 1;
					styleRange.length = msg.length();

					if (bold)
						styleRange.fontStyle = SWT.BOLD;
					else
						styleRange.fontStyle = SWT.NORMAL;

					styleRange.foreground = color;
					textChat.setStyleRange(styleRange);

					if (textChat.getCharCount() >= 1000)
					{
						textChat.setSelection(0, 1000 - textChat.getCharCount());
						textChat.cut();
					}

					textChat.setRedraw(true);

					textChat.setSelection(textChat.getCharCount() - 1);
					textChat.showSelection();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void putSystemMessage(final String msg, final Color color, final boolean bold)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					textChat.setRedraw(false);
					textChat.append(msg);
					textChat.append("\n");

					StyleRange styleRange = new StyleRange();
					styleRange.start = textChat.getCharCount() - msg.length() - 1;
					styleRange.length = msg.length();

					if (bold)
						styleRange.fontStyle = SWT.BOLD;
					else
						styleRange.fontStyle = SWT.NORMAL;

					styleRange.foreground = color;
					textChat.setStyleRange(styleRange);

					if (textChat.getCharCount() >= 1000)
					{
						textChat.setSelection(0, 1000 - textChat.getCharCount());
						textChat.cut();
					}

					textChat.setRedraw(true);

					textChat.setSelection(textChat.getCharCount() - 1);
					textChat.showSelection();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void putCombatMessage(final String msg, final Color color, final boolean bold)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					textCombatMessages.setRedraw(false);
					textCombatMessages.append(msg);
					textCombatMessages.append("\n");

					StyleRange styleRange = new StyleRange();
					styleRange.start = textCombatMessages.getCharCount() - msg.length() - 1;
					styleRange.length = msg.length();

					if (bold)
						styleRange.fontStyle = SWT.BOLD;
					else
						styleRange.fontStyle = SWT.NORMAL;

					styleRange.foreground = color;
					textCombatMessages.setStyleRange(styleRange);

					if (textCombatMessages.getCharCount() >= 1000)
					{
						textCombatMessages.setSelection(0, 1000 - textCombatMessages.getCharCount());
						textCombatMessages.cut();
					}

					textCombatMessages.setRedraw(true);

					textCombatMessages.setSelection(textCombatMessages.getCharCount() - 1);
					textCombatMessages.showSelection();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void setBtnLoginEnabled(final boolean value)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					btnLogin.setEnabled(value);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void setBtnLogoutEnabled(final boolean value)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					btnLogout.setEnabled(value);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void setTabName(final String value)
	{

		parentTabItem.setText(value);

	}

	private void clearForLogout()
	{
		btnLogout.setEnabled(false);
		putSystemMessage("LOGOUT", colors.colorSystemAtention, true);
		//putChatMessage("LOGOUT", colors.colorSystemAtention, true);
		visualEfectsTimer.removeAll();
		skillAreaActive.removeAll();
		skillAreaPassive.removeAll();
		partyArea.removeAll();
		inventoryAreaNormal.removeAll();
		gridPlayers.removeAll();
		gridMobs.removeAll();
		gridNpcs.removeAll();
		gridItems.removeAll();
		targetArea.removeTargetChar();
		user_life_area.removeUserChar();
		if(myTradeDialog != null)
			myTradeDialog.closeTradeDialog();

		UserMainArea.this.setBtnLogoutEnabled(false);
		UserMainArea.this.setBtnLoginEnabled(true);
	}

	private void btnLogoutWidgetSelected(SelectionEvent evt)
	{
		//btnLogout.setEnabled(false);
		//gameEngine.setLogout();
 		gameEngine.logOut();
	}

	public CTabItem getParentTabItem()
	{
		return parentTabItem;
	}

	public void setParentTabItem(CTabItem parentTabItem)
	{
		this.parentTabItem = parentTabItem;
	}

	private void edChatSendKeyPressed(KeyEvent evt)
	{

		if (evt.keyCode == 13 || evt.keyCode == 16777296)
		{
			evt.doit = false;
			gameEngine.sendMessage(cbChatType.getText(), edChatSend.getText(), cbChatTarget.getText().trim());
			edChatSend.setText("");
		}
	}

	private void cbChatTypeWidgetSelected(SelectionEvent evt)
	{
		if (cbChatType.getText().equalsIgnoreCase("TELL"))
			cbChatTarget.setVisible(true);
		else
			cbChatTarget.setVisible(false);
	}

	public void setDbObjects(DbObjects dbObjects)
	{
		gameEngine.setDbObjects(dbObjects);
	}

	private void btnLeavePartyWidgetSelected(SelectionEvent evt)
	{
		try
		{
			gameEngine.sendLeaveParty();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void procLogoutEvent()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					clearForLogout();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void putMessage(String msg, int msg_type, boolean bold)
	{
		switch (msg_type)
		{
		case GameEngine.MSG_SYSTEM_NORMAL:
			putSystemMessage(Printer.getCurrDate()+msg, colors.colorMsgNormal, bold);
			break;

		case GameEngine.MSG_SYSTEM_FAIL:
			putSystemMessage(Printer.getCurrDate()+msg, colors.colorSystemFail, bold);
			break;

		case GameEngine.MSG_SYSTEM_SUCESS:
			putSystemMessage(Printer.getCurrDate()+msg, colors.colorSystemSucess, bold);
			break;

		case GameEngine.MSG_SYSTEM_ATENTION:
			putSystemMessage(Printer.getCurrDate()+msg, colors.colorSystemAtention, bold);
			break;

		case GameEngine.MSG_COMBAT:
			putCombatMessage(msg, colors.colorCombat, bold);
			break;

		case GameEngine.MSG_CHAT_NORMAL:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgNormal, bold);
			break;

		case GameEngine.MSG_CHAT_ALL:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgAll, bold);
			break;

		case GameEngine.MSG_ANUNCEMENT:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorAnuncement, bold);
			break;

		case GameEngine.MSG_TELL:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgPrivate, bold);
			break;

		case GameEngine.MSG_TRADE:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgTrade, bold);
			break;

		case GameEngine.MSG_PARTY:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgParty, bold);
			break;

		case GameEngine.MSG_CLAN:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgClan, bold);
			break;

		case GameEngine.MSG_ALLY:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgAlly, bold);
			break;

		case GameEngine.MSG_HERO:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgHero, bold);
			break;

		default:
			putChatMessage(Printer.getCurrDate()+msg, colors.colorMsgNormal, bold);
			break;
		}

	}

	public void requestPartyDialog(String requestor, String partyType)
	{
		RequestPartyDaialog.createDialog(this, "[" + requestor + "]" + " request to join party "+"("+partyType+")");
	}

	public void requestHtmlDialog(String msg)
	{
		RequestHtmlDialog.main(this, msg);
	}

	public void requestTradeDialog(final L2Item items[])
	{
		RequestTradeDialog.createDialog(this, items);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myTradeDialog.addMyItems();

	}

	public void requestTrade(final L2Char tradeChar)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					MessageBox messageBox = new MessageBox(getShell(), SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
					messageBox.setText(tradeChar.realName+" - trade request");
					messageBox.setMessage("Do you accept?");
						if (messageBox.open() == SWT.OK){
							gameEngine.sendAnswerTradeRequest(1);
						}
						else{
							gameEngine.sendAnswerTradeRequest(0);
						}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void requestHeroesListDialog(Heroes[] heroesList, int size)
	{
		RequestHeroesListDialog.main(this, heroesList, size);
	}

	public void setMapServersConfig(FastMap<String, ServerConfig> mapServersConfig)
	{
		this.mapServersConfig = mapServersConfig;

		cbServer.removeAll();

		for (Iterator<ServerConfig> iter = mapServersConfig.values().iterator(); iter.hasNext();)
		{
			ServerConfig serverConfig = (ServerConfig) iter.next();
			cbServer.add(serverConfig.name);
		}

		cbServer.select(0);
		cbHostNum.setItems(mapServersConfig.get(cbServer.getText()).gameServers);
		cbHostNum.select(0);

	}

	private void cbServerWidgetSelected(SelectionEvent evt)
	{
		ServerConfig serverConfig = mapServersConfig.get(cbServer.getText());
		cbHostNum.removeAll();
		cbHostNum.setItems(serverConfig.gameServers);
		cbHostNum.select(0);
	}

	public void procSetUserChar(final UserChar userChar)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					user_life_area.setUserChar(userChar);
					setTabName("[" + userChar.realName + "]");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	public void procPlayerChar(final PlayerChar playerChar)
	{
		if (playerChar.visualObject == null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						L2CharVisualObject visualObject = new L2CharVisualObject();
						visualObject.tableItemChar = new TableItem(gridPlayers, SWT.NONE);
						visualObject.tableItemChar.setData(playerChar);
						visualObject.tableItemChar.setText(new String[]
						{
								playerChar.name, playerChar.className, playerChar.clanInfo.clanName
						});
						playerChar.visualObject = visualObject;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void procAddPartyChars(final L2PartyChar[] l2PartyChars)
	{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{

						partyArea.addPartyChars(gameEngine, l2PartyChars);

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

	}

	public void procUpdatePartyChar(final L2PartyChar l2PartyChar)
	{
		if(l2PartyChar.visualObject==null)return;


			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						((L2PartyCharVisualObject)l2PartyChar.visualObject).partyAreaItem.updateL2PartyChar();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});


	}

	public void procDeletePartyChar(final L2PartyChar l2PartyChar)
	{
		if (l2PartyChar.visualObject != null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						L2PartyCharVisualObject visualObject = (L2PartyCharVisualObject) l2PartyChar.visualObject;
						if (visualObject != null)
						{
							visualObject.partyAreaItem.removeL2PartyChar();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void procDeleteAllPartyChars( )
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					partyArea.removeAll();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procNpcChar(final NpcChar npcChar)
	{
		if (npcChar.visualObject == null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						String npcType = npcChar.dbl2npc.getType();
						Table grid = null;
						if (npcType.equalsIgnoreCase("Monster") || npcType.equalsIgnoreCase("Minion") || npcType.equalsIgnoreCase("FriendlyMob") || npcType.equalsIgnoreCase("RaidBoss"))
						{
							grid = gridMobs;
						} else
						{
							grid = gridNpcs;
						}

						L2CharVisualObject visualObject = new L2CharVisualObject();
						visualObject.tableItemChar = new TableItem(grid, SWT.NONE);
						visualObject.tableItemChar.setData(npcChar);
						visualObject.tableItemChar.setText(new String[]
						{
								npcChar.realName, npcChar.dbl2npc.getType()
						});

						npcChar.visualObject = visualObject;

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void procAddEnvItem(final L2Item item)
	{
		if (item.visualObject == null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						Table grid = null;
						grid = gridItems;

						L2ItemVisualObject visualObject = new L2ItemVisualObject();
						visualObject.tableItemChar = new TableItem(grid, SWT.NONE);
						visualObject.tableItemChar.setData(item);
						visualObject.tableItemChar.setText(new String[]
						{
								item.dbL2Item.getName(), String.valueOf(item.itemCount)
						});

						item.visualObject = visualObject;

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void procPlayerCharClanInfo(final PlayerChar playerChar)
	{
		if (playerChar.visualObject != null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						L2CharVisualObject visualObject = (L2CharVisualObject) playerChar.visualObject;
						visualObject.tableItemChar.setText(new String[]
						{
								playerChar.name, playerChar.className, playerChar.clanInfo.clanName
						});
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void gridPlayresMouseDoubleClick(MouseEvent evt)
	{
		if (gridPlayers.getSelectionCount() == 0)
			return;
		TableItem tableItem = gridPlayers.getSelection()[0];
		PlayerChar playerChar = (PlayerChar) tableItem.getData();
		gameEngine.sendAction(playerChar.objId);
	}

	private void procRequestTrade()
	{
		if (gridPlayers.getSelectionCount() == 0)
			return;
		TableItem tableItem = gridPlayers.getSelection()[0];
		PlayerChar playerChar = (PlayerChar) tableItem.getData();
		gameEngine.requestTrade(playerChar.objId);
	}
	
	public boolean checkTradeDialog()
	{
		if (myTradeDialog != null)
			return true;
		return false;
	}

	public void procMyTargetSelected(final L2Char targetChar)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					targetArea.setTargetChar(targetChar);
					if (targetChar instanceof PlayerChar)
					{
						edPartyTarget.setText(targetChar.realName);
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procMyTargetUnselected()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					targetArea.removeTargetChar();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procDeleteL2char(final L2Char l2Char)
	{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						L2CharVisualObject visualObject = (L2CharVisualObject) l2Char.visualObject;
						visualObject.tableItemChar.dispose();
						visualObject.tableItemChar = null;

						l2Char.visualObject = null;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
	}

	public void procDeleteItem(final L2Item item)
	{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					try
					{
						L2ItemVisualObject visualObject = (L2ItemVisualObject) item.visualObject;
						visualObject.tableItemChar.dispose();
						visualObject.tableItemChar = null;

						item.visualObject = null;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
	}


	public void procUpdateTargetcharStatus(final L2Char targetChar)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					targetArea.updateHP();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	public void procUpdateUsercharStatus(final UserChar userChar)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					user_life_area.updateStatus();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procAddSkill(final L2Skill l2Skill)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					SkillArea skillArea = null;
					if (l2Skill.passive)
						skillArea = skillAreaPassive;
					else
						skillArea = skillAreaActive;

					L2UserSkillVisualObject visualObject = new L2UserSkillVisualObject();
					visualObject.skillAreaItem = skillArea.newItem(gameEngine, visualEfectsTimer, l2Skill);
					l2Skill.visualObject = visualObject;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	public void procRemoveSkill(final L2Skill l2Skill)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					L2UserSkillVisualObject visualObject = (L2UserSkillVisualObject) l2Skill.visualObject;
					if (visualObject != null)
					{
						visualObject.skillAreaItem.removeL2Skill();
						visualObject.skillAreaItem = null;
						l2Skill.visualObject = null;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procSelfSkillUse(final L2Skill l2Skill, final L2SkillUse l2SkillUse)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					L2UserSkillVisualObject visualObject = (L2UserSkillVisualObject) l2Skill.visualObject;
					if (visualObject != null)
					{
						visualObject.skillAreaItem.skillUse(l2SkillUse);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procAddItems(final L2Item  items[])
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					 inventoryAreaNormal.addItems(gameEngine,items);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


	public void procUpdateItems(final L2Item  items[])
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					  inventoryAreaNormal.updateItems(gameEngine,items);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procDeletItems(final L2Item  items[])
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					  inventoryAreaNormal.deleteItems(gameEngine,items);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procTeleportClear()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					gridPlayers.removeAll();
					gridMobs.removeAll();
					gridNpcs.removeAll();
					targetArea.removeTargetChar();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procSendTradeDone()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					myTradeDialog.closeTradeDialog();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void procTradeAddOwnItem(final L2Item item)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					myTradeDialog.addOwnItem(item);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


	public void procTradeAddOtherItem(final L2Item item)
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					myTradeDialog.addOtherItem(item);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public void procConfirmedTrade()
	{
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				try
				{
					myTradeDialog.confirmedTrade();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void targetAreaKeyPressed(KeyEvent evt)
	{
		if (evt.keyCode == 27)
		{
			gameEngine.sendCancelTarget();
		}
	}

	private void btnInvitePartyWidgetSelected(SelectionEvent evt)
	{
		gameEngine.sendPartyInvite(edPartyTarget.getText(), 0);
	}

	private void gridMobsMouseDoubleClick(MouseEvent evt)
	{
		if (gridMobs.getSelectionCount() == 0)
			return;
		TableItem tableItem = gridMobs.getSelection()[0];
		NpcChar npcChar = (NpcChar) tableItem.getData();
		gameEngine.sendAction(npcChar.objId);
	}

	private void gridNpcsMouseDoubleClick(MouseEvent evt)
	{
		if (gridNpcs.getSelectionCount() == 0)
			return;
		TableItem tableItem = gridNpcs.getSelection()[0];
		NpcChar npcChar = (NpcChar) tableItem.getData();
		gameEngine.sendAction(npcChar.objId);
	}

	private void gridItemsMouseDoubleClick(MouseEvent evt)
	{
		if (gridItems.getSelectionCount() == 0)
			return;
		TableItem tableItem = gridItems.getSelection()[0];
		L2Item myItem = (L2Item) tableItem.getData();
		gameEngine.sendAction(myItem.objectId);
	}

	private void targetAreaMouseDoubleClick(MouseEvent evt)
	{
		System.out.println("UserMainArea.targetAreaMouseDoubleClick()");
		L2Char l2Char = targetArea.getTargetChar();
		if (l2Char != null)
			gameEngine.sendAction(l2Char.objId);
	}

	private void user_life_areaMouseDoubleClick(MouseEvent evt)
	{
		UserChar userChar = user_life_area.getUserChar();
		if (userChar != null)
			gameEngine.sendAction(userChar.objId);
	}

	private void tab_informationsWidgetSelected(SelectionEvent evt)
	{
		if (tab_informations.getSelection() == tbi_skills)
		{
			gameEngine.sendRequestSkillList();
			return;
		}
	}

}
