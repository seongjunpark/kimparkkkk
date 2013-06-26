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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.os.AsyncTask;
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
import android.widget.EditText;
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

public class NoteActivity extends Activity {

	private ViewPager mPager;

	SessionManager session;

	public ImageLoader imageLoader;

	static String mynoteid = MainActivity.mynoteid;
	public static String userid;
	public static String tabnoteid, tabflistid;
	static String[] BDFANPIC;
	static String[] BDFANID;
	static String[] BDFANNAME;

	EditText edit_search;

	static JSONArray gwlAPI = null;

	static JSONArray guflAPI = null;
	static String opt3_Result;
	static JSONArray opt3_Result_data = null;

	static String Fan_Result;
	static JSONArray Fan_Result_data = null;

	static JSONArray graiAPI = null;
	static String Result = null;
	static JSONArray Result_data = null;
	static JSONArray guplAPI = null;
	static JSONArray guiAPI = null;

	static String opt4_Result;
	static JSONArray opt4_Result_data = null;

	private static ArrayList<FriendInfo> mFriendList;
	private static FriendAdapter f_adapter;
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

		Intent intentget = getIntent();
		userid = intentget.getStringExtra("userid");

		if (userid == null) {
			userid = mynoteid;
		} else {
		}

		// tabnoteid = 1, NoteActivity is MINE!!
		// tabnoteid =2, NoteActivity is someone.
		if (userid.equals(mynoteid)) {
			tabnoteid = "1";
		} else {
			tabnoteid = "2";
		}

