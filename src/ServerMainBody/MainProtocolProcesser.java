
package ServerMainBody;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import GameUtilInformation.CatchmeBoardStatues;
import GameUtilInformation.TicTacTocPoint;
import Information.ClientAccessInformation;
import Information.ClientGameInformation;
import PangPang.Map_Controler;
import PangPang.PangPangEnemy;
import PangPang.AttackEnemy;

import Utility.EncryptionManager;
import Utility.SplitPacketManager;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class manage the Server and Database System.
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */

public class MainProtocolProcesser implements Initializable {
	/**
	 * this boardPane is the main borderPane
	 */
	@FXML
	private BorderPane login;

	/**
	 * this button's function is that Return to Main Logo
	 */
	@FXML
	private Button btnMain;

	/**
	 * this TextArea For View The Message about Server Message from clients
	 */
	@FXML
	private TextArea textArea;

	/**
	 * this list view for the connected clients list for the Main Server
	 */
	@FXML
	private ListView<String> clientListView;

	@FXML
	private javafx.scene.control.TextField serverTextEdit;

	/**
	 * this button for server Start or Stop
	 */
	private Button btnStartStop;

	/**
	 * this executorService for managing the runnable events
	 */
	private ExecutorService executorService;

	/**
	 * this variable for the server connection
	 */
	private ServerSocket serverSocket;

	/**
	 * this variable for connected clients
	 */
	private List<Client> connections = new Vector<Client>();

	/**
	 * this variable for the Created Game room by client(Manager)
	 */
	private List<GameRoom> gameRooms = new Vector<MainProtocolProcesser.GameRoom>();

	private List<UniverseGame> universeGames = new Vector<MainProtocolProcesser.UniverseGame>();

	/**
	 * this variable for the listView(Clients)
	 */
	private ObservableList<String> items;

	/**
	 * this executorService for managing the runnable events
	 */
	private ScheduledExecutorService scheduler;

	/**
	 * client default id
	 */
	private int numbering;

	/**
	 * this variable for SQL statement
	 */
	private java.sql.Statement stmt;

	/**
	 * this variable for dialog event
	 */
	private Stage dialog;

	/**
	 * this variable for PieChart dialog main subject string
	 */
	private String piechartClientName;

	private boolean isServerExit;

	private int nExitCount;

	private int nCommandIndicatorPoisition;

	private int nCommandsContainerIndicator;

	private String[] sCommandsContainer;

	private boolean isMakeSudoId;

	private boolean isSudoIdLogin;

	private String sSudoID;

	private String sSudoPassword;

	private String sEmailAddress;

	private boolean isSudodeleteEventOccured;

	private String sSudoDeleteUserID;

	private boolean isSudoIdDelete;

