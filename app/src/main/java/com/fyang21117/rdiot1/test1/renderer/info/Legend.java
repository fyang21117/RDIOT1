package com.fyang21117.rdiot1.test1.renderer.info;

import android.graphics.Paint;
import com.fyang21117.rdiot1.test1.renderer.line.PlotDot;


/*
 * @ClassName Legend
 * @Description 动态图例 基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class Legend extends DyInfo{
	
	protected float mXPercentage = 0.0f;
	protected float mYPercentage = 0.0f;
	
	
	public Legend()
	{

	}
	
	/*
	 * 设置显示位置
	 * @param xPercentage	占绘图区的竖向百分比位置
	 * @param yPercentage	占绘图区的横向百分比位置
	 */
	public void setPosition(float xPercentage,float yPercentage)
	{
		mXPercentage = xPercentage;
		mYPercentage = yPercentage;
	}
	
	/*
	 * 增加动态图例
	 * @param text	文本
	 * @param paint	画笔
	 */
	public void addLegend(String text,Paint paint)
	{
		addInfo(text,paint);
	}
	
	/*
	 * 增加动态图例
	 * @param dotStyle		图案风格
	 * @param text			文本
	 * @param paint			画笔
	 */
	public void addLegend(PlotDot dotStyle,String text,Paint paint)
	{
		addInfo(dotStyle,text,paint);
	}
	

}
