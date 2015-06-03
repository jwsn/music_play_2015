package linhai.example.com.floatview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;
import linhai.example.com.constant.MyApplication;
import linhai.example.com.lrc.LrcContent;
import linhai.example.com.lrc.LrcView;

/**
 * Created by linhai on 15/5/9.
 */
public class FloatViewManager {
    private static final String TAG = "FloatViewManager";
    private static FloatViewManager instance;
    private FloatView floatView;
    private WindowManager.LayoutParams params;
    private WindowManager wm;
    private Context mcontext;

    private FloatViewManager(Context context){
        mcontext = context;
    }

    public static FloatViewManager getInstance(){
        if(instance == null){
            instance = new FloatViewManager(MyApplication.getContext());
        }
        return instance;
    }
    public void createFloatView(){
        Context context = MyApplication.getContext();
        WindowManager wm = getWindowManager(context);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        if(floatView == null){
            floatView = new FloatView(context);
            if(params == null){
                params = new WindowManager.LayoutParams();
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                params.format = 1;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//不能抢占焦点
                params.flags = params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                params.flags = params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;//排版不受限制

                params.alpha = 1.8f;
                params.gravity = Gravity.LEFT | Gravity.TOP;

                params.x = 0;
                params.y = screenHeight / 2;

                params.width = floatView.viewWidth;
                params.height = floatView.viewHeight;
            }
            floatView.setLayoutParams(params);
            wm.addView(floatView, params);
        }
    }

    private WindowManager getWindowManager(Context context){
        if(wm == null){
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return wm;
    }

    public boolean isFloatViewShowing(){
        return floatView != null;
    }

    public void showFloatView(){
        if(isHome() && !isFloatViewShowing()){
            createFloatView();
        }else if(!isHome() && isFloatViewShowing()){
            removeFloatView();
        }
    }

    public void removeFloatView(){
        if(isFloatViewShowing()){
            WindowManager windowManager = getWindowManager(mcontext);
            windowManager.removeView(floatView);
            floatView = null;
        }
    }

    public boolean isHome(){
        ActivityManager activityManager = (ActivityManager)mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes(){
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = mcontext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfos){
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    public void setLrcVeiw(List<LrcContent> lrcRows){
        if(floatView != null && floatView.mLrcView != null) {
            Log.e(TAG, "HAIBINGHAIBING");
            floatView.mLrcView.setLrcContents(lrcRows);
        }
    }

    public void setFloatWindowLrcView(int progress,boolean fromSeekBar,boolean fromSeekBarByUser){
        if(floatView != null && floatView.mLrcView != null)
        {
            //Log.e(TAG, "floatView != null && floatView.mLrcView != null");
            floatView.mLrcView.seekTo(progress, fromSeekBar, fromSeekBarByUser);
        }
    }
}
