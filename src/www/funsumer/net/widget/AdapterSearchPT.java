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
import www.funsumer.net.NoteActivity;
import www.funsumer.net.PartyActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.SearchInfoPT;
import android.content.Context;
import android.content.Intent;
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

public class AdapterSearchPT extends ArrayAdapter<SearchInfoPT> {
	private Context mContext;
	private int mResource;
	private ArrayList<SearchInfoPT> memList;
	private LayoutInflater mInflater;
	public ImageLoader imageloader;
	private String mynoteid;
	private DisplayImageOptions options;

	/**
	 * @param context
	 * @param layoutResource
	 * @param objects
	 */
	public AdapterSearchPT(Context context, int layoutResource,
			ArrayList<SearchInfoPT> objects) {
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
		SearchInfoPT tweet = memList.get(position);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}
			TextView fname = (TextView) convertView.findViewById(R.id.fname);
			fname.setText(tweet.getpname());
			fname.setOnClickListener(on_FrpicClick);
			fname.setTag(Integer.valueOf(position));

			
			TextView invite_text = (TextView) convertView.findViewById(R.id.invite_text);
			invite_text.setText("내가 아는 멤버 " + tweet.getpcount() + "명");
			invite_text.setOnClickListener(on_FrpicClick);
			invite_text.setTag(Integer.valueOf(position));
			
			ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
			imageloader.displayImage(tweet.getppic(), fpic, options);
			fpic.setOnClickListener(on_FrpicClick);
			fpic.setTag(Integer.valueOf(position));
			
			btn_invite = (Button)convertView.findViewById(R.id.btn_invite);
			btn_invite.setOnClickListener(on_FrpicClick);
			btn_invite.setTag(Integer.valueOf(position));
			btn_invite.setText("파티구경");
		
		return convertView;
	}

	final OnClickListener on_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			SearchInfoPT fri_info = memList.get(position);
			String invparty_id = fri_info.getpid();
				
			NoteActivity.tabnoteid = null;
			Intent intentput = new Intent(mContext, PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentput.putExtra("partyid", invparty_id);
			v.getContext().startActivity(intentput);
		}
	};
	
	Button btn_invite;
	public static String userid_fromnoteleft = null;
	final OnClickListener partyvisit_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mynoteid = MainActivity.mynoteid;
			int position = (Integer) v.getTag();
			SearchInfoPT fri_info = memList.get(position);
			String invparty_id = fri_info.getpid();
			
			//go in party
			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);
				
				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "12"));
				params1.add(new BasicNameValuePair("pid", invparty_id));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				
				btn_invite.setText("�꾨즺");
				
			} catch (Exception e) {

				// TODO: handle exception
			}
			
			
		}
		
	};
}
