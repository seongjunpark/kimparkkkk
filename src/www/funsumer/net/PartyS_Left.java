package www.funsumer.net;

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

import www.funsumer.net.constants.FriendInfo;
import www.funsumer.net.widget.InvitetopartyAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PartyS_Left extends Activity {

	public ListView m_lv = null;
	public LinearLayout fr_header = null;

	ArrayList<FriendInfo> mFriendList;
//	FriendAdapter f_adapter;
	InvitetopartyAdapter f_adapter;

	JSONParser jParser = new JSONParser();

	JSONArray guflAPI = null;
	String Result = null;
	JSONArray Result_data = null;

	String mynoteid = MainActivity.mynoteid;
	SparseBooleanArray booleans;
	String array = "";
	static int count = 0;
	String writeName;
	String FROM_PARTY = "0";
	String partyid;

	ImageView btnimage;
	ImageView btnimage02;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitefr);

		String url = "http://funsumer.net/json?opt=3&mynoteid=" + mynoteid
				+ "&userid=" + mynoteid;

		JSONParser jParser = new JSONParser();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		try {
			JSONObject jsonurl = jParser.getJSONFromUrl(url);
			guflAPI = jsonurl.getJSONArray("guflAPI");
			JSONObject resultdata = guflAPI.getJSONObject(0);
			Result = resultdata.getString("Result");
			Result_data = resultdata.getJSONArray("Result_data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Constants_friendlist_databox.BDURL(Result_data);

		try {
			m_lv = (ListView) findViewById(R.id.friendlist1);
			// m_lv.addHeaderView(fr_header());

			mFriendList = new ArrayList<FriendInfo>();
			f_adapter = new InvitetopartyAdapter(this,
					R.layout.invite_friendlist_item, mFriendList);
			m_lv.setAdapter(f_adapter);
			m_lv.setDividerHeight(0);
			m_lv.setItemsCanFocus(false);
			m_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		setFriendListView();

		Intent intent = getIntent();
		writeName = intent.getStringExtra("writeName");
		FROM_PARTY = intent.getStringExtra("FROM_PARTY");
		partyid = intent.getStringExtra("partyid");

		Log.e("abc", "FROM_PARTY" + FROM_PARTY);
		
		Button backbtn = (Button) findViewById(R.id.backbtn);
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirm(v);
				// TODO Auto-generated method stub

				patissier_left();
				Intent intent = new Intent(PartyS_Left.this, PartylistActivity.class);
				intent.putExtra("mynoteid", mynoteid);
				intent.putExtra("userid", mynoteid);
				startActivity(intent);
				finish();
				

				
			}
		});

	}

	public void confirm(View v) {
		booleans = m_lv.getCheckedItemPositions();
		try {
			for (int i = 0; i < Result_data.length(); i++) {
				if (booleans.get(i)) {
					
					
					JSONObject object = Result_data.getJSONObject(i);
					FriendInfo tweet = new FriendInfo();

					String fr_id = object.getString("Fid");
					
					if (count > 0) {
						array += ",";
					}
					count++;
					if (count > 0) {
						array += fr_id;
					}

					
					// sb.append(fr_id+",");
					Log.e("bbb", "String fr_id = " + fr_id);
				}
			}
			Toast.makeText(getApplicationContext(), array + "/" + count,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static InputStream is = null;
	static String json = "";
	String regId;
	int num;

	public void InviteFR() {
		num = count;
		count = 0;
		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "7"));
			params1.add(new BasicNameValuePair("num", String.valueOf(num)));
			params1.add(new BasicNameValuePair("array", array));
			params1.add(new BasicNameValuePair("pid", partyid));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			
			
			Log.e("bbb", "booleans = " + booleans);
			Log.e("bbb", "array = " + array);
			Log.e("bbb", "count = " + count);
			Log.e("bbb", "num = " + num);
			
			Log.e("bbb", "pid = " + partyid);
			Log.e("bbb", "mynoteid = " + mynoteid);

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

				for (int i = 0; i < obj.length(); i++) {

					JSONObject tjo = obj.getJSONObject(i);
					regId = tjo.getString("pid");
				}
			}
		} catch (Exception e) {

			// TODO: handle exception
		}

	}

	public void setFriendListView() {
		mFriendList.clear();

		try {
			int arrayLength = Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Result_data.getJSONObject(i);
				FriendInfo tweet = new FriendInfo();

				tweet.setFname(object.getString("Fname"));

				tweet.setFpic(object.getString("Fpic"));

				tweet.setFid(object.getString("Fid"));
				// String fr_id = object.getString("Fid");

				mFriendList.add(tweet);
			}

			Log.e("note", "tweet.setFname = " + mFriendList);

			f_adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public LinearLayout fr_header() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fr_header = (LinearLayout) inflater.inflate(R.layout.invitefr_header,
				null);

		return fr_header;
	}

	TextView fname;

	public class FriendAdapter extends ArrayAdapter<FriendInfo> {
		private Context mContext;
		private int mResource;
		private ArrayList<FriendInfo> mFriendList;
		private LayoutInflater mLiInflater = null;
		public ImageLoader imageLoader;

		public FriendAdapter(Context context, int layoutResource,
				ArrayList<FriendInfo> objects) {
			super(context, layoutResource, objects);
			this.mContext = context;
			this.mResource = layoutResource;
			this.mFriendList = objects;
			this.mLiInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.imageLoader = ImageLoader.getInstance();
		}

		public int getCount() {
			return mFriendList.size();
		}

		public FriendInfo getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			FriendInfo fri_info = mFriendList.get(position);

			// View vi = convertView;
			if (convertView == null) {
				convertView = mLiInflater.inflate(mResource, null);
			}
			fname = (TextView) convertView.findViewById(R.id.fname);
			fname.setText(fri_info.getFname());

			fname.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// btnimage.setSelected(true);
					fname.setBackgroundColor(Color.RED);
					System.out.println("asefasefasef" + booleans);
				}
			});

			ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
			imageLoader.displayImage(fri_info.getFpic(), fpic, options);
			// fpic.setOnClickListener(friend_Click);
			fpic.setTag(Integer.valueOf(position));

			return convertView;
		}
	}

	public void patissier_left() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "29"));
			params1.add(new BasicNameValuePair("fid", array));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("pid", partyid));

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

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
