package com.example.fishear;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarDataSet;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WordInfoActivity extends ActionBarActivity {

	private TextView tingWordTV;
	private TextView assocWordView0;
	private TextView assocWordView1;
	private TextView assocWordView2;
	private TextView assocWordView3;
	private TextView assocWordView4;
	private TextView gjcTv;
	private ListView lv;
	private String tingWord;
	private String guanLianCiKey="Searching...";
	String wordShown;
	
	private Handler mHandler;
	private ProgressDialog progressDialog;
	
	private String xTingWord="";
	private double xHeat=0.5;
	private int xIntense=0;
	private double xEmotionDegree=0.3;
	private double xPositive=0.3;
	private double xNegative=0.3;
	private double xNeutral=0.4;
	private double xHappy=0.2;
	private double xHappy_PA=0.15;
	private double xHappy_PE=0.05;
	private double xFond=0.1;
	private double xFond_PD=0.01;
	private double xFond_PH=0.02;
	private double xFond_PG=0.03;
	private double xFond_PB=0.035;
	private double xFond_PK=0.005;
	private double xAngry=0.2;
	private double xAngry_NA=0;
	private double xSad=0.1;
	private double xSad_NB=0.01;
	private double xSad_NJ=0.02;
	private double xSad_NH=0.04;
	private double xSad_PF=0.03;
	private double xFear = 0.2;
	private double xFear_NI=0.05;
	private double xFear_NC=0.12;
	private double xFear_NG=0.03;
	private double xHate=0.05;
	private double xHate_NE=0.01;
	private double xHate_ND=0.02;
	private double xHate_NN=0.03;
	private double xHate_NK=0.035;
	private double xHate_NL=0.005;
	private double xShock=0.05;
	private double xShock_PC=0;
	private ArrayList<String> associWordsList = new ArrayList<String>();
	
	private String testHeat;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_word_info);
		
		
		findView();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss(); 
				String jsonString = (String) msg.obj;
				
				try {
					JSONObject jo = new JSONObject(jsonString);
					xTingWord = jo.getString("word");
					xHeat     = jo.getDouble("heat");
					xIntense  = jo.getInt("intens");
					xEmotionDegree = jo.getDouble("emotionDegree");
					xPositive = jo.getJSONObject("PnN").getDouble("positive");
					xNegative = jo.getJSONObject("PnN").getDouble("negative");
					xNeutral  = jo.getJSONObject("PnN").getDouble("neutral");
					xHappy = jo.getJSONObject("sentiment").getJSONObject("happy").getDouble("total");
					xHappy_PA = jo.getJSONObject("sentiment").getJSONObject("happy").getDouble("PA");
					xHappy_PE = jo.getJSONObject("sentiment").getJSONObject("happy").getDouble("PE");
					xFond  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("total");
					xFond_PD  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("PD");
					xFond_PH  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("PH");
					xFond_PG  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("PG");
					xFond_PB  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("PB");
					xFond_PK  = jo.getJSONObject("sentiment").getJSONObject("fond").getDouble("PK");
					xSad = jo.getJSONObject("sentiment").getJSONObject("sad").getDouble("total");
					xSad_NB = jo.getJSONObject("sentiment").getJSONObject("sad").getDouble("NB");
					xSad_NJ = jo.getJSONObject("sentiment").getJSONObject("sad").getDouble("NJ");
					xSad_NH = jo.getJSONObject("sentiment").getJSONObject("sad").getDouble("NH");
					xSad_PF = jo.getJSONObject("sentiment").getJSONObject("sad").getDouble("PF");
					xFear = jo.getJSONObject("sentiment").getJSONObject("fear").getDouble("total");
					xFear_NI = jo.getJSONObject("sentiment").getJSONObject("fear").getDouble("NI");
					xFear_NC = jo.getJSONObject("sentiment").getJSONObject("fear").getDouble("NC");
					xFear_NG = jo.getJSONObject("sentiment").getJSONObject("fear").getDouble("NG");
					xHate = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("total");
					xHate_NE = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("NE");
					xHate_ND = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("ND");
					xHate_NN = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("NN");
					xHate_NK = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("NK");
					xHate_NL = jo.getJSONObject("sentiment").getJSONObject("hate").getDouble("NL");
					xShock = jo.getJSONObject("sentiment").getJSONObject("shock").getDouble("total");
					xShock_PC = jo.getJSONObject("sentiment").getJSONObject("shock").getDouble("PC");
					JSONObject associated = jo.getJSONObject("associated");
					wordShown = associated.getString("0")+" "
							+associated.getString("1")+" "
							+associated.getString("2")+" "
							+associated.getString("3")+" "
							+associated.getString("4");
							
					for(int i=0;i<8;i++){
						associWordsList.add(associated.getString(Integer.toString(i)));
					}
				} catch (JSONException e) {
					wordShown = "T。T 听鱼未能完成搜寻，请再尝试";
					e.printStackTrace();
				}

				assocWordView0.setText(associWordsList.get(0));
				assocWordView1.setText(associWordsList.get(1));
				assocWordView2.setText(associWordsList.get(2));
				assocWordView3.setText(associWordsList.get(3));
				assocWordView4.setText(associWordsList.get(4));
				gjcTv.setText("关联词：");
				setChartListView();
				setAssocWordListener();
				super.handleMessage(msg);
			}
		};
		init();
		setChartListView();
	}

	

	

	

	protected void setAssocWordListener() {
		assocWordView0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", associWordsList.get(0));
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);
				
			}
		});
		assocWordView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", associWordsList.get(1));
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);
				
			}
		});
		assocWordView2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", associWordsList.get(2));
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);
				
			}
		});
		assocWordView3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", associWordsList.get(3));
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);
				
			}
		});
		assocWordView4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WordInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", associWordsList.get(4));
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);
				
			}
		});
		
	}







	private void setChartListView() {
		ArrayList<ChartItem> list = new ArrayList<ChartItem>();
		int i=1;
		list.add(new PieChartItem(generateHeatData(1), getApplicationContext(),"热度\nHeat"));
		list.add(new PieChartItem(generateEmotionData(2),getApplicationContext(),"情绪化\nEmotional"));
		list.add(new PieChartItem(generatePnNData(3), getApplicationContext(), "立场\nStandpoint"));
		list.add(new BarChartItem(generateSentmData(4),getApplicationContext()));//情感
		list.add(new BarChartItem(generateHappyData(5),getApplicationContext()));//喜
		list.add(new BarChartItem(generateFondData(6),getApplicationContext()));//好
	//	list.add(new BarChartItem(generateAngryData(5),getApplicationContext()));//怒
		list.add(new BarChartItem(generateSorrowData(7),getApplicationContext()));//哀
		list.add(new BarChartItem(generateFearData(8),getApplicationContext()));//惧
		list.add(new BarChartItem(generateHateData(9),getApplicationContext()));
	//	list.add(new BarChartItem(generateHappyData(10),getApplicationContext()));
		
		ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
		lv.setAdapter(cda);
	}

	private BarData generateHateData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xHate_NE,0));
		entries.add(new BarEntry((float)xHate_ND,1));
		entries.add(new BarEntry((float)xHate_NN,2));
		entries.add(new BarEntry((float)xHate_NK,3));
		entries.add(new BarEntry((float)xHate_NL,4));
		BarDataSet d = new BarDataSet(entries, "厌 Hate（情感细分）Total:"+(float)xHate);
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(155, 73, 51),
				Color.rgb(155, 93, 51),
				Color.rgb(155, 113, 51),
				Color.rgb(155, 133, 51),
				Color.rgb(155, 153, 51)
		};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("烦闷");
		q.add("憎恶");
		q.add("贬责");
		q.add("妒忌");
		q.add("怀疑");
		BarData hateData = new BarData(q,d);
		return hateData;
	}







	private BarData generateFearData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xFear_NI,0));
		entries.add(new BarEntry((float)xFear_NC,1));
		entries.add(new BarEntry((float)xFear_NG,2));
		BarDataSet d = new BarDataSet(entries, "惧 Fear（情感细分）Total:"+(float)xFear);
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(154, 113, 215),
				Color.rgb(174, 113, 215),
				Color.rgb(194, 113, 215)
		};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("慌张");
		q.add("恐惧");
		q.add("害羞");
		BarData fearData = new BarData(q,d);
		return fearData;
	}







	private BarData generateSorrowData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xSad_NB,0));
		entries.add(new BarEntry((float)xSad_NJ,1));
		entries.add(new BarEntry((float)xSad_NH,2));
		entries.add(new BarEntry((float)xSad_PF,3));
		BarDataSet d = new BarDataSet(entries, "哀 Sad（情感细分）Total:"+(float)xSad);
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(0, 194, 255),
				Color.rgb(0, 174, 255),
				Color.rgb(0, 154, 255),
				Color.rgb(0, 134, 255)
		};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("悲伤");
		q.add("失望");
		q.add("愧疚");
		q.add("反思");
		BarData angryData = new BarData(q,d);
		return angryData;
	}







	private BarData generateFondData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xFond_PD,0));
		entries.add(new BarEntry((float)xFond_PH,1));
		entries.add(new BarEntry((float)xFond_PG,2));
		entries.add(new BarEntry((float)xFond_PB,3));
		entries.add(new BarEntry((float)xFond_PK,4));
		BarDataSet d = new BarDataSet(entries, "好 Fond（情感细分）Total:"+(float)xFond);
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(255, 255, 0),
				Color.rgb(255, 245, 0),
				Color.rgb(255, 235, 0),
				Color.rgb(255, 225, 0),
				Color.rgb(255, 215, 0)
		};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("尊敬");
		q.add("赞扬");
		q.add("相信");
		q.add("喜爱");
		q.add("祝愿");
		BarData fondData = new BarData(q,d);
		return fondData;
	}







	private BarData generateHappyData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xHappy_PA,0));
		entries.add(new BarEntry((float)xHappy_PE,1));
		BarDataSet d = new BarDataSet(entries, "喜 Delightful（情感细分）Total:"+(float)xHappy);
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(252, 51, 0),
				Color.rgb(255, 204, 0)
		};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("快乐 Happy");
		q.add("安心 Peace");
		BarData happyData = new BarData(q,d);
		return happyData;
	}




	private BarData generateSentmData(int i) {
		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry((float)xHappy, 0));
		entries.add(new BarEntry((float)xFond, 1));
		entries.add(new BarEntry((float)xAngry, 2));
		entries.add(new BarEntry((float)xSad, 3));
		entries.add(new BarEntry((float)xFear, 4));
		entries.add(new BarEntry((float)xHate, 5));
		entries.add(new BarEntry((float)xShock, 6));
		BarDataSet d = new BarDataSet(entries, "情感分析 Sentiment Analysis");
		d.setBarSpacePercent(5f);
		int colors[] = {
				Color.rgb(255, 51, 0),
				Color.rgb(252, 222, 0),
				Color.rgb(51, 255, 0),
				Color.rgb(51, 204, 255),
				Color.rgb(204, 153, 255),
				Color.rgb(153, 51, 51),
				Color.rgb(255, 140, 0)};
		d.setColors(colors);
		d.setHighLightAlpha(50);
		ArrayList<String> q = new ArrayList<String>();
		q.add("喜");
		q.add("好");
		q.add("怒");
		q.add("哀");
		q.add("惧");
		q.add("厌");
		q.add("惊");
		BarData sentimData = new BarData(q,d);
				
		return sentimData;
	}







	private PieData generatePnNData(int i) {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(new Entry((float) xPositive, 0));
		entries.add(new Entry((float) xNeutral, 1));
		entries.add(new Entry((float) xNegative, 2));
		
		PieDataSet d = new PieDataSet(entries, "");
		
		d.setSliceSpace(2f);
		int colors[]={Color.rgb(235, 205, 0),Color.rgb(190, 205, 0),Color.rgb(80, 205, 0)};
		d.setColors(colors);
		ArrayList<String> q = new ArrayList<String>();
		q.add("积极");
		q.add("中立");
		q.add("消极");
		
		PieData PnNData = new PieData(q,d);
		return PnNData;
	}







	private PieData generateEmotionData(int i) {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(new Entry((float) xEmotionDegree, 0));
		entries.add(new Entry((float) (1-xEmotionDegree), 1));
		
		PieDataSet d = new PieDataSet(entries, "");
		
		d.setSliceSpace(2f);
		int colors[]={Color.rgb(255, 153, 0),Color.rgb(0, 204, 255)};
		d.setColors(colors);
		ArrayList<String> q = new ArrayList<String>();
		q.add("激动的");
		q.add("理智的");
		PieData emotionData = new PieData(q,d);
		return emotionData ;
	}
	
	private PieData generateHeatData(int i) {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(new Entry((float) xHeat,0));
		entries.add(new Entry((float) (1-xHeat),1));
		
		PieDataSet d = new PieDataSet(entries, "");
		
		d.setSliceSpace(2f);
		int colors[]={Color.rgb(214, 0, 0),Color.rgb(255, 153, 153)};
		d.setColors(colors);
		ArrayList<String> q = new ArrayList<String>();
		q.add("兴趣高");
		q.add("兴趣低");
		PieData heatData = new PieData(q,d);
		return heatData;
	}

	/** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {
        
        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }
        
        @Override
        public int getItemViewType(int position) {           
            // return the views type
            return getItem(position).getItemType();
        }
        
        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }













	private void init() {
		gjcTv.setText(guanLianCiKey);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		tingWord = bundle.getString("tingWord");
		tingWordTV.setText(tingWord);
		progressDialog = ProgressDialog.show(WordInfoActivity.this,"听鱼正在搜寻...","") ;
		new Thread(new DownLoadTask(tingWord)).start();

	}

	private void findView() {
		tingWordTV = (TextView)findViewById(R.id.tingTextView);
		assocWordView0 = (TextView)findViewById(R.id.assocWord0);
		assocWordView1 = (TextView)findViewById(R.id.assocWord1);
		assocWordView2 = (TextView)findViewById(R.id.assocWord2);
		assocWordView3 = (TextView)findViewById(R.id.assocWord3);
		assocWordView4 = (TextView)findViewById(R.id.assocWord4);
		lv = (ListView)findViewById(R.id.chartListView);
		gjcTv = (TextView)findViewById(R.id.guanLianCi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class DownLoadTask implements Runnable{

		private String tingWord4Search = "";
		
		public DownLoadTask(String tingWord){
			super();
			tingWord4Search = tingWord;
		}
		@Override
		public void run() {
			String resStr = getRemoteInfo(tingWord4Search);
			Message msg = mHandler.obtainMessage();
			msg.obj = resStr;
			mHandler.sendMessage(msg);
			
		}
		private String getRemoteInfo(String tingWord2Search) {
			String url1 = "http://fishear.sinaapp.com/get_data/?kword="+tingWord2Search;
			HttpClient mClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url1);
			StringBuilder sb1 = new StringBuilder();
			
			try{
				HttpResponse mResp = mClient.execute(mHttpGet);
				HttpEntity mEntity = mResp.getEntity();
				InputStream mStream = mEntity.getContent();
				
				int len = 0;
				byte[] b = new byte[1024];
				
				while((len = mStream.read(b,0,b.length)) != -1){
					sb1.append(new String(b,0,len));
				}
			//	JSONObject mJsonObj = new JSONObject(sb1.toString());
				return sb1.toString();
			}catch(Exception e){
				wordShown = "T。T 听鱼未能成功获取网络服务。";
				e.printStackTrace();
			}
			String result = "";
			return result;
			
		}
		
	}
}

