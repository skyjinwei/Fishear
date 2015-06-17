package com.example.fishear;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fishear.WordInfoActivity.DownLoadTask;
import com.github.mikephil.charting.charts.LineChart;    
import com.github.mikephil.charting.components.Legend;    
import com.github.mikephil.charting.components.Legend.LegendForm;    
import com.github.mikephil.charting.data.Entry;    
import com.github.mikephil.charting.data.LineData;    
import com.github.mikephil.charting.data.LineDataSet;  

public class MainActivity extends ActionBarActivity {
	

	private Button tingBtn;
	private TextView locationView;
	private TextView satisfKeyView;
	private TextView satisfValueView;
	private TextView cityWordKeyView;
	private TextView cityWordValue0View;
	private TextView cityWordValue1View;
	private TextView cityWordValue2View;
	private TextView cityWordValue3View;
	private TextView cityWordValue4View;
	private ProgressDialog progressDialog;
	
	private EditText tingEdit;
	private String tingWord="";
	private String locationStr = "广州";
	private float satisfValue;
	private String wordShown;
	private ArrayList<String> associWordsList= new ArrayList<String>();

	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				//progressDialog.dismiss(); 
				String jsonString = (String) msg.obj;
				boolean obtained=false;
				try {
					JSONObject jo = new JSONObject(jsonString);
					
					double xNegative = jo.getJSONObject("PnN").getDouble("negative");
					satisfValue = (float)(1-xNegative);
					JSONObject associated = jo.getJSONObject("associated");
					
					obtained = true;		
					for(int i=0;i<5;i++){
						associWordsList.add(associated.getString(Integer.toString(i)));
					}
				} catch (JSONException e) {
					wordShown = "未能获取地方热词";
					satisfKeyView.setText(wordShown);
					e.printStackTrace();
				}
				if(obtained){
				locationView.setText(locationStr);
				satisfKeyView.setText("城市满意度：");
				satisfValueView.setText(satisfValue+"");
				cityWordKeyView.setText("城市热词：");
				cityWordValue0View.setText(associWordsList.get(0));
				cityWordValue1View.setText(associWordsList.get(1));
				cityWordValue2View.setText(associWordsList.get(2));
				cityWordValue3View.setText(associWordsList.get(3));
				cityWordValue4View.setText(associWordsList.get(4));
				setCityWordListener();
				}
				super.handleMessage(msg);
			}
		};
		//progressDialog = ProgressDialog.show(MainActivity.this,"听鱼正在搜寻...","") ;
		new Thread(new DownLoadTask(locationStr)).start();
		findView();
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getBundleExtra("bundle");
			tingWord = bundle.getString("tingWord");
			tingEdit.setText(tingWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setlistener();
	}


	protected void setCityWordListener() {
		cityWordValue0View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tingEdit.setText(associWordsList.get(0));
			}
		});
		cityWordValue1View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tingEdit.setText(associWordsList.get(1));
			}
		});
		cityWordValue2View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tingEdit.setText(associWordsList.get(2));
			}
		});
		cityWordValue3View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tingEdit.setText(associWordsList.get(3));
			}
		});
		cityWordValue4View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tingEdit.setText(associWordsList.get(4));
			}
		});
	}


	private void setlistener() {
		
		tingBtn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				tingWord = tingEdit.getText().toString();
				if(!tingWord.isEmpty()){
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, WordInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tingWord", tingWord);
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);}
			}
		});
		
	}


	private void findView() {
		System.out.println("Finding mainacitvity view...");
		tingBtn = (Button)findViewById(R.id.tingbutton);
		tingEdit = (EditText)findViewById(R.id.tingWord);
		tingEdit.setBackgroundColor(Color.rgb(255, 255, 255));
		tingEdit.getBackground().setAlpha(150);
		locationView = (TextView)findViewById(R.id.locationView);
		satisfKeyView = (TextView)findViewById(R.id.satisfKey);
		satisfValueView = (TextView)findViewById(R.id.satisValue);
		cityWordKeyView = (TextView)findViewById(R.id.cityWordKey);
		cityWordValue0View = (TextView)findViewById(R.id.cityWord0);
		cityWordValue1View = (TextView)findViewById(R.id.cityWord1);
		cityWordValue2View = (TextView)findViewById(R.id.cityWord2);
		cityWordValue3View = (TextView)findViewById(R.id.cityWord3);
		cityWordValue4View = (TextView)findViewById(R.id.cityWord4);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
				System.out.println("Start searching..");
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
