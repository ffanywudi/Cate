package com.trade.app.multiphotopicker.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trade.app.activity.R;
import com.trade.app.multiphotopicker.adapter.ImageBucketAdapter;
import com.trade.app.multiphotopicker.model.ImageBucket;
import com.trade.app.multiphotopicker.util.CustomConstants;
import com.trade.app.multiphotopicker.util.ImageFetcher;
import com.trade.app.multiphotopicker.util.IntentConstants;

/**
 * 选择相册
 * 
 */

public class ImageBucketChooseActivity extends Activity
{
	private ImageFetcher mHelper;
	private List<ImageBucket> mDataList = new ArrayList<ImageBucket>();
	private ListView mListView;
	private ImageBucketAdapter mAdapter;
	private int availableSize;
	private long exitTime = 0;
	private String beijingString;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_image_bucket_choose);
		mHelper = ImageFetcher.getInstance(getApplicationContext());
		initData();
		initView();
	}

	private void initData()
	{
		beijingString=this.getIntent().getStringExtra("beijing");
		mDataList = mHelper.getImagesBucketList(false);
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);
	}

	private void initView()
	{
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);
		TextView titleTv  = (TextView) findViewById(R.id.title);
		titleTv.setText("相册");
		mListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				selectOne(position);

				Intent intent = new Intent(ImageBucketChooseActivity.this,
						ImageChooseActivity.class);
				intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
						(Serializable) mDataList.get(position).imageList);
				intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME,
						mDataList.get(position).bucketName);
				intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
						availableSize);
                
				if (! TextUtils.isEmpty(beijingString)) {
					intent.putExtra("beijing", "beijing");
				}
				startActivity(intent);
			}
		});

		TextView cancelTv = (TextView) findViewById(R.id.action);
		cancelTv.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(ImageBucketChooseActivity.this,
						PublishActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void selectOne(int position)
	{
		int size = mDataList.size(); 
		for (int i = 0; i != size; i++)
		{
			if (i == position) mDataList.get(i).selected = true;
			else
			{
				mDataList.get(i).selected = false;
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit()
	{
		if ((System.currentTimeMillis() - exitTime) > 2000)
		{
			Toast.makeText(getApplicationContext(), "Press again to exit!",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else
		{
			finish();
			System.exit(0);
		}
	}
}
