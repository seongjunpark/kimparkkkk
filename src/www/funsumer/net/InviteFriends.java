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
import www.funsumer.net.widget.InviteAdapter_fr;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class InviteFriends extends Activity {

	static String mynoteid = MainActivity.mynoteid;
	static String json = "";
	
	JSONArray friendreq = null;
	
	static InputStream is = null;
	public static ArrayList<InviteInfo_fr> mInviteInfo;
	public static InviteAdapter_fr mInviteAdapter;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_friends);

		getData();

		ListView invite_list = (ListView) findViewById(R.id.invite_list);

		mInviteInfo = new ArrayList<InviteInfo_fr>();
		mInviteAdapter = new InviteAdapter_fr(getApplicationContext(),
				R.layout.invite_item_fr, mInviteInfo);
		invite_list.setAdapter(mInviteAdapter);

		setInviteListView();

	}

	public void getData() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "18"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
					HTTP.UTF_8);
			post.setEntity(ent);

			HttpResponse responsePOST = client.execute(post);

			HttpEntity resEntity = responsePOST.getEntity();
			is = resEntity.getContent();

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "euc-kr"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			if (resEntity != null) {
				JSONArray obj = new JSONArray(json);

				Log.e("setting", "json =" + json);
				
				JSONObject tjo = obj.getJSONObject(0);

				Log.e("infr","tjo =" + tjo);
				
				String NumberOfReq = tjo.getString("NumberOfReq");

				friendreq = tjo.getJSONArray("friendreq");

			}

		} catch (Exception e) {
		}

	}

	public void setInviteListView() {
		mInviteAdapter.clear();
		
		try {
			int arrayLength = friendreq.length();
			Log.e("infr", "friendreq == " + friendreq);

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = friendreq.getJSONObject(i);
				InviteInfo_fr tweet = new InviteInfo_fr();

				tweet.setreqme_id(object.getString("reqme_id"));
				tweet.setreqme_name(object.getString("reqme_name"));
				tweet.setreqme_pic(object.getString("reqme_pic"));
				tweet.setreqme_time(object.getString("reqme_time"));

				mInviteInfo.add(tweet);
				
				Log.e("infr", "mInviteInfo" + object);
				Log.e("infr", "mInviteInfo" + object.getString("reqme_pic"));
			}
			mInviteAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
