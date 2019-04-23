package com.fyang21117.rdiot1.test1.event.touch;

import android.view.MotionEvent;

/*
 * @InterfaceName IChartTouch
 * @Description  用于手势操作图表的接口
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public interface IChartTouch {

	public void handleTouch(MotionEvent event);
}
