package com.fyang21117.rdiot1.view.event.click;

import android.graphics.PointF;

/*
 * @InterfaceName ChartBarListener
 * @Description  用于针对bar的点击操作响应接口
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public interface ChartBarListener {
	public void onClick(PointF point, BarPosition barPosition);
	

}
