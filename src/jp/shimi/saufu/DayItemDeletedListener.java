package jp.shimi.saufu;

import java.util.EventListener;

public interface DayItemDeletedListener extends EventListener{
	
	/**
	 * 日データのアイテムが削除されたイベントを通知
	 */
	public void DayItemDeleted();

}
