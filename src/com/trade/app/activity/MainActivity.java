package com.trade.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.trade.app.adapter.BaseAdapterHelper;
import com.trade.app.adapter.QuickAdapter;
import com.trade.app.bean.Book;
import com.trade.app.multiphotopicker.view.PublishActivity;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnItemClickListener, OnRefreshListener {
	private static final int REFRESH_COMPLETE = 0X110;
	RelativeLayout relative;
	private ListView car_list;
	// c701e364a2db9f90d25388f3d9753a46 storys
	private QuickAdapter<Book> adapter_dress;
	private List<Book> sp = new ArrayList<Book>();
	private Button upload;
	private LinearLayout swipelayout;// 下拉和progressbar layout
	private SwipeRefreshLayout mSwipeLayout;// 下拉刷新组件
	private TextView tv;
	private RelativeLayout progress;
	ProgressBar pb;

	private long exitTime = 0;
	private Button exit;
	private Button createCardBtn;
	private RelativeLayout nodataLayout;

	private Button backBtn;

	private List<Book> list;
	private MediaPlayer mp;
	private boolean isPlaying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab01_car);
		initView();// 初始化
		initEvent();
		setAdapter();
		loadData();
	}

	private void initEvent() {
		mp = new MediaPlayer();
		isPlaying = false;
		car_list.setOnItemClickListener(this);
		upload.setOnClickListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		createCardBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	private void initView() {
		createCardBtn = (Button) findViewById(R.id.createCard);
		nodataLayout = (RelativeLayout) findViewById(R.id.nodata_real);
		car_list = (ListView) findViewById(R.id.list_car);
		upload = (Button) findViewById(R.id.btn_addUp);
		tv = (TextView) findViewById(R.id.tt);
		pb = (ProgressBar) findViewById(R.id.pb);
		swipelayout = (LinearLayout) findViewById(R.id.swipe_layou);
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		progress = (RelativeLayout) findViewById(R.id.progress);
		exit = (Button) findViewById(R.id.btn_exit);
		exit.setOnClickListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		backBtn = (Button) findViewById(R.id.back_id);

		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}

	@SuppressWarnings("unused")
	@Override
	public void onClick(View v) {
		if (v == upload) {
			Intent it = new Intent(this, PublishActivity.class);
			startActivity(it);
			finish();
		} else if (v == exit) {
			BmobUser.logOut(this); // 清除缓存用户对象
			BmobUser currentUser = BmobUser.getCurrentUser(this); // 现在的currentUser是null了
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		} else if (v == createCardBtn) {
			Intent it = new Intent(this, PublishActivity.class);
			startActivity(it);
			finish();
		} else if (v == backBtn) {
			finish();
		}
	}

	private void loadData() {
		final BmobUser user = BmobUser.getCurrentUser(this);
		BmobQuery<Book> query1 = new BmobQuery<Book>();
		query1.addWhereContains("leixin", "card");
        query1.addWhereContains("userId", user.getObjectId());
        query1.findObjects(this, new FindListener<Book>() {

			@Override
			public void onSuccess(List<Book> arg0) {

				adapter_dress.clear();
				if (null == arg0 || arg0.size() == 0) {
					progress.setVisibility(View.GONE);
					nodataLayout.setVisibility(View.VISIBLE);
					return;
				}
				loading();
			}

			@Override
			public void onError(int arg0, String errorInfo) {
				Toast.makeText(MainActivity.this, errorInfo, 0).show();
			}
		});
        
     
	}
	private void loading(){
		   
		
				BmobQuery<Book> query = new BmobQuery<Book>();
				query.addWhereContains("leixin", "card");
				// query.addWhereContains("userId", user.getObjectId());
				query.findObjects(this, new FindListener<Book>() {

					@Override
					public void onSuccess(List<Book> arg0) {

						adapter_dress.clear();
						if (null == arg0 || arg0.size() == 0) {
							progress.setVisibility(View.GONE);
							nodataLayout.setVisibility(View.VISIBLE);
							return;
						}
						// list = new ArrayList<Book>();
						// list.add(arg0.get(arg0.size() - 1));
						list=arg0;
						adapter_dress.addAll(arg0);
						sp.clear();
						for (int i = 0; i < arg0.size(); i++) {
							sp.add(arg0.get(i));
						}
						// for (int i = 0; i < list.size(); i++) {
						// sp.add(list.get(i));
						// }
						progress.setVisibility(View.GONE);
						if (!isPlaying) {
							playMusic();// 播放音乐
						}
					}

					@Override
					public void onError(int arg0, String errorInfo) {
						Toast.makeText(MainActivity.this, errorInfo, 0).show();
					}
				});
				car_list.setAdapter(adapter_dress);
	}

	private void playMusic() {
		if (null == list) {
			return;
		}
		String song = null;
		final BmobUser user = BmobUser.getCurrentUser(this);
		for (int i = 0; i < list.size(); i++) {
			Book book=list.get(i);
			if (user.getObjectId().equals(book.getUserId())) {
				song = book.getMusic();
			}
		}
		try {
			if ( null != song) {
			mp.setDataSource(song);
			mp.prepare();
			mp.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAdapter() {

		if (adapter_dress == null) {
			adapter_dress = new QuickAdapter<Book>(this, R.layout.goods_item) {
				@Override
				protected void convert(BaseAdapterHelper helper, Book item) {
					if (null != item.getPic()) {
						helper.setImageUrl(R.id.goods_image, item.getPic()
								.getFileUrl(MainActivity.this));
					}
					if (null != item.getName()) {
						helper.setText(R.id.goods_item_name, item.getName());
					}
					if (null != item.getPrice()) {
						helper.setText(R.id.goods_item_price, item.getPrice());
					}
					if (null != item.getBack()) {

						helper.setImageUrl(R.id.back_beijing, item.getBack()
								.getFileUrl(MainActivity.this));
					}
					if (null != item.getLove()) {
						helper.setText(R.id.goods_item_love, item.getLove());
					}
					Button btn = helper.getView(R.id.code_play);
					btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent it = new Intent(MainActivity.this,
									CodeActivity.class);
							startActivity(it);
							finish();
						}
					});

				}
			};
		}
	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int index, long l) {
		Intent it = new Intent(this, GoodDetailActivity.class);
		it.putExtra("sb", new String[] { "Book",
				sp.get(index).getObjectId().toString() });
		startActivity(it);
		finish();

	}

	// 更新进度条
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				pb.setVisibility(ProgressBar.GONE);
				swipelayout.setVisibility(LinearLayout.GONE);
				mSwipeLayout.setRefreshing(false);
				tv.setText("");
				Toast.makeText(MainActivity.this, "Refresh successfully!",
						Toast.LENGTH_SHORT).show();
				break;

			}
		};
	};

	@Override
	public void onRefresh() {
		swipelayout.setVisibility(LinearLayout.VISIBLE);
		tv.setText("Drop down....");
		pb.setVisibility(1);
		isPlaying = true;
		loadData();
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
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

	@Override
	protected void onResume() {
		super.onResume();
		// mp.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mp) {

			if (mp.isPlaying()) {
				mp.pause();
			} else {
				mp.release();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mp) {
			mp.release();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (null != mp) {
			mp.release();
		}
	}
}
