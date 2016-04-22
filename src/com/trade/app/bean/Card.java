	package com.trade.app.bean;

import cn.bmob.v3.BmobObject;
/**
 * 
* @ClassName: Comment 
* @Description: 评论数据原型 
* @author A18ccms a18ccms_gmail_com 
* @date 2015-12-5 上午12:33:37 
*
 */
public class Card extends BmobObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;// 标题
	private String describe;// 描述
	private String phone;// 联系手机
	private String step;// 评论的楼层

	public String getStep()
	{
		return step;
	}

	public void setStep(String step)
	{
		this.step = step;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescribe()
	{
		return describe;
	}

	public void setDescribe(String describe)
	{
		this.describe = describe;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}
}
