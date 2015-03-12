package jp.shimi.saufu;

import java.util.EventListener;

public interface ItemRemoveListener extends EventListener{
	
	/**
	 * アイテムが削除されたイベントを通知
	 */
	public void removeItem();

}
