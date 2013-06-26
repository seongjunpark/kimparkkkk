package www.funsumer.net;

//import static www.funsumer.net.widget.CustomAdapter.authorid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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

import www.funsumer.net.constants.FriendInfo;
import www.funsumer.net.constants.PartyInfo;
import www.funsumer.net.constants.TweetInfo;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.CustomAdapter;
import www.funsumer.net.widget.FriendAdapter;
import www.funsumer.net.widget.GridAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class NoteFlistActivity extends Activity {

	private ViewPager mPager;

	SessionManager session;

	public ImageLoader imageLoader;

	static String mynoteid = MainActivity.mynoteid;
	static String userid;
	public static String getuserid;
	public static String tabnoteid;
	// String authorid;
	String[] BDFANPIC;
	String[] BDFANID;
	String[] BDFANNAME;

	JSONArray gwlAPI = null;
	JSONArray guflAPI = null;
	String Fan_Result;
	JSONArray Fan_Result_data = null;

	JSONArray graiAPI = null;
	String Result = null;
	JSONArray Result_data = null;
	JSONArray guplAPI = null;
	static JSONArray guiAPI = null;

	private ArrayList<FriendInfo> mFriendList;
	private FriendAdapter f_adapter;
	static ArrayList<TweetInfo> mTweetList;
	static CustomAdapter mAdapter;
	private ArrayList<PartyInfo> mPartyList;
	private GridAdapter gridadapter;

	static ImageLoader imageloader;
	
	static public RelativeLayout askdelete;
	static public Button del_ok;
	static public Button del_can;
	private DisplayImageOptions options;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_base);
		
		tabnoteid = "1";

		userid = mynoteid;

		this.imageLoader = ImageLoader.getInstance();

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(this));
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();
		
//		askdelete = (RelativeLayout) findViewById(R.id.askdelete);
		
//		del_ok = (Button) findViewById(R.id.del_ok);
//		del_can = (Button) findViewById(R.id.del_cancel);
		
		final ImageButton logo = (ImageButton) findViewById(R.id.logo);
		logo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tabnoteid = null;
				Intent intentput = new Intent(NoteFlistActivity.this,
						MainActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentput);
			}
		});
		logo.setOnTouchListener(new OnTouchListener() {
	    	public boolean onTouch(View v, MotionEvent event) {       
	    		if(event.getAction() == MotionEvent.ACTION_DOWN)          
	    			logo.setBackgroundResource(R.drawable.top_touch);        
	    		if(event.getAction() == MotionEvent.ACTION_UP){             
	    			logo.setBackgroundResource(R.drawable.stub_image);         
	    			}          
	    		return false;      
	    		}     
	    	});
		
		final Button btn_one = (Button) findViewById(R.id.btn_one);
		btn_one.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tabnoteid = null;
				Intent intentput = new Intent(NoteFlistActivity.this,
						PartylistActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("mynoteid", mynoteid);
				intentput.putExtra("userid", mynoteid);
				startActivity(intentput);
			}
		});
		btn_one.setOnTouchListener(new OnTouchListener() {
			// ��ư ��ġ�� �̺�Ʈ
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					btn_one.setBackgroundResource(R.drawable.top_touch);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_one.setBackgroundResource(R.drawable.stub_image);
				}
				return false;
			}
		});
		
		final Button btn_three = (Button) findViewById(R.id.btn_three);
		btn_three.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
				Intent intentput = new Intent(NoteFlistActivity.this,
						WassupActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentput);
			}
		});
		btn_three.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					btn_three.setBackgroundResource(R.drawable.top_touch);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_three.setBackgroundResource(R.drawable.stub_image);
				}
				return false;
			}
		});

		Button btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
		btn_friendlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
				Intent intentput = new Intent(getApplicationContext(),
						Friendlist_Activity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentput);
			}
		});

		Button btn_partylist = (Button) findViewById(R.id.btn_partylist);
		btn_partylist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
				Intent intentput = new Intent(NoteFlistActivity.this,
						PartylistActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("mynoteid", mynoteid);
				intentput.putExtra("userid", mynoteid);
				startActivity(intentput);
			}
		});

		Animation();

