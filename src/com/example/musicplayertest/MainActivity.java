package com.example.musicplayertest;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayertest.PlayServices.MusicBinder;
import com.liushuan.MediaUtil.MediaUtil;
import com.liushuan.MediaUtil.RoundImageView;

public class MainActivity extends Activity implements OnClickListener,
		MusicPlayOver {
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
	private SeekBar musicSeekBar;

	private ImageView albumCD;
	private RoundImageView albumpicture;
	ObjectAnimator animator = null, animator1 = null;
	private float currentValue = 0f;
	Bitmap bm;
	// music popwindow
	private MusicListPop menuWindow;

	// music service
	private PlayServices playServices;
	private boolean isBound = false;
	// music data
	private List<AudioData> musicList;
	private int currentPosition = 0;

	private boolean isPlayed = false;// palying music

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		// start service
		Intent intent = new Intent(this, PlayServices.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		getData();
		init();
		initPopWindow();
		// 初始化界面信息
		initUI(currentPosition);
		registerHeadsetPlugReceiver();
	}

	// init popwindow
	public void initPopWindow() {
		menuWindow = new MusicListPop(MainActivity.this,
				new OnItemClickListener() {

					@TargetApi(Build.VERSION_CODES.HONEYCOMB)
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						System.out.println("点击了没有");
						// 记录最后一次点击的歌曲位置
						currentPosition = position;
						menuWindow.lastSelectPosition = currentPosition;
						menuWindow.adapter.notifyDataSetChanged();
						playServices.startPlay(musicList.get(currentPosition));
						if (animator != null) {
							animator.end();
							animator1.end();
							currentValue = 0;
						}
						initUI(currentPosition);
						isPlayed = false;
						switchPlayOrPause();
					}
				}, currentPosition, musicList);
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

		// seek bar
		musicSeekBar = (SeekBar) findViewById(R.id.music_seekBar);
		musicSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

		music_name.setOnClickListener(this);
		music_next.setOnClickListener(this);
		music_pre.setOnClickListener(this);
		music_pause_paly.setOnClickListener(this);
		music_list.setOnClickListener(this);

	}

	// init album animation
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
		AudioData mData = musicList.get(i);
		music_name.setText(mData.getTitle());
		music_singer.setText(mData.getArtist());
		music_record.setText(mData.getAlbum());
		music_end_time.setText(DataPri.formatTime(mData.getDuration()));
		musicSeekBar.setMax(mData.getDuration());

		bm = MediaUtil.getArtwork(this, musicList.get(i).getId(), musicList
				.get(i).getAlbumId(), false);
		if (bm != null) {
			albumpicture.setImageBitmap(bm);
		} else {
			/*
			 * Toast.makeText( MainActivity.this, "bm = null" + musicList.size()
			 * + musicList.get(i).getTitle() + musicList.get(i).getTitle() +
			 * musicList.get(i).getAlbum(), Toast.LENGTH_SHORT) .show();
			 */
			albumpicture.setImageResource(R.drawable.defaultalbum);
		}

	}

	// get music data
	private void getData() {
		DataPri dataPri = DataPri.getInstance();
		musicList = dataPri.getAudioList(MainActivity.this);
		if (musicList == null || musicList.size() <= 0) {
			Toast.makeText(this, "no music found exit..", Toast.LENGTH_LONG)
					.show();
			finish();
		}
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
			// Toast.makeText(this, "上", Toast.LENGTH_SHORT).show();
			switchToPre();
			break;
		case R.id.music_next:
			// Toast.makeText(this, "下", Toast.LENGTH_SHORT).show();
			switchToNext();
			break;
		case R.id.music_pause:
			controlMusic();
			switchPlayOrPause();
			break;

		case R.id.music_list:

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void switchToNext() {
		// TODO Auto-generated method stub
		// 停止播放
		if (animator != null) {
			animator.end();
			animator1.end();
			currentValue = 0;
		}
		currentPosition += 1;

		if (currentPosition > musicList.size() - 1)
			currentPosition = 0;

		if (menuWindow.isShowing()) {
			menuWindow.lastSelectPosition = currentPosition;
			menuWindow.adapter.notifyDataSetChanged();
		}
		playServices.startPlay(musicList.get(currentPosition));
		initUI(currentPosition);
		isPlayed = false;
		switchPlayOrPause();
		bm = null;
	}

	private void switchToPre() {
		// TODO Auto-generated method stub

		// 停止播放动画
		if (animator != null) {
			animator.end();
			animator1.end();
			currentValue = 0;
		}
		currentPosition -= 1;
		if (menuWindow.isShowing()) {
			menuWindow.lastSelectPosition = currentPosition;
			menuWindow.adapter.notifyDataSetChanged();
		}
		if (currentPosition < 0)
			currentPosition = musicList.size() - 1;
		playServices.startPlay(musicList.get(currentPosition));
		initUI(currentPosition);
		isPlayed = false;
		switchPlayOrPause();
		bm = null;
	}

	private void showMusicList() {
		// TODO Auto-generated method stub

	}

	private void switchPlayOrPause() {
		if (isPlayed) {
			isPlayed = false;
			music_pause_paly.setImageResource(R.drawable.widget_play_normal);
			if (animator != null) {
				animator.cancel();
				animator1.cancel();
			}

		} else {
			isPlayed = true;
			music_pause_paly.setImageResource(R.drawable.widget_pause_normal);
			initAlbum();
			animator.start();
			animator1.start();
		}
		handler.sendEmptyMessageDelayed(1, 0);
	}

	// bind service call back
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			playServices = binder.getServices();
			playServices.setOnMusicPlayerOver(MainActivity.this);
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
		System.out.println("-----------------------on destory");
		unregisterReceiver();
	}

	@Override
	public void musicPlayOver() {
		// TODO Auto-generated method stub
		switchToNext();
	}

	public void updateSeekBar() {
		// music_cur_time
		if (playServices != null && playServices.isPlaying()) {
			music_cur_time.setText(DataPri.formatTime(playServices
					.getCurrentTime()));
			musicSeekBar.setProgress(playServices.getCurrentTime());
		}
	}

	// update seekbar
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				updateSeekBar();
				handler.sendEmptyMessageDelayed(1, 1000);
				break;
			}
		};
	};
	private HeadsetPlugReceiver headsetPlugReceiver;

	// onSeekBarChangeListener
	private class SeekBarChangeListener implements
			SeekBar.OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			handler.removeMessages(1);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			playServices.playCurrentTime(seekBar.getProgress());
			handler.sendEmptyMessage(1);
		}
	}

	// 按返回键提示框
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new AlertDialog.Builder(this)
					.setTitle("退出!")
					.setMessage("你确定要退出吗?")
					.setNegativeButton("取消", null)
					.setPositiveButton("退出",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									handler.removeMessages(1);

									finish();
								}
							}).show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_main1);
		init();
		initUI(currentPosition);
		isPlayed = !playServices.isPlaying();
		switchPlayOrPause();
	}

	private class HeadsetPlugReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
				int state = intent.getIntExtra("state", -1);
				switch (state) {
				case 0:
					// 拔出耳机
					if (playServices != null) {
						playServices.pause();
						isPlayed = true;
						switchPlayOrPause();
					}

					break;
				case 1:
					// 插耳机自动播放
					if (isPlayed)
						break;
					if (playServices != null) {
						playServices.resume();
						isPlayed = false;
						switchPlayOrPause();
					}
					break;
				default:
					break;
				}

			}
			// //只监听拔出耳机
			// if
			// (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction()))
			// {
			// if(playServices!=null){
			// playServices.pause();
			// switchPlayOrPause();
			// }
			// }
		}
	}

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.HEADSET_PLUG");
		registerReceiver(headsetPlugReceiver, filter);
	}

	private void unregisterReceiver() {
		this.unregisterReceiver(headsetPlugReceiver);
	}
}
