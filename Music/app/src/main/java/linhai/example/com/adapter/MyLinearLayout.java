package linhai.example.com.adapter;

import android.content.Context;
import android.net.LinkAddress;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Scroller;

/**
 * Created by linhai on 15/5/5.
 */
public class MyLinearLayout extends LinearLayout{
    private int mLastX = 0;
    private final int MAX_WIDTH = 100;
    private Context mContext;
    private Scroller mScroller;
    private OnScrollListener mScrollListener;
    public void setOnScrollListener(OnScrollListener scrollListener){
        mScrollListener = scrollListener;
    }

    public void smoothScrollTo(int destX, int destY){
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0);
        invalidate();
    }

    public MyLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }

    public static interface OnScrollListener{
        public void onScroll(MyLinearLayout view);
    }

    public void disPatchTouchEvent(MotionEvent event){
        int maxLength = dipToPx(mContext, MAX_WIDTH);

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{

            }
            break;
            case MotionEvent.ACTION_MOVE:{
                int scrollX = this.getScrollX();
                int newScrollX = scrollX + mLastX - x;
                if(newScrollX < 0){
                    newScrollX = 0;
                }else if(newScrollX > maxLength){
                    newScrollX = maxLength;
                }
                this.scrollTo(newScrollX, 0);
            }
            break;
            case MotionEvent.ACTION_UP:{
                int scrollX = this.getScrollX();
                int newScrollX = scrollX + mLastX - x;
                if(newScrollX > maxLength / 2){
                    newScrollX = maxLength;
                    mScrollListener.onScroll(this);
                }else{
                    newScrollX = 0;
                }
                mScroller.startScroll(scrollX, 0, newScrollX - scrollX, 0);
                invalidate();
            }
            break;
        }
        mLastX = x;
    }

    @Override
    public void computeScroll(){
        if(mScroller.computeScrollOffset()){
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
        invalidate();
    }


    private int dipToPx(Context context, int dip){
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }


}
