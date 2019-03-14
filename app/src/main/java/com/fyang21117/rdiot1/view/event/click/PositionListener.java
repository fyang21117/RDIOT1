package com.fyang21117.rdiot1.view.event.click;

import android.graphics.PointF;

/*
 * @InterfaceName PositionListener
 * @Description  用于针对所有图形的点击操作响应接口
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public interface PositionListener {
	
	public void onClick(PointF point, PositionRecord positionRecord);

}
