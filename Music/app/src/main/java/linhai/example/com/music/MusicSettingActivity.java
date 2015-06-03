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
public class MusicSettingActivity extends SwipeBackActivity{
    private static final String TAG = "MusicSettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_setting_activity);
    }

    /*** back key press ***/
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
