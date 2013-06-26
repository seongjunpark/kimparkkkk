package www.funsumer.net;

//import static www.funsumer.net.widget.CustomAdapter.authorid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import www.funsumer.net.constants.InviteInfo_fr;
import www.funsumer.net.constants.PartyJoinInfo;
import www.funsumer.net.widget.InviteAdapter_fr;
import www.funsumer.net.widget.PartyJoinAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class PartyJoin extends Activity {

	static String mynoteid = MainActivity.mynoteid;
	static String json = "";
	public static String num;

//	JSONArray member = null;

	static InputStream is = null;
	public static ArrayList<PartyJoinInfo> mInviteInfo;
	public static PartyJoinAdapter mInviteAdapter;
	String pid;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partyjoin);

		Intent intent = getIntent();
		pid = intent.getStringExtra("partyid");
		
		Log.e("partyjoin", "pid = " + pid);
		
//		getData();

		ListView invite_list = (ListView) findViewById(R.id.invite_list);

		mInviteInfo = new ArrayList<PartyJoinInfo>();
		mInviteAdapter = new PartyJoinAdapter(getApplicationContext(),
				R.layout.invite_item_fr, mInviteInfo, pid);
		invite_list.setAdapter(mInviteAdapter);

		setInviteListView();

	}

//	public void getData() {
//
//		try {
//
//			HttpClient client = new DefaultHttpClient();
//			String postURL = "http://funsumer.net/json/";
//			HttpPost post = new HttpPost(postURL);
//
//			List params1 = new ArrayList();
//			params1.add(new BasicNameValuePair("oopt", "24"));
//			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
//			params1.add(new BasicNameValuePair("pid", pid));
//			params1.add(new BasicNameValuePair("position", "1"));
//
//			System.out.println("lihasliefasef"+params1);
//			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
//					HTTP.UTF_8);
//			post.setEntity(ent);
//
//			HttpResponse responsePOST = client.execute(post);
//
//			HttpEntity resEntity = responsePOST.getEntity();
//			is = resEntity.getContent();
//
//			try {
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(is, "euc-kr"), 8);
//				StringBuilder sb = new StringBuilder();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//				is.close();
//				json = sb.toString();
//			} catch (Exception e) {
//				Log.e("Buffer Error", "Error converting result " + e.toString());
//			}
//
//			if (resEntity != null) {
//				Log.e("setting", "json =" + json);
//
//				JSONArray obj = new JSONArray(json);
//				Log.e("setting", "obj" + obj);
//				JSONObject tjo = obj.getJSONObject(0);
//
//				num = tjo.getString("num");
//
//				member = tjo.getJSONArray("member");
//			}
//
//		} catch (Exception e) {
//		}
//
//	}

	public void setInviteListView() {
		mInviteAdapter.clear();

		try {
			int arrayLength = SettingDialog.member.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = SettingDialog.member.getJSONObject(i);
				PartyJoinInfo tweet = new PartyJoinInfo();

				tweet.setmid(object.getString("mid"));
				tweet.setmname(object.getString("mname"));
				tweet.setmpic(object.getString("mpic"));

				mInviteInfo.add(tweet);

			}
			mInviteAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
