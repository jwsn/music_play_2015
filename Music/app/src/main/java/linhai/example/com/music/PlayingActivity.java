package linhai.example.com.music;

import java.util.List;
import java.util.Random;

import linhai.example.com.adapter.MusicListAdapter;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.lrc.LrcView;
import linhai.example.com.service.PlayMusicService;
import linhai.example.com.utils.AudioUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;

public class PlayingActivity extends Activity {
	private static final String TAG = "PlayingActivity";
	//private static List<songInfo> musicList;
	//MyListViewAdapter adapter;
	//private ListView listView;
	//private View playingListView;
	
	
	/*** music list info ***/
	private ListView musicListView;
	//private List<AudioInfo> audioInfoList = null;
	private MusicListAdapter musicListAdapter;
	
	/*** Update Handler ***/
	private Handler updateHandler;
	

	private Context context;
	
	//private boolean playinglistscreen = false;
	//private boolean lrcsize = false;
	
	/*** image button ***/
	private ImageButton pauseOrstartBtn;
	private ImageButton nextBtn;
	private ImageButton preBtn;
	private ImageButton backBtn;
	private SeekBar audioTrackBar;
	private TextView curTrackBarTime;
	private TextView finTrackBarTime;
	//private ImageButton playModeBtn;
	//private ImageButton collectbutton;
	//private ImageButton playingListButton;
	//private ImageButton lrcsizeButton;
	//private ImageView songimageview;
	
	//boolean isPlaying = false;
	//private int currentDuration;
	//private String currentSongName;
	//private int currentPosition;

	//private TextView final_text_time;
	//private TextView current_text_time;
	private TextView display_music_name;
	//private TextView lrc_size;
	
	//private SeekBar mLrcSeekBar;
	private static LrcView mLrcView;
	//private LrcHandler mLrcHandler = LrcHandler.getInstance();
	//LrcView fLrcView;
	//private Toast mLrcToast;
	
	//private SeekBar audioTrack;
	
	/*** control flag ***/
	//private boolean bPlayingFlag = false;
	//private boolean bPauseFlag = false;
	//private boolean bFirstTimePlayFlag = true;
	//private int curMusicPos;
	/*** play mode control ***/
	//private int playMode = GlobalConstant.NORMAL_PLAY_MODE;
	
