package jp.shimi.saufu;

import java.util.EventListener;

public interface DayEmptyListener extends EventListener{
	
	/**
	 * 日データのアイテムが空になったイベントを通知
	 */
	public void DayBecameEmpty();

}
