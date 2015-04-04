package jp.shimi.saihu.dialog;

import java.util.Calendar;
import java.util.EventListener;

public interface ItemRemoveListener extends EventListener{
	
	/**
	 * アイテムが削除されたイベントを通知
	 */
	public void removeItem(Calendar deletedDate);

}
