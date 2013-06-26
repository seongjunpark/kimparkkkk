package www.funsumer.net;

import static www.funsumer.net.widget.CustomAdapter.authorid;
import static www.funsumer.net.widget.GridAdapter.getpartyid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import www.funsumer.net.constants.PartyInfo;
import www.funsumer.net.constants.TweetInfo;
import www.funsumer.net.login.ListArrayItem;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.CustomAdapter;
import www.funsumer.net.widget.GridAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class WassupActivity extends Activity {

	private static ViewPager mPager;

	SessionManager session;
	// public ListView m_lv = null;
	// LazyAdapter adapter;

	public ImageLoader imageLoader;

	EditText edit_search;

	static JSONArray graiAPI = null;
	static String Result = null;
	static JSONArray Result_data = null;
	static JSONArray guplAPI = null;

	public static String mynoteid;
	static String userid;
	static String partyid;
	String opt5_url;

	private Cursor c;
	ContentResolver cr;
	int idd;
	String dial1 = "";
	private List<ListArrayItem> items = new ArrayList<ListArrayItem>();
	// String authorid;

	public static ArrayList<TweetInfo> mTweetList;
	public static CustomAdapter mAdapter;
	private static ArrayList<PartyInfo> mPartyList;
	private static GridAdapter gridadapter;

	private static final String SENDER_ID = "147463031498";
	private static final String INSERT_PAGE = "http://funsumer.net/GCM/insert_registration.php";
	private GCMHttpConnect httpConnect = null;
	private GCMHttpConnect.Request httpRequest = new GCMHttpConnect.Request() {

		@Override
		public void OnComplete() {
			// TODO Auto-generated method stub
		}
	};

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wassup_base);

		mynoteid = MainActivity.mynoteid;

		if (mynoteid == null) {
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
		} else {

			mPager = (ViewPager) findViewById(R.id.pager);
			mPager.setAdapter(new PagerAdapterClass(this));

			// setLayout();

			final Button home = (Button) findViewById(R.id.home);
			home.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentput = new Intent(getApplicationContext(),
							MainActivity.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentput);
				}
			});
			home.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						home.setBackgroundResource(R.drawable.icon_home_selected);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						home.setBackgroundResource(R.drawable.icon_home);
					}
					return false;
				}
			});

			final Button btn_one = (Button) findViewById(R.id.btn_one);
			btn_one.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					btn_one_thread thread = new btn_one_thread();
					thread.execute();
				}
			});
			btn_one.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_one.setBackgroundResource(R.drawable.top_touch);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_one.setBackgroundResource(R.drawable.stub_image);
					}
					return false;
				}
			});

			final Button btn_two = (Button) findViewById(R.id.btn_two);
			btn_two.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					btn_two_thread thread = new btn_two_thread();
					thread.execute();
				}
			});
			btn_two.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_two.setBackgroundResource(R.drawable.top_touch);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_two.setBackgroundResource(R.drawable.stub_image);
					}
					return false;
				}
			});

			edit_search = (EditText) findViewById(R.id.edit_search);
			final Button search_glass = (Button) findViewById(R.id.search_glass);
			search_glass.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (edit_search.getText().toString().equals("")) {
					} else {
						Intent intent = new Intent(getApplicationContext(),
								SearchFRorPT.class);
						intent.putExtra("value", edit_search.getText()
								.toString());
						startActivity(intent);
					}
				}
			});
			search_glass.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						search_glass
								.setBackgroundResource(R.drawable.btn_search_selected);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						search_glass
								.setBackgroundResource(R.drawable.btn_search);
					}
					return false;
				}
			});

			// KAKAO IN SLIDE
			final Button btnkakao = (Button) findViewById(R.id.btnkakao);
			btnkakao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),
							InviteKakao.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
			});
			btnkakao.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btnkakao.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btnkakao.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			final Button invite_alarm = (Button) findViewById(R.id.invite_alarm);
			invite_alarm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(getApplicationContext(),
							InviteParty.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
			});
			invite_alarm.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						invite_alarm
								.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						invite_alarm.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			int int_NumberOfInv = Integer.parseInt(MainActivity.NumberOfInv);
			Log.e("ksy", "int_NumberOfInv = " + int_NumberOfInv);
			ImageView icon_rednew_party = (ImageView) findViewById(R.id.icon_rednew_party);
			if (int_NumberOfInv > 0) {
				Log.e("ksy", "icon_rednew_party VISIBLE1");
				icon_rednew_party.setVisibility(View.VISIBLE);
				Log.e("ksy", "icon_rednew_party VISIBLE2");
			} else if (int_NumberOfInv == 0) {
				Log.e("ksy", "icon_rednew_party INVISIBLE1");
				icon_rednew_party.setVisibility(View.INVISIBLE);
				Log.e("ksy", "icon_rednew_party INVISIBLE2");
			}

			final Button invite_fr_alarm = (Button) findViewById(R.id.invite_fr_alarm);
			invite_fr_alarm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),
							InviteFriends.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
			});
			invite_fr_alarm.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						invite_fr_alarm
								.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						invite_fr_alarm.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			int int_NumberOfReq = Integer.parseInt(MainActivity.NumberOfReq);
			Log.e("ksy", "int_NumberOfReq = " + int_NumberOfReq);
			ImageView icon_rednew_fr = (ImageView) findViewById(R.id.icon_rednew_fr);
			if (int_NumberOfReq > 0) {
				Log.e("ksy", "icon_rednew_fr VISIBLE1");
				icon_rednew_fr.setVisibility(View.VISIBLE);
				Log.e("ksy", "icon_rednew_fr VISIBLE2");
			} else if (int_NumberOfReq == 0) {
				Log.e("ksy", "icon_rednew_fr INVISIBLE1");
				icon_rednew_fr.setVisibility(View.INVISIBLE);
				Log.e("ksy", "icon_rednew_fr INVISIBLE1");
			}

			final Button btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
			btn_friendlist.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Notethread thread = new Notethread();
					thread.execute();
				}
			});
			btn_friendlist.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_friendlist
								.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_friendlist.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			final Button btn_partylist = (Button) findViewById(R.id.btn_partylist);
			btn_partylist.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					btn_partylist_thread thread = new btn_partylist_thread();
					thread.execute();
				}
			});
			btn_partylist.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_partylist
								.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_partylist.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			// ACCOUNT
			final Button btn_account = (Button) findViewById(R.id.btn_account);
			btn_account.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),
							Account.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
			});
			btn_account.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_account
								.setBackgroundResource(android.R.color.darker_gray);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_account.setBackgroundResource(00000000);
					}
					return false;
				}
			});

			Animation();
		}
	}

	public static void setCurrentInflateItem(int type) {
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
			return 1;
		}

		public Object instantiateItem(View pager, int position) {
			View v = null;

			if (position == 0) {

				getData(0);

				if (Result.equals("0")) {
					v = (View) mInflater.inflate(R.layout.wassup_center, null);

					ListView m_lv = (ListView) v.findViewById(R.id.list1);

					mTweetList = new ArrayList<TweetInfo>();
					mAdapter = new CustomAdapter(WassupActivity.this,
							R.layout.item, mTweetList);
					m_lv.setAdapter(mAdapter);

					setListView();
				} else if (Result.equals("1")) {
					v = (View) mInflater.inflate(R.layout.wassup_center_none,
							null);

					Button btn_invite_inwassupnone = (Button) v
							.findViewById(R.id.btn_invite_inwassupnone);
					btn_invite_inwassupnone
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											getApplicationContext(),
											InviteKakao.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								}
							});
				}
			} else if (position == 1) {

				// getData(1);
				//
				// if (Result.equals("0")) {
				// v = mInflater.inflate(R.layout.inflate_two, null);
				//
				// ListView m_lv = (ListView) v.findViewById(R.id.list2);
				// m_lv.addHeaderView(my_header02());
				// mTweetList = new ArrayList<TweetInfo>();
				// mAdapter = new CustomAdapter(MainActivity.this,
				// R.layout.item, mTweetList);
				// m_lv.setAdapter(mAdapter);
				// setListView();
				//

				// } else if (Result.equals("1")) {
				// v = (View) mInflater.inflate(R.layout.inflate_one_none,
				// null);
				// }

				// v.findViewById(R.id.iv_two);
				// v.findViewById(R.id.btn_click_2).setOnClickListener(mPagerListener);
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

	public void getData(int i) {

		JSONParser jParser = new JSONParser();
		if (i == 0) {
			opt5_url = "http://funsumer.net/json/?opt=5&afrom=0&mynoteid="
					+ mynoteid;

			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(opt5_url);
				graiAPI = jsonurl.getJSONArray("graiAPI");
				JSONObject resultdata = graiAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (i == 1) {
			if (userid == null) {
				userid = mynoteid;
			} else {
				userid = authorid;
			}
			String url = "http://funsumer.net/json/?opt=5&afrom=1&mynoteid="
					+ mynoteid + "&userid=" + userid;
			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				graiAPI = jsonurl.getJSONArray("graiAPI");
				JSONObject resultdata = graiAPI.getJSONObject(0);
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (i == 2) {
			partyid = getpartyid;
			if (partyid == null) {

				userid = mynoteid;

				String url = "http://funsumer.net/json/?opt=4&mynoteid="
						+ mynoteid + "&userid=" + userid;
				try {
					JSONObject jsonurl = jParser.getJSONFromUrl(url);
					guplAPI = jsonurl.getJSONArray("guplAPI");
					JSONObject resultdata = guplAPI.getJSONObject(0);
					Result = resultdata.getString("Result");
					Result_data = resultdata.getJSONArray("Result_data");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				// partyid = getpartyid;

				String url = "http://funsumer.net/json/?opt=5&afrom=2&mynoteid="
						+ mynoteid + "&partyid=" + partyid;

				try {
					JSONObject jsonurl = jParser.getJSONFromUrl(url);
					graiAPI = jsonurl.getJSONArray("graiAPI");
					JSONObject resultdata = graiAPI.getJSONObject(0);
					Result_data = resultdata.getJSONArray("Result_data");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void setListView() {
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

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.btn_one:
	// setCurrentInflateItem(0);
	// break;
	// case R.id.btn_two:
	// setCurrentInflateItem(1);
	// break;
	// case R.id.btn_three:
	// setCurrentInflateItem(2);
	// break;
	// }
	// }

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

	public static Bitmap getRoundedCornerImage(Bitmap bitmap) {
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
				R.anim.translate_left);
		translateRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.translate_right);
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		translateLeftAnim.setAnimationListener(animListener);
		translateRightAnim.setAnimationListener(animListener);

		logomenu = (ImageView) findViewById(R.id.logo);
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

	public void insertRegistrationID(String id) {
		httpConnect = new GCMHttpConnect(INSERT_PAGE + "?regID=" + id
				+ "&mynoteid=" + mynoteid, httpRequest);
		httpConnect.start();
	}

	int DialogID;

	private class Notethread extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DialogID);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {

			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", mynoteid);
			NoteActivity.tabflistid = "1";
			startActivity(intentput);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onCancelled();
		}

	}

	private class btn_two_thread extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DialogID);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {

			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", mynoteid);
			startActivity(intentput);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onCancelled();
		}

	}

	private class btn_one_thread extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DialogID);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {

			Intent intentput = new Intent(getApplicationContext(),
					PartylistActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("mynoteid", mynoteid);
			intentput.putExtra("userid", mynoteid);
			startActivity(intentput);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onCancelled();
		}

	}

	private class btn_partylist_thread extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DialogID);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Intent intentput = new Intent(getApplicationContext(),
					PartylistActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("mynoteid", mynoteid);
			intentput.putExtra("userid", mynoteid);
			startActivity(intentput);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			dismissDialog(DialogID);
			removeDialog(DialogID);
			super.onCancelled();
		}

	}

	protected Dialog onCreateDialog(int id) {
		if (id == DialogID) {

			ProgressDialog prog = new ProgressDialog(this);
			prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prog.setTitle("");
			prog.setIndeterminate(false);
			prog.setCancelable(true);
			prog.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
				}
			});
			prog.setMessage(getString(R.string.progress_loading));
			return prog;
		} else
			return super.onCreateDialog(id);
	}

}
