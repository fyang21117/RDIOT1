package com.fyang21117.rdiot1.view.chart;

import android.graphics.Canvas;
import android.util.Log;

import com.fyang21117.rdiot1.view.common.DrawHelper;
import com.fyang21117.rdiot1.view.renderer.LnChart;
import com.fyang21117.rdiot1.view.renderer.XEnum;
import com.fyang21117.rdiot1.view.renderer.line.PlotDot;
import com.fyang21117.rdiot1.view.renderer.line.PlotDotRender;
import com.fyang21117.rdiot1.view.renderer.line.PlotLine;

import java.util.ArrayList;
import java.util.List;

/*
 * @ClassName LineChart
 * @Description  线图基类
 *  
 */
public class LineChart extends LnChart{
	
	private  static final  String TAG ="LineChart";
	
	//数据源
	protected List<LineData> mDataSet;
	//当线与轴交叉时是否不断开连接
	private boolean mLineAxisIntersectVisible = true;
	//图例
	private List<LnData> mLstKey = new ArrayList<>();
								
	public LineChart()
	{
		
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.LINE;
	}
			 
		/*
		 * 分类轴的数据源
		 * @param categories 标签集
		 */
		public void setCategories( List<String> categories) {
			if(null != categoryAxis)categoryAxis.setDataBuilding(categories);
		}
		
		/*
		 *  设置数据轴的数据源
		 * @param dataSet 数据源
		 */
		public void setDataSource( List<LineData> dataSet)
		{										
			this.mDataSet = dataSet;		
		}			
		
		/*
		 * 返回数据源
		 * @return 数据源
		 */
		public List<LineData> getDataSource()
		{
			return this.mDataSet;
		}
						
						
		/*
		 *  设置当值与底轴的最小值相等时，线是否与轴连结显示. 默认为连接(true)
		 * @param visible 是否连接
		 */
		public void setLineAxisIntersectVisible( boolean visible)
		{
			mLineAxisIntersectVisible = visible;
		}
		
		/*
		 * 返回当值与底轴的最小值相等时，线是否与轴连结的当前状态
		 * @return 状态
		 */
		public boolean getLineAxisIntersectVisible()
		{
			return mLineAxisIntersectVisible;
		}	
		
		/*
		 * 设置柱形居中位置,依刻度线居中或依刻度中间点居中。
		 * @param style 居中风格
		 */
		public void setBarCenterStyle(XEnum.BarCenterStyle style)
		{
			mBarCenterStyle = style;
		}
		
		/*
		 * 返回柱形居中位置,依刻度线居中或依刻度中间点居中。
		 * @return 居中风格
		 */
		public XEnum.BarCenterStyle getBarCenterStyle()
		{
			return mBarCenterStyle;
		}
		
