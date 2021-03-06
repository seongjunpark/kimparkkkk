package www.funsumer.net.widget;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.PartyActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.PartyInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends ArrayAdapter<PartyInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<PartyInfo> mList;
	private LayoutInflater mLiInflater = null;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;
	
//	public static final int ACTIVITY_CREATE = 10;

	public GridAdapter(Context context, int layoutResource, ArrayList<PartyInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.mList = objects;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = ImageLoader.getInstance();
	}

//	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public PartyInfo getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PartyInfo par_info = mList.get(position);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();
		
		if (convertView == null) {
			convertView = mLiInflater.inflate(mResource, null);
		} else {

		}

		
		ImageView ppic = (ImageView) convertView.findViewById(R.id.partyls_pic);
		imageLoader.displayImage(par_info.getPAR_IMAGES(), ppic, options);
		ppic.setOnClickListener(on_PartypicClick);
		ppic.setTag(Integer.valueOf(position));
		
		TextView pname = (TextView) convertView.findViewById(R.id.partyls_name);
		pname.setText(par_info.getBDPNAME());
		
		Log.e("main", "pname = " + pname);
//		try {
//			iv.setImageBitmap((Bitmap) getURLImage(new URL(par_info.getPAR_IMAGES())));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		imageLoader.DisplayImage(PAR_IMAGES[position] , iv);
		
		return convertView;
	}
	
	public static String getpartyid = null;
	final OnClickListener on_PartypicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			PartyInfo par_info = mList.get(position);
			getpartyid = par_info.getPid();
			
			Log.e("party", "getpartyid = " + par_info.getPid());
			Log.e("party", "getpartyid = " + getpartyid);
			
			Intent intentput = new Intent(mContext,
					PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("partyid", getpartyid);
			NoteActivity.tabnoteid = null;
			v.getContext().startActivity(intentput);
		}
	};
	
}
