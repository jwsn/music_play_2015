package linhai.example.com.audio;

import android.os.Bundle;
import android.provider.MediaStore;

import org.litepal.crud.DataSupport;

public class AudioInfo extends DataSupport{
	private String title,
				   titleKey,
				   artist,
				   artistKey,
				   composer,
				   album,
				   albumKey,
				   displayName,
				   mimeType,
				   path;
	
	private long id,
				artistId,
				albumId,
				year,
				track;
	
	private long duration = 0,
				size = 0;
	
	private boolean isRingtone = false,
					isPodcast = false,
					isAlarm = false,
					isMusic = false,
					isNotification = false;
	
	private String url;
	
	
	
	/***
	public AudioInfo (Bundle bundle)
	{
		id = bundle.getInt(MediaStore.Audio.Media._ID);
		title = bundle.getString(MediaStore.Audio.Media.TITLE);
		titleKey = bundle.getString(MediaStore.Audio.Media.TITLE_KEY);
		artist = bundle.getString(MediaStore.Audio.Media.ARTIST);
		artistKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
		composer = bundle.getString(MediaStore.Audio.Media.COMPOSER);
		album = bundle.getString(MediaStore.Audio.Media.ALBUM);
		albumKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
		displayName = bundle.getString(MediaStore.Audio.Media.DISPLAY_NAME);
		year = bundle.getInt(MediaStore.Audio.Media.YEAR);
		mimeType = bundle.getString(MediaStore.Audio.Media.MIME_TYPE);
		path = bundle.getString(MediaStore.Audio.Media.DATA);
		
		artistId = bundle.getInt(MediaStore.Audio.Media.ARTIST_ID);
		albumId =bundle.getInt(MediaStore.Audio.Media.ALBUM_ID);
		track = bundle.getInt(MediaStore.Audio.Media.TRACK);
		duration = bundle.getInt(MediaStore.Audio.Media.DURATION);
		size = bundle.getInt(MediaStore.Audio.Media.SIZE);
		isRingtone = bundle.getInt(MediaStore.Audio.Media.IS_RINGTONE) == 1;
		isPodcast = bundle.getInt(MediaStore.Audio.Media.IS_PODCAST) == 1;
		isAlarm = bundle.getInt(MediaStore.Audio.Media.IS_ALARM) == 1;
		isMusic = bundle.getInt(MediaStore.Audio.Media.IS_MUSIC) == 1;
		isNotification = bundle.getInt(MediaStore.Audio.Media.IS_NOTIFICATION) == 1;
		
	}*/
	
	public long getId(){
		return id;
	}
	
	public void setId(long _id){
		id = _id;
	}
	
	public String getMimeType(){
		return mimeType;
	}
	
	public void setMimeType(String mt){
		mimeType = mt;
	}
	
	public long getDuration(){
		return duration;
	}
	
	public void setDuration(long d){
		duration = d;
	}
	
	public long getSize(){
		return size;
	}

	public void setSize(long _size){
		size = _size;
	}
	
	public boolean isRingtone(){
		return isRingtone;
	}
	
	public void setIsRingtone(boolean flag){
		isRingtone = flag;
	}
	
	public boolean isPodcast(){
		return isPodcast;
	}
	
	public void setIsPodcast(boolean flag){
		isPodcast = flag;
	}
	
	public boolean isAlarm(){
		return isAlarm;
	}
	
	public void setIsAlarm(boolean flag){
		isAlarm = flag;
	}
	
	public boolean isMusic(){
		return isMusic;
	}
	
	public void setIsMusic(boolean flag){
		isMusic = flag;
	}
	
	public boolean isNotification(){
		return isNotification;
	}
	
	public void setNotification(boolean flag){
		isNotification = flag;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String _title){
		title = _title;
	}
	
	public String getTitleKey(){
		return titleKey;
	}
	
	public void setmTitleKey(String _titleKey){
		titleKey = _titleKey;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public void setArtist(String _artist){
		artist = _artist;
	}
	
	public long getArtistId(){
		return artistId;
	}
	
	public void setArtistId(long id){
		artistId = id;
	}
	
	public String getArtistKey(){
		return artistKey;
	}
	
	public void setArtistKey(String ak){
		artistKey = ak;
	}
	
	public String getComposer(){
		return composer;
	}
	
	public void setComposer(String _composer){
		composer = _composer;
	}
	
	public String getAlbum(){
		return album;
	}
	
	public void setAlbum(String ab){
		album = ab;
	}
	
	public long getAlbumId(){
		return albumId;
	}
	
	public void setAlbumId(long id){
		albumId = id;
	}
	
	public String getAlbumKey(){
		return albumKey;
	}
	
	public void setAlbumkey(String key){
		albumKey = key;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public void setDisplayName(String name){
		displayName = name;
	}
	
	public long getYear(){
		return year;
	}
	
	public void setYear(long _year){
		year = _year;
	}
	
	public long getTrack(){
		return track;
	}
	
	public void setTrack(long _track){
		track = _track;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String _path){
		path = _path;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String _url) {
		url = _url;
	}
}











