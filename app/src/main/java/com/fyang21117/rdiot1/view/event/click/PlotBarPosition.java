package com.fyang21117.rdiot1.view.event.click;

import android.graphics.RectF;

/*
 * @ClassName PlotBarPosition
 * @Description  bar位置记录信息类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */


public class PlotBarPosition extends BarPosition{
	
	public PlotBarPosition()
	{	
	}	
	
	//当前记录在数据源中行号
	public void savePlotDataID(int num)
	{
		saveDataID(num);
	}

	//当前记录所属数据集的行号
	public void savePlotDataChildID(int num)
	{
		saveDataChildID(num);
	}	
		
	
	public void savePlotRectF(float left,float top,float right,float bottom)
	{		
		saveRectF(left, top, right, bottom);
	}
	
	public void savePlotRectF(RectF r)
	{
		saveRectF(r);
	}
	
	public  boolean compareF(float x, float y) 
	{
		// TODO Auto-generated method stub
		
		return compareRange(x,y);	
	}			
	
	

}
