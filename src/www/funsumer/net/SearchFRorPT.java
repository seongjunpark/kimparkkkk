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

import www.funsumer.net.constants.SearchInfoFR;
import www.funsumer.net.constants.SearchInfoPT;
import www.funsumer.net.widget.AdapterSearchFR;
import www.funsumer.net.widget.AdapterSearchPT;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class SearchFRorPT extends Activity {

	static String mynoteid = MainActivity.mynoteid;
	static String userid;
	public static String getuserid;
	static String json = "";
	static InputStream is = null;
	private ArrayList<SearchInfoFR> mInviteInfo;
	private ArrayList<SearchInfoPT> mInviteInfoPT;
	private AdapterSearchFR mInviteAdapter;
	private AdapterSearchPT mInviteAdapterPT;
	String value;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchlayout);

		Intent intent = getIntent();
		value = intent.getStringExtra("value");

		getData();

		ListView invite_list = (ListView) findViewById(R.id.invite_list);
		ListView invite_list2 = (ListView) findViewById(R.id.invite_list2);

		mInviteInfo = new ArrayList<SearchInfoFR>();
		mInviteAdapter = new AdapterSearchFR(getApplicationContext(),
				R.layout.search_item, mInviteInfo);
		invite_list.setAdapter(mInviteAdapter);

		mInviteInfoPT = new ArrayList<SearchInfoPT>();
		mInviteAdapterPT = new AdapterSearchPT(getApplicationContext(),
				R.layout.search_item, mInviteInfoPT);
		invite_list2.setAdapter(mInviteAdapterPT);

		setInviteListView();
		setInviteListView2();

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
			params1.add(new BasicNameValuePair("oopt", "19"));
			params1.add(new BasicNameValuePair("mynoteid",mynoteid));
			params1.add(new BasicNameValuePair("value", value));

			System.out.println("asefasefasefaef" + params1);

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

				String Numbers = tjo.getString("Numbers");

				people = tjo.getJSONArray("people");
				parties = tjo.getJSONArray("parties");
			}

		} catch (Exception e) {
		}

	}

	JSONArray parties = null;
	JSONArray people = null;

	public void setInviteListView() {

		try {
			int arrayLength = people.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = people.getJSONObject(i);
				SearchInfoFR tweet = new SearchInfoFR();

				tweet.setname(object.getString("name"));
				tweet.setid(object.getString("id"));
				tweet.setpic(object.getString("pic"));
				tweet.setpcount(object.getString("pcount"));
				tweet.setfstat(object.getString("fstat"));

				mInviteInfo.add(tweet);
			}
			mInviteAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setInviteListView2() {

		try {
			int arrayLength = parties.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = parties.getJSONObject(i);
				SearchInfoPT tweet = new SearchInfoPT();

				tweet.setpname(object.getString("pname"));
				tweet.setpid(object.getString("pid"));
				tweet.setppic(object.getString("ppic"));
				tweet.setpadmin(object.getString("padmin"));
				tweet.setpcount(object.getString("pcount"));

				mInviteInfoPT.add(tweet);
			}
			mInviteAdapterPT.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
