package com.fyang21117.rdiot1.view.event.click;

import android.graphics.RectF;

/*
 * @ClassName RectPosition
 * @Description  rect类型的位置记录信息基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class RectPosition extends PositionRecord{
	
	protected RectF mRectF = null;
	protected RectF mRectFRange = null;
	
	//放大值
	protected int mExtValue = 0;
	
	public RectPosition()
	{	
	}	
	
	public void extPointClickRange(int value)
	{
		mExtValue = value;
	}	
	
	
	protected void saveRectF(float left,float top,float right,float bottom)
	{
		if(null == mRectF)mRectF = new RectF();			
		mRectF.set(left, top, right, bottom);
	}
	
	protected void saveRectF(RectF r)
	{
		mRectF = r;
	}
	
	public float getRadius()
	{
		float radius = 0.0f;		
		radius = (mRectF.bottom -  mExtValue) - (mRectF.top +  mExtValue) ;
		return radius; //MathHelper.getInstance().div(radius, 2.f);		
	}
			
	public String getRectInfo()
	{	
		if(null == mRectF)return "";
		
		float left = mRectF.left +  mExtValue;
		float top = mRectF.top +  mExtValue;
		float right = mRectF.right -  mExtValue;
		float bottom = mRectF.bottom -  mExtValue;
		
		String info =" left:"+Float.toString(left)+" top:"+Float.toString(top) +
				 	 " right:"+Float.toString(right)+" bottom:"+Float.toString(bottom);
		return info;
	}
	
	public RectF getRectF()
	{
		return mRectF;
	}

	@Override
	protected boolean compareRange(float x, float y) {
		// TODO Auto-generated method stub
		
		if(null == mRectF)return false;		
		if(null == mRectFRange)  mRectFRange = new RectF();
	
		mRectFRange.setEmpty();
		mRectFRange.set(mRectF);
		
		mRectFRange.left -=  mExtValue;
		mRectFRange.top -=  mExtValue;
		mRectFRange.right +=  mExtValue;
		mRectFRange.bottom +=  mExtValue;			
			
		//contains 在范围比较小时很不好使.
		if( mRectFRange.contains(x, y)) return true;
		
		//再加层手工检查
		if( (Float.compare(x, mRectFRange.left) == 1 || Float.compare(x, mRectFRange.left) == 0 )
				&& (Float.compare(x, mRectFRange.right) == -1 || Float.compare(x, mRectFRange.right) == 0 ) 
			&& (Float.compare(y, mRectFRange.bottom) == 1 || Float.compare(y, mRectFRange.bottom) == 0 )
			&& (Float.compare(y, mRectFRange.top) == -1 || Float.compare(y, mRectFRange.top) == 0 ) ) 
			{
				return true;
			}

		return false;
	}	
	

}
