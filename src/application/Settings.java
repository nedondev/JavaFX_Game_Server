
package application;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class Manage The Global Constant about NetWork Protocol or
 *              Program Constant
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class Settings {

    // Game Network Protocol

    public static final int ZEROINIT = 0;

    /**
     * Error Code -1
     */
    public static final int ERRORCODE = -1;

    /**
     * Check the client Access Status 1
     */
    public static final int _ACCESS_THE_SERVER = 0x00000001;

    /**
     * Request the Room List Information 2
     */
    public static final int _REQUEST_THE_ROOM_LIST_INFORMATION = 0x00000002;

    /**
     * Request the Make Room 3
     */
    public static final int _REQUEST_THE_MAKE_ROOM = 0x00000003;

    /**
     * Request The List View Clicked Item Information 4
     */
    public static final int _REQUEST_CLICKED_ROOM_INFORMATION = 0x00000004;

    /**
     * Answer The Room is Exist 5
     */
    public static final int _ANSWER_THE_ROOM_IS_EXIST = 0x00000005;

    /**
     * Request The Room Member Number 6
     */
    public static final int _REQUEST_ROOM_MEMBER_NUMBER = 0x00000006;

    /**
     * Answer The Room member Number 7
     */
    public static final int _ANSWER_ROOM_MEMBER_NUMBER = 0x00000007;

    /**
     * Request The Game Room List 8
     */
    public static final int _REQUEST_GAME_ROOM_LIST = 0x00000008;

    /**
     * Answer The Game Room List 9
     */
    public static final int _ANSWER_GAME_ROOM_LIST = 0x00000009;

    /**
     * Answer The List View Clicked Item Information 10
     */
    public static final int _ANSWER_CLICKED_ROOM_INFORMATION = 0x0000000a;

    /**
     * Request The Wait Room Clients Sending Message 11
     */
    public static final int _REQUEST_WAITING_ROOM_SENDING_MESSAGE = 0x0000000b;

    /**
     * Answer The Wait Room Clients Sending Message 12
     */
    public static final int _ANSWER_WAITING_ROOM_SENDING_MESSAGE = 0x0000000c;

    /**
     * Request the Login 13
     */
    public static final int _REQUEST_LOGIN = 0x0000000d;

    /**
     * Answer The Login 14
     */
    public static final int _ANSWER_LOGIN = 0x0000000e;

    /**
     * Answer The Room Making Successful 15
     */
    public static final int _ANSWER_THE_ROOM_MAKE_SUCCESS = 0x0000000f;

    /**
     * Request The Out of the Room 16
     */
    public static final int _REQUEST_OUT_OF_THE_ROOM = 0x00000010;

    /**
     * Request The Guest Enter The Room 17
     */
    public static final int _REQUEST_GUEST_ENTER_THE_ROOM = 0x00000011;

    /**
     * Answer The Guest Enter The Room Success 18
     */
    public static final int _ANSWER_GUEST_ENTER_THE_ROOM_SUCCESS = 0x00000012;

    /**
     * Answer The Guest Enter The Room Fail 19
     */
    public static final int _ANSWER_GUEST_ENTER_THE_ROOM_FAIL = 0x00000013;

    /**
     * Answer The Guest Enter The Room Error 20
     */
    public static final int _ANSWER_GUEST_ENTER_THE_ROOM_LOGICAL_ERROR = 0x00000014;

    /**
     * Request Game Room Member Number 21
     */
    public static final int _REQUEST_GAME_ROOM_MEMEBER_NUMBER = 0x00000015;

    /**
     * Answer Game Room Member Number 22
     */
    public static final int _ANSWER_GAME_ROOM_MEMEBER_NUMBER = 0x00000016;

    /**
     * Answer Out of The Room 23
     */
    public static final int _ANSWER_OUT_OF_THE_ROOM = 0x00000017;

    /**
     * Request Terminate the client 24
     */
    public static final int _REQUEST_TERMINATE = 0x00000018;

    /**
     * Request Guest is Sending Message to each other in the Game Room 25
     */
    public static final int _REQUEST_ROOM_MEMEBER_MESSAGE = 0x00000019;

    /**
     * Answer Guest is Sending Message to each other in The Game Room 26
     */
    public static final int _ANSWER_ROOM_MEMEBER_MESSAGE = 0x0000001a;

    /**
     * Answer New Room Guest Entered in the Room 27
     */
    public static final int _ANSWER_NEW_ROOM_MEMEBER_NOTIFICATION = 0x0000001b;

    /**
     * Request new Room Guest Entered in the Room 28
     */
    public static final int _REQUEST_NEW_ROOM_MEMEBER_NOTIFICATION = 0x0000001c;

    /**
     * Request The Game Start 29
     */
    public static final int _REQUEST_START_THE_GAME = 0x0000001d;

    /**
     * Answer The Game Start 30
     */
    public static final int _ANSWER_START_THE_GAME = 0x0000001e;

    /**
     * Answer Room Game Start 31
     */
    public static final int _ANSWER_ROOM_GAME_START = 0x0000001f;

    /**
     * Request Answer Room People Message 32
     */
    public static final int _REQUEST_ANWER_ROOM_GAME_MESSAGE = 0x00000020;

    /**
     * Request TicTacToc Game event 33
     */
    public static final int _REQUEST_TICTACTOC_STONE_EVENT = 0x00000021;

    /**
     * Answer TicTacToc Game event 34
     */
    public static final int _ANSWER_TICTACTOC_STONE_EVENT = 0x00000022;

    /**
     * Answer Set Game Player Unique tag Number 35
     */
    public static final int _ANSWER_SET_GAME_PLAYER_UNIQUE_TAG_NUMBER = 0x00000023;

    /**
     * Request TicTacToc player Name 36
     */
    public static final int _REQUEST_TICTACTOC_TURN_PLAYER_NAME = 0x00000024;

    /**
     * Answer TicTacToc player Name 37
     */
    public static final int _ANSWER_TICTACTOC_TURN_PLAYER_NAME = 0x00000025;

    /**
     * Request GamePlayer Unique Number 38
     */
    public static final int _REQUEST_SET_GAME_PLAYER_UNIQUE_TAG_NUMBER = 0x00000026;

    /**
     * Answer Game end 39
     */
    public static final int _ANSWER_GAME_END = 0x00000027;

    /**
     * Answer Game End Reverse 40
     */
    public static final int _ANSWER_GAME_END_REVERSE = 0x00000028;

    /**
     * Request Register New User 41
     */
    public static final int _REQUEST_REGISTER_NEW_USER = 0x00000029;

    /**
     * Answer Register New User 42
     */
    public static final int _ANSWER_REGISTER_NEW_USER = 0x0000002a;

    /**
     * Answer TicTacTocAi 43
     */
    public static final int _ANSWER_TICTACTOC_AI = 0x0000002b;

    /**
     * Request User Out Of the Server 44
     */
    public static final int _REQUEST_OUT_OF_THE_SERVER = 0x0000002c;

    /**
     * Request User Log Out 45
     */
    public static final int _REQUEST_LOGOUT = 0x0000002d;

    /**
     * Answer TicTacToc Game mode 46
     */
    public static final int _ANSWER_SET_THE_TICTACTOC_COLOR_MODE = 0x0000002e;

    /**
     * Answer TicTactoc Game Setting 47
     */
    public static final int _ANSWER_TICTACTOC_AI_SETTING = 0x0000002f;

    /**
     * Request CatchMe Game Item 48
     */
    public static final int _REQUEST_CATCHM_ESELECT_ITEM = 0x00000030;

    /**
     * Answer CatchMe Game Item 49
     */
    public static final int _ANSWER_CATCHM_ESELECT_ITEM = 0x00000031;

    /**
     * Request CatchMe Game Click Event 50
     */
    public static final int _REQUEST_CATCHME_STONE_EVENT = 0x00000032;

    /**
     * Answer CatchMe Game Click Event 51
     */
    public static final int _ANSWER_CATCHME_STONE_EVENT = 0x00000033;

    /**
     * Answer CatchMe User First Click init Event 52
     */
    public static final int _ANSWER_CATCHME_INIT_PLAY_CLICKED_NUMBER = 0x00000034;

    /**
     * Answer CatchMe Draw End of Path 53
     */
    public static final int _ANSWER_CATCHME_DRAW_PATH = 0x00000035;

    /**
     * Answer CatchMe User Click Count 54
     */
    public static final int _ANSWER_SET_THE_CATCHME_PLAY_COUNT = 0x00000036;

    /**
     * Request Mobile login to server 55
     */
    public static final int _REQUEST_MOBILE_LOGIN = 0x00000037;

    /**
     * Request Mobile login to server 56
     */
    public static final int _REQUEST_MOBILE_MAKE_GAME_UNIVERSE = 0x00000038;

    /**
     * Request Mobile login to server 57
     */
    public static final int _ANSWER_MOBILE_MAKE_GAME_UNIVERSE = 0x00000039;

    /**
     * Request Client METEORGAME STONE SET CLICK EVENT 58
     */
    public static final int _REQUEST_METEORGAME_SET_CLIECK_EVENT = 0x0000003a;

    /**
     * Answer Client METEORGAME STONE SET CLICK EVENT 59
     */
    public static final int _ANSWER_METEORGAME_SET_CLIECK_EVENT = 0x0000003b;

    /**
     * Request Client METEORGAME STONE INIT GAME PLAY 60
     */
    public static final int _REQUEST_METEORGAME_INIT_GAME_PLAY = 0x0000003c;

    /**
     * Answer Client METEORGAME STONE INIT GAME PLAY 61
     */
    public static final int _ANSWER_METEORGAME_INIT_GAME_PLAY = 0x0000003d;

    /**
     * Request Client METEORGAME STONE REINIT GAME PLAY 62
     */
    public static final int _REQUEST_METEORGAME_REINIT_GAME_PLAY = 0x0000003e;

    /**
     * Answer Client METEORGAME STONE REINIT GAME PLAY 63
     */
    public static final int _ANSWER_METEORGAME_REINIT_GAME_PLAY = 0x0000003f;

    /**
     * Answer Client METEORGAME STONE REINIT GAME PLAY 64
     */
    public static final int _REQUEST_METEORGAME_OUT_OF_PLAYER = 0x00000040;

    /**
     * Answer Client METEORGAME STONE REINIT GAME PLAY 65
     */
    public static final int _ANSWER_METEORGAME_OUT_OF_PLAYER = 0x00000041;

    /**
     * Answer Client METEORGAME STONE REINIT GAME PLAY 66
     */
    public static final int _ANSWER_METEORGAME_UNIVERSE_INIT = 0x00000042;

    /**
     * Request METEORGAME STONE REINIT GAME PLAY 67
     */
    public static final int _REQUEST_METEORGAME_PLAYER_MOVING = 0x00000043;

    /**
     * Request METEORGAME STONE REINIT GAME PLAY 68
     */
    public static final int _ANSWER_METEORGAME_PLAYER_MOVING = 0x00000044;

    /**
     * Request METEORGAME STONE REINIT GAME PLAY 69
     */
    public static final int _ANSWER_METEORGAME_PLAY_START = 0x00000045;
    
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 70
     */
    public static final int _REQUEST_METEORGAME_METEOR_DELETE = 0x00000046;
    
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 71
     */
    public static final int _ANSWER_METEORGAME_METEOR_DELETE = 0x00000047;
    
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 72
     */
    public static final int _REQUEST_METEORGAME_METEOR_GAME_FINISH = 0x00000048;
    
    /**
     * Answer METEORGAME STONE REINIT GAME PLAY 73
     */
    public static final int _ANSWER_METEORGAME_METEOR_GAME_FINISH = 0x00000049;
    
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 74
     */
    public static final int _REQEUST_MOBILE_PANGPANG_SCORE  = 0x0000004a;
    
    /**
     * Answer METEORGAME STONE REINIT GAME PLAY 75
     */
    public static final int _ANSWER_MOBILE_PANGPANG_SCORE  = 0x0000004b;
  
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 76
     */
    public static final int _REQUEST_PC_CLIENT_VERSION_CHECK = 0x0000004c;
    
    /**
     * Answer METEORGAME STONE REINIT GAME PLAY 77
     */
    public static final int _ANSWER_PC_CLIENT_VERSION_CHECK = 0x0000004d;
    
	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 78
	 */
	public static final int _REQUEST_PANGPANG_INIT_GAME_PLAY = 0x0000004e;

	/**
	 * Answer METEORGAME STONE REINIT GAME PLAY 79
	 */
	public static final int _ANSWER_PANGPANG_INIT_GAME_PLAY = 0x0000004f;
	
	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 80
	 */
	public static final int _REQUEST_PANGPANG_REINIT_GAME_PLAY = 0x00000050;

	/**
	 * Answer METEORGAME STONE REINIT GAME PLAY 81
	 */
	public static final int _ANSWER_PANGPANG_REINIT_GAME_PLAY = 0x00000051;
	
    /**
     * Request METEORGAME STONE REINIT GAME PLAY 82
     */
    public static final int _ANSWER_PANGPANG_PLAY_START = 0x00000052;
    
	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 83
	 */
	public static final int _REQUEST_PANGPANG_PLAYER_MOVING = 0x00000053;

	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 84
	 */
	public static final int _ANSWER_PANGPANG_PLAYER_MOVING = 0x00000054;
	
	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 85
	 */
	public static final int _REQUEST_METEORGAME_METEOR_PLAYER_SIZE_UP = 0x00000055;
	
	/**
	 * Request METEORGAME STONE REINIT GAME PLAY 86
	 */
	public static final int _ANSWER_METEORGAME_METEOR_PLAYER_SIZE_UP = 0x00000056;

    // Asset Constant

    /**
     * this Valuable using for setting the GameScene Size Width
     */
    public static final int nGameScreenWidth = 480;

    /**
     * this Valuable using for setting the GameScene Size Height
     */
    public static final int nGameScreenHeight = 480;

    /**
     * Char Size for the packet Calculation
     */
    public static final int nOnePacketStringSize = 2;

    /**
     * game prepare prepare constant
     */
    public static final boolean isGamePrepareStart = true;

    /**
     * game prepare stop constant
     */
    public static final boolean isGamePrepareStop = false;

    /**
     * game room start constant
     */
    public static final boolean isRoomGameStart = true;

    /**
     * game room stop constant
     */
    public static final boolean isRoomGameStop = false;

    /**
     * game start token start number
     */
    public static final int nGameTokenStartNumberPosition = 0;

    /**
     * TicTacToc win Count
     */
    public static final int nWinTheTicTacTocCountNumber = 5;

    /**
     * TicTacToc Game Width
     */
    public static final int nTicTacTocBlockWidth = 12;

    /**
     * TicTacToc Game Height
     */
    public static final int nTicTacTocBlockHeight = 9;

    /**
     * CatchMe Game Width
     */
    public static final int nCatchMeBlockWidth = 12;

    /**
     * CatchMe Game Height
     */
    public static final int nCatchMeBlockHeight = 9;

    /**
     * AI Tag Number
     */
    public static final int nComputerAiTagNumber = 999;

    /**
     * AI TicTacToc Constant Weight
     */
    public static final float nAiTicTacTocWeight = 1.2f;

    /**
     * Enemy AI TicTacToc Constant Weight
     */
    public static final int nAiTicTacTocEnemyInitWeight = 1;

    /**
     * Game CatchMe Constant Count
     */
    public static final int nGameCatchMe = 1;

    /**
     * Game TicTacToc Constant Count
     */
    public static final int nGameTicTacToc = 2;

    /**
     * Game MeteorGame Constant Count
     */
    public static final int nGameMeteorGame = 3;
    
	public static final int nGamePangPang = 4;

    /**
     * Game CatchMe Item Left Constant 0
     */
    public static final int nCatchMeItemLeft = 0;

    /**
     * Game CatchMe Item Down Constant 1
     */
    public static final int nCatchMeItemDown = 1;

    /**
     * Game CatchMe Item Up Constant 2
     */
    public static final int nCatchMeItemUp = 2;

    /**
     * Game CatchMe Item Right Constant 3
     */
    public static final int nCatchMeItemRight = 3;

    /**
     * Game CatchMe Item Clicked Maximum Number 3
     */
    public static final int nCatchMeMAXClickCount = 3;

    /**
     * game String Constant CatchMe
     */
    public static final String sGameStringStyleCatchMe = "술래잡기";

    /**
     * game String Constant TicTacToc
     */
    public static final String sGameStringStyleTicTacToc = "오목";
    
    /**
     * game String Constant TicTacToc
     */
    public static final String sGameStringStyleMeteorGame = "운석게임";
    
    public static final String sGameStringStyleSiegeWarefare = "공성게임";

    /**
     * the Game Server Port Number
     */
    public static final int nConnectionServerPortNumber = 5001;

    public static final int nSettingAsteroidNumber = 100;

    public static final int nGameAsteroidSceneWidth = 360;

    public static final int nGameAsteroidSceneHeight = 280;
    

    public static final int nServerMaxCommandLenth = 50;

    public static final int nServerExitDefaultSeconds = 5;

    public static final int nMaximumSizeOfCommandsContainer = 20;

    public static final int nMaximumCommandWordSize = 10;

    public static final String sEncryptKey = "fe8025947de7cd71";

    public static int nReceiveBufferSize = 1000;
    
    public static final double fUpDoubleMeteorSize = 0.5f;
    
    public static final String DBName = "test";
    
    public static final boolean isDevelopingMode = true;
    
    public static String clientVersion = "0.0.1v";
    
    public static int nBuildingTimes;
    
    public static final String sBuildingVersionFileName = "serverV.jrc";
    
    public static final String sServerInfo = "serverinfo.jrc";
    
    public static final String sServerTitle = "J.R.C Server";
    
    public static final String sSenderSplitProtocolToken = "/&";
    
    public static final String sReceiverSplitProtocolToken = "/&&";
    
    
    public static final String sSenderSplitMultipleToken = "@";

    public static final String sReceiverSplitMultipleToken = "@";
    
    public static final int nPangPangMainPlayerHeigh = 15;
    
    public static final String sDatabaseSystemPassword = "-";
    
    public static final String sDatabaseSystemRoot= "root";
    
    public static final String sDataBaseSystemUrl = "jdbc:mysql://localhost";
}
