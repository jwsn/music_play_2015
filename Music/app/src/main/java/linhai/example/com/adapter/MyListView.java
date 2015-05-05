package linhai.example.com.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by linhai on 15/5/5.
 */
public class MyListView extends ListView {

    private Context mContext;
    private MyLinearLayout mCurView;

    public MyListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                int pos = pointToPosition(x, y);
                if(pos != INVALID_POSITION){
                    MyAdapter.DataHolder data = (MyAdapter.DataHolder)getItemAtPosition(pos);
                    mCurView = data.rootView;
                }
            }
            break;
            default:
            break;
        }
        if(mCurView != null){
            mCurView.disPatchTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


}
