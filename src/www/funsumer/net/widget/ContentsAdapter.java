package www.funsumer.net.widget;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.PartyActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.ContentsInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentsAdapter extends ArrayAdapter<ContentsInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<ContentsInfo> mList;
	private LayoutInflater mInflater;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;

	public ContentsAdapter(Context context, int layoutResource,
			ArrayList<ContentsInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentsInfo contents = mList.get(position);

		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();
		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		if (contents != null) {
			TextView pname = (TextView) convertView.findViewById(R.id.pname);
			if (MainActivity.Mypage.equals("0")) {
				pname.setText(contents.getpname());
			} else if (MainActivity.Mypage.equals("1")) {
				pname.setText(contents.getmypname());
			}

			ImageView ppic = (ImageView) convertView.findViewById(R.id.ppic);
			if (MainActivity.Mypage.equals("0")) {
				imageLoader.displayImage(contents.getppic() + "_m", ppic, options);
			} else if (MainActivity.Mypage.equals("1")) {
				imageLoader.displayImage(contents.getmyppic() + "_m", ppic, options);
			}
			ppic.setOnClickListener(cotentspicClick);
			ppic.setTag(Integer.valueOf(position));
			
		}

		return convertView;
	}
	
	public String getpid;
	final OnClickListener cotentspicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			ContentsInfo fri_info = mList.get(position);
			if (MainActivity.Mypage.equals("0")) {
				getpid = fri_info.getpid();
			} else if (MainActivity.Mypage.equals("1")) {
				getpid = fri_info.getmypid();
			}

			Intent intentput = new Intent(mContext, PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			NoteActivity.userid = MainActivity.mynoteid;
			intentput.putExtra("partyid", getpid);
			v.getContext().startActivity(intentput);
		}
	};

}
