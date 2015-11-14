package com.example.musicplayertest;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class PlayServices extends Service implements OnCompletionListener {
	
	private final IBinder iBinder = new MusicBinder();
	private MediaPlayer mediaPlayer;
	MusicPlayOver onMusicPlayOver;
	
	private boolean isFirstTime = true;
	private boolean isPause = false;
	
	public class MusicBinder extends Binder{
		public PlayServices getServices(){
			return PlayServices.this;
		}
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//init mediaplayer
		mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(PlayServices.this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return iBinder;
	}
	
	public String testConnection(){
		return "Hello World";
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
		isFirstTime = true;
	}
	
	public void play(AudioData mData){
		if(isFirstTime){
			startPlay(mData);
			isFirstTime = false;
			isPause = false;
		}else if(isPause){
			resume();
		}else {
			pause();
		}
	}
	
	public void startPlay(AudioData mData){
		mediaPlayer.reset();
        try {
			mediaPlayer.setDataSource(mData.getPath());
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mediaPlayer.setOnPreparedListener(new PreparedListener());
        isFirstTime = false;
        isPause = false;
	}
	 //-继续播放
    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }
    //-暂停播放
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if(onMusicPlayOver!=null){
     	   onMusicPlayOver.musicPlayOver();
        }
	}
	
	//prepare music
	private class PreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    }
	
	//set call back 
	public void setOnMusicPlayerOver(MusicPlayOver onMusicPlayOver){
		this.onMusicPlayOver = onMusicPlayOver;
	}
	
	//get current music time
	public int getCurrentTime(){
		return mediaPlayer.getCurrentPosition();
	}
	
	public void playCurrentTime(int currentTime){
		mediaPlayer.seekTo(currentTime);
	}
	
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}
	


}
