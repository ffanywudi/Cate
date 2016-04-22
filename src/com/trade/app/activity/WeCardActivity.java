package com.trade.app.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.trade.app.multiphotopicker.view.PublishActivity;

/**
 * 主页面
 * 
 * @ClassName: WeCardActivity
 * @Description: TODO
 * @author chewenbing
 * @date
 */
public class WeCardActivity extends Activity implements OnClickListener {

	private Button one_btn;

	private Button two_btn;

	private Button three_btn;

	private Button four_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_detail);// 加载登录的界面布局
		// 初始化控件
		initView();
		initLisenter();
	}

	private void initLisenter() {
		one_btn.setOnClickListener(this);
		two_btn.setOnClickListener(this);
		three_btn.setOnClickListener(this);
		four_btn.setOnClickListener(this);

	}

	private void initView() {
		one_btn = (Button) findViewById(R.id.one_relayout_btn);
		two_btn = (Button) findViewById(R.id.two_relayout_btn);
		three_btn = (Button) findViewById(R.id.three_relayout_btn);
		four_btn = (Button) findViewById(R.id.four_relayout_btn);

	}

	@Override
	public void onClick(View v) {
		if (v == one_btn) {
			Intent it = new Intent(this, MainActivity.class);
			startActivity(it);
		}
		if (v == two_btn) {
			Intent it = new Intent(this, PublishActivity.class);
			startActivity(it);
		}
		if (v == three_btn) {
			Intent it = new Intent(this, PublishActivity.class);
			it.putExtra("beijing", "beijing");
			it.putExtra("first", "first");
			startActivity(it);
		}
		if (v == four_btn) {
			 Intent it = new Intent(this, MusicChooseActivity.class);
			 startActivity(it);
		}

	}
}
