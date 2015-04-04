package jp.shimi.saihu.dialog;

import java.util.Calendar;
import java.util.EventListener;

public interface DayDeletedListener extends EventListener{
	
	/**
	 * 日データが削除されたイベントを通知
	 */
	public void DayDeleted(Calendar deletedDate);

}
