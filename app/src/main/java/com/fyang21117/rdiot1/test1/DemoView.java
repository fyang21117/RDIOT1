
package com.fyang21117.rdiot1.test1;

import android.content.Context;
import android.util.AttributeSet;

import com.fyang21117.rdiot1.test1.common.DensityUtil;

/*
 * @ClassName DemoView
 * @Description  各个例子view的view基类
 */
public class DemoView extends ChartView{

	public DemoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	 public DemoView(Context context, AttributeSet attrs){   
	        super(context, attrs);   
	        
	 }
	 
	 public DemoView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		
	 }
	
	//Demo中bar chart所使用的默认偏移值。
	//偏移出来的空间用于显示tick,axistitle....
	protected int[] getBarLnDefaultSpadding()
	{
		int [] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 30); //left
		ltrb[1] = DensityUtil.dip2px(getContext(), 50); //top
		ltrb[2] = DensityUtil.dip2px(getContext(), 10); //right
		ltrb[3] = DensityUtil.dip2px(getContext(), 30); //bottom
		return ltrb;
	}
	
	protected int[] getPieDefaultSpadding()
	{
		int [] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 20); //left	
		ltrb[1] = DensityUtil.dip2px(getContext(), 65); //top	
		ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right		
		ltrb[3] = DensityUtil.dip2px(getContext(), 20); //bottom						
		return ltrb;
	}
		
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
    
    }  

}
