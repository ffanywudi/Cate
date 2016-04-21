package com.trade.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
/**
 * 
* @ClassName: RegisterActivity 
* @Description: 注册 
* @author  
* @date 2015-12-5 上午12:44:17 
*
 */
public class RegisterActivity extends Activity
{
	private EditText name, psd, edtChkCode;
	private Button register, reset;
	private ImageView register_back;
	private TextView CheckCode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		init();
	}

	private void init()
	{
		name = (EditText) findViewById(R.id.username);
		psd = (EditText) findViewById(R.id.password);

		register = (Button) findViewById(R.id.register_button);
		register_back = (ImageView) findViewById(R.id.iv_register_back);

		edtChkCode = (EditText) findViewById(R.id.et_identify_code);
		CheckCode = (TextView) findViewById(R.id.txtChkCode);
		CheckCode.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				((TextView) v).setText(getCheckCode());
			}
		});
	}

	private String getCheckCode()
	{
		String strInt = " ";
		Integer i = new Integer((int) (Math.random() * 10000));
		strInt = String.valueOf(i);
		if (strInt.length() != 4)
		{
			return getCheckCode();
		} else
		{
			return strInt;
		}
	}

	public void register(View v)
	{
		if (!CheckCode.getText().toString()
				.equals(edtChkCode.getText().toString()))
		{
			Toast.makeText(this, "验证码错误，请重新输入!", Toast.LENGTH_SHORT).show();
			String chkcode = getCheckCode();
			CheckCode.setText(chkcode);
			return;
		}

		String username = name.getText().toString();
		String password = psd.getText().toString();

		BmobUser user = new BmobUser();
		user.setUsername(username);
		user.setPassword(password);
		user.signUp(this, new SaveListener()
		{
			@Override
			public void onSuccess()
			{
				Toast.makeText(RegisterActivity.this, "注册成功!",
						Toast.LENGTH_SHORT).show();
				// 注册成功，跳转到登录页面
				Intent it = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(it);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1)
			{
				Toast.makeText(RegisterActivity.this, arg1,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void register_back(View v)
	{ // 标题栏 返回操作
		this.finish();
	}

	public void reset(View v)
	{ // 重置按钮
		name.setText("");
		psd.setText("");
		edtChkCode.setText("");
	}
}
