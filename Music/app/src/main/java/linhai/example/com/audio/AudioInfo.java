package linhai.example.com.audio;

import android.os.Bundle;
import android.provider.MediaStore;

public class AudioInfo{
	private String mTitle,
				   mTitleKey,
				   mArtist,
				   mArtistKey,
				   mComposer,
				   mAlbum,
				   mAlbumKey,
				   mDisplayName,
				   mMimeType,
				   mPath;
	
	private long mId,
				mArtistId,
				mAlbumId,
				mYear,
				mTrack;
	
	private long mDuration = 0,
				mSize = 0;
	
	private boolean isRingtone = false,
					isPodcast = false,
					isAlarm = false,
					isMusic = false,
					isNotification = false;
	
	private String url; // ����·�� 5
	
	
	
	
	public AudioInfo (Bundle bundle)
	{
		mId = bundle.getInt(MediaStore.Audio.Media._ID);
		mTitle = bundle.getString(MediaStore.Audio.Media.TITLE);
		mTitleKey = bundle.getString(MediaStore.Audio.Media.TITLE_KEY);
		mArtist = bundle.getString(MediaStore.Audio.Media.ARTIST);
		mArtistKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
		mComposer = bundle.getString(MediaStore.Audio.Media.COMPOSER);
		mAlbum = bundle.getString(MediaStore.Audio.Media.ALBUM);
		mAlbumKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
		mDisplayName = bundle.getString(MediaStore.Audio.Media.DISPLAY_NAME);
		mYear = bundle.getInt(MediaStore.Audio.Media.YEAR);
		mMimeType = bundle.getString(MediaStore.Audio.Media.MIME_TYPE);
		mPath = bundle.getString(MediaStore.Audio.Media.DATA);
		
		mArtistId = bundle.getInt(MediaStore.Audio.Media.ARTIST_ID);
		mAlbumId =bundle.getInt(MediaStore.Audio.Media.ALBUM_ID);
		mTrack = bundle.getInt(MediaStore.Audio.Media.TRACK);
		mDuration = bundle.getInt(MediaStore.Audio.Media.DURATION);
		mSize = bundle.getInt(MediaStore.Audio.Media.SIZE);
		isRingtone = bundle.getInt(MediaStore.Audio.Media.IS_RINGTONE) == 1;
		isPodcast = bundle.getInt(MediaStore.Audio.Media.IS_PODCAST) == 1;
		isAlarm = bundle.getInt(MediaStore.Audio.Media.IS_ALARM) == 1;
		isMusic = bundle.getInt(MediaStore.Audio.Media.IS_MUSIC) == 1;
		isNotification = bundle.getInt(MediaStore.Audio.Media.IS_NOTIFICATION) == 1;
		
	}
	
	public AudioInfo (){
		
	}
	
	public long getId(){
		return mId;
	}
	
	public void setId(long id){
		mId = id;
	}
	
	public String getMimeType(){
		return mMimeType;
	}
	
	public void setMimeType(String mt){
		mMimeType = mt;
	}
	
	public long getDuration(){
		return mDuration;
	}
	
	public void setDuration(long d){
		mDuration = d;
	}
	
	public long getSize(){
		return mSize;
	}

	public void setSize(long size){
		mSize = size;
	}
	
	public boolean isRingtone(){
		return isRingtone;
	}
	
	public void setIsRingtone(boolean flag){
		isRingtone = flag;
	}
	
	public boolean isPoscast(){
		return isPodcast;
	}
	
	public void setIsPoscast(boolean flag){
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
		return mTitle;
	}
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public String getTitleKey(){
		return mTitleKey;
	}
	
	public void setTitleKey(String titleKey){
		mTitleKey = titleKey;
	}
	
	public String getArtist(){
		return mArtist;
	}
	
	public void setArtist(String artist){
		mArtist = artist;
	}
	
	public long getArtistId(){
		return mArtistId;
	}
	
	public void setArtistId(long id){
		mArtistId = id;
	}
	
	public String getArtistKey(){
		return mArtistKey;
	}
	
	public void setArtistKey(String ak){
		mArtistKey = ak;
	}
	
	public String getComposer(){
		return mComposer;
	}
	
	public void setComposer(String composer){
		mComposer = composer;
	}
	
	public String getAlbum(){
		return mAlbum;
	}
	
	public void setAlbum(String ab){
		mAlbum = ab;
	}
	
	public long getAlbumId(){
		return mAlbumId;
	}
	
	public void setAlbumId(long id){
		mAlbumId = id;
	}
	
	public String getAlbumKey(){
		return mAlbumKey;
	}
	
	public void setAlbumkey(String key){
		mAlbumKey = key;
	}
	
	public String getDisplayName(){
		return mDisplayName;
	}
	
	public void setDisplayName(String name){
		mDisplayName = name;
	}
	
	public long getYear(){
		return mYear;
	}
	
	public void setYear(long year){
		mYear = year;
	}
	
	public long getTrack(){
		return mTrack;
	}
	
	public void setTrack(long track){
		mTrack = track;
	}
	
	public String getPath(){
		return mPath;
	}
	
	public void setPath(String path){
		mPath = path;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}











