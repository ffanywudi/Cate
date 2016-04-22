package com.trade.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.bmob.v3.Bmob;

/**
 * 闪屏页
 *
 */
public class AppStart extends Activity{
	String AppID = "c701e364a2db9f90d25388f3d9753a46";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏显示
		
		setContentView(R.layout.appstart);
		 // 初始化 Bmob SDK
        Bmob.initialize(this, AppID);
		
	new Handler(getMainLooper()).postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (AppStart.this,Welcome.class);			
			startActivity(intent);			
			AppStart.this.finish();
			}
		}, 3000);
	}
}
