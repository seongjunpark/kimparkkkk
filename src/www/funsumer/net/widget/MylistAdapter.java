package www.funsumer.net.widget;

import java.util.ArrayList;

import www.funsumer.net.MainActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.ContentsInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MylistAdapter extends ArrayAdapter<ContentsInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<ContentsInfo> mList;
	private LayoutInflater mInflater;
	public ImageLoader imageLoader;

	public MylistAdapter(Context context, int layoutResource,
			ArrayList<ContentsInfo> objects) {
		super(context, layoutResource, objects);
		this.mContext = context;
		this.mResource = layoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = new ImageLoader(mContext.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContentsInfo mylist = mList.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		if (mylist != null) {
			TextView pname = (TextView) convertView.findViewById(R.id.pname);
			if (MainActivity.Event.equals("0")) {
				pname.setText(mylist.getpname());
			} else if (MainActivity.Event.equals("1")) {
				pname.setText(mylist.getmypname());
			}

			Log.e("main", "mylist.getpname() = " + mylist.getpname());

			ImageView ppic = (ImageView) convertView.findViewById(R.id.ppic);
			if (MainActivity.Event.equals("0")) {
				imageLoader.DisplayImage(mylist.getppic(), ppic);
			} else if (MainActivity.Event.equals("1")) {
				imageLoader.DisplayImage(mylist.getmyppic(), ppic);
			}

		}

		return convertView;
	}

}
