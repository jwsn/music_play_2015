package linhai.example.com.music;

//import com.example.adapter.GridViewAdapter;
//import com.example.constant.GlobalConstant;

import linhai.example.com.adapter.GridViewAdapter;
import linhai.example.com.constant.GlobalConstant;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.musicplayer.R;

public class MusicManagerActivity extends Activity{
    private static final String TAG = "MusicManagerActivity";
	private GridView gridview;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_music_manager);
		
		gridview = (GridView)findViewById(R.id.gridview);
		gridview.setAdapter(new GridViewAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int itemid,long arg3){
				//arg1�ǵ�ǰitem��view��ͨ�������Ի�ø����еĸ�������� 
				//arg2�ǵ�ǰitem��ID�����id���������������е�д�������Լ����塣 
				//arg3�ǵ�ǰ��item��listView�е����λ�ã� 
				Intent intent = new Intent();
				switch(itemid){
				case GlobalConstant.LOCAL_MUSIC:{
                    Log.d(TAG, "press local music key");
					intent.setClass(MusicManagerActivity.this, MainActivity.class);
					startActivity(intent);
				}
				break;
				case GlobalConstant.HISTORY_MUSIC:{
				    Log.d(TAG, "press history key");
                    intent.setClass(MusicManagerActivity.this, MusicHistoryActivity.class);
                    startActivity(intent);
                }
				break;
				case GlobalConstant.COLLECT_MUSIC:{
                    Log.d(TAG, "press collect key");
                    intent.setClass(MusicManagerActivity.this, MusicCollectActivity.class);
                    startActivity(intent);
				}
				break;
				case GlobalConstant.SEARCH_MUSIC:{
                    Log.d(TAG, "press search key");
                    intent.setClass(MusicManagerActivity.this, MusicSearchActivity.class);
                    startActivity(intent);
				}
				break;
				case GlobalConstant.SETTING_MUSIC:{
                    Log.d(TAG, "press setting key");
                    intent.setClass(MusicManagerActivity.this, MusicSettingActivity.class);
                    startActivity(intent);
				}
				break;
				default:
					break;
				}
			}
		});
		
	}

    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed");

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

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

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy");
        Intent stopIntent = new Intent();
        stopIntent.setAction(GlobalConstant.MUSIC_SERVICE);
        stopService(stopIntent);
    	super.onDestroy();
    }
	
}