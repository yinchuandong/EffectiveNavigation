package com.ycd.service;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

public class UpgradeUtil {

	public final static String DOWNLOAD_ID = "downloadId";
	private Context context;
	
	public UpgradeUtil(Context context){
		this.context = context;
	}
	
	/**
	 * 通过DownloadManager下载应用
	 * @param downloadUrl
	 * @param name
	 */
	public void download(String downloadUrl, String name){
		DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(downloadUrl);
		Request request = new Request(uri);
		request.setTitle("超级图书馆");
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
		request.setVisibleInDownloadsUi(false);
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setMimeType("application/vnd.android.package-archive");
		request.setDestinationInExternalFilesDir(context, null, name);
		long id = downloadManager.enqueue(request);
		SharedPreferences preferences = context.getSharedPreferences("com.ycd.sp.global",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong(DOWNLOAD_ID, id);
		editor.commit();
	}
	
	/**
	 * 通过浏览器下载
	 * @param url
	 */
	public void download(String url){
		Uri uri = Uri.parse(url);
		Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(downloadIntent);
	}
	
	/**
	 * 判断sdcard是否可用
	 * @return
	 */
	public static boolean hasSdcard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 安装apk
	 * @param fileId 下载的文件的id
	 */
	public void installApk(long fileId){
		SharedPreferences preferences = context.getSharedPreferences("com.ycd.sp.global",
				Context.MODE_PRIVATE);
		long confirmId = preferences.getLong(DOWNLOAD_ID, -1);
		if (confirmId != fileId) {
			return;
		}
		
		Query query = new Query();
		query.setFilterById(fileId);
		DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		Cursor cursor = downloadManager.query(query);
		int columnCount = cursor.getColumnCount();
		String path = null;                                                                                                                                       //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path  
        while(cursor.moveToNext()) {  
            for (int j = 0; j < columnCount; j++) {  
                String columnName = cursor.getColumnName(j);  
                String string = cursor.getString(j);  
                if(columnName.equals("local_uri")) {  
                    path = string;  
                }  
                if(string != null) {  
                    System.out.println(columnName+": "+ string);  
                }else {  
                    System.out.println(columnName+": null");  
                }  
            }  
        }  
        cursor.close(); 
        
        String filePath = path.substring(7,path.length());
		File apkfile = new File(filePath); 
        if (!apkfile.exists())  
        {  
            return;  
        } 
        
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive");  
        context.startActivity(i);
	}
	
}
