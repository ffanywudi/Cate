package com.trade.app.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * 基类
 */
public abstract class BaseActivity extends Activity {

	protected int mScreenWidth;
	protected int mScreenHeight;

	private long exitTime = 0;
	private Button exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取当前屏幕宽高
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		initView();
		initData();
	}

	public abstract void initData();

	public abstract void initView();

	Toast mToast;

	public void ShowToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(
							BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_SHORT);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});

	}

	/**
	 * 获取当前状态栏的高度 getStateBar
	 * 
	 * @Title: getStateBar
	 * @throws
	 */
	public int getStateBar() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale * dipValue + 0.5f);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出!",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}
}
