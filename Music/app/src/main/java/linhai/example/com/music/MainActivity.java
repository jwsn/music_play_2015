package linhai.example.com.music;

import java.util.List;
import java.util.Random;
//import com.example.adapter.MusicListAdapter;
//import com.example.audio.AudioInfo;
//import com.example.constant.GlobalConstant;
//import com.example.utils.AudioUtils;
import linhai.example.com.adapter.MusicListAdapter;
import linhai.example.com.adapter.MyAdapter;
import linhai.example.com.adapter.MyLinearLayout;
import linhai.example.com.adapter.MyListView;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.baseview.SwipeBackActivity;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.service.PlayMusicService;
import linhai.example.com.utils.AudioUtils;
import linhai.example.com.utils.ControlUtils;
import linhai.example.com.utils.ImageUtils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.musicplayer.R;

public class MainActivity extends SwipeBackActivity implements MyLinearLayout.OnScrollListener, View.OnClickListener{
	private final String TAG = "MainActivity";
	
	/*** music list info ***/
	private ListView musicListView;
	public static List<AudioInfo> audioInfoList = null;
	private MusicListAdapter musicListAdapter;
	private int musicListPos;

	
	/*** image button ***/
	private ImageButton pauseOrstartBtn;
	private ImageButton nextBtn;
	private ImageButton preBtn;
	private ImageButton songImageBtn;
	private ImageButton playModeBtn;
	private TextView songNameDisplay;

	/*** control flag ***/
	//public static boolean bPlayingFlag = false;
	//public static boolean bPauseFlag = true;
	//public static boolean bFirstTimePlayFlag = true;
    //public static int curMusicPos;
    //public static int playMode = GlobalConstant.NORMAL_PLAY_MODE;

	/*** Receiver ***/
	public MainActivityReceiver maReceiver;
	
	/*** play mode control ***/
	//private GlobalConstant.PlayMode playMode = GlobalConstant.PlayMode.NORMAL_PLAY_MODE;
	
	//private long exitTime = 0;

    private MyLinearLayout mLastScrollView;
    private MyAdapter myAdapter;
    private MyListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* init music list view*/
        musicListPos = 0;
        musicListView = (ListView) findViewById(R.id.music_list);
        //myListView = (MyListView)findViewById(R.id.music_list);
        audioInfoList = AnimationActivity.audioInfoList;
        musicListAdapter = new MusicListAdapter(this, audioInfoList);
        //myAdapter = new MyAdapter(this, AnimationActivity.mDataList, this, this);
        //myListView.setAdapter(myAdapter);

