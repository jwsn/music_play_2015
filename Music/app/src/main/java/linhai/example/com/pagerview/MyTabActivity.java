package linhai.example.com.pagerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.musicplayer.R;

import java.util.Timer;
import java.util.TimerTask;

import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.floatview.FloatViewManager;

/**
 * Created by linhai on 15/6/2.
 */
public class MyTabActivity extends FragmentActivity {
    private static final String TAG = "MyTabActivity";
    private long exitTime = 0;
    private Handler handler = new Handler();
    private Timer timer;
    private RefreshFloatViewTask mrefreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tab_activity);

        ViewPager viewPager = (ViewPager) findViewById (R.id.viewpager);
        viewPager.setAdapter(new MyPageFragmentAdapter(getSupportFragmentManager(), MyTabActivity.this));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById (R.id.tabs);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new MyOnPageChangeListener());

        showFloatWindow();
        startTimerRefreshFloatWindow();
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position){
            Toast.makeText(MyTabActivity.this, "none", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

        }

        @Override
        public void onPageScrollStateChanged(int state){

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(MyTabActivity.this, "再按一次退出哦", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {
                finish();
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
        stopTimerRefreshFloatWindow();
        super.onDestroy();
    }


    private void showFloatWindow(){
        FloatViewManager.getInstance().showFloatView();
    }

    private void startTimerRefreshFloatWindow()
    {
        if(timer == null){
            timer = new Timer();
            if(mrefreshTask == null){
                mrefreshTask = new RefreshFloatViewTask();
            }
            timer.scheduleAtFixedRate(mrefreshTask, 0, 500);
        }
    }

    private class RefreshFloatViewTask extends TimerTask {

        @Override
        public void run(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FloatViewManager.getInstance().showFloatView();
                }
            });
        }
    }

    private void stopTimerRefreshFloatWindow(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(mrefreshTask != null){
            mrefreshTask.cancel();
            mrefreshTask = null;
        }
        removeFloatWindow();
    }

    private void removeFloatWindow(){
        FloatViewManager.getInstance().removeFloatView();
    }
}
