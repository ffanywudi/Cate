package com.trade.app.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.trade.app.adapter.QuickAdapter;
import com.trade.app.bean.MusicInfo;

/**
 * 闪屏页
 * 
 */
public class CodeActivity extends BaseActivity {

	private ArrayList<MusicInfo> mylist;

	private QuickAdapter<MusicInfo> adapter_dress;

	public ListView music_list;

	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.code);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		back = (Button) findViewById(R.id.back_id);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