	/*** Receiver ***/
	private  PlayingActivityReceiver PAReceiver;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.playing_activity);
    	
    	getDataFromIntent();
        initButtonView();
        setButtonListener();
    	
    	/*����һ��handler�����ڸ���UI�Ĳ���*/
    	updateHandler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			switch(msg.what)
    			{
    				case GlobalConstant.UPDATE_LRC_VIEW:
    					if(MainActivity.bPlayingFlag)
    					{
    						Log.v(TAG, "handleMessage->UPDATE_LRC_VIEW");
    						//update LRC VIEW
    						mLrcView.seekTo(PlayMusicService.getCurrentPlayPos(), true,false);
    						//update trackbar time
    						int position = PlayMusicService.getCurrentPlayPos();
    						//int total = PlayMusicService.getDuration();
    						int total = (int)MainActivity.audioInfoList.get(MainActivity.curMusicPos).getDuration();
    						int max = audioTrackBar.getMax();
    						if(position>=0 && total!=0)
    						{
    							audioTrackBar.setProgress(position*max/total);
    						}
    						
    						String time = TimeformatChange(position);
    						curTrackBarTime.setText(time);
    					}
    					break;
    				default:
    					break;
    			}
    		}
    	};
    	
    	/***  start a thread  ***/
    	updateRunnable updateRun = new updateRunnable();
    	(new Thread(updateRun)).start();
    	
    	/*** Receiver ***/
        /*** register the MainActivityReceiver ***/
        PAReceiver = new PlayingActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(GlobalConstant.PLAY_NEXT_BOARDCAST);
        intentFilter.addAction(GlobalConstant.UPDATE_LRC_LIST);
        registerReceiver(PAReceiver, intentFilter);
    }
    
    private void getDataFromIntent(){

    }
    
    private void initButtonView(){
    	Log.d(TAG, "initButtonView");
    	display_music_name = (TextView)findViewById(R.id.music_name_text);
    	pauseOrstartBtn = (ImageButton)findViewById(R.id.playing_pause_or_start);
    	nextBtn = (ImageButton)findViewById(R.id.playing_next);
    	preBtn = (ImageButton)findViewById(R.id.playing_prev);
    	backBtn = (ImageButton)findViewById(R.id.backbutton);
    	audioTrackBar = (SeekBar)findViewById(R.id.audioTrackBar);
    	curTrackBarTime = (TextView)findViewById(R.id.current_time_text);
    	finTrackBarTime = (TextView)findViewById(R.id.final_time_text);
    	mLrcView = (LrcView)findViewById(R.id.song_lrc_text);
    	setLrcView();
    	setTrackBarTime();
    	setPauseOrPlayBtn();
    }
    
    private void setButtonListener(){
    	Log.d(TAG, "setButtonListener");
   	 	//String duration = TimeformatChange((int)MainActivity.audioInfoList.get(MainActivity.curMusicPos).getDuration());
    	ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
    	pauseOrstartBtn.setOnClickListener(viewOnClickListener);
    	nextBtn.setOnClickListener(viewOnClickListener);
    	preBtn.setOnClickListener(viewOnClickListener);
    	backBtn.setOnClickListener(viewOnClickListener);
    	//curTrackBarTime.setText("00:00");
    	//finTrackBarTime.setText(duration);
    	audioTrackBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }
    
    private void setLrcView(){
    	Log.d(TAG, "setLrcView");
    	mLrcView.setLrcContents(PlayMusicService.getLrcList());
    	
    }
    
    private void setTrackBarTime(){
    	Log.d(TAG, "setTrackBarTime");
    	String duration = TimeformatChange((int)MainActivity.audioInfoList.get(MainActivity.curMusicPos).getDuration());
    	//curTrackBarTime.setText("00:00");
    	finTrackBarTime.setText(duration);
    }
    
    private class ViewOnClickListener implements OnClickListener{
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	    	Log.d(TAG, "ViewOnClickListener");
	    	switch(v.getId()){
	    	case R.id.playing_pause_or_start:{
	    		pauseOrstartBtnClickHandler();
	    	}
	    	break;
	    	case R.id.playing_prev:{
	    		preBtnClickHandler();
	    		//setLrcView();
	    	}
	    	break;
	    	case R.id.playing_next:{
	    		nextBtnClickHandler();
	    		//setLrcView();
	    	}
	    	break;
	    	case R.id.backbutton:{
	        	Intent intent = new Intent();
	        	setResult(RESULT_OK, intent);
	        	finish();
	    	}
	    	break;
	    	default:{
	    	}
	    	break;
	    	}
		}
    }
    
    private void pauseOrstartBtnClickHandler(){
    	Log.d(TAG, "pauseOrstartBtnClickHandler");
    	
		Intent intent = new Intent();
		if(MainActivity.bFirstTimePlayFlag == true){
			MainActivity.bPlayingFlag = true;
			MainActivity.bPauseFlag = false;
			MainActivity.bFirstTimePlayFlag = false;
			setPauseOrPlayBtn();
			intent.setAction(GlobalConstant.MUSIC_SERVICE);
			intent.putExtra(GlobalConstant.SONG_PATH_KEY,MainActivity.audioInfoList.get(MainActivity.curMusicPos).getUrl());
			intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_FIRST);
			startService(intent);
			//setLrcView();
		}
		else if(MainActivity.bPlayingFlag == false){
			MainActivity.bPlayingFlag = true;
			MainActivity.bPauseFlag = false;
			setPauseOrPlayBtn();
			//pauseOrstartBtn.setBackgroundResource(R.drawable.play_button);
			intent.setAction(GlobalConstant.MUSIC_SERVICE);
			intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_RESUME);
			startService(intent);
			
		}else{
			MainActivity.bPlayingFlag = false;
			MainActivity.bPauseFlag = true;
			setPauseOrPlayBtn();
			//pauseOrstartBtn.setBackgroundResource(R.drawable.pause_button);
			intent.setAction(GlobalConstant.MUSIC_SERVICE);
			intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PAUSE);
			startService(intent);
		}
    }
    
    private void setPauseOrPlayBtn(){
    	Log.d(TAG, "setPauseOrPlayBtn");
    	if(MainActivity.bPauseFlag == true){
    		pauseOrstartBtn.setBackgroundResource(R.drawable.play_button);
    	}else{
    		pauseOrstartBtn.setBackgroundResource(R.drawable.pause_button);
    	}
    	display_music_name.setText(MainActivity.audioInfoList.get(MainActivity.curMusicPos).getTitle());
    }
    
    private void preBtnClickHandler(){
    	Log.d(TAG, "preBtnClickHandler");
    	
    	setCurrentPlayPositionWhenPlayPre();
		if(MainActivity.curMusicPos < 0){
			MainActivity.curMusicPos = 0;
			Toast.makeText(this, "�Ѿ�û����һ����", Toast.LENGTH_SHORT).show();
		}else{
			setPauseOrPlayBtn();
			if(MainActivity.bPauseFlag == true){
				MainActivity.bFirstTimePlayFlag = true;
				mLrcView.setLrcContents(null);
		    	setTrackBarTime();
				return;
			}
			MainActivity.bPlayingFlag = true;
			MainActivity.bPauseFlag = false;
			MainActivity.bFirstTimePlayFlag = false;
			Intent intent = new Intent();
			intent.setAction(GlobalConstant.MUSIC_SERVICE);
			intent.putExtra(GlobalConstant.SONG_PATH_KEY, MainActivity.audioInfoList.get(MainActivity.curMusicPos).getUrl());
			intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PRE);
			startService(intent);
			//setLrcView();
			
		}
    }
    
    private void nextBtnClickHandler(){
    	Log.d(TAG, "nextBtnClickHandler");
    	
    	setCurrentPlayPositionWhenPlayNext();
		if( MainActivity.curMusicPos >= MainActivity.audioInfoList.size() ){
			MainActivity.curMusicPos = MainActivity.audioInfoList.size() - 1;
			Toast.makeText(this, "�������һ����", Toast.LENGTH_SHORT).show();
		}else{
			setPauseOrPlayBtn();
			if(MainActivity.bPauseFlag == true){
				MainActivity.bFirstTimePlayFlag = true;
				mLrcView.setLrcContents(null);
		    	setTrackBarTime();
				return;
			}

			MainActivity.bPlayingFlag = true;
			MainActivity.bPauseFlag = false;
			MainActivity.bFirstTimePlayFlag = false;
			Intent intent = new Intent();
			intent.setAction(GlobalConstant.MUSIC_SERVICE);
			intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_NEXT);
			intent.putExtra(GlobalConstant.SONG_PATH_KEY, MainActivity.audioInfoList.get(MainActivity.curMusicPos).getUrl());
			startService(intent);
			//setLrcView();
		}
    }

    private void setCurrentPlayPositionWhenPlayPre(){
    	Log.d(TAG, "setCurrentPlayPositionWhenPlayPre");

    	switch(MainActivity.playMode){
    		case GlobalConstant.NORMAL_PLAY_MODE:{
    			MainActivity.curMusicPos--;
    		}
    		break;
    		case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
    			MainActivity.curMusicPos--;
    			if(MainActivity.curMusicPos < 0){
    				MainActivity.curMusicPos = MainActivity.audioInfoList.size() - 1;
    			}
    		}
    		break;
    		case GlobalConstant.RANDOM_PLAY_MODE:{
    			Random rand = new Random();
    			int randPos = rand.nextInt(MainActivity.audioInfoList.size());
    			MainActivity.curMusicPos = randPos % (MainActivity.audioInfoList.size());
    		}
    		break;
    		case GlobalConstant.REPEAT_ONE_PLAY_MODE:
    		default:
    		break;
    	}
    	//return curMusicPos;
    }
    
    private void setCurrentPlayPositionWhenPlayNext(){
    	Log.d(TAG, "setCurrentPlayPositionWhenPlayNext");

    	switch(MainActivity.playMode){
    		case GlobalConstant.NORMAL_PLAY_MODE:{
    			MainActivity.curMusicPos++;
    		}
    		break;
    		case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
    			MainActivity.curMusicPos++;
    			if(MainActivity.curMusicPos > MainActivity.audioInfoList.size() - 1){
    				MainActivity.curMusicPos = 0;
    			}
    		}
    		break;
    		case GlobalConstant.RANDOM_PLAY_MODE:{
    			Random rand = new Random();
    			int randPos = rand.nextInt(MainActivity.audioInfoList.size());
    			MainActivity.curMusicPos = randPos % (MainActivity.audioInfoList.size());
    		}
    		break;
    		case GlobalConstant.REPEAT_ONE_PLAY_MODE:
    		default:
    		break;
    	}
    }
    
    private class updateRunnable implements Runnable{
    	@Override
    	public void run(){
    		while(true){
    			if(MainActivity.bPlayingFlag == true){
    				/*** update Lrcview and trackBarTime  ***/
    				Message updateLrcMsg = new Message();
    				updateLrcMsg.what = GlobalConstant.UPDATE_LRC_VIEW;
    				updateHandler.sendMessage(updateLrcMsg);
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
    
    /*** reveiver ***/
    public class PlayingActivityReceiver extends BroadcastReceiver{
    	@Override
    	public void onReceive(Context context, Intent intent){
    		String Action = intent.getAction();
    		if(Action.equals(GlobalConstant.PLAY_NEXT_BOARDCAST)){
    			Log.d(TAG, "onReceiver->PLAY_NEXT_BOARDCAST");
    			nextBtnClickHandler();
    		}else if(Action.equals(GlobalConstant.UPDATE_LRC_LIST)){
    			Log.d(TAG, "onReceiver->UPDATE_LRC_LIST");
    			setLrcView();
    			setTrackBarTime();
    		}
    	}
    }
    
    
	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.v(TAG, "onProgressChanged");
			if(seekBar == audioTrackBar){
				
				int max = audioTrackBar.getMax();
				int total = (int)MainActivity.audioInfoList.get(MainActivity.curMusicPos).getDuration();
				
				if(fromUser)
				{
					if(progress>=0 && total!=0)
					{
						Intent intent = new Intent();
						intent.setAction(GlobalConstant.MUSIC_SERVICE);
						intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_SEEK);
						intent.putExtra(GlobalConstant.SONG_PATH_KEY, MainActivity.audioInfoList.get(MainActivity.curMusicPos).getUrl());
						intent.putExtra(GlobalConstant.PLAY_SEEK_KEY, progress*total/max);
						startService(intent);
						//PlayMusicService.seekTo(progress*total/max);
					}
				}
				/*���ƽ�����ϻ���*/
				
				if(fromUser)
				{
					//mLrcView.seekTo(progress*total/max, true,fromUser);
//					if(fLrcView!=null)
//					fLrcView.seekTo(progress*total/max, true,fromUser);
				}
				
				
			}
		}

	};
    
	/*�˺�������ת��ʱ���ʽ����Ϊ���ǻ�ȡ����ʱ���Ǻ��룬����������԰Ѻ���ת���ɷ�+��ĸ�ʽ����02:23����*/
	public static String TimeformatChange(int msec)
	{
		Log.v(TAG, "TimeformatChange");
		String min = msec/(1000*60) +"";
		String sec = msec%(1000*60)+"";
		if(min.length()<2)
			min = "0"+msec/(1000*60);
		
		if (sec.length() == 4)  
			sec = "0" + (msec % (1000 * 60)) + "";  
	    else if (sec.length() == 3)  
	    	sec = "00" + (msec % (1000 * 60)) + "";  
	    else if (sec.length() == 2)   
	    	sec = "000" + (msec % (1000 * 60)) + "";  
		else if (sec.length() == 1)   
	        sec = "0000" + (msec % (1000 * 60)) + "";  
	   
		return min+":"+sec.trim().substring(0, 2);
		
	}
    @Override
    public void onBackPressed(){
    	Log.d(TAG, "onBackPressed");
    	
    	Intent intent = new Intent();
    	setResult(RESULT_OK, intent);
    	finish();
    }
	
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(PAReceiver);
    }
}