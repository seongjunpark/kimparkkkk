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

import www.funsumer.net.constants.InviteInfo;
import www.funsumer.net.widget.InviteAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class InviteParty extends Activity {

	static String mynoteid = MainActivity.mynoteid;
	static String userid;
	public static String getuserid;
	static String json = "";
	static InputStream is = null;
	public static ArrayList<InviteInfo> mInviteInfo;
	public static InviteAdapter mInviteAdapter;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_party);

		getData();

		ListView invite_list = (ListView) findViewById(R.id.invite_list);

		mInviteInfo = new ArrayList<InviteInfo>();
		mInviteAdapter = new InviteAdapter(getApplicationContext(),
				R.layout.invite_item, mInviteInfo);
		invite_list.setAdapter(mInviteAdapter);

		setInviteListView();

	}

	public void getData() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);
			/*
			 * Toast.makeText(getApplicationContext(), "�곌껐�꾨즺",
			 * Toast.LENGTH_LONG) .show();
			 */

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "17"));
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

				JSONObject tjo = obj.getJSONObject(0);

				String NumberOfInv = tjo.getString("NumberOfInv");

				partyinv = tjo.getJSONArray("partyinv");

			}

		} catch (Exception e) {
		}

	}

	JSONArray partyinv = null;

	public void setInviteListView() {
		mInviteAdapter.clear();
		
		try {
			int arrayLength = partyinv.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = partyinv.getJSONObject(i);
				InviteInfo tweet = new InviteInfo();

				tweet.setinvme_id(object.getString("invme_id"));
				tweet.setinvme_name(object.getString("invme_name"));
				tweet.setinvme_pic(object.getString("invme_pic"));
				tweet.setinvparty_id(object.getString("invparty_id"));
				tweet.setinvparty_name(object.getString("invparty_name"));
				tweet.setinvtime(object.getString("invtime"));

				mInviteInfo.add(tweet);
				System.out.println("aishefasef"+mInviteInfo);
			}
			mInviteAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
