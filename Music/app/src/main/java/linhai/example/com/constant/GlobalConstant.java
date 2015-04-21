package linhai.example.com.constant;

public class GlobalConstant{
	
	public static final String MUSIC_SERVICE = "com.example.service.PlayMusicService";
	public static final String PLAY_CONTROL = "play_control_msg";
	public static final String SONG_PATH_KEY = "song_path_key";
	public static final String PLAY_SEEK_KEY = "play_seek_key";
	
	public static final int PLAY_RESUME = 0;
	public static final int PLAY_FIRST = 1;
	public static final int PLAY_PAUSE = 2;
	public static final int PLAY_NEXT = 3;
	public static final int PLAY_PRE = 4;
	public static final int PLAY_SEEK = 5;
	
	/*** MainActivity Receiver ***/
	public static final String PLAY_NEXT_BOARDCAST = "com.example.PLAY_NEXT";
	
	/*** PlayingActivity Receiver ***/
	public static final String UPDATE_LRC_LIST = "com.example.UPDATE_LRC_LIST";
	/*** play mode **
	public enum PlayMode{
		NORMAL_PLAY_MODE,
		REPEAT_ALL_PLAY_MODE,
		REPEAT_ONE_PLAY_MODE,
		RANDOM_PLAY_MODE
	};*/
	
	public static final int NORMAL_PLAY_MODE = 0;
	public static final int REPEAT_ALL_PLAY_MODE = 1;
	public static final int REPEAT_ONE_PLAY_MODE = 2;
	public static final int RANDOM_PLAY_MODE = 3;
	
	/*** MainActivity send para to PlayingActivity***/
	/*
	private boolean bPlayingFlag = false;
	private boolean bPauseFlag = false;
	private boolean bFirstTimePlayFlag = true;
	private int curMusicPos;
	private GlobalConstant.PlayMode playMode = GlobalConstant.PlayMode.NORMAL_PLAY_MODE;
	*/
	public static final String CURRENT_POS_KEY = "curMusicPos_key";
	public static final String BFIRST_TIME_FLAG_KEY = "bFirstTimePlayFlag_key";
	public static final String BPAUSE_FLAG_KEY = "bPauseFlag_key";
	public static final String BPLAYINGFLAG_KEY = "bPlayingFlag_key";
	public static final String PLAYMODE_KEY = "PlayMode_key";
	
	/*** Message info ***/
	public static final int UPDATE_LRC_VIEW = 100;
	public static final int UPDATE_TRACKBAR_TIME = 101;
	
	public final static int LOCAL_MUSIC = 0;
	public final static int COLLECT_MUSIC = 1;
	public final static int HISTORY_MUSIC = 2;
	public final static int SEARCH_MUSIC = 3;
	public final static int SETTING_MUSIC = 4;
	
	
}