package www.funsumer.net;

//import static fun.example.park.constants.Constants_PartyGrid.PAR_IMAGES;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import www.funsumer.net.constants.ContentsInfo;
import www.funsumer.net.constants.PartyInfo;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.ContentsAdapter;
import www.funsumer.net.widget.GridAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PartylistActivity extends Activity {

	SessionManager session;

	String mynoteid, userid;

	// JSONArray user_party, con_party;
	// public static String Mypage = "1";

	JSONArray guplAPI = null;
	String Result = null;
	JSONArray Result_data = null;
	JSONArray con_party = null;

	GridAdapter gridadapter;
	public ArrayList<PartyInfo> mPartyList;
	public ArrayList<ContentsInfo> mConList;
	public ContentsAdapter contentsAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partylist);

		Intent intent = getIntent();
		mynoteid = intent.getStringExtra("mynoteid");
		userid = intent.getStringExtra("userid");

		// GET DATA START
		getData(2);

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

		MainActivity.Mypage = "1";

		GridView mainlist = (GridView) findViewById(R.id.maingrid);
		// mainlist.addHeaderView(main_header());

		mConList = new ArrayList<ContentsInfo>();
		contentsAdapter = new ContentsAdapter(PartylistActivity.this,
				R.layout.main_contentslist_grid, mConList);
		// mylistAdapter = new MylistAdapter(MainActivity.this,
		// R.layout.main_mylist_grid, mConList);
		mainlist.setAdapter(contentsAdapter);

		if (MainActivity.Mypage.equals("0")) {
			setContentsView();

		} else if (MainActivity.Mypage.equals("1")) {
			if (Result.equals("0")) {
				setMyView();
			} else if (Result.equals("1")) {

			}
			Log.e("plist", "RUNNINGRUNNINGRUNNING" + MainActivity.Mypage);
		}

		final Button switch_cotentslist_left = (Button) findViewById(R.id.switch_cotentslist_left);
		final Button switch_cotentslist_right = (Button) findViewById(R.id.switch_cotentslist_right);
		switch_cotentslist_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.Mypage.equals("1")) {
					MainActivity.Mypage = "0";
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

		switch_cotentslist_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.Mypage.equals("0")) {
					MainActivity.Mypage = "1";
					if (Result.equals("0")) {
						setMyView();
						switch_cotentslist_left
								.setBackgroundResource(R.drawable.btn_contents_left);
						switch_cotentslist_left.setTextColor(Color
								.parseColor("#000000"));
						switch_cotentslist_right
								.setBackgroundResource(R.drawable.btn_contents_right_selected);
						switch_cotentslist_right.setTextColor(Color
								.parseColor("#ffffff"));
					} else if (Result.equals("1")) {
						Toast.makeText(getApplicationContext(),
								"���Ե� ��Ƽ�� ����ϴ�. ��Ƽ�� ������ ������.",
								Toast.LENGTH_SHORT).show();
					}

				} else {
				}

			}
		});

		final Button home = (Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NoteActivity.tabnoteid = null;
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

		final Button btn_two = (Button) findViewById(R.id.btn_two);
		btn_two.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_two_thread thread = new btn_two_thread();
				thread.execute();

			}
		});
		btn_two.setOnTouchListener(new OnTouchListener() {
			// ��ư ��ġ�� �̺�Ʈ
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
			public void onClick(View arg0) {
				Intent intentput = new Intent(getApplicationContext(),
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
			// ��ư ��ġ�� �̺�Ʈ
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
			// ��ư ��ġ�� �̺�Ʈ
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
			// ��ư ��ġ�� �̺�Ʈ
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

		Button btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
		btn_friendlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intentput = new Intent(getApplicationContext(),
						NoteActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NoteActivity.tabflistid = "1";
				startActivity(intentput);
			}
		});

		Button btn_partylist = (Button) findViewById(R.id.btn_partylist);
		btn_partylist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				btn_partylist_thread thread = new btn_partylist_thread();
				thread.execute();
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
			// ��ư ��ġ�� �̺�Ʈ
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

		// Button makeparty = (Button)findViewById(R.id.makeparty);
		// makeparty.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(getApplicationContext(), makeParty.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		//
		// }
		// });

		Animation();
	}

	// GET DATA
	public void getData(int i) {

		JSONParser jParser = new JSONParser();
		if (i == 2) {

			String url = "http://funsumer.net/json/?opt=4&mynoteid=" + mynoteid
					+ "&userid=" + userid;

			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				guplAPI = jsonurl.getJSONArray("guplAPI");
				JSONObject resultdata = guplAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				if (Result.equals("1")) {
					String con_party_num = resultdata
							.getString("con_party_num");
					con_party = resultdata.getJSONArray("con_party");
				} else if (Result.equals("0")) {
					Result_data = resultdata.getJSONArray("Result_data");
					String con_party_num = resultdata
							.getString("con_party_num");
					con_party = resultdata.getJSONArray("con_party");
				}

				Log.e("plist", "Result = " + Result);
				Log.e("plist", "Result_data ggg = " + Result_data);
				Log.e("plist", "con_party ggg = " + con_party);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// public void setPartyView() {
	// mPartyList.clear();
	//
	// try {
	// int arrayLength = Result_data.length();
	//
	// for (int i = 0; i < arrayLength; i++) {
	// JSONObject object = Result_data.getJSONObject(i);
	// PartyInfo par_ls = new PartyInfo();
	//
	// par_ls.setBDPNAME(object.getString("Pname"));
	//
	// par_ls.setPAR_IMAGES(object.getString("Ppic"));
	//
	// par_ls.setPid(object.getString("Pid"));
	//
	// mPartyList.add(par_ls);
	// }
	//
	// gridadapter.notifyDataSetChanged();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }

	public void setContentsView() {
		mConList.clear();

		try {
			int arrayLength = con_party.length();

			Log.e("plist", "con arrayLength = " + arrayLength);

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
			int arrayLength = Result_data.length();

			Log.e("plist", "my arrayLength = " + arrayLength);

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = Result_data.getJSONObject(i);

				ContentsInfo my_ls = new ContentsInfo();

				my_ls.setmypid(object.getString("pid"));
				my_ls.setmypname(object.getString("pname"));
				my_ls.setmyppic(object.getString("ppic"));

				Log.e("plist",
						"object.getString = " + object.getString("pname"));

				mConList.add(my_ls);
			}
			contentsAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	LinearLayout slidingPage01;
	boolean isPageOpen = false;
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

	int DialogID;

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

}