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
//import www.funsumer.net.MainActivity.PagerAdapterClass;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InvitetopartyAdapter extends ArrayAdapter<FriendInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<FriendInfo> mFriendList;
	private LayoutInflater mLiInflater = null;
//	public ImageLoader imageLoader;
	public MemberLoader memberloader;

	public InvitetopartyAdapter(Context context, int layoutResource,
			ArrayList<FriendInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.mFriendList = objects;
		this.mLiInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.memberloader = new MemberLoader(mContext.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendInfo fri_info = mFriendList.get(position);

		// View vi = convertView;
		if (convertView == null) {
			convertView = mLiInflater.inflate(mResource, null);
		}
		TextView fname = (TextView) convertView.findViewById(R.id.fname);
		fname.setText(fri_info.getFname());
		
		CheckBox fr_check = (CheckBox)convertView.findViewById(R.id.fr_check);
		fr_check.setChecked(((ListView)parent).isItemChecked(position));
		fr_check.setFocusable(false);
		fr_check.setClickable(false);

		TextView fshare = (TextView) convertView.findViewById(R.id.fshare);
		fshare.setText("나와 공유한 파티 " + fri_info.getFparty() + "개");

		ImageView fpic = (ImageView) convertView.findViewById(R.id.fpic);
		memberloader.DisplayImage(fri_info.getFpic(), fpic);
		fpic.setTag(Integer.valueOf(position));

		return convertView;
	}
}
