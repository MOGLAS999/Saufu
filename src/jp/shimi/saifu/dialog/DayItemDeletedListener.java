package jp.shimi.saifu.dialog;

import java.util.Calendar;
import java.util.EventListener;

public interface DayItemDeletedListener extends EventListener{
	
	/**
	 * 日データのアイテムが削除されたイベントを通知
	 */
	public void DayItemDeleted(Calendar deletedDate);

}
