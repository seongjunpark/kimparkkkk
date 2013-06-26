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

import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.CommentInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class CommentAdapter extends ArrayAdapter<CommentInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<CommentInfo> mList;
	private LayoutInflater mInflater;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;

	public CommentAdapter(Context context, int layoutResource,
			ArrayList<CommentInfo> objects) {
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
		CommentInfo tweet = mList.get(position);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		if (tweet != null) {
			TextView comment = (TextView) convertView
					.findViewById(R.id.comment);
			comment.setText(tweet.getComment_Name());

			TextView comment_time = (TextView) convertView
					.findViewById(R.id.comment_time);
			comment_time.setText(tweet.getComment_Time());

			TextView comment_info = (TextView) convertView
					.findViewById(R.id.comment_info);
			comment_info.setText(tweet.getComment_Info());

			ImageView commentpic = (ImageView) convertView
					.findViewById(R.id.commentpic);
			imageLoader.displayImage(tweet.getComment_Pic(), commentpic, options);
			commentpic.setOnClickListener(on_CommentAuthorpicClick);
			commentpic.setTag(Integer.valueOf(position));

			if (MainActivity.mynoteid.equals(tweet.getComment_ID())) {
				ImageButton comment_del = (ImageButton) convertView
						.findViewById(R.id.comment_del);
				comment_del.setVisibility(View.VISIBLE);
				comment_del.setOnClickListener(deleteComment);
				comment_del.setTag(Integer.valueOf(position));
			} else {
				ImageButton comment_del = (ImageButton) convertView
						.findViewById(R.id.comment_del);
				comment_del.setVisibility(View.INVISIBLE);
			}

			// �ؽ�Ʈ ����
			// time.setText(Html.fromHtml(tweet.getArticle()));
		}

		return convertView;
	}

	String commentid;
	final OnClickListener on_CommentAuthorpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			CommentInfo tweet = mList.get(position);
			commentid = tweet.getComment_ID();

			Intent intentput = new Intent(mContext, NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", commentid);
			v.getContext().startActivity(intentput);

			Log.e("comment", "authorid On Click CustomAdapter = " + commentid);
		}
	};
	// delete comment
	final OnClickListener deleteComment = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			CommentInfo tweet = mList.get(position);
			String commentids = tweet.getcommentid();

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "11"));
				params1.add(new BasicNameValuePair("position", "2"));
				params1.add(new BasicNameValuePair("toid", commentids));

				System.out.println("aefaseasefas" + params1);

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);

			} catch (Exception e) {
			}
			mList.remove(position);
			CommentAdapter.this.notifyDataSetChanged();
		}
	};

}
