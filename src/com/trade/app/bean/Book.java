package com.trade.app.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 
 * @ClassName: Book
 * @Description: 商品数据原型
 * @author A18ccms a18ccms_gmail_com
 * @date 2015-12-5 上午12:32:16
 * 
 */
public class Book extends BmobObject {

	private static final long serialVersionUID = 1L;
	private BmobFile pic;
	private String price;
	private String name;
	private String size;
	private String Color;
	private String leixin;
	private BmobFile back;
	private String userId;
	private String love;
	private String music;
	private String wechart;
	
	public String getWechart() {
		return wechart;
	}

	public void setWechart(String wechart) {
		this.wechart = wechart;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public String getLove() {
		return love;
	}

	public void setLove(String love) {
		this.love = love;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BmobFile getBack() {
		return back;
	}

	public void setBack(BmobFile back) {
		this.back = back;
	}

	public String getLeixin() {
		return leixin;
	}

	public void setLeixin(String leixin) {
		this.leixin = leixin;
	}

	public BmobFile getPic() {
		return pic;
	}

	public void setPic(BmobFile pic) {
		this.pic = pic;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		this.Color = color;
	}

}
