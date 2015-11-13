package com.example.musicplayertest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

	private TextView tvMusicListNums;

	public BaseAdapter adapter;

	private int lastSelectPosition;

	public MusicListPop(Activity context1,
			OnItemClickListener onItemClickListener1, int lastSelectPosition1,
			List<AudioData> data1) {
		super(context1);

		this.onItemClickListener = onItemClickListener1;

		this.context = context1;

		this.adapter = new MyAdapter(context1, data1);

		this.lastSelectPosition = lastSelectPosition1;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.music_list, null);

		listView = (ListView) mMenuView.findViewById(R.id.list);

		tvTitle = (TextView) mMenuView.findViewById(R.id.music_list_title);

		tvMusicListNums = (TextView) mMenuView
				.findViewById(R.id.music_list_nums);

		tvMusicListNums.setText("(" + data1.size() + "首)");

		listView.setAdapter(adapter);

		listView.setSelectionFromTop(lastSelectPosition, 0);
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

	private class MyAdapter extends BaseAdapter {

		private Context context;

		private List<AudioData> data;

		public MyAdapter(Context context1, List<AudioData> data1) {

			this.context = context1;

			this.data = data1;

		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public final class ViewHolder {

			ImageView itemImg;

			TextView itemtitle;

			TextView itemName;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.menu_item, null);

				viewHolder.itemImg = (ImageView) convertView
						.findViewById(R.id.music_item_img);
				viewHolder.itemtitle = (TextView) convertView
						.findViewById(R.id.music_item_title);
				viewHolder.itemName = (TextView) convertView
						.findViewById(R.id.music_item_name);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			AudioData audioData = data.get(position);
			int musicId = audioData.getId();
			int albumId = audioData.getAlbumId();

			String musicTitle = audioData.getTitle();
			String musicArtist = " - " + audioData.getArtist();
			// Bitmap musicPic = dataPri.getArtwork(MainActivity.this, musicId,
			// albumId, true);

			viewHolder.itemName.setText(musicArtist);
			viewHolder.itemtitle.setText(musicTitle);
			if (lastSelectPosition == position) {
				viewHolder.itemName.setTextColor(context.getResources()
						.getColor(R.color.green));
				viewHolder.itemtitle.setTextColor(context.getResources()
						.getColor(R.color.green));
			} else {
				viewHolder.itemName.setTextColor(context.getResources()
						.getColor(R.color.grey));
				viewHolder.itemtitle.setTextColor(context.getResources()
						.getColor(R.color.white));
			}

			// viewHolder.itemImg.setImageBitmap(musicPic);

			return convertView;
		}
	}

	public interface ItemOnclick {

		public void changeClick(int position);

	}

}
