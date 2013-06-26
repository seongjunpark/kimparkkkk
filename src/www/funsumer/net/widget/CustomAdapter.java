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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import www.funsumer.net.CommentDialog;
import www.funsumer.net.CustomDialog;
import www.funsumer.net.DeleteDialog;
import www.funsumer.net.JSONParser;
import www.funsumer.net.MainActivity;
import www.funsumer.net.NoteActivity;
import www.funsumer.net.PartyActivity;
import www.funsumer.net.R;
import www.funsumer.net.constants.CommentInfo;
import www.funsumer.net.constants.TweetInfo;
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
//import www.funsumer.net.MainActivity.PagerAdapterClass;

public class CustomAdapter extends ArrayAdapter<TweetInfo> {
	private Context mContext;
	private int mResource;
	private ArrayList<TweetInfo> mList;
	private LayoutInflater mInflater;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;
	// public PagerAdapterClass pager;

	TextView Article_Like_Num_0;
	TextView Article_Like_Num_1;

	public CustomAdapter(Context context, int layoutResource,
			ArrayList<TweetInfo> objects) {
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
		TweetInfo tweet = mList.get(position);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		if (convertView == null) {
			convertView = mInflater.inflate(mResource, null);
		}

		if (tweet != null) {
			TextView author = (TextView) convertView.findViewById(R.id.author);
			author.setText(tweet.getAuthor());

			TextView articlefrom = (TextView) convertView
					.findViewById(R.id.articlefrom);
			articlefrom.setText(tweet.getArticleFrom());
			articlefrom.setOnClickListener(on_PartyClick);
			articlefrom.setTag(Integer.valueOf(position));

			TextView time = (TextView) convertView.findViewById(R.id.time);
			time.setText(tweet.getArtime());

			TextView article = (TextView) convertView
					.findViewById(R.id.article);
			article.setText(tweet.getArticle());

			Article_Like_Num_0 = (TextView) convertView
					.findViewById(R.id.Article_Like_Num_0);
			// Article_Like_Num_0.setFactory((ViewFactory) this);
			int likenum_bef = Integer.parseInt(tweet.getArticle_Like_Num());
			String likenum_before = Integer.toString(likenum_bef);
			if (tweet.getArticle_Like_Num().equals("0")) {

			} else {
				Article_Like_Num_0.setText("x" + likenum_before + " 캔디");
			}
			Article_Like_Num_0.setOnClickListener(likeClick);
			Article_Like_Num_0.setTag(Integer.valueOf(position));

			// Article_Like_Num_1 = (TextView)
			// convertView.findViewById(R.id.Article_Like_Num_1);
			// int likenum = Integer.parseInt(tweet.getArticle_Like_Num());
			// String likenum2 = Integer.toString(likenum + 1);
			// Article_Like_Num_1.setText(likenum2);

			TextView Article_Comment_Num = (TextView) convertView
					.findViewById(R.id.Article_Comment_Num);
			if (tweet.getArticle_Comment_Num().equals("0")) {

			} else {
				Article_Comment_Num.setText("x" + tweet.getArticle_Comment_Num()
						+ " 댓글");
			}
			Article_Comment_Num.setOnClickListener(commentClick);
			Article_Comment_Num.setTag(Integer.valueOf(position));

			// TextView Article_Scrap_Num = (TextView) convertView
			// .findViewById(R.id.Article_Scrap_Num);
			// Article_Scrap_Num.setText(tweet.getArticle_Comment_Num() +
			// " 스크랩");

			ImageView authorpic = (ImageView) convertView
					.findViewById(R.id.authorpic);
			imageLoader.displayImage(tweet.getAuthorpic(), authorpic, options);
			authorpic.setOnClickListener(on_AuthorpicClick);
			authorpic.setTag(Integer.valueOf(position));

			ImageView arpic = (ImageView) convertView.findViewById(R.id.arpic);
			imageLoader.displayImage(tweet.getArPic(), arpic, options);

			ImageButton likebtn = (ImageButton) convertView
					.findViewById(R.id.btclick1);
			likebtn.setOnClickListener(likeClick);
			likebtn.setTag(Integer.valueOf(position));

			ImageButton commentbtn = (ImageButton) convertView
					.findViewById(R.id.btclick3);
			commentbtn.setOnClickListener(commentClick);
			commentbtn.setTag(Integer.valueOf(position));

			Log.e("ooo", "tabnoteid = " + NoteActivity.tabnoteid);

			ImageButton delete = (ImageButton) convertView
					.findViewById(R.id.delete);
			if (NoteActivity.tabnoteid == "1") {
				delete.setVisibility(convertView.VISIBLE);
				delete.setOnClickListener(deleteClick);
				delete.setTag(Integer.valueOf(position));
			} else {
				if (mynoteid.equals(tweet.getAuthorid())) {
					delete.setVisibility(convertView.VISIBLE);
					delete.setOnClickListener(deleteClick);
					// delete.setOnClickListener(NoteActivity.deleteArticle);
					delete.setTag(Integer.valueOf(position));

					// DeleteDialog.btn_ok.setOnClickListener(NoteActivity.deleteArticle);
					// DeleteDialog.btn_ok.setTag(Integer.valueOf(position));
				} else {
					delete.setVisibility(convertView.INVISIBLE);
				}
			}

			// else if (mynoteid.equals(tweet.getAuthorid())) {
			// Button delete = (Button) convertView.findViewById(R.id.delete);
			// delete.setVisibility(convertView.VISIBLE);
			// delete.setOnClickListener(deleteClick);
			// // delete.setOnClickListener(NoteActivity.deleteArticle);
			// delete.setTag(Integer.valueOf(position));
			//
			// //
			// DeleteDialog.btn_ok.setOnClickListener(NoteActivity.deleteArticle);
			// // DeleteDialog.btn_ok.setTag(Integer.valueOf(position));
			// } else {
			// Button delete = (Button) convertView.findViewById(R.id.delete);
			// delete.setVisibility(convertView.INVISIBLE);
			// }

			// ImageButton scrapbtn = (ImageButton)
			// convertView.findViewById(R.id.btclick2);
			// scrapbtn.setOnClickListener(scrapClick);
			// scrapbtn.setTag(Integer.valueOf(position));
		}

		return convertView;
	}

