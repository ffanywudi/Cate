package com.trade.app.activity;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.trade.app.adapter.BaseAdapterHelper;
import com.trade.app.adapter.QuickAdapter;
import com.trade.app.bean.Book;
import com.trade.app.bean.MusicInfo;
import com.trade.app.multiphotopicker.util.UploadFile;

/**
 * 闪屏页
 * 
 */
public class MusicChooseActivity extends BaseActivity {

	private ArrayList<MusicInfo> mylist;

	private QuickAdapter<MusicInfo> adapter_dress;

	public ListView music_list;

	private Button back;

	// 获取专辑封面的Uri
	public static final Uri albumArtUri = Uri
			.parse("content://media/external/audio/albumart");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.music_choose);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initData() {

		 Cursor cursor = getContentResolver().query(
		  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
		 null);
		 // MediaStore.Audio.Media.DEFAULT_SORT_ORDER
		// "_display_name like '%.mp3'"
		// + sql
		// Cursor cursor = this.getContentResolver()
		// .query( Media.EXTERNAL_CONTENT_URI, null,
		// "_display_name like '%.mp3'"+sql, null, 
		// MediaStore.Audio.Media.DATE_ADDED + " desc limit " + 100 + " offset "
		// + 100 * 1);
//		String where = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
//		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
//				null, null, null, Media.DATA);
		// 遍历媒体数据库
		mylist = new ArrayList<MusicInfo>();

		if (cursor.moveToFirst()) {

			while (!cursor.isAfterLast()) {
				// 歌曲编号
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲标题
				String tilte = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

				int albumId = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

				if (size > 1024 * 800) {// 大于800K
					// HashMap<String, Object> map = new HashMap<String,
					// Object>();
					// map.put("musicId", id);
					// map.put("musicTitle", tilte);
					// map.put("musicFileUrl", url);
					// map.put("music_file_name", tilte);
					MusicInfo info = new MusicInfo();
					info.setName(tilte);
					info.setArtist(artist);
					info.setAlbum(album);
					info.setPath(url);
					//info.setBitmap(getbimap(url, false));
					 Bitmap bm = getArtwork(MusicChooseActivity.this, id,
					 albumId, true);
					 info.setBitmap(bm);
					mylist.add(info);
				}
				cursor.moveToNext();
			}
		}

		if (mylist != null && mylist.size() > 0) {
			adapter_dress.addAll(mylist);
		}
	}

	@Override
	public void initView() {
		back = (Button) findViewById(R.id.back_id);
		music_list = (ListView) findViewById(R.id.music_list);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (adapter_dress == null) {
			adapter_dress = new QuickAdapter<MusicInfo>(this,
					R.layout.item_song_option) {
				@Override
				protected void convert(BaseAdapterHelper helper, MusicInfo info) {

					if (null != info.getName()) {
						helper.setText(R.id.vtitle, info.getName());
					}
					if (null != info.getArtist()) {

						helper.setText(R.id.vartist, info.getArtist());
					}
					if (null != info.getAlbum()) {
						helper.setText(R.id.vablum, info.getAlbum());
					}
					ImageView image = helper.getView(R.id.music_image);
					if (null != info.getBitmap()) {
						image.setImageBitmap(info.getBitmap());
					}
				}
			};
		}
		music_list.setAdapter(adapter_dress);

		music_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MusicInfo info = mylist.get(position);
				Book book = new Book();
				book.setMusic(info.getPath());
				UploadFile.upload(MusicChooseActivity.this, book, null);

			}
		});
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	@SuppressLint("NewApi")
	public static Bitmap getbimap(String path, boolean bit) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		byte[] data = mmr.getEmbeddedPicture();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = null;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize = 8;
		if (data != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
			return bitmap;
		} else {
			return null;
		}
	}

	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, boolean allowdefault) {
		if (album_id < 0) {
			// This is something that is not in the database, so get the album
			// art directly
			// from the file.
			if (song_id >= 0) {
				Bitmap bm = getArtworkFromFile(context, song_id, -1);
				if (bm != null) {
					return bm;
				}
			}
			if (allowdefault) {
				return getDefaultArtwork(context);
			}
			return null;
		}
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			InputStream in = null;
			try {
				in = res.openInputStream(uri);
				return BitmapFactory.decodeStream(in, null, sBitmapOptions);
			} catch (FileNotFoundException ex) {
				// The album art thumbnail does not actually exist. Maybe the
				// user deleted it, or
				// maybe it never existed to begin with.
				Bitmap bm = getArtworkFromFile(context, song_id, album_id);
				if (bm != null) {
					if (bm.getConfig() == null) {
						bm = bm.copy(Bitmap.Config.RGB_565, false);
						if (bm == null && allowdefault) {
							return getDefaultArtwork(context);
						}
					}
				} else if (allowdefault) {
					bm = getDefaultArtwork(context);
				}
				return bm;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
				}
			}
		}

		return null;
	}

	private static Bitmap getArtworkFromFile(Context context, long songid,
			long albumid) {
		Bitmap bm = null;
		byte[] art = null;
		String path = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			}
		} catch (FileNotFoundException ex) {

		}
		if (bm != null) {
			mCachedBit = bm;
		}
		return bm;
	}

	private static Bitmap getDefaultArtwork(Context context) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.music_default), null, opts);
	}

	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static Bitmap mCachedBit = null;

}
