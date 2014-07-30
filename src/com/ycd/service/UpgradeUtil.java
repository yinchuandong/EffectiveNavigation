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
	 * ͨ��DownloadManager����Ӧ��
	 * @param downloadUrl
	 * @param name
	 */
	public void download(String downloadUrl, String name){
		DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(downloadUrl);
		Request request = new Request(uri);
		request.setTitle("����ͼ���");
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
	 * ͨ�����������
	 * @param url
	 */
	public void download(String url){
		Uri uri = Uri.parse(url);
		Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(downloadIntent);
	}
	
	/**
	 * �ж�sdcard�Ƿ����
	 * @return
	 */
	public static boolean hasSdcard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * ��װapk
	 * @param fileId ���ص��ļ���id
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
		String path = null;                                                                                                                                       //TODO ��������е��ж���ӡһ�£���ʲô���󣬾���ô����,�ļ��ı���·������path  
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