//		setCurrentInflateItem(1);
	}

	public void setCurrentInflateItem(int type) {
		// type = 1;
		if (type == 0) {
			mPager.setCurrentItem(0);
		} else if (type == 1) {
			mPager.setCurrentItem(1);
		} else {
			mPager.setCurrentItem(2);
		}
	}

	private View.OnClickListener mPagerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((Button) v).getText().toString();
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
					.show();
		}
	};

	public class PagerAdapterClass extends PagerAdapter {
		private LayoutInflater mInflater;

		public PagerAdapterClass(Context c) {
			super();
			mInflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return 3;
		}

		public float getPageWidth(int position) {
			if (position == 0 || position == 2) {
				return 0.85f;
			}
			return 1f;
		}

		public Object instantiateItem(View pager, int position) {
			View v = null;

			if (position == 0) {

				getData(0);

				v = mInflater.inflate(R.layout.note_left, null);

				ListView m_lv1 = (ListView) v.findViewById(R.id.friend_list);
				m_lv1.addHeaderView(noteleft_header());

				mFriendList = new ArrayList<FriendInfo>();
				f_adapter = new FriendAdapter(getApplicationContext(),
						R.layout.noteleft_friendlist_item, mFriendList);
				m_lv1.setAdapter(f_adapter);

				setFriendListView();

			} else if (position == 1) {

				getData(1);

				if (Result.equals("0")) {
					v = mInflater.inflate(R.layout.note_center, null);

					ListView m_lv = (ListView) v.findViewById(R.id.list2);
					m_lv.addHeaderView(my_header02());

					mTweetList = new ArrayList<TweetInfo>();
					mAdapter = new CustomAdapter(NoteFlistActivity.this,
							R.layout.item, mTweetList);
					m_lv.setAdapter(mAdapter);

					setListView();
				} else if (Result.equals("1")) {
					v = mInflater.inflate(R.layout.note_center_none, null);
				}

			} else if (position == 2) {

				getData(2);

				v = mInflater.inflate(R.layout.note_right, null);

				TextView result_Name = (TextView) v
						.findViewById(R.id.note_right_user);
				result_Name.setText(Result_Name + "���� ��Ƽ���");

				GridView gridview = (GridView) v.findViewById(R.id.gridview);

				mPartyList = new ArrayList<PartyInfo>();
				gridadapter = new GridAdapter(NoteFlistActivity.this,
						R.layout.partylist_grid, mPartyList);
				gridview.setAdapter(gridadapter);

				setPartyView();

			}

			((ViewPager) pager).addView(v, 0);

			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// GET DATA
	public void getData(int i) {

		JSONParser jParser = new JSONParser();
		if (i == 0) {
			// gwlAPI
			String fan_url = "http://funsumer.net/json?opt=6&mynoteid="
					+ mynoteid;

			Log.e("note", "fan_url = " + fan_url);

			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(fan_url);
				gwlAPI = jsonurl.getJSONArray("gwlAPI");
				JSONObject resultdata = gwlAPI.getJSONObject(0);
				Fan_Result = resultdata.getString("Result");
				Fan_Result_data = resultdata.getJSONArray("Result_data");

				BDFANPIC = new String[Fan_Result_data.length()];
				BDFANID = new String[Fan_Result_data.length()];
				BDFANNAME = new String[Fan_Result_data.length()];
				for (int i1 = 0; i1 < Fan_Result_data.length(); i1++) {
					JSONObject b = Fan_Result_data.getJSONObject(i1);

					String k = b.getString("Wpic");
					String BDpicc = "http://www.funsumer.net/" + k;

					String BDfid = b.getString("Wid");
					
					String BDfname = b.getString("Wname");

					BDFANPIC[i1] = BDpicc;
					BDFANID[i1] = BDfid;
					BDFANNAME[i1] = BDfname;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// guflAPI
			String url = "http://funsumer.net/json?opt=3&mynoteid=" + mynoteid
					+ "&userid=" + userid;

			Log.e("note", "f url = " + url);

			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				guflAPI = jsonurl.getJSONArray("guflAPI");
				JSONObject resultdata = guflAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (i == 1) {

			// list2 url
			String url = "http://funsumer.net/json/?opt=5&afrom=1&mynoteid="
					+ mynoteid + "&userid=" + userid;
			
			Log.e("note", "opt5 url = " + url);
			
			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				graiAPI = jsonurl.getJSONArray("graiAPI");
				JSONObject resultdata = graiAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// header02 url
			String noteheaderurl = "http://funsumer.net/json/?opt=2"
					+ "&mynoteid=" + mynoteid + "&userid=" + userid;

			try {
				JSONObject json = jParser.getJSONFromUrl(noteheaderurl);
				guiAPI = json.getJSONArray("guiAPI");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (i == 2) {
			// opt4 GET USER PARTY LIST url
			String url = "http://funsumer.net/json/?opt=4&mynoteid=" + mynoteid
					+ "&userid=" + userid;

			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				guplAPI = jsonurl.getJSONArray("guplAPI");
				JSONObject resultdata = guplAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setListView() {
		mTweetList.clear();

		try {
			int arrayLength = Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Result_data.getJSONObject(i);
				TweetInfo tweet = new TweetInfo();

				tweet.setAuthor(object.getString("Author"));
				tweet.setArticleFrom(object.getString("ArticleFrom"));
				tweet.setArtime(object.getString("ArTime")
						.replaceAll("2013-04-", "Apr.")
						.replaceAll("2013-05-", "May.")
						.replaceAll("2013-06-", "Jun."));
				tweet.setArticle(object.getString("ArInfo").replaceAll("<br>",
						"\n"));
				tweet.setArticle_Like_Num(object.getString("Article_Like_Num"));
				tweet.setArticle_Comment_Num(object
						.getString("Article_Comment_Num"));

				tweet.setAuthorpic(object.getString("AuthorPic"));
				tweet.setArPic(object.getString("ArPic"));

				tweet.setAuthorid(object.getString("AuthorID"));
				tweet.setArticleid(object.getString("ArticleID"));

				tweet.setBelong(object.getString("Belong"));
				tweet.setIsparty(object.getString("Isparty"));

				mTweetList.add(tweet);
			}

			mAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setFriendListView() {
		mFriendList.clear();

		try {
			int arrayLength = Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Result_data.getJSONObject(i);
				FriendInfo tweet = new FriendInfo();

				tweet.setFname(object.getString("Fname"));
				tweet.setFparty(object.getString("Fparty"));

				tweet.setFpic(object.getString("Fpic"));

				tweet.setFid(object.getString("Fid"));

				mFriendList.add(tweet);
			}

			f_adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setPartyView() {
		mPartyList.clear();

		try {
			int arrayLength = Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Result_data.getJSONObject(i);
				PartyInfo par_ls = new PartyInfo();

				par_ls.setBDPNAME(object.getString("Pname"));

				par_ls.setPAR_IMAGES(object.getString("Ppic"));

				par_ls.setPid(object.getString("Pid"));

				mPartyList.add(par_ls);
			}

			gridadapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	Bitmap getURLImage(URL url) {
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(conn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeStream(bis);
		try {
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	public Bitmap getRoundedCornerImage(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 50;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	LinearLayout slidingPage01;
	boolean isPageOpen = false;
	// ImageButton openBtn01;
	ImageView logomenu;
	Animation translateLeftAnim;
	Animation translateRightAnim;

	private void Animation() {
		slidingPage01 = (LinearLayout) findViewById(R.id.slidingPage01);

		translateLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.translate_right);
		translateRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.translate_left);
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		translateLeftAnim.setAnimationListener(animListener);
		translateRightAnim.setAnimationListener(animListener);

		logomenu = (ImageView) findViewById(R.id.logomenu);
		logomenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// start animation
				if (isPageOpen) {
					slidingPage01.startAnimation(translateLeftAnim);
				} else {
					slidingPage01.setVisibility(View.VISIBLE);
					slidingPage01.startAnimation(translateRightAnim);
				}
			}
		});
	}

	private class SlidingPageAnimationListener implements AnimationListener {
		public void onAnimationEnd(Animation animation) {
			if (isPageOpen) {
				slidingPage01.setVisibility(View.INVISIBLE);
				logomenu.setTag("Open");
				isPageOpen = false;
			} else {
				logomenu.setTag("Close");
				isPageOpen = true;
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation animation) {
		}
	}

	// NOTE_LEFT HEADER
	public LinearLayout noteleft_header02 = null;
	private LayoutInflater mInflater;

	public LinearLayout noteleft_header() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (mynoteid.equals(userid)) {
			noteleft_header02 = (LinearLayout) mInflater.inflate(R.layout.noteleft_header_my, null);
			
			ImageView fan0 = (ImageView) noteleft_header02.findViewById(R.id.fan0);
			imageloader.displayImage(BDFANPIC[0], fan0, options);
			fan0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intentput = new Intent(NoteFlistActivity.this,
							NoteFlistActivity.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intentput.putExtra("userid", BDFANID[0]);
					startActivity(intentput);
				}
			});
			TextView fanname0 = (TextView) noteleft_header02.findViewById(R.id.fanname0);
			fanname0.setText(BDFANNAME[0]);
			
			ImageView fan1 = (ImageView) noteleft_header02.findViewById(R.id.fan1);
			imageloader.displayImage(BDFANPIC[1], fan1, options);
			fan1.setOnClickListener(on_fanpicClick1);
			TextView fanname1 = (TextView) noteleft_header02.findViewById(R.id.fanname1);
			fanname1.setText(BDFANNAME[1]);
			
			ImageView fan2 = (ImageView) noteleft_header02.findViewById(R.id.fan2);
			imageloader.displayImage(BDFANPIC[2], fan2, options);
			fan2.setOnClickListener(on_fanpicClick2);
			TextView fanname2 = (TextView) noteleft_header02.findViewById(R.id.fanname2);
			fanname2.setText(BDFANNAME[2]);
			
			ImageView fan3 = (ImageView) noteleft_header02.findViewById(R.id.fan3);
			imageloader.displayImage(BDFANPIC[3], fan3, options);
			fan3.setOnClickListener(on_fanpicClick3);
			TextView fanname3 = (TextView) noteleft_header02.findViewById(R.id.fanname3);
			fanname3.setText(BDFANNAME[3]);

			Log.e("note", "BDFANID 0 = " + BDFANID[0]);
			Log.e("note", "BDFANID 1 = " + BDFANID[1]);
			Log.e("note", "BDFANID 2 = " + BDFANID[2]);
			
		} else {
			noteleft_header02 = (LinearLayout) mInflater.inflate(R.layout.noteleft_header, null);
		}

		ImageView note_left_top2 = (ImageView) findViewById(R.id.note_left_top2);

		TextView flist_title = (TextView) findViewById(R.id.flist_title);

		ImageView note_left_top3 = (ImageView) findViewById(R.id.note_left_top3);

		return noteleft_header02;
	}

	final OnClickListener on_fanpicClick1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(NoteFlistActivity.this, NoteFlistActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[1]);
			startActivity(intentput);
		}
	};
	final OnClickListener on_fanpicClick2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(NoteFlistActivity.this, NoteFlistActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[2]);
			v.getContext().startActivity(intentput);
		}
	};
	final OnClickListener on_fanpicClick3 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(NoteFlistActivity.this, NoteFlistActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[3]);
			v.getContext().startActivity(intentput);
		}
	};

	// NOTE CENTER HEADER
	public static RelativeLayout m_header02 = null;

	// private LayoutInflater mInflater;

	public RelativeLayout my_header02() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_header02 = (RelativeLayout) mInflater.inflate(R.layout.my_header02,
				null);

		head_data();

		Button btn_go_noteleft = (Button) m_header02
				.findViewById(R.id.btn_go_noteleft);
		btn_go_noteleft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentInflateItem(0);
			}
		});
		Button btn_makearticle = (Button) m_header02
				.findViewById(R.id.btn_makearticle);
		btn_makearticle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(NoteFlistActivity.this, WriteNote.class);
				intent1.putExtra("position", "1");
				startActivity(intent1);
			}
		});

		Button btn_go_noteright = (Button) m_header02
				.findViewById(R.id.btn_go_noteright);
		btn_go_noteright.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentInflateItem(2);
			}
		});

		return m_header02;
	}

	// HEAD_DATA
	static String ResultinOPT2 = null;
	static String Result_WidePic = null;
	static String Result_ProfilePic = null;
	static String Result_Name = null;
	static String Result_Fnum = null;
	static String Result_Vnum = null;
	static String Result_Vote = null;

	static TextView friendnumber;
	static TextView votenumber;
	static ImageView votenumber_pic;

	static ImageView novote;
	static ImageView yesvote;

	public void head_data() {

		try {
			JSONObject wide = guiAPI.getJSONObject(0);
			ResultinOPT2 = wide.getString("Result");

			Result_WidePic = "http://www.funsumer.net/"
					+ wide.getString("Result_WidePic");
			Result_ProfilePic = "http://www.funsumer.net/"
					+ wide.getString("Result_ProfilePic");

			Result_Name = wide.getString("Result_Name");
			Result_Fnum = wide.getString("Result_Fnum");
			Result_Vnum = wide.getString("Result_Vnum");
			Result_Vote = wide.getString("Result_Vote");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		TextView result_Name = (TextView) m_header02
				.findViewById(R.id.result_Name);
		result_Name.setText(Result_Name);

		friendnumber = (TextView) m_header02.findViewById(R.id.Result_Fnum);
		friendnumber.setText(Result_Fnum);

		votenumber = (TextView) m_header02.findViewById(R.id.Result_Vnum);
		votenumber.setText(Result_Vnum);

		ImageView notepic = (ImageView) m_header02.findViewById(R.id.notepic);
		imageloader.displayImage(Result_WidePic, notepic, options);

		ImageView profilepic = (ImageView) m_header02
				.findViewById(R.id.profilepic);
		imageloader.displayImage(Result_ProfilePic, (profilepic), options);

		votenumber_pic = (ImageView) m_header02.findViewById(R.id.countvote);

		// try {
		// notepic.setImageBitmap((Bitmap) getURLImage(new
		// URL(Result_WidePic)));
		//
		// Bitmap bitmap = (Bitmap) getURLImage(new URL(Result_ProfilePic));
		// profilepic.setImageBitmap(getRoundedCornerImage(bitmap));
		//
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }

		// VOTE ACTION
		novote = (ImageView) m_header02.findViewById(R.id.novote);
		yesvote = (ImageView) m_header02.findViewById(R.id.yesvote);
		Log.e("note", "here is HEADdata.. mynoteid = " + mynoteid
				+ "  userid = " + userid);
		if (mynoteid.equals(userid)) {
			novote.setImageResource(R.drawable.stub_image);
			defaultImage();
			votenumber_pic.setVisibility(View.VISIBLE);
			votenumber.setVisibility(View.VISIBLE);
			/*
			 * novote.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) { Intent intentput = new
			 * Intent(ALL.getAppContext(), Friendlist_Activity.class);
			 * intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * intentput.putExtra("mynoteid", mynoteid);
			 * ALL.getAppContext().startActivity(intentput); } });
			 */
		} else {
			if (Result_Vote.equals("1")) {
				Log.e("note", "111 Result_vote is = " + Result_Vote);
				votenumber_pic.setVisibility(View.VISIBLE);
				votenumber.setVisibility(View.VISIBLE);
				novote.setImageResource(R.drawable.vote_after);
				novote.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						/*
						 * new CustomDialog(ALL.getAppContext(),
						 * CommentDialog.TYPE_BASIC_OK, null,
						 * "�̹� ��ǥ�Ͽ����ϴ�.").show();
						 */
						Toast.makeText(ALL.getAppContext(),
								"�̹� ��ǥ�Ͽ����ϴ�.", Toast.LENGTH_LONG)
								.show();
					}
				});
			} else if (Result_Vote.equals("0")) {
				Log.e("note", "000 Result_vote is = " + Result_Vote);
				votenumber_pic.setVisibility(View.INVISIBLE);
				votenumber.setVisibility(View.INVISIBLE);
				novote.setVisibility(View.VISIBLE);
				yesvote.setVisibility(View.INVISIBLE);
				novote.setImageResource(R.drawable.vote_before);
				novote.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							HttpClient client = new DefaultHttpClient();
							String postURL = "http://funsumer.net/json/";
							HttpPost post = new HttpPost(postURL);

							List params1 = new ArrayList();
							params1.add(new BasicNameValuePair("oopt", "4"));
							params1.add(new BasicNameValuePair("mynoteid",
									mynoteid));
							params1.add(new BasicNameValuePair("userid", userid));

							UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
									params1, HTTP.UTF_8);
							post.setEntity(ent);
							HttpResponse responsePOST = client.execute(post);

							changeImage();
							votenumber_pic.setVisibility(View.VISIBLE);
							votenumber.setVisibility(View.VISIBLE);

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				yesvote.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// new CustomDialog(ALL.getAppContext(),
						// CommentDialog.TYPE_BASIC_OK, null,
						// "�̹� ��ǥ�Ͽ����ϴ�.").show();
						Toast.makeText(ALL.getAppContext(),
								"�̹� ��ǥ�ϼ̽��ϴ�.", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
		}

	}

	public static void changeImage() {
		novote.setVisibility(View.INVISIBLE);
		yesvote.setVisibility(View.VISIBLE);
		yesvote.setImageResource(R.drawable.vote_after);
	}

	public static void defaultImage() {
		novote.setVisibility(View.VISIBLE);
		yesvote.setVisibility(View.INVISIBLE);
	}

