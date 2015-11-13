package com.example.musicplayertest;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayertest.PlayServices.MusicBinder;
import com.liushuan.MediaUtil.MediaUtil;
import com.liushuan.MediaUtil.RoundImageView;

public class MainActivity extends Activity implements OnClickListener {
	private TextView music_name; // �����spinner���������滻��������
	private ImageView music_pic;
	private TextView music_singer;
	private TextView music_record;
	private TextView music_cur_time;
	private TextView music_end_time;
	private ImageView music_pre;
	private ImageView music_next;
	private ImageView music_pause_paly;
	
	private ImageView albumCD;
	private RoundImageView albumpicture;
	ObjectAnimator animator=null , animator1=null;
	private float currentValue = 0f;
	
	//music service
	private PlayServices playServices;
	private boolean isBound = false;
	//music data
	private List<AudioData> musicList;

	private boolean isPlayed;// palying music

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		isPlayed = false;
		//start service
		Intent intent = new Intent(this, PlayServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        getData();
        
        //初始化界面信息
        initUI(0);
	}

	private void init() {
		music_name = (TextView) findViewById(R.id.music_name);
		music_singer = (TextView) findViewById(R.id.music_singer);
		music_record = (TextView) findViewById(R.id.music_record);
		music_cur_time = (TextView) findViewById(R.id.music_cur_time);
		music_end_time = (TextView) findViewById(R.id.music_end_time);
		music_pic = (ImageView) findViewById(R.id.music_pic);
		music_pre = (ImageView) findViewById(R.id.music_pre);
		music_next = (ImageView) findViewById(R.id.music_next);
		music_pause_paly = (ImageView) findViewById(R.id.music_pause);
		
		albumCD = (ImageView)findViewById(R.id.music_pic);
		albumpicture = (RoundImageView)findViewById(R.id.music_people);
		
		music_name.setOnClickListener(this);
		music_next.setOnClickListener(this);
		music_pre.setOnClickListener(this);
		music_pause_paly.setOnClickListener(this);
	}
	
	
	private void initAlbum(){
		animator = ObjectAnimator.ofFloat(albumCD, "Rotation", currentValue-360,currentValue).setDuration(20000);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.setInterpolator(null);
		
		animator1 = ObjectAnimator.ofFloat(albumpicture, "Rotation", currentValue-360,currentValue).setDuration(20000);
		animator1.setRepeatCount(ObjectAnimator.INFINITE);
		animator1.setInterpolator(null);
		
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				currentValue = (Float)animation.getAnimatedValue();
			}
		});	
	}
	private void initUI(int i){
		
		music_name.setText(musicList.get(0).getTitle());
		music_singer.setText(musicList.get(0).getArtist());
		music_record.setText(musicList.get(0).getArtistKey());
		
		Bitmap bm = MediaUtil.getArtwork(this, musicList.get(1).getId(), musicList.get(1).getAlbumId(),false);
		if (bm != null){
			albumpicture.setImageBitmap(bm);
		}else{
			Toast.makeText(MainActivity.this, "bm = null"+musicList.size()+musicList.get(0).getTitle()+musicList.get(0).getTitle()+musicList.get(0).getAlbum(), Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//get music data
	private void getData(){
		DataPri dataPri=DataPri.getInstance();
		musicList = dataPri.getAudioList(MainActivity.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.music_name:
			Toast.makeText(this, "歌曲名字", Toast.LENGTH_SHORT).show();
			showMusicList();
			break;
		case R.id.music_pre:
			Toast.makeText(this, "上", Toast.LENGTH_SHORT).show();
			switchToPre();
			break;
		case R.id.music_next:
			Toast.makeText(this, "下", Toast.LENGTH_SHORT).show();
			switchToNext();
			
			break;
		case R.id.music_pause:
			
			//test play music	
			if(musicList.size()>0)
				playServices.play(musicList.get(0));
				
				switchPlayOrPause();
				controlMusic();
				
			
			break;
		default:
			break;
		}
	}

	private void controlMusic() {
		// TODO Auto-generated method stub

	}

	private void switchToNext() {
		// TODO Auto-generated method stub
		//停止播放
		animator.end();
		animator1.end();
		currentValue = 0;
		
		
	}

	private void switchToPre() {
		// TODO Auto-generated method stub
		
		//停止播放动画
		animator.end();
		animator1.end();
		currentValue = 0;
	}

	private void showMusicList() {
		// TODO Auto-generated method stub

	}

	private void switchPlayOrPause() {
		if (isPlayed) {
			isPlayed = false;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_play_press);
			Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
			
			animator.cancel();
			animator1.cancel();
			
		} else {
			isPlayed = true;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_pause_press);
			Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();
			
			initAlbum();
			animator.start();
			animator1.start();
			
		}

	}
	
	//bind service call back
	private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
        	MusicBinder binder = (MusicBinder) service;
        	playServices = binder.getServices();
        	isBound = true;
        	System.out.println(playServices.testConnection());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }
}
