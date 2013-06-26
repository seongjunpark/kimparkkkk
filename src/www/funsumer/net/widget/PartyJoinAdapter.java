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

import www.funsumer.net.MainActivity;
import www.funsumer.net.PartyJoin;
import www.funsumer.net.R;
import www.funsumer.net.constants.PartyJoinInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PartyJoinAdapter extends ArrayAdapter<PartyJoinInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<PartyJoinInfo> memList;
	private LayoutInflater mInflater;
	public ImageLoader imageloader;
	private String mynoteid;
	private String pid;
	Button btn_invite;
	Button btn_none;
	private DisplayImageOptions options;

	/**
	 * @param context
	 * @param layoutResource
	 * @param objects
	 */
	public PartyJoinAdapter(Context context, int layoutResource,
			ArrayList<PartyJoinInfo> objects, String partyid) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.memList = objects;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageloader = ImageLoader.getInstance();
		pid = partyid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PartyJoinInfo tweet = memList.get(position);

		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();
		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}
			TextView fname = (TextView) convertView.findViewById(R.id.fname);
			fname.setText(tweet.getmname());
			
			
			ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
			imageloader.displayImage(tweet.getmpic(), fpic, options);
			
			btn_invite = (Button)convertView.findViewById(R.id.btn_invite);
			btn_invite.setOnClickListener(on_FrpicClick);
			btn_invite.setTag(Integer.valueOf(position));
			btn_invite.setText("승인");
			
			btn_none = (Button)convertView.findViewById(R.id.btn_none);
			btn_none.setOnClickListener(none_FrpicClick);
			btn_none.setTag(Integer.valueOf(position));
			btn_none.setText("거절");
		
		return convertView;
	}

	
	final OnClickListener on_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			PartyJoinInfo fri_info = memList.get(position);
			String invparty_id = fri_info.getmid();
			
			//go in party
			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);
				
				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "24"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("pid", pid));
				params1.add(new BasicNameValuePair("position", "2"));
				params1.add(new BasicNameValuePair("mid", invparty_id));
				
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				
//				btn_invite.setText("�꾨즺");
				

			} catch (Exception e) {

				// TODO: handle exception
			}
			
			PartyJoin.mInviteInfo.remove(position);
			PartyJoin.mInviteAdapter.notifyDataSetChanged();
		}
		
	};
	final OnClickListener none_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			PartyJoinInfo fri_info = memList.get(position);
			String invparty_id = fri_info.getmid();
			
			//go in party
			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);
				
				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "24"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("pid", pid));
				params1.add(new BasicNameValuePair("position", "3"));
				params1.add(new BasicNameValuePair("mid", invparty_id));
				
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				
//				btn_none.setText("�꾨즺");
				
			} catch (Exception e) {

				// TODO: handle exception
			}
			
			PartyJoin.mInviteInfo.remove(position);
			PartyJoin.mInviteAdapter.notifyDataSetChanged();
		}
		
	};
}
