package com.ycd.ui;

import java.io.File;
import java.lang.reflect.Field;

import com.xjl.effectivenavigation.R;
import com.ycd.service.ApkService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	public DrawerLayout drawerLayout;// 侧边栏布局
	public ListView leftList;// 侧边栏内的选项
	public ArrayAdapter<String> arrayAdapter;
	private String[] items;
	private ActionBarDrawerToggle mDrawerToggle;
	private ShareActionProvider shareActionProvider;
	public final static String ACTION_RECEIVER = "com.ycd.ui.receiver";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		debugMemory("oncreate");
		initViews();
		initActionBar();
		initFragments();
		
		IntentFilter intentFilter = new IntentFilter(ACTION_RECEIVER);
		try {
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		registerReceiver(broadcastReceiver, intentFilter);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		debugMemory("onstart");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		debugMemory("onresume");
	}
	
	@Override
	public void onPause(){
		super.onPause();
		debugMemory("onpause");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		debugMemory("onstop");
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		debugMemory("ondestroy");
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
	}
	
	@SuppressLint("NewApi")
	private void initActionBar(){
		getActionBar().setIcon(null);
		mDrawerToggle = new ActionBarDrawerToggle(this, 
				drawerLayout, 
				R.drawable.nav_white, 
				R.string.hello_world, 
				R.string.app_name)
		{
			@Override
			public void onDrawerClosed(View view){
				super.onDrawerClosed(view);
				getActionBar().setTitle("closed");
			}
			
			@Override
			public void onDrawerOpened(View drawerView){
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle("opened");
			}
		};
		
		//menubtn左移
		drawerLayout.setDrawerListener(mDrawerToggle);
		
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        Log.d("MainActivity", "onPostCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
        Log.d("MainActivity", "onConfigurationChanged");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu){
    	getMenuInflater().inflate(R.menu.menu2, menu);
    	MenuItem searchItem = menu.findItem(R.id.action_search);
    	initSearchMenuItem(searchItem);
    	MenuItem alarmItem = menu.findItem(R.id.action_alarm);
    	MenuItem shareItem = menu.findItem(R.id.action_share);
    	MenuItem overflowItem = menu.findItem(R.id.action_overflow);
    	Menu subMenu = overflowItem.getSubMenu();
    	MenuItem shareItem2 = subMenu.findItem(R.id.action_share2);
    	View view = overflowItem.getActionView();
    	TextView textView = (TextView)view.findViewById(R.id.menu_alarm_icon);
    	Log.w("onCreateOptionsMenu------", "text:" + textView.getText());
//    	overflowItem.setIcon(new BitmapDrawable(getResources(), genIcon(getResIcon(getResources(), R.drawable.alarm_white))));
//    	overflowItem.setIcon(getResources().getDrawable(R.drawable.alarm_orange));
    	initShareMenuItem(shareItem);
    	initShareMenuItem(shareItem2);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
    	Log.d("MainActivity", "onOptionsItemSelected");
        if (mDrawerToggle.onOptionsItemSelected(item)) {
        	Log.d("MainActivity", "======");
//        	return true;
        }
        debugMemory("title:"+item.getTitle()+"--itemId:"+item.getItemId()+" android.id"+android.R.id.home);
        
        switch (item.getItemId()) {
		case R.id.action_search:
			Log.d("MainActivity", "===action_search==");
			getActionBar().setIcon(R.drawable.search_white);
			mDrawerToggle.setDrawerIndicatorEnabled(false);
			break;
		case R.id.action_alarm:
			mDrawerToggle.setDrawerIndicatorEnabled(false);
			Log.d("MainActivity", "===action_alarm==");
			break;
		case android.R.id.home:
			if (mDrawerToggle.isDrawerIndicatorEnabled() == false) {
				getActionBar().setIcon(null);
				mDrawerToggle.setDrawerIndicatorEnabled(true);
				debugMemory("android.R.id.home");
			}
			break;
		default:
			break;
		}
        
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }
    


	// 初始化控件
	private void initViews() {
		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		items = getResources().getStringArray(R.array.left_array);
		leftList = (ListView) findViewById(R.id.left_drawer);
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		leftList.setAdapter(arrayAdapter);
		leftList.setOnItemClickListener(itemListener);

	}

	// 添加碎片
	private void initFragments() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		DrawerFragment fragment = new DrawerFragment();
		// 设置在fragment中按钮来控制侧边栏的打开
		fragment.setDrawerLayout(drawerLayout, leftList);
		transaction.add(R.id.main_content, fragment);
		transaction.commit();
	}
	
	private void initSearchMenuItem(MenuItem searchItem){
		SearchView searchView = (SearchView)searchItem.getActionView();
    	
    	//默认展开
    	searchView.setIconified(true);
    	
    	//将内部searchbtn放到左侧
//    	searchView.setIconifiedByDefault(false);
    	
    	//改变搜索图标
    	int searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_button", null, null);
		ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
		searchIcon.setImageResource(R.drawable.search_white);
		
		//改变背景颜色
		int linlayId = getResources().getIdentifier("android:id/search_plate", null, null);
        ViewGroup v = (ViewGroup) searchView.findViewById(linlayId);
//        v.setBackgroundResource(R.drawable.sidebar_background);
        v.setBackgroundColor(getResources().getColor(R.color.bg));
        
        //编辑框
        int idText = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(idText);
        textView.setTextColor(Color.WHITE);
        textView.setHint("Search something here");
        textView.setHintTextColor(Color.WHITE);
//        
        int idClose = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeBtn = (ImageView)searchView.findViewById(idClose);
        closeBtn.setImageResource(R.drawable.clear_white);
        
//        searchView.setQueryHint("请搜索");
//        SearchManager mSearchManager=(SearchManager)getSystemService(SEARCH_SERVICE);
//        SearchableInfo info=mSearchManager.getSearchableInfo(getComponentName());
//        searchView.setSearchableInfo(info); //需要在Xml文件加下建立searchable.xml,搜索框配置文件
    	
//    	searchItem.expandActionView();
    	searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				debugMemory("onMenuItemActionExpand");
				getActionBar().setIcon(R.drawable.search_white);
				mDrawerToggle.setDrawerIndicatorEnabled(false);
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				debugMemory("onMenuItemActionCollapse");
				
				return true;
			}
		});
    	
    	searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				debugMemory("onQueryTextSubmit");
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				debugMemory("onQueryTextChange");
				return false;
			}
		});
    	
    	searchView.setOnCloseListener(new OnCloseListener() {
			
			@Override
			public boolean onClose() {
				debugMemory("searchview onclose");
				return true;
			}
		});
    	
	}

	private void initShareMenuItem(MenuItem menuItem){
		shareActionProvider = (ShareActionProvider)menuItem.getActionProvider();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/Camera/1370446972332.jpg")));
		intent.setType("image/*");
		shareActionProvider.setShareIntent(intent);
		debugMemory(getFileStreamPath("share.png").getAbsolutePath());
		File file = new File("/sdcard/DCIM/Camera/1370446972332.jpg");
		debugMemory(file.getAbsolutePath());
		debugMemory("file----:"+file.exists());
//		startActivity(Intent.createChooser(intent, getResources().getText(R.string.hello_world));
	}
	
	// 选项点击事件
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			// 设置Activity的标题，这里只是用来做一个测试，你可以在这里用来处理单击侧边栏的选项事件
			setTitle(items[position]);
			leftList.setItemChecked(position, true);
			// 关闭侧边栏
			drawerLayout.closeDrawer(leftList);
			Log.i("onItemSelected", "open?:" + drawerLayout.isDrawerOpen(leftList));
			if (position % 2 == 0) {
				getActionBar().setIcon(R.drawable.search_white);
				mDrawerToggle.setDrawerIndicatorEnabled(false);
			}else{
				getActionBar().setIcon(null);
				mDrawerToggle.setDrawerIndicatorEnabled(true);
			}
			
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("onkeydown", "keyi:"+event.getKeyCode());
		
		// 使用menu键打开或关闭侧边栏
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			Log.d("onkeydown", drawerLayout.isDrawerOpen(leftList)+"");
			if (drawerLayout.isDrawerOpen(leftList)) {
				drawerLayout.closeDrawer(leftList);
				return true;
			} else {
				drawerLayout.openDrawer(leftList);
				return true;
			}
		}
		
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1) {
			Log.d("onkeydown", drawerLayout.isDrawerOpen(leftList)+":keyback");
			return false;
		}
		Log.d("onkeydown", "按了返回键之后");
		return super.onKeyDown(keyCode, event);
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		private DownloadManager downloadManager;
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String name = bundle.getString("name");
			long fileId = bundle.getLong("fileid");
			debugMemory("-name:"+name + "  fileid:"+fileId);
			
			debugMemory("----------------------------------------");
			
