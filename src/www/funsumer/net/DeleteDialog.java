package www.funsumer.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import www.funsumer.net.constants.TweetInfo;
import www.funsumer.net.widget.CustomAdapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeleteDialog extends Dialog implements View.OnClickListener {
	static final float DIMENSIONS_DIFF_PORTRAIT = 40;
	private CustomDialogListener mListener;
	private Context mContext;
	private int mType;

	// private ArrayList<TweetInfo> mTweetList;
	// private static CustomAdapter mAdapter;

	private String mStr;
	private int IMAGE_MAX_SIZE = 1500;

	public static final int TYPE_BASIC_CENCEL_OK = 10;
	public static final int TYPE_BASIC_OK = 20;
	public static final int TYPE_SINGO = 30;
	public static final int TYPE_REPLY_SINGO = 40;
	public static final int TYPE_LOGIN_SUCCESS = 50;
	public static final int TYPE_WRITE_SUCCESS = 60;
	public static final int TYPE_BUY_COMPLETE = 70;
	public static final int TYPE_BUY_COMPLETE2 = 80;
	public static final int TYPE_INPUT_YOUTUBE = 90;
	public static final int TYPE_INPUT_COPY_MUSIC = 100;
	public static final int TYPE_LOGOUT = 110;

	private String mResult;
	private LinearLayout layout_right1;
	private LinearLayout layout_right2;
	private LinearLayout layout_right3;
	private LinearLayout layout_right4;

	private EditText et_name;
	
	int position;
	String articleid;

	// public CustomDialog(Context context, int type, CustomDialogListener
	// listener) {
	// super(context);
	// mContext = context;
	// mListener = listener;
	// mType = type;
	// }

	public DeleteDialog(Context context, int type, CustomDialogListener listener) {
		super(context);
		mContext = context;
		mType = type;
		mListener = listener;
	}

	public static Button btn_del;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = null;

		v = vi.inflate(R.layout.delete_dialog, null);

//		TextView tv_desc = (TextView) v.findViewById(R.id.tv_desc);

		btn_del = (Button) v.findViewById(R.id.btn_del);
		btn_del.setOnClickListener(this);
		// btn_ok.setOnClickListener(NoteActivity.deleteArticle);

		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);

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
	
	public TweetInfo tweet;
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//
		case R.id.btn_cancel: {

			position = CustomAdapter.del_position;
			
			if (NoteActivity.tabnoteid == "1") {
				tweet = NoteActivity.mTweetList.get(position);
				
			} else if (NoteActivity.tabnoteid == "2") {
				tweet = NoteActivity.mTweetList.get(position);
			} else {
				Log.e("ooo", "runnuing 1 ");
				tweet = PartyActivity.mTweetList.get(position);
				Log.e("ooo", "runnuing 2 ");
			}
			articleid = tweet.getArticleid();

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "9"));
				params1.add(new BasicNameValuePair("origin_article", articleid));
				params1.add(new BasicNameValuePair("mynoteid", MainActivity.mynoteid));
				params1.add(new BasicNameValuePair("position", "1"));
				params1.add(new BasicNameValuePair("toid", MainActivity.mynoteid));
				params1.add(new BasicNameValuePair("content", "design"));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
			} catch (Exception e) {
			}

			new CustomDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
					"마이노트로 스크랩하였습니다.").show();
		
			dismiss();
			break;
		}
		case R.id.btn_del: {

			// 여기로 옮겨왔다
			int position = CustomAdapter.del_position;
			
			if (NoteActivity.tabnoteid == "1") {
				tweet = NoteActivity.mTweetList.get(position);
				
			} else if (NoteActivity.tabnoteid == "2") {
				tweet = NoteActivity.mTweetList.get(position);
			} else {
				Log.e("ooo", "runnuing 1 ");
				tweet = PartyActivity.mTweetList.get(position);
				Log.e("ooo", "runnuing 2 ");
			}
			String articleid = tweet.getArticleid();
			
			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "11"));
				params1.add(new BasicNameValuePair("position", "1"));
				params1.add(new BasicNameValuePair("toid", articleid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);

			} catch (Exception e) {
			}

			if (NoteActivity.tabnoteid == "1") {
				NoteActivity.mTweetList.remove(position);
				NoteActivity.mAdapter.notifyDataSetChanged();
			} else if (NoteActivity.tabnoteid == "2") {
				NoteActivity.mTweetList.remove(position);
				NoteActivity.mAdapter.notifyDataSetChanged();
			} else {
				Log.e("ooo", "runnuing3 ");
				PartyActivity.mTweetList.remove(position);
				PartyActivity.mAdapter.notifyDataSetChanged();
			}

			dismiss();
			break;
		}
		}

	}

	private void setSingo(int type) {
		if (type == 1) {
			mResult = "1";
			layout_right1.setVisibility(View.VISIBLE);
			layout_right2.setVisibility(View.GONE);
			layout_right3.setVisibility(View.GONE);
			layout_right4.setVisibility(View.GONE);
		} else if (type == 2) {
			mResult = "2";
			layout_right1.setVisibility(View.GONE);
			layout_right2.setVisibility(View.VISIBLE);
			layout_right3.setVisibility(View.GONE);
			layout_right4.setVisibility(View.GONE);
		} else if (type == 3) {

			mResult = "3";
			layout_right1.setVisibility(View.GONE);
			layout_right2.setVisibility(View.GONE);
			layout_right3.setVisibility(View.VISIBLE);
			layout_right4.setVisibility(View.GONE);
		} else if (type == 4) {

			mResult = "4";
			layout_right1.setVisibility(View.GONE);
			layout_right2.setVisibility(View.GONE);
			layout_right3.setVisibility(View.GONE);
			layout_right4.setVisibility(View.VISIBLE);
		}
	}
}