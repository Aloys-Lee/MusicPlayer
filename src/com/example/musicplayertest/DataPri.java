package com.example.musicplayertest;

import java.util.List;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * DataPri作为底层数据提供者。例如：DataPri dataPri=DataPri.getInstance();
 * 请在Manifest文件中添加相应的读写SD卡权限。
 * @author rockylee
 *
 */
public class DataPri {
	
	/**
	 * 构造函数。初始化数据查询类MediaUtils
	 */
	private DataPri(){
		mediaUtils =  new MediaUtils();
	}
	
	private static  DataPri single = new DataPri() ;
	private MediaUtils mediaUtils;
	
	/**
	 * 获得数据服务的对象
	 * @return DataPri
	 */
	public static  DataPri getInstance(){
		if (single==null) {
			single = new DataPri();
		}
		return single;
	}
	
	/**
	 * 获得音乐信息列表，请你们注意判断返回值为null的情况
	 * @param context
	 * @return 有的话，返回列表。没有的话，返回null。
	 */
	public List<AudioData> getAudioList(Context context){
		return mediaUtils.getAudioList(context);
	}
	
	/**
	 * 私有数据查询类。主要用来查询数据。
	 * @author rockylee
	 *
	 */
	private class MediaUtils {
	    private  final String[] AUDIO_KEYS = new String[]{
	            MediaStore.Audio.Media._ID,
	            MediaStore.Audio.Media.TITLE,
	            MediaStore.Audio.Media.TITLE_KEY,
	            MediaStore.Audio.Media.ARTIST,
	            MediaStore.Audio.Media.ARTIST_ID,
	            MediaStore.Audio.Media.ARTIST_KEY,
	            MediaStore.Audio.Media.COMPOSER,
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

	    /**
	     * 返回查询到的数据
	     * @param context
	     * @return
	     */
	    public  List<AudioData> getAudioList(Context context) {
	    	
	        List<AudioData> audioList = new ArrayList<AudioData>();
	        ContentResolver resolver;
	        Cursor cursor;
	        try {
	        	resolver = context.getContentResolver();
		        cursor = resolver.query(
		                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		                AUDIO_KEYS,
		               null,
		                null,
		                MediaStore.Audio.Media.DATA);
			} catch (Exception e) {
				return null;
			}
	        

	        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	            Bundle bundle = new Bundle ();
	            for (int i = 0; i < AUDIO_KEYS.length; i++) {
	                final String key = AUDIO_KEYS[i];
	                final int columnIndex = cursor.getColumnIndex(key);
	                final int type = cursor.getType(columnIndex);
	                switch (type) {
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
	            AudioData audio = new AudioData (bundle);
	            audioList.add(audio);
	        }
	        cursor.close();
	        return audioList;
	    }
	}
}
