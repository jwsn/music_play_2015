package linhai.example.com.pagerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.musicplayer.R;
import linhai.example.com.adapter.GridViewAdapter;
import linhai.example.com.constant.GlobalConstant;
import linhai.example.com.music.MainActivity;
import linhai.example.com.music.MusicCollectActivity;
import linhai.example.com.music.MusicHistoryActivity;
import linhai.example.com.music.MusicSearchActivity;
import linhai.example.com.music.MusicSettingActivity;

/**
 * Created by linhai on 15/6/2.
 */
public class MyPageFragment extends Fragment{
    private static final String TAG = "MyPageFragment";
    private static final String ARG_PAGE = "ARG_PAGE";

    private static Context mContext;
    private GridView gridview;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.my_fragment_page, container, false);
        initGridView(view);
        return view;
    }

    private void initGridView(View view){
        gridview = (GridView)view.findViewById(R.id.gridview);
        gridview.setAdapter(new GridViewAdapter(mContext));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int itemid,long arg3){
                Intent intent = new Intent();
                switch(itemid){
                    case GlobalConstant.LOCAL_MUSIC:{
                        Log.d(TAG, "press local music key");
                        intent.setClass(mContext, MainActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                    break;
                    case GlobalConstant.HISTORY_MUSIC:{
                        Log.d(TAG, "press history key");
                        intent.setClass(mContext, MusicHistoryActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                    break;
                    case GlobalConstant.COLLECT_MUSIC:{
                        Log.d(TAG, "press collect key");
                        intent.setClass(mContext, MusicCollectActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                    break;
                    case GlobalConstant.SEARCH_MUSIC:{
                        Log.d(TAG, "press search key");
                        intent.setClass(mContext, MusicSearchActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                    break;
                    case GlobalConstant.SETTING_MUSIC:{
                        Log.d(TAG, "press setting key");
                        intent.setClass(mContext, MusicSettingActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                    }
                    break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause(){
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop(){
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView(){
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    public static MyPageFragment newInstance(Context context){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, 0);
        mContext = context;
        MyPageFragment fragment = new MyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
