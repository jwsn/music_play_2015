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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.musicplayer.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import linhai.example.com.adapter.MusicListAdapter;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.databaseHelper.MusicDatabaseHelper;
import linhai.example.com.utils.ControlUtils;
import linhai.example.com.utils.ImageUtils;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicCollectActivity extends Activity {
    private static final String TAG = "MusicCollectActivity";

    private MusicDatabaseHelper dbHelper; //= new MusicDatabaseHelper(this, "collect.db", null, 1);

    private ListView collectListView;
    private List<AudioInfo> audioInfoList = new ArrayList<AudioInfo>();
    private MusicListAdapter musicListAdapter;


    /*** image button ***/
    private ImageButton pauseOrstartBtn;
    private ImageButton nextBtn;
    private ImageButton preBtn;
    private ImageButton songImageBtn;
    private ImageButton playModeBtn;
    private TextView songNameDisplay;

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
               AudioInfo audioInfo = new AudioInfo();
               //audioInfoList.add(AnimationActivity.audioInfoList.get(pos));
               AudioInfo temp = AnimationActivity.audioInfoList.get(pos);
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


            }while(cursor.moveToNext());
        }

        if(audioInfoList.size() == 0) return;

        musicListAdapter = new MusicListAdapter(this, audioInfoList);
        collectListView.setAdapter(musicListAdapter);

        initButtonView();
        setButtonListener();


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
        Bitmap bitmap = ImageUtils.getInstance().getArtwork(MusicCollectActivity.this, audioInfoList.get(ControlUtils.curCollPos).getId(), audioInfoList.get(ControlUtils.curCollPos).getAlbumId(), true, false);
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
            Toast.makeText(MusicCollectActivity.this, "没有上一首啦", Toast.LENGTH_SHORT).show();
        }else{
            setPauseOrPlayBtn();
            if(ControlUtils.bPauseFlag == true){
                ControlUtils.bFirstTimePlayFlag = true;
                return;
            }

            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
            Intent intent = new Intent();
            intent.setAction(GlobalConstant.MUSIC_SERVICE);
            intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curMusicPos).getUrl());
            intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PRE);
            startService(intent);
        }
    }

    private void nextBtnClickHandler(){
        Log.d(TAG, "nextBtnClickHandler");

        ControlUtils.curCollPos = ControlUtils.getInstance().setCurrentPlayPositionWhenPlayNext(ControlUtils.curCollPos, audioInfoList);
        if( ControlUtils.curCollPos >= audioInfoList.size() ){
            ControlUtils.curCollPos = audioInfoList.size() - 1;
            Toast.makeText(MusicCollectActivity.this, "这是最后一首啦", Toast.LENGTH_SHORT).show();
        }else{
            setPauseOrPlayBtn();
            if(ControlUtils.bPauseFlag == true){
                ControlUtils.bFirstTimePlayFlag = true;
                return;
            }

            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
            Intent intent = new Intent();
            intent.setAction(GlobalConstant.MUSIC_SERVICE);
            intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_NEXT);
            intent.putExtra(GlobalConstant.SONG_PATH_KEY, audioInfoList.get(ControlUtils.curMusicPos).getUrl());
            startService(intent);
        }
    }

    private void pauseOrstartBtnClickHandler(){
        Log.d(TAG, "pauseOrstartBtnClickHandler");

        Intent intent = new Intent();
        if(ControlUtils.bFirstTimePlayFlag == true){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            ControlUtils.bFirstTimePlayFlag = false;
            setPauseOrPlayBtn();
            intent.setAction(GlobalConstant.MUSIC_SERVICE);
            intent.putExtra(GlobalConstant.SONG_PATH_KEY,audioInfoList.get(ControlUtils.curMusicPos).getUrl());
            intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_FIRST);
            startService(intent);
        }
        else if(ControlUtils.bPlayingFlag == false){
            ControlUtils.bPlayingFlag = true;
            ControlUtils.bPauseFlag = false;
            setPauseOrPlayBtn();
            intent.setAction(GlobalConstant.MUSIC_SERVICE);
            intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_RESUME);
            startService(intent);

        }else{
            ControlUtils.bPlayingFlag = false;
            ControlUtils.bPauseFlag = true;
            setPauseOrPlayBtn();
            intent.setAction(GlobalConstant.MUSIC_SERVICE);
            intent.putExtra(GlobalConstant.PLAY_CONTROL, GlobalConstant.PLAY_PAUSE);
            startService(intent);
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

        Intent intent = new Intent(MusicCollectActivity.this, PlayingActivity.class);
        //intent.putExtra(GlobalConstant.BFIRST_TIME_FLAG_KEY, bFirstTimePlayFlag);
        //intent.putExtra(GlobalConstant.BPAUSE_FLAG_KEY, bPauseFlag);
        //intent.putExtra(GlobalConstant.BPLAYINGFLAG_KEY, bPlayingFlag);
        //intent.putExtra(GlobalConstant.CURRENT_POS_KEY, curMusicPos);
        //startActivity(intent);
        startActivityForResult(intent, 1);
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
            break;
            default:
            break;
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
