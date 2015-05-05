package linhai.example.com.adapter;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import linhai.example.com.audio.AudioInfo;
import linhai.example.com.utils.ImageUtils;
import linhai.example.com.utils.AudioUtils;
import com.example.musicplayer.R;


public class MusicListAdapter extends BaseAdapter{
	private Context context;
	private List<AudioInfo> audioInfoList;
	private AudioInfo audioInfo;
	private int pos = -1;
	

	public MusicListAdapter(Context context, List<AudioInfo> AudioInfoList) {
		this.context = context;
		this.audioInfoList = AudioInfoList;
	}

	@Override
	public int getCount() {
		return audioInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			//convertView = LayoutInflater.from(context).inflate(R.layout.music_list_view, null);
			convertView = LayoutInflater.from(context).inflate(R.layout.music_list_view, null);
			viewHolder.albumImage = (ImageView) convertView.findViewById(R.id.albumImage);
			viewHolder.musicTitle = (TextView) convertView.findViewById(R.id.music_title);
			viewHolder.musicArtist = (TextView) convertView.findViewById(R.id.music_Artist);
			viewHolder.musicDuration = (TextView) convertView.findViewById(R.id.music_duration);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		audioInfo = audioInfoList.get(position);
		
		if(position == pos) {
			viewHolder.albumImage.setImageResource(R.drawable.playing_button);
		} else {
			Bitmap bitmap = ImageUtils.getInstance().getArtwork(context, audioInfo.getId(),audioInfo.getAlbumId(), true, true);
			if(bitmap == null) {
				viewHolder.albumImage.setImageResource(R.drawable.playing_button);
			} else {
				viewHolder.albumImage.setImageBitmap(bitmap);
			}
			
		}
		viewHolder.musicTitle.setText(audioInfo.getTitle());
		viewHolder.musicArtist.setText(audioInfo.getArtist());
		viewHolder.musicDuration.setText(AudioUtils.getInstance().formatTime(audioInfo.getDuration()));
		
		return convertView;
	}
	

	public class ViewHolder {
		public ImageView albumImage;
		public TextView musicTitle;
		public TextView musicDuration;
		public TextView musicArtist;
	}
}