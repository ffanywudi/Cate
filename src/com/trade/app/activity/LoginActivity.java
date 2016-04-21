package com.trade.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录页面
 * 
 * @ClassName: LoginActivity
 * @Description: TODO
 * @author chewenbing
 * @date
 */
public class LoginActivity extends Activity
{

	// 声明使用到的Button实例
	private Button loginBtn; // 登录按钮
	// 声明使用到的EditText实例
	private EditText userName, userPwd;
	private ImageView login_back;
	// 声明使用到的TextView实例
	private TextView free_register;
    private String sign;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);// 加载登录的界面布局
		// 初始化控件
		init();
		sign=getIntent().getStringExtra("key");
	}

	public void login(View v)
	{
		String username = userName.getText().toString();
		String password = userPwd.getText().toString();

		BmobUser user = new BmobUser();
		user.setUsername(username);
		user.setPassword(password);
		user.login(this, new SaveListener()
		{

			@Override
			public void onSuccess()
			{
				// 登录成功，进入主界面
				Intent it = new Intent(LoginActivity.this, MainActivity.class);
				it.putExtra("key", sign);
				startActivity(it);
				finish();
			}

			@Override
			public void onFailure(int arg0, String error)
			{
				Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	private void init()
	{
		// loginBtn = (Button) findViewById(R.id.btn_login);

		userName = (EditText) findViewById(R.id.username);
		userPwd = (EditText) findViewById(R.id.password);
		login_back = (ImageView) findViewById(R.id.imgView_login_back);
		free_register = (TextView) findViewById(R.id.tv_free_register);
		free_register.setOnClickListener(new OnClickListener()
		{ // 注册

					@Override
					public void onClick(View v)
					{
						Intent it = new Intent(LoginActivity.this,
								RegisterActivity.class);
						startActivity(it);
						finish();
					}
				});
	}

	public void login_back(View v)
	{ // 标题栏 返回操作
		this.finish();
	}

	public void forget_pwd(View v)
	{ // 忘记密码
		Uri uri = Uri.parse("http://3g.qq.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		// Intent intent = new Intent();
		// intent.setClass(Login.this,Whatsnew.class);
		// startActivity(intent);
	}
}
