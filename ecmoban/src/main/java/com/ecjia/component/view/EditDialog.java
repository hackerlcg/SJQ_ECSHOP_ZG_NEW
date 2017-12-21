package com.ecjia.component.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.adapter.EditDialogAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/2.
 */
public class EditDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    public TextView cancel,title;
    public MyListView lv_edit_dialog;
    private EditDialogAdapter adapter;

    @SuppressLint("NewApi")
    public EditDialog(Context context,ArrayList<String> strs) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        View view = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null);
        view.setMinimumWidth(display.getWidth());
//        view.setAlpha(0.5f);
        title = (TextView) view.findViewById(R.id.tv_edit_dialog_title);

        cancel = (TextView) view.findViewById(R.id.edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lv_edit_dialog=(MyListView)view.findViewById(R.id.lv_edit_dialog);
        adapter=new EditDialogAdapter(context,strs);
        lv_edit_dialog.setAdapter(adapter);

        // 创建Dialog
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }



    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}