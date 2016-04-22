package com.trade.app.multiphotopicker.view;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.datatype.BmobFile;

import com.trade.app.activity.MainActivity;
import com.trade.app.activity.R;
import com.trade.app.activity.WeCardActivity;
import com.trade.app.bean.Book;
import com.trade.app.multiphotopicker.adapter.ImageGridAdapter;
import com.trade.app.multiphotopicker.model.ImageItem;
import com.trade.app.multiphotopicker.util.CustomConstants;
import com.trade.app.multiphotopicker.util.IntentConstants;
import com.trade.app.multiphotopicker.util.UploadFile;

/**
 * 
 * @ClassName: ImageChooseActivity
 * @Description: 图片选择
 * @author
 * @date 2015-12-5 上午12:50:12
 * 
 */
public class ImageChooseActivity extends Activity {
	/**
	 * 图片信息的list
	 */
	private List<ImageItem> mDataList = new ArrayList<ImageItem>();
	/**
	 * 标题
	 */
	private String mBucketName;
	/**
	 * 允许的数量
	 */
	private int availableSize;
	/**
	 * 显示图片的GridView
	 */
	private GridView mGridView;
	private TextView mBucketNameTv;
	private TextView cancelTv;
	private ImageGridAdapter mAdapter;
	private Button mFinishBtn;
	/**
	 * 被选择图片的HashMap
	 */
	private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();

	private long exitTime = 0;

	private String beijingString;

	private String chooseId;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_image_choose);

		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
				IntentConstants.EXTRA_IMAGE_LIST);
		if (mDataList == null)
			mDataList = new ArrayList<ImageItem>();
		mBucketName = getIntent().getStringExtra(
				IntentConstants.EXTRA_BUCKET_NAME);

		if (TextUtils.isEmpty(mBucketName)) {
			mBucketName = "please choose";
		}
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);

		initView();
		initListener();

	}

	private void initView() {
		beijingString = this.getIntent().getStringExtra("beijing");
		mBucketNameTv = (TextView) findViewById(R.id.vtitle);
		mBucketNameTv.setText(mBucketName);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
		mGridView.setAdapter(mAdapter);
		mFinishBtn = (Button) findViewById(R.id.finish_btn);
		cancelTv = (TextView) findViewById(R.id.vaction);
		mFinishBtn.setText("down");
		mAdapter.notifyDataSetChanged();

	}

	private void initListener() {
		mFinishBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(
						IntentConstants.EXTRA_IMAGE_LIST,
						(Serializable) new ArrayList<ImageItem>(selectedImgs
								.values()));
				intent.putExtra("sign", "come");
				if (!TextUtils.isEmpty(beijingString)) {
					intent.putExtra("beijing", "beijing");
					Book book = new Book();
					String picPath = selectedImgs.get(chooseId).sourcePath;
					final BmobFile file = new BmobFile(new File(picPath));
					book.setBack(file);
					UploadFile.upload(ImageChooseActivity.this, book, file);// 上传图片到服务器
				} else {
					intent.setClass(ImageChooseActivity.this, 
							PublishActivity.class);
					startActivity(intent);
					ImageChooseActivity.this.finish();
				}
			}

		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ImageItem item = mDataList.get(position);
				if (item.isSelected) {
					item.isSelected = false;
					selectedImgs.remove(item.imageId);
				} else {
					if (selectedImgs.size() >= availableSize) {
						// Toast.makeText(ImageChooseActivity.this,
						// "最多选择" + availableSize + "张图片",
						// Toast.LENGTH_SHORT).show();
						// return;
					}
					item.isSelected = true;
					selectedImgs.put(item.imageId, item);
					chooseId = item.imageId;
				}

				mFinishBtn.setText("down");
				mAdapter.notifyDataSetChanged();
			}

		});

		cancelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageChooseActivity.this.finish();
			}
		});
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