package www.funsumer.net;

import static www.funsumer.net.widget.GridAdapter.getpartyid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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

import www.funsumer.net.Dialog.PartyjoinDialog;
import www.funsumer.net.constants.MemberInfo;
import www.funsumer.net.constants.PartyInfo;
import www.funsumer.net.constants.TweetInfo;
import www.funsumer.net.login.SessionManager;
import www.funsumer.net.widget.CustomAdapter;
import www.funsumer.net.widget.GridAdapter;
import www.funsumer.net.widget.MemberAdapter;
import www.funsumer.net.widget.MemberLoader;
import www.funsumer.net.widget.StarLoader;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PartyActivity extends Activity {

	private ViewPager mPager;

	SessionManager session;
	// public ListView m_lv = null;
	// LazyAdapter adapter;

	public ImageLoader imageloader;
	public StarLoader starloader;
	public MemberLoader memberloader;

	public PartyjoinDialog partyjoinDialog;

	String mynoteid = MainActivity.mynoteid;
	String userid;
	String getuserid;
	public static String partyid;
	// String authorid;

	String manpic, womanpic, manname, womanname, manid, womanid, manvote,
			womanvote;

	EditText edit_search;

	JSONArray guflAPI = null;
	JSONArray graiAPI = null;
	String Result = null;
	JSONArray Result_data = null;
	JSONArray guplAPI = null;
	static JSONArray gpiAPI = null;
	String opt7_Result = null;
	String opt_PartySSierID = null;
	String opt7_joined = null;
	JSONArray opt7_Party_Mem_data = null;

	String opt4_Result = null;
	JSONArray opt4_Result_data = null;

	private ArrayList<MemberInfo> mMemberList;
	private MemberAdapter mem_adapter;
	// private ArrayList<FriendInfo> mFriendList;
	// private FriendAdapter f_adapter;
	static ArrayList<TweetInfo> mTweetList;
	static CustomAdapter mAdapter;
	private ArrayList<PartyInfo> mPartyList;
	private GridAdapter gridadapter;
	public ImageLoader imageLoader;

	InputStream is = null;
	String json = "";
	private List<String> registrationIds = new ArrayList<String>();
	private DisplayImageOptions options;

	// @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.party_base);

		userid = NoteActivity.userid;
		// Intent intentget = getIntent();
		// userid = intentget.getStringExtra("userid");
		Log.e("party", "Note userid = " + NoteActivity.userid);
		Log.e("party", "Party userid = " + userid);

		// partyid = getpartyid;
		Intent intentget = getIntent();
		partyid = intentget.getStringExtra("partyid");

		Log.e("party", "partyid = " + partyid);

		this.imageLoader = ImageLoader.getInstance();
		starloader = new StarLoader(this.getApplicationContext());
		memberloader = new MemberLoader(this.getApplicationContext());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(this));
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory().cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20)).build();

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

		final Button btn_one = (Button) findViewById(R.id.btn_one);
		btn_one.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						PartylistActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("mynoteid", mynoteid);
				intentput.putExtra("userid", mynoteid);
				startActivity(intentput);
			}
		});
		btn_one.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					btn_one.setBackgroundResource(R.drawable.top_touch);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_one.setBackgroundResource(R.drawable.top_selected);
				}
				return false;
			}
		});

		final Button btn_two = (Button) findViewById(R.id.btn_two);
		btn_two.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentput = new Intent(getApplicationContext(),
						NoteActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("userid", mynoteid);
				startActivity(intentput);
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

		// SEARCH IN SLIDE
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
					intent.putExtra("value", edit_search.getText().toString());
					startActivity(intent);
				}
			}
		});
		search_glass.setOnTouchListener(new OnTouchListener() {
			// ��ư ��ġ�� �̺�Ʈ
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

		final Button btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
		btn_friendlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intentput = new Intent(getApplicationContext(),
						NoteActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("userid", mynoteid);
				NoteActivity.tabflistid = "1";
				startActivity(intentput);
			}
		});
		btn_friendlist.setOnTouchListener(new OnTouchListener() {
			// ��ư ��ġ�� �̺�Ʈ
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
				Intent intentput = new Intent(getApplicationContext(),
						PartylistActivity.class);
				intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentput.putExtra("mynoteid", mynoteid);
				intentput.putExtra("userid", mynoteid);
				startActivity(intentput);
			}
		});
		btn_partylist.setOnTouchListener(new OnTouchListener() {
			// ��ư ��ġ�� �̺�Ʈ
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

		Animation();

		setCurrentInflateItem(1);
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

			getData(1);

			if (position == 0) {

				getData(0);

				v = mInflater.inflate(R.layout.party_left, null);

				ListView m_lv1 = (ListView) v.findViewById(R.id.member_list);
				m_lv1.addHeaderView(partyleft_header());
				if (opt_PartySSierID.equals(mynoteid)) {
				
				} else {
					if (opt7_joined.equals("0")) {
						
					} else {
					m_lv1.addFooterView(partyleft_footer());
					}
				}
				
				Log.e("party", "opt_PartySSierID 123= " + opt_PartySSierID);

				mMemberList = new ArrayList<MemberInfo>();
				mem_adapter = new MemberAdapter(getApplicationContext(),
						R.layout.noteleft_friendlist_item, mMemberList);
				m_lv1.setAdapter(mem_adapter);

				setMemberListView();

			} else if (position == 1) {

				if (Result.equals("0")) {
					v = mInflater.inflate(R.layout.party_center, null);

					ListView m_lv = (ListView) v
							.findViewById(R.id.partyplay_list);
					m_lv.addHeaderView(my_header03());

					mTweetList = new ArrayList<TweetInfo>();
					mAdapter = new CustomAdapter(PartyActivity.this,
							R.layout.item, mTweetList);
					m_lv.setAdapter(mAdapter);

					setListView();
				} else if (Result.equals("1")) {
					v = mInflater.inflate(R.layout.party_center_none, null);

					Button btn_go_partyleft = (Button) v
							.findViewById(R.id.btn_go_partyleft);
					btn_go_partyleft
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									setCurrentInflateItem(0);
								}
							});

					Button btn_go_partyright = (Button) v
							.findViewById(R.id.btn_go_partyright);
					btn_go_partyright
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
									setCurrentInflateItem(2);
								}
							});

					Button writePT = (Button) v.findViewById(R.id.writePT);
					if (opt7_joined.equals("0")) {
						writePT.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Toast.makeText(ALL.getAppContext(),
										"��Ƽ�� �����ϼž� ���� �ۼ��� �� �ֽ��ϴ�.",
										Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						writePT.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent intentput = new Intent(
										getApplicationContext(),
										WriteNote.class);
								intentput.putExtra("position", "2");
								intentput
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intentput);
							}
						});
					}

					try {
						JSONObject wide = gpiAPI.getJSONObject(0);
						ResultinOPT7 = wide.getString("Result");

						PartyPic = "http://www.funsumer.net/"
								+ wide.getString("PartyPic");
						PartySSierPic = "http://www.funsumer.net/"
								+ wide.getString("PartySSierPic");

						PartyName = wide.getString("PartyName");
						PartySSierName = wide.getString("PartySSierName");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					TextView partyname = (TextView) v
							.findViewById(R.id.partyname);
					partyname.setText(PartyName);

					TextView partyssiername = (TextView) v
							.findViewById(R.id.partyssiername);
					partyssiername.setText(PartySSierName);

					ImageView partypic = (ImageView) v
							.findViewById(R.id.partypic);
					imageloader.displayImage(PartyPic, partypic, options);

					// 1 is joined
					if (opt7_joined.equals("1")) {
						btn_join = (Button) v.findViewById(R.id.btn_join);
						btn_join.setVisibility(View.INVISIBLE);

						btn_invite = (Button) v.findViewById(R.id.btn_invite);
						btn_invite.setVisibility(View.VISIBLE);
					} else if (opt7_joined.equals("0")) {
						btn_join = (Button) v.findViewById(R.id.btn_join);
						btn_join.setVisibility(View.VISIBLE);
						btn_join.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								new PartyjoinDialog(v.getContext(),
										PartyjoinDialog.TYPE_BASIC_OK, null)
										.show();

								// joinParty();
								// btn_join.setVisibility(View.INVISIBLE);
								// btn_invite.setVisibility(View.VISIBLE);
							}
						});
						btn_invite = (Button) v.findViewById(R.id.btn_invite);
						btn_invite.setVisibility(View.INVISIBLE);
					}
					btn_invite.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									inviteFR.class);
							intent.putExtra("userid", userid);
							intent.putExtra("FROM_PARTY", "1");
							intent.putExtra("partyid", partyid);
							startActivity(intent);

						}
					});

					Log.e("party", "opt_PartySSierID = " + opt_PartySSierID);

					setting_off = (Button) v.findViewById(R.id.setting_off);
					if (opt_PartySSierID.equals(mynoteid)) {
						setting_off.setVisibility(View.VISIBLE);
					} else {
						setting_off.setVisibility(View.INVISIBLE);
					}
					setting_off.setOnTouchListener(new OnTouchListener() {
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_DOWN)
								setting_off
										.setBackgroundResource(R.drawable.icon_setting_on);
							if (event.getAction() == MotionEvent.ACTION_UP) {
								setting_off
										.setBackgroundResource(R.drawable.icon_setting_off);
							}
							return false;
						}
					});
					setting_off.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							new SettingDialog(v.getContext(),
									SettingDialog.TYPE_BASIC_OK, null, partyid)
									.show();
						}
					});
				}

			} else if (position == 2) {

				v = mInflater.inflate(R.layout.party_right, null);

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

			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://funsumer.net/json/";
				HttpPost post = new HttpPost(postURL);

				List params = new ArrayList();
				params.add(new BasicNameValuePair("oopt", "10"));
				params.add(new BasicNameValuePair("pid", partyid));

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
					Log.e("party", "starsumer JSONArray = " + obj);

					JSONObject obj2 = obj.getJSONObject(0);
					String manok = obj2.getString("manok");
					String womanok = obj2.getString("womanok");

					// man array
					JSONArray marr = obj2.getJSONArray("man");
					JSONObject man2obj = marr.getJSONObject(0);
					manid = man2obj.getString("manid");
					manname = man2obj.getString("manname");
					manpic = man2obj.getString("manpic");
					manvote = man2obj.getString("manvote");

					// woman array
					JSONArray warr = obj2.getJSONArray("woman");
					JSONObject woman2obj = warr.getJSONObject(0);
					womanid = woman2obj.getString("womanid");
					womanname = woman2obj.getString("womanname");
					womanpic = woman2obj.getString("womanpic");
					womanvote = woman2obj.getString("womanvote");

					Log.e("party", "starsumer womanok = " + womanok);
					Log.e("party", "starsumer woman2obj = " + woman2obj);
					Log.e("party", "starsumer womanvote = " + womanvote);
					Log.e("party", "starsumer womanname = " + womanname);
					Log.e("party", "starsumer womanid = " + womanid);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (i == 1) {
			if (userid == null) {
				userid = mynoteid;
			}

			// list url
			String url = "http://funsumer.net/json/?opt=5&afrom=2&mynoteid="
					+ mynoteid + "&partyid=" + partyid;
			Log.e("party", "partyplay url = " + url);
			try {
				JSONObject jsonurl = jParser.getJSONFromUrl(url);
				graiAPI = jsonurl.getJSONArray("graiAPI");
				JSONObject resultdata = graiAPI.getJSONObject(0);
				Result = resultdata.getString("Result");
				Result_data = resultdata.getJSONArray("Result_data");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// header03 url
			String partyheadurl = "http://funsumer.net/json/?opt=7"
					+ "&mynoteid=" + mynoteid + "&userid=" + userid
					+ "&partyid=" + partyid;

			Log.e("party", "partyhead = " + partyheadurl);

			try {
				JSONObject json = jParser.getJSONFromUrl(partyheadurl);
				gpiAPI = json.getJSONArray("gpiAPI");
				Log.e("party", "gpiAPI = " + gpiAPI);
				JSONObject resultdata = gpiAPI.getJSONObject(0);
				opt7_Result = resultdata.getString("Result");
				opt_PartySSierID = resultdata.getString("PartySSierID");
				opt7_joined = resultdata.getString("joined");
				opt7_Party_Mem_data = resultdata.getJSONArray("Party_Mem_data");
				Log.e("party", "opt7_Party_Mem_data = " + opt7_Party_Mem_data);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (i == 2) {
			partyid = getpartyid;
			if (partyid == null) {

				// userid = mynoteid;

				String url = "http://funsumer.net/json/?opt=4&mynoteid="
						+ mynoteid + "&userid=" + userid;
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

	public void setMemberListView() {
		mMemberList.clear();

		try {
			int arrayLength = opt7_Party_Mem_data.length();

			for (int i = 0; i < arrayLength; i++) {
				JSONObject object = opt7_Party_Mem_data.getJSONObject(i);
				MemberInfo tweet = new MemberInfo();

				tweet.setParty_Mem_Name(object.getString("Party_Mem_Name"));

				tweet.setParty_Mem_Pic(object.getString("Party_Mem_Pic"));

				tweet.setParty_Mem_ID(object.getString("Party_Mem_ID"));

				tweet.setParty_Mem_Party(object.getString("Party_Mem_Party"));

				tweet.setFstat(object.getString("Fstat"));

				mMemberList.add(tweet);
			}

			mem_adapter.notifyDataSetChanged();
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

	// PARTY LEFT HEADER
	public LinearLayout partyleft_header03 = null;

	public LinearLayout partyleft_header() {
		minflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		partyleft_header03 = (LinearLayout) minflater.inflate(
				R.layout.party_left_header, null);

		ImageView starman = (ImageView) partyleft_header03
				.findViewById(R.id.starman);
		memberloader.DisplayImage("http://funsumer.net/" + manpic, starman);
		starman.setOnClickListener(starmanClick);

		TextView starmanname = (TextView) partyleft_header03
				.findViewById(R.id.starmanname);
		starmanname.setText(manname);

		ImageView starwoman = (ImageView) partyleft_header03
				.findViewById(R.id.starwoman);
		memberloader.DisplayImage("http://funsumer.net/" + womanpic, starwoman);
		starwoman.setOnClickListener(starwomanClick);

		TextView starwomanname = (TextView) partyleft_header03
				.findViewById(R.id.starwomanname);
		starwomanname.setText(womanname);

		return partyleft_header03;
	}
	
	final OnClickListener starmanClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", manid);
			NoteActivity.tabnoteid = null;
			v.getContext().startActivity(intentput);

			return;
		}
	};

	final OnClickListener starwomanClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intentput = new Intent(getApplicationContext(),
					NoteActivity.class);
			intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentput.putExtra("userid", womanid);
			NoteActivity.tabnoteid = null;
			v.getContext().startActivity(intentput);

			return;
		}
	};
	
	//PARTY_LEFT_ HEADER04
	public LinearLayout partyleft_header04 = null;
	private LayoutInflater minflater1;
	Button left_party;

	public LinearLayout partyleft_footer() {
		minflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		partyleft_header04 = (LinearLayout) minflater.inflate(
				R.layout.party_left_footer, null);
		left_party = (Button) partyleft_header04.findViewById(R.id.left_party);

		left_party.setOnClickListener(left_partyB);

		return partyleft_header04;

	}

	final OnClickListener left_partyB = new OnClickListener() {
		@Override
		public void onClick(View v) {

			left_party();
			if (admin == "1" || admin.equals("1")) {
				Intent intent = new Intent(PartyActivity.this,
						PartyS_Left.class);
				intent.putExtra("userid", userid);
				intent.putExtra("partyid", partyid);
				startActivity(intent);
				finish();

			} else {
				Intent intent = new Intent(PartyActivity.this, PartylistActivity.class);
				intent.putExtra("mynoteid", mynoteid);
				intent.putExtra("userid", mynoteid);
				startActivity(intent);
				finish();
			}
			return;
		}
	};
	String admin;

	public void left_party() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "28"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("pid", partyid));

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
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			if (resEntity != null) {
				JSONArray obj = new JSONArray(json);

				for (int i = 0; i < obj.length(); i++) {

					JSONObject tjo = obj.getJSONObject(i);

					admin = tjo.getString("admin");
					System.out.println("ihasef" + admin);

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// PARTY HEADER 03
	public LinearLayout m_header03 = null;
	private LayoutInflater minflater;

	public LinearLayout my_header03() {
		minflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_header03 = (LinearLayout) minflater.inflate(R.layout.my_header03,
				null);

		head_data();

		Button btn_go_partyleft = (Button) m_header03
				.findViewById(R.id.btn_go_partyleft);
		btn_go_partyleft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentInflateItem(0);
			}
		});

		Button btn_go_partyright = (Button) m_header03
				.findViewById(R.id.btn_go_partyright);
		btn_go_partyright.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentInflateItem(2);
			}
		});

		Button writePT = (Button) m_header03.findViewById(R.id.writePT);
		if (opt7_joined.equals("0")) {
			writePT.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Toast.makeText(ALL.getAppContext(),
							"��Ƽ�� �����ϼž� ���� �ۼ��� �� �ֽ��ϴ�.", Toast.LENGTH_SHORT)
							.show();
				}
			});
		} else {
			writePT.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intentput = new Intent(getApplicationContext(),
							WriteNote.class);
					intentput.putExtra("position", "2");
					intentput.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentput);
				}
			});
		}

		return m_header03;
	}

	String ResultinOPT7 = null;
	String PartyPic = null;
	String PartySSierPic = null;
	String PartyName = null;
	String PartySSierName = null;
	public static Button btn_join, btn_invite;
	Button setting_off;

	public void head_data() {

		try {
			JSONObject wide = gpiAPI.getJSONObject(0);
			ResultinOPT7 = wide.getString("Result");

			PartyPic = "http://www.funsumer.net/" + wide.getString("PartyPic");
			PartySSierPic = "http://www.funsumer.net/"
					+ wide.getString("PartySSierPic");

			PartyName = wide.getString("PartyName");
			PartySSierName = wide.getString("PartySSierName");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		TextView partyname = (TextView) m_header03.findViewById(R.id.partyname);
		partyname.setText(PartyName);

		TextView partyssiername = (TextView) m_header03
				.findViewById(R.id.partyssiername);
		partyssiername.setText(PartySSierName);

		ImageView partypic = (ImageView) m_header03.findViewById(R.id.partypic);
		imageloader.displayImage(PartyPic, partypic, options);

		// 1 is joined, 0 is not joined
		if (opt7_joined.equals("1")) {
			btn_join = (Button) m_header03.findViewById(R.id.btn_join);
			btn_join.setVisibility(View.INVISIBLE);

			btn_invite = (Button) m_header03.findViewById(R.id.btn_invite);
			btn_invite.setVisibility(View.VISIBLE);
			btn_invite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							inviteFR.class);
					intent.putExtra("userid", userid);
					intent.putExtra("FROM_PARTY", "1");
					intent.putExtra("partyid", partyid);
					startActivity(intent);

				}
			});
		} else if (opt7_joined.equals("0")) {
			btn_join = (Button) m_header03.findViewById(R.id.btn_join);
			btn_join.setVisibility(View.VISIBLE);
			btn_join.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new PartyjoinDialog(getApplicationContext(),
							PartyjoinDialog.TYPE_BASIC_OK, null).show();

					// joinParty();
					// btn_join.setVisibility(View.INVISIBLE);
					// btn_invite.setVisibility(View.VISIBLE);
				}
			});
			btn_invite = (Button) m_header03.findViewById(R.id.btn_invite);
			btn_invite.setVisibility(View.INVISIBLE);
		}

		Log.e("party", "opt_PartySSierID = " + opt_PartySSierID);

		setting_off = (Button) m_header03.findViewById(R.id.setting_off);
		if (opt_PartySSierID.equals(mynoteid)) {
			setting_off.setVisibility(View.VISIBLE);
		} else {
			setting_off.setVisibility(View.INVISIBLE);
		}
		setting_off.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					setting_off
							.setBackgroundResource(R.drawable.icon_setting_on);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					setting_off
							.setBackgroundResource(R.drawable.icon_setting_off);
				}
				return false;
			}
		});
		setting_off.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new SettingDialog(v.getContext(), SettingDialog.TYPE_BASIC_OK,
						null, partyid).show();
			}
		});

		// try {
		// partypic.setImageBitmap((Bitmap) getURLImage(new URL(PartyPic)));
		//
		// Bitmap bitmap = (Bitmap) getURLImage(new URL(PartySSierPic));
		// partyssierpic.setImageBitmap(getRoundedCornerImage(bitmap));
		//
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }
	}

	public void joinParty() {

		try {

			HttpClient client = new DefaultHttpClient();
			String postURL = "http://funsumer.net/json/";
			HttpPost post = new HttpPost(postURL);

			List params1 = new ArrayList();
			params1.add(new BasicNameValuePair("oopt", "12"));
			params1.add(new BasicNameValuePair("mynoteid", mynoteid));
			params1.add(new BasicNameValuePair("pid", partyid));
			params1.add(new BasicNameValuePair("position", "1"));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,
					HTTP.UTF_8);
			post.setEntity(ent);

			HttpResponse responsePOST = client.execute(post);

			HttpEntity resEntity = responsePOST.getEntity();

		} catch (Exception e) {

			// TODO: handle exception
		}

	}

}
