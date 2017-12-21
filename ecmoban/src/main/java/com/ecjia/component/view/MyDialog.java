package com.ecjia.component.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;

public class MyDialog {

    public Dialog mDialog;
    private TextView dialog_title;
    private TextView dialog_message;
    public LinearLayout positive;
    public LinearLayout version_positive;
    public LinearLayout negative;
    public LinearLayout dialog_buttom;
    private Context context;

    public TextView positiveText, negativeText, sureText;

    public MyDialog(Context context, String title, String message) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        this.context = context;

        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);

        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_message = (TextView) view.findViewById(R.id.dialog_message);
        dialog_title.setText(title);
        dialog_message.setText(message);

        version_positive = (LinearLayout) view.findViewById(R.id.version_sure);
        sureText = (TextView) view.findViewById(R.id.version_yes);

        positive = (LinearLayout) view.findViewById(R.id.neadpay);//确定
        positiveText = (TextView) positive.findViewById(R.id.yes);


        negative = (LinearLayout) view.findViewById(R.id.unneadpay);//取消
        negativeText = (TextView) view.findViewById(R.id.no);

        dialog_buttom = (LinearLayout) view.findViewById(R.id.update_cancel);

    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setBackKeyNo() {
        mDialog.setCancelable(false);
    }

    public void setBackKeyYes() {
        mDialog.setCancelable(true);
    }

}
