package www.funsumer.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
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

import www.funsumer.net.constants.ContentsInfo;
import www.funsumer.net.constants.EventInfo1;
import www.funsumer.net.login.AlertDialogManager;
import www.funsumer.net.login.ListArrayItem;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.ContentsAdapter;
import www.funsumer.net.widget.EventAdapter;
import www.funsumer.net.widget.gsHttpConnect;
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.ContactsContract;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MainActivity extends Activity {

	private static ViewPager mPager;

	SessionManager session;
	// public ListView m_lv = null;
	// LazyAdapter adapter;

	public ImageLoader imageloader;

	JSONArray user_party, con_party;
	public static String Mypage = "0";

	public Button switch_cotentslist_left, switch_cotentslist_right;

	JSONArray first, second, third;

	static JSONArray graiAPI = null;
	static String Result = null;
	static JSONArray Result_data = null;
	static JSONArray guplAPI = null;

	public static String mynoteid;
	public static String Event, Alert, school;
	static String userid;
	static String partyid;
	String opt5_url;
	private String version;
	public String strFormat_day;
	public String strFormat_hour;
	public String strFormat_min;
	public String strFormat_second;

	String best_first_party_id, best_first_party_name, best_first_party_pic,
			best_first_party_vote, best_first_top_id, best_first_top_name,
			best_first_top_pic;
	String best_second_party_id, best_second_party_name, best_second_party_pic,
			best_second_party_vote, best_second_top_id, best_second_top_name,
			best_second_top_pic;
	String best_third_party_id, best_third_party_name, best_third_party_pic,
			best_third_party_vote, best_third_top_id, best_third_top_name,
			best_third_top_pic;

	public static String NumberOfReq, NumberOfInv;

	int interval_int;

	private Handler handler = new Handler();
	Timer timer;

	InputStream is = null;
	String json = "";

	private Cursor c;
	ContentResolver cr;
	int idd;
	String dial1 = "";
	private List<ListArrayItem> items = new ArrayList<ListArrayItem>();
	// String authorid;
	String one;

	public ArrayList<EventInfo1> mEventList;
	public EventAdapter eventAdapter;
	public ArrayList<ContentsInfo> mConList;
	public ContentsAdapter contentsAdapter;
	// public ArrayList<ContentsInfo> mMyList;
	// public MylistAdapter mylistAdapter;
	private DisplayImageOptions options;

	private static final String SENDER_ID = "147463031498";
	private static final String INSERT_PAGE = "http://funsumer.net/GCM/insert_registration.php";
	private GCMHttpConnect httpConnect = null;
	private GCMHttpConnect.Request httpRequest = new GCMHttpConnect.Request() {

		@Override
		public void OnComplete() {
			// TODO Auto-generated method stub
		}
	};
	EditText edit_search;
	int state;

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		super.onCreate(savedInstanceState);

		this.imageloader = ImageLoader.getInstance();

		ALL myApp = (ALL) getApplicationContext();
		state = myApp.getState();

		if (state == 0) {
			Intent loadingintent = new Intent(getApplicationContext(),
					LoadingActivity.class);
			loadingintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(loadingintent, 100);
			myApp.setState(5);
			state = myApp.getState();
		}

		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		HashMap<String, String> user = session.getUserDetails();
		mynoteid = user.get(SessionManager.KEY_NAME);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.stub_image)
				.showImageForEmptyUri(R.drawable.image_for_empty_url)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20)).build();

		// eventnumber = user.get(SessionManager.KEY_EVENT);

		if (mynoteid == null) {
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
		} else {

			final String regID = GCMRegistrar.getRegistrationId(this);
			if (state == 5) {
				GCMRegistrar.register(this, SENDER_ID);
				insertRegistrationID(regID);
				idd = 0;
				cr = getContentResolver();
				c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
						null, null);
				// send phone number!!
				//gsHttpConnect com = new gsHttpConnect();
				//GetContact contact = new GetContact();
				//contact.execute(c);
				myApp.setState(10);
			}

			AlertDialogManager alert = new AlertDialogManager();

			getData(0);

			if (Event.equals("0")) {
				setContentView(R.layout.main_0);

				if (Alert.equals("0")) {

				} else {
					new SchoolDialog(MainActivity.this,
							SchoolDialog.TYPE_BASIC_OK, null).show();
				}

				getData(1);

				try {
					HttpClient client = new DefaultHttpClient();
					String postURL = "http://funsumer.net/json/";
					HttpPost post = new HttpPost(postURL);

					List params = new ArrayList();
					params.add(new BasicNameValuePair("version_param", "1"));

					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);
					post.setEntity(ent);

					HttpResponse responsePOST = client.execute(post);

					HttpEntity resEntity = responsePOST.getEntity();
					is = resEntity.getContent();

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "euc-kr"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						json = sb.toString();
					} catch (Exception e) {
						Log.e("Buffer Error",
								"Error converting result " + e.toString());
					}

					if (resEntity != null) {
						JSONArray obj = new JSONArray(json);
						JSONObject obj2 = obj.getJSONObject(0);
						version = obj2.getString("version");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				Log.e("main", "version = " + version);

				if (version.equals("1.0.6")) {
				} else {
					// alert.showAlertDialog(MainActivity.this, "업데이트 하세요", "",
					// false);
					new UpdateDialog(MainActivity.this,
							UpdateDialog.TYPE_BASIC_OK, null).show();
				}

				// GET DAT START

				GridView mainlist = (GridView) findViewById(R.id.maingrid);
				// mainlist.addHeaderView(main_header());

				mConList = new ArrayList<ContentsInfo>();
				contentsAdapter = new ContentsAdapter(MainActivity.this,
						R.layout.main_contentslist_grid, mConList);
				// mylistAdapter = new MylistAdapter(MainActivity.this,
				// R.layout.main_mylist_grid, mConList);
				mainlist.setAdapter(contentsAdapter);

				if (Mypage.equals("0")) {
					setContentsView();

				} else if (Mypage.equals("1")) {
					setMyView();
				}

				switch_cotentslist_left = (Button) findViewById(R.id.switch_cotentslist_left);
				switch_cotentslist_right = (Button) findViewById(R.id.switch_cotentslist_right);
				switch_cotentslist_left
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (Mypage.equals("1")) {
									Mypage = "0";
									setContentsView();
									switch_cotentslist_left
											.setBackgroundResource(R.drawable.btn_contents_left_selected);
									switch_cotentslist_left.setTextColor(Color
											.parseColor("#ffffff"));
									switch_cotentslist_right
											.setBackgroundResource(R.drawable.btn_contents_right);
									switch_cotentslist_right.setTextColor(Color
											.parseColor("#000000"));
								} else {
								}
							}
						});

				switch_cotentslist_right
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (Mypage.equals("0")) {
									Mypage = "1";
									setMyView();
									switch_cotentslist_left
											.setBackgroundResource(R.drawable.btn_contents_left);
									switch_cotentslist_left.setTextColor(Color
											.parseColor("#000000"));
									switch_cotentslist_right
											.setBackgroundResource(R.drawable.btn_contents_right_selected);
									switch_cotentslist_right.setTextColor(Color
											.parseColor("#ffffff"));
								} else {
								}

							}
						});

				Button makeparty = (Button) findViewById(R.id.makeparty);
				makeparty.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(),
								makeParty.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});

			} else if (Event.equals("1")) {
				Log.e("main", "YES EVENT");
				setContentView(R.layout.main);
				try {
					HttpClient client = new DefaultHttpClient();
					String postURL = "http://funsumer.net/json/";
					HttpPost post = new HttpPost(postURL);

					List params = new ArrayList();
					params.add(new BasicNameValuePair("version_param", "1"));

					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);
					post.setEntity(ent);

					HttpResponse responsePOST = client.execute(post);

					HttpEntity resEntity = responsePOST.getEntity();
					is = resEntity.getContent();

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "euc-kr"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						json = sb.toString();
					} catch (Exception e) {
						Log.e("Buffer Error",
								"Error converting result " + e.toString());
					}

					if (resEntity != null) {
						JSONArray obj = new JSONArray(json);

						JSONObject obj2 = obj.getJSONObject(0);
						version = obj2.getString("version");

						Log.e("version", "version = " + version);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (version.equals("1.0.6")) {
				} else {
					// alert.showAlertDialog(MainActivity.this, "업데이트 하세요", "",
					// false);
					new UpdateDialog(MainActivity.this,
							UpdateDialog.TYPE_BASIC_OK, null).show();
				}

				Log.e("main", "state = " + state);

				timer = new Timer();
				timer.schedule(new UpdateTimerTask(), 1000, 1000);

				getData(2);

				ListView mainlist = (ListView) findViewById(R.id.mainlist);
				mainlist.addHeaderView(main_header());

				mEventList = new ArrayList<EventInfo1>();
				eventAdapter = new EventAdapter(MainActivity.this,
						R.layout.mainitem, mEventList);
				mainlist.setAdapter(eventAdapter);

				setEventView();
				headData();

			}

			final Button btn_one = (Button) findViewById(R.id.btn_one);
			if (Event.equals("0")) {
				btn_one.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Mypage = "1";

						setMyView();

						switch_cotentslist_left
								.setBackgroundResource(R.drawable.btn_contents_left);
						switch_cotentslist_left.setTextColor(Color
								.parseColor("#000000"));
						switch_cotentslist_right
								.setBackgroundResource(R.drawable.btn_contents_right_selected);
						switch_cotentslist_right.setTextColor(Color
								.parseColor("#ffffff"));

						// setMyView();
						// btn_one_thread thread = new btn_one_thread();
						// thread.execute();
					}

				});
			} else if (Event.equals("1")) {
				btn_one.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						btn_one_thread thread = new btn_one_thread();
						thread.execute();
					}

				});
			}
			btn_one.setOnTouchListener(new OnTouchListener() {
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
					 

					/*Intent intentput = new Intent(getApplicationContext(),
							NoteActivity.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intentput.putExtra("userid", mynoteid);
					startActivity(intentput);*/
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

			final Button btn_three = (Button) findViewById(R.id.btn_three);
			btn_three.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intentput = new Intent(getApplicationContext(),
							WassupActivity.class);
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentput);
				}
			});
			btn_three.setOnTouchListener(new OnTouchListener() {
				// 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						btn_three.setBackgroundResource(R.drawable.top_touch);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						btn_three.setBackgroundResource(R.drawable.stub_image);
					}
					return false;
				}
			});

			// SLIDE MENU
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

			Log.e("ksy", "NumberOfInv = " + NumberOfInv);
			int int_NumberOfInv = Integer.parseInt(NumberOfInv);
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

			Log.e("ksy", "NumberOfReq = " + NumberOfReq);
			int int_NumberOfReq = Integer.parseInt(NumberOfReq);
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
					Noteflistthread thread = new Noteflistthread();
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
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

	TextView text_day;
	TextView text_hour;
	TextView text_min;
	TextView text_second;

	int reday;
	int rehour = 1;
	int remin = 5;
	int resec = 5;

	int day = 0;
	private int hour = 0;
	private int minute = 0;
	private int seconds = 0;

	class UpdateTimerTask extends TimerTask {
		@Override
		public void run() {
			handler.post(new Runnable() {
				public void run() {
					// t100ms--;
					// if (ret100 + t100ms == 0) {
					// t100ms = 59 - ret100;

					resec = interval_int % 60;
					remin = interval_int / 60 % 60;
					rehour = interval_int / 60 / 60 % 24;
					reday = interval_int / 60 / 60 / 24;

					seconds--;
					if (resec + seconds == -1) {
						seconds = 59 - resec;

						minute--;
						if (remin + minute == -1) {
							minute = 59 - remin;

							hour--;
							if (rehour + hour == -1)
								hour = 23 - rehour;
						}
					}

					// Log.e("main", "-----------");
					// Log.e("main", "remin = " + remin);
					// Log.e("main", "rehour = " + rehour);
					// Log.e("main", "reday = " + reday);

					text_day = (TextView) main_header
							.findViewById(R.id.timeclock_day);
					text_hour = (TextView) main_header
							.findViewById(R.id.timeclock_hour);
					text_min = (TextView) main_header
							.findViewById(R.id.timeclock_min);
					text_second = (TextView) main_header
							.findViewById(R.id.timeclock_second);
					if (interval_int < 10) {
						strFormat_day = String.format("%d", 0);
						strFormat_hour = String.format("%02d", 0);
						strFormat_min = String.format("%02d", 0);
						strFormat_second = String.format("%d", 0);
					} else {
						strFormat_day = String.format("%d", reday);
						strFormat_hour = String.format("%02d", rehour + hour);
						strFormat_min = String.format("%02d", remin + minute);
						strFormat_second = String.format("%d", resec + seconds);
					}
					text_day.setText(strFormat_day);
					text_hour.setText(strFormat_hour);
					text_min.setText(strFormat_min);
					text_second.setText(strFormat_second);

					// 100ms 후에 doUpdateTimer라는 Runnable이 실행되도록 한다.
					// handler.postDelayed(UpdateTimerTask, 100);
				}
			});
		}
	}

	private class GetContact extends AsyncTask<Cursor, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(Cursor... params) {

			// TODO Auto-generated method stub
			readContacts();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	public void readContacts() {
		gsHttpConnect com = new gsHttpConnect();
		while (c.moveToNext()) {
			// 洹몃９
			int group = c
					.getInt(c
							.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP));
			if (group != 1)
				continue;

			// ID
			String id = c.getString(c
					.getColumnIndex(ContactsContract.Contacts._ID));

			// �쒖�紐�
			String Name = c.getString(c
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			// �꾪솕
			if (Integer
					.parseInt(c.getString(c
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				Cursor cp = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ? ", new String[] { id }, null);
				while (cp.moveToNext()) {
					dial1 = cp
							.getString(cp
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
					if (dial1.length() > 8) {
						dial1 = dial1.substring(dial1.length() - 8);
						dial1 = "010" + dial1;
						System.out.println("iahsefasef" + dial1);
					}

				}
				cp.close();
			}
			idd = idd + 1;
			// 異붽�
			items.add(new ListArrayItem(Name, dial1, idd));

			Map<String, Object> params = new HashMap<String, Object>();

			params.put("a", dial1);
			params.put("mynoteid", mynoteid);
			try {

				String rec = com.uploadAndRequest(new URL(
						"http://funsumer.net/mobile_autofriend.php"), params);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public void getData(int i) {

		JSONParser jParser = new JSONParser();
		if (i == 0) {
			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params = new ArrayList();
				params.add(new BasicNameValuePair("oopt", "26"));
				params.add(new BasicNameValuePair("mynoteid", mynoteid));
				params.add(new BasicNameValuePair("userid", mynoteid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				is = resEntity.getContent();

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "euc-kr"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
				} catch (Exception e) {
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
				}

				// Log.e("main", "runggining " + json);

				if (resEntity != null) {
					JSONArray obj = new JSONArray(json);
					JSONObject obj2 = obj.getJSONObject(0);
					Event = obj2.getString("Event");
					Alert = obj2.getString("Alert");
					school = obj2.getString("school");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// MENU Invite_party
			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);
				/*
				 * Toast.makeText(getApplicationContext(), "占쎄퀗猿먲옙袁⑥┷",
				 * Toast.LENGTH_LONG) .show();
				 */

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "17"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				is = resEntity.getContent();

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "euc-kr"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
				} catch (Exception e) {
					// Log.e("Buffer Error", "Error converting result " +
					// e.toString());
				}

				if (resEntity != null) {
					JSONArray obj = new JSONArray(json);

					JSONObject tjo = obj.getJSONObject(0);

					NumberOfInv = tjo.getString("NumberOfInv");
				}

			} catch (Exception e) {
			}

			// MENU Invite_Friend
			try {

				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params1 = new ArrayList();
				params1.add(new BasicNameValuePair("oopt", "18"));
				params1.add(new BasicNameValuePair("mynoteid", mynoteid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				is = resEntity.getContent();

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "euc-kr"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
				} catch (Exception e) {
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
				}

				if (resEntity != null) {
					JSONArray obj = new JSONArray(json);

					JSONObject tjo = obj.getJSONObject(0);

					NumberOfReq = tjo.getString("NumberOfReq");
				}

			} catch (Exception e) {
			}

		} else if (i == 1) {
			// Evetn == 0

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params = new ArrayList();
				params.add(new BasicNameValuePair("oopt", "26"));
				params.add(new BasicNameValuePair("mynoteid", mynoteid));
				params.add(new BasicNameValuePair("userid", mynoteid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				is = resEntity.getContent();

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "euc-kr"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
				} catch (Exception e) {
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
				}

				if (resEntity != null) {
					JSONArray obj = new JSONArray(json);
					JSONObject obj2 = obj.getJSONObject(0);
					Event = obj2.getString("Event");

					Mypage = "0";
					// Mypage = obj2.getString("Mypage");
					String con_party_num = obj2.getString("con_party_num");
					con_party = obj2.getJSONArray("con_party");
					String user_party_num = obj2.getString("user_party_num");
					user_party = obj2.getJSONArray("user_party");

					Log.e("main", "user_party = " + user_party);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (i == 2) {
			// Event == 1

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params = new ArrayList();
				params.add(new BasicNameValuePair("oopt", "26"));
				params.add(new BasicNameValuePair("mynoteid", mynoteid));
				params.add(new BasicNameValuePair("userid", mynoteid));

				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);

				HttpEntity resEntity = responsePOST.getEntity();
				is = resEntity.getContent();

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "euc-kr"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
				} catch (Exception e) {
					Log.e("Buffer Error",
							"Error converting result " + e.toString());
				}

				if (resEntity != null) {
					JSONArray obj = new JSONArray(json);
					// Log.e("main", "event JSONArray = " + obj);

					JSONObject obj2 = obj.getJSONObject(0);
					Event = obj2.getString("Event");
					String interval = obj2.getString("interval");
					String nums = obj2.getString("nums");
					first = obj2.getJSONArray("first");
					second = obj2.getJSONArray("second");
					third = obj2.getJSONArray("third");
					// JSONObject obj3 = first.getJSONObject(0);
					// String first_party_name =
					// obj3.getString("first_party_name");

					interval_int = Integer.parseInt(interval);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public void setContentsView() {
		mConList.clear();

		try {
			int arrayLength = con_party.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = con_party.getJSONObject(i);

				ContentsInfo con_ls = new ContentsInfo();

				con_ls.setpid(object.getString("pid"));
				con_ls.setpname(object.getString("pname"));
				con_ls.setppic(object.getString("ppic"));

				mConList.add(con_ls);
			}
			contentsAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setMyView() {
		mConList.clear();

		try {
			int arrayLength = user_party.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = user_party.getJSONObject(i);

				ContentsInfo my_ls = new ContentsInfo();

				my_ls.setmypid(object.getString("pid"));
				my_ls.setmypname(object.getString("pname"));
				my_ls.setmyppic(object.getString("ppic"));

				mConList.add(my_ls);
			}
			contentsAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	int temp_vote1 = -1;
	int temp_vote2 = -1;
	int temp_vote3 = -1;
	int temp_toparray = 0;

	public void setEventView() {
		mEventList.clear();

		try {
			int arrayLength = first.length();
			// int arrayLength2 = second.length();
			// int arrayLength3 = third.length();

			// EventbestInfo best_ls = new EventbestInfo();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = first.getJSONObject(i);
				JSONObject object2 = second.getJSONObject(i);
				JSONObject object3 = third.getJSONObject(i);

				EventInfo1 par_ls = new EventInfo1();

				par_ls.setfirst_party_id(object.getString("first_party_id"));
				par_ls.setfirst_party_name(object.getString("first_party_name"));
				par_ls.setfirst_party_pic(object.getString("first_party_pic"));
				par_ls.setfirst_party_vote(object.getString("first_party_vote"));
				par_ls.setfirst_top_id(object.getString("first_top_id"));
				par_ls.setfirst_top_name(object.getString("first_top_name"));
				par_ls.setfirst_top_pic(object.getString("first_top_pic"));

				int first_party_vote = Integer.parseInt(object
						.getString("first_party_vote"));
				if (first_party_vote > temp_vote1) {
					temp_vote1 = first_party_vote;
					temp_toparray = i;

					best_first_party_id = (object.getString("first_party_id"));
					best_first_party_name = (object
							.getString("first_party_name"));
					best_first_party_pic = "http://funsumer.net/"
							+ (object.getString("first_party_pic"));
					best_first_party_vote = (Integer.toString(temp_vote1));
					best_first_top_id = (object.getString("first_top_id"));
					best_first_top_name = (object.getString("first_top_name"));
					best_first_top_pic = "http://funsumer.net/"
							+ (object.getString("first_top_pic"));
				}

				par_ls.setsecond_party_id(object2.getString("second_party_id"));
				par_ls.setsecond_party_name(object2
						.getString("second_party_name"));
				par_ls.setsecond_party_pic(object2
						.getString("second_party_pic"));
				par_ls.setsecond_party_vote(object2
						.getString("second_party_vote"));
				par_ls.setsecond_top_id(object2.getString("second_top_id"));
				par_ls.setsecond_top_name(object2.getString("second_top_name"));
				par_ls.setsecond_top_pic(object2.getString("second_top_pic"));

				int second_party_vote = Integer.parseInt(object2
						.getString("second_party_vote"));
				if (second_party_vote > temp_vote2) {
					temp_vote2 = second_party_vote;
					temp_toparray = i;

					best_second_party_id = (object2
							.getString("second_party_id"));
					best_second_party_name = (object2
							.getString("second_party_name"));
					best_second_party_pic = "http://funsumer.net/"
							+ (object2.getString("second_party_pic"));
					best_second_party_vote = (Integer.toString(temp_vote2));
					best_second_top_id = (object2.getString("second_top_id"));
					best_second_top_name = (object2
							.getString("second_top_name"));
					best_second_top_pic = "http://funsumer.net/"
							+ (object2.getString("second_top_pic"));
				}

				par_ls.setthird_party_id(object3.getString("third_party_id"));
				par_ls.setthird_party_name(object3
						.getString("third_party_name"));
				par_ls.setthird_party_pic(object3.getString("third_party_pic"));
				par_ls.setthird_party_vote(object3
						.getString("third_party_vote"));
				par_ls.setthird_top_id(object3.getString("third_top_id"));
				par_ls.setthird_top_name(object3.getString("third_top_name"));
				par_ls.setthird_top_pic(object3.getString("third_top_pic"));

				int third_party_vote = Integer.parseInt(object3
						.getString("third_party_vote"));
				if (third_party_vote > temp_vote3) {
					temp_vote3 = third_party_vote;
					temp_toparray = i;

					best_third_party_id = (object3.getString("third_party_id"));
					best_third_party_name = (object3
							.getString("third_party_name"));
					best_third_party_pic = "http://funsumer.net/"
							+ (object3.getString("third_party_pic"));
					best_third_party_vote = (Integer.toString(temp_vote3));
					best_third_top_id = (object3.getString("third_top_id"));
					best_third_top_name = (object3.getString("third_top_name"));
					best_third_top_pic = "http://funsumer.net/"
							+ (object3.getString("third_top_pic"));
				}

				mEventList.add(par_ls);
			}

			eventAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	LinearLayout slidingPage01;
	boolean isPageOpen = false;
	// ImageButton openBtn01;
	ImageView logomenu;
	Animation translateLeftAnim;
	Animation translateRightAnim;

	private void Animation() {
		slidingPage01 = (LinearLayout) findViewById(R.id.slidingPage01);

		translateLeftAnim = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_left);
		translateRightAnim = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_right);
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

	public RelativeLayout main_header = null;
	private LayoutInflater mInflater;

	public RelativeLayout main_header() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		main_header = (RelativeLayout) mInflater.inflate(R.layout.main_header,
				null);

		headData();

		return main_header;
	}

	public void headData() {
		// BEST CLASS NAME
		TextView grade1_name = (TextView) main_header
				.findViewById(R.id.grade1_name);
		grade1_name.setText(best_first_party_name);
		TextView grade2_name = (TextView) main_header
				.findViewById(R.id.grade2_name);
		grade2_name.setText(best_second_party_name);
		TextView grade3_name = (TextView) main_header
				.findViewById(R.id.grade3_name);
		grade3_name.setText(best_third_party_name);

		// BEST CLASS VOTE
		TextView first_party_vote = (TextView) main_header
				.findViewById(R.id.first_party_vote);
		first_party_vote.setText(best_first_party_vote);
		TextView second_party_vote = (TextView) main_header
				.findViewById(R.id.second_party_vote);
		second_party_vote.setText(best_second_party_vote);
		TextView third_party_vote = (TextView) main_header
				.findViewById(R.id.third_party_vote);
		third_party_vote.setText(best_third_party_vote);

		// BEST CLASS PIC
		ImageView grade1 = (ImageView) main_header.findViewById(R.id.grade1);
		imageloader.displayImage(best_first_party_pic, grade1);
		grade1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						PartyActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NoteActivity.userid = mynoteid;
				intentput.putExtra("partyid", best_first_party_id);
				startActivity(intentput);
			}
		});
		ImageView grade2 = (ImageView) main_header.findViewById(R.id.grade2);
		imageloader.displayImage(best_second_party_pic, grade2, options);
		grade2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						PartyActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NoteActivity.userid = mynoteid;
				intentput.putExtra("partyid", best_second_party_id);
				startActivity(intentput);
			}
		});
		ImageView grade3 = (ImageView) main_header.findViewById(R.id.grade3);
		imageloader.displayImage(best_third_party_pic, grade3, options);
		grade3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						PartyActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NoteActivity.userid = mynoteid;
				intentput.putExtra("partyid", best_third_party_id);
				startActivity(intentput);
			}
		});
		// // grade1.setOnClickListener(firstpicClick);

		// BEST CLASS STARSUMER
		ImageView first_top_pic = (ImageView) main_header
				.findViewById(R.id.first_top_pic);
		imageloader.displayImage(best_first_top_pic, first_top_pic, options);
		ImageView second_top_pic = (ImageView) main_header
				.findViewById(R.id.second_top_pic);
		imageloader.displayImage(best_second_top_pic, second_top_pic, options);
		ImageView third_top_pic = (ImageView) main_header
				.findViewById(R.id.third_top_pic);
		imageloader.displayImage(best_third_top_pic, third_top_pic, options);

		ImageView btn_howpizza = (ImageView) main_header
				.findViewById(R.id.btn_howpizza);
		btn_howpizza.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						HowpizzaActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentput);
			}
		});
	}

	int DialogID;

	private class Noteflistthread extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DialogID);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		// NOTE FLIST THREAD
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

		// BUTTON TWO THREAD
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
