package www.funsumer.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class makeParty extends Activity {
	private String mynoteid;
	static InputStream is = null;
	static String json = "";
	String userid, array;
	int num;
	String writeName_re;

	public ListView m_lv = null;
	EditText writeName;
	CheckBox fr_check;

	JSONArray guflAPI = null;
	String Result = null;
	JSONArray Result_data = null;

	ArrayList<FriendInfo> mFriendList;
	// FriendAdapter f_adapter;
	InvitetopartyAdapter f_adapter;

	// public LinearLayout fr_header = null;
	SparseBooleanArray booleans;
	static int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeparty);

		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		writeName_re = intent.getStringExtra("writeName_re");

		mynoteid = MainActivity.mynoteid;

		writeName = (EditText) findViewById(R.id.writeName);
		if (writeName_re != null) {
			writeName.setText(writeName_re);
		}
//		InputFilter[] filters = new InputFilter[] { new ByteLengthFilter(24,
//				"KSC5601") };
//		writeName.setFilters(filters);

		Button makeparty = (Button) findViewById(R.id.btn_make);
		makeparty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Result.equals("0")) {
				confirm(v);
				}
				onlyText();

				Log.e("party", "partyid = " + regId);
			}
		});
		JSONParser jParser = new JSONParser();
		String url = "http://funsumer.net/json?opt=3&mynoteid=" + mynoteid
				+ "&userid=" + mynoteid;
		try {
			JSONObject jsonurl = jParser.getJSONFromUrl(url);
			guflAPI = jsonurl.getJSONArray("guflAPI");
			JSONObject resultdata = guflAPI.getJSONObject(0);
			Result = resultdata.getString("Result");
			if (Result.equals("0")) {
			Result_data = resultdata.getJSONArray("Result_data");
			} else {
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Constants_friendlist_databox.BDURL(Result_data);

		try {
			m_lv = (ListView) findViewById(R.id.friendlist1);
			mFriendList = new ArrayList<FriendInfo>();
			f_adapter = new InvitetopartyAdapter(this,
					R.layout.makeparty_flist_item, mFriendList);
			m_lv.setAdapter(f_adapter);
			m_lv.setDividerHeight(0);
			m_lv.setItemsCanFocus(false);
			m_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Result.equals("0")) {
		setFriendListView();
		}
	}

	public void confirm(View v) {

		booleans = m_lv.getCheckedItemPositions();

		try {
			array = "";
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
				}
			}
			Toast.makeText(getApplicationContext(), array + "/" + count,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
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
				tweet.setFparty(object.getString("Fparty"));

				tweet.setFpic(object.getString("Fpic"));

				tweet.setFid(object.getString("Fid"));

				mFriendList.add(tweet);

				Log.e("ooo", "tweet.setFparty = " + object.getString("Fparty"));
			}

			f_adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class ByteLengthFilter implements InputFilter {

		private String mCharset;
		protected int mMaxByte;

		public ByteLengthFilter(int maxbyte, String charset) {
			this.mMaxByte = maxbyte;
			this.mCharset = charset;
		}

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			String expected = new String();
			expected += dest.subSequence(0, dstart);
			expected += source.subSequence(start, end);
			expected += dest.subSequence(dend, dest.length());
			int keep = calculateMaxLength(expected)
					- (dest.length() - (dend - dstart));
			if (keep < 0) {
				keep = 0;
			}
			int Rekeep = plusMaxLength(dest.toString(), source.toString(),
					start);

			if (keep <= 0 && Rekeep <= 0) {
				return "";

			} else if (keep >= end - start) {
				return null;
			} else {
				if (dest.length() == 0 && Rekeep <= 0) {
					return source.subSequence(start, start + keep);
				} else if (Rekeep <= 0) {
					return source.subSequence(start, start
							+ (source.length() - 1));
				} else {
					return source.subSequence(start, start + Rekeep);
				}
			}
		}

		protected int plusMaxLength(String expected, String source, int start) {
			int keep = source.length();
			int maxByte = mMaxByte - getByteLength(expected.toString());

			while (getByteLength(source.subSequence(start, start + keep)
					.toString()) > maxByte) {
				keep--;
			}
			;
			return keep;
		}

		protected int calculateMaxLength(String expected) {
			int expectedByte = getByteLength(expected);
			if (expectedByte == 0) {
				return 0;
			}
			return mMaxByte - (getByteLength(expected) - expected.length());
		}

		private int getByteLength(String str) {
			try {
				return str.getBytes(mCharset).length;
			} catch (UnsupportedEncodingException e) {

			}
			return 0;
		}
	}

	String regId;

	public void onlyText() {
		num = count;
		count = 0;
		if (writeName.getText().toString().equals("")) {

			new CustomDialog(makeParty.this, CustomDialog.TYPE_BASIC_OK, null,
					"파티명을 입력해주세요.").show();
//			return;

		} else if (writeName.getText().toString().length() > 12) {
			new CustomDialog(makeParty.this, CustomDialog.TYPE_BASIC_OK, null,
					"파티이름은 12자를 넘을 수 없습니다. ").show();

//			return;
		} else {

			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "6"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("array", array));
				params1.add(new BasicNameValuePair("num", String.valueOf(num)));
				params1.add(new BasicNameValuePair("newname", writeName
						.getText().toString()));

				System.out.println("iahiashefa03" + "//" + params1);

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
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
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
			
			Intent intentput = new Intent(makeParty.this,
					PartyActivity.class);
			intentput.putExtra("partyid", regId);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intentput);
			
			finish();
		}

	}
	// TextView fname;
	// public class FriendAdapter extends ArrayAdapter<FriendInfo> {
	// private Context mContext;
	// private int mResource;
	// private ArrayList<FriendInfo> mFriendList;
	// private LayoutInflater mLiInflater = null;
	// public ImageLoader imageLoader;
	//
	// public FriendAdapter(Context context, int layoutResource,
	// ArrayList<FriendInfo> objects) {
	// super(context, layoutResource, objects);
	// this.mContext = context;
	// this.mResource = layoutResource;
	// this.mFriendList = objects;
	// this.mLiInflater = (LayoutInflater) mContext
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// this.imageLoader = new ImageLoader(mContext.getApplicationContext());
	// }
	//
	// public int getCount() {
	// return mFriendList.size();
	// }
	//
	// public FriendInfo getItem(int position) {
	// return null;
	// }
	//
	// public long getItemId(int position) {
	// return 0;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// FriendInfo fri_info = mFriendList.get(position);
	//
	// // View vi = convertView;
	// if (convertView == null) {
	// convertView = mLiInflater.inflate(mResource, null);
	// }
	// fname = (TextView) convertView.findViewById(R.id.fname);
	// fname.setText(fri_info.getFname());
	//
	// fr_check = (CheckBox)convertView.findViewById(R.id.fr_check);
	// fr_check.setChecked(((ListView)parent).isItemChecked(position));
	// fr_check.setFocusable(false);
	// fr_check.setClickable(false);
	//
	// TextView fshare = (TextView) convertView.findViewById(R.id.fshare);
	// fshare.setText("나와 공유한 파티 " + fri_info.getFparty() + "개");
	//
	// ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
	// imageLoader.DisplayImage(fri_info.getFpic(), fpic);
	// // fpic.setOnClickListener(friend_Click);
	// fpic.setTag(Integer.valueOf(position));
	//
	// return convertView;
	// }
	// }

}
