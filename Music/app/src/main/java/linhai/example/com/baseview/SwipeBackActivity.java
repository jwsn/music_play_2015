package linhai.example.com.baseview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.musicplayer.R;

/**
 * Created by linhai on 15/6/3.
 */
public class SwipeBackActivity extends Activity {
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipe_activity_base, null);
        layout.attachToActivity(this);
    }

    @Override
    public void startActivity(Intent intent){
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
