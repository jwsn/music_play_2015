package linhai.example.com.adapter;

import java.util.List;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.musicplayer.R;

public class GridViewAdapter extends BaseAdapter{
	private Context context;		//�����Ķ�������
	private ImageButton imageBtn;
	private static int position = -1;
	private Integer[] mImageIds = {
			R.drawable.my_local_music_button,
			R.drawable.my_collect_button,
			R.drawable.history_button,
			R.drawable.search_button,
			R.drawable.settings_button,
	};
	
	public GridViewAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview = null;
		if(convertView == null){
			imageview = new ImageView(context);
			imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		}else{
			imageview = (ImageView)convertView;
		}
		imageview.setImageResource(mImageIds[position]);
		return imageview;
	}
	
}