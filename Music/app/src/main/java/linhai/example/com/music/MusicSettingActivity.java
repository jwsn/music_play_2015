package linhai.example.com.music;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.musicplayer.R;

import linhai.example.com.baseview.SwipeBackActivity;

/**
 * Created by linhai on 15/4/22.
 */
public class MusicSettingActivity extends SwipeBackActivity{
    private static final String TAG = "MusicSettingActivity";

    private SearchView sv;
    private ListView lv;

    private final String[] mStrings = {"aaaa", "bbb", "ccc", "ddd"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_setting_activity);

        lv = (ListView) findViewById(R.id.lv);
        sv = (SearchView)findViewById(R.id.sv);

        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings));
        lv.setTextFilterEnabled(true);
       //lv.setVisibility(View.GONE);

        sv.setIconifiedByDefault(false);
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("搜索歌曲");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    lv.clearTextFilter();
                } else {
                    lv.setFilterText(newText);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MusicSettingActivity.this, "xianshi", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void doMySearch(String query) {
        //do my search
    }
    /*** back key press ***/
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
