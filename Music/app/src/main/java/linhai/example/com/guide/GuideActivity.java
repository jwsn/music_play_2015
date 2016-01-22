package linhai.example.com.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicplayer.R;

import java.util.ArrayList;


/**
 * Created by linhai on 16/1/13.
 */
public class GuideActivity extends Activity {

    private static final int[] imageIds = {R.drawable.music_manage_screen, R.drawable.my_collect_screen, R.drawable.settings_screen,
            R.drawable.my_local_screen};

    private ViewPager guideViewPager;

    private ArrayList<ImageView> imageList;

    private LinearLayout grayPointLinearLayout;

    private View viewRedPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        initData();
        initView();
    }


    /*
    * init the view
    */
    private void initView(){
        guideViewPager = (ViewPager)findViewById(R.id.guide_viewpager);
        guideViewPager.setAdapter(new MyAdapter());
        grayPointLinearLayout = (LinearLayout) findViewById(R.id.gray_point);
        viewRedPoint = findViewById(R.id.red_point);
    }

    /*
    * init the data
    */
    private void initData() {
        //初始化引导的几个页面
        imageList = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            imageList.add(imageView);
        }

        //初始化引导页面的的底部小圆点
        for(int i = 0; i < imageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.red_point_shape);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            if(i > 0){
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            grayPointLinearLayout.addView(point);
        }
    }

    /*
    * Guild pager Adapter
    */
    public class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(imageList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageList.get(position));
            return imageList.get(position);
        }
    }

}
