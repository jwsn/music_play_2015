package linhai.example.com.pagerview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.*;
import android.util.Log;
import linhai.example.com.constant.GlobalConstant;
/**
 * Created by linhai on 15/6/2.
 */
public class MyPageFragmentAdapter extends FragmentPagerAdapter {
    private final static String TAG = "MyPageFragmentAdapter";
    final int PAGE_COUNT = 4;
    private Context mcontext;
    private String tabTitles[] = new String[] {"我的", "推荐", "排行", "分类"};

    public MyPageFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
        mcontext = context;
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position){
        return createFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }

    private Fragment createFragment(int position){
        Log.d(TAG,"createFragment");
        Fragment fragment = null;
        switch(position){
            case GlobalConstant.MY_TAB:{
                fragment = MyPageFragment.newInstance(mcontext);
            }
            break;
            case GlobalConstant.CO_TAB:{

            }
            break;
            case GlobalConstant.RA_TAB:{

            }
            break;
            case GlobalConstant.SO_TAB:{

            }
            break;
            default:
            break;
        }
        if(fragment == null){
            fragment = MyPageFragment.newInstance(mcontext);
        }
        return fragment;
    }

}
