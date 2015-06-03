package linhai.example.com.floatview;

/**
 * Created by linhai on 15/5/10.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicplayer.R;

import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.lrc.LrcView;
import linhai.example.com.service.PlayMusicService;

/**
 * Created by linhai on 15/5/9.
 */
public class FloatView extends LinearLayout{
    private static final String TAG = "FloatView";

    private float mTouchStartX;
    private float mTouchStartY;

    private float x;
    private float y;

    public static int viewWidth;
    public static int viewHeight;

    /*** ImageButton ***/
    private ImageButton playBtn;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private ImageButton deleBtn;
    public static LrcView mLrcView;
    private Context mcontext;
    private FloatViewReceiver mFVR;

    private WindowManager wm;// = (WindowManager)getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams params; //= ((MyApplication)getContext().getApplicationContext()).getMywnParams();

    public FloatView(Context context){
        super(context);
        mcontext = context;
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_view, this);
        View view = findViewById(R.id.floatView_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        initFloatView();
        registerReceiverForFloatView();
    }

    private void initFloatView(){
        //find view
        playBtn = (ImageButton) findViewById(R.id.pause_or_start_btn);
        nextBtn = (ImageButton) findViewById(R.id.next_btn);
        prevBtn = (ImageButton) findViewById(R.id.pre_btn);
        deleBtn = (ImageButton) findViewById(R.id.delete_btn);
        mLrcView = (LrcView) findViewById(R.id.song_lrc_text);
        mLrcView.setLrcContents(PlayMusicService.getLrcList());
        //set click listener
        ViewOnClickListener viewOnClickListener = new ViewOnClickListener();
        playBtn.setOnClickListener(viewOnClickListener);
        nextBtn.setOnClickListener(viewOnClickListener);
        prevBtn.setOnClickListener(viewOnClickListener);
        deleBtn.setOnClickListener(viewOnClickListener);
    }

    private class ViewOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Log.d(TAG, "ViewOnClickListener");
            switch(v.getId()){
                case R.id.pause_or_start_btn:{
                    Toast.makeText(mcontext, "顺序播放", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.pre_btn:{
                    Toast.makeText(mcontext, "顺序播放", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.next_btn:{
                    Toast.makeText(mcontext, "顺序播放", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.delete_btn:{
                    Toast.makeText(mcontext, "顺序播放", Toast.LENGTH_SHORT).show();
                }
                break;
                default:{

                }
                break;
            }
        }
    }

    private void registerReceiverForFloatView(){
        mFVR = new FloatViewReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalConstant.UPDATE_LRC_LIST);
        intentFilter.addAction(GlobalConstant.UPLATE_LRC_DISP);
        registerReceiver(mFVR, intentFilter);
    }

    private void registerReceiver(FloatViewReceiver mFVR, IntentFilter intentFilter) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY();

        Log.i(TAG, "CurrX = " + x + "currY = " + y);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                Log.i(TAG, "startX = " + mTouchStartX + "startY = " + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                break;
            default:
                break;
        }
        return true;
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams){
        params = layoutParams;
    }

    private void updateViewPosition(){
        if(isFloatViewNeedMove()) {
            wm.updateViewLayout(this, params);
        }
    }

    private boolean isFloatViewNeedMove(){
        boolean ret;
        if(params != null){
            int dx, dy;
            Log.e(TAG, "X = " + x + " Y = " + y);
            Log.e(TAG, "mTouchStartX = " + mTouchStartX + " mTouchStartY = " + mTouchStartY);
            dx = (int)(x - mTouchStartX);
            dy = (int)(y - mTouchStartY);
            if((params.x - dx <= 5 && params.x - dx >= -5) && params.y - dy <=5 && params.y -dy >= -5){
                ret = false;
            }else{
                params.x = dx;
                params.y = dy;
                if(params.x > 500){
                    params.x = 500;
                }else if(params.x < 0){
                    params.x = 0;
                }
                if(params.y > 950){
                    params.y = 950;
                }else if(params.y < 0) {
                    params.y = 0;
                }
                ret = true;
            }
        }else{
            ret = false;
        }
        return ret;
    }

    /*** reveiver ***/
    public class FloatViewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String Action = intent.getAction();
            if(Action.equals(GlobalConstant.UPDATE_LRC_LIST)){
                Log.d(TAG, "onReceiver->UPDATE_LRC_LIST");
                //setLrcView();
            }
        }
    }

}
