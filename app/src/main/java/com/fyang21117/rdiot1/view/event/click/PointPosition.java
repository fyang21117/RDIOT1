package com.fyang21117.rdiot1.view.event.click;

import android.graphics.PointF;


/*
 * @ClassName PointPosition
 * @Description  点位置记录信息基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class PointPosition extends RectPosition {
		
	protected PointF mPoint = null;		
	
	public PointPosition()
	{	
	}
	
	public PointF getPosition()
	{
		return mPoint;
	}
	
	
	public String getPointInfo()
	{	
		if(null == mPoint)return "";
		String info = "x:"+Float.toString(mPoint.x)+" y:"+Float.toString(mPoint.y);
		return info;
	}
	
}
