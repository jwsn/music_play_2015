package linhai.example.com.music;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import java.util.ArrayList;
import java.util.List;
import com.example.musicplayer.R;

import linhai.example.com.audio.AudioInfo;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicHistoryActivity extends Activity{
    private static final String TAG = "MusicHistoryActivity";
    //private
    private List<AudioInfo> audioInfoList = new ArrayList<AudioInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_history_activity);




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
