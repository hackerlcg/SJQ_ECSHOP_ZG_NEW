package com.ecjia.util.common;

import android.content.Context;
import android.view.View;

import com.ecjia.component.view.MyDialog;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-21.
 */

public class DialogUtils {

    /**
     * @param context
     * @param title       标题
     * @param tipContent 提示内容
     * @param listener
     */
    public static void showDialog(Context context, String title, String tipContent, final ButtonClickListener listener) {
        MyDialog myDialog = new MyDialog(context, title, tipContent);
        myDialog.show();
        myDialog.negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                listener.negativeButton();
            }
        });
        myDialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                listener.positiveButton();
                //getVerify(jsonStr);
            }
        });
    }

    public interface ButtonClickListener {
        void negativeButton();//取消操作按钮

        void positiveButton();//操作按钮
    }

    public static abstract class ButtonClickListenerAbstract implements ButtonClickListener {
        @Override
        public void negativeButton() {
        }
    }
}