	// ONCLICK AUTHOR
	public static String authorid = null;
	final OnClickListener on_AuthorpicClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			TweetInfo tweet = mList.get(position);
			authorid = tweet.getAuthorid();

			Intent intentput = new Intent(mContext, NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", authorid);
			v.getContext().startActivity(intentput);

			Log.e("note", "userid= " + authorid);
		}
	};

	// ONCLICK PARTY TEXT
	String isparty;
	public static String belong = null;
	final OnClickListener on_PartyClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			TweetInfo tweet = mList.get(position);
			isparty = tweet.getIsparty();
			belong = tweet.getBelong();

			if (isparty.equals("0")) {
				// CustomAdapter.authorid = belong;
				Intent intentput = new Intent(mContext, NoteActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("userid", belong);
				v.getContext().startActivity(intentput);
			} else if (isparty.equals("1")) {
				// GridAdapter.getpartyid = belong;
				NoteActivity.tabnoteid = null;
				Intent intentput = new Intent(mContext, PartyActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("partyid", belong);
				v.getContext().startActivity(intentput);
			}
		}
	};

	// DELETE BUTTON
	public static int del_position;
	final OnClickListener deleteClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			del_position = (Integer) v.getTag();
			Log.e("ooo", "position is === " + del_position);

			new DeleteDialog(mContext, DeleteDialog.TYPE_BASIC_OK, null).show();

			return;
		}
	};

	int candy_yes_no = 0;
	// ONCLICK LIKE BUTTON
	final OnClickListener likeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			TweetInfo tweet = mList.get(position);
			String articleid = tweet.getArticleid();

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "8"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("aid", articleid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
			} catch (Exception e) {
			}

			// Article_Like_Num_1 = (TextView)
			// convertView.findViewById(R.id.Article_Like_Num_1);
			// int likenum = Integer.parseInt(tweet.getArticle_Like_Num());
			// String likenum2 = Integer.toString(likenum + 1);
			// Article_Like_Num_1.setText(likenum2);

			if (candy_yes_no==0) {
				new CustomDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
						"캔디를 주었습니다.").show();
				candy_yes_no = 1;
			} else {
				new CustomDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
						"캔디를 뺏었습니다.").show();
				candy_yes_no = 0;
			}
		}
	};

	// scrap mynote
	final OnClickListener scrapClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			TweetInfo tweet = mList.get(position);
			String articleid = tweet.getArticleid();

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "9"));
				params1.add(new BasicNameValuePair("origin_article", articleid));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));
				params1.add(new BasicNameValuePair("position", "1"));
				params1.add(new BasicNameValuePair("toid", mynoteid));
				params1.add(new BasicNameValuePair("content", "design"));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);
			} catch (Exception e) {
			}

			new CustomDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
					"scrap!!.").show();
		}
	};

	// ONCLICK COMMENT BUTTON

	public static String articleid = null;
	private ArrayList<CommentInfo> mCommentList;
	private CommentAdapter mAdapter;

	String mynoteid = MainActivity.mynoteid;

	JSONArray gaciAPI = null;
	String Result = null;
	public static String goResult;
	String Article_Comment_Num = null;
	JSONArray Article_Comment = null;

	final OnClickListener commentClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			TweetInfo tweet = mList.get(position);
			articleid = tweet.getArticleid();

			getCommentData(0);

			if (Result.equals("0")) {
				mCommentList = new ArrayList<CommentInfo>();
				mAdapter = new CommentAdapter(mContext, R.layout.comment_item,
						mCommentList);

				setCommentView();

				new CommentDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
						mAdapter).show();
			} else if (Result.equals("1")) {
				mCommentList = new ArrayList<CommentInfo>();
				mAdapter = new CommentAdapter(mContext, R.layout.comment_item,
						mCommentList);

				new CommentDialog(mContext, CommentDialog.TYPE_BASIC_OK, null,
						mAdapter).show();
			}
			return;
		}
	};

	public void getCommentData(int i) {

		JSONParser jParser = new JSONParser();
		if (i == 0) {
			String url = "http://funsumer.net/json/?opt=8&mynoteid=" + mynoteid
					+ "&articleID=" + articleid;
			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				gaciAPI = jsonurl.getJSONArray("gaciAPI");
				JSONObject article_com = gaciAPI.getJSONObject(0);
				Result = article_com.getString("Result");
				if (Result.equals("0")) {
					Article_Comment_Num = article_com
							.getString("Article_Comment_Num");
					Article_Comment = article_com
							.getJSONArray("Article_Comment");
				}
				goResult = Result;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setCommentView() {
		mCommentList.clear();

		try {
			int arrayLength = Article_Comment.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Article_Comment.getJSONObject(i);
				CommentInfo tweet = new CommentInfo();

				tweet.setComment_Name(object.getString("Comment_Name"));
				tweet.setComment_Time(object.getString("Comment_Time")
						.replaceAll("2013-04-", "Apr.")
						.replaceAll("2013-05-", "May.")
						.replaceAll("2013-06-", "Jun.")
						.replaceAll("2013-07-", "Jul.")
						.replaceAll("2013-08-", "Aut.")
						.replaceAll("2013-09-", "Sep.")
						.replaceAll("2013-10-", "Oct.")
						.replaceAll("2013-11-", "Nov.")
						.replaceAll("2013-12-", "Dec.")
						.replaceAll("2014-01-", "Jan.")
						.replaceAll("2014-02-", "Feb.")
						.replaceAll("2014-03-", "Mar.")
						.replaceAll("2014-04-", "Apr.")
						.replaceAll("2014-05-", "May.")
						.replaceAll("2014-06-", "Jun."));
				tweet.setComment_Info(object.getString("Comment_Info"));
				// tweet.setArticle(object.getString("ArInfo").replaceAll("<br>","\n"));

				tweet.setComment_Pic(object.getString("Comment_Pic"));

				tweet.setComment_ID(object.getString("Comment_ID"));
				tweet.setcommentid(object.getString("Comment_aID"));

				mCommentList.add(tweet);
			}

			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
