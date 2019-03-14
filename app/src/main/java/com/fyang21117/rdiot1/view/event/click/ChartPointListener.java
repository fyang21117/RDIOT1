package com.fyang21117.rdiot1.view.event.click;

import android.graphics.PointF;

/*
 * @InterfaceName ChartPointListener
 * @Description  用于针对point的点击操作响应接口
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public interface ChartPointListener {

	public void onClick(PointF point, PointPosition pointPosition);
	
}
