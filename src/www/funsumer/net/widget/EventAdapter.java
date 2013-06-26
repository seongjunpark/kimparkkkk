package www.funsumer.net.widget;

import java.util.ArrayList;

import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.PartyActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.EventInfo1;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class EventAdapter extends ArrayAdapter<EventInfo1> {
	private Context mContext;
	private int mResource;
	private ArrayList<EventInfo1> mList;
	private LayoutInflater mInflater;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;

	public EventAdapter(Context context, int layoutResource,
			ArrayList<EventInfo1> objects) {
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
		EventInfo1 event = mList.get(position);

		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();
		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		if (event != null) {
			// PARTY NAME
			TextView first_party_name = (TextView) convertView.findViewById(R.id.first_party_name);
			first_party_name.setText(event.getfirst_party_name());
			TextView second_party_name = (TextView) convertView.findViewById(R.id.second_party_name);
			second_party_name.setText(event.getsecond_party_name());
			TextView third_party_name = (TextView) convertView.findViewById(R.id.third_party_name);
			third_party_name.setText(event.getthird_party_name());
			
			// VOTE NUMBER
			TextView first_party_vote = (TextView) convertView.findViewById(R.id.first_party_vote);
			first_party_vote.setText(event.getfirst_party_vote());
			TextView second_party_vote = (TextView) convertView.findViewById(R.id.second_party_vote);
			second_party_vote.setText(event.getsecond_party_vote());
			TextView third_party_vote = (TextView) convertView.findViewById(R.id.third_party_vote);
			third_party_vote.setText(event.getthird_party_vote());
			
			// PARTY PIC
			ImageView first_party_pic = (ImageView) convertView.findViewById(R.id.first_party_pic);
			imageLoader.displayImage(event.getfirst_party_pic(), first_party_pic, options);
			first_party_pic.setOnClickListener(firstpicClick);
			first_party_pic.setTag(Integer.valueOf(position));
			
			ImageView second_party_pic = (ImageView) convertView.findViewById(R.id.second_party_pic);
			imageLoader.displayImage(event.getsecond_party_pic(), second_party_pic, options);
			second_party_pic.setOnClickListener(secondpicClick);
			second_party_pic.setTag(Integer.valueOf(position));
			
			ImageView third_party_pic = (ImageView) convertView.findViewById(R.id.third_party_pic);
			imageLoader.displayImage(event.getthird_party_pic(), third_party_pic, options);
			third_party_pic.setOnClickListener(thirdpicClick);
			third_party_pic.setTag(Integer.valueOf(position));
			
			// TOP PIC
			ImageView first_top_pic = (ImageView) convertView.findViewById(R.id.first_top_pic);
			imageLoader.displayImage(event.getfirst_top_pic(), first_top_pic, options);
			ImageView second_top_pic = (ImageView) convertView.findViewById(R.id.second_top_pic);
			imageLoader.displayImage(event.getsecond_top_pic(), second_top_pic, options);
			ImageView third_top_pic = (ImageView) convertView.findViewById(R.id.third_top_pic);
			imageLoader.displayImage(event.getthird_top_pic(), third_top_pic, options);
		}

		return convertView;
	}
	
	final OnClickListener firstpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			EventInfo1 event_info = mList.get(position);
			String getpid = event_info.getfirst_party_id();

			Intent intentput = new Intent(mContext, PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			NoteActivity.userid = MainActivity.mynoteid;
			intentput.putExtra("partyid", getpid);
			v.getContext().startActivity(intentput);
		}
	};

	final OnClickListener secondpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			EventInfo1 event_info = mList.get(position);
			String getpid = event_info.getsecond_party_id();

			Intent intentput = new Intent(mContext, PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			NoteActivity.userid = MainActivity.mynoteid;
			intentput.putExtra("partyid", getpid);
			v.getContext().startActivity(intentput);
		}
	};
	
	final OnClickListener thirdpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			EventInfo1 event_info = mList.get(position);
			String getpid = event_info.getthird_party_id();

			Intent intentput = new Intent(mContext, PartyActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			NoteActivity.userid = MainActivity.mynoteid;
			intentput.putExtra("partyid", getpid);
			v.getContext().startActivity(intentput);
		}
	};
}