//	public final static OnClickListener deleteArticle = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
////			int position = (Integer) v.getTag();
//			
////			CustomAdapter.del_position = (Integer) v.getTag();
//			int position = CustomAdapter.del_position;
//
//			if (mTweetList == null || mTweetList.equals("")) {
//				/*
//				 * new CustomDialog(NoteActivity.this,
//				 * CustomDialog.TYPE_BASIC_OK, null,
//				 * "can't delete this article") .show(); return;
//				 */
//			} else {
//
//				if (mynoteid == userid) {
//
//					TweetInfo tweet = mTweetList.get(position);
//					String articleid = tweet.getArticleid();
//
//					try {
//						HttpClient client = new DefaultHttpClient();
//						String postURL = "http://funsumer.net/json/";
//						HttpPost post = new HttpPost(postURL);
//
//						List params1 = new ArrayList();
//						params1.add(new BasicNameValuePair("oopt", "11"));
//						params1.add(new BasicNameValuePair("position", "1"));
//						params1.add(new BasicNameValuePair("toid", articleid));
//
//						UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
//								params1, HTTP.UTF_8);
//						post.setEntity(ent);
//						HttpResponse responsePOST = client.execute(post);
//
//					} catch (Exception e) {
//					}
//					mTweetList.remove(position);
//					mAdapter.notifyDataSetChanged();
//				} else {
//					/*
//					 * new CustomDialog(NoteActivity.this,
//					 * CustomDialog.TYPE_BASIC_OK, null,
//					 * "can't delete this article") .show(); return;
//					 */
//
//				}
//			}
//		}
//	};

}
