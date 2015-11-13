package com.liushuan.MediaUtil;

import android.graphics.Bitmap;
import android.net.Uri;

/* Mp3 基本信息类*/
public class Mp3Info {

	private long id;
	private String title;
	private String artist;
	private String album;
	private long size;
	private String url;
	
	private long albumId;
	private long duration;
	
	
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration2) {
		this.duration = duration2;
	}
	public long getId() {
		return id;
	}
	public void setId(long id2) {
		this.id = id2;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album2) {
		this.album = album2;
	}
	
	public long getSize() {
		return size;
	}
	public void setSize(long size2) {
		this.size = size2;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url2) {
		this.url = url2;
	}
	
	

}
