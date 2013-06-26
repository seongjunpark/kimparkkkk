package www.funsumer.net.widget;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import www.funsumer.net.ALL;
import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.FriendInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
//import www.funsumer.net.MainActivity.PagerAdapterClass;

public class FriendAdapter extends ArrayAdapter<FriendInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<FriendInfo> fList;
	private LayoutInflater mInflater;
	public ImageLoader imageloader;
	public MemberLoader memberloader;

	// public PagerAdapterClass pager;

	/**
	 * @param context
	 * @param layoutResource
	 * @param objects
	 */
	public FriendAdapter(Context context, int layoutResource,
			ArrayList<FriendInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.fList = objects;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.memberloader = new MemberLoader(mContext.getApplicationContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendInfo tweet = fList.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		TextView fname = (TextView) convertView.findViewById(R.id.fname);
		fname.setText(tweet.getFname());

		ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
		memberloader.DisplayImage(tweet.getFpic(), fpic);
		fpic.setOnClickListener(on_FrpicClick);
		fpic.setTag(Integer.valueOf(position));

		TextView fshare = (TextView) convertView.findViewById(R.id.fshare);
		fshare.setText("나와 공유한 파티 " + tweet.getFparty() + "개");

		Button fr_already = (Button) convertView.findViewById(R.id.fr_already);
		Button fr_req = (Button) convertView.findViewById(R.id.fr_req);
		fr_already.setTag(Integer.valueOf(position));
		if (tweet.getFvote().equals("0")) {
			fr_already.setVisibility(View.VISIBLE);
			fr_req.setVisibility(View.INVISIBLE);
			fr_already.setFocusable(false);
			fr_already.setClickable(false);
			fr_already.setOnClickListener(noteleft_voteClick);
		} else {
			fr_already.setVisibility(View.INVISIBLE);
			fr_req.setVisibility(View.VISIBLE);
			fr_req.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(ALL.getAppContext(), "이미 투표하였습니다.",
							Toast.LENGTH_SHORT).show();
				}
			});
		}

		// fr_already.setOnTouchListener(noteleft_voteTouch);

		// }

		Log.e("note", "fname = " + tweet.getFname());

		return convertView;
	}

	// public static String userid_fromnoteleft = null;
	final OnClickListener on_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			FriendInfo fri_info = fList.get(position);
			String userid_fromnoteleft = fri_info.getFid();

			Log.e("flist", "tweet.get = " + fri_info.getFid());
			Log.e("flist", "userid_fromnoteleft = " + userid_fromnoteleft);

			Intent intentput = new Intent(mContext, NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentput.putExtra("userid", userid_fromnoteleft);
			v.getContext().startActivity(intentput);
		}
	};

	final OnClickListener noteleft_voteClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Button fr_already = (Button) v.findViewById(R.id.fr_already);
			fr_already.setBackgroundResource(R.drawable.btn_cancle2);
			fr_already.setText("투표완료");
			fr_already.setPadding(15, 0, 15, 0);
			fr_already.setTextColor(Color.parseColor("#000000"));

			String mynoteid = MainActivity.mynoteid;

			int position = (Integer) v.getTag();
			System.out.println("ihasefasefase" + position);
			FriendInfo fri_info = fList.get(position);
			String userid_fromnoteleft = fri_info.getFid();

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "4"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("userid",
						userid_fromnoteleft));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// fr_req.setVisibility(View.VISIBLE);
		}
	};

	final OnTouchListener noteleft_voteTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			Button fr_already = (Button) v.findViewById(R.id.fr_already);

			if (event.getAction() == MotionEvent.ACTION_DOWN)
				fr_already.setBackgroundResource(R.drawable.btn_cancle3);
			if (event.getAction() == MotionEvent.ACTION_UP) {
				fr_already.setBackgroundResource(R.drawable.btn_cancle3);
			}
			return false;
		}
	};
}
