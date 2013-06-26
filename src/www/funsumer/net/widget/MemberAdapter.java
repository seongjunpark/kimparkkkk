package www.funsumer.net.widget;

import java.util.ArrayList;

import www.funsumer.net.NoteActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.FriendInfo;
import www.funsumer.net.constants.MemberInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberAdapter extends ArrayAdapter<MemberInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<MemberInfo> memList;
	private LayoutInflater mInflater;
	// public ImageLoader imageLoader;
	public MemberLoader memberloader;

	// public PagerAdapterClass pager;

	/**
	 * @param context
	 * @param layoutResource
	 * @param objects
	 */
	public MemberAdapter(Context context, int layoutResource,
			ArrayList<MemberInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.memList = objects;
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
		MemberInfo tweet = memList.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		TextView fname = (TextView) convertView.findViewById(R.id.fname);
		fname.setText(tweet.getParty_Mem_Name());

		ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
		memberloader.DisplayImage(tweet.getParty_Mem_Pic(), fpic);
		fpic.setOnClickListener(on_FrpicClick);
		fpic.setTag(Integer.valueOf(position));

		TextView fshare = (TextView) convertView.findViewById(R.id.fshare);
		if (tweet.getParty_Mem_Party().equals("-1")) {
			fshare.setText("본인입니다.");
		} else {
			fshare.setText("나와 공유한 파티 " + tweet.getParty_Mem_Party() + "개");
		}

		Button fr_already = (Button) convertView.findViewById(R.id.fr_already);
		Button fr_req = (Button) convertView.findViewById(R.id.fr_req);
		if (tweet.getParty_Mem_Party().equals("-1")) {
			fr_already.setVisibility(View.VISIBLE);
			fr_req.setVisibility(View.INVISIBLE);
			fr_already.setText("본인");
		} else {
			Log.e("abc", "tweet.getFstat() = " + tweet.getFstat());
			if (tweet.getFstat().equals("1")) {
				fr_already.setVisibility(View.VISIBLE);
				fr_req.setVisibility(View.INVISIBLE);
				fr_already.setText("친구");
				Log.e("abc", "case 1");
			} else if (tweet.getFstat().equals("4")) {
				fr_already.setVisibility(View.INVISIBLE);
				fr_req.setVisibility(View.VISIBLE);
				fr_req.setText("친구신청");
				Log.e("abc", "case 2");
			}
			// else {
			// fr_already.setVisibility(View.VISIBLE);
			// fr_req.setVisibility(View.INVISIBLE);
			// fr_already.setText("친구");
			// Log.e("abc","case 3");
			// }
		}

		// if ()
		// fr_already.setVisibility(View.VISIBLE);

		// fr_req.setVisibility(View.INVISIBLE);

		return convertView;
	}

	// public static String userid_fromnoteleft = null;
	final OnClickListener on_FrpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			MemberInfo fri_info = memList.get(position);
			String userid_fromnoteleft = fri_info.getParty_Mem_ID();

			Log.e("flist", "tweet.get = " + fri_info.getParty_Mem_ID());
			Log.e("flist", "userid_fromnoteleft = " + userid_fromnoteleft);

			Intent intentput = new Intent(mContext, NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentput.putExtra("userid", userid_fromnoteleft);
			v.getContext().startActivity(intentput);
		}
	};
}
