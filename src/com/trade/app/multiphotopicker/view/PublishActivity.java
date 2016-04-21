package com.trade.app.multiphotopicker.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.alibaba.fastjson.JSON;
import com.trade.app.activity.MainActivity;
import com.trade.app.activity.R;
import com.trade.app.bean.Book;
import com.trade.app.multiphotopicker.adapter.ImagePublishAdapter;
import com.trade.app.multiphotopicker.model.ImageItem;
import com.trade.app.multiphotopicker.util.CustomConstants;
import com.trade.app.multiphotopicker.util.IntentConstants;
import com.trade.app.multiphotopicker.util.UploadFile;

public class PublishActivity extends Activity {
	/**
	 * 显示图片的 GridView
	 */
	private GridView mGridView;
	/**
	 * 显示图片的 Adapter
	 */
	private ImagePublishAdapter mAdapter;
	/**
	 * 发送按钮
	 */
	private TextView sendTv;
	/**
	 * 获取图片的list
	 */
	public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
	/**
	 * 名字
	 */
	private EditText nameEdit;
	/**
	 * 价格
	 */
	private EditText priceEdit;

	/**
	 * 爱好
	 */
	private EditText loveEdit;
	
	private EditText wechartEdit;

	private String name;
	private String price;
	private String love;
	private String wechart;

	private long exitTime = 0;
	private ImageView back;

	private String bjString;

	private LinearLayout editCard;

	private TextView beijing_tips;

	private String first;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.act_publish);
		initData();
		initView();
	}

	protected void onPause() {
		super.onPause();
		saveTempToPref();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveTempToPref();
	}

	/**
	 * 
	 * @Title: saveTempToPref
	 * @Description: 保存文件到内存
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void saveTempToPref() {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = JSON.toJSONString(mDataList);
		sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();

	}

	/**
	 * 
	 * @Title: getTempFromPref
	 * @Description: 获取图片信息文件
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getTempFromPref() {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
		if (!TextUtils.isEmpty(prefStr)) {
			List<ImageItem> tempImages = JSON.parseArray(prefStr,
					ImageItem.class);
			mDataList = tempImages;
		}
	}

	/**
	 * 
	 * @Title: removeTempFromPref
	 * @Description: 删除临时文件
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void removeTempFromPref() {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		first = this.getIntent().getStringExtra("first");

		getTempFromPref();
		List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
				.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
		if (incomingDataList != null) { // 只拿最后一张
			if (incomingDataList.size() > 0) {
				List<ImageItem> list = new ArrayList<ImageItem>();
				list.add(incomingDataList.get(incomingDataList.size() - 1));
				mDataList = new ArrayList<ImageItem>();
				mDataList.addAll(list);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}

	public void initView() {
		/**
		 * 获取当前登陆用户
		 */
		final BmobUser user = BmobUser.getCurrentUser(this);
		Intent it = this.getIntent();
		bjString = it.getStringExtra("beijing");
		editCard = (LinearLayout) findViewById(R.id.act_publish_edits);
		beijing_tips = (TextView) findViewById(R.id.beijing_tips);
		if (!TextUtils.isEmpty(bjString)) {
			editCard.setVisibility(View.GONE);
		} else {
			beijing_tips.setText(" ↑ 点 击 设 置 头 像 ！");
		}

		TextView titleTv = (TextView) findViewById(R.id.title);
		titleTv.setText("Publish");
		loveEdit = (EditText) findViewById(R.id.text_love);
		back = (ImageView) findViewById(R.id.back);
		nameEdit = (EditText) findViewById(R.id.text_name);
		priceEdit = (EditText) findViewById(R.id.text_price);
		wechartEdit=(EditText) findViewById(R.id.text_wechart);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImagePublishAdapter(this, mDataList);
		mGridView.setAdapter(mAdapter);
        
		sendTv = (TextView) findViewById(R.id.action);
		sendTv.setText("Upload");
		
		if (!TextUtils.isEmpty(first)) { 
			mDataList=null;
			mAdapter.notifyDataSetChanged();
		}

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new PopupWindows(PublishActivity.this, mGridView);
			}
		});
		sendTv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				removeTempFromPref();
				if (null == mDataList || mDataList.size() == 0) {
					Toast.makeText(PublishActivity.this, "请选择一张图片", 0).show();
					return;
				}
				String picPath = mDataList.get(0).sourcePath;
				// TODO 这边以mDataList为来源做上传的动作
				if (add()) {
					final BmobFile file = new BmobFile(new File(picPath));
					Book book = new Book();
					if (TextUtils.isEmpty(bjString)) {
						book.setPrice(price);
						book.setName(name);
						book.setLove(love);
						book.setPic(file);
						book.setWechart(wechart);
					}else{
						book.setBack(file);
					}
					book.setLeixin("card");
					book.setUserId(user.getObjectId());
					UploadFile.upload(PublishActivity.this, book, file);// 上传图片到服务器
				}

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 
	 * @Title: add
	 * @Description:判断输入字段是否为空
	 * @param @return
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean add() {
		name = nameEdit.getText().toString();
		price = priceEdit.getText().toString();
		love = loveEdit.getText().toString();
		wechart=wechartEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "请输入名字!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (TextUtils.isEmpty(price)) {
			Toast.makeText(this, "请输入签名!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (TextUtils.isEmpty(love)) {
			Toast.makeText(this, "请输入爱好!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (TextUtils.isEmpty(wechart)) {
			Toast.makeText(this, "请输入微信号!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		return true;
	}

	private int getDataSize() {
		return mDataList == null ? 0 : mDataList.size();
	}

	private int getAvailableSize() {
		
		if (mDataList ==null || mDataList.size()==0) {
			return 0;
		}
		int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
		if (availSize >= 0) {
			return availSize;
		}
		return 0;
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	/**
	 * 
	 * @ClassName: PopupWindows
	 * @Description: 选择图片类型的PopupWindow
	 * @author
	 * @date 2015-12-5 上午12:48:11
	 * 
	 */
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.item_popupwindow, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishActivity.this,
							ImageBucketChooseActivity.class);
					intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,getAvailableSize());
					if ( !TextUtils.isEmpty(bjString)) {
						intent.putExtra("beijing", "beijing"); 
					}
					startActivity(intent);
					dismiss();
					PublishActivity.this.finish();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File vFile = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		path = vFile.getPath();
		Uri cameraUri = Uri.fromFile(vFile);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	/**
	 * 
	 * @Title: add
	 * @Description:获取相机的图片
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == -1 && !TextUtils.isEmpty(path)) {
				// 只拿最后一张
				ImageItem item = new ImageItem();
				item.sourcePath = path;
				mDataList.clear();
				mDataList.add(item);
			}
			break;
		}
	}

	private void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
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