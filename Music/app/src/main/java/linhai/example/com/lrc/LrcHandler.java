package linhai.example.com.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.example.musicplayer.R;

/**
 * Ĭ�ϵĸ�ʽ�����
 *
 */
public class LrcHandler{
	private static final String TAG = "LrcHandler";
	
	private static final LrcHandler instance = new LrcHandler();
	final int LRCTYPE = 0;
	final int TRCTYPE = 1;
	private final int SEARCH_LRC_METHOD = 1;
	private List<String> lrcPathList = new ArrayList<String>();
	private int global_lrc_flag = 0;
	private String global_lrc_path = null;
	private int count = 0;
    //��ȡר�������Uri  
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart"); 
	private LinkedList<File> FileList = new LinkedList<File>(); //��ʽ�洢
	public static final LrcHandler getInstance(){
		return instance;
	}
	
	private LrcHandler(){
	
	}
	
	/***
	 * ������ļ�������ַ��� ������һ��List<LrcContent>
	 */
	public List<LrcContent> getLrcContent(String str,int RCType){
		
		if(TextUtils.isEmpty(str)){
			return null;
		}
		
		BufferedReader br = new BufferedReader(new StringReader(str));
		
		List<LrcContent> lrcContents = new ArrayList<LrcContent>();
		
		String lrcLine;
		
		try{
			while((lrcLine = br.readLine()) != null){
				//List<LrcContent> rows = LrcContent.createLrcContent(lrcLine);
				List<LrcContent> rows =null;
				if(RCType ==LRCTYPE)
					rows = LrcContent.createLrcContent(lrcLine);
				else if(RCType ==TRCTYPE)
					rows = LrcContent.createTrcContent(lrcLine);
				
				if(rows != null && rows.size() > 0){
					lrcContents.addAll(rows);
				}
			}
			//Collections.sort(lrcContents);
			
			for(int i = 0; i < lrcContents.size() - 1; i++)
			{
				lrcContents.get(i).setTotalTime(lrcContents.get(i+1).getTime() - lrcContents.get(i).getTime());
			}
			
			lrcContents.get(lrcContents.size() - 1).setTotalTime(5000);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}finally{
			if(br != null){
				try{
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return lrcContents;
	}
	
	public int getLrcSize()
	{
		return lrcPathList.size();
	}
	
	public List<LrcContent> readLRC(String path){
		Log.d(TAG, "readLRC");
		if(path == null)
			return null;

		List<LrcContent> LrcContents = null;
		int  RCType =LRCTYPE;
		String songName = path.substring(path.lastIndexOf("/") + 1, path.length());
		String root = "/";
		if(path.contains(".mp3") && (".mp3").equals(path.substring(path.lastIndexOf("."), path.length())))
		{
			File f;
			f= new File(path.replace(".mp3", ".lrc"));
			if(!f.exists())
			{
				f= new File(path.replace(".mp3", ".trc"));
				if(!f.exists())
				{
					global_lrc_flag = 0;
					global_lrc_path = null;
					if(lrcPathList.size() > 0){
						for(int i = 0; i < lrcPathList.size(); i++ ){
							String songNameTemp = lrcPathList.get(i).substring(lrcPathList.get(i).lastIndexOf("/") + 1, lrcPathList.get(i).length());
							String songNameLrc = songName.replace(".mp3", ".lrc");
							String songNameTrc = songName.replace(".mp3",  ".trc");
							
  							if(songNameLrc.equals(songNameTemp))
							{
								RCType = LRCTYPE;
								global_lrc_flag = 1;
								global_lrc_path = lrcPathList.get(i) ;
								break;
							}else if(songNameTrc.equals(songNameTemp)){
								RCType = TRCTYPE;
								global_lrc_flag = 1;
								global_lrc_path = lrcPathList.get(i) ;
								break;
							}
						}
					}
					if(global_lrc_flag == 1 && global_lrc_path != null){
						f= new File(global_lrc_path);
						if(!f.exists()){
							return null;
						}
					}
					else{
						return null;
					}
				}
				RCType = TRCTYPE;
			}
			
			String line;
			try{
				FileInputStream fis = new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				StringBuffer sb=new StringBuffer();
				try{
					while((line = br.readLine()) != null){
						sb.append(line+"\n");
					}
					Log.d("readLRC", sb.toString());
					LrcContents = getLrcContent(sb.toString(),RCType);
				}catch(IOException e){
					e.printStackTrace();
				}

			}catch(FileNotFoundException e){
				e.printStackTrace();
				//strBuilder.append("�]�и��~");
			}catch(IOException e){
				e.printStackTrace();
				//strBuilder.append("�]�и��~");
			}
		}
		return LrcContents;
	}
	

	public void searchLrcFile(String root){
		int num=0;

		File[] files = new File(root).listFiles();

		FileList.clear();
		
		if(files != null)
		{
			for(int i = 0; i < files.length; i++){
				if (files[i].canRead() && files[i].isDirectory()) { 
					FileList.add(files[i]);
					//searchLrcFile(file.getAbsolutePath());  
				} 
				else{
					if(files[i].getName().contains(".lrc") || files[i].getName().contains(".trc")){
						//global_lrc_path = file.getAbsolutePath();
						lrcPathList.add(files[i].getAbsolutePath());
						System.out.println("This file is Lrc File,fileName="+files[i].getName()+",filePath="+files[i].getAbsolutePath()); 
					}
				}
			}
		}
		
		File tempfile;
		
		while(!FileList.isEmpty()){
			count++;
			if(count >= 200)
			{
				break;
			}
			tempfile = FileList.removeFirst();
			if(tempfile.isDirectory() && tempfile.canRead()){
				files = tempfile.listFiles();
				if(files != null){
					for(int i = 0; i < files.length; i++){
						if(files[i].isDirectory() && files[i].canRead()){
							FileList.add(files[i]);
							System.out.println("This file is for times" + num++); 
						}else{
							if(files[i].getName().contains(".lrc") || files[i].getName().contains(".trc")){
								//global_lrc_path = file.getAbsolutePath();
								lrcPathList.add(files[i].getAbsolutePath());
								System.out.println("This file is Lrc File,fileName="+files[i].getName()+",filePath="+files[i].getAbsolutePath()); 
							}
						}
					}
				}
			}
			
		}
		
	}
	
	
    /** 
     * ��ȡĬ��ר��ͼƬ 
     * @param context 
     * @return 
     */
    public static Bitmap getDefaultArtwork(Context context,boolean small) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inPreferredConfig = Bitmap.Config.RGB_565;  
        if(small){  //����СͼƬ  
            return BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.playing_button), null, opts);  
        }  
        //return BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.bg_animation), null, opts);
        return null;
    } 
	
