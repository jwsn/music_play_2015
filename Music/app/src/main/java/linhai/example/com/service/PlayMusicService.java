package linhai.example.com.service;

import java.io.IOException;
import java.util.List;

import linhai.example.com.audio.AudioInfo;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.databaseHelper.HistoryTable;
import linhai.example.com.databaseHelper.MusicDatabaseHelper;
import linhai.example.com.lrc.LrcContent;
import linhai.example.com.lrc.LrcHandler;
import linhai.example.com.music.MainActivity;
import linhai.example.com.utils.ControlUtils;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

import org.litepal.crud.DataSupport;

public class PlayMusicService extends Service{
	private static final String TAG = "PlayMusicService";
	
	private static MediaPlayer mediaPlayer;

	private String songPath;
    private String songName;
    private int songPos;

	/*** control flag ***/
	private boolean isPause = false;
	
	/*** Lrc display ***/
	private LrcHandler mLrcHandler;
	private static List<LrcContent> lrcList;
	private static int lrcList_index = 0;
	private String mediaPlayerSongPath;
	private static int mediaPlayer_currentTime;
	private static int mediaPlayer_duration;
	private static int currentPlayPos = 0;

    /*** dataBaseHelper ***/
    private MusicDatabaseHelper dbHelper = new MusicDatabaseHelper(this, "history.db", null, 1);



