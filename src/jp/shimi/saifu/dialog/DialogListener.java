package jp.shimi.saifu.dialog;

public interface DialogListener {
	
	/**
	 * OKボタンが押されたイベントを通知
	 */
	public void doPositiveClick();
	
	/**
	 * Cancelボタンが押されたイベントを通知
	 */
	public void doNegativeClick();
	
}
