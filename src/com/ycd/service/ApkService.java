package com.ycd.service;

import java.io.File;

import com.ycd.ui.MainActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager.Request;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class ApkService extends Service{

	private AlertDialog dialog;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
//		showNotifyDialog();
//		beginDownload();
//		download();
		new UpgradeUtil(this).download("http://192.168.233.21/EffectiveNavigation.apk", "EffectiveNavigationyyy.apk");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	private int count = 0;
	private void beginDownload(){
		new Thread(){
			@Override
			public void run(){
				try {
//					while(true){
//						Thread.sleep(3000);
						Intent intent = new Intent(MainActivity.ACTION_RECEIVER);
						Bundle bundle = new Bundle();
						bundle.putString("name", "yinchuandong count="+(count++));
						intent.putExtras(bundle);
						sendBroadcast(intent);
//					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.start();
	}
	
	private void showNotifyDialog(){
//		AlertDialog dialog = AlertDialog.Builder(getApplicationContext());
		AlertDialog.Builder builder = new Builder(getApplicationContext());
		builder.setTitle("检查更新");
		builder.setMessage("有更新");
		dialog = builder.create();
		dialog.show();
	}
	
	private void download(){
		DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		String downloadUrl = "http://192.168.233.21/EffectiveNavigation.apk";
		Uri uri = Uri.parse(downloadUrl);
		Request request = new Request(uri);
		request.setTitle("超级图书馆");
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
		request.setVisibleInDownloadsUi(false);
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setMimeType("application/vnd.android.package-archive");
		debugMemory(uri.getPath());
		String mimeString = MimeTypeMap.getFileExtensionFromUrl(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
		debugMemory("mime:"+mimeString);
		debugMemory("----------------------------------------");
		debugMemory(Environment.getDownloadCacheDirectory().getPath());
		debugMemory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
		debugMemory(Environment.getExternalStorageDirectory().getPath());
		debugMemory(Environment.getDataDirectory().getAbsolutePath());
		
//		File dir = new File("sdcard/gwlibrary/apk");
//		if (!dir.exists()) {
//			dir.mkdir();
//		}
//		request.setDestinationUri(Uri.fromFile(new File("sdcard/gwlibrary/apk/EffectiveNavigation.apk")));
		request.setDestinationInExternalFilesDir(this, null, "EffectiveNavigation.apk");
		long id = downloadManager.enqueue(request);
		debugMemory("download id:"+id);
		Intent intent = new Intent(MainActivity.ACTION_RECEIVER);
		Bundle bundle = new Bundle();
		bundle.putString("name", "yinchuandong count="+(count++));
		bundle.putLong("fileid", id);
		intent.putExtras(bundle);
		sendBroadcast(intent);
		
//		Intent intent2 = new Intent("ccom.ycd.service.MbroadCast");
//		Bundle bundle2 = new Bundle();
//		bundle.putString("name", "yinchuandong count="+(count++));
//		bundle.putLong("fileid", id);
//		intent.putExtras(bundle2);
//		sendBroadcast(intent2);
	}
	
	public void debugMemory(String msg){
		Log.i(getClass().getSimpleName(), msg);
	}

}
