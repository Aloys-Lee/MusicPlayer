package com.example.musicplayertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow;

/**
 * 音乐列表Popupwindow
 * 
 * @author liang.chen
 * 
 */
public class MusicListPop extends PopupWindow {
	private OnItemSelectedListener itemSelectedListener;

	private Context context;

	private View mMenuView;

	public MusicListPop(Context context1,
			OnItemSelectedListener itemSelectedListener1) {

		super(context1);

		this.context = context1;

		this.itemSelectedListener = itemSelectedListener1;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
//		this.mMenuView =  
		
		
//		// 设置SelectPicPopupWindow的View
//		this.setContentView(mMenuView);
//		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.FILL_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimationPop);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);

	}

}