	int _firstPoistion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle) init the Initializable and previous defined
	 * variables;
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isSudodeleteEventOccured = false;
		isSudoIdDelete = false;
		sCommandsContainer = new String[Settings.nMaximumSizeOfCommandsContainer];
		// init the client default id start number
		numbering = Settings.ZEROINIT;
		nExitCount = Settings.ZEROINIT;
		nCommandIndicatorPoisition = Settings.ZEROINIT;
		nCommandsContainerIndicator = Settings.ZEROINIT;
		isMakeSudoId = false;
		isSudoIdLogin = false;

		items = FXCollections.observableArrayList();
		clientListView.setItems(items);
		// set the view list style
		clientListView.setStyle(
				"-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color;-fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");
		// register the click listener about clientListView
		clientListView.setOnMouseClicked(event -> handleTableViewMouseClicked(event));
		// txtArea Can not Edit
		textArea.setEditable(false);
		// txtArea's Style
		textArea.setStyle(
				"-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color;-fx-background-insets: 0, 1, 2;-fx-background-radius: 5, 4, 3;");

		// btnStartStop's Strint is Start
		btnStartStop = new Button("start");
		btnStartStop.setPrefHeight(30);
		btnStartStop.setMaxWidth(Double.MAX_VALUE);
		// register the click listener about btnStartStop
		btnStartStop.setOnAction(e -> {
			// if btnStartStop's String same 'Start' then StartServer Function
			// Run
			if (btnStartStop.getText().equals("start")) {
				startServer();
				// else start The Stop Server
			} else if (btnStartStop.getText().equals("stop")) {
				stopServer();
			}
		});

		// Initialize the database statues
		try {
			Connection con = null;

			// set the JDBC URL and connection id and password
			con = DriverManager.getConnection(Settings.sDataBaseSystemUrl, Settings.sDatabaseSystemRoot,
					Settings.sDatabaseSystemPassword);

			// Initialize the statements
			stmt = null;

			// allocated the Statement
			stmt = con.createStatement();

			// use the default test database system

			initUsingMainDB(Settings.DBName);

			initCreateUserinfoSchma();

			initCreateGameDBSchema();

			// if error occur then this exception occur
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}

		// if success the database connection then this print show the message
		System.out.println("Database Connection Success");

		// set the event about login panel
		login.setBottom(btnStartStop);

		// register the event handler
		btnMain.setOnAction(e -> handleBtnMain(e));

		serverTextEdit.addEventFilter(KeyEvent.KEY_TYPED, message_text_Validation(Settings.nServerMaxCommandLenth));
		serverTextEdit.setOnKeyPressed(e -> handleBtnKeyEvent(e));
		isServerExit = false;
		readSudoUserinformationFromFile();
	}

	private void initUsingMainDB(String A) throws SQLException {
		stmt.executeQuery("use " + A);
	}

	private void initCreateUserinfoSchma() throws SQLException {
		ResultSet rs;
		/*
		 * if user information table is not existed in the database system. then
		 * this application make the user information table
		 */
		rs = stmt.executeQuery("show tables from test like'userinformation'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE userinformation " + "(id varchar(10) not NULL, "
					+ " password VARCHAR(10) not Null, " + " PRIMARY KEY ( id )) Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}

		/*
		 * if user IP and access table is not existed in the database system.
		 * then this application make the user IP and access table
		 */
		rs = stmt.executeQuery("show tables from test like'useripandaccess'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE useripandaccess " + "(id varchar(10) not NULL, "
					+ "ipaddress varchar(30) not NULL, " + "accessAndTerminateTime datetime not null,"
					+ "type varchar(10)" + ") Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}
	}

	private void initCreateGameDBSchema() throws SQLException {
		ResultSet rs;
		/*
		 * TicTacToc and CatchMe must make at after make the user information
		 * Because the two tables have foreign key if TicTacToc table is not
		 * existed in the database system. then this application make the
		 * TicTacToc table
		 */
		rs = stmt.executeQuery("show tables from test like'tictactoc'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE tictactoc " + "(id varchar(10) not NULL, " + " win integer(1), "
					+ " defeat integer(1), " + " playtimes integer(1), " + " PRIMARY KEY ( id ),"
					+ "FOREIGN KEY (id) REFERENCES userinformation (id)) Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}

		/*
		 * if catchMe table is not existed in the database system. then this
		 * application make the catchMe table
		 */
		rs = stmt.executeQuery("show tables from test like'catchme'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE catchme " + "(id varchar(10) not NULL, " + " win integer(1), "
					+ " defeat integer(1), " + " playtimes integer(1), " + " PRIMARY KEY ( id ),"
					+ "FOREIGN KEY (id) REFERENCES userinformation (id)) Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}

		/*
		 * if catchMe table is not existed in the database system. then this
		 * application make the catchMe table
		 */
		rs = stmt.executeQuery("show tables from test like'meteor'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE meteor " + "(id varchar(10) not NULL, " + " win integer(1), "
					+ " defeat integer(1), " + " playtimes integer(1), " + " PRIMARY KEY ( id ),"
					+ "FOREIGN KEY (id) REFERENCES userinformation (id)) Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}

		// pangpang score saving position.
		rs = stmt.executeQuery("show tables from test like'pangpang'");
		if (false == rs.next()) {
			String sql = "CREATE TABLE pangpang " + "(id varchar(10) not NULL, " + " win integer(1), "
					+ " defeat integer(1), " + " playtimes integer(1), " + " score integer(10), "
					+ " PRIMARY KEY ( id ),"
					+ "FOREIGN KEY (id) REFERENCES userinformation (id)) Engine=Innodb default charset = utf8";

			stmt.executeUpdate(sql);
		}
	}

	public EventHandler<KeyEvent> message_text_Validation(final Integer max_Lengh) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {

				javafx.scene.control.TextField txt_TextField = (javafx.scene.control.TextField) e.getSource();
				if (txt_TextField.getText().length() >= max_Lengh) {
					e.consume();
				}

			}
		};
	}

	public void handleBtnKeyEvent(KeyEvent e) {
		switch (e.getCode()) {
		case ENTER:
			processCommand();
			break;

		case BACK_SPACE:
			break;

		case UP:
			if (nCommandIndicatorPoisition != nCommandsContainerIndicator
					&& nCommandIndicatorPoisition > Settings.ERRORCODE)
				setServerCommandTextEdit();

			if (nCommandIndicatorPoisition != nCommandsContainerIndicator)
				nCommandIndicatorPoisition--;

			if (nCommandIndicatorPoisition < 0
					&& sCommandsContainer[Settings.nMaximumSizeOfCommandsContainer - 1] != null)
				nCommandIndicatorPoisition = Settings.nMaximumSizeOfCommandsContainer - 1;

			break;
		case DOWN:
			if (nCommandIndicatorPoisition != _firstPoistion)
				nCommandIndicatorPoisition++;

			if (nCommandIndicatorPoisition >= Settings.nMaximumSizeOfCommandsContainer
					&& sCommandsContainer[Settings.ZEROINIT] != null)
				nCommandIndicatorPoisition = Settings.ZEROINIT;

			if (nCommandIndicatorPoisition != nCommandsContainerIndicator
					&& nCommandIndicatorPoisition > Settings.ERRORCODE)
				setServerCommandTextEdit();

			break;

		case LEFT:
			break;

		case RIGHT:
			break;

		default:
			e.consume();
			break;
		}
	}

	private void setServerCommandTextEdit() {
		Platform.runLater(() -> serverTextEdit.setText(sCommandsContainer[nCommandIndicatorPoisition]));
		Platform.runLater(() -> serverTextEdit.positionCaret(serverTextEdit.getLength()));

	}

	private Thread temp;

	private void processCommand() {
		String command = serverTextEdit.getText();

		{
			sCommandsContainer[nCommandsContainerIndicator] = command;
			nCommandIndicatorPoisition = nCommandsContainerIndicator;
			_firstPoistion = nCommandIndicatorPoisition;
			nCommandsContainerIndicator++;

			if (nCommandsContainerIndicator >= Settings.nMaximumSizeOfCommandsContainer)
				nCommandsContainerIndicator = Settings.ZEROINIT;
		}

		if (command.length() > 0) {
			String sCommandWords[];

			String query;

			ResultSet rs;

			Platform.runLater(() -> serverTextEdit.clear());

			sCommandWords = parserOfCommandSyntax(command);

			boolean existId;

			if (sCommandWords == null) {
				invalidShellCommand();
				return;
			}

			if (isSudodeleteEventOccured == true) {
				switch (sCommandWords[0]) {
				case "yes":
					try {
						query = "delete from catchme where id='" + sSudoDeleteUserID + "'";

						stmt.executeUpdate(query);
						query = "delete from tictactoc where id='" + sSudoDeleteUserID + "'";
						stmt.executeUpdate(query);

						query = "delete from userinformation where id='" + sSudoDeleteUserID + "'";
						stmt.executeUpdate(query);

						query = "delete from useripandaccess where id='" + sSudoDeleteUserID + "'";
						stmt.executeUpdate(query);
						Platform.runLater(() -> displayText("command user[" + sSudoDeleteUserID + "] remove success"));
						sSudoDeleteUserID = null;
						isSudodeleteEventOccured = false;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					Platform.runLater(() -> displayText("command user remove cancel"));
					sSudoDeleteUserID = null;
					isSudodeleteEventOccured = false;
					break;
				}

			}
			if (isSudoIdDelete == true) {
				switch (sCommandWords[0]) {
				case "yes":
					sSudoID = null;
					sSudoPassword = null;
					sEmailAddress = null;
					isMakeSudoId = false;
					writeFileClean();
					Platform.runLater(() -> displayText("Sudo ID deleting is success."));
					break;

				default:
					Platform.runLater(() -> displayText("Sudo ID deleting is canceled."));
					break;

				}

			} else
				switch (sCommandWords[0]) {
				case "exit":
					switch (sCommandWords[1]) {
					case "-help":
						Platform.runLater(() -> displayText("exit command list : default, -no"));
						break;
					case " ":
						Platform.runLater(() -> displayText("exit javafx game server"));
						isServerExit = true;
						break;

					case "-no":
						Platform.runLater(() -> displayText("cancel the exit javafx game server"));
						isServerExit = false;
						nExitCount = 0;
						break;

					default:
						invalidShellCommand();
						break;
					}
					break;

				// processing log -saving function - log event time
				case "log":
					// textArea
					break;

				// user command <-- manage connection user
				case "user":
					switch (sCommandWords[1]) {
					case "-help":
						Platform.runLater(() -> displayText(
								"user command list : -info, -all, -access username type(Start,Finish, -all)"));
						break;
					case " ":
					case "-info":
						if (connections.size() <= 0)
							Platform.runLater(() -> displayText("No User Access The Server"));
						else {
							Platform.runLater(() -> displayText("Name - IP - AccessTime"));
							for (Client client : connections)
								Platform.runLater(() -> displayText(client.getClientName() + " "
										+ client.getClientIpAddress() + " " + client.getdAccessTime()));
						}
						break;

					case "-all":
						// database all user
						if (true == isSudoIdLogin) {
							query = "SELECT id FROM userinformation";
							try {
								rs = stmt.executeQuery(query);
								while (rs.next()) {
									String userName = rs.getString("id");

									Platform.runLater(() -> displayText(userName + ""));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else
							Platform.runLater(() -> displayText("you need to login using your sudo ID"));
						break;

					// -access username type(Start,Finish, -all)
					case "-access":
						// database all user
						if (true == isSudoIdLogin) {
							if (sCommandWords[3].equals("start") || sCommandWords[3].equals("finish")) {
								query = "SELECT * FROM useripandaccess where id='" + sCommandWords[2] + "' and type='"
										+ sCommandWords[3] + "'";
								Platform.runLater(() -> displayText("username ipaddress accessAndTerminateTime"));
							} else if (sCommandWords[3].equals("all")) {
								query = "SELECT * FROM useripandaccess where id='" + sCommandWords[2] + "'";
								Platform.runLater(() -> displayText("username ipaddress accessAndTerminateTime Type"));
							} else {
								invalidShellParameter();
								break;
							}
							try {
								rs = stmt.executeQuery(query);
								while (rs.next()) {
									String userName = rs.getString("id");
									String ipaddress = rs.getString("ipaddress");
									String accessAndTerminateTime = rs.getString("accessAndTerminateTime");
									String type = rs.getString("type");
									if (sCommandWords[3].equals("start") || sCommandWords[3].equals("finish"))
										Platform.runLater(() -> displayText(
												userName + " " + ipaddress + " " + accessAndTerminateTime));
									else if (sCommandWords[3].equals("all"))
										Platform.runLater(() -> displayText(userName + " " + ipaddress + " "
												+ accessAndTerminateTime + " " + type));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else
							Platform.runLater(() -> displayText("you need to login using your sudo ID"));

						break;

					default:
						invalidShellCommand();
						break;
					}
					break;

				// help command
				case "help":
					switch (sCommandWords[1]) {
					case " ":
					case "-all":
						Platform.runLater(
								() -> displayText("command list : user, sudo, log, exit, create, delete, protocol "));
						break;
					case "-user":
						Platform.runLater(() -> displayText(
								"-help <show The -user command info>, -all <all userID including DB>, -info <access the user>, -access username start or finish <show the all user type including DB>"));
						break;
					case "-sudo":
						break;
					case "-log":
						break;
					case "-exit":
						Platform.runLater(() -> displayText(
								"-help <show The -exit command info>, -no <cancel terminate server>, -default<terminate the server>"));
						break;
					case "-create":
						break;
					case "-delete":
						break;
					case "-protocol":
						break;
					default:
						invalidShellCommand();
						break;
					}
					break;

				// sudo function related on database system, and network
				case "sudo":
					switch (sCommandWords[1]) {
					case "-help":
						Platform.runLater(() -> displayText(
								"sudo command list : -register, -login, -create, -remove, -modify, -userinfo"));
						break;
					case " ":
						final String temp1 = (isMakeSudoId == true) ? "SUDO ID exist" : "SUDO ID not exist";
						Platform.runLater(() -> displayText(temp1 + ""));
						final String temp2 = (isSudoIdLogin == true) ? "SUDO LOGIN" : "SUDO DO NOT LOGIN";
						Platform.runLater(() -> displayText(temp2));
						break;

					case "-deletesudoId":
						if (isMakeSudoId == true && isSudoIdLogin == false) {
							if (sSudoID.equals(sCommandWords[2]) && sSudoPassword.equals(sCommandWords[3])) {
								isSudoIdDelete = true;
								Platform.runLater(() -> displayText("please input yes or other word."));
							}
						} else
							Platform.runLater(() -> displayText(
									"you don't register sudo ID. you need to make your sudo ID or logout sudo id"));

						break;

					case "-logout":
						if (isSudoIdLogin == true) {
							isSudoIdLogin = false;
							Platform.runLater(() -> displayText(sSudoID + " is logout"));
						} else
							Platform.runLater(() -> displayText("not exist for logout"));
						break;

					case "-register":
						if (isMakeSudoId == false) {
							if (sCommandWords[2].trim().length() > 0 && sCommandWords[3].trim().length() > 0
									&& sCommandWords[4].trim().length() > 0) {
								sSudoID = sCommandWords[2];
								sSudoPassword = sCommandWords[3];
								sEmailAddress = sCommandWords[4];
								Platform.runLater(() -> displayText("Success Making Sudo ID"));
								isMakeSudoId = true;
								writeSudoUserinformationToFile();
							} else {
								Platform.runLater(() -> displayText("Fail, you must insert String word"));
							}
							break;
						} else
							Platform.runLater(() -> displayText(
									"already you register your sudo id. if you want to change your id then you can use command \'-find\'"));

						break;
					case "-login":

						if (sSudoID.equals(sCommandWords[2]) && sSudoPassword.equals(sCommandWords[3])) {
							isSudoIdLogin = true;
							Platform.runLater(() -> displayText("Hello " + sSudoID));
						} else {
							isSudoIdLogin = false;
							Platform.runLater(() -> displayText("Fail to login "));
						}
						break;
					// gmail
					case "-findid":
						if (isMakeSudoId == true) {
							if (sCommandWords[2].equals(sEmailAddress)) {
								sSudoID = sCommandWords[2];
								sSudoPassword = sCommandWords[3];
								temp = new Thread() {
									@Override
									public void run() {
										sendingEmail(sEmailAddress + "@gmail.com", "jrc-Server your sudo ID",
												"hello. your ID :" + sSudoID + " and your Password : " + sSudoPassword);
									}
								};
								temp.start();

								Platform.runLater(() -> displayText("your password and ID send your e-mail"));

								break;
							} else {
								Platform.runLater(() -> displayText("Fail, your e-mail address is wrong"));
								break;
							}
						} else
							Platform.runLater(() -> displayText("you need to make sudoId"));
						break;

					// user
					case "-create":
						if (false == isSudoIdLogin) {
							Platform.runLater(() -> displayText("you don have permission"));
							break;
						}

						if (sCommandWords[2].trim().length() <= 0 || sCommandWords[3].trim().length() <= 0) {
							Platform.runLater(() -> displayText("id or password must long word 1"));
							break;
						}

						if (!sCommandWords[3].equals(sCommandWords[4])) {
							Platform.runLater(() -> displayText("you need to check your confirmation password"));
							break;
						}
						query = "SELECT id FROM userinformation where id='" + sCommandWords[2] + "'";

						try {
							rs = stmt.executeQuery(query);

							if (rs.next()) {
								Platform.runLater(() -> displayText("this id already exist in the DB."));
								break;
							}

							query = "INSERT INTO userinformation VALUE ('" + sCommandWords[2] + "','" + sCommandWords[3]
									+ "')";
							stmt.executeUpdate(query);

							query = "INSERT INTO tictactoc VALUE ('" + sCommandWords[2] + "'," + 0 + "," + 0 + "," + 0
									+ ")";
							stmt.executeUpdate(query);

							query = "INSERT INTO catchme VALUE ('" + sCommandWords[2] + "'," + 0 + "," + 0 + "," + 0
									+ ")";
							Platform.runLater(() -> displayText("Make [" + sCommandWords[2] + "] success"));

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						break;

					// user
					case "-remove":
						existId = false;
						if (false == isSudoIdLogin) {
							Platform.runLater(() -> displayText("you don have permission"));
							break;
						}

						query = "SELECT id FROM userinformation where id='" + sCommandWords[2] + "'";

						try {
							rs = stmt.executeQuery(query);

							if (rs.next()) {
								existId = true;
							}

							if (existId) {
								isSudodeleteEventOccured = true;
								sSudoDeleteUserID = sCommandWords[2];
								Platform.runLater(() -> displayText("please input yes or other word."));
							} else
								Platform.runLater(() -> displayText(sCommandWords[2] + " do not exist in the DB"));

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						break;

					// user information modify
					case "-modify":
						if (false == isSudoIdLogin) {
							Platform.runLater(() -> displayText("you don have permission"));
							break;
						}

						if (sCommandWords[4].equals("id")) {
							Platform.runLater(() -> displayText("id can not change using -modify"));
							break;
						}

						query = "update" + sCommandWords[3] + "set " + sCommandWords[4] + "=" + sCommandWords[5]
								+ "where id='" + sCommandWords[2] + "'";
						try {
							stmt.executeUpdate(query);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Platform.runLater(() -> displayText("-modify is successfull"));
						break;

					// show user information
					case "-userplayinfo":
						existId = false;

						if (false == isSudoIdLogin) {
							Platform.runLater(() -> displayText("you don have permission"));
							break;
						}

						query = "SELECT id FROM userinformation where id='" + sCommandWords[2] + "'";

						try {
							rs = stmt.executeQuery(query);

							if (rs.next()) {
								existId = true;
							}

							if (existId) {
								Platform.runLater(() -> displayText(sCommandWords[2] + "info"));
								query = "SELECT id,win,defeat,playtimes FROM tictactoc where id='" + sCommandWords[2]
										+ "'";

								rs = stmt.executeQuery(query);
								while (rs.next()) {
									String win = rs.getString("win");
									String defeat = rs.getString("defeat");
									String playtime = rs.getString("playtimes");

									Platform.runLater(() -> displayText(
											"tictacotc win:" + win + " defeat:" + defeat + " playtime:" + playtime));

								}
								query = "SELECT id,win,defeat,playtimes FROM catchme where id='" + sCommandWords[2]
										+ "'";

								rs = stmt.executeQuery(query);

								while (rs.next()) {
									String win = rs.getString("win");
									String defeat = rs.getString("defeat");
									String playtime = rs.getString("playtimes");

									Platform.runLater(() -> displayText(
											"catchme win:" + win + " defeat:" + defeat + " playtime:" + playtime));
								}
								query = "SELECT id,win,defeat,playtimes FROM meteor where id='" + sCommandWords[2]
										+ "'";

								rs = stmt.executeQuery(query);

								while (rs.next()) {
									String win = rs.getString("win");
									String defeat = rs.getString("defeat");
									String playtime = rs.getString("playtimes");

									Platform.runLater(() -> displayText(
											"catchme win:" + win + " defeat:" + defeat + " playtime:" + playtime));
								}

							} else
								Platform.runLater(() -> displayText(sCommandWords[2] + " do not exist in the DB"));

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					case "-useraccessinfo":
						existId = false;

						if (false == isSudoIdLogin) {
							Platform.runLater(() -> displayText("you don have permission"));
							break;
						}

						query = "SELECT id FROM userinformation where id='" + sCommandWords[2] + "'";

						try {
							rs = stmt.executeQuery(query);

							if (rs.next()) {
								existId = true;
							}

							if (existId) {

								Platform.runLater(() -> displayText(sCommandWords[2] + "access info"));
								query = "SELECT id, ipaddress, accessAndTerminateTime, type FROM useripandaccess where id='"
										+ sCommandWords[2] + "'";

								rs = stmt.executeQuery(query);
								while (rs.next()) {
									String ipaddress = rs.getString("ipaddress");
									String accessAndTerminateTime = rs.getString("accessAndTerminateTime");
									String type = rs.getString("type");

									Platform.runLater(() -> displayText("ipaddress:" + ipaddress + " time:"
											+ accessAndTerminateTime + " type:" + type));

								}

							} else
								Platform.runLater(() -> displayText(sCommandWords[2] + " do not exist in the DB"));

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					default:
						invalidShellCommand();
						break;
					}
					break;

				case "create":
					switch (sCommandWords[1]) {
					case "-help":
						break;

					case "-room":
						break;

					default:
						invalidShellCommand();
						break;
					}
					break;

				case "delete":
					switch (sCommandWords[1]) {
					case "-help":
						break;

					case "-room":
						break;

					default:
						invalidShellCommand();
						break;
					}
					break;

				// related on protocol.
				case "protocol":
					break;

				default:
					invalidShellCommand();
					break;
				}

		} else {
			invalidShellCommand();
		}
	}

	private void invalidShellParameter() {
		Platform.runLater(() -> displayText("wrong parameter"));
	}

	private void invalidShellCommand() {
		Platform.runLater(() -> displayText("invalid shall command"));
	}

	private void readSudoUserinformationFromFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(Settings.sServerInfo));
			sSudoID = EncryptionManager.decrypt64bits(br.readLine());
			sSudoPassword = EncryptionManager.decrypt64bits(br.readLine());
			sEmailAddress = EncryptionManager.decrypt64bits(br.readLine());
			isMakeSudoId = Boolean.parseBoolean(EncryptionManager.decrypt64bits(br.readLine()));
			br.close();
		}

		catch (FileNotFoundException e) {
			System.out.println("serverinfo.jrc do not exist");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("file reading complete");
	}

	private void writeFileClean() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(Settings.sServerInfo);
			pw.println(" ");
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("serverinfo.jrc do not exist");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void writeSudoUserinformationToFile() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(Settings.sServerInfo);
			pw.println(EncryptionManager.encrypt64bits(sSudoID));
			pw.println(EncryptionManager.encrypt64bits(sSudoPassword));
			pw.println(EncryptionManager.encrypt64bits(sEmailAddress));
			pw.println(EncryptionManager.encrypt64bits(Boolean.toString(isMakeSudoId)));
			pw.close();

		} catch (FileNotFoundException e) {
			System.out.println("serverinfo.jrc do not exist");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String[] parserOfCommandSyntax(String commandSyntax) {
		String sCommandWords[] = new String[Settings.nMaximumCommandWordSize];

		int nSyntexOnePosition;
		int nRepeatCounter = 0;

		for (;;) {
			nSyntexOnePosition = commandSyntax.toLowerCase().indexOf(" ");

			if (nRepeatCounter >= Settings.nMaximumCommandWordSize)
				return null;
			if (Settings.ERRORCODE == nSyntexOnePosition) {
				sCommandWords[nRepeatCounter] = commandSyntax;
				break;
			} else if (0 == nSyntexOnePosition) {
				return null;
			} else {
				sCommandWords[nRepeatCounter] = commandSyntax.substring(0, nSyntexOnePosition);
				commandSyntax = commandSyntax.substring(nSyntexOnePosition + 1, commandSyntax.length());
			}

			nRepeatCounter++;
		}

		for (int i = nRepeatCounter + 1; i < Settings.nMaximumCommandWordSize; i++) {
			sCommandWords[i] = " ";
		}

		for (int i = 0; i < Settings.nMaximumCommandWordSize; i++) {
			if (sCommandWords[i] != " ")
				System.out.println("execution command Queue[" + i + "] :" + sCommandWords[i]);
		}

		return sCommandWords;
	}

	// unCheck the warnings.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handleTableViewMouseClicked(MouseEvent event) {
		// if it is not double click event. this event is removed by next code
		if (event.getClickCount() != 2) {
			event.consume();
			return;
		}

		// if you click the client id in the client list view. then this dialog
		// PopUp
		Platform.runLater(() -> {
			try {
				dialog = new Stage(StageStyle.UTILITY);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(btnMain.getScene().getWindow());
				dialog.setTitle("Client Information");
				Parent parent;

				parent = FXMLLoader.load(getClass().getResource("piechart.fxml"));

				PieChart pieChart = (PieChart) parent.lookup("#pieChart");

				TableView<ClientGameInformation> tableView = (TableView) parent.lookup("#tableView");

				ObservableList<ClientGameInformation> clientGameInformationlist;

				TableView<ClientAccessInformation> tableView2 = (TableView) parent.lookup("#tableAccess");

				ObservableList<ClientAccessInformation> clientGameInformationlist2 = FXCollections
						.observableArrayList();

				Label caption = (Label) parent.lookup("#label");
				piechartClientName = clientListView.getSelectionModel().getSelectedItem();

				// find the data set about clicked the client name
				for (Client client : connections) {
					if (client.getClientName().equals(piechartClientName)) {

						String query = "SELECT ipaddress, accessAndTerminateTime ,type FROM useripandaccess where id='"
								+ client.getClientName() + "'";

						ResultSet rs = stmt.executeQuery(query);

						while (rs.next()) {
							clientGameInformationlist2.add(new ClientAccessInformation(rs.getString("ipaddress"),
									rs.getString("accessAndTerminateTime"), rs.getString("type")));

						}

						pieChart.setTitle("Client " + piechartClientName + " information");
						pieChart.setLabelLineLength(4);
						pieChart.setLegendSide(Side.RIGHT);
						pieChart.setLegendVisible(true);

						pieChart.setData(client.getQueryDataSet());
						Button btnClose = (Button) parent.lookup("#btnClose");
						btnClose.setOnAction(e -> dialog.close());
						Scene scene = new Scene(parent);

						caption.setTextFill(Color.DARKORANGE);
						caption.setStyle("-fx-font: 10 arial;");
						caption.setFont(new Font("Cambria", 10));

						clientGameInformationlist = FXCollections.observableArrayList(client.getTicTacToc(),
								client.getCatchMe());
						TableColumn tc = tableView.getColumns().get(0);
						tc.setCellValueFactory(new PropertyValueFactory("gameName"));
						tc.setStyle("-fx-alignment: CENTER;");

						tc = tableView.getColumns().get(1);
						tc.setCellValueFactory(new PropertyValueFactory("win"));
						tc.setStyle("-fx-alignment: CENTER;");

						tc = tableView.getColumns().get(2);
						tc.setCellValueFactory(new PropertyValueFactory("defeat"));
						tc.setStyle("-fx-alignment: CENTER;");

						tc = tableView.getColumns().get(3);
						tc.setCellValueFactory(new PropertyValueFactory("playTimes"));
						tc.setStyle("-fx-alignment: CENTER;");

						tableView.setItems(clientGameInformationlist);

						tc = tableView2.getColumns().get(0);
						tc.setCellValueFactory(new PropertyValueFactory("ipAddress"));
						tc.setStyle("-fx-alignment: CENTER;");

						tc = tableView2.getColumns().get(1);
						tc.setCellValueFactory(new PropertyValueFactory("AccessAndTerminateTime"));
						tc.setStyle("-fx-alignment: CENTER;");

						tc = tableView2.getColumns().get(2);
						tc.setCellValueFactory(new PropertyValueFactory("type"));
						tc.setStyle("-fx-alignment: CENTER;");

						tableView2.setItems(clientGameInformationlist2);

						/*
						 * if you click the pie chart you can get the message
						 * about pie chart information in the pie chart
						 */
						for (PieChart.Data data : pieChart.getData()) {
							data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
								double total = 0;
								for (PieChart.Data d : pieChart.getData()) {
									total += d.getPieValue();
								}
								caption.setTranslateX(e.getSceneX());
								caption.setTranslateY(e.getSceneY());
								try {
									String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
									if (!text.isEmpty()) {
										// System.out.println(data.getName()
										// + ":" + text);
										caption.setText(data.getName() + ":" + text);
									}
								} catch (Exception k) {
									System.err.println(k + " here");
								}
							});
						}

						dialog.setResizable(false);
						dialog.setScene(scene);
						dialog.show();

						/*
						 * if you call the close request then this dialog is
						 * closed by system
						 */
						dialog.setOnCloseRequest(e -> {
							dialog.close();
						});

					}
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

	}

	public void sendingEmail(String Email, String sTitle, String sMessage) {
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		final String username = "-";//
		final String password = "-";
		try {
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// -- Create a new message --
			Message msg = new MimeMessage(session);

			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress("-"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email, false));
			msg.setSubject(sTitle);
			msg.setText(sMessage);
			msg.setSentDate(new Date());
			Transport.send(msg);
			System.out.println("Message sent.");
		} catch (MessagingException e) {
			System.out.println("Erreur d'envoi, cause: " + e);
		}

		temp.interrupt();
	}

	/**
	 * this method manage the main button if you call the this function. you can
	 * return the main log scene
	 * 
	 * @param event
	 */
	public void handleBtnMain(ActionEvent event) {
		try {
			StackPane root = (StackPane) btnMain.getScene().getRoot();

			login.setTranslateX(0);

			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(login.translateXProperty(), 350);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					stopServer();
					root.getChildren().remove(login);
				}
			}, keyValue);
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * display the TXT on the textArea in the server
	 * 
	 * @param text
	 */
	void displayText(String text) {
		textArea.appendText(text + "\n");
	}

	/**
	 * this method's function is that start the server.
	 */
	void startServer() {
		serverTextCommandEditInit();

		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);

		try {
			serverSocket = new ServerSocket();
			// set the server's IP and Port Number;
			serverSocket.bind(new InetSocketAddress(Settings.nConnectionServerPortNumber));
		} catch (Exception e) {
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}

		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {

			/*
			 * if find the not existed client in the listView , this scheduler
			 * delete the client in the listView
			 */
			@Override
			public void run() {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						cleanTheList();

						if (isServerExit) {
							nExitCount++;
							if (nExitCount % 20 == 0) {
								Platform.runLater(() -> displayText("Exit server after "
										+ (Settings.nServerExitDefaultSeconds - nExitCount / 20) + "s"));
								if (1 == (Settings.nServerExitDefaultSeconds - nExitCount / 20))
									Platform.runLater(() -> displayText("Good Bye"));
							}
						}
						if (nExitCount >= Settings.nServerExitDefaultSeconds * 20)

							System.exit(0);

					}
				});

			}
			// run between 50 milli seconds
		}, 50, 50, TimeUnit.MILLISECONDS);

		// if start the server. the server receive the clients
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					displayText("[서버 시작]");
					btnStartStop.setText("stop");

				});
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						String message = "[연결 수락: " + socket.getRemoteSocketAddress() + ": "
								+ Thread.currentThread().getName() + "]";
						Platform.runLater(() -> displayText(message));

						Client client = new Client(socket, numbering + "", socket.getRemoteSocketAddress() + "",
								new Date());
						numbering++;
						connections.add(client);
						Platform.runLater(() -> displayText("[연결 개수: " + connections.size() + "]"));

						Platform.setImplicitExit(false);

					} catch (Exception e) {
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}

		};
		executorService.submit(runnable);

	}

	private void serverTextCommandEditInit() {
		serverTextEdit.setEditable(true);
		serverTextEdit.setVisible(true);
		serverTextEdit.setPromptText("Input your shell command");
	}

	/**
	 * clean the list per 50 milli seconds. for the new connected client.
	 */
	void cleanTheList() {

		int length = connections.size();
		int total = items.size();
		boolean tag[] = new boolean[total];
		Platform.runLater(() -> {
			for (int j = 0; j < length; j++)
				if (items.size() == 0) {
					if (connections.get(j).isRename())
						items.add(connections.get(j).getClientName());
				} else if (items.contains(connections.get(j).getClientName()) == false) {
					if (connections.get(j).isRename())
						items.add(connections.get(j).getClientName());
				} else if (connections.size() > 0 && tag.length > 0 && items.size() > 0
						&& connections.get(j).getClientName().length() > 0) {
					tag[items.indexOf(connections.get(j).getClientName())] = true;
				}
			for (int i = total - 1; i > -1; i--)
				if (items.size() > 0 && tag.length > 0 && tag[i] == false) {
					if (items.get(i).equals(piechartClientName))
						dialog.close();

					items.remove(i);
				}
		});

	}

	/**
	 * stop server method. if occurred error or cancel event. this method will
	 * be operated and init the all variables.
	 */
	void stopServer() {
		try {
			for (int i = connections.size() - 1; i > -1; i--) {
				connections.get(i).getSocket().close();
				connections.remove(i);
			}

			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if (executorService != null && !executorService.isShutdown()) {
				executorService.shutdown();
			}

			if (scheduler != null & !scheduler.isShutdown()) {
				scheduler.shutdown();
			}

			this.cleanTheList();

			Platform.runLater(() -> {
				displayText("[서버 멈춤]");
				btnStartStop.setText("start");
				unableServerTextEdit();
			});
		} catch (Exception e) {
		}
	}

	private void unableServerTextEdit() {
		serverTextEdit.setEditable(false);
		serverTextEdit.setVisible(false);
		serverTextEdit.setPromptText("");
	}

	/**
	 * @author KJW this class is for the accessed client.
	 */
	class Client {

		private Date dAccessTime;

		private Date dTerminateTime;

		/**
		 * client CatchMe Game possible Click Count
		 */
		private int nCatchmePlayCount;

		/**
		 * client's Entered Room name
		 */
		private String sEnteredRoom;

		/**
		 * set the Game start Event
		 */
		private boolean gameStartSet;

		/**
		 * check the this client have turn token
		 */
		private boolean playToken;

		/**
		 * check the this user's name redefine
		 */
		private boolean isRename;

		/**
		 * for the analyze the packat's amounts
		 */
		private ObservableList<Data> queryDataSet;

		/**
		 * for manage the sending message
		 */
		private ExecutorService executorServiceSending;

		/**
		 * client's name variable
		 */
		private String clientName;

		/**
		 * client' socket
		 */
		private Socket socket;

		/**
		 * client's unique game tag
		 */
		private int clientGameTag;

		/**
		 * client's input stream
		 */
		private InputStream inputStream;

		/**
		 * client IP address
		 */
		private String clientIpAddress;

		/**
		 * game information about TicTacToc
		 */
		private ClientGameInformation ticTacToc;

		/**
		 * game information about CatchMe
		 */
		private ClientGameInformation catchMe;

		private ClientGameInformation meteor;

		/**
		 * check the when the client is access or terminate the server
		 */
		private ArrayList<Integer> CheckAccessTime = new ArrayList<Integer>();

		private Timer msTimer;

		/**
		 * for the database system query
		 */
		private String query;

		/**
		 * for the database system's query's result
		 */
		private ResultSet rs;

		private Random rnd;

		/**
		 * for the CatchMe previous item number check to continuous check the
		 * item number
		 */
		private int nPreviousItemNumber;

		private int nDestoryMeteor;

		/*
		 * if not exist any message from client. the server finish the
		 * connection for saving resources
		 */
		private TimerTask msSecond = new TimerTask() {
			public void run() {
				if (0 == CheckAccessTime.size()) {
					String messageCheck = "[클라이언트 반응 없음: " + socket.getRemoteSocketAddress() + ": "
							+ Thread.currentThread().getName() + "]";
					Platform.runLater(() -> displayText(messageCheck));
					try {
						msTimer.cancel();
						msSecond.cancel();
						socket.close();
						inputStream.close();
						connections.remove(Client.this);
						deleteTheClientInTheRooms(Client.this);
						Platform.runLater(() -> displayText("[연결 개수: " + connections.size() + "]"));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				CheckAccessTime.clear();
			}
		};

		/**
		 * client's construct
		 * 
		 * @param socket
		 * @param name
		 * @param clientIpAddress
		 */
		Client(Socket socket, String name, String clientIpAddress, Date accessTime) {
			this.dAccessTime = accessTime;
			this.ticTacToc = new ClientGameInformation(Settings.sGameStringStyleTicTacToc);
			this.catchMe = new ClientGameInformation(Settings.sGameStringStyleCatchMe);
			this.meteor = new ClientGameInformation(Settings.sGameStringStyleMeteorGame);
			this.sEnteredRoom = null;
			this.clientName = name;
			this.socket = socket;
			this.nDestoryMeteor = Settings.ZEROINIT;
			this.clientIpAddress = clientIpAddress;
			this.queryDataSet = FXCollections.observableArrayList();
			this.setGameStartSet(false);
			this.setPlayToken(false);
			this.setRename(false);
			this.clientGameTag = Settings.ERRORCODE;
			this.rnd = new Random();
			this.nCatchmePlayCount = Settings.nCatchMeMAXClickCount;
			executorServiceSending = Executors.newSingleThreadExecutor();

			receive();
			checkTheTime();

		}

		/*
		 * call the msSecond Event per 10seconds. msSecond check the terminated
		 * Clients
		 */
		void checkTheTime() {
			msTimer = new Timer();
			msTimer.schedule(msSecond, 10000, 10000);
		}

		// Receive the packet from client
		void receive() {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {

						while (true) {
							byte[] byteArr = new byte[Settings.nReceiveBufferSize];

							// sending message.
							inputStream = socket.getInputStream();

							// if client exit incorrectly then exception is
							// occurred
							int readByteCount = inputStream.read(byteArr);

							// check the socket event success
							if (readByteCount == -1) {
								throw new IOException();
							}

							String data = new String(byteArr, 0, readByteCount, "UTF-8");

							String[] multiplePackets = SplitPacketManager
									.splitMultiplePacket(EncryptionManager.decrypt64bits(data));

							for (int ik = 0; ik < multiplePackets.length; ik++) {

								/*
								 * processing all packet particle using next
								 * method for splitting the packet
								 */
								String[] splitPacket = SplitPacketManager.splitProtocol(multiplePackets[ik]);

								if (splitPacket == null)
									System.err.println("Protocol Error occured code Line 310");

								int protocol = Integer.parseInt(splitPacket[0]);

								if (protocol != Settings._REQUEST_THE_ROOM_LIST_INFORMATION
										&& protocol != Settings._REQUEST_GAME_ROOM_LIST
										&& protocol != Settings._REQUEST_ROOM_MEMBER_NUMBER
										&& protocol != Settings._ACCESS_THE_SERVER
										&& protocol != Settings._REQUEST_METEORGAME_PLAYER_MOVING) {
									String message = "[ " + protocol + " 요청 처리: " + socket.getRemoteSocketAddress()
											+ ": " + Thread.currentThread().getName() + "]";
									Platform.runLater(() -> displayText(message));
								}

								// processing each protocol
								switch (protocol) {

								// client first access the server protocol
								case Settings._ACCESS_THE_SERVER:
									AddQueryDataSet("R ACCESSTHESERVER", data.length());
									CheckAccessTime.add(Integer.valueOf(protocol));
									// writeOnTheBoard(protocol, splitPacket);
									break;

								// client request the room list protocol
								case Settings._REQUEST_THE_ROOM_LIST_INFORMATION:
									AddQueryDataSet("R REQUESTTHEROOMLISTINFORMATION", data.length());
									boolean _check = true;

									for (int i = 0; i < gameRooms.size(); i++)
										if (0 == gameRooms.get(i).getsRoomName().compareTo(splitPacket[1])) {
											sendPacket(Settings._ANSWER_THE_ROOM_IS_EXIST + "");
											AddQueryDataSet("S ANSWERTHEROOMISEXIST", 2);
											_check = false;
											writeOnTheBoard(Settings._ANSWER_THE_ROOM_IS_EXIST, splitPacket);
											break;
										}

									if (_check == true) {
										Client.this.setsEnteredRoom(splitPacket[1]);
										gameRooms.add(new GameRoom(Client.this, splitPacket[1],
												Integer.parseInt(splitPacket[2]), Integer.parseInt(splitPacket[3])));
										writeOnTheBoard(protocol, splitPacket);

										sendPacket(Settings._ANSWER_THE_ROOM_MAKE_SUCCESS + "", splitPacket[1],
												splitPacket[2], splitPacket[3], getClientName());
										AddQueryDataSet("S ANSWERTHEROOMMAKESUCCESS", splitPacket[1].length()
												+ splitPacket[2].length() + splitPacket[3].length());
									}

									break;

								/*
								 * client request just the clicked room member
								 * number information about room member
								 */
								case Settings._REQUEST_ROOM_MEMBER_NUMBER:
									AddQueryDataSet("R REQUESTROOMMEMBERNUMBER", data.length());
									int _enterTheClient = -1;
									for (int i = 0; i < gameRooms.size(); i++)
										if (0 == gameRooms.get(i).getsRoomName().compareTo(splitPacket[1]))
											_enterTheClient = gameRooms.get(i).getEnteredTheClients();
									// writeOnTheBoard(protocol, splitPacket);
									sendPacket(Settings._ANSWER_ROOM_MEMBER_NUMBER + "", _enterTheClient + "");
									AddQueryDataSet("S ANSWERROOMMEMBERNUMBER", 2);
									break;

								// client request the game room list
								case Settings._REQUEST_GAME_ROOM_LIST:
									AddQueryDataSet("R REQUESTGAMEROOMLIST", data.length());
									sendRoomListPacket(Settings._ANSWER_GAME_ROOM_LIST, gameRooms);
									AddQueryDataSet("S ANSWERGAMEROOMLIST", gameRooms.size());
									break;

								// client request the clicked room information
								case Settings._REQUEST_CLICKED_ROOM_INFORMATION:
									AddQueryDataSet("R REQUESTCLICKEDROOMINFORMATION", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (0 == gameRooms.get(i).getsRoomName().compareTo(splitPacket[1])) {
											sendPacket(Settings._ANSWER_CLICKED_ROOM_INFORMATION + "",
													gameRooms.get(i).getnMaxmumClients() + "",
													gameRooms.get(i).getGameType() + "");
											AddQueryDataSet("S ANSWERCLICKEDROOMINFORMATION",
													gameRooms.get(i).getnMaxmumClients()
															+ gameRooms.get(i).getGameType());
											break;
										}
									break;

								/*
								 * client request the sending message protocol
								 * in the waitingRoom
								 */
								case Settings._REQUEST_WAITING_ROOM_SENDING_MESSAGE:
									AddQueryDataSet("R REQUESTWAITINGROOMSENDINGMESSAGE", data.length());

									String[] sMessageProtocol = splitPacket[1].split(" ");

									sMessageProtocol[0] = sMessageProtocol[0].toLowerCase();

									if (sMessageProtocol[0].equals("-info")) {

										if (false == waitingRoomMessageValidCheck(sMessageProtocol))
											break;

										query = "SELECT id,win,defeat,playtimes FROM " + sMessageProtocol[2]
												+ " where id='" + sMessageProtocol[1] + "'";

										try {
											rs = stmt.executeQuery(query);
										} catch (SQLException e) {
											sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "",
													"Wrong command", "error");
											break;
										}
										if (rs.next()) {
											String win = rs.getString("win");
											String defeat = rs.getString("defeat");
											String playtime = rs.getString("playtimes");

											sendPacket(
													Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "", "win:" + win
															+ " " + "defeat:" + defeat + " " + "playtime:" + playtime,
													sMessageProtocol[1] + " -info");

											break;
										}
									} else if (sMessageProtocol[0].equals("-w")) {

										if (false == waitingRoomMessageValidCheck(sMessageProtocol))
											break;

										String sSendingMessage = "";

										for (int i = 2; i < sMessageProtocol.length; i++)
											sSendingMessage += sMessageProtocol[i] + " ";

										if (sSendingMessage == "")
											break;

										for (Client client : connections)
											if (client.getClientName().equals(getClientName())
													|| client.getClientName().equals(sMessageProtocol[1])) {
												client.sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "",
														sSendingMessage, Client.this.getClientName() + " -w");
												client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "",
														sSendingMessage, Client.this.getClientName() + " -w");
											}
										break;

									}

									for (Client client : connections) {
										client.sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "",
												splitPacket[1], Client.this.getClientName());

										AddQueryDataSet("S ANSWERWAITINGROOMSENDINGMESSAGE",
												splitPacket[1].length() + Client.this.getClientName().length());
									}
									break;

								/*
								 * client request the sending message in the
								 * game room
								 */
								case Settings._REQUEST_ROOM_MEMEBER_MESSAGE:
									AddQueryDataSet("R REQUESTROOMMEMEBERMESSAGE", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[2])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", splitPacket[1],
													Client.this.getClientName());

											AddQueryDataSet("S ANSWERROOMMEMEBERMESSAGE",
													splitPacket[1].length() + Client.this.getClientName().length());
											break;
										}
									break;

								// client request the game server login
								case Settings._REQUEST_LOGIN:
									AddQueryDataSet("R REQUESTLOGIN", data.length());

									query = "SELECT id FROM userinformation where id='" + splitPacket[1]
											+ "' and password='" + splitPacket[2] + "'";

									rs = stmt.executeQuery(query);

									/*
									 * check id in the database system. and
									 * protects error about concurrency enter
									 * the server
									 */
									if (rs.next() && !items.contains(splitPacket[1])) {
										if (!splitPacket[1].equals(""))
											setClientName(splitPacket[1]);

										setRename(true);

										sendPacket(Settings._ANSWER_LOGIN + "", true + "", getClientName());
										AddQueryDataSet("S ANSWERLOGIN", getClientName().length() + 2);

										java.util.Date dt = new java.util.Date();

										java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
												"yyyy-MM-dd HH:mm:ss");

										String currentTime = sdf.format(dt);
										query = "INSERT INTO useripandaccess VALUE ('" + splitPacket[1] + "','"
												+ getSocket().getRemoteSocketAddress().toString() + "','" + currentTime
												+ "','" + "Start" + "')";
										stmt.executeUpdate(query);

										query = "SELECT win, defeat FROM catchme where id ='" + splitPacket[1] + "'";
										rs = stmt.executeQuery(query);

										while (rs.next()) {
											getCatchMe().setWin(rs.getInt("win"));
											getCatchMe().setDefeat(rs.getInt("defeat"));
										}

										query = "SELECT win, defeat FROM tictactoc where id ='" + splitPacket[1] + "'";
										rs = stmt.executeQuery(query);

										while (rs.next()) {
											getTicTacToc().setWin(rs.getInt("win"));
											getTicTacToc().setDefeat(rs.getInt("defeat"));
										}

										query = "SELECT win, defeat FROM meteor where id ='" + splitPacket[1] + "'";
										rs = stmt.executeQuery(query);

										while (rs.next()) {
											getMeteor().setWin(rs.getInt("win"));
											getMeteor().setDefeat(rs.getInt("defeat"));
										}

										writeOnTheBoard(protocol, splitPacket);

										break;
									}

									sendPacket(Settings._ANSWER_LOGIN + "", false + "", getClientName());
									AddQueryDataSet("S ANSWERLOGIN", getClientName().length() + 2);

									break;

								// client request the out of the room event
								case Settings._REQUEST_OUT_OF_THE_ROOM:
									AddQueryDataSet("R REQUESTOUTOFTHEROOM", data.length());
									for (int i = 0; i < connections.size(); i++)
										if (connections.get(i).getClientName().equals(splitPacket[1]))
											deleteTheClientInTheRooms(connections.get(i));
									break;

								/*
								 * client request the enter the room event check
								 * the maximum nuber
								 */
								case Settings._REQUEST_GUEST_ENTER_THE_ROOM:
									AddQueryDataSet("R REQUESTGUESTENTERTHEROOM", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											for (int j = 0; j < connections.size(); j++)
												if (connections.get(j).getClientName().equals(splitPacket[2]))
													if (gameRooms.get(i).enterTheRoom(connections.get(j))) {
														sendPacket(Settings._ANSWER_GUEST_ENTER_THE_ROOM_SUCCESS + "",
																gameRooms.get(i).getsRoomName(),
																gameRooms.get(i).getnMaxmumClients() + "",
																gameRooms.get(i).getGameType() + "",
																gameRooms.get(i).getManager().getClientName());
														AddQueryDataSet("S ANSWERGUESTENTERTHEROOMSUCCESS",
																gameRooms.get(i).getsRoomName().length()
																		+ gameRooms.get(i).getnMaxmumClients()
																		+ gameRooms.get(i).getGameType());
													} else {
														sendPacket(Settings._ANSWER_GUEST_ENTER_THE_ROOM_FAIL + "",
																gameRooms.get(i).getsRoomName());
														AddQueryDataSet("S ANSWERGUESTENTERTHEROOMFAIL",
																gameRooms.get(i).getsRoomName().length());
													}
										} else {
											sendPacket(Settings._ANSWER_GUEST_ENTER_THE_ROOM_LOGICAL_ERROR + "");
											AddQueryDataSet("S ANSWERGUESTENTERTHEROOMLOGICALERROR", 1);
										}
									break;

								// request the game room's people number
								case Settings._REQUEST_GAME_ROOM_MEMEBER_NUMBER:
									AddQueryDataSet("R REQUESTGAMEROOMMEMEBERNUMBER", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_GAME_ROOM_MEMEBER_NUMBER + "",
													gameRooms.get(i).getEnteredTheClients() + "");
											AddQueryDataSet("S REQUESTGAMEROOMMEMEBERNUMBER",
													gameRooms.get(i).getEnteredTheClients());
											break;
										}
									break;

								// request the client logout
								case Settings._REQUEST_LOGOUT:
									try {
										java.util.Date dt = new java.util.Date();

										java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
												"yyyy-MM-dd HH:mm:ss");

										String currentTime = sdf.format(dt);
										query = "INSERT INTO useripandaccess VALUE ('" + getClientName() + "','"
												+ getSocket().getRemoteSocketAddress().toString() + "','" + currentTime
												+ "','" + "Finish" + "')";

										stmt.executeUpdate(query);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;

								// request the client terminate event
								case Settings._REQUEST_TERMINATE:

									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getRoomClients().contains(Client.this))
											gameRooms.get(i).exitTheRoom(Client.this);
									AddQueryDataSet("R REQUESTTERMINATE", data.length());
									String message1 = "[Client Terminate " + socket.getRemoteSocketAddress() + ": "
											+ Thread.currentThread().getName() + "]";
									Platform.runLater(() -> displayText(message1));

									try {
										if (isRename()) {
											java.util.Date dt = new java.util.Date();

											java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");

											String currentTime = sdf.format(dt);
											query = "INSERT INTO useripandaccess VALUE ('" + getClientName() + "','"
													+ getSocket().getRemoteSocketAddress().toString() + "','"
													+ currentTime + "','" + "Finish" + "')";

											stmt.executeUpdate(query);
										}
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;

								/*
								 * if new member enter the game room. this
								 * protocol called by the clients
								 */
								case Settings._REQUEST_NEW_ROOM_MEMEBER_NOTIFICATION:
									AddQueryDataSet("R REQUESTNEWROOMMEMEBERNOTIFICATION", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_NEW_ROOM_MEMEBER_NOTIFICATION + "",
													Client.this.getClientName());

											AddQueryDataSet("S REQUESTNEWROOMMEMEBERNOTIFICATION",
													Client.this.getClientName().length());
											break;
										}
									break;

								/*
								 * client request the game start event. if you
								 * all clients clicked game start, the game will
								 * start
								 */
								case Settings._REQUEST_START_THE_GAME:
									AddQueryDataSet("R REQUESTSTARTTHEGAME", data.length());
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[2])) {
											if (gameRooms.get(i).startTheRoomGame(splitPacket[1],
													Boolean.parseBoolean(splitPacket[3]))) {

												sendPacket(Settings._ANSWER_START_THE_GAME + "", splitPacket[3]);

												gameRooms.get(i).sendMessageInTheRoomPeople(
														Settings._ANSWER_ROOM_GAME_START + "",
														Settings.isRoomGameStart + "",
														gameRooms.get(i).getPlayTokenPositionNumber() + "");

												if (gameRooms.get(i).getGameType() == Settings.nGameCatchMe)
													gameRooms.get(i).sendCatchMeClickedInitNumber();
											} else {
												sendPacket(Settings._ANSWER_START_THE_GAME + "", splitPacket[3]);
												gameRooms.get(i).sendMessageInTheRoomPeople(
														Settings._ANSWER_ROOM_GAME_START + "",
														Settings.isRoomGameStop + "");
											}

										}
									AddQueryDataSet("S ANSWERSTARTTHEGAME", splitPacket[3].length() + 2);
									break;

								// display user's game start statues
								case Settings._REQUEST_ANWER_ROOM_GAME_MESSAGE:
									AddQueryDataSet("R REQUESTANWERROOMGAMEMESSAGE", data.length());

									String _meassage;
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											if (Settings.isGamePrepareStart == Boolean.parseBoolean(splitPacket[2]))
												_meassage = "게임 시작 준비를 완료하였습니다.";
											else
												_meassage = "게임 시작 준비를 취소하였습니다.";

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", _meassage,
													Client.this.getClientName());

											AddQueryDataSet("S ANSWERROOMMEMEBERMESSAGE",
													_meassage.length() + Client.this.getClientName().length());
											break;
										}
									break;

								/*
								 * client request CathMe game clicked event
								 * about board
								 */
								case Settings._REQUEST_CATCHME_STONE_EVENT:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											int _itemNumber = rnd.nextInt(4);
											int _choice = rnd.nextInt(2);

											if (_itemNumber == getnPreviousItemNumber())
												if (_choice == 0)
													_itemNumber++;
												else
													_itemNumber--;

											if (_itemNumber >= 4)
												_itemNumber = 0;

											if (_itemNumber < 0)
												_itemNumber = 3;

											setnPreviousItemNumber(_itemNumber);

											if (Settings.ERRORCODE == gameRooms.get(i).getCatchmeBoardStatues()[Integer
													.parseInt(splitPacket[2])][Integer.parseInt(splitPacket[3])]
															.getTagNumber()) {

												int _clickPossibleNumber;

												_clickPossibleNumber = gameRooms.get(i).setTheCatchmeBoard(
														Integer.parseInt(splitPacket[2]),
														Integer.parseInt(splitPacket[3]), getClientGameTag(),
														Integer.parseInt(splitPacket[4]));

												gameRooms.get(i).sendMessageInTheRoomPeople(
														Settings._ANSWER_CATCHME_STONE_EVENT + "", getClientName(),
														splitPacket[2], splitPacket[3], true + "",
														gameRooms.get(i).getNextGamePlayer() + "",
														Integer.parseInt(splitPacket[4]) + "", _itemNumber + "",
														_clickPossibleNumber + "");

												gameRooms.get(i).checktheCatchMeGameWin();
											} else {

												sendPacket(Settings._ANSWER_CATCHME_STONE_EVENT + "", getClientName(),
														splitPacket[2], splitPacket[3], false + "",
														gameRooms.get(i).getPlayTokenPositionNumber() + "",
														Integer.parseInt(splitPacket[4]) + "", _itemNumber + "",
														Settings.ERRORCODE + "");
											}
										}
									break;

								// client request the TicTacToc game click event
								case Settings._REQUEST_TICTACTOC_STONE_EVENT:
									AddQueryDataSet("R REQUESTTICTACTOCSTONEEVENT", data.length());

									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											if (Settings.ERRORCODE == gameRooms.get(i)
													.getTictactocBoardStatues()[Integer.parseInt(
															splitPacket[2])][Integer.parseInt(splitPacket[3])]) {

												gameRooms.get(i).setTheTicTacTocBoard(Integer.parseInt(splitPacket[2]),
														Integer.parseInt(splitPacket[3]), getClientGameTag());

												gameRooms.get(i).sendMessageInTheRoomPeople(
														Settings._ANSWER_TICTACTOC_STONE_EVENT + "", getClientName(),
														splitPacket[2], splitPacket[3], true + "",
														gameRooms.get(i).getNextGamePlayer() + "");
											} else {

												sendPacket(Settings._ANSWER_TICTACTOC_STONE_EVENT + "", getClientName(),
														splitPacket[2], splitPacket[3], false + "",
														gameRooms.get(i).getPlayTokenPositionNumber() + "");
											}
										}
									break;

								// turn client name is called by the clients
								case Settings._REQUEST_TICTACTOC_TURN_PLAYER_NAME:
									AddQueryDataSet("R REQUESTTICTACTOCTURNPLAYERNAME", data.length());

									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_TICTACTOC_TURN_PLAYER_NAME + "",
													gameRooms.get(i).getTurnClientName());
											break;
										}
									break;

								/*
								 * allocate the unique number to the each
								 * clients in the game room
								 */
								case Settings._REQUEST_SET_GAME_PLAYER_UNIQUE_TAG_NUMBER:
									AddQueryDataSet("R REQUESTSETGAMEPLAYERUNIQUETAGNUMBER", data.length());
									sendPacket(Settings._ANSWER_SET_GAME_PLAYER_UNIQUE_TAG_NUMBER + "",
											getClientGameTag() + "");
									break;

								// register new client to the database system
								case Settings._REQUEST_REGISTER_NEW_USER:
									AddQueryDataSet("R REQUESTREGISTERNEWUSER", data.length());

									query = "SELECT id FROM userinformation where id='" + splitPacket[1] + "'";

									rs = stmt.executeQuery(query);

									if (rs.next()) {
										sendPacket(Settings._ANSWER_REGISTER_NEW_USER + "", false + "");
										break;
									}

									initUserDBinformation(splitPacket);

									writeOnTheBoard(Settings._REQUEST_REGISTER_NEW_USER, splitPacket);
									sendPacket(Settings._ANSWER_REGISTER_NEW_USER + "", true + "");

									break;

								/*
								 * this protocol occurred at the end of client
								 * connection
								 */
								case Settings._REQUEST_OUT_OF_THE_SERVER:

									Platform.runLater(() -> items.remove(splitPacket[1]));
									setRename(false);
									writeOnTheBoard(Settings._REQUEST_OUT_OF_THE_SERVER, splitPacket);

									break;

								/*
								 * if client request new CatchMe Selected Item
								 * request this protocol processing this event
								 */
								case Settings._REQUEST_CATCHM_ESELECT_ITEM:
									int _itemNumber = rnd.nextInt(4);

									setnPreviousItemNumber(_itemNumber);
									sendPacket(Settings._ANSWER_CATCHM_ESELECT_ITEM + "", _itemNumber + "");
									break;

								// client request the game server login on
								// mobile
								case Settings._REQUEST_MOBILE_LOGIN:
									AddQueryDataSet("R REQUESTLOGIN", data.length());

									query = "SELECT id FROM userinformation where id='" + splitPacket[1]
											+ "' and password='" + splitPacket[2] + "'";

									rs = stmt.executeQuery(query);

									/*
									 * check id in the database system. and
									 * protects error about concurrency enter
									 * the server
									 */
									if (rs.next() && !items.contains(splitPacket[1])) {
										if (!splitPacket[1].equals(""))
											setClientName(splitPacket[1]);

										setRename(true);

										sendPacket(Settings._ANSWER_LOGIN + "", true + "");
										AddQueryDataSet("S ANSWERLOGIN", getClientName().length() + 2);

										java.util.Date dt = new java.util.Date();

										java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
												"yyyy-MM-dd HH:mm:ss");

										String currentTime = sdf.format(dt);
										query = "INSERT INTO useripandaccess VALUE ('" + splitPacket[1] + "','"
												+ getSocket().getRemoteSocketAddress().toString() + "','" + currentTime
												+ "','" + "Start" + "')";
										stmt.executeUpdate(query);

										query = "SELECT win, defeat FROM catchme where id ='" + splitPacket[1] + "'";
										rs = stmt.executeQuery(query);

										while (rs.next()) {
											getCatchMe().setWin(rs.getInt("win"));
											getCatchMe().setDefeat(rs.getInt("defeat"));
										}

										query = "SELECT win, defeat FROM tictactoc where id ='" + splitPacket[1] + "'";
										rs = stmt.executeQuery(query);

										while (rs.next()) {
											getTicTacToc().setWin(rs.getInt("win"));
											getTicTacToc().setDefeat(rs.getInt("defeat"));
										}

										writeOnTheBoard(protocol, splitPacket);

										break;
									}

									sendPacket(Settings._ANSWER_LOGIN + "", false + "");
									AddQueryDataSet("S ANSWERLOGIN", getClientName().length() + 2);

									break;

								case Settings._REQUEST_MOBILE_MAKE_GAME_UNIVERSE:
									universeGames.add(new UniverseGame(Client.this, 2, 100));
									break;

								case Settings._REQUEST_METEORGAME_SET_CLIECK_EVENT:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_SET_CLIECK_EVENT + "", getClientName(),
													splitPacket[2], splitPacket[3]);
											break;
										}
									break;

								case Settings._REQUEST_PANGPANG_INIT_GAME_PLAY:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_PANGPANG_INIT_GAME_PLAY + "", splitPacket[2],
													rnd.nextInt(Settings.nGameAsteroidSceneWidth) + "",
													Settings.nGameAsteroidSceneHeight
															- Settings.nPangPangMainPlayerHeigh + "");
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_INIT_GAME_PLAY:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_INIT_GAME_PLAY + "", splitPacket[2],
													rnd.nextInt(Settings.nGameAsteroidSceneWidth) + "",
													rnd.nextInt(Settings.nGameAsteroidSceneHeight) + "");
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_REINIT_GAME_PLAY:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_REINIT_GAME_PLAY + "", splitPacket[2],
													splitPacket[3], splitPacket[4], splitPacket[5]);
											break;
										}
									writeOnTheBoard(protocol, splitPacket);
									break;

								case Settings._REQUEST_PANGPANG_REINIT_GAME_PLAY:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_PANGPANG_REINIT_GAME_PLAY + "", splitPacket[2],
													splitPacket[3], splitPacket[4], splitPacket[5]);
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_OUT_OF_PLAYER:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_OUT_OF_PLAYER + "", splitPacket[2]);
											break;
										}
									writeOnTheBoard(protocol, splitPacket);

									break;

								case Settings._REQUEST_PANGPANG_OUT_OF_PLAYER:

									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {

											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_PANGPANG_OUT_OF_PLAYER + "", splitPacket[2]);
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_PLAYER_MOVING:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_PLAYER_MOVING + "", splitPacket[2],
													splitPacket[3], splitPacket[4]);
											break;
										}
									break;

								case Settings._REQUEST_PANGPANG_PLAYER_MOVING:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_PANGPANG_PLAYER_MOVING + "", splitPacket[2],
													splitPacket[3], splitPacket[4], splitPacket[5]);
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_METEOR_DELETE:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											String winner;

											winner = gameRooms.get(i).setTheMeteriorDestroyClientScore(splitPacket[2]);
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_METEOR_DELETE + "", splitPacket[2],
													splitPacket[3], splitPacket[4], winner);
											break;
										}
									break;

								case Settings._REQUEST_METEORGAME_METEOR_GAME_FINISH:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).checkTheMeteorGameFinishCondition(splitPacket);
											break;
										}
									break;

								case Settings._REQEUST_MOBILE_PANGPANG_SCORE:
									sendPacket(Settings._ANSWER_MOBILE_PANGPANG_SCORE + "", "success");
									break;

								case Settings._REQUEST_PC_CLIENT_VERSION_CHECK:
									if (splitPacket[1].equals(Settings.clientVersion))
										sendPacket(Settings._ANSWER_PC_CLIENT_VERSION_CHECK + "", true + "");
									else
										sendPacket(Settings._ANSWER_PC_CLIENT_VERSION_CHECK + "", false + "");

									break;

								case Settings._REQUEST_METEORGAME_METEOR_PLAYER_SIZE_UP:
									for (int i = 0; i < gameRooms.size(); i++)
										if (gameRooms.get(i).getsRoomName().equals(splitPacket[1])) {
											gameRooms.get(i).sendMessageInTheRoomPeople(
													Settings._ANSWER_METEORGAME_METEOR_PLAYER_SIZE_UP + "",
													splitPacket[2],
													Double.parseDouble(splitPacket[3]) + Settings.fUpDoubleMeteorSize
															+ "",
													Double.parseDouble(splitPacket[4]) + Settings.fUpDoubleMeteorSize
															+ "");
											break;
										}
									break;

								/*
								 * check the protocol exist in the switch
								 * statements
								 */
								default:
									System.err.println(
											"[" + protocol + "]this Protocol hasn't in the server Protocol list");

									break;
								}
							}
							System.in.skip(System.in.available());
						}
					} catch (Exception e) {
						try {
							/*
							 * if occurred disconnection about client this
							 * exception will occur
							 */
							String message = "[클라이언트 통신 안됨: " + socket.getRemoteSocketAddress() + ": "
									+ Thread.currentThread().getName() + "]";
							Platform.runLater(() -> displayText(message));
							connections.remove(Client.this);
							// deleteTheClientInTheRooms(Client.this);
							socket.close();
							Platform.runLater(() -> displayText("[연결 개수: " + connections.size() + "]"));
						} catch (IOException e2) {
						}
					}
				}

				private boolean waitingRoomMessageValidCheck(String[] sMessageProtocol) {

					if (sMessageProtocol.length <= 2) {

						sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "", "Wrong command", "error");
						return false;
					}

					if (!items.contains(sMessageProtocol[1])) {
						sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "",
								sMessageProtocol[1] + " is not entered in server", "error");
						return false;
					}

					return true;
				}

				private void initUserDBinformation(String[] splitPacket) throws SQLException {
					query = "INSERT INTO userinformation VALUE ('" + splitPacket[1] + "','" + splitPacket[2] + "')";
					stmt.executeUpdate(query);

					query = "INSERT INTO tictactoc VALUE ('" + splitPacket[1] + "'," + 0 + "," + 0 + "," + 0 + ")";
					stmt.executeUpdate(query);

					query = "INSERT INTO catchme VALUE ('" + splitPacket[1] + "'," + 0 + "," + 0 + "," + 0 + ")";
					stmt.executeUpdate(query);

					query = "INSERT INTO meteor VALUE ('" + splitPacket[1] + "'," + 0 + "," + 0 + "," + 0 + ")";
					stmt.executeUpdate(query);
				}
			};
			// if this client exit the executor is removed by system
			if (executorService.isShutdown() == false)
				executorService.submit(runnable);
		}

		/**
		 * write the message on the server's panel. this method manage the
		 * previous behavior
		 * 
		 * @param protocol
		 * @param splitPacket
		 */
		void writeOnTheBoard(int protocol, String[] splitPacket) {
			String message;

			switch (protocol) {
			case Settings._REQUEST_METEORGAME_REINIT_GAME_PLAY:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + splitPacket[1] + "(RoomName)"
						+ "] meteorgame reinit";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._ACCESS_THE_SERVER:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Client connection Check" + "]";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_THE_ROOM_LIST_INFORMATION:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Create a Room " + "\""
						+ splitPacket[1] + "\"" + " Room Number : " + gameRooms.size() + "]";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._ANSWER_THE_ROOM_IS_EXIST:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Fail to Create a Room "
						+ splitPacket[1] + "]";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_ROOM_MEMBER_NUMBER:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Send the Room Member Number "
						+ "]";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_LOGIN:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Login " + splitPacket[1] + ","
						+ splitPacket[2] + "]";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_MOBILE_LOGIN:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Login " + splitPacket[1] + ","
						+ splitPacket[2] + "]";
				Platform.runLater(() -> displayText(message));
				break;
			case Settings._REQUEST_REGISTER_NEW_USER:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + "Create New Id " + splitPacket[1]
						+ "," + splitPacket[2] + "] Success";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_OUT_OF_THE_SERVER:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + " " + splitPacket[1]
						+ "] out of the server Success";
				Platform.runLater(() -> displayText(message));
				break;

			case Settings._REQUEST_METEORGAME_OUT_OF_PLAYER:
				message = "[protocol : " + socket.getRemoteSocketAddress() + ": " + " " + splitPacket[2]
						+ "] out of the meteorGame Success";
				Platform.runLater(() -> displayText(message));
				break;

			// 보완

			default:
				System.err.println("[" + protocol + "]" + "This Protocol hasn't in the server Board Protocol list");
				break;
			}
		}

		public Date getdTerminateTime() {
			return dTerminateTime;
		}

		public void setdTerminateTime(Date dTerminateTime) {
			this.dTerminateTime = dTerminateTime;
		}

		public Date getdAccessTime() {
			return dAccessTime;
		}

		/**
		 * sends packed in the game room people
		 * 
		 * @param protocol
		 * @param gameRooms
		 */
		public void sendRoomListPacket(int protocol, List<GameRoom> gameRooms) {
			String packet = new String();
			int partitionPacketNumber = 0;

			for (int i = 0; i < gameRooms.size(); i++)
				if (gameRooms.get(i).isGameRunning() == false)
					partitionPacketNumber++;
			packet = packet.concat(Settings.sSenderSplitProtocolToken);
			packet = packet.concat(protocol + Settings.sSenderSplitProtocolToken);
			packet = packet.concat(partitionPacketNumber + Settings.sSenderSplitProtocolToken);

			for (int i = 0; i < gameRooms.size(); i++) {
				if (gameRooms.get(i).isGameRunning() == false)
					packet = packet.concat(gameRooms.get(i).getsRoomName() + Settings.sSenderSplitProtocolToken);
			}
			packet = packet.concat(Settings.sSenderSplitMultipleToken);

			send(packet);

		}

		/**
		 * basic method to send the message to the clients
		 * 
		 * @param partitionPacketNumber
		 * @param datas
		 */
		public void sendPacket(String... datas) {
			String packet = new String();

			packet = packet.concat(Settings.sSenderSplitProtocolToken);

			for (int i = 0; i < datas.length; i++) {
				packet = packet.concat(datas[i] + Settings.sSenderSplitProtocolToken);
			}

			packet = packet.concat(Settings.sSenderSplitMultipleToken);

			send(packet);
		}

		/**
		 * super basic method to send packet to clients
		 * 
		 * @param data
		 */
		void send(String data) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						byte[] byteArr = EncryptionManager.encrypt64bits(data).getBytes("UTF-8");
						OutputStream outputStream = socket.getOutputStream();
						outputStream.write(byteArr);
						outputStream.flush();
					} catch (Exception e) {
						try {

							String message = "[클라이언트 통신 안됨: " + socket.getRemoteSocketAddress() + ": "
									+ Thread.currentThread().getName() + "]";
							Platform.runLater(() -> displayText(message));
							connections.remove(Client.this);
							// deleteTheClientInTheRooms(Client.this);

							socket.close();

							Platform.runLater(() -> displayText("[연결 개수: " + connections.size() + "]"));
							executorServiceSending.shutdownNow();
						} catch (IOException e2) {
						}
					}
				}
			};
			if (executorServiceSending.isShutdown() == false)
				executorServiceSending.submit(runnable);
		}

		/**
		 * init the client information
		 */
		public void init() {
			this.sEnteredRoom = " ";
			this.gameStartSet = false;
			this.playToken = false;
		}

		/**
		 * delete the client in the room
		 * 
		 * @param client
		 * @return
		 */
		boolean deleteTheClientInTheRooms(Client client) {
			boolean result = false;

			for (int i = gameRooms.size() - 1; i > -1; i--) {

				if (gameRooms.get(i).exitTheRoom(client)) {
					result = true;
					break;
				}
			}

			return result;
		}

		/**
		 * get the query data set. observableList<Data>
		 * 
		 * @return
		 */
		public ObservableList<Data> getQueryDataSet() {
			return queryDataSet;
		}

		/**
		 * this method's database for processing the view about user information
		 * 
		 * @param protocol
		 * @param packetSize
		 * @return
		 */
		public boolean AddQueryDataSet(String protocol, double packetSize) {

			for (int i = 0; i < queryDataSet.size(); i++)
				if (queryDataSet.get(i).getName().equals(protocol)) {
					queryDataSet.get(i).setPieValue(
							queryDataSet.get(i).getPieValue() + packetSize * Settings.nOnePacketStringSize);
					return true;
				}
			Platform.runLater(() -> {
				queryDataSet.add(new Data(protocol, packetSize * Settings.nOnePacketStringSize));
			});
			return false;
		}

		/**
		 * return the VatchMe user's Previous having item.
		 * 
		 * @return
		 */
		public int getnPreviousItemNumber() {
			return nPreviousItemNumber;
		}

		/**
		 * set the previous CatchMe Item number
		 * 
		 * @param nPreviousItemNumber
		 */
		public void setnPreviousItemNumber(int nPreviousItemNumber) {
			this.nPreviousItemNumber = nPreviousItemNumber;
		}

		/**
		 * get the CatchMe Player's Item count Integer
		 * 
		 * @return
		 */
		public int getnCatchmePlayCount() {
			return nCatchmePlayCount;
		}

		/**
		 * set the CatchMe Game Item Using Counts
		 * 
		 * @param nCatchmePlayCount
		 */
		public void setnCatchmePlayCount(int nCatchmePlayCount) {
			this.nCatchmePlayCount = nCatchmePlayCount;
		}

		/**
		 * if your client's Name Change. then this function occur
		 * 
		 * @return
		 */
		public boolean isRename() {
			return isRename;
		}

		/**
		 * set the Rename's statues
		 * 
		 * @param isRename
		 */
		public void setRename(boolean isRename) {
			this.isRename = isRename;
		}

		/**
		 * get the client information
		 * 
		 * @return
		 */
		public Socket getSocket() {
			return this.socket;
		}

		/**
		 * set the client name
		 * 
		 * @param clientName
		 * @return
		 */
		public boolean setClientName(String clientName) {
			if (!this.clientName.equals("")) {
				this.setRename(true);
				this.clientName = clientName;
				return true;
			}
			return false;

		}

		/**
		 * get the isGameStartSet boolean
		 * 
		 * @return
		 */
		public boolean isGameStartSet() {
			return gameStartSet;
		}

		/**
		 * set the gameStartSet
		 * 
		 * @param gameStartSet
		 */
		public void setGameStartSet(boolean gameStartSet) {
			this.gameStartSet = gameStartSet;
		}

		/**
		 * get the client Game tag. when you enter the game room integer
		 * 
		 * @return
		 */
		public int getClientGameTag() {
			return clientGameTag;
		}

		/**
		 * set the client game tag number, if the client
		 * 
		 * @param clientGameTag
		 */
		public void setClientGameTag(int clientGameTag) {
			this.clientGameTag = clientGameTag;
		}

		/**
		 * get the player Token
		 * 
		 * @return
		 */
		public boolean isPlayToken() {
			return playToken;
		}

		/**
		 * set the play token
		 * 
		 * @param playToken
		 */
		public void setPlayToken(boolean playToken) {
			this.playToken = playToken;
		}

		/**
		 * get the game information about TicTacToc
		 * 
		 * @return
		 */
		public ClientGameInformation getTicTacToc() {
			return ticTacToc;
		}

		/**
		 * set the TicTacToc client game information
		 * 
		 * @param ticTacToc
		 */
		public void setTicTacToc(ClientGameInformation ticTacToc) {
			this.ticTacToc = ticTacToc;
		}

		public ClientGameInformation getMeteor() {
			return meteor;
		}

		public void setMeteor(ClientGameInformation meteor) {
			this.meteor = meteor;
		}

		/**
		 * get the CatchMe information
		 * 
		 * @return
		 */
		public ClientGameInformation getCatchMe() {
			return catchMe;
		}

		/**
		 * set the catchMe information
		 * 
		 * @param catchMe
		 */
		public void setCatchMe(ClientGameInformation catchMe) {
			this.catchMe = catchMe;
		}

		/**
		 * get the room entered client
		 * 
		 * @return
		 */
		public String getsEnteredRoom() {
			return sEnteredRoom;
		}

		/**
		 * sets Entered Room
		 * 
		 * @param sEnteredRoom
		 */
		public void setsEnteredRoom(String sEnteredRoom) {
			this.sEnteredRoom = sEnteredRoom;
		}

		/**
		 * get the client ip adrress
		 * 
		 * @return
		 */
		public String getClientIpAddress() {
			return clientIpAddress;
		}

		/**
		 * set the client ip and port
		 * 
		 * @param clientIpAddress
		 */
		public void setClientIpAddress(String clientIpAddress) {
			this.clientIpAddress = clientIpAddress;
		}

		/**
		 * get the client name
		 * 
		 * @return
		 */
		public String getClientName() {
			return this.clientName;
		}

		public int getnDestoryMeteor() {
			return nDestoryMeteor;
		}

		public void setnDestoryMeteor(int nDestoryMeteor) {
			this.nDestoryMeteor = nDestoryMeteor;
		}

		public void setnIncreaseDestoryMeteor() {
			this.nDestoryMeteor++;
		}

		/*
		 * (non-Javadoc) to the default to string to user's toString string
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Client Name : " + this.getClientName() + " Socket : " + this.getSocket() + " ip address : "
					+ this.getSocket().getRemoteSocketAddress();
		}

	}

	class MobileClientGameRoom {
		private final Client manager;

		private int nMaximumClients;

		private boolean isGameRunning;

		private boolean isGameEnd;

		/**
		 * room's client variable or information.
		 */
		private List<Client> roomClients;

		public MobileClientGameRoom(Client manager, int nMaximumClients) {
			this.roomClients = new Vector<Client>();

			this.manager = manager;
			this.roomClients.add(manager);
			this.setnMaximumClients(nMaximumClients);
		}

		public int getnMaximumClients() {
			return nMaximumClients;
		}

		public void setnMaximumClients(int nMaximumClients) {
			this.nMaximumClients = nMaximumClients;
		}

		public boolean isGameRunning() {
			return isGameRunning;
		}

		public void setGameRunning(boolean isGameRunning) {
			this.isGameRunning = isGameRunning;
		}

		public boolean isGameEnd() {
			return isGameEnd;
		}

		public void setGameEnd(boolean isGameEnd) {
			this.isGameEnd = isGameEnd;
		}

		public Client getManager() {
			return manager;
		}

		public List<Client> getRoomClients() {
			return roomClients;
		}

	}

	class UniverseGame extends MobileClientGameRoom {

		private Random rnd;

		private int nTotalAsteroid;

		private ArrayList<Point2D> Asteroids;

		public UniverseGame(Client manager, int nMaximumClients, int nTotalAsteroid) {
			super(manager, nMaximumClients);

			this.setRnd(new Random());
			this.setnTotalAsteroid(nTotalAsteroid);
			this.setAsteroids(new ArrayList<Point2D>());

			initGame();
		}

		// 320 480
		private void setRnd(Random rnd) {
			this.rnd = rnd;
		}

		private void setAsteroids(ArrayList<Point2D> asteroids) {
			Asteroids = asteroids;
		}

		private void initGame() {

			for (int i = 0; i < getnTotalAsteroid(); i++) {
				Point2D _point = new Point2D(getRnd().nextInt(320), getRnd().nextInt(480));

				addAsteroidToArrayList(_point);

				getRoomClients().get(0).sendPacket(Settings._ANSWER_MOBILE_MAKE_GAME_UNIVERSE + "", _point.getX() + "",
						_point.getY() + "");

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		public void addAsteroidToArrayList(Point2D position) {
			getAsteroids().add(position);
		}

		public int getnTotalAsteroid() {
			return nTotalAsteroid;
		}

		public void setnTotalAsteroid(int nTotalAsteroid) {
			this.nTotalAsteroid = nTotalAsteroid;
		}

		public ArrayList<Point2D> getAsteroids() {
			return Asteroids;
		}

		private Random getRnd() {
			return rnd;
		}

	}

	/*
	 * class UniverseGame extends MobileClientGameRoom{ }
	 */

	/**
	 * manage the gameRoom event
	 * 
	 * @author KJW
	 */
	class GameRoom {

		/**
		 * maximum connected client number
		 */
		private final int nMaxmumClients;

		/**
		 * manager id about client
		 */
		private final Client manager;

		/**
		 * room's client variable or information.
		 */
		private List<Client> roomClients;

		/**
		 * this room's name
		 */
		private final String sRoomName;

		/**
		 * make the room time
		 */
		private final Date makeTime;

		/**
		 * this room's game type
		 */
		private final int gameType;

		/**
		 * client tag
		 */
		private int clientGameTag;

		/**
		 * set the TicTacToc board statues (store the client Tag Number)
		 */
		private int tictactocBoardStatues[][];

		/**
		 * CatchMe Board Statues (store the client Tag Number)
		 */
		private CatchmeBoardStatues catchmeBoardStatues[][];

		/**
		 * empty seat of the game room
		 */
		private int emptySeat;

		/**
		 * current play token position
		 */
		private int playTokenPositionNumber;

		/**
		 * Click event Counter for the CatchMe
		 */
		private int _counter;

		/**
		 * check the condition about game Running
		 */
		private boolean isGameRunning;

		/**
		 * check the condition about game end
		 */
		private boolean isGameEnd;

		/**
		 * check the TicTacToc AI turn or or off
		 */
		private boolean isAITicTacToc;

		/**
		 * local variable for checking continue
		 */
		private boolean _isGameContinueCheck;

		/**
		 * catchMe Click event count
		 */
		private int nCatchmePlayCount;

		/**
		 * catchMe game start Position x, y
		 */
		private TicTacTocPoint pointCatchmeFirstStart;

		private int nReceiveFinishEventCount;

		private boolean isCheckMeteorGameCheckFinishOneTime;

		private Map_Controler mapControler;

		private AnimationTimer spriteAnimationTimer;

		public GameRoom(Client roomManager, String sRoomName, int nMaxmumClients, int nGameType) {
			this.nReceiveFinishEventCount = Settings.ZEROINIT;
			this.isCheckMeteorGameCheckFinishOneTime = true;
			this.roomClients = new Vector<Client>();

			this.isAITicTacToc = false;
			this.isGameRunning = false;
			this.clientGameTag = Settings.ZEROINIT;
			this._counter = Settings.ZEROINIT;
			this._isGameContinueCheck = true;
			roomManager.setClientGameTag(clientGameTag++);
			this.makeTime = new Date();
			this.sRoomName = sRoomName;
			this.manager = roomManager;
			this.roomClients.add(roomManager);
			this.nMaxmumClients = nMaxmumClients;
			this.gameType = nGameType;
			this.isGameEnd = false;
			this.setPlayTokenPositionNumber(Settings.nGameTokenStartNumberPosition);

			if (getGameType() == Settings.nGameTicTacToc) {
				this.emptySeat = Settings.nTicTacTocBlockWidth * Settings.nTicTacTocBlockHeight;

				this.tictactocBoardStatues = new int[Settings.nTicTacTocBlockWidth][Settings.nTicTacTocBlockHeight];

				for (int i = 0; i < Settings.nTicTacTocBlockWidth; i++)
					for (int j = 0; j < Settings.nTicTacTocBlockHeight; j++)
						tictactocBoardStatues[i][j] = Settings.ERRORCODE;
			} else if (getGameType() == Settings.nGameCatchMe) {

				this.emptySeat = Settings.nCatchMeBlockWidth * Settings.nCatchMeBlockHeight;

				this.catchmeBoardStatues = new CatchmeBoardStatues[Settings.nCatchMeBlockWidth][Settings.nCatchMeBlockHeight];

				this.nCatchmePlayCount = Settings.nCatchMeMAXClickCount;

				for (int i = 0; i < Settings.nCatchMeBlockWidth; i++)
					for (int j = 0; j < Settings.nCatchMeBlockHeight; j++) {
						catchmeBoardStatues[i][j] = new CatchmeBoardStatues(Settings.ERRORCODE, Settings.ERRORCODE);
					}
			} else if (getGameType() == Settings.nGamePangPang) {
				this.mapControler = new Map_Controler();
			}
		}

		/**
		 * this method is called when client enter the room
		 * 
		 * @param client
		 * @return
		 */
		public boolean enterTheRoom(Client client) {
			if (roomClients.size() < nMaxmumClients)
				if (!roomClients.contains(client)) {
					client.setClientGameTag(clientGameTag++);
					client.setsEnteredRoom(getsRoomName());
					return roomClients.add(client);
				} else
					return false;
			else
				return false;
		}

		/**
		 * this method is called when client click the game start button
		 * 
		 * @param clientName
		 * @param setReady
		 * @return
		 */
		public boolean startTheRoomGame(String clientName, boolean setReady) {
			for (Client client : roomClients)
				if (client.getClientName().equals(clientName))
					client.setGameStartSet(setReady);

			for (Client client : roomClients)
				if (client.isGameStartSet() == false)
					return false;

			if (Settings.nGameCatchMe == getGameType()) {
				for (Client client : roomClients) {
					String query;
					client.getCatchMe().setPlayTimes(client.getCatchMe().getPlayTimes() + 1);

					try {
						query = "update catchme set playtimes =" + client.getCatchMe().getPlayTimes() + " where id='"
								+ client.getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (Settings.nGameTicTacToc == getGameType()) {
				for (Client client : roomClients) {
					String query;
					client.getTicTacToc().setPlayTimes(client.getTicTacToc().getPlayTimes() + 1);

					try {
						query = "update tictactoc set playtimes =" + client.getTicTacToc().getPlayTimes()
								+ " where id='" + client.getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (Settings.nGameMeteorGame == getGameType()) {
				for (Client client : roomClients) {
					String query;
					client.getMeteor().setPlayTimes(client.getMeteor().getPlayTimes() + 1);

					try {
						query = "update meteor set playtimes =" + client.getTicTacToc().getPlayTimes() + " where id='"
								+ client.getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			setGameRunning(true);
			if (Settings.nGameMeteorGame == getGameType())
				initMeteorGameWhenConditionStart();
			else if (Settings.nGamePangPang == getGameType())
				initPangPangWhenConditionStart();
			else
				roomClients.get(playTokenPositionNumber).setPlayToken(true);

			return true;
		}

		public void initPangPangWhenConditionStart() {
			spriteAnimationTimer = new AnimationTimer() {

				Long lastNanoTime = new Long(System.nanoTime());
				PangPangEnemy mEnemy[][] = new PangPangEnemy[Settings.nPangPangEnemyHeight][Settings.nPangPangEnemyWidth];
				Map_Controler mpCtr = new Map_Controler();
				boolean isInitialization = false;
				AttackEnemy mAttack;
				// init part

				public void handle(long currentNanoTime) {

					if (false == isInitialization) {
						mpCtr.readMap(1);
						
						for (int i = 0; i < Settings.nPangPangEnemyHeight; i++) {
							for (int j = 0; j < Settings.nPangPangEnemyWidth; j++) {
								mEnemy[i][j] = new PangPangEnemy(mpCtr);
								mEnemy[i][j].MakeEnemy(i, j);
							} // for i
						} // for j
						

						mAttack = new AttackEnemy(mpCtr,mEnemy);
						mAttack.ResetAttack();
						isInitialization = true;
					}

					// calculate time since last update.
					double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
					lastNanoTime = currentNanoTime;

					String sendingPacket = Settings.sPangPangPositionInformationWordToken;

					for (int i = 0; i < Settings.nPangPangEnemyHeight; i++) {
						for (int j = 0; j < Settings.nPangPangEnemyWidth; j++) {
							mEnemy[i][j].update(elapsedTime);

							sendingPacket += mEnemy[i][j].getPosition().x + Settings.sPangPangPositionCoordinationToken
									+ mEnemy[i][j].getPosition().y + Settings.sPangPangPositionInformationWordToken;

						} // for i
					} // for j
					mAttack.Attack();
					
					sendMessageInTheRoomPeople(Settings._ANSWER_PANGPANG_ENEMY_EVENT + "", sendingPacket);

				}
			};

			spriteAnimationTimer.start();

			sendMessageInTheRoomPeople(Settings._ANSWER_PANGPANG_PLAY_START + "", Boolean.toString(true));
		}

		public void initMeteorGameWhenConditionStart() {
			Random rnd = new Random();

			for (int i = 0; i < roomClients.size(); i++)
				roomClients.get(i).setnDestoryMeteor(0);

			for (int i = 0; i < Settings.nSettingAsteroidNumber; i++) {

				sendMessageInTheRoomPeople(Settings._ANSWER_METEORGAME_UNIVERSE_INIT + "",
						rnd.nextInt(Settings.nGameAsteroidSceneWidth) + "",
						rnd.nextInt(Settings.nGameAsteroidSceneHeight) + "");

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			sendMessageInTheRoomPeople(Settings._ANSWER_METEORGAME_PLAY_START + "", Boolean.toString(true));

		}

		/**
		 * get the client who have the token
		 * 
		 * @return
		 */
		public String getTurnClientName() {
			for (int i = 0; i < roomClients.size(); i++)
				if (playTokenPositionNumber == roomClients.get(i).getClientGameTag())
					return roomClients.get(i).getClientName();

			return null;
		}

		/**
		 * get the next player tag number (include inner algorithm)
		 * 
		 * @return
		 */
		public int getNextGamePlayer() {
			roomClients.get(playTokenPositionNumber).setPlayToken(false);

			playTokenPositionNumber++;

			if (playTokenPositionNumber >= roomClients.size())
				playTokenPositionNumber = Settings.nGameTokenStartNumberPosition;

			roomClients.get(playTokenPositionNumber).setPlayToken(true);

			return playTokenPositionNumber;
		}

		/**
		 * if client exit the game room, this method call check the two
		 * condition, client is manager, or guest
		 * 
		 * @param client
		 * @return
		 */
		public boolean exitTheRoom(Client client) {
			if (manager.getClientName().equals(client.getClientName())) {
				if (isGameRunning() && isGameEnd() == false)
					if (getGameType() == Settings.nGameTicTacToc)
						setTheClientScoreAboutTicTacTokReverse(manager.getClientGameTag());
					else if (getGameType() == Settings.nGameCatchMe)
						setTheClientScoreAboutCatchmeReverse(manager.getClientGameTag());
				sendMessageInTheRoomPeople(Settings._ANSWER_OUT_OF_THE_ROOM + "");
				if (spriteAnimationTimer != null)
					spriteAnimationTimer.stop();
				return gameRooms.remove(GameRoom.this);
			} else {
				if (client.getsEnteredRoom().equals(getsRoomName())) {
					sendMessageInTheRoomPeople(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", "님께서 퇴장하셨습니다.",
							client.getClientName());
					client.init();
					client.setClientGameTag(Settings.ERRORCODE);
					if (isGameRunning() && isGameEnd() == false)
						if (getGameType() == Settings.nGameTicTacToc)
							setTheClientScoreAboutTicTacTokReverse(client.getClientGameTag());
						else if (getGameType() == Settings.nGameCatchMe)
							setTheClientScoreAboutCatchmeReverse(client.getClientGameTag());
					client.sendPacket(Settings._ANSWER_OUT_OF_THE_ROOM + "");
				}

			}
			return roomClients.remove(client);
		}

		public String setTheMeteriorDestroyClientScore(String userName) {
			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientName().equals(userName))
					roomClients.get(i).setnIncreaseDestoryMeteor();

			Collections.sort(roomClients, new NoDescCompare());

			for (Client temp : roomClients) {
				return temp.getClientName();
			}

			return getManager().getClientName();

		}

		/**
		 * No 내림차순
		 * 
		 * @author falbb
		 */
		class NoDescCompare implements Comparator<Client> {

			/**
			 * 내림차순(DESC)
			 */
			@Override
			public int compare(Client arg0, Client arg1) {
				// TODO Auto-generated method stub
				return arg0.getnDestoryMeteor() > arg1.getnDestoryMeteor() ? -1
						: arg0.getnDestoryMeteor() < arg1.getnDestoryMeteor() ? 1 : 0;
			}

		}

		/**
		 * send message each client who is in the game room
		 * 
		 * @param packetNumber
		 * @param protocols
		 */
		public void sendMessageInTheRoomPeople(String... protocols) {

			if (getEnteredTheClients() == 1
					&& Integer.parseInt(protocols[0]) == Settings._ANSWER_TICTACTOC_STONE_EVENT) {

				AICelculate();

			}

			if (isGameRunning() == false && protocols.length > 2 && protocols[1].length() >= 8
					&& protocols[2].equals(manager.getClientName()) && getGameType() == Settings.nGameTicTacToc) {
				String _temp = protocols[1].substring(0, 8).toLowerCase();

				/* TicTacToc console parser */
				if (_temp.equals("-setgame")) {

					if (protocols[1].length() >= 20) {
						_temp = protocols[1].substring(9, 14).toLowerCase();
						/* check the style console commends */
						if (_temp.equals("style")) {
							_temp = protocols[1].substring(15, protocols[1].length()).toLowerCase();
							if (_temp.equals("color")) {
								for (int i = 0; i < roomClients.size(); i++)
									if (roomClients.get(i).getsEnteredRoom().equals(getsRoomName()))
										roomClients.get(i).sendPacket(
												Settings._ANSWER_SET_THE_TICTACTOC_COLOR_MODE + "", false + "");
								return;
							} else if (_temp.equals("blackwhite")) {
								for (int i = 0; i < roomClients.size(); i++)
									if (roomClients.get(i).getsEnteredRoom().equals(getsRoomName()))
										roomClients.get(i).sendPacket(
												Settings._ANSWER_SET_THE_TICTACTOC_COLOR_MODE + "", true + "");
								return;
							}
						}
					}
					/* check the AI console commends */
					if (protocols[1].length() >= 16 && getEnteredTheClients() <= 1) {
						_temp = protocols[1].substring(9, 11).toLowerCase();
						if (_temp.equals("ai")) {
							_temp = protocols[1].substring(12, protocols[1].length()).toLowerCase();

							if (_temp.equals("attack")) {
								setAITicTacToc(true);
								manager.sendPacket(Settings._ANSWER_TICTACTOC_AI_SETTING + "", "" + true);
								return;
							} else if (_temp.equals("defense")) {
								setAITicTacToc(false);
								manager.sendPacket(Settings._ANSWER_TICTACTOC_AI_SETTING + "", "" + false);
								return;
							}
						}
					}

				}
				/* catchMe console parser */
			} else if (isGameRunning() == false && protocols.length > 2 && protocols[1].length() >= 8
					&& protocols[2].equals(manager.getClientName()) && getGameType() == Settings.nGameCatchMe) {
				String _temp = protocols[1].substring(0, 8).toLowerCase();
				if (_temp.equals("-setgame")) {

					if (protocols[1].length() >= 16) {
						_temp = protocols[1].substring(9, 14).toLowerCase();
						/* check the count console commend */
						if (_temp.equals("count")) {
							_temp = protocols[1].substring(15, protocols[1].length()).toLowerCase();

							setnCatchmePlayCount(Integer.parseInt(_temp));

							for (int i = 0; i < roomClients.size(); i++)
								if (roomClients.get(i).getsEnteredRoom().equals(getsRoomName()))
									roomClients.get(i).sendPacket(Settings._ANSWER_SET_THE_CATCHME_PLAY_COUNT + "",
											_temp + "");

							return;

						}
					}

				}

			}

			Client _client = null;
			String[] sMessageProtocol = null;
			if (protocols.length > 2) {

				for (int i = 0; i < roomClients.size(); i++)
					if (roomClients.get(i).getClientName().equals(protocols[2])) {
						_client = roomClients.get(i);
					}

				sMessageProtocol = protocols[1].split(" ");
			}

			if (_client != null && gameRoomMessageValidCheck(_client, sMessageProtocol)) {

				sMessageProtocol[0] = sMessageProtocol[0].toLowerCase();

				String query;

				ResultSet rs;

				if (sMessageProtocol[0].equals("-info")) {

					query = "SELECT id,win,defeat,playtimes FROM " + sMessageProtocol[2] + " where id='"
							+ sMessageProtocol[1] + "'";

					try {
						rs = stmt.executeQuery(query);
					} catch (SQLException e) {
						_client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", "Wrong command", "error");
						return;
					}
					try {
						if (rs.next()) {
							String win = rs.getString("win");
							String defeat = rs.getString("defeat");
							String playtime = rs.getString("playtimes");

							_client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "",
									"win:" + win + " " + "defeat:" + defeat + " " + "playtime:" + playtime,
									sMessageProtocol[1] + " -info");

							return;
						}
					} catch (SQLException e) {
						_client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", "Wrong command", "error");
						return;
					}
				} else if (sMessageProtocol[0].equals("-w")) {

					String sSendingMessage = "";

					for (int i = 2; i < sMessageProtocol.length; i++)
						sSendingMessage += sMessageProtocol[i] + " ";

					if (sSendingMessage == "")
						return;

					for (Client client : connections)
						if (client.getClientName().equals(protocols[2])
								|| client.getClientName().equals(sMessageProtocol[1])) {
							client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "", sSendingMessage,
									protocols[2] + " -w");

							client.sendPacket(Settings._ANSWER_WAITING_ROOM_SENDING_MESSAGE + "", sSendingMessage,
									protocols[2] + " -w");
						}
					return;
				}
			}

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getsEnteredRoom().equals(getsRoomName())) {
					if (!(Settings._ANSWER_ROOM_MEMEBER_MESSAGE == Integer.parseInt(protocols[0])
							&& protocols[1].substring(0, 1).equals("-")))
						roomClients.get(i).sendPacket(protocols);
				}

			if (Integer.parseInt(protocols[0]) == Settings._ANSWER_OUT_OF_THE_ROOM)
				for (int i = 0; i < roomClients.size(); i++) {
					roomClients.get(i).setClientGameTag(Settings.ERRORCODE);
					roomClients.get(i).init();
				}
		}

		private boolean gameRoomMessageValidCheck(Client client, String[] sMessageProtocol) {

			if (sMessageProtocol.length <= 2) {
				return false;
			}

			for (int i = 0; i < connections.size(); i++)
				if (connections.get(i).getClientName().equals(sMessageProtocol[1]))
					return true;
			if (!sMessageProtocol[1].equals("시작"))
				client.sendPacket(Settings._ANSWER_ROOM_MEMEBER_MESSAGE + "",
						sMessageProtocol[1] + " is not entered in server", "error");

			return false;
		}

		/**
		 * if CatchMe Game Start, this method is called by game room's manager
		 * and the manager have game token and item set number
		 */
		public void sendCatchMeClickedInitNumber() {
			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getsEnteredRoom().equals(getsRoomName())) {

					roomClients.get(i).setnCatchmePlayCount(getnCatchmePlayCount());

					roomClients.get(i).sendPacket(Settings._ANSWER_CATCHME_INIT_PLAY_CLICKED_NUMBER + "",
							roomClients.get(i).getnCatchmePlayCount() + "");
				}
		}

		/**
		 * Process the TicTacToc AI
		 */
		public void AICelculate() {

			float nWeightTictactocBoard[][] = new float[Settings.nTicTacTocBlockWidth][Settings.nTicTacTocBlockHeight];

			int _weight;

			int _x = 0, _y = 0;

			ArrayList<TicTacTocPoint> _selectTag = new ArrayList<TicTacTocPoint>();

			for (int i = 0; i < Settings.nTicTacTocBlockWidth; i++)
				for (int j = 0; j < Settings.nTicTacTocBlockHeight; j++)
					if (tictactocBoardStatues[i][j] == Settings.ERRORCODE) {
						nWeightTictactocBoard[i][j] = 0;

						// 위
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int y = j - 1;; y--) {
							if (y < 0)
								break;
							if (tictactocBoardStatues[i][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 왼쪽 위 대각선
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i - 1, y = j - 1;; x--, y--) {
							if (x < 0)
								break;
							if (y < 0)
								break;
							if (tictactocBoardStatues[x][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 왼쪽
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i - 1;; x--) {
							if (x < 0)
								break;
							if (tictactocBoardStatues[x][j] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 왼쪽 아래 대각선
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i - 1, y = j + 1;; x--, y++) {
							if (x < 0)
								break;
							if (y >= Settings.nTicTacTocBlockHeight)
								break;
							if (tictactocBoardStatues[x][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 아래쪽
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int y = j + 1;; y++) {
							if (y >= Settings.nTicTacTocBlockHeight)
								break;
							if (tictactocBoardStatues[i][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 오른쪽 아래 대각선
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i + 1, y = j + 1;; x++, y++) {
							if (x >= Settings.nTicTacTocBlockWidth)
								break;
							if (y >= Settings.nTicTacTocBlockHeight)
								break;
							if (tictactocBoardStatues[x][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 오른쪽
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i + 1;; x++) {
							if (x >= Settings.nTicTacTocBlockWidth)
								break;
							if (tictactocBoardStatues[x][j] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						// 오른쪽 위 대각선
						_weight = Settings.nAiTicTacTocEnemyInitWeight;
						for (int x = i + 1, y = j - 1;; x++, y--) {
							if (x >= Settings.nTicTacTocBlockWidth)
								break;
							if (y < 0)
								break;
							if (tictactocBoardStatues[x][y] == roomClients.get(0).getClientGameTag()) {
								nWeightTictactocBoard[i][j] += _weight;
								_weight++;
							} else
								break;
						}

						if (isAITicTacToc()) {
							// 위 AI
							for (int y = j - 1;; y--) {
								if (y < 0)
									break;
								if (tictactocBoardStatues[i][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 왼쪽 위 대각선 AI
							for (int x = i - 1, y = j - 1;; x--, y--) {
								if (x < 0)
									break;
								if (y < 0)
									break;
								if (tictactocBoardStatues[x][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 왼쪽 AI
							for (int x = i - 1;; x--) {
								if (x < 0)
									break;
								if (tictactocBoardStatues[x][j] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 왼쪽 아래 대각선 AI
							for (int x = i - 1, y = j + 1;; x--, y++) {
								if (x < 0)
									break;
								if (y >= Settings.nTicTacTocBlockHeight)
									break;
								if (tictactocBoardStatues[x][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 아래쪽 AI
							for (int y = j + 1;; y++) {
								if (y >= Settings.nTicTacTocBlockHeight)
									break;
								if (tictactocBoardStatues[i][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 오른쪽 아래 대각선 AI
							for (int x = i + 1, y = j + 1;; x++, y++) {
								if (x >= Settings.nTicTacTocBlockWidth)
									break;
								if (y >= Settings.nTicTacTocBlockHeight)
									break;
								if (tictactocBoardStatues[x][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 오른쪽 AI
							for (int x = i + 1;; x++) {
								if (x >= Settings.nTicTacTocBlockWidth)
									break;
								if (tictactocBoardStatues[x][j] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
								} else
									break;
							}

							// 오른쪽 위 대각선 AI
							for (int x = i + 1, y = j - 1;; x++, y--) {
								if (x >= Settings.nTicTacTocBlockWidth)
									break;
								if (y < 0)
									break;
								if (tictactocBoardStatues[x][y] == Settings.nComputerAiTagNumber) {
									nWeightTictactocBoard[i][j] += Settings.nAiTicTacTocWeight;
									_weight++;
								} else
									break;
							}
						}

					} else {
						nWeightTictactocBoard[i][j] = Settings.ERRORCODE;
					}
			//
			// this comments can show the AI multiple sets
			// System.out.println();
			// for (int j = 0; j < Settings.TICTACTOCBLOCKHEIGHT; j++) {
			// for (int i = 0; i < Settings.TICTACTOCBLOCKWIDTH; i++)
			// System.out.print(nWeightTictactocBoard[i][j] + " ");
			// System.out.println();
			// }

			float temp = -1;

			for (int i = 0; i < Settings.nTicTacTocBlockWidth; i++)
				for (int j = 0; j < Settings.nTicTacTocBlockHeight; j++) {

					if (nWeightTictactocBoard[i][j] > temp) {
						temp = nWeightTictactocBoard[i][j];
						_x = i;
						_y = j;
						_selectTag.clear();
						_selectTag.add(new TicTacTocPoint(_x, _y));
					} else if (nWeightTictactocBoard[i][j] == temp) {
						_x = i;
						_y = j;
						_selectTag.add(new TicTacTocPoint(_x, _y));
					}
				}

			Random _rnd = new Random();
			int _selectTheTag = _rnd.nextInt(_selectTag.size());
			setTheTicTacTocBoard(_selectTag.get(_selectTheTag).getX(), _selectTag.get(_selectTheTag).getY(),
					Settings.nComputerAiTagNumber);

			roomClients.get(0).sendPacket(Settings._ANSWER_TICTACTOC_AI + "", "Computer AI",
					_selectTag.get(_selectTheTag).getX() + "", _selectTag.get(_selectTheTag).getY() + "", true + "",
					Settings.nComputerAiTagNumber + "");
		}

		/**
		 * check the game win detection or defeat check
		 * 
		 * @param x
		 * @param y
		 * @param tag
		 */
		private void checkTheTicTacTocGameWin(int x, int y, int tag) {

			int _x = 0;
			int _y = 0;

			for (int i = x, j = y;; i--, j--) {
				_x = i;
				_y = j;

				if (i <= 0)
					break;
				if (j <= 0)
					break;
			}

			// 대각선 위
			for (int i = _y, j = _x;; i++, j++) {
				subCheckTheTicTacTocWin(tag, i, j);

				if (i >= 8)
					break;
				if (j >= 11)
					break;
			}

			// y축
			_counter = 0;
			_x = x;
			_y = 0;
			for (int i = _y, j = _x; i < 9; i++)
				subCheckTheTicTacTocWin(tag, i, j);

			// x축
			_counter = 0;
			_x = 0;
			_y = y;
			for (int i = _y, j = _x; j < 12; j++)
				subCheckTheTicTacTocWin(tag, i, j);

			// 대각선 아래
			_counter = 0;
			for (int i = x, j = y;; i--, j++) {

				_x = i;
				_y = j;

				if (i <= 0)
					break;
				if (j >= 8)
					break;
			}
			for (int i = _y, j = _x;; i--, j++) {
				subCheckTheTicTacTocWin(tag, i, j);
				if (i <= 0)
					break;
				if (j >= 11)
					break;
			}

		}

		/**
		 * subCheck the TicTacToc game win using user define constant
		 * 
		 * @param tag
		 * @param i
		 * @param j
		 */
		private void subCheckTheTicTacTocWin(int tag, int i, int j) {
			if (_isGameContinueCheck)
				if (getTictactocBoardStatues()[j][i] == tag) {
					_counter++;
					if (_counter >= Settings.nWinTheTicTacTocCountNumber) {
						setTheClientScoreAboutTicTacTok(tag);
					}
				} else {
					_counter = 0;
				}

		}

		/**
		 * set the client scored about CatchMe
		 * 
		 * @param tag
		 */
		@SuppressWarnings("unused")
		private void setTheClientScoreAboutCatchme(int tag) {
			String query;
			_isGameContinueCheck = false;
			setGameEnd(true);

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientGameTag() == tag) {
					roomClients.get(i).getCatchMe().increaseWin();
					try {
						query = "update catchme set win =" + roomClients.get(i).getCatchMe().getWin() + " where id='"
								+ roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					roomClients.get(i).getCatchMe().increaseDefeat();
					try {
						query = "update catchme set defeat =" + roomClients.get(i).getCatchMe().getDefeat()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			sendMessageInTheRoomPeople(Settings._ANSWER_GAME_END + "", tag + "");

		}

		private void checkTheMeteorGameFinishCondition(String[] packet) {
			increaseReceiveFinishEventCount();

			if (getnReceiveFinishEventCount() >= getnMaxmumClients() && isCheckMeteorGameCheckFinishOneTime) {
				sendMessageInTheRoomPeople(Settings._ANSWER_METEORGAME_METEOR_GAME_FINISH + "");
				isCheckMeteorGameCheckFinishOneTime = false;

				setTheClientScoreAboutMeteorGame(packet[2]);
			}
		}

		private void setTheClientScoreAboutMeteorGame(String sWinnerId) {

			String query;
			_isGameContinueCheck = false;
			setGameEnd(true);
			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientName().equals(sWinnerId)) {
					roomClients.get(i).getMeteor().increaseWin();
					try {
						query = "update meteor set win =" + roomClients.get(i).getTicTacToc().getWin() + " where id='"
								+ roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					roomClients.get(i).getTicTacToc().increaseDefeat();
					try {
						query = "update meteor set defeat =" + roomClients.get(i).getTicTacToc().getDefeat()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}

		/**
		 * set the client score about TicTacToc if you win the game, you win
		 * score increase but an other case, your defeat increase
		 * 
		 * @param tag
		 */
		private void setTheClientScoreAboutTicTacTok(int tag) {
			String query;
			_isGameContinueCheck = false;
			setGameEnd(true);

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientGameTag() == tag) {
					roomClients.get(i).getTicTacToc().increaseWin();
					try {
						query = "update tictactoc set win =" + roomClients.get(i).getTicTacToc().getWin()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					roomClients.get(i).getTicTacToc().increaseDefeat();
					try {
						query = "update tictactoc set defeat =" + roomClients.get(i).getTicTacToc().getDefeat()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			sendMessageInTheRoomPeople(Settings._ANSWER_GAME_END + "", tag + "");

		}

		/**
		 * check the reverse win or defeat increasing events about CatchMe
		 * 
		 * @param tag
		 */
		private void setTheClientScoreAboutCatchmeReverse(int tag) {
			String query;
			setGameEnd(true);

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientGameTag() == tag) {
					roomClients.get(i).getCatchMe().increaseDefeat();
					try {
						query = "update catchme set defeat =" + roomClients.get(i).getCatchMe().getDefeat()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					roomClients.get(i).getCatchMe().increaseWin();
					try {
						query = "update catchme set win =" + roomClients.get(i).getCatchMe().getWin() + " where id='"
								+ roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			sendMessageInTheRoomPeople(Settings._ANSWER_GAME_END_REVERSE + "", tag + "");

		}

		/**
		 * set the win or defeat score base on the tag. tag defeat another win
		 * 
		 * @param tag
		 */
		private void setTheClientScoreAboutTicTacTokReverse(int tag) {
			String query;
			setGameEnd(true);

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientGameTag() == tag) {
					roomClients.get(i).getTicTacToc().increaseDefeat();
					try {
						query = "update tictactoc set defeat =" + roomClients.get(i).getTicTacToc().getDefeat()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					roomClients.get(i).getTicTacToc().increaseWin();
					try {
						query = "update tictactoc set win =" + roomClients.get(i).getTicTacToc().getWin()
								+ " where id='" + roomClients.get(i).getClientName() + "'";
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			sendMessageInTheRoomPeople(Settings._ANSWER_GAME_END_REVERSE + "", tag + "");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString() to the string method you can see the
		 * name, or totalmember, game type, make tpye
		 */
		@Override
		public String toString() {
			return "Name : " + this.getsRoomName() + " ToTalMember : " + this.getnMaxmumClients() + " Game Type : "
					+ this.getGameType() + " Make Time : " + this.getMakeTime();
		}

		public int getnReceiveFinishEventCount() {
			return nReceiveFinishEventCount;
		}

		public void increaseReceiveFinishEventCount() {
			this.nReceiveFinishEventCount++;
		}

		/**
		 * get the TicTacToc Board Statues (player Tag Number)
		 * 
		 * @return
		 */
		public int[][] getTictactocBoardStatues() {
			return tictactocBoardStatues;
		}

		/**
		 * get the CatchMe Board Statues (player Tag Number)
		 * 
		 * @return
		 */
		public CatchmeBoardStatues[][] getCatchmeBoardStatues() {
			return catchmeBoardStatues;
		}

		/**
		 * get the state about game Running Boolean
		 * 
		 * @return
		 */
		public boolean isGameRunning() {
			return isGameRunning;
		}

		/**
		 * set the state about game Running
		 * 
		 * @param isGameRunning
		 */
		public void setGameRunning(boolean isGameRunning) {
			this.isGameRunning = isGameRunning;
		}

		/**
		 * get the state about game end Boolean
		 * 
		 * @return
		 */
		public boolean isGameEnd() {
			return isGameEnd;
		}

		/**
		 * set the state about game end
		 * 
		 * @param isGameEnd
		 */
		public void setGameEnd(boolean isGameEnd) {
			this.isGameEnd = isGameEnd;
		}

		/**
		 * get the room's maximum client size integer
		 * 
		 * @return
		 */
		public int getnMaxmumClients() {
			return nMaxmumClients;
		}

		/**
		 * get the manager client
		 * 
		 * @return
		 */
		public Client getManager() {
			return manager;
		}

		/**
		 * get the room name string
		 * 
		 * @return
		 */
		public String getsRoomName() {
			return sRoomName;
		}

		/**
		 * get the array of entered clients
		 * 
		 * @return
		 */
		public int getEnteredTheClients() {
			return this.roomClients.size();
		}

		/**
		 * get the state about AI of TicTacToc
		 * 
		 * @return
		 */
		public boolean isAITicTacToc() {
			return isAITicTacToc;
		}

		/**
		 * set the state about AI of TicTacToc
		 * 
		 * @param isAITicTacToc
		 */
		public void setAITicTacToc(boolean isAITicTacToc) {
			this.isAITicTacToc = isAITicTacToc;
		}

		public List<Client> getRoomClients() {
			return roomClients;
		}

		/**
		 * get the room make time Date
		 * 
		 * @return
		 */
		public Date getMakeTime() {
			return makeTime;
		}

		/**
		 * get the game type integer
		 * 
		 * @return
		 */
		public int getGameType() {
			return gameType;
		}

		/**
		 * get the CatchMe Play Count about client integer
		 * 
		 * @return
		 */
		public int getnCatchmePlayCount() {
			return nCatchmePlayCount;
		}

		/**
		 * set the CatchMe play count
		 * 
		 * @param nCatchmePlayCount
		 */
		public void setnCatchmePlayCount(int nCatchmePlayCount) {
			this.nCatchmePlayCount = nCatchmePlayCount;
		}

		/**
		 * get the current token position integer
		 * 
		 * @return
		 */
		public int getPlayTokenPositionNumber() {

			return playTokenPositionNumber;
		}

		/**
		 * set the current token position
		 * 
		 * @param playTokenPositionNumber
		 */
		public void setPlayTokenPositionNumber(int playTokenPositionNumber) {
			this.playTokenPositionNumber = playTokenPositionNumber;
		}

		/**
		 * set the TicTacToc board
		 * 
		 * @param x
		 * @param y
		 * @param tag
		 */
		public void setTheTicTacTocBoard(int x, int y, int tag) {
			emptySeat--;

			getTictactocBoardStatues()[x][y] = tag;

			if (emptySeat <= 0)
				setTheClientScoreAboutTicTacTok(tag);

			checkTheTicTacTocGameWin(x, y, tag);

		}

		/**
		 * set the initialize the catchMe Board
		 * 
		 * @param x
		 * @param y
		 * @param tag
		 * @param itemNumber
		 * @return
		 */
		public int setTheCatchmeBoard(int x, int y, int tag, int itemNumber) {
			if (emptySeat == Settings.nCatchMeBlockWidth * Settings.nCatchMeBlockHeight)
				pointCatchmeFirstStart = new TicTacTocPoint(x, y);
			emptySeat--;

			getCatchmeBoardStatues()[x][y].setTagNumber(tag);
			getCatchmeBoardStatues()[x][y].setItemset(itemNumber);

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getClientGameTag() == tag)
					if (roomClients.get(i).getnCatchmePlayCount() >= 1) {
						roomClients.get(i).setnCatchmePlayCount(roomClients.get(i).getnCatchmePlayCount() - 1);

						return roomClients.get(i).getnCatchmePlayCount();
					}

			return -1;
		}

		/**
		 * make delay for the sending delay packet
		 * 
		 * @param deltaTime
		 */
		public void delayMethod(int deltaTime) {
			Robot robot;
			try {
				robot = new Robot();
				robot.delay(deltaTime);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/**
		 * check the catch me win about the game and using delay function for
		 * sending packet
		 */
		public void checktheCatchMeGameWin() {
			boolean _isCheck = true;

			for (int i = 0; i < roomClients.size(); i++)
				if (roomClients.get(i).getnCatchmePlayCount() != 0)
					_isCheck = false;

			if (_isCheck == true) {
				int _x = pointCatchmeFirstStart.getX();
				int _y = pointCatchmeFirstStart.getY();
				int _tempX;
				int _tempY;

				while (true) {
					boolean _isNext = false;

					_tempX = _x;
					_tempY = _y;

					if (getCatchmeBoardStatues()[_x][_y].getItemset() == Settings.nCatchMeItemRight) {

						for (int x = _x; x < Settings.nCatchMeBlockWidth; x++)
							if (getCatchmeBoardStatues()[x][_y].getTagNumber() != Settings.ERRORCODE && x != _x) {
								getCatchmeBoardStatues()[_x][_y].setTagNumber(Settings.ERRORCODE);
								_x = x;
								_isNext = true;
								break;
							}

					} else if (getCatchmeBoardStatues()[_x][_y].getItemset() == Settings.nCatchMeItemLeft) {
						for (int x = _x; x >= 0; x--)
							if (getCatchmeBoardStatues()[x][_y].getTagNumber() != Settings.ERRORCODE && x != _x) {
								getCatchmeBoardStatues()[_x][_y].setTagNumber(Settings.ERRORCODE);
								_x = x;
								_isNext = true;
								break;
							}

					} else if (getCatchmeBoardStatues()[_x][_y].getItemset() == Settings.nCatchMeItemDown) {
						for (int y = _y; y < Settings.nCatchMeBlockHeight; y++)
							if (getCatchmeBoardStatues()[_x][y].getTagNumber() != Settings.ERRORCODE && y != _y) {
								getCatchmeBoardStatues()[_x][_y].setTagNumber(Settings.ERRORCODE);
								_y = y;
								_isNext = true;
								break;
							}

					} else if (getCatchmeBoardStatues()[_x][_y].getItemset() == Settings.nCatchMeItemUp) {
						for (int y = _y; y >= 0; y--)
							if (getCatchmeBoardStatues()[_x][y].getTagNumber() != Settings.ERRORCODE && y != _y) {
								getCatchmeBoardStatues()[_x][_y].setTagNumber(Settings.ERRORCODE);
								_y = y;
								_isNext = true;
								break;
							}
					}

					if (false == _isNext) {
						sendMessageInTheRoomPeople(Settings._ANSWER_CATCHME_DRAW_PATH + "", _x + "", _y + "");

						setTheClientScoreAboutCatchmeReverse(getCatchmeBoardStatues()[_x][_y].getTagNumber());
						break;
					} else {
						if (_tempY != _y && _tempX != _x)
							getCatchmeBoardStatues()[_tempX][_tempY].setTagNumber(Settings.ERRORCODE);

						sendMessageInTheRoomPeople(Settings._ANSWER_CATCHME_DRAW_PATH + "", _tempX + "", _tempY + "");

						delayMethod(500);
					}
				}
			}

		}
	}
}
