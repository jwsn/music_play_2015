package linhai.example.com.music;

//import com.example.adapter.GridViewAdapter;
//import com.example.constant.GlobalConstant;

import linhai.example.com.adapter.GridViewAdapter;
import linhai.example.com.constant.GlobalConstant;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.example.musicplayer.R;

public class MusicManagerActivity extends Activity{
	private GridView gridview;
	
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
					intent.setClass(MusicManagerActivity.this,MainActivity.class);
					startActivity(intent);
				}
				break;
				case GlobalConstant.HISTORY_MUSIC:{
				}
				break;
				case GlobalConstant.COLLECT_MUSIC:{
				}
				break;
				case GlobalConstant.SEARCH_MUSIC:{
				}
				break;
				case GlobalConstant.SETTING_MUSIC:{
				}
				break;
				default:
					break;
				}
			}
		});
		
	}
	
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }
	
}