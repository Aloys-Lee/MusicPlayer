package com.example.musicplayertest;

import java.util.List;

import android.R.integer;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayertest.PlayServices.MusicBinder;
import com.liushuan.MediaUtil.MediaUtil;
import com.liushuan.MediaUtil.RoundImageView;

public class MainActivity extends Activity implements OnClickListener {
	private TextView music_name;
	private ImageView music_pic;
	private TextView music_singer;
	private TextView music_record;
	private TextView music_cur_time;
	private TextView music_end_time;
	private ImageView music_pre;
	private ImageView music_next;
	private ImageView music_pause_paly;
	private ImageView music_list;

	private ImageView albumCD;
	private RoundImageView albumpicture;
	ObjectAnimator animator = null, animator1 = null;
	private float currentValue = 0f;

	// music popwindow
	private MusicListPop menuWindow;
	private List<AudioData> musicData;
	private DataPri dataPri;
	private int lastSelectPosition;
	// music service
	private PlayServices playServices;
	private boolean isBound = false;
	// music data
	private List<AudioData> musicList;
	private int currentPosition = 0;

	private boolean isPlayed;// palying music
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		isPlayed = false;
		// start service
		Intent intent = new Intent(this, PlayServices.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		getData();

		// 初始化界面信息
		initUI(currentPosition);
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
		music_list = (ImageView) findViewById(R.id.music_list);

		albumCD = (ImageView) findViewById(R.id.music_pic);
		albumpicture = (RoundImageView) findViewById(R.id.music_people);

		music_name.setOnClickListener(this);
		music_next.setOnClickListener(this);
		music_pre.setOnClickListener(this);
		music_pause_paly.setOnClickListener(this);
		music_list.setOnClickListener(this);

		// music list popwindow data init
		dataPri = DataPri.getInstance();

		musicData = dataPri.getAudioList(MainActivity.this);


	}

	private void initAlbum() {
		animator = ObjectAnimator.ofFloat(albumCD, "Rotation",
				currentValue - 360, currentValue).setDuration(20000);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.setInterpolator(null);

		animator1 = ObjectAnimator.ofFloat(albumpicture, "Rotation",
				currentValue - 360, currentValue).setDuration(20000);
		animator1.setRepeatCount(ObjectAnimator.INFINITE);
		animator1.setInterpolator(null);

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				currentValue = (Float) animation.getAnimatedValue();
			}
		});
	}

	private void initUI(int i) {

		music_name.setText(musicList.get(i).getTitle());
		music_singer.setText(musicList.get(i).getArtist());
		music_record.setText(musicList.get(i).getAlbum());

		Bitmap bm = MediaUtil.getArtwork(this, musicList.get(i).getId(),
				musicList.get(i).getAlbumId(), false);
		if (bm != null) {
			albumpicture.setImageBitmap(bm);
		} else {
			/*Toast.makeText(
					MainActivity.this,
					"bm = null" + musicList.size()
							+ musicList.get(i).getTitle()
							+ musicList.get(i).getTitle()
							+ musicList.get(i).getAlbum(), Toast.LENGTH_SHORT)
					.show();*/
		}

	}

	// get music data
	private void getData() {
		DataPri dataPri = DataPri.getInstance();
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
			//Toast.makeText(this, "上", Toast.LENGTH_SHORT).show();
			switchToPre();
			break;
		case R.id.music_next:
			//Toast.makeText(this, "下", Toast.LENGTH_SHORT).show();
			switchToNext();
			break;
		case R.id.music_pause:
			switchPlayOrPause();
			controlMusic();

			break;

		case R.id.music_list:
			menuWindow = new MusicListPop(MainActivity.this,
					new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0,
						View arg1, int position, long arg3) {

					System.out.println("点击了没有");
					//记录最后一次点击的歌曲位置
					lastSelectPosition = position;
				}
			}, lastSelectPosition, musicData);
			// 显示窗口
			menuWindow.showAtLocation(
					MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;

		default:
			break;
		}
	}

	private void controlMusic() {
		// TODO Auto-generated method stub
		playServices.play(musicList.get(currentPosition));
	}

	private void switchToNext() {
		// TODO Auto-generated method stub
		// 停止播放
		if(animator!=null){
			animator.end();
			animator1.end();
			currentValue = 0;
		}
		currentPosition +=1;
		if(currentPosition>musicList.size()-1)
			currentPosition = 0;
		playServices.startPlay(musicList.get(currentPosition));
		initUI(currentPosition);
		isPlayed = false;
		switchPlayOrPause();
	}

	private void switchToPre() {
		// TODO Auto-generated method stub

		// 停止播放动画
		if(animator!=null){
			animator.end();
			animator1.end();
			currentValue = 0;
		}
		currentPosition -=1;
		if(currentPosition<0)
			currentPosition = musicList.size()-1;
		playServices.startPlay(musicList.get(currentPosition));
		initUI(currentPosition);
		isPlayed = false;
		switchPlayOrPause();
	}

	private void showMusicList() {
		// TODO Auto-generated method stub

	}

	private void switchPlayOrPause() {
		if (isPlayed) {
			isPlayed = false;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_play_press);
			animator.cancel();
			animator1.cancel();

		} else {
			isPlayed = true;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_pause_press);
			initAlbum();
			animator.start();
			animator1.start();
		}
	}

	// bind service call back
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
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
