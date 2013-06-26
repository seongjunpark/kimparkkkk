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
import org.json.JSONObject;

import www.funsumer.net.constants.TweetInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class SettingDialog extends Dialog implements View.OnClickListener {
	static final float DIMENSIONS_DIFF_PORTRAIT = 40;
	private CustomDialogListener mListener;
	private Context mContext;
	private int mType;
	
	public TweetInfo tweet;

	static String mynoteid = MainActivity.mynoteid;
	private String partyid;
//	String pid;
	
	static InputStream is = null;
	static String json = "";
	public static String num;
	
	public static JSONArray member = null;
	
	public static final int TYPE_BASIC_OK = 20;

	public SettingDialog(Context context, int type,
			CustomDialogListener listener, String partyidd) {
		super(context);
		mContext = context;
		mType = type;
		mListener = listener;
		partyid = partyidd;
	}

	public static Button btn_req_accept, btn_mem_manage, btn_opensetting,
			btn_change_partypic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = null;

		v = vi.inflate(R.layout.setting_dialog, null);

		getData();
		
		
		Log.e("setting", "case 0 =" + num);
		
		btn_req_accept = (Button) v.findViewById(R.id.btn_req_accept);
		if (num.equals("0")) {
			Log.e("setting", "case 1 = " + num);
		} else {
			btn_req_accept.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentput = new Intent(v.getContext(),
							PartyJoin.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intentput.putExtra("partyid", partyid);
					v.getContext().startActivity(intentput);
				}
			});

			Log.e("setting", "case 2");
		}

		btn_mem_manage = (Button) v.findViewById(R.id.btn_mem_manage);

		btn_opensetting = (Button) v.findViewById(R.id.btn_opensetting);

		btn_change_partypic = (Button) v.findViewById(R.id.btn_change_partypic);

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float dimensions = DIMENSIONS_DIFF_PORTRAIT;

		addContentView(v, new LinearLayout.LayoutParams(display.getWidth()
				- ((int) (dimensions * scale + 0.5f)),
				LinearLayout.LayoutParams.WRAP_CONTENT));

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mListener != null)
			mListener.onClose(mType);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//
		// case R.id.btn_req_accept: {
		// Intent intentput = new Intent(v.getContext(), PartyJoin.class);
		// intentput.putExtra("partyid", partyid);
		// intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// v.getContext().startActivity(intentput);
		//
		// dismiss();
		// break;
		// }
		}

	}
	
	public void getData() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "24"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("pid", partyid));
			params1.add(new BasicNameValuePair("position", "1"));

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
				Log.e("setting", "json =" + json);

				JSONArray obj = new JSONArray(json);
				Log.e("setting", "obj" + obj);
				JSONObject tjo = obj.getJSONObject(0);

				num = tjo.getString("num");

				member = tjo.getJSONArray("member");
			}

		} catch (Exception e) {
		}

	}

}