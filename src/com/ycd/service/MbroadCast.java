package com.ycd.service;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MbroadCast extends BroadcastReceiver{

	private DownloadManager downloadManager;
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String teString = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
		debugMemory(intent.getAction());
		long fileId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		new UpgradeUtil(context).installApk(fileId);
		
//		Query query = new Query();
//		query.setFilterById(fileId);
//		downloadManager = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
//		Cursor cursor = downloadManager.query(query);
//		int columnCount = cursor.getColumnCount();
//		String path = null;                                                                                                                                       //TODO ��������е��ж���ӡһ�£���ʲô���󣬾���ô����,�ļ��ı���·������path  
//        while(cursor.moveToNext()) {  
//            for (int j = 0; j < columnCount; j++) {  
//                String columnName = cursor.getColumnName(j);  
//                String string = cursor.getString(j);  
//                if(columnName.equals("local_uri")) {  
//                    path = string;  
//                }  
//                if(string != null) {  
//                    System.out.println(columnName+": "+ string);  
//                }else {  
//                    System.out.println(columnName+": null");  
//                }  
//            }  
//        }  
//        cursor.close(); 
        //���sdcard������ʱ�����������ļ�����ô���ｫ��һ�������ṩ�ߵ�·���������ӡ��������ʲô�������ô������                                                   if(path.startsWith("content:")) {  
//      cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);  
//      columnCount = cursor.getColumnCount();  
//      while(cursor.moveToNext()) {  
//          for (int j = 0; j < columnCount; j++) {  
//              String columnName = cursor.getColumnName(j);  
//              String string = cursor.getString(j);  
//              if(string != null) {  
//                   debugMemory(columnName+": "+ string);  
//              }else {  
//                  debugMemory(columnName+": null");  
//              }  
//          }  
//      }  
//      cursor.close();  
//      installApk(path);
	}
	
	private void installApk(String path){
		String filePath = path.substring(7,path.length());
		debugMemory(filePath);
		File apkfile = new File(filePath); 
        if (!apkfile.exists())  
        {  
            return;  
        }  
		debugMemory("----------------------------------------");
//		File file = Environment.getDownloadCacheDirectory();
//		for (File file2 : file.listFiles()) {
//			debugMemory(file2.getAbsolutePath());
//		}
        // ͨ��Intent��װAPK�ļ�  
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive");  
        context.startActivity(i);
	}
	public void debugMemory(String msg){
		Log.i(getClass().getSimpleName(), msg);
	}

}