	/*** a runnable ***/
	//private serviceRunnable updatRun;
	
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	
	@Override
	public void onCreate(){
		Log.d(TAG, "onCreate");
		
		mediaPlayer = new MediaPlayer();
		
		//start a thread
        serviceRunnable sr = new serviceRunnable();
        (new Thread(sr)).start();

		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp){
				Log.d(TAG, "onCompletion");
				sendPlayNextBroadcast();
			}
		});
		
		
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(TAG, "onStartCommand");
		
		if(mediaPlayer == null || intent == null){
			Log.i(TAG, "mediaPlayer == null || intent == null");
			return -1;
		}
			
		int playControlFlag = intent.getIntExtra(GlobalConstant.PLAY_CONTROL, 1);

		currentPlayPos = 0;
		switch(playControlFlag){
			case GlobalConstant.PLAY_RESUME:{
				playResume();
			}
			break;
			case GlobalConstant.PLAY_FIRST:{
                songPath = intent.getStringExtra(GlobalConstant.SONG_PATH_KEY);
                songName = intent.getStringExtra(GlobalConstant.SONG_NAME_KEY);
                songPos  = intent.getIntExtra(GlobalConstant.SONG_POS_KEY, 0);
				playFirst();
			}
			break;
			case GlobalConstant.PLAY_NEXT:{
                songPath = intent.getStringExtra(GlobalConstant.SONG_PATH_KEY);
                songName = intent.getStringExtra(GlobalConstant.SONG_NAME_KEY);
                songPos  = intent.getIntExtra(GlobalConstant.SONG_POS_KEY, 0);
				playNext();		
			}
			break;
			case GlobalConstant.PLAY_PAUSE:{
				playPause();
			}
			break;
			case GlobalConstant.PLAY_PRE:{
                songPath = intent.getStringExtra(GlobalConstant.SONG_PATH_KEY);
                songName = intent.getStringExtra(GlobalConstant.SONG_NAME_KEY);
                songPos  = intent.getIntExtra(GlobalConstant.SONG_POS_KEY, 0);
				playPre();
			}
			case GlobalConstant.PLAY_SEEK:{
				int seekValue = intent.getIntExtra(GlobalConstant.PLAY_SEEK_KEY, 0);
				playSeek(seekValue);
			}
			break;
			default:
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void playPause(){
		Log.d(TAG, "GlobalConstant.PLAY_PAUSE");
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}
	
	private void playSeek(int value){
		Log.d(TAG, "PlaySeek");
		mediaPlayer.seekTo(value);
	}
	
	private void playPre(){
		Log.d(TAG, "GlobalConstant.PLAY_PRE");
		play();
	} 
	
	private void playNext(){
		Log.d(TAG, "GlobalConstant.PLAY_NEXT");
		play();
	}
	
	private void playResume(){
		Log.d(TAG, "GlobalConstant.PLAY_RESUME");
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}
	
	private void playFirst(){
		Log.d(TAG, "GlobalConstant.PLAY_FIRST");
		if(isPause == true)
			isPause = false;
		
		play();
	}
	
	private void play(){
		Log.d(TAG, "play");
		
		if(isPause)
		{
			return;
		}

		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		
		mediaPlayer.reset();
		try{
			isPause = false;
			mediaPlayer.setDataSource(songPath);
			mediaPlayer.prepare();
			mediaPlayer.start();
			//update the lrc
			initLrc();
			sendUpdateLrcListBroadcast();

			/*** save in history table*/
            //SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Cursor cursor = db.query("history", null, null, null, null, null, null);
            boolean isInserted = false;
            //if(cursor.moveToFirst()) {
                //do {
			List<HistoryTable> hisList = DataSupport.findAll(HistoryTable.class);
			for(HistoryTable h : hisList) {
				//String name = cursor.getString(cursor.getColumnIndex("name"));
				String name = h.getName();
				if (name.equals(songName)) {
					isInserted = true;
				}
			}
                //} while (cursor.moveToNext());
            //}
            if(isInserted == false) {
                //ContentValues values = new ContentValues();
                //values.put("name", songName);
                //values.put("path", songPath);
                //values.put("pos", songPos);
                //db.insert("history", null, values);
				HistoryTable h = new HistoryTable();
				h.setName(songName);
				h.setPos(songPos);
				h.setPath(songPath);
				h.save();
            }
            //cursor.close();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*** send to MainActivity play next song ***/
	private void sendPlayNextBroadcast(){
		Log.d(TAG, "sendPlayNextBroadcast");
		Intent intent = new Intent();
		intent.setAction(GlobalConstant.PLAY_NEXT_BOARDCAST);
		sendBroadcast(intent);
	}
	
	/*** send to PlayingActivity to update LRC ***/
	private void sendUpdateLrcListBroadcast(){
		Log.d(TAG, "sendUpdateLrcListBroadcast");
		Intent intent = new Intent();
		intent.setAction(GlobalConstant.UPDATE_LRC_LIST);
		sendBroadcast(intent);
	}
	
	public void initLrc(){
		Log.d(TAG, "initLrc()");
		
		lrcList= LrcHandler.getInstance().readLRC(songPath);
		//mLrcHandler.getLrcContent(mediaPlayerSongPath);
		//lrcList = mLrcHandler.readLRC(songPath);
	}
	
	public static int lrcIndex(){
		Log.v(TAG, "lrcIndex()");
		if(mediaPlayer == null){
			return 0;
		}

		if(mediaPlayer.isPlaying()){
			mediaPlayer_currentTime = mediaPlayer.getCurrentPosition();
			mediaPlayer_duration = mediaPlayer.getDuration();
			if(mediaPlayer_currentTime < mediaPlayer_duration){
				for(int i = 0; i < lrcList.size(); i++){
					if(i < lrcList.size() - 1){
						if(mediaPlayer_currentTime < lrcList.get(i).getTime() && i == 0){
							lrcList_index = i;
						}
						if(mediaPlayer_currentTime > lrcList.get(i).getTime() && mediaPlayer_currentTime < lrcList.get(i + 1).getTime()){
							lrcList_index = i;
						}
					}
					if(i == lrcList.size() - 1 && mediaPlayer_currentTime > lrcList.get(i).getTime()){
						lrcList_index = i;
					}
				}
			}
		}
		return lrcList_index;
	}
	
	
	public static void setlrcIndex(int index)
	{
		lrcList_index = index;
		//mediaPlayer_currentTime =  lrcList.get(index).getLrcTime();
		//mediaPlayer.seekTo(mediaPlayer_currentTime);
		
	}
	
	public static List<LrcContent> getLrcList()
	{	
		return lrcList;
	}
	
	//public static int getCurrentPlayPosition()
	{
		//return mediaPlayer.getCurrentPosition();
	}
	
    private class serviceRunnable implements Runnable{
    	@Override
    	public void run(){
    		while(true){
                Log.d(TAG, "serviceRunnable");
    			if(ControlUtils.bPlayingFlag == true){
    				if(mediaPlayer != null){
    					if(mediaPlayer.isPlaying()){
                            Log.i(TAG, "mediaPlayer.isplaying()");
    						currentPlayPos = mediaPlayer.getCurrentPosition();
    					}
    				}
    			}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
	
    public static int getCurrentPlayPos(){
    	Log.v(TAG, "getCurrentPlayPos");
    	return currentPlayPos;
    }
	
	@Override
	public void onDestroy(){
		Log.d(TAG, "onDestroy");
		
		super.onDestroy();
	}

    public static void serviceStart(Context context, int playControlKey, AudioInfo audioInfo, int pos){
        Log.d(TAG, context.getClass().getName() + "serviceStart");

        Intent intent = new Intent();
        intent.setAction(GlobalConstant.MUSIC_SERVICE);
        intent.putExtra(GlobalConstant.PLAY_CONTROL, playControlKey);
        intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfo.getUrl());
        intent.putExtra(GlobalConstant.SONG_POS_KEY, pos);
        intent.putExtra(GlobalConstant.SONG_NAME_KEY, audioInfo.getTitle());
        context.startService(intent);
    }
}