		/*
		 * 设置线图的X坐标开始计算位置，默认false,即从轴上开始,true则表示从第一个刻度线位置开始
		 * @param status 状态
		 */
		public void setXCoordFirstTickmarksBegin(boolean status)
		{
			mXCoordFirstTickmarksBegin = status;
		}

				
		/*
		 * 绘制线
		 * @param canvas	画布
		 * @param bd		数据类
		 * @param type		处理类型
		 */
		private boolean renderLine(Canvas canvas, LineData bd,String type,int dataID)
		{
			if(null == categoryAxis || null == dataAxis) return false;
			
			if(null == bd)
			{
				Log.i(TAG,"传入的线的数据序列参数为空.");
				return false;
			}
			
			float initX =  plotArea.getLeft();
            float initY =  plotArea.getBottom();             
			float lineStartX = initX,lineStartY = initY;         
            float lineStopX,lineStopY;
            													
			//得到分类轴数据集
			List<String> dataSet =  categoryAxis.getDataSet();
			if(null == dataSet ||dataSet.size() == 0){
				Log.w(TAG,"分类轴数据为空.");
				return false;
			}		
			//数据序列
			List<Double> chartValues = bd.getLinePoint();
			if(null == chartValues ||chartValues.size() == 0 )
			{
				Log.w(TAG,"当前分类的线数据序列值为空.");
				return false;
			}
				
			//步长	
			float XSteps ;
			int j = 0; //,childID = 0;	
			int tickCount = dataSet.size();		
			if (1 == tickCount)j = 1;  //label仅一个时右移 !mXCoordFirstTickmarksBegin && 
					
			int labeltickCount = getCategoryAxisCount();		
			XSteps = getVerticalXSteps(labeltickCount );
						
			 						
			float itemAngle = bd.getItemLabelRotateAngle();
			PlotLine pLine = bd.getPlotLine();   
			PlotDot pDot = pLine.getPlotDot();	        
			float radius = pDot.getDotRadius();	
			double bv ;
			
		    //画线
			int count = chartValues.size();
			for(int i=0;i<count;i++)
            {								
				bv = chartValues.get(i);            	
            	lineStopY = getVPValPosition(bv);		            	            	
            	
            	if(mXCoordFirstTickmarksBegin)
				{
            		lineStopX = add(initX , mul((j+1) , XSteps));  
				}else{
					lineStopX = add(initX , mul(j , XSteps));  
				}          
            	//当柱图与线图混合，且柱图柱形为BarCenterStyle.SPACE时
            	if(mXCoordFirstTickmarksBegin && 
            			XEnum.BarCenterStyle.SPACE == mBarCenterStyle)
            					lineStopX = sub(lineStopX ,div(XSteps,2)); 
            	            	            	           
            	if(0 == j)
            	{
            		lineStartX = lineStopX;
            		lineStartY = lineStopY;
            	}
                        	
            	if( !getLineAxisIntersectVisible() &&
            			Double.compare(bv, dataAxis.getAxisMin()) == 0 )
            	{
            		//如果值与最小值相等，即到了轴上，则忽略掉  
            		lineStartX = lineStopX;
    				lineStartY = lineStopY;

    				j++;
            	}else{
	            	        
	            	if(type.equalsIgnoreCase("LINE"))
	            	{	            		
	            		if(getLineAxisIntersectVisible() ||
	            					Float.compare(lineStartY, initY) != 0 )	
	            		{	            		
	            			DrawHelper.getInstance().drawLine(bd.getLineStyle(), 
	            					lineStartX ,lineStartY ,lineStopX ,lineStopY,
	            					canvas,pLine.getLinePaint());		            			
	            		}
	            	}else if(type.equalsIgnoreCase("DOT2LABEL")){
	            		
	            		if(!pLine.getDotStyle().equals(XEnum.DotStyle.HIDE))
	                	{          
	                		PlotDotRender.getInstance().renderDot(canvas,pDot,
	                				lineStopX ,lineStopY,
	                				pLine.getDotPaint()); //标识图形            		
	                			                		
	                		savePointRecord(dataID,i, lineStopX  + mMoveX, lineStopY  + mMoveY,	                		
					                		lineStopX - radius + mMoveX, 
					                		lineStopY - radius + mMoveY,
					    					lineStopX + radius + mMoveX, 
					    					lineStopY + radius + mMoveY);  
	            			lineStopX = add(lineStopX  , radius);           			
	                	}
	            		
	            		//显示批注形状
	    				drawAnchor(getAnchorDataPoint(),dataID,i,canvas,lineStopX - radius,lineStopY,radius);
	            		
	            		if(bd.getLabelVisible()) //标签
	                	{	                	            
	            			bd.getLabelOptions().drawLabel(canvas, pLine.getDotLabelPaint(), 
	            					getFormatterItemLabel(bv), lineStopX  - radius, lineStopY,
	            					itemAngle,bd.getLineColor());
	                	}
	            			            		
	            	}else{
	            		Log.w(TAG,"未知的参数标识。");
	            		return false;
	            	}      				
            	
					lineStartX = lineStopX;
					lineStartY = lineStopY;
	
					j++;
            	} //if(bv != dataAxis.getAxisMin())
            } 				
			return true;
		}
					
		/*
		 * 绘制图表
		 */
		private boolean renderVerticalPlot(Canvas canvas)
		{											
			if(null == mDataSet) 
			{
				Log.w(TAG,"数据轴数据为空.");
				return false;
			}			
			
			mLstKey.clear();
			String key ;
			//开始处 X 轴 即分类轴                  
			int count = mDataSet.size();
			for(int i=0;i<count;i++)
			{								
				if(!renderLine(canvas,mDataSet.get(i),"LINE",i))
					return false;
				if(!renderLine(canvas,mDataSet.get(i),"DOT2LABEL",i))
					return false;
				key = mDataSet.get(i).getLineKey();				
				if("" != key && key.length() > 0)
					mLstKey.add(mDataSet.get(i));
			}			
			
			return true;
		}	

		@Override
		protected void drawClipPlot(Canvas canvas)
		{
			if(renderVerticalPlot(canvas))
			{				
				if(null != mCustomLine) //画横向定制线
				{
					mCustomLine.setVerticalPlot(dataAxis, plotArea, getAxisScreenHeight());
					mCustomLine.renderVerticalCustomlinesDataAxis(canvas);	
				}
				
			}
		}
			
		@Override
		protected void drawClipLegend(Canvas canvas)
		{
			plotLegend.renderLineKey(canvas, mLstKey);
			mLstKey.clear();
		}
}
