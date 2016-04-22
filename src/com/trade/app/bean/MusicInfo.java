	package com.trade.app.bean;

import android.graphics.Bitmap;
import cn.bmob.v3.BmobObject;
/**
 * 
* @ClassName: MusicInfo 
* @Description: 音乐数据原型 
* @author chewb 
* @date 2016-03-24 上午12:33:37 
*
 */
public class MusicInfo extends BmobObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;// 歌名
	private String artist;// 艺术家
	private String album;// 专辑
	private String path;// 歌曲路径
	private Bitmap bitmap;
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
