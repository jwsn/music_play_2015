package linhai.example.com.utils;

import java.util.ArrayList;
import java.util.List;

//import com.example.audio.AudioInfo;
import linhai.example.com.audio.AudioInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

public class AudioUtils{
	public static final AudioUtils instance = new AudioUtils();
	
	public static final String[] AUDIO_KEYS = new String[]{
		MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.TITLE_KEY,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.ARTIST_ID,
		MediaStore.Audio.Media.ARTIST_KEY,
		MediaStore.Audio.Media.COMPOSER,
		MediaStore.Audio.Media.ALBUM,
		MediaStore.Audio.Media.ALBUM_ID,
		MediaStore.Audio.Media.ALBUM_KEY,
		MediaStore.Audio.Media.DISPLAY_NAME,
		MediaStore.Audio.Media.DURATION,
		MediaStore.Audio.Media.SIZE,
		MediaStore.Audio.Media.YEAR,
		MediaStore.Audio.Media.TRACK,
		MediaStore.Audio.Media.IS_RINGTONE,
		MediaStore.Audio.Media.IS_PODCAST,
		MediaStore.Audio.Media.IS_ALARM,
		MediaStore.Audio.Media.IS_MUSIC,
		MediaStore.Audio.Media.IS_NOTIFICATION,
		MediaStore.Audio.Media.MIME_TYPE,
		MediaStore.Audio.Media.DATA
	};
	
	public static AudioUtils getInstance(){
		return instance;
	}
	
	private AudioUtils(){
		
	}
/*
	public List<AudioInfo> getAudioList(Context context){
		
		List<AudioInfo> audioList = new ArrayList<AudioInfo>();
		
		ContentResolver resolver = context.getContentResolver();
		
		Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				AUDIO_KEYS, 
				null, 
				null, 
				null);
		
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToLast()){
			Bundle bundle = new Bundle();
			for(int i = 0; i < AUDIO_KEYS.length; i++){
				final String key = AUDIO_KEYS[i];
				final int columnIndex = cursor.getColumnIndex(key);
				final int type = cursor.getType(columnIndex);
				switch (type){
				case Cursor.FIELD_TYPE_BLOB:
					break;
				case Cursor.FIELD_TYPE_FLOAT:
					float floatValue = cursor.getFloat(columnIndex);
					bundle.putFloat(key, floatValue);
					break;
				case Cursor.FIELD_TYPE_INTEGER:
					int intValue = cursor.getInt(columnIndex);
					bundle.putInt(key, intValue);
					break;
				case Cursor.FIELD_TYPE_NULL:
					break;
				case Cursor.FIELD_TYPE_STRING:
					String strValue = cursor.getString(columnIndex);
					bundle.putString(key, strValue);
					break;
				}
			}
			AudioInfo audioInfo = new AudioInfo(bundle);
			audioList.add(audioInfo);
		}
		cursor.close();
		return audioList;
	}
	*/
	public List<AudioInfo> getAudioInfoList(Context context) {
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		
		List<AudioInfo> audioInfoList = new ArrayList<AudioInfo>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			AudioInfo audioInfo = new AudioInfo();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));	//����id
			String title = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Media.TITLE))); // ���ֱ���
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)); // ������
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));	//ר��
			String displayName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION)); // ʱ��
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // �ļ���С
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // �ļ�·��
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // �Ƿ�Ϊ����
			if (isMusic != 0) { // ֻ��������ӵ����ϵ���
				audioInfo.setId(id);
				audioInfo.setTitle(title);
				audioInfo.setArtist(artist);
				audioInfo.setAlbum(album);
				audioInfo.setDisplayName(displayName);
				audioInfo.setAlbumId(albumId);
				audioInfo.setDuration(duration);
				audioInfo.setSize(size);
				audioInfo.setUrl(url);
				audioInfoList.add(audioInfo);
			}
		}
		return audioInfoList;
	}
	
	/**
	 * ��ʽ��ʱ�䣬������ת��Ϊ��:���ʽ
	 * @param time
	 * @return
	 */
	public String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}
}