		this.imageLoader = ImageLoader.getInstance();

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(this));

		// askdelete = (RelativeLayout) findViewById(R.id.askdelete);

		// del_ok = (Button) findViewById(R.id.del_ok);
		// del_can = (Button) findViewById(R.id.del_cancel);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

		final Button home = (Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tabnoteid = null;
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
				tabnoteid = null;
				btn_one_thread thread = new btn_one_thread();
				thread.execute();
			}
		});
		btn_one.setOnTouchListener(new OnTouchListener() {
			// 占쏙옙튼 占쏙옙치占쏙옙 占싱븝옙트
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
				tabnoteid = null;
				Intent intentput = new Intent(getApplicationContext(),
						NoteActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentput);
			}
		});
		btn_two.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					btn_two.setBackgroundResource(R.drawable.top_touch);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_two.setBackgroundResource(R.drawable.top_selected);
				}
				return false;
			}
		});

		final Button btn_three = (Button) findViewById(R.id.btn_three);
		btn_three.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
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

		edit_search = (EditText) findViewById(R.id.edit_search);
		final Button search_glass = (Button) findViewById(R.id.search_glass);
		search_glass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit_search.getText().toString().equals("")) {
				} else {
					tabnoteid = null;
					Intent intent = new Intent(getApplicationContext(),
							SearchFRorPT.class);
					intent.putExtra("value", edit_search.getText().toString());
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
					search_glass.setBackgroundResource(R.drawable.btn_search);
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

		int int_NumberOfInv = Integer.parseInt(MainActivity.NumberOfInv);
		ImageView icon_rednew_party = (ImageView) findViewById(R.id.icon_rednew_party);
		if (int_NumberOfInv>0) {
			Log.e ("ksy", "icon_rednew_party VISIBLE1");
			icon_rednew_party.setVisibility(View.VISIBLE);
			Log.e ("ksy", "icon_rednew_party VISIBLE2");
		} else if (int_NumberOfInv==0) {
			Log.e ("ksy", "icon_rednew_party INVISIBLE1");
			icon_rednew_party.setVisibility(View.INVISIBLE);
			Log.e ("ksy", "icon_rednew_party INVISIBLE2");
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
		Log.e ("ksy", "int_NumberOfReq = " + int_NumberOfReq);
		ImageView icon_rednew_fr = (ImageView) findViewById(R.id.icon_rednew_fr);
		if (int_NumberOfReq>0) {
			Log.e ("ksy", "icon_rednew_fr VISIBLE1");
			icon_rednew_fr.setVisibility(View.VISIBLE);
			Log.e ("ksy", "icon_rednew_fr VISIBLE2");
		} else if (int_NumberOfReq==0) {
			Log.e ("ksy", "icon_rednew_fr INVISIBLE1");
			icon_rednew_fr.setVisibility(View.INVISIBLE);
			Log.e ("ksy", "icon_rednew_fr INVISIBLE1");
		}

		Button btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
		btn_friendlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
				Notethread thread = new Notethread();
				thread.execute();
			}
		});

		Button btn_partylist = (Button) findViewById(R.id.btn_partylist);
		btn_partylist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tabnoteid = null;
				btn_partylist_thread thread = new btn_partylist_thread();
				thread.execute();
			}
		});

		final Button btn_account = (Button) findViewById(R.id.btn_account);
		btn_account.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tabnoteid = null;
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

		Log.e("note", "tabflistid = " + tabflistid);

		if (tabflistid == "1") {
			setCurrentInflateItem(0);
			tabflistid = null;
		} else {
			setCurrentInflateItem(1);
		}
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

				if (opt3_Result.equals("0")) {
					v = mInflater.inflate(R.layout.note_left, null);

					ListView m_lv1 = (ListView) v
							.findViewById(R.id.friend_list);
					m_lv1.addHeaderView(noteleft_header());

					mFriendList = new ArrayList<FriendInfo>();
					f_adapter = new FriendAdapter(getApplicationContext(),
							R.layout.noteleft_friendlist_item, mFriendList);
					m_lv1.setAdapter(f_adapter);

					setFriendListView();
				} else {
					v = mInflater.inflate(R.layout.note_left_none, null);

					LinearLayout myfansumer = (LinearLayout) v
							.findViewById(R.id.myfansumer);
					if (mynoteid.equals(userid)) {
						myfansumer.setVisibility(View.VISIBLE);
					} else {
						myfansumer.setVisibility(View.GONE);
					}

					ImageView fan0 = (ImageView) v.findViewById(R.id.fan0);
					imageloader.displayImage(BDFANPIC[0], fan0, options);
					fan0.setOnClickListener(on_fanpicClick0);
					TextView fanname0 = (TextView) v
							.findViewById(R.id.fanname0);
					fanname0.setText(BDFANNAME[0]);

					ImageView fan1 = (ImageView) v.findViewById(R.id.fan1);
					imageloader.displayImage(BDFANPIC[1], fan1, options);
					fan1.setOnClickListener(on_fanpicClick1);
					TextView fanname1 = (TextView) v
							.findViewById(R.id.fanname1);
					fanname1.setText(BDFANNAME[1]);

					ImageView fan2 = (ImageView) v.findViewById(R.id.fan2);
					imageloader.displayImage(BDFANPIC[2], fan2, options);
					fan2.setOnClickListener(on_fanpicClick2);
					TextView fanname2 = (TextView) v
							.findViewById(R.id.fanname2);
					fanname2.setText(BDFANNAME[2]);

					ImageView fan3 = (ImageView) v.findViewById(R.id.fan3);
					imageloader.displayImage(BDFANPIC[3], fan3, options);
					fan3.setOnClickListener(on_fanpicClick3);
					TextView fanname3 = (TextView) v
							.findViewById(R.id.fanname3);
					fanname3.setText(BDFANNAME[3]);
					
					Button note_left_none_invitekakao = (Button) v.findViewById(R.id.note_left_none_invitekakao);
					note_left_none_invitekakao.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getApplicationContext(),
									InviteKakao.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);

						}
					});
				}

			} else if (position == 1) {

				getData(1);

				if (Result.equals("0")) {
					// NOTE CENTER
					v = mInflater.inflate(R.layout.note_center, null);

					ListView m_lv = (ListView) v.findViewById(R.id.list2);
					m_lv.addHeaderView(my_header02());

					mTweetList = new ArrayList<TweetInfo>();
					mAdapter = new CustomAdapter(NoteActivity.this,
							R.layout.item, mTweetList);
					m_lv.setAdapter(mAdapter);

					setListView();
				} else if (Result.equals("1")) {
					// NOTE CENTER NONE
					v = mInflater.inflate(R.layout.note_center_none, null);

					TextView result_Name = (TextView) v
							.findViewById(R.id.result_Name);
					result_Name.setText(Result_Name);

					friendnumber = (TextView) v.findViewById(R.id.Result_Fnum);
					friendnumber.setText(Result_Fnum);

					votenumber = (TextView) v.findViewById(R.id.Result_Vnum);
					votenumber.setText(Result_Vnum);

					ImageView notepic = (ImageView) v
							.findViewById(R.id.notepic);
					imageloader.displayImage(Result_WidePic, notepic, options);

					ImageButton profilepic = (ImageButton) v
							.findViewById(R.id.profilepic);
					imageloader.displayImage(Result_ProfilePic, (profilepic), options);
					profilepic.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intentput = new Intent(
									getApplicationContext(),
									ProfileActivity.class);
							intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intentput.putExtra("mynoteid", mynoteid);
							intentput.putExtra("userid", mynoteid);
							startActivity(intentput);
						}
					});

					Button req_friend = (Button) v
							.findViewById(R.id.req_friend);
					req_friend.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								HttpClient client = new DefaultHttpClient();
								String postURL = "http://funsumer.net/json/";
								HttpPost post = new HttpPost(postURL);

								List params13 = new ArrayList();
								params13.add(new BasicNameValuePair("oopt",
										"13"));
								params13.add(new BasicNameValuePair("mynoteid",
										mynoteid));
								params13.add(new BasicNameValuePair("fid",
										userid));
								params13.add(new BasicNameValuePair("fopt", "1"));

								UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
										params13, HTTP.UTF_8);
								post.setEntity(ent);
								HttpResponse responsePOST = client
										.execute(post);
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});

					votenumber_pic = (ImageView) v.findViewById(R.id.countvote);

					// VOTE ACTION
					novote = (ImageView) v.findViewById(R.id.novote);
					yesvote = (ImageView) v.findViewById(R.id.yesvote);
					Log.e("note", "here is HEADdata.. mynoteid = " + mynoteid
							+ "  userid = " + userid);
					if (mynoteid.equals(userid)) {
						novote.setImageResource(R.drawable.stub_image);
						defaultImage();
						votenumber_pic.setVisibility(View.VISIBLE);
						votenumber.setVisibility(View.VISIBLE);
					} else {
						if (Result_Vote.equals("1")) {
							Log.e("note", "111 Result_vote is = " + Result_Vote);
							votenumber_pic.setVisibility(View.VISIBLE);
							votenumber.setVisibility(View.VISIBLE);
							novote.setImageResource(R.drawable.vote_after);
							novote.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(ALL.getAppContext(),
											"인기투표는 한 사람에게 하루 한 번만 가능합니다.",
											Toast.LENGTH_LONG).show();
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
										params1.add(new BasicNameValuePair(
												"oopt", "4"));
										params1.add(new BasicNameValuePair(
												"mynoteid", mynoteid));
										params1.add(new BasicNameValuePair(
												"userid", userid));

										UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
												params1, HTTP.UTF_8);
										post.setEntity(ent);
										HttpResponse responsePOST = client
												.execute(post);

										changeImage();
										votenumber_pic
												.setVisibility(View.VISIBLE);
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
									// "占싱뱄옙 占쏙옙표占싹울옙占쏙옙占싹댐옙.").show();
									Toast.makeText(ALL.getAppContext(),
											"인기투표는 한 사람에게 하루 한 번만 가능합니다.",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
					}

					Button btn_go_noteleft = (Button) v
							.findViewById(R.id.btn_go_noteleft);
					btn_go_noteleft
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									setCurrentInflateItem(0);
								}
							});
					Button btn_makearticle = (Button) v
							.findViewById(R.id.btn_makearticle);
					btn_makearticle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent1 = new Intent(
									getApplicationContext(), WriteNote.class);
							intent1.putExtra("position", "1");
							startActivity(intent1);
						}
					});

					Button btn_go_noteright = (Button) v
							.findViewById(R.id.btn_go_noteright);
					btn_go_noteright
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									setCurrentInflateItem(2);
								}
							});
					//
				}

			} else if (position == 2) {

				getData(2);

				if (opt4_Result.equals("0")) {
					v = mInflater.inflate(R.layout.note_right, null);

					TextView result_Name = (TextView) v
							.findViewById(R.id.note_right_user);
					result_Name.setText(Result_Name + "님의 파티목록");

					GridView gridview = (GridView) v
							.findViewById(R.id.gridview);

					mPartyList = new ArrayList<PartyInfo>();
					gridadapter = new GridAdapter(NoteActivity.this,
							R.layout.note_right_grid, mPartyList);
					gridview.setAdapter(gridadapter);

					setPartyView();

					Button makeparty = (Button) v.findViewById(R.id.makeparty);
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
				} else {
					v = mInflater.inflate(R.layout.note_right_none, null);

					TextView result_Name = (TextView) v
							.findViewById(R.id.note_right_user);
					result_Name.setText(Result_Name + "님의 파티목록");

					Button makeparty = (Button) v.findViewById(R.id.makeparty);
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
				}

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
	public static void getData(int i) {

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
					String BDpicc = "http://www.funsumer.net/" + k + "50";

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
				opt3_Result = resultdata.getString("Result");
				opt3_Result_data = resultdata.getJSONArray("Result_data");
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

				JSONObject wide = guiAPI.getJSONObject(0);
				ResultinOPT2 = wide.getString("Result");

				Result_WidePic = "http://www.funsumer.net/"
						+ wide.getString("Result_WidePic");
				Result_ProfilePic = "http://www.funsumer.net/"
						+ wide.getString("Result_ProfilePic") + "50";

				Result_Name = wide.getString("Result_Name");
				Result_Fnum = wide.getString("Result_Fnum");
				Result_Vnum = wide.getString("Result_Vnum");
				Result_Vote = wide.getString("Result_Vote");
				Result_Fstat = wide.getString("Result_Fstat");
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
				opt4_Result = resultdata.getString("Result");
				opt4_Result_data = resultdata.getJSONArray("Result_data");
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
				tweet.setArticle(object.getString("ArInfo").replaceAll("<br>",
						"\n"));
				tweet.setArticle_Like_Num(object.getString("Article_Like_Num"));
				tweet.setArticle_Comment_Num(object
						.getString("Article_Comment_Num"));
				tweet.setArticle_Scrap_Num(object.getString("ScrapNum"));

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

	public static void setFriendListView() {
		mFriendList.clear();

		try {
			int arrayLength = opt3_Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = opt3_Result_data.getJSONObject(i);
				FriendInfo tweet = new FriendInfo();

				tweet.setFname(object.getString("Fname"));
				tweet.setFparty(object.getString("Fparty"));

				tweet.setFpic(object.getString("Fpic"));

				tweet.setFid(object.getString("Fid"));

				tweet.setFvote(object.getString("Fvote"));

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
			int arrayLength = opt4_Result_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = opt4_Result_data.getJSONObject(i);
				PartyInfo par_ls = new PartyInfo();

				par_ls.setBDPNAME(object.getString("pname"));

				par_ls.setPAR_IMAGES(object.getString("ppic"));

				par_ls.setPid(object.getString("pid"));

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

	// NOTE_LEFT HEADER
	public LinearLayout noteleft_header02 = null;
	private LayoutInflater mInflater;

	public LinearLayout noteleft_header() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (mynoteid.equals(userid)) {
			noteleft_header02 = (LinearLayout) mInflater.inflate(
					R.layout.noteleft_header_my, null);

			ImageView fan0 = (ImageView) noteleft_header02
					.findViewById(R.id.fan0);
			imageloader.displayImage(BDFANPIC[0], fan0, options);
			fan0.setOnClickListener(on_fanpicClick0);
			// fan0.setOnClickListener(new View.OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// Intent intentput = new Intent(getApplicationContext(),
			// NoteActivity.class);
			// intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intentput.putExtra("userid", BDFANID[0]);
			// startActivity(intentput);
			// }
			// });
			TextView fanname0 = (TextView) noteleft_header02
					.findViewById(R.id.fanname0);
			fanname0.setText(BDFANNAME[0]);

			ImageView fan1 = (ImageView) noteleft_header02
					.findViewById(R.id.fan1);
			imageloader.displayImage(BDFANPIC[1], fan1, options);
			fan1.setOnClickListener(on_fanpicClick1);
			TextView fanname1 = (TextView) noteleft_header02
					.findViewById(R.id.fanname1);
			fanname1.setText(BDFANNAME[1]);

			ImageView fan2 = (ImageView) noteleft_header02
					.findViewById(R.id.fan2);
			imageloader.displayImage(BDFANPIC[2], fan2, options);
			fan2.setOnClickListener(on_fanpicClick2);
			TextView fanname2 = (TextView) noteleft_header02
					.findViewById(R.id.fanname2);
			fanname2.setText(BDFANNAME[2]);

			ImageView fan3 = (ImageView) noteleft_header02
					.findViewById(R.id.fan3);
			imageloader.displayImage(BDFANPIC[3], fan3, options);
			fan3.setOnClickListener(on_fanpicClick3);
			TextView fanname3 = (TextView) noteleft_header02
					.findViewById(R.id.fanname3);
			fanname3.setText(BDFANNAME[3]);

			Log.e("note", "BDFANID 0 = " + BDFANID[0]);
			Log.e("note", "BDFANID 1 = " + BDFANID[1]);
			Log.e("note", "BDFANID 2 = " + BDFANID[2]);

		} else {
			noteleft_header02 = (LinearLayout) mInflater.inflate(
					R.layout.noteleft_header, null);
		}

		ImageView note_left_top2 = (ImageView) findViewById(R.id.note_left_top2);

		TextView flist_title = (TextView) findViewById(R.id.flist_title);

		ImageView note_left_top3 = (ImageView) findViewById(R.id.note_left_top3);

		return noteleft_header02;
	}

	final OnClickListener on_fanpicClick0 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[0]);
			startActivity(intentput);
		}
	};
	final OnClickListener on_fanpicClick1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[1]);
			startActivity(intentput);
		}
	};
	final OnClickListener on_fanpicClick2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", BDFANID[2]);
			v.getContext().startActivity(intentput);
		}
	};
	final OnClickListener on_fanpicClick3 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
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
				Intent intent1 = new Intent(getApplicationContext(),
						WriteNote.class);
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
	public static String Result_Fstat = null;

	static TextView friendnumber;
	static TextView votenumber;
	static ImageView votenumber_pic;

	static ImageView novote;
	static ImageView yesvote;

	public void head_data() {
		TextView notetoptuto = (TextView) m_header02
				.findViewById(R.id.notetoptuto);
		notetoptuto.setText(Result_Name + "님의 노트");

		TextView result_Name = (TextView) m_header02
				.findViewById(R.id.result_Name);
		result_Name.setText(Result_Name);

		friendnumber = (TextView) m_header02.findViewById(R.id.Result_Fnum);
		friendnumber.setText(Result_Fnum);

		votenumber = (TextView) m_header02.findViewById(R.id.Result_Vnum);
		votenumber.setText(Result_Vnum);

		ImageView notepic = (ImageView) m_header02.findViewById(R.id.notepic);
		imageloader.displayImage(Result_WidePic, notepic);
		notepic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProfileActivity.tabprofileid = "2";
				new WidepicDialog(v.getContext(), WidepicDialog.TYPE_BASIC_OK,
						null).show();
			}
		});

		ImageView profilepic = (ImageView) m_header02
				.findViewById(R.id.profilepic);
		imageloader.displayImage(Result_ProfilePic, (profilepic), options);
		profilepic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						ProfileActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("mynoteid", mynoteid);
				intentput.putExtra("userid", userid);
				ProfileActivity.tabprofileid = "1";
				startActivity(intentput);
			}
		});

		Log.e("note", "Result_Fstat = " + Result_Fstat);

		Button req_friend = (Button) m_header02.findViewById(R.id.req_friend);
		if (Result_Fstat.equals("4")) {
			req_friend.setVisibility(View.VISIBLE);
		}
		req_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					HttpClient client = new DefaultHttpClient();
					String postURL = "http://funsumer.net/json/";
					HttpPost post = new HttpPost(postURL);

					List params13 = new ArrayList();
					params13.add(new BasicNameValuePair("oopt", "13"));
					params13.add(new BasicNameValuePair("mynoteid", mynoteid));
					params13.add(new BasicNameValuePair("fid", userid));
					params13.add(new BasicNameValuePair("fopt", "1"));

					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
							params13, HTTP.UTF_8);
					post.setEntity(ent);
					HttpResponse responsePOST = client.execute(post);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

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
						 * "占싱뱄옙 占쏙옙표占싹울옙占쏙옙占싹댐옙.").show();
						 */
						Toast.makeText(ALL.getAppContext(),
								"인기투표는 한 사람에게 하루 한 번만 가능하십니다.",
								Toast.LENGTH_LONG).show();
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
						// "占싱뱄옙 占쏙옙표占싹울옙占쏙옙占싹댐옙.").show();
						Toast.makeText(ALL.getAppContext(), "이미 투표하셨습니다.",
								Toast.LENGTH_SHORT).show();
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