//			Query query = new Query();
//			query.setFilterById(fileId);
//			downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
//			Cursor cursor = downloadManager.query(query);
//			int columnCount = cursor.getColumnCount();
//			String path = null;                                                                                                                                       //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path  
//	        while(cursor.moveToNext()) {  
//	            for (int j = 0; j < columnCount; j++) {  
//	                String columnName = cursor.getColumnName(j);  
//	                String string = cursor.getString(j);  
//	                if(columnName.equals("local_uri")) {  
//	                    path = string;  
//	                }  
//	                if(string != null) {  
//	                    System.out.println(columnName+": "+ string);  
//	                }else {  
//	                    System.out.println(columnName+": null");  
//	                }  
//	            }  
//	        }  
	       
//	        cursor.close();  
	        //如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理                                                   if(path.startsWith("content:")) {  
//            cursor = getContentResolver().query(Uri.parse(path), null, null, null, null);  
//            columnCount = cursor.getColumnCount();  
//            while(cursor.moveToNext()) {  
//                for (int j = 0; j < columnCount; j++) {  
//                    String columnName = cursor.getColumnName(j);  
//                    String string = cursor.getString(j);  
//                    if(string != null) {  
//                         System.out.println(columnName+": "+ string);  
//	                }else {  
//	                    System.out.println(columnName+": null");  
//	                }  
//	            }  
//	        }  
//	        cursor.close();  
		}
		
	};
	
	/**
     * 在给定的图片的右上角加上联系人数量。数量用红色表示
     * @param icon 给定的图片
     * @return 带联系人数量的图片
     */
    private Bitmap genIcon(Bitmap icon){
    	//初始化画布
    	int iconSize=(int)getResources().getDimension(android.R.dimen.app_icon_size);
    	int width = icon.getWidth();
    	int height = icon.getHeight();
    	Log.w("mainactivity------", "size:" + iconSize + "--" + width + "--" + height);
    	Bitmap contactIcon=Bitmap.createBitmap(width, height, Config.ARGB_8888);
    	Canvas canvas=new Canvas(contactIcon);
    	
    	//拷贝图片
    	Paint iconPaint=new Paint();
    	iconPaint.setDither(true);//防抖动
    	iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
    	Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());
    	Rect dst=new Rect(0, 0, width, height);
    	canvas.drawBitmap(icon, src, dst, iconPaint);
    	
    	float radius = 13.0f;
    	float cx = width - radius;
    	float cy = radius;
    	
    	Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
    	circlePaint.setColor(Color.RED);
    	canvas.drawCircle(cx, cy, radius, circlePaint);
    	
    	//在图片上创建一个覆盖的联系人个数
    	int contacyCount=14;
    	//启用抗锯齿和使用设备的文本字距
    	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
    	countPaint.setColor(Color.WHITE);
    	countPaint.setTextSize(20f);
    	countPaint.setTypeface(Typeface.DEFAULT_BOLD);
    	canvas.drawText(String.valueOf(contacyCount), cx-radius, cy + 5, countPaint);
    	return contactIcon;
    }
	
	/**
     * 根据id获取一个图片
     * @param res
     * @param resId
     * @return
     */
    private Bitmap getResIcon(Resources res,int resId){
    	Drawable icon=res.getDrawable(resId);
    	if(icon instanceof BitmapDrawable){
    		BitmapDrawable bd=(BitmapDrawable)icon;
    		return bd.getBitmap();
    	}else{
    		return null;
    	}
    }

	
	private void installApk(String path){
		File apkfile = new File(path); 
        if (!apkfile.exists())  
        {  
            return;  
        }  
        // 通过Intent安装APK文件  
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
        startActivity(i);
	}
	
	public void debugMemory(String msg){
		Log.i(getClass().getSimpleName(), msg);
	}

}
