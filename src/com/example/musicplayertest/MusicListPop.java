package com.example.musicplayertest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 音乐列表Popupwindow
 * 
 * @author liang.chen
 * 
 */
public class MusicListPop extends PopupWindow {
	private View mMenuView;

	private OnItemClickListener onItemClickListener;

	private Context context;

	private ListView listView;

	private TextView tvTitle;

	private TextView tvMusicNums;

	private BaseAdapter adapter;

	public MusicListPop(Activity context1,
			OnItemClickListener onItemClickListener1, BaseAdapter adapter1) {
		super(context1);

		this.onItemClickListener = onItemClickListener1;

		this.context = context1;

		this.adapter = adapter1;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.music_list, null);

		int musicNums = adapter1.getCount();

		listView = (ListView) mMenuView.findViewById(R.id.list);

		tvTitle = (TextView) mMenuView.findViewById(R.id.music_list_title);

		tvMusicNums = (TextView) mMenuView.findViewById(R.id.music_list_nums);

		tvMusicNums.setText("(" + musicNums + "首)");

		listView.setAdapter(adapter);

		// listView.setOnItemSelectedListener(itemSelectedListener);

		listView.setOnItemClickListener(onItemClickListener1);

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		BaseTools baseTools = new BaseTools();
		this.setHeight(baseTools.getWindowHeigh(context) * 2 / 3);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationPop);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

	}

	public class BaseTools {
		// 获取屏幕分辨率
		public int getWindowWidth(Context context) {

			WindowManager wm = (WindowManager) (context
					.getSystemService(Context.WINDOW_SERVICE));
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			int mScreenWidth = dm.widthPixels;
			return mScreenWidth;
		}

		public int getWindowHeigh(Context context) {
			// 获取屏幕分辨率
			WindowManager wm = (WindowManager) (context
					.getSystemService(Context.WINDOW_SERVICE));
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			int mScreenHeigh = dm.heightPixels;
			return mScreenHeigh;
		}
	}

}