    /** 
     * ���ļ����л�ȡר������λͼ 
     * @param context 
     * @param songid 
     * @param albumid 
     * @return 
     */  
    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid){  
        Bitmap bm = null;  
        if(albumid < 0 && songid < 0) {  
            throw new IllegalArgumentException("Must specify an album or a song id");  
        }  
        try {  
            BitmapFactory.Options options = new BitmapFactory.Options();  
            FileDescriptor fd = null;  
            if(albumid < 0){  
                Uri uri = Uri.parse("content://media/external/audio/media/"  
                        + songid + "/albumart");  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");  
                if(pfd != null) {  
                    fd = pfd.getFileDescriptor();  
                }  
            } else {  
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);  
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");  
                if(pfd != null) {  
                    fd = pfd.getFileDescriptor();  
                }  
            }  
            options.inSampleSize = 1;  
            // ֻ���д�С�ж�  
            options.inJustDecodeBounds = true;  
            // ���ô˷����õ�options�õ�ͼƬ��С  
            BitmapFactory.decodeFileDescriptor(fd, null, options);  
            // ���ǵ�Ŀ������800pixel�Ļ�������ʾ  
            // ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı���  
            options.inSampleSize = 100;  
            // ���ǵõ������ŵı��������ڿ�ʼ��ʽ����Bitmap����  
            options.inJustDecodeBounds = false;  
            options.inDither = false;  
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
              
            //����options��������������Ҫ���ڴ�  
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
        return bm;  
    } 
    
    /** 
     * ��ȡר������λͼ���� 
     * @param context 
     * @param song_id 
     * @param album_id 
     * @param allowdefalut 
     * @return 
     */  
    public static Bitmap getArtwork(Context context, long song_id, long album_id, boolean allowdefalut, boolean small){  
        if(album_id < 0) {  
            if(song_id < 0) {  
                Bitmap bm = getArtworkFromFile(context, song_id, -1);  
                if(bm != null) {  
                    return bm;  
                }  
            }  
            if(allowdefalut) {  
                return getDefaultArtwork(context, small);  
            }  
            return null;  
        }  
        ContentResolver res = context.getContentResolver();  
        Uri uri = ContentUris.withAppendedId(albumArtUri, album_id);  
        if(uri != null) {  
            InputStream in = null;  
            try {  
                in = res.openInputStream(uri);  
                BitmapFactory.Options options = new BitmapFactory.Options();  
                //���ƶ�ԭʼ��С  
                options.inSampleSize = 1;  
                //ֻ���д�С�ж�  
                options.inJustDecodeBounds = true;  
                //���ô˷����õ�options�õ�ͼƬ�Ĵ�С  
                BitmapFactory.decodeStream(in, null, options);  
                /** ���ǵ�Ŀ��������N pixel�Ļ�������ʾ�� ������Ҫ����computeSampleSize�õ�ͼƬ���ŵı��� **/  
                /** �����targetΪ800�Ǹ���Ĭ��ר��ͼƬ��С�����ģ�800ֻ�ǲ������ֵ���������������Ľ�� **/  
                if(small){  
                    options.inSampleSize = computeSampleSize(options, 65);  
                } else{  
                    options.inSampleSize = computeSampleSize(options, 600);  
                }  
                // ���ǵõ������ű��������ڿ�ʼ��ʽ����Bitmap����  
                options.inJustDecodeBounds = false;  
                options.inDither = false;  
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
                in = res.openInputStream(uri);  
                return BitmapFactory.decodeStream(in, null, options);  
            } catch (FileNotFoundException e) {  
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);  
                if(bm != null) {  
                    if(bm.getConfig() == null) {  
                        bm = bm.copy(Bitmap.Config.RGB_565, false);  
                        if(bm == null && allowdefalut) {  
                            return getDefaultArtwork(context, small);  
                        }  
                    }  
                } else if(allowdefalut) {  
                    bm = getDefaultArtwork(context, small);  
                }  
                return bm;  
            } finally {  
                try {  
                    if(in != null) {  
                        in.close();  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return null;  
    }
	
    /** 
     * ��ͼƬ���к��ʵ����� 
     * @param options 
     * @param target 
     * @return 
     */  
    public static int computeSampleSize(Options options, int target) {  
        int w = options.outWidth;  
        int h = options.outHeight;  
        int candidateW = w / target;  
        int candidateH = h / target;  
        int candidate = Math.max(candidateW, candidateH);  
        if(candidate == 0) {  
            return 1;  
        }  
        if(candidate > 1) {  
            if((w > target) && (w / candidate) < target) {  
                candidate -= 1;  
            }  
        }  
        if(candidate > 1) {  
            if((h > target) && (h / candidate) < target) {  
                candidate -= 1;  
            }  
        }  
        return candidate;  
    }  

	/*
	public void searchLrcFile(String root){

		File[] files = new File(root).listFiles();
		count++;
		if(count >= 60000)
			return;
		
		if(files != null)
		{
			for(File file : files){
				if (file.canRead() && file.isDirectory()) { 
					String name = file.getAbsolutePath();
					searchLrcFile(name);  
				}  
				else{
					if(file.getName().contains(".lrc") || file.getName().contains(".trc")){
						global_lrc_path = file.getAbsolutePath();
						lrcPathList.add(global_lrc_path);
						System.out.println("This file is Lrc File,fileName="+file.getName()+",filePath="+file.getAbsolutePath()); 
						//return ;
					}
				}
			}
		}
	}


	private List<LrcContent> lrcList;
	private LrcContent mLrcContent;
	
	public LrcHandler(){
		mLrcContent = new LrcContent();
		lrcList = new ArrayList<LrcContent>();
	}
	
	public String readLRC(String path){
		
		StringBuilder strBuilder = new StringBuilder();
		if(path.contains(".mp3"))
		{
		
			File f = new File(path.replace(".mp3", ".lrc"));
			
			try{
				FileInputStream fis = new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				
				String s = "";
				
				while((s = br.readLine()) != null){
					s = s.replace("[", "");
					s = s.replace("]", "@");
					
					String splitLrcData[] = s.split("@");
					
					if(splitLrcData.length > 1){
						mLrcContent.setLrcStr(splitLrcData[1]);
						
						int lrcTime = time2Str(splitLrcData[0]);
						
						mLrcContent.setLrcTime(lrcTime);
						
						lrcList.add(mLrcContent);
						
						mLrcContent = new LrcContent();
						
					}
				}
			}catch(FileNotFoundException e){
				e.printStackTrace();
				strBuilder.append("�]�и��~");
			}catch(IOException e){
				e.printStackTrace();
				strBuilder.append("�]�и��~");
			}
		}
		else
		{
			strBuilder.append("�]�и��~");
		}
		return strBuilder.toString();
		
	}
	
	public int time2Str(String timeStr){
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		
		String timeData[] = timeStr.split("@");
		
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		
		return currentTime; 
	}
	
	public List<LrcContent> getLrcList(){
		return lrcList;
	}
*/
}