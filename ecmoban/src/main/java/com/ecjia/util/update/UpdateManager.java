package com.ecjia.util.update;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.entity.JsonRet;
import com.ecjia.entity.Update;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ToastUtil;


public class UpdateManager {
	// 缓存目录文件 wln 2.28
	public static String CACHE_PATH = Environment.getExternalStorageDirectory()
			.getPath() + "/sjq/";
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载出现错误 */
	private static final int DOWNLOAD_ERROR = 3;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	private Activity mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;

	private Dialog mDownloadDialog;
	private String FileName = "";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				Log.d("test", "下载:" + progress);
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				Log.d("test", "下载完成");
				if (mDownloadDialog != null)
					mDownloadDialog.dismiss();
				installApk();
				break;
			case DOWNLOAD_ERROR:
				ToastUtil.getInstance().makeLongToast(mContext, "下载出现错误");
				break;
			default:
				break;
			}
		};
	};
	public Update update;

	public UpdateManager(Activity context) {
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		RetrofitAPIManager.getAPIPublic()
				.updateFile("app.versionUpdate",
						String.valueOf(getVersionCode(mContext)),
						"1")
				.compose(RxSchedulersHelper.io_main())
				.subscribe(d -> onVersion(d),
						e -> ToastUtil.getInstance().makeLongToast(mContext,"暂无更新"));
	}

	public void onVersion(JsonRet<Update> data){
		if(data.getStatus()) {
			update = data.getData();
			if (update.getUpdateFlag() != 0) {//update.getUpdateFlag() != 0
				showUpdateDialog(update);
				FileName = update.getVersion() + ".apk";
			}
		}
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	private void showUpdateDialog(final Update update) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("发现新版本" + update.getVersion());
		if (2 == (update.getUpdateFlag())) {
			builder.setMessage("当前版本已过期，必须更新后才能继续使用!");
		} else {
			builder.setMessage("检测到新版本，请及时更新！");
		}

		String btntip1 = "";// 更新按钮
		String btntip2 = "";// 取消更新按钮

		// 强制更新，点击直接下载更新
		if (2 == (update.getUpdateFlag())) {
			btntip1 = "立即更新";
			btntip2 = "退出";

		} else {
			btntip1 = "立即更新";
			btntip2 = "稍后再说";
		}

		builder.setPositiveButton(btntip1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 强制更新，点击直接下载更新
						if (2 == (update.getUpdateFlag())) {
							showDownloadDialog();
						}
//						else {
//							Intent intent = new Intent(mContext,
//									AppDownLoader.class);
//							intent.putExtra("update", update);
//							intent.putExtra("cruVersion",
//									getVersionName(mContext));
//							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							mContext.startActivity(intent);
//							GlobalApplication.getMlssApp().setDownload(true);
//						}

					}
				}).setNegativeButton(btntip2,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (2 == (update.getUpdateFlag())) {
							// 强制更新 不更新直接退出程序wln
							showExitTip();
						}

					}
				});
		AlertDialog dialog = builder.create();
		// dialog.getWindow()
		// .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		if (2 == (update.getUpdateFlag())) {
			dialog.setCanceledOnTouchOutside(false);
		} else {
			dialog.setCanceledOnTouchOutside(true);
		}
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				} else {
					return false;
				}
			}
		});
		dialog.show();
	}

	/**
	 * 强制更新退出提示
	 * 
	 * 
	 */
	private void showExitTip() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("温馨提示");
		builder.setMessage("为了更好的使用客户端功能,必须更新后才能使用，确认退出吗?");

		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				showDownloadDialog();
			}
		}).setNegativeButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mContext.startActivity(intent);
				} catch (Exception e) {
					e.getStackTrace();
				}
//				HXUtils.loginOutHXService();
				ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
				am.killBackgroundProcesses(mContext.getApplicationContext().getPackageName());
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		});

		AlertDialog TipDialog = builder.create();
		// TipDialog.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		TipDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				} else {
					return false;
				}
			}
		});
		TipDialog.show();
	}

	public static int dip2px(Context context, int dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	private void showDownloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		mDownloadDialog = new Dialog(mContext, R.style.LoadDialog);
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels - dip2px(mContext, 180);
		mDownloadDialog.setContentView(v);
		Window dialogWindow = mDownloadDialog.getWindow();
		dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = screenWidth;
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);
		// dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mDownloadDialog.show();
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				} else {
					return false;
				}
			}
		});

		downloadApk();// 下载apk
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					File file = new File(CACHE_PATH);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}

					File apkfile = new File(CACHE_PATH, FileName);
					URL url = new URL(update.getUpdateUrl());
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 文件大小相同,则判断为已下载
					if (apkfile.length() == length) {
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						return;
					}
					// 创建输入流
					InputStream is = conn.getInputStream();

					FileOutputStream fos = new FileOutputStream(apkfile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成

							fos.flush();
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate); // 点击取消就停止下载.

					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
				Log.e("test", e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("test", e.toString());
				mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(CACHE_PATH, FileName);
		if (!apkfile.exists()) {
			ToastUtil.getInstance().makeLongToast(mContext, "安装文件不存在");
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}