package com.ecjia.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ecmoban.android.shopkeeper.sijiqing.R;

/**
 * Created by Administrator on 2016/1/18.
 */
public class BaseFragment extends Fragment {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }


}
