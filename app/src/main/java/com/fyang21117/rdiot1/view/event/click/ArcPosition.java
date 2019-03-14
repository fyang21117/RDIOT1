package com.fyang21117.rdiot1.view.event.click;

import android.graphics.PointF;

import com.fyang21117.rdiot1.view.common.MathHelper;

/*
 * @ClassName ArcPosition
 * @Description  arc位置记录信息基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class ArcPosition extends PositionRecord {
	
	protected float mOffsetAngle = 0.0f;
	protected float mCurrentAngle = 0.0f;	
	protected float mRadius = 0.0f;	
	protected float mSelectedOffset = 0.0f;
	
	//初始偏移角度
	protected float mInitAngle = 0.0f;//180;
	
	protected PointF mCirXY = null;
	

	public ArcPosition()
	{	
	}
	
	
	public float getAngle()
	{
		return MathHelper.getInstance().add(mOffsetAngle , mCurrentAngle);
		//return mOffsetAngle;
	}

	
	/*
	 * 饼图(pie chart)起始偏移角度
	 * @param Angle 偏移角度
	 */
	public void saveInitialAngle(float Angle)
	{
		mInitAngle = Angle;
	}
	
	
	public float getRadius()
	{
		return mRadius;		
	}
	
	public PointF getPointF()
	{
		return mCirXY;
	}
	
	public float getStartAngle()
	{
		return  MathHelper.getInstance().add(mOffsetAngle , this.mInitAngle);
	}
	
	public float getSweepAngle()
	{
		return mCurrentAngle;
	}
		
	public float getSelectedOffset()
	{
		return mSelectedOffset;
	}
	
	@Override
	protected boolean compareRange(float x, float y) {
		// TODO Auto-generated method stub
		if(null == mCirXY)return false;								
		return compareRadius( x,  y) ;
	}
		
	private boolean compareRadius(float x, float y) 
	{		
		double distance =  MathHelper.getInstance().getDistance(mCirXY.x, mCirXY.y, x,y);		
		if(Double.compare(distance, mRadius)  == 0 || Double.compare(distance, mRadius) == -1)
		{							
			float Angle1 = (float) MathHelper.getInstance().getDegree(mCirXY.x, mCirXY.y, x,y);
			float currAngle = getAngle();	
		
			//??? 如果有设初始角度(mInitAngle)，则初始角度范围内的那个扇区点击判断会有问题，原因还没查出来			
			if( Float.compare(currAngle, Angle1) == 1 
					|| Float.compare(currAngle, Angle1) == 0  )
			{						
				return true;
			}
		}		
		return false;
	}
	

	
}
