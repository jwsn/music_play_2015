package linhai.example.com.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.musicplayer.R;
import android.view.View;

import linhai.example.com.adapter.MyAdapter;
import linhai.example.com.adapter.MyListView;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.utils.AudioUtils;

public class AnimationActivity extends Activity {

	//private List<songInfo> musicList;
	//private Audio laudio = new LocalAudio(this);
	//private  Audio laudio = new LocalAudio(this);
	//private static List<songInfo> localmusicList;
	//private static List<songInfo> collectmusicList = new ArrayList<songInfo>();
	//private static List<songInfo> collectmusicList;
	//private String exitMusicName;
	//private String exitPlayMode;
	//private String shakeseting;
	//private String lrcshow;

	//private  Context Aminationcontext; 

    public static List<AudioInfo> audioInfoList = null;
    public static List<MyAdapter.DataHolder> mDataList = new ArrayList<MyAdapter.DataHolder>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView();
		setContentView(R.layout.activity_amination);
        audioInfoList = AudioUtils.getInstance().getAudioInfoList(AnimationActivity.this);
        if(audioInfoList != null && audioInfoList.size() > 0) {
            for (int i = 0; i < audioInfoList.size(); i++) {
                MyAdapter.DataHolder holder = new MyAdapter.DataHolder();
                holder.audioInfo = audioInfoList.get(i);
                mDataList.add(holder);
            }
        }

		//musicList = laudio.getLocalAudioList();
		//PushManager.getInstance().initialize(this.getApplicationContext());
		//Aminationcontext = this.getApplicationContext();
		//PushManager.getInstance().initialize(this.getApplicationContext());
		
		//AdManager.getInstance(this).init("5b543c257193a433", "78b2c25cabeb8e59", false);
		
		//final DBHelper data=new DBHelper(this);
	   // SQLiteDatabase db=data.getReadableDatabase();
	   // Cursor c = db.query("exitinfo", null, null, null, null, null, null);
	   // while(c.moveToNext())
	    {
	    	//exitMusicName = c.getString(0);
	    	//exitPlayMode = c.getString(1);
	    } 
	   // Cursor d = db.query("settings", null, null, null, null, null, null);
	    //while(d.moveToNext())
	    //{
	    	//shakeseting = d.getString(0);
	    	//lrcshow = d.getString(1);
	    //} 
	   

		//musicList = laudio.getLocalAudioList();
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				Bundle b = new Bundle();
				// TODO Auto-generated method stub
			
				Intent  intent  = new Intent();
				//intent.putExtra("isplaying",false);
				//b.putString("exitMusicName",exitMusicName);
				//b.putString("exitPlayMode",exitPlayMode);
				// b.putString("shakeseting",shakeseting);
				// b.putString("lrcshow",lrcshow);
			//	b.putParcelableList("list",musicList);
				//intent.putExtra("list",musicList);
				//intent.putExtras(b);
				intent.setClass(AnimationActivity.this,MusicManagerActivity.class);
				startActivity(intent);
				//laudio.searchLocalAudioLRC();
				AnimationActivity.this.finish();		
			}
			
		},400);
		
		

 

	//	wmParams.gravity=Gravity.LEFT|Gravity.TOP;  
		//wmParams.x = 10;
	//	wmParams.y = 50;



		
		/*
		 final DBHelper helpter=new DBHelper(this,"CollectMusiclist");

		SQLiteDatabase db = helpter.getWritableDatabase();

		
		Cursor c = db.query("mycollect", null, null, null, null, null, null);
		if (c.moveToFirst()) {
			for (int i = c.getCount()-1; i >=0 ; i--) {
				c.moveToPosition(i);
				songInfo audio = new songInfo();
				Boolean inlocallist = false;
				for(int j= 0;j<localmusicList.size();j++){
					if(c.getString(0).equals(localmusicList.get(j).getName())){
						audio.setId(localmusicList.get(j).getId());
						audio.setName(c.getString(0));
						audio.setPath(localmusicList.get(j).getPath());
						collectmusicList.add(audio);
						inlocallist = true;
						break;
					}
				}
				if(!inlocallist){
					db.delete("mycollect", "collectmusicname=?", new String[]{c.getString(0)});
				}
				
			}
		}
		db.close();*/

	}

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}