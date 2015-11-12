package com.example.musicplayertest;

import android.os.Bundle;
import android.provider.MediaStore;

/**
 * AudioData是数据封装类。主要的数据信息请在这里查阅。
 * @author rockylee
 *
 */
public class AudioData {
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
		private int mId,
	                mArtistId,
					mAlbumId,
	                mYear,
					mTrack;
		
		private int mDuration = 0,
					 mSize = 0;
		
		private boolean isRingtone = false,
						isPodcast = false,
						isAlarm = false,
						isMusic = false,
						isNotification = false;
		
		/**
		 * 初始化构造函数。主要用来初始化与查询相关的变量。
		 * @param bundle
		 */
		public AudioData (Bundle bundle) {
	        mId = bundle.getInt(MediaStore.Audio.Media._ID);
			mTitle = bundle.getString(MediaStore.Audio.Media.TITLE);
			mTitleKey = bundle.getString(MediaStore.Audio.Media.TITLE_KEY);
			mArtist = bundle.getString(MediaStore.Audio.Media.ARTIST);
			mArtistKey = bundle.getString(MediaStore.Audio.Media.ARTIST_KEY);
			mComposer = bundle.getString(MediaStore.Audio.Media.COMPOSER);
			mAlbum = bundle.getString(MediaStore.Audio.Media.ALBUM);
			mAlbumKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
			mDisplayName = bundle.getString(MediaStore.Audio.Media.DISPLAY_NAME);
			mYear = bundle.getInt(MediaStore.Audio.Media.YEAR);
			mMimeType = bundle.getString(MediaStore.Audio.Media.MIME_TYPE);
			mPath = bundle.getString(MediaStore.Audio.Media.DATA);
			
			mArtistId = bundle.getInt(MediaStore.Audio.Media.ARTIST_ID);
			mAlbumId = bundle.getInt(MediaStore.Audio.Media.ALBUM_ID);
			mTrack = bundle.getInt(MediaStore.Audio.Media.TRACK);
			mDuration = bundle.getInt(MediaStore.Audio.Media.DURATION);
			mSize = bundle.getInt(MediaStore.Audio.Media.SIZE);
			isRingtone = bundle.getInt(MediaStore.Audio.Media.IS_RINGTONE) == 1;
			isPodcast = bundle.getInt(MediaStore.Audio.Media.IS_PODCAST) == 1;
			isAlarm = bundle.getInt(MediaStore.Audio.Media.IS_ALARM) == 1;
			isMusic = bundle.getInt(MediaStore.Audio.Media.IS_MUSIC) == 1;
			isNotification = bundle.getInt(MediaStore.Audio.Media.IS_NOTIFICATION) == 1;
					 
		}

		/**
		 * 获得歌曲ID
		 * @return
		 */
	    public int getId() {
	        return mId;
	    }
	    
	    /**
	     * 获得音乐格式。如：audio/mpeg audio/amr audio/x-wav
	     * @return
	     */
	    public String getMimeType () {
			return mMimeType;
		}
	    /**
	     * 歌曲文件的总时长。以毫秒为单位。
	     * @return
	     */
		public int getDuration () {
			return mDuration;
		}
		/**
		 * 歌曲文件的大小。单位Byte
		 * @return
		 */
		public int getSize () {
			return mSize;
		}
		
		public boolean isRingtone () {
			return isRingtone;
		}
		
		public boolean isPodcast () {
			return isPodcast;
		}
		
		public boolean isAlarm () {
			return isAlarm;
		}
		
		public boolean isMusic () {
			return isMusic;
		}
		
		public boolean isNotification () {
			return isNotification;
		}
		/**
		 * 获得歌曲的名称	
		 * @return
		 */
		public String getTitle () {
			return mTitle;
		}
		
		public String getTitleKey () {
			return mTitleKey;
		}
		/**
		 * 获得歌曲的歌手名
		 * @return
		 */
		public String getArtist () {
			return mArtist;
		}
		
		public int getArtistId () {
			return mArtistId;
		}
		
		public String getArtistKey () {
			return mArtistKey;
		}
		
		public String getComposer () {
			return mComposer;
		}
		/**
		 * 获得歌曲的专辑名
		 * @return
		 */
		public String getAlbum () {
			return mAlbum;
		}
		
		public int getAlbumId () {
			return mAlbumId;
		}
		
		public String getAlbumKey () {
			return mAlbumKey;
		}
		/**
		 * 获得文件名称。如：李阳.mp3
		 * @return
		 */
		public String getDisplayName () {
			return mDisplayName;
		}
		/**
		 * 获得年份。如：1994
		 * @return
		 */
		public int getYear () {
			return mYear;
		}
		/**
		 * 获得音轨数目。
		 * @return
		 */
		public int getTrack () {
			return mTrack;
		}
		/**
		 * 获得歌曲文件的完整路径。
		 * @return
		 */
		public String getPath () {
			return mPath;
		}
}
