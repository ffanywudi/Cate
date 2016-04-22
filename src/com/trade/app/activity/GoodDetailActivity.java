package com.trade.app.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

import com.trade.app.adapter.BaseAdapterHelper;
import com.trade.app.adapter.QuickAdapter;
import com.trade.app.bean.Book;
import com.trade.app.bean.Comment;

/**
 * 
 * @ClassName: GoodDetailActivity
 * @Description: 商品详情
 * @author
 * @date 2015-12-5 上午12:35:11
 * 
 */
public class GoodDetailActivity extends Activity implements OnClickListener {
	/**
	 * 名
	 */
	private TextView sp_name;
	/**
	 * 签名
	 */
	private TextView sp_price;
	
	private TextView wechart;
	
	/**
	 * 图片
	 */
	private ImageView goodImage;
	/**
	 * 服务器id数组
	 */
	private String[] sb = null;
	/**
	 * listview
	 */
	private ListView listview;
	/**
	 * 右下角的评论按钮
	 */
	private Button btn_add;
	/**
	 * 评论adaper
	 */
	private QuickAdapter<Comment> commentAdapter;
	/**
	 * 评论弹出框
	 */
	private AlertDialog mMaterialDialog;
	/**
	 * 评论标题
	 */
	private String title = "";
	/**
	 * 评论描述
	 */
	private String describe = "";
	/**
	 * 评论手机
	 */
	private String phone = "";
	/**
	 * 评论编辑框
	 */
	private EditText edit_title, edit_phone, edit_describe;
	/**
	 * 评论选择按钮
	 */
	private Button btn_back, btn_true;
	/**
	 * 添加按钮
	 */
	private TextView tv_add;
	/**
	 * 返回按钮
	 */
	private ImageView back;
	/**
	 * 商品服务器id
	 */
	String objectId;

	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xx);
		Intent it = getIntent();
		initView();
		initData();
		sb = it.getStringArrayExtra("sb");
		if (sb != null) {
			objectId = sb[1];
		} else {
			objectId = "";
		}
		query();
		queryComments();
	}

	/**
	 * 
	 * @Title: query
	 * @Description: 查询获取商品数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void query() {
		// 查询数据库
		@SuppressWarnings("rawtypes")
		BmobQuery query = new BmobQuery<Book>();
		query.getObject(this, objectId, new GetListener<Book>() {
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(GoodDetailActivity.this, arg1, 0).show();
			}

			@Override
			public void onSuccess(Book book) {
				Book b = book;
				sp_name.setText("昵称 :" + b.getName()  !=null ? b.getName():"");
				sp_price.setText(b.getPrice()  !=null ? b.getPrice():"");
				wechart.setText( b.getWechart() !=null ? b.getWechart():"");
				BmobFile pic = b.getPic();
				pic.loadImage(GoodDetailActivity.this, goodImage); 
			}
		});

	}

	/**
	 * 
	 * @Title: initView
	 * @Description: 初始化view
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initView() {
		wechart=(TextView) findViewById(R.id.wechant);
		listview = (ListView) findViewById(R.id.list_lost);
		btn_add = (Button) findViewById(R.id.btn_add);
		goodImage = (ImageView) findViewById(R.id.goodImage);
		sp_name = (TextView) findViewById(R.id.sp_name);
		sp_price = (TextView) findViewById(R.id.sp_price);
		btn_add = (Button) findViewById(R.id.btn_add);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		btn_add.setOnClickListener(this);

	}

	/**
	 * 
	 * @Title: initData
	 * @Description: 初始化数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void initData() {
		if (commentAdapter == null) {
			commentAdapter = new QuickAdapter<Comment>(this, R.layout.item_list) {

				@Override
				protected void convert(BaseAdapterHelper helper, Comment c) {
					helper.setText(R.id.tv_title, c.getTitle())
							.setText(R.id.tv_describe, c.getDescribe())
							.setText(R.id.tv_time, c.getCreatedAt())
							.setText(R.id.tv_phone, c.getPhone())
							.setText(R.id.tv_user, c.getUser());
				}
			};
		}
		listview.setAdapter(commentAdapter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void cha_book(BmobQuery query, String objectId) {
		query.getObject(this, objectId, new GetListener<Book>() {
			@Override
			public void onFailure(int arg0, String error) {
				Toast.makeText(GoodDetailActivity.this, error, 0).show();
			}

			@Override
			public void onSuccess(Book book) {
				Book b = book;
				sp_name.setText(b.getName());
				sp_price.setText(b.getPrice());
				BmobFile pic = b.getPic();
				pic.loadImage(GoodDetailActivity.this, goodImage);
			}
		});
	}

	/**
	 * 
	 * @Title: queryComments
	 * @Description: 查询评论数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void queryComments() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.order("createdAt");// 按照时间降序
		query.addWhereContains("step", objectId);
		query.findObjects(this, new FindListener<Comment>() {
			@Override
			public void onSuccess(List<Comment> comments) {
				commentAdapter.clear();
				if (comments == null || comments.isEmpty()) {
					commentAdapter.notifyDataSetChanged();
					return;
				}
				commentAdapter.addAll(comments);
				listview.setAdapter(commentAdapter);
			}

			@Override
			public void onError(int arg0, String error) {
				Toast.makeText(GoodDetailActivity.this, error, 0).show();
			}
		});
	}

	/**
	 * 
	 * @Title: add
	 * @Description:发布评论
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void add() {
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		phone = edit_phone.getText().toString();

		if (TextUtils.isEmpty(title)) {
			Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(describe)) {
			Toast.makeText(this, "Please enter describe", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "Please enter phoneNum", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		final BmobUser user = BmobUser.getCurrentUser(GoodDetailActivity.this);
		Comment c = new Comment();
		c.setDescribe(describe);
		c.setPhone(phone);
		c.setTitle(title);
		c.setStep(objectId);
		c.setUser(user.getUsername());
		c.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(GoodDetailActivity.this, "添加评论成功",
						Toast.LENGTH_SHORT).show();
				mMaterialDialog.dismiss();
				queryComments();
			}

			@Override
			public void onFailure(int code, String error) {
				Toast.makeText(GoodDetailActivity.this, error,
						Toast.LENGTH_SHORT).show();
				mMaterialDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			View dialogView = LayoutInflater.from(this).inflate(
					R.layout.activity_add, null);
			mMaterialDialog = new AlertDialog.Builder(this).setView(dialogView)
					.show();
			tv_add = (TextView) dialogView.findViewById(R.id.tv_add);
			btn_back = (Button) dialogView.findViewById(R.id.btn_back);
			btn_true = (Button) dialogView.findViewById(R.id.btn_true);
			edit_phone = (EditText) dialogView.findViewById(R.id.edit_phone);
			edit_describe = (EditText) dialogView
					.findViewById(R.id.edit_describe);
			edit_title = (EditText) dialogView.findViewById(R.id.edit_title);
			btn_back.setOnClickListener(this);
			tv_add.setOnClickListener(this);
			btn_true.setOnClickListener(this);
			break;
		case R.id.btn_true:
			add();
			break;
		case R.id.btn_back:
			mMaterialDialog.dismiss();
			break;
		case R.id.back:
			startActivity(new Intent(GoodDetailActivity.this,
					MainActivity.class));

			GoodDetailActivity.this.finish();

			break;
		}
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
			Toast.makeText(getApplicationContext(), "Press again to exit!",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}
}
