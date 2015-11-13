package com.example.musicplayertest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private TextView music_name;
	private ImageView music_pic;
	private ImageButton music_list;
	private TextView music_singer;
	private TextView music_record;
	private TextView music_cur_time;
	private TextView music_end_time;
	private ImageView music_pre;
	private ImageView music_next;
	private ImageView music_pause_paly;

	private boolean isPlayed;// palying music

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		isPlayed = false;
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
		music_list = (ImageButton) findViewById(R.id.music_list);

		music_name.setOnClickListener(this);
		music_next.setOnClickListener(this);
		music_pre.setOnClickListener(this);
		music_pause_paly.setOnClickListener(this);
		music_list.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.music_name:
			Toast.makeText(this, "点击了歌名，显示歌曲列表", Toast.LENGTH_SHORT).show();
			showMusicList();
			break;
		case R.id.music_pre:
			Toast.makeText(this, "点击了上一首", Toast.LENGTH_SHORT).show();
			switchToPre();
			break;
		case R.id.music_next:
			Toast.makeText(this, "点击了下一首", Toast.LENGTH_SHORT).show();
			switchToNext();
			break;
		case R.id.music_pause:
			switchPlayOrPause();
			controlMusic();
			break;
		case R.id.music_list:
			showMusicList();
			break;

		default:
			break;
		}
	}

	/**
	 * @author lixi
	 * 控制音乐暂停或播放
	 */
	private void controlMusic() {
		// TODO Auto-generated method stub

	}

	/**
	 * @author lixi
	 * 播放下一首音乐
	 */
	private void switchToNext() {
		// TODO Auto-generated method stub

	}

	/**
	 * @author lixi
	 * 播放上一首音乐
	 */
	private void switchToPre() {
		// TODO Auto-generated method stub

	}

	/**
	 * @author lixi
	 * 播放或暂停按钮图片切换
	 */
	private void showMusicList() {
		// TODO Auto-generated method stub

	}

	/**
	 * @author lixi
	 * 播放或暂停按钮图片切换
	 */
	private void switchPlayOrPause() {
		if (isPlayed) {
			isPlayed = false;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_play_press);
			Toast.makeText(this, "点击了暂停", Toast.LENGTH_SHORT).show();
		} else {
			isPlayed = true;
			music_pause_paly
					.setImageResource(R.drawable.widget_music_btn_pause_press);
			Toast.makeText(this, "点击了播放", Toast.LENGTH_SHORT).show();
		}

	}
}
