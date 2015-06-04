package linhai.example.com.baseview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.example.musicplayer.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by linhai on 15/6/3.
 */
public class SwipeBackLayout extends FrameLayout {
    private static final String TAG = SwipeBackLayout.class.getSimpleName();

    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private Scroller mScroller;
    private int viewWidth;
    private boolean isSliding;
    private boolean isFinish;
    private Drawable mShadowDrawable;
    private Activity mActivity;
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();

    public SwipeBackLayout(Context context, AttributeSet attrs){
        super(context, attrs, 0);
        Log.d(TAG, "constructor");
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        Log.d(TAG, "constructor");
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);

    }

    public void attachToActivity(Activity activity){
        Log.d(TAG, "attachToActivity");
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});

        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        decor.addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild){
        Log.d(TAG, "setContentView");
        mContentView = (View) decorChild.getParent();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        Log.d(TAG, "onInterceptTouchEvent");
        //处理viewPager冲突问题
        ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
        Log.i(TAG, "mViewPagers = " + mViewPager);

        if(mViewPager != null && mViewPager.getCurrentItem() != 0){
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                //满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if((moveX - downX > mTouchSlop || downX - moveX > mTouchSlop) && Math.abs((int) ev.getRawY() - downY) < mTouchSlop){
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        Log.d(TAG, "onTouchEvent");
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                Log.e(TAG, "tempX = " + tempX);
                int deltaX = tempX - moveX;
                tempX = moveX;
                Log.e(TAG, "moveX = " + moveX);
                Log.e(TAG, "deltaX = " + deltaX);
                Log.e(TAG, "downX = " + downX);
                Log.e(TAG, "mTouchSlop = " + mTouchSlop);
                if((moveX - downX > mTouchSlop || downX - moveX > mTouchSlop) && Math.abs((int) ev.getRawY() - downY) < mTouchSlop){
                    isSliding = true;
                }
                if(/*moveX - downX >= 0 &&*/ isSliding){
                    mContentView.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                Log.e(TAG, "downX = " + viewWidth);
                if(mContentView.getScrollX() <= -viewWidth / 2){
                    isFinish = true;
                    scrollRight();
                }else if(mContentView.getScrollX() >= viewWidth / 2){
                    isFinish = true;
                    scrollLeft();
                }else{
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }
        return true;
    }

    /**
     * 获取SwipeBackLayout里面的ViewPager的集合
     * @param mViewPagers
     * @param parent
     */
    private void getAllViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
        Log.d(TAG, "getAllViewPager");
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = parent.getChildAt(i);
            if(child instanceof ViewPager){
                mViewPagers.add((ViewPager)child);
            }else if(child instanceof ViewGroup){
                getAllViewPager(mViewPagers, (ViewGroup)child);
            }
        }
    }

    /**
     * 返回我们touch的ViewPager
     * @param mViewPagers
     * @param ev
     * @return
     */
    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
        Log.d(TAG, "getTouchViewPager");
        if(mViewPagers == null || mViewPagers.size() == 0){
            return null;
        }

        Rect mRect = new Rect();
        for(ViewPager v : mViewPagers){
            v.getHitRect(mRect);
            if(mRect.contains((int)ev.getX(), (int)ev.getY())){
                return v;
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        Log.d(TAG, "onLayout");
        super.onLayout(changed, l, t, r, b);
        if(changed){
            viewWidth = this.getWidth();
            getAllViewPager(mViewPagers, this);
            Log.i(TAG, "ViewPager size = " + mViewPagers.size());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        Log.d(TAG, "dispatchDraw");
        super.dispatchDraw(canvas);
        if(mShadowDrawable != null && mContentView != null){
            int left = mContentView.getLeft() - mShadowDrawable.getIntrinsicWidth();
            int right = left + mShadowDrawable.getIntrinsicWidth();
            int top = mContentView.getTop();
            int bottom = mContentView.getBottom();

            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(canvas);
        }
    }

    /**
     * 滚动出界面
     */
    private void scrollRight(){
        Log.d(TAG, "scrollRight");
        final int delta = (viewWidth + mContentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 5, 0, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin(){
        Log.d(TAG, "scrollOrigin");
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollLeft(){
        Log.d(TAG, "scrollLeft");
        int delta = mContentView.getScrollX() - viewWidth;
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta -5, 0, Math.abs(delta));
        postInvalidate();
    }
    @Override
    public void computeScroll(){
        Log.d(TAG, "computeScroll");
        //调用startScroll的时候scroller.computeScrollOffset()返回true，
        if(mScroller.computeScrollOffset()){
            mContentView.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
            if(mScroller.isFinished() && isFinish){
                mActivity.finish();
            }
        }
    }
}
