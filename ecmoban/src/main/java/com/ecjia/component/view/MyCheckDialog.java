package com.ecjia.component.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;

public class MyCheckDialog {

	private Dialog mDialog;
	private TextView dialog_title;
	private EditText dialog_edit;
	public LinearLayout positive;
	public LinearLayout version_positive;
	public LinearLayout negative;
	public LinearLayout dialog_buttom;

	public MyCheckDialog(Context context, String title, String edit) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.check_dialog_layout, null);

		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		
		dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_edit = (EditText) view.findViewById(R.id.dialog_edit);
		dialog_title.setText(title);
        dialog_edit.setHint(edit);

		positive = (LinearLayout) view.findViewById(R.id.neadpay);
		version_positive = (LinearLayout) view.findViewById(R.id.version_sure);
		negative = (LinearLayout) view.findViewById(R.id.unneadpay);
		dialog_buttom = (LinearLayout) view.findViewById(R.id.update_cancel);

	}
	
	public void show() {
		mDialog.show();
	}
	
	public void dismiss() {
		mDialog.dismiss();
	}

    public String getEnter(){
        return dialog_edit.getText().toString();
    }

}
