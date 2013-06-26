package www.funsumer.net.widget;

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

import www.funsumer.net.InviteFriends;
import www.funsumer.net.MainActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.InviteInfo_fr;
import android.content.Context;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class InviteAdapter_fr extends ArrayAdapter<InviteInfo_fr> {
	private Context mContext;
	private int mResource;
	private ArrayList<InviteInfo_fr> memList;
	private LayoutInflater mInflater;
	public ImageLoader imageloader;
	private String mynoteid;
	private DisplayImageOptions options;
	Button btn_invite, btn_none;

	public InviteAdapter_fr(Context context, int layoutResource,
			ArrayList<InviteInfo_fr> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.memList = objects;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageloader = ImageLoader.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InviteInfo_fr tweet = memList.get(position);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}
		TextView fname = (TextView) convertView.findViewById(R.id.fname);
		fname.setText(tweet.getreqme_name());

		Log.e("infr", "tweet.getreqme_name() = " + tweet.getreqme_name());

		ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
		imageloader.displayImage(tweet.getreqme_pic(), fpic, options);

		btn_invite = (Button) convertView.findViewById(R.id.btn_invite);
		btn_invite.setOnClickListener(on_FrpicClick);
		btn_invite.setTag(Integer.valueOf(position));
		btn_invite.setText("수락");

		btn_none = (Button) convertView.findViewById(R.id.btn_none);
		btn_none.setOnClickListener(noacceptClick);
		btn_none.setTag(Integer.valueOf(position));
//		btn_none.setOnTouchListener(noacceptTouch);
		btn_none.setText("거절");

		return convertView;
	}

	// ACCEPT YES
	final OnClickListener on_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			InviteInfo_fr fri_info = memList.get(position);
			String invparty_id = fri_info.getreqme_id();

			if (invparty_id == "0") {
				Log.e("infr", "null point is running~~~ ");
			} else {
				// go in party
				try {

					HttpClient client = new DefaultHttpClient();
					String postURL = "http://funsumer.net/json/";
					HttpPost post = new HttpPost(postURL);

					List params1 = new ArrayList();
					params1.add(new BasicNameValuePair("oopt", "13"));
					params1.add(new BasicNameValuePair("mynoteid", mynoteid));
					params1.add(new BasicNameValuePair("fid", invparty_id));
					params1.add(new BasicNameValuePair("fopt", "1"));

					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
							params1, HTTP.UTF_8);
					post.setEntity(ent);

					HttpResponse responsePOST = client.execute(post);

					HttpEntity resEntity = responsePOST.getEntity();

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				InviteFriends.mInviteInfo.remove(position);
				InviteFriends.mInviteAdapter.notifyDataSetChanged();

			}
		}
	};

	// ACCEPT NO
	final OnClickListener noacceptClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			InviteInfo_fr fri_info = memList.get(position);
			String invparty_id = fri_info.getreqme_id();

			if (invparty_id == "0") {
				Log.e("infr", "null point is running~~~ ");
			} else {
				// go in party
				try {

					HttpClient client = new DefaultHttpClient();
					String postURL = "http://funsumer.net/json/";
					HttpPost post = new HttpPost(postURL);

					List params1 = new ArrayList();
					params1.add(new BasicNameValuePair("oopt", "13"));
					params1.add(new BasicNameValuePair("mynoteid", mynoteid));
					params1.add(new BasicNameValuePair("fid", invparty_id));
					params1.add(new BasicNameValuePair("fopt", "2"));

					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
							params1, HTTP.UTF_8);
					post.setEntity(ent);

					HttpResponse responsePOST = client.execute(post);

					HttpEntity resEntity = responsePOST.getEntity();

					btn_none.setText("완료");

				} catch (Exception e) {
					// TODO: handle exception
				}

				InviteFriends.mInviteInfo.remove(position);
				InviteFriends.mInviteAdapter.notifyDataSetChanged();
			}
		}
	};

	final OnTouchListener noacceptTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// TextView tv = (TextView) mContext.findViewById(R.id.text1);

				btn_none.setText("완료");
			}
			// btn_none.setBackgroundResource(R.drawable.top_touch);
			if (event.getAction() == MotionEvent.ACTION_UP) {
				btn_none.setText("완료");
				// btn_none.setBackgroundResource(R.drawable.stub_image);
			}

			return false;
		}
	};
}