        musicListView.setAdapter(musicListAdapter);
        musicListView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            	Log.d(TAG, "setOnItemClickListener->onItemClick");
            	ControlUtils.curMusicPos = position;
        		AudioInfo audioInfo = audioInfoList.get(ControlUtils.curMusicPos);
                ControlUtils.bFirstTimePlayFlag = false;
                ControlUtils.bPlayingFlag = true;
                ControlUtils.bPauseFlag = false;
        		setPauseOrPlayBtn();
                PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_FIRST, audioInfo, ControlUtils.curCollPos);
        	}
        });

        /*** listen the call state changed ***/
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListenHander(), PhoneStateListener.LISTEN_CALL_STATE);
        
        initButtonView();
        setButtonListener();
        
        /*** register the MainActivityReceiver ***/
        maReceiver = new MainActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalConstant.PLAY_NEXT_BOARDCAST);
        registerReceiver(maReceiver, intentFilter);
    }

    private void initButtonView(){
    	Log.i(TAG, "initButtonView");
    	pauseOrstartBtn = (ImageButton)findViewById(R.id.pause_or_start_btn);
    	nextBtn = (ImageButton)findViewById(R.id.next_btn);
    	preBtn = (ImageButton)findViewById(R.id.pre_btn);
    	playModeBtn = (ImageButton)findViewById(R.id.playmode);
        playModeBtn.setBackgroundResource(GlobalConstant.playMode[ControlUtils.playMode]);
    	songImageBtn = (ImageButton)findViewById(R.id.playingimageview);
    	songNameDisplay = (TextView)findViewById(R.id.mylocal_song_name_text);
    	setPauseOrPlayBtn();
        setSongImageBtn();
    }

    private void setButtonListener(){
    	Log.i(TAG, "setButtonListener");
    	ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
    	pauseOrstartBtn.setOnClickListener(viewOnClickListener);
    	nextBtn.setOnClickListener(viewOnClickListener);
    	preBtn.setOnClickListener(viewOnClickListener);
    	playModeBtn.setOnClickListener(viewOnClickListener);
    	songImageBtn.setOnClickListener(viewOnClickListener);
    }
    
    private void setPauseOrPlayBtn(){
    	Log.d(TAG, "setPauseOrPlayBtn");
    	if(ControlUtils.bPauseFlag == true){
    		pauseOrstartBtn.setBackgroundResource(R.drawable.play_button);
    	}else{
    		pauseOrstartBtn.setBackgroundResource(R.drawable.pause_button);
    	}
    	songNameDisplay.setText(audioInfoList.get(ControlUtils.curMusicPos).getDisplayName());
        setSongImageBtn();
    }

    private void setSongImageBtn(){
        Bitmap bitmap = ImageUtils.getInstance().getArtwork(MainActivity.this, audioInfoList.get(ControlUtils.curMusicPos).getId(), audioInfoList.get(ControlUtils.curMusicPos).getAlbumId(), true, false);
        if(bitmap == null){
            songImageBtn.setBackgroundResource(R.drawable.playing_button);
        }else{
            songImageBtn.setBackground(null);
            songImageBtn.setImageBitmap(bitmap);
        }
    }

    private class ViewOnClickListener implements OnClickListener{
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	    	Log.d(TAG, "ViewOnClickListener");
	    	switch(v.getId()){
	    	case R.id.pause_or_start_btn:{
	    		pauseOrstartBtnClickHandler();
	    	}
	    	break;
	    	case R.id.pre_btn:{
	    		preBtnClickHandler();
	    	}
	    	break;
	    	case R.id.next_btn:{
	    		nextBtnClickHandler();
	    	}
	    	break;
	    	case R.id.playingimageview:{
	    		playingImageBtnClickHandler();
	    	}
	    	break;
	    	case 
	    	R.id.playmode:{
	    		playModeBtnClickHandler();
	    	}
	    	break;
	    	default:{
	    		
	    	}
	    	break;
	    	}
		}
    }
    
    
    private void preBtnClickHandler(){
    	Log.d(TAG, "preBtnClickHandler");

        ControlUtils.curMusicPos = ControlUtils.getInstance().setCurrentPlayPositionWhenPlayPre(ControlUtils.curMusicPos, audioInfoList);
		if(ControlUtils.curMusicPos < 0){
            ControlUtils.curMusicPos = 0;
			Toast.makeText(MainActivity.this, "没有上一首啦", Toast.LENGTH_SHORT).show();
		}else{
			setPauseOrPlayBtn();
			if(ControlUtils.bPauseFlag == true){
                ControlUtils.bFirstTimePlayFlag = true;
				return;
			}

            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
			//Intent intent = new Intent();
			//intent.setAction(GlobalConstant.MUSIC_SERVICE);
			//intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curMusicPos).getUrl());
			//intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PRE);
			//startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
            PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_PRE, audioInfo, ControlUtils.curCollPos);
		}
    }
    
    private void nextBtnClickHandler(){
    	Log.d(TAG, "nextBtnClickHandler");

        ControlUtils.curMusicPos = ControlUtils.getInstance().setCurrentPlayPositionWhenPlayNext(ControlUtils.curMusicPos, audioInfoList);
		if( ControlUtils.curMusicPos >= audioInfoList.size() ){
            ControlUtils.curMusicPos = audioInfoList.size() - 1;
			Toast.makeText(MainActivity.this, "这是最后一首啦", Toast.LENGTH_SHORT).show();
		}else{
			setPauseOrPlayBtn();
			if(ControlUtils.bPauseFlag == true){
                ControlUtils.bFirstTimePlayFlag = true;
				return;
			}

            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
			//Intent intent = new Intent();
			//intent.setAction(GlobalConstant.MUSIC_SERVICE);
			//intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_NEXT);
			//intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curMusicPos).getUrl());
			//startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
            PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_NEXT, audioInfo, ControlUtils.curCollPos);
		}
    }
    
    private void pauseOrstartBtnClickHandler(){
    	Log.d(TAG, "pauseOrstartBtnClickHandler");
    	
		//Intent intent = new Intent();
		if(ControlUtils.bFirstTimePlayFlag == true){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
			setPauseOrPlayBtn();
			//intent.setAction(GlobalConstant.MUSIC_SERVICE);
			//intent.putExtra(GlobalConstant.SONG_PATH_KEY,audioInfoList.get(ControlUtils.curMusicPos).getUrl());
			//intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_FIRST);
			//startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
            PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_FIRST, audioInfo, ControlUtils.curCollPos);
		}
		else if(ControlUtils.bPlayingFlag == false){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
			setPauseOrPlayBtn();
			//intent.setAction(GlobalConstant.MUSIC_SERVICE);
			//intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_RESUME);
			//startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
            PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_RESUME, audioInfo, ControlUtils.curCollPos);
			
		}else{
            ControlUtils.bPlayingFlag = false;
            ControlUtils.bPauseFlag = true;
			setPauseOrPlayBtn();
			//intent.setAction(GlobalConstant.MUSIC_SERVICE);
			//intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PAUSE);
			//startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
            PlayMusicService.serviceStart(MainActivity.this, GlobalConstant.PLAY_PAUSE, audioInfo, ControlUtils.curCollPos);
		}
    }
    
    private void playModeBtnClickHandler(){
    	Log.d(TAG, "playModeBtnClickHandler");
    	switch(ControlUtils.playMode){
    		case GlobalConstant.NORMAL_PLAY_MODE:{
                ControlUtils.playMode = GlobalConstant.REPEAT_ALL_PLAY_MODE;
    			playModeBtn.setBackgroundResource(R.drawable.playmode_repeate_all);
    			Toast.makeText(this, "循环播放", Toast.LENGTH_SHORT).show();
    		}
    		break;
    		case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
                ControlUtils.playMode = GlobalConstant.REPEAT_ONE_PLAY_MODE;
    			playModeBtn.setBackgroundResource(R.drawable.playmode_repeate_single);
    			Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
    		}
    		break;
    		case GlobalConstant.REPEAT_ONE_PLAY_MODE:{
                ControlUtils.playMode = GlobalConstant.RANDOM_PLAY_MODE;
    			playModeBtn.setBackgroundResource(R.drawable.playmode_repeate_random);
    			Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
    		}
    		break;
    		case GlobalConstant.RANDOM_PLAY_MODE:{
                ControlUtils.playMode = GlobalConstant.NORMAL_PLAY_MODE;
    			playModeBtn.setBackgroundResource(R.drawable.playmode_normal);
    			Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
    		}
    		break;
    		default:
    		break;
    	}
    	
    }
    
    private void playingImageBtnClickHandler(){
    	Log.d(TAG, "playingImageBtnClickHandler");
    	
		Intent intent = new Intent(MainActivity.this, PlayingActivity.class);
		intent.putExtra(GlobalConstant.SONG_NAME_KEY, audioInfoList.get(ControlUtils.curMusicPos).getTitle());
		intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curMusicPos).getUrl());
        intent.putExtra(GlobalConstant.ACTIVITY_KEY, GlobalConstant.FROM_MAIN_ACTIVITY);
		//intent.putExtra(GlobalConstant.BPLAYINGFLAG_KEY, bPla);
		//intent.putExtra(GlobalConstant.CURRENT_POS_KEY, curMusicPos);
		//startActivity(intent);
		startActivityForResult(intent, 0);
    }
    
    /*** reveiver ***/
    public class MainActivityReceiver extends BroadcastReceiver{
    	@Override
    	public void onReceive(Context context, Intent intent){
    		String Action = intent.getAction();
    		if(Action.equals(GlobalConstant.PLAY_NEXT_BOARDCAST)){
    			Log.d(TAG, "onReceiver->PLAY_NEXT_BOARDCAST");
    			nextBtnClickHandler();
    		}
    	}
    }
    /*
    private void setCurrentPlayPositionWhenPlayPre(){
    	Log.d(TAG, "setCurrentPlayPositionWhenPlayPre");

    	switch(ControlUtils.playMode){
    		case GlobalConstant.NORMAL_PLAY_MODE:{
                ControlUtils.curMusicPos--;
    		}
    		break;
    		case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
                ControlUtils.curMusicPos--;
    			if(ControlUtils.curMusicPos < 0){
                    ControlUtils.curMusicPos = audioInfoList.size() - 1;
    			}
    		}
    		break;
    		case GlobalConstant.RANDOM_PLAY_MODE:{
    			Random rand = new Random();
    			int randPos = rand.nextInt(audioInfoList.size());
                ControlUtils.curMusicPos = randPos % (audioInfoList.size());
    		}
    		break;
    		case GlobalConstant.REPEAT_ONE_PLAY_MODE:
    		default:
    		break;
    	}
    	//return curMusicPos;
    }*/
    /*
    private void setCurrentPlayPositionWhenPlayNext(){
    	Log.d(TAG, "setCurrentPlayPositionWhenPlayNext");

    	switch(ControlUtils.playMode){
    		case GlobalConstant.NORMAL_PLAY_MODE:{
                ControlUtils.curMusicPos++;
    		}
    		break;
    		case GlobalConstant.REPEAT_ALL_PLAY_MODE:{
                ControlUtils.curMusicPos++;
    			if(ControlUtils.curMusicPos > audioInfoList.size() - 1){
                    ControlUtils.curMusicPos = 0;
    			}
    		}
    		break;
    		case GlobalConstant.RANDOM_PLAY_MODE:{
    			Random rand = new Random();
    			int randPos = rand.nextInt(audioInfoList.size());
                ControlUtils.curMusicPos = randPos % ( n.size());
    		}
    		break;
    		case GlobalConstant.REPEAT_ONE_PLAY_MODE:
    		default:
    		break;
    	}
    }
    */
    /*
    public static List<AudioInfo> getAudioList(){
    	return audioInfoList;
    }
    
    public static int getCurPos(){
    	return ControlUtils.curMusicPos;
    }
    */
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	Log.d(TAG, "onActivityResult");
    	
    	switch(requestCode){
    		case 0:{
    			if(resultCode == RESULT_OK){
    				Log.d(TAG, "onActivityResult");
    				setPauseOrPlayBtn();
    			}
        	break;
    		}
        	default:
        	break;
    	}
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出哦", Toast.LENGTH_SHORT).show();
	            exitTime = System.currentTimeMillis();
	            
	        } else {
	            finish();
	    		//Intent intent = new Intent("com.angel.Android.MUSIC");
	    		//intent.setClass(MusicManagerActivity.this,PlayMusicService.class);
	    		//stopService(intent);
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyUp(keyCode, event);
    }
    */
    
    /*** listen call state changed ***/
    private class PhoneStateListenHander extends PhoneStateListener{
    	@Override
    	public void onCallStateChanged(int state, String callNum){
    		switch(state){
    		case TelephonyManager.CALL_STATE_IDLE:{
    			
    		}
    		break;
    		case TelephonyManager.CALL_STATE_OFFHOOK:
    		case TelephonyManager.CALL_STATE_RINGING:{
    			 if(ControlUtils.bPlayingFlag == true){
    				 pauseOrstartBtnClickHandler();
    			 }
    		}
    		break;
    		default:
    		break;
    		}
    	}
    }

    @Override
    public void onScroll(MyLinearLayout view){
        if(mLastScrollView != null){
            mLastScrollView.smoothScrollTo(0, 0);
        }
        mLastScrollView = view;
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.del){
            int pos = myListView.getPositionForView(v);
            myAdapter.removeItem(pos);
        }
    }

    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(maReceiver);
    }
    
}
