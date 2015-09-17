package linhai.example.com.music;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.musicplayer.R;

import linhai.example.com.baseview.SwipeBackActivity;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicSearchActivity extends SwipeBackActivity {
    private static final String TAG = "MusicSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_search_activity);
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
