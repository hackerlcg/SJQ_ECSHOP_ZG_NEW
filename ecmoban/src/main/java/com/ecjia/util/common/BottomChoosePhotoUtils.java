package com.ecjia.util.common;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.ecjia.component.view.MyBottomSheetDialog;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.reactivex.functions.Consumer;

/**
 * 底部弹框选择照相机/相册弹框
 */
public class BottomChoosePhotoUtils extends BottomSheetDialogFragment {
    private OnChoosePhoneResult result;
    private int count;

    private Boolean isShowPic = false;
    private String path;
    public static final int CAMERA_CODE = 1111;

    private int requestPosition=200000;

    public BottomChoosePhotoUtils() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getActivity(), getTheme());
    }

    public static BottomChoosePhotoUtils newInstance() {
        return new BottomChoosePhotoUtils();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_photo, null, false);

        view.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDcimMuti(count);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
    }

    /**
     * 底部弹框
     */
    public void show(FragmentManager manager, OnChoosePhoneResult result,int requestPosition) {
        this.count = 1;
        this.result = result;
        this.requestPosition=requestPosition;
        show(manager, "tag");
    }

    /**
     * 底部弹框
     */
    public void show(FragmentManager manager, int count, OnChoosePhoneResult result,int requestPosition) {
        this.count = count;
        this.result = result;
        this.requestPosition=requestPosition;
        show(manager, "tag");
    }

    /**
     * 底部弹框
     */
    public void show(FragmentManager manager, int count, Boolean isShowPic, String path, OnChoosePhoneResult result) {
        this.count = count;
        this.result = result;
        this.isShowPic = isShowPic;
        this.path = path;
        show(manager, "tag");
    }

    public interface OnChoosePhoneResult {
        void onHandlerSuccess(List<PhotoInfo> resultList,int requestCode);

        void onHandlerFailure(String errorMsg,int requestCode);
    }

    /**
     * 照相
     */
    private void openCamera() {
        final Activity activity = getActivity();
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        if (activity == null) return;
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            GalleryFinal.openCamera(requestPosition, new GalleryFinal.OnHanlderResultCallback() {
                                @Override
                                public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
                                    result.onHandlerSuccess(resultList,requestCode);
                                }

                                @Override
                                public void onHanlderFailure(int requestCode, String errorMsg) {
                                    result.onHandlerFailure(errorMsg,requestCode);
                                }
                            });
                        } else {
                            DialogUtils.showDialog(activity, "提示", "相机 或者 读写手机存储 权限被关闭，是否设置开启？", new DialogUtils.ButtonClickListener() {
                                @Override
                                public void negativeButton() {

                                }

                                @Override
                                public void positiveButton() {
                                    IntentUtils.IntentAppSetting(activity, CAMERA_CODE);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 相册-最多9张
     */
    private void openDcimMuti(final int count) {
        final Activity activity = getActivity();
        RxPermissions rxPermissions = new RxPermissions(activity);
        if (activity == null) return;
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            GalleryFinal.openGalleryMuti(requestPosition, count, new GalleryFinal.OnHanlderResultCallback() {
                                @Override
                                public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
                                    result.onHandlerSuccess(resultList,requestCode);
                                }

                                @Override
                                public void onHanlderFailure(int requestCode, String errorMsg) {
                                    result.onHandlerFailure(errorMsg,requestCode);
                                }
                            });
                        } else {
                            DialogUtils.showDialog(activity, "提示", "读写手机存储权限 被关闭，是否设置开启？", new DialogUtils.ButtonClickListener() {
                                @Override
                                public void negativeButton() {
                                }

                                @Override
                                public void positiveButton() {
                                    IntentUtils.IntentAppSetting(activity, CAMERA_CODE);
                                }
                            });
                        }
                    }
                });
    }
}
