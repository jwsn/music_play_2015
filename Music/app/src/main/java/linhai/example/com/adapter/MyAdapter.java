package linhai.example.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

import linhai.example.com.audio.AudioInfo;
import linhai.example.com.utils.AudioUtils;
import linhai.example.com.utils.ImageUtils;

/**
 * Created by linhai on 15/5/5.
 */
public class MyAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater mInflate;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    private View.OnClickListener mDelClickListener;
    private MyLinearLayout.OnScrollListener mScrollerListener;
    private List<AudioInfo> mAudioInfoList = new ArrayList<AudioInfo>();
    private int pos = -1;

    private static class ViewHolder{
        public ImageView albumImage;
        public TextView musicTitle;
        public TextView musicDuration;
        public TextView musicArtist;
        public TextView del;
    }

    public static class DataHolder{
        public AudioInfo audioInfo;
        public MyLinearLayout rootView;
    }

    public MyAdapter(Context context, List<DataHolder> dataList, View.OnClickListener delListener,MyLinearLayout.OnScrollListener listener){
        mcontext = context;
        mInflate = LayoutInflater.from(context);
        mDelClickListener = delListener;
        mScrollerListener = listener;
        if(dataList != null && dataList.size() > 0){
            mDataList.addAll(dataList);
        }
    }

    @Override
    public int getCount(){
        return mDataList.size();
    }

    @Override
    public Object getItem(int position){
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null || convertView.getTag() == null){
            convertView = mInflate.inflate(R.layout.new_item_layout, parent, false);

            holder = new ViewHolder();
            holder.albumImage = (ImageView)convertView.findViewById(R.id.albumImage);
            holder.musicTitle = (TextView)convertView.findViewById(R.id.music_title);
            holder.musicDuration = (TextView)convertView.findViewById(R.id.music_duration);
            holder.musicArtist = (TextView)convertView.findViewById(R.id.music_Artist);
            //holder.del = (TextView)convertView.findViewById(R.id.del);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        DataHolder item = mDataList.get(position);
        AudioInfo audioInfo = item.audioInfo;

        if(position == pos) {
            holder.albumImage.setImageResource(R.drawable.playing_button);
        } else {
            Bitmap bitmap = ImageUtils.getInstance().getArtwork(mcontext, audioInfo.getId(),audioInfo.getAlbumId(), true, true);
            if(bitmap == null) {
                holder.albumImage.setImageResource(R.drawable.playing_button);
            } else {
                holder.albumImage.setImageBitmap(bitmap);
            }

        }
        holder.musicTitle.setText(audioInfo.getTitle());
        holder.musicArtist.setText(audioInfo.getArtist());
        holder.musicDuration.setText(AudioUtils.getInstance().formatTime(audioInfo.getDuration()));

        item.rootView = (MyLinearLayout)convertView.findViewById(R.id.lin_root);
        item.rootView.scrollTo(0, 0);
        item.rootView.setOnScrollListener(mScrollerListener);

        TextView delTv = (TextView)convertView.findViewById(R.id.del);
        delTv.setOnClickListener(mDelClickListener);

        return convertView;
    }

    public void removeItem(int pos){
        mDataList.remove(pos);
        notifyDataSetChanged();
    }


}
