package www.funsumer.net;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import www.funsumer.net.login.AlertDialogManager;
import www.funsumer.net.login.KakaoLink;
import www.funsumer.net.login.ListArrayItem;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.gsHttpConnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Copyright 2012 Kakao Crop. All rights reserved.
 * 
 * @author kakaolink@kakao.com
 */
public class InviteKakao extends Activity  {
	String dial1 = "";
	String url = "";
	ProgressDialog dialog;
	// Recommened Charset UTF-8
	private String encoding = "UTF-8";
	JSONArray LoginAPI = null;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitekakao);


		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

		// If application is support Android platform.
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl", "market://details?id=www.funsumer.net");
		metaInfoAndroid.put("executeurl", "kakaoLinkTest://starActivity");

		// add to array
		metaInfoArray.add(metaInfoAndroid);

		// Recommended: Use application context for parameter.
		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!kakaoLink.isAvailableIntent())
			return;

		/**
		 * @param activity
		 * @param url
		 * @param message
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 * @param metaInfoArray
		 */
		try {
			kakaoLink
					.openKakaoAppLink(
							this,
							"http://link.kakao.com/?test-android-app",
							"<펀슈머> 우리끼리 이빨터는 모임 SNS!!",
							getPackageName(),
							getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
							"www.Funsumer.net", encoding, metaInfoArray);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		Button bt_nexter = (Button) findViewById(R.id.bt_nexter);
		bt_nexter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				finish();
//				InviteKakao.this.finish();
				// TODO Auto-generated method stub
				
			}
		});

	}
	/**
	 * Send URL
	 * 
	 * @throws NameNotFoundException
	 */
	public void sendUrlLink(View v) throws NameNotFoundException {
		// Recommended: Use application context for parameter.
		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!kakaoLink.isAvailableIntent())
			return;

		/**
		 * @param activity
		 * @param url
		 * @param message
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 */
//		kakaoLink
//				.openKakaoLink(
//						this,
//						"http://link.kakao.com/?test-android-app",
//						"First KakaoLink Message for send url.",
//						getPackageName(),
//						getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
//						"KakaoLink Test App", encoding);
	}

	/**
	 * Send App data
	 */
	public void sendAppData(View v) throws NameNotFoundException {
		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();

		// If application is support Android platform.
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl", "market://details?id=www.funsumer.net");
		metaInfoAndroid.put("executeurl", "kakaoLinkTest://starActivity");

		// add to array
		metaInfoArray.add(metaInfoAndroid);

		// Recommended: Use application context for parameter.
		KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

		// check, intent is available.
		if (!kakaoLink.isAvailableIntent())
			return;

		/**
		 * @param activity
		 * @param url
		 * @param message
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 * @param metaInfoArray
		 */
//		kakaoLink
//				.openKakaoAppLink(
//						this,
//						"http://link.kakao.com/?test-android-app",
//						"<펀슈머> 우리끼리 이빨터는 모임 SNS!!",
//						getPackageName(),
//						getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
//						"www.Funsumer.net", encoding, metaInfoArray);
	}

}
