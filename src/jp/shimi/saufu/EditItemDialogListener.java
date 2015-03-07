package jp.shimi.saufu;

public interface EditItemDialogListener {

		/**
		 * OKボタンが押されたイベントを通知
		 */
		public ItemData doPositiveClick();
		
		/**
		 * Cancelボタンが押されたイベントを通知
		 */
		public void doNegativeClick();
}
