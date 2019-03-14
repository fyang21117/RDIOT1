package com.fyang21117.rdiot1.view.renderer.plot;

import android.graphics.Color;
import android.graphics.Paint;

import com.fyang21117.rdiot1.view.renderer.XEnum;

/*
 * @ClassName PlotTitle
 * @Description 标题类,定制其属性
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * 
 */

public class PlotTitle {	

	//图表标题文字
	private  String mChartTitle = "";
	private  String mSubtitle = "";	
	//图表标题画笔
	private Paint mPaintTitle = null;
	private Paint mPaintSubtitle = null;	
	//图表标题显示位置
	private XEnum.HorizontalAlign mChartTitleAlign = XEnum.HorizontalAlign.CENTER;	
	//标题的显示位置(TOP,CENTER,BOTTOM)即是否靠最上面，
	//还是Chart top与Plot top的中间位置，还是PLOT TOP的位置
	private XEnum.VerticalAlign mTitlePosition = XEnum.VerticalAlign.MIDDLE;
	
	public PlotTitle()
	{
	}

	/*
	 * 设置标题
	 * @param title 标题内容
	 */
	public void setTitle(String title)
	{
		mChartTitle = title;		
	}
	
	/*
	 * 返回标题
	 * @return 标题
	 */
	public String getTitle()
	{
		return mChartTitle;
	}
	
	
	/*
	 * 设置子标题
	 * @param subtitle 子标题
	 */
	public void setSubtitle(String subtitle)
	{
		mSubtitle = subtitle;					
	}
	
	/*
	 * 返回子标题
	 * @return 子标题
	 */
	public String getSubtitle()
	{
		return mSubtitle;
	}
	 
	
	/*
	 * 开放标题画笔
	 * @return 画笔
	 */
	public Paint getTitlePaint()
	 {
		
		if(null == mPaintTitle)
		{
			mPaintTitle  = new Paint();				
			mPaintTitle.setTextSize(32);				
			mPaintTitle.setColor(Color.BLACK);				
			mPaintTitle.setAntiAlias(true);
		}
		
		 return mPaintTitle ;
	 }

	
	/*
	 * 开放子标题画笔
	 * @return 画笔
	 */
	public Paint getSubtitlePaint()
	 {
		if(null == mPaintSubtitle)
		{
			mPaintSubtitle  = new Paint();
			mPaintSubtitle.setTextSize(22);
			mPaintSubtitle.setColor(Color.BLACK);	
			mPaintSubtitle.setAntiAlias(true);	
		}
		 return mPaintSubtitle ;
	 }
			
	/*
	 * 设置标题横向显示位置(靠左，居中，靠右)
	 * @param align 横向显示位置
	 */
	public void setTitleAlign(XEnum.HorizontalAlign align)
	{
		mChartTitleAlign = align;
	}
		
	/*
	 * 返回标题横向显示位置
	 * @return 横向显示位置
	 */
	public XEnum.HorizontalAlign getTitleAlign()
	{
		return mChartTitleAlign;
	}
	
	/*
	 * 设置标题上下显示位置,即图上边距与绘图区间哪个位置(靠上，居中，靠下).
	 * @param Position  上下显示位置
	 */
	public void setVerticalAlign(XEnum.VerticalAlign Position)
	{
		mTitlePosition = Position;
	}
		
	/*
	 * 设置标题上下显示位置
	 * @return 上下显示位置
	 */
	public XEnum.VerticalAlign getVerticalAlign()
	{
		return mTitlePosition;
	}
	
	
}
