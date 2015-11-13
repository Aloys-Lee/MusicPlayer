package com.example.musicplayertest;

import java.util.List;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

/**
 * DataPri作为底层数据提供者。例如：DataPri dataPri=DataPri.getInstance();
 * 请在Manifest文件中添加相应的读写SD卡权限。
 * 
 * @author rockylee
 *
 */
public class DataPri {

	private final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	private final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();

	/**
	 * 构造函数。初始化数据查询类MediaUtils
	 */
	private DataPri() {
		mediaUtils = new MediaUtils();
	}

	private static DataPri single = new DataPri();
	private MediaUtils mediaUtils;

	/**
	 * 获得数据服务的对象
	 * 
	 * @return DataPri
	 */
	public static DataPri getInstance() {
		if (single == null) {
			single = new DataPri();
		}
		return single;
	}

	/**
	 * 获得音乐信息列表，请你们注意判断返回值为null的情况
	 * 
	 * @param context
	 * @return 有的话，返回列表。没有的话，返回null。
	 */
	public List<AudioData> getAudioList(Context context) {
		return mediaUtils.getAudioList(context);
	}

	/**
	 * 根据音乐id删除，本地音乐
	 * 
	 * @param path
	 * @return 删除结果。0表示成功，1表示文件不存在，2表示删除异常。
	 */
	public int delMusic(String path) {
		int re;
		File file = new File(path);
		try {
			if (!file.exists()) {
				re = 1;
			} else {
				file.delete();
				re = 0;
			}
		} catch (Exception e) {
			re = 2;
		}
		return re;
	}

	/**
	 * 获取音乐专辑封面bitmap图片。
	 * 
	 * @param context
	 *            设备上下文
	 * @param song_id
	 *            歌曲id
	 * @param album_id
	 *            专辑id
	 * @param allowdefault
	 *            没找到是否允许传回默认图片.
	 * @return .bitmap专辑图片。null表示没找到图片。
	 */
	public Bitmap getArtwork(Context context, long song_id, long album_id,  
            boolean allowdefault) {  
        if (album_id < 0) {  
            // This is something that is not in the database, so get the album art directly  
            // from the file.  
            if (song_id >= 0) {  
                Bitmap bm = getArtworkFromFile(context, song_id, -1);  
                if (bm != null) {  
                    return bm;  
                }  
            }  
            if (allowdefault) {  
                return getDefaultArtwork(context);  
            }  
            return null;  
        }  
        ContentResolver res = context.getContentResolver();  
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);  
        if (uri != null) {  
            InputStream in = null;  
            try {  
                in = res.openInputStream(uri);  
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);  
            } catch (FileNotFoundException ex) {  
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or  
                // maybe it never existed to begin with.  
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);  
                if (bm != null) {  
                    if (bm.getConfig() == null) {  
                        bm = bm.copy(Bitmap.Config.RGB_565, false);  
                        if (bm == null && allowdefault) {  
                            return getDefaultArtwork(context);  
                        }  
                    }  
                } else if (allowdefault) {  
                    bm = getDefaultArtwork(context);  
                }  
                return bm;  
            } finally {  
                try {  
                    if (in != null) {  
                        in.close();  
                    }  
                } catch (IOException ex) {  
                }  
            }  
        }  
          
        return null;  
    }  
	/**
	 * 直接从文件内部获取专辑图片
	 * 
	 * @param context
	 * @param songid
	 * @param albumid
	 * @return 返回.bitmp图片。null表示没有。
	 */
	private Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
		Bitmap bm = null;
		byte[] art = null;
		String path = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			}
		} catch (FileNotFoundException ex) {
		}
		return bm;
	}
/**
 * 获取自定义默认图片
 * @param context
 * @return
 */
	private Bitmap getDefaultArtwork(Context context) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inPreferredConfig = Bitmap.Config.RGB_565;          
        return BitmapFactory.decodeStream(  
                context.getResources().openRawResource(R.drawable.ic_launcher), null, opts);                 
    }  
	/**
	 * 私有数据查询类。主要用来查询数据。
	 * 
	 * @author rockylee
	 *
	 */
	private class MediaUtils {
		private final String[] AUDIO_KEYS = new String[] {
				MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.TITLE_KEY,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ARTIST_ID,
				MediaStore.Audio.Media.ARTIST_KEY,
				MediaStore.Audio.Media.COMPOSER,
				MediaStore.Audio.Media.ALBUM_ID,
				MediaStore.Audio.Media.ALBUM_KEY,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.TRACK,
				MediaStore.Audio.Media.IS_RINGTONE,
				MediaStore.Audio.Media.IS_PODCAST,
				MediaStore.Audio.Media.IS_ALARM,
				MediaStore.Audio.Media.IS_MUSIC,
				MediaStore.Audio.Media.IS_NOTIFICATION,
				MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DATA };

		/**
		 * 返回查询到的数据
		 * 
		 * @param context
		 * @return
		 */
		public List<AudioData> getAudioList(Context context) {

			List<AudioData> audioList = new ArrayList<AudioData>();
			ContentResolver resolver;
			Cursor cursor;
			try {
				resolver = context.getContentResolver();
				cursor = resolver.query(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						AUDIO_KEYS, null, null, MediaStore.Audio.Media.DATA);
			} catch (Exception e) {
				return null;
			}

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Bundle bundle = new Bundle();
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
				AudioData audio = new AudioData(bundle);
				audioList.add(audio);
			}
			cursor.close();
			return audioList;
		}
	}
}
