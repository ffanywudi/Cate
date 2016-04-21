package com.trade.app.activity;

import cn.bmob.v3.BmobUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * @ClassName: Welcome
 * @Description: 欢迎页
 * @author
 * @date 2015-12-5 上午12:34:50
 * 
 */
public class Welcome extends Activity
{
	private Intent it;
	boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏显示
		it = new Intent();
		BmobUser user = BmobUser.getCurrentUser(this);
		if (user != null)
		{
			isLogin = true;
		}
	}

	public void buy(View v)
	{
		if (isLogin)
		{// 如果登陆过了，不用再次登陆。
			it.setClass(Welcome.this, WeCardActivity.class);
		} else
		{
			it.setClass(Welcome.this, LoginActivity.class);
		}
		startActivity(it);
		finish();
	}

	public void sell(View v)
	{
			it.setClass(Welcome.this, RegisterActivity.class);
			
		startActivity(it);
		finish();
	}
}
