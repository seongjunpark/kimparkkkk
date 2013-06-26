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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WidepicDialog extends Dialog implements View.OnClickListener {
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

	public WidepicDialog(Context context, int type,
			CustomDialogListener listener) {
		super(context);
		mContext = context;
		mType = type;
		mListener = listener;
	}

	public static Button btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = null;

		v = vi.inflate(R.layout.widepic_dialog, null);

		btn_ok = (Button) v.findViewById(R.id.btn_ok);
		if (NoteActivity.mynoteid.equals(NoteActivity.userid)) {
			btn_ok.setText("노트사진 수정하기");
		} else {
			btn_ok.setText("노트사진 보기");
		}
//		if (NoteActivity.mynoteid.equals(NoteActivity.userid)) {
			btn_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentput = new Intent(v.getContext(),
							ProfileActivity.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intentput.putExtra("mynoteid", NoteActivity.mynoteid);
					intentput.putExtra("userid", NoteActivity.userid);
					v.getContext().startActivity(intentput);
				}
			});
//		}

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
		case R.id.btn_ok: {
			Intent intentput = new Intent(v.getContext(), ProfileActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("mynoteid", NoteActivity.mynoteid);
			intentput.putExtra("userid", NoteActivity.userid);
			v.getContext().startActivity(intentput);

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