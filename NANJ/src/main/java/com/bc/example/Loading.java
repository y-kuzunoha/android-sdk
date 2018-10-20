package com.bc.example;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ProgressBar;

/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
public class Loading extends Dialog {
	
	Loading(@NonNull Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new ProgressBar(getContext()));
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		if(window != null) {
			window.setBackgroundDrawableResource(android.R.color.transparent);
		}
	}
}
