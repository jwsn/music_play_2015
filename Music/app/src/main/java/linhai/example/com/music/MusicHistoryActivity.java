package linhai.example.com.music;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.musicplayer.R;

import linhai.example.com.adapter.MusicListAdapter;
import linhai.example.com.adapter.MyAdapter;
import linhai.example.com.adapter.MyLinearLayout;
import linhai.example.com.adapter.MyListView;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.databaseHelper.MusicDatabaseHelper;
import linhai.example.com.service.PlayMusicService;
import linhai.example.com.utils.ControlUtils;
import linhai.example.com.utils.ImageUtils;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicHistoryActivity extends Activity implements MyLinearLayout.OnScrollListener, View.OnClickListener{
    private static final String TAG = "MusicHistoryActivity";

    private MusicDatabaseHelper dbHelper; //= new MusicDatabaseHelper(this, "collect.db", null, 1);

    private ListView historyListView;
    public static List<AudioInfo> audioInfoList = new ArrayList<AudioInfo>();
    private List<MyAdapter.DataHolder> mdataList = new ArrayList<MyAdapter.DataHolder>();
    private MyAdapter myAdapter;
    private MyLinearLayout mLastScrollView;

    /*** image button ***/
    private ImageButton pauseOrstartBtn;
    private ImageButton nextBtn;
    private ImageButton preBtn;
    private ImageButton songImageBtn;
    private ImageButton playModeBtn;
    private TextView songNameDisplay;

    /*** database ***/
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_history_activity);

        historyListView = (MyListView) findViewById(R.id.music_history_list);
        dbHelper = new MusicDatabaseHelper(this, "history.db", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("history", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                for(int i = 0; i < AnimationActivity.audioInfoList.size(); i++)
                {
                    if(AnimationActivity.audioInfoList.get(i).getTitle().equals(name)){
                        AudioInfo audioInfo = new AudioInfo();
                        AudioInfo temp = AnimationActivity.audioInfoList.get(i);
                        audioInfo.setId(temp.getId());
                        audioInfo.setTitle(temp.getTitle());
                        audioInfo.setArtist(temp.getArtist());
                        audioInfo.setAlbum(temp.getAlbum());
                        audioInfo.setDisplayName(temp.getDisplayName());
                        audioInfo.setAlbumId(temp.getAlbumId());
                        audioInfo.setDuration(temp.getDuration());
                        audioInfo.setSize(temp.getSize());
                        audioInfo.setUrl(temp.getUrl());
                        audioInfoList.add(audioInfo);
                        MyAdapter.DataHolder dataholder = new MyAdapter.DataHolder();
                        dataholder.audioInfo = audioInfo;
                        mdataList.add(dataholder);
                        break;
                    }
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

        initButtonView();
        setButtonListener();

        if(audioInfoList.size() == 0) return;

        myAdapter = new MyAdapter(this, mdataList, this, this);
        historyListView.setAdapter(myAdapter);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.d(TAG, "setOnItemClickListener->onItemClick");
                ControlUtils.curCollPos = position;
                AudioInfo audioInfo = audioInfoList.get(ControlUtils.curCollPos);
                ControlUtils.bFirstTimePlayFlag = false;
                ControlUtils.bPlayingFlag = true;
                ControlUtils.bPauseFlag = false;
                setPauseOrPlayBtn();
                //Intent intent = new Intent();
                //intent.setAction(GlobalConstant.MUSIC_SERVICE);
                //intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_FIRST);
                //Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
                //intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfo.getUrl());
                //Log.d(TAG, "songpath = " + audioInfo.getUrl());
                //startService(intent);
                PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_FIRST, audioInfo, ControlUtils.curHisPos);
            }
        });
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

    private void setPauseOrPlayBtn(){
        Log.d(TAG, "setPauseOrPlayBtn");
        if(ControlUtils.bPauseFlag == true){
            pauseOrstartBtn.setBackgroundResource(R.drawable.play_button);
        }else{
            pauseOrstartBtn.setBackgroundResource(R.drawable.pause_button);
        }
        songNameDisplay.setText(audioInfoList.get(ControlUtils.curCollPos).getDisplayName());
        setSongImageBtn();
    }

    private void setSongImageBtn(){
        Bitmap bitmap = ImageUtils.getInstance().getArtwork(MusicHistoryActivity.this, audioInfoList.get(ControlUtils.curCollPos).getId(), audioInfoList.get(ControlUtils.curCollPos).getAlbumId(), true, false);
        if(bitmap == null){
            songImageBtn.setBackgroundResource(R.drawable.playing_button);
        }else{
            songImageBtn.setBackground(null);
            songImageBtn.setImageBitmap(bitmap);
        }
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


    private class ViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Log.d(TAG, "ViewOnClickListener");
            if(audioInfoList.size() == 0){
                Toast.makeText(MusicHistoryActivity.this, "你最近没有播放歌曲哦，赶紧来听一首吧", Toast.LENGTH_SHORT).show();
                return;
            }
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

        ControlUtils.curCollPos = ControlUtils.getInstance().setCurrentPlayPositionWhenPlayPre(ControlUtils.curCollPos, audioInfoList);
        if(ControlUtils.curCollPos < 0){
            ControlUtils.curCollPos = 0;
            Toast.makeText(MusicHistoryActivity.this, "没有上一首啦", Toast.LENGTH_SHORT).show();
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
            //intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curCollPos).getUrl());
            //intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PRE);
            //startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curHisPos);
            PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_PRE, audioInfo, ControlUtils.curHisPos);
        }
    }

    private void nextBtnClickHandler(){
        Log.d(TAG, "nextBtnClickHandler");

        ControlUtils.curCollPos = ControlUtils.getInstance().setCurrentPlayPositionWhenPlayNext(ControlUtils.curCollPos, audioInfoList);
        if( ControlUtils.curCollPos >= audioInfoList.size() ){
            ControlUtils.curCollPos = audioInfoList.size() - 1;
            Toast.makeText(MusicHistoryActivity.this, "这是最后一首啦", Toast.LENGTH_SHORT).show();
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
            //intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curCollPos).getUrl());
            //startService(intent);
            AudioInfo audioInfo = audioInfoList.get(ControlUtils.curHisPos);
            PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_NEXT, audioInfo, ControlUtils.curHisPos);
        }
    }

    private void pauseOrstartBtnClickHandler(){
        Log.d(TAG, "pauseOrstartBtnClickHandler");

        //Intent intent = new Intent();
        AudioInfo audioInfo = audioInfoList.get(ControlUtils.curHisPos);
        if(ControlUtils.bFirstTimePlayFlag == true){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
            setPauseOrPlayBtn();
            //intent.setAction(GlobalConstant.MUSIC_SERVICE);
            //intent.putExtra(GlobalConstant.SONG_PATH_KEY,audioInfoList.get(ControlUtils.curCollPos).getUrl());
            //intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_FIRST);
            //startService(intent);
            PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_FIRST, audioInfo, ControlUtils.curHisPos);
        }
        else if(ControlUtils.bPlayingFlag == false){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            setPauseOrPlayBtn();
            //intent.setAction(GlobalConstant.MUSIC_SERVICE);
            //intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_RESUME);
            //startService(intent);
            PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_RESUME, audioInfo, ControlUtils.curHisPos);

        }else{
            ControlUtils.bPlayingFlag = false;
            ControlUtils.bPauseFlag = true;
            setPauseOrPlayBtn();
            //intent.setAction(GlobalConstant.MUSIC_SERVICE);
            //intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PAUSE);
            //startService(intent);
            PlayMusicService.serviceStart(MusicHistoryActivity.this, GlobalConstant.PLAY_PAUSE, audioInfo, ControlUtils.curHisPos);
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
/*
    private void setCurrentPlayPositionWhenPlayPre(){
        Log.d(TAG, "setCurrentPlayPositionWhenPlayPre");

        switch(ControlUtils.playMode){
            case GlobalConstant.NORMAL_PLAY_MODE:{
                ControlUtils.curCollPos--;
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
    }
    */

    private void playingImageBtnClickHandler(){
        Log.d(TAG, "playingImageBtnClickHandler");

        Intent intent = new Intent(MusicHistoryActivity.this, PlayingActivity.class);
        intent.putExtra(GlobalConstant.SONG_NAME_KEY, audioInfoList.get(ControlUtils.curHisPos).getTitle());
        intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curHisPos).getUrl());
        intent.putExtra(GlobalConstant.ACTIVITY_KEY, GlobalConstant.FROM_HIST_ACTIVITY);
        startActivityForResult(intent, 2);
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
            case 1:{
                if(resultCode == RESULT_OK){
                    Log.d(TAG, "onActivityResult");
                    setPauseOrPlayBtn();
                }
            }
            case 2:{
                if(resultCode == RESULT_OK){
                    Log.d(TAG, "onActivityResult");
                    setPauseOrPlayBtn();
                }
            }
            break;
            default:
                break;
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
            int pos = historyListView.getPositionForView(v);
            myAdapter.removeItem(pos);
            Cursor cursor = db.query("history", null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    //int p = cursor.getInt(cursor.getColumnIndex("pos"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    if(name.equals(audioInfoList.get(pos).getTitle()))
                    {
                        db.delete("history", "name=?", new String[]{name});
                        break;
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
            audioInfoList.remove(pos);
        }
    }

    /*** back key press ***/
    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed");
        finish();
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
