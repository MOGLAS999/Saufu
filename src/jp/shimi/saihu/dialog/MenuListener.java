package jp.shimi.saihu.dialog;

import java.util.EventListener;

public interface MenuListener extends EventListener{
	
	/**
	 * リストの一段目が押されたイベントを通知
	 */
	public void doFitstClick();
	
	/**
	 * リストの二段目が押されたイベントを通知
	 */
	public void doSecondClick();

}
