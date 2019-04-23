package com.fyang21117.rdiot1.test1.event.click;


/*
 * @ClassName PositionRecord
 * @Description  位置记录信息基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public abstract class PositionRecord {
	
	//private static final String TAG = "PositionRecord";
		
	protected int mDataID = -1;
	protected int mDataChildID = -1;
		
	public PositionRecord()
	{
		
	}
		
	
	//确认是否在范围内
	protected abstract boolean compareRange(float x, float y) ;
	

	//当前记录在数据源中行号
	public int getDataID()
	{
		return mDataID;
	}

	//当前记录所属数据集的行号
	public int getDataChildID()
	{
		return mDataChildID;
	}	
	
	public int getRecordID()
	{			
		if(-1 == mDataID && -1 == mDataChildID ) return -1;
		
		int id = 0;	
		if( mDataID > 0)  id += mDataChildID;
		if( mDataChildID > 0) id +=  mDataChildID;
	
		return id;
	}
		
	
	//当前记录在数据源中行号
	protected void saveDataID(int num)
	{
		mDataID = num;
	}

	//当前记录所属数据集的行号
	protected void saveDataChildID(int num)
	{
		mDataChildID = num;
	}	
		
	
	/*
	//分类轴的行号
	public int getCategoryID()
	{
		return mCategoryID;
	}
	*/

	
	
}
