package com.trade.app.multiphotopicker.util;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.trade.app.activity.R;
import com.trade.app.activity.WeCardActivity;
import com.trade.app.bean.Book;

/**
 * 
 * @ClassName: UploadFile
 * @Description: 上传文件到服务器
 * @author
 * @date 2015-12-5 上午12:54:19
 * 
 */
public class UploadFile {

	static AlertDialog builder = null;

	public static void upload(final Context context, final Book book,
			final BmobFile file) {
		// final String
		// path=Environment.getExternalStorageDirectory().getPath();
		// Intent it=new Intent(Uri.parse(Uri.));
		// final BmobFile file = new BmobFile(new File(filepath));

		LayoutInflater inflater = LayoutInflater.from(context);
		final View textEntryView = inflater.inflate(R.layout.include_progress,
				null);
		builder = new AlertDialog.Builder(context).setView(textEntryView)
				.create();
		builder.show();
		if (null == file) {
			updateOrAdd(context, book);
			return;
		}
		/**
		 * 如果有就修改，没有就新增
		 */
		file.upload(context, new UploadFileListener() {

			@Override
			public void onSuccess() {
				updateOrAdd(context, book);
			}

			@Override
			public void onFailure(int arg0, String error) {
				Toast.makeText(context, error, 0).show();

			}
		});
	}

	public static void updateOrAdd(final Context context, final Book book) {
		final BmobUser user = BmobUser.getCurrentUser(context);
		BmobQuery<Book> query = new BmobQuery<Book>();
		query.addWhereContains("leixin", "card");
		query.addWhereContains("userId", user.getObjectId());
		query.findObjects(context, new FindListener<Book>() {

			@Override
			public void onSuccess(List<Book> bookList) {

				if (null == bookList || bookList.size() == 0) {
					book.setUserId(user.getObjectId());
					book.save(context, new SaveListener() {
						@Override
						public void onSuccess() {
							Toast.makeText(context, "成功!", 0).show();
							Intent it = new Intent(context,
									WeCardActivity.class);
							builder.dismiss();
							context.startActivity(it);
							((Activity) context).finish();
						}

						@Override
						public void onFailure(int arg0, String error) {
							Toast.makeText(context, error, 0).show();
							builder.dismiss();

						}
					});
					return;
				}

				book.update(context, bookList.get(bookList.size() - 1)
						.getObjectId(), new UpdateListener() {

					@Override
					public void onSuccess() {
						builder.dismiss();
						Toast.makeText(context, "成功 !", 0).show();
						Intent it = new Intent(context, WeCardActivity.class);
						context.startActivity(it);
						((Activity) context).finish();
					}

					@Override
					public void onFailure(int arg0, String error) {
						Toast.makeText(context, error, 0).show();
						builder.dismiss();
					}
				});
			}

			@Override
			public void onError(int arg0, String error) {
				Toast.makeText(context, error, 0).show();
				builder.dismiss();
			}
		});
	}
}
