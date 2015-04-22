package linhai.example.com.music;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.musicplayer.R;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicSettingActivity extends Activity{
    private static final String TAG = "MusicSettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_setting_activity);
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
