package com.maxxcoffee.mobile.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.Html;

public class TBaseProgress {
	
	private ProgressDialog mProgressDialog;
	
//	private String mMessage;
	
	public TBaseProgress(Context p_context) {
		mProgressDialog = new ProgressDialog(p_context);
		mProgressDialog.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(Html.fromHtml(getDefaultMessage()));
    }

	public final TBaseProgress setOnCancelListener(OnCancelListener p_listener) {
		mProgressDialog.setOnCancelListener(p_listener);
		return this;
	}
	
	public final TBaseProgress setMessage(String p_message) {
		mProgressDialog.setMessage(Html.fromHtml("<b>"+ p_message +"</b>"));
		return this;
	}

//	public final String getMessage() {
//		return mMessage;
//	}
	
	public final String getDefaultMessage() {
		return "<b>Tunggu Sebentar... </b>";
	}
	
	public final TBaseProgress setCancelable(boolean p_cancelable) {
		mProgressDialog.setCancelable(p_cancelable);
		return this;
	}
	
	public final void show() {
		mProgressDialog.show();
	}
	
	public final void dismiss() {
		mProgressDialog.dismiss();
	}
	
	public final boolean isShowing() {
		return mProgressDialog.isShowing();
	}
		
}