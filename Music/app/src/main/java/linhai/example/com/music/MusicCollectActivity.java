package linhai.example.com.music;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import linhai.example.com.adapter.MusicListAdapter;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.databaseHelper.MusicDatabaseHelper;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicCollectActivity extends Activity {
    private static final String TAG = "MusicCollectActivity";

    private MusicDatabaseHelper dbHelper; //= new MusicDatabaseHelper(this, "collect.db", null, 1);

    private ListView collectListView;
    private List<AudioInfo> audioInfoList = new ArrayList<AudioInfo>();
    private MusicListAdapter musicListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_collect_activity);

        collectListView = (ListView) findViewById(R.id.music_collect_list);
        dbHelper = new MusicDatabaseHelper(this, "collect.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("collect", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
               int pos = cursor.getInt(cursor.getColumnIndex("pos"));
               audioInfoList.add(AnimationActivity.audioInfoList.get(pos));
            }while(cursor.moveToNext());
        }

        if(audioInfoList.size() == 0) return;

        musicListAdapter = new MusicListAdapter(this, audioInfoList);
        //need debug
        //collectListView.setAdapter(musicListAdapter);

    }

    /*** back key press ***/
    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed");
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
