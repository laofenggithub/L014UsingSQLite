package com.fs.usingasynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView)findViewById(R.id.textView1);
		findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReadURL("http://www.baidu.com");
			}
		});
	}
	
	public void ReadURL(String url){
		new AsyncTask<String , Float, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					// ��doInBackground���漰UI�����Ĳ�����UI�����Ĳ��������������н���
					URL url = new URL(params[0]);
					URLConnection connection = url.openConnection();
					long total = connection.getContentLength();
					InputStream iStream = connection.getInputStream();
					InputStreamReader isr = new InputStreamReader(iStream);
					BufferedReader br = new BufferedReader(isr);
					String line;
					StringBuilder builder = new StringBuilder();
					while ( (line=br.readLine()) != null ) {
						builder.append(line);
						publishProgress((float)builder.toString().length()/total);
					};
					br.close();
					isr.close();
					iStream.close();
					return builder.toString();
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				Toast.makeText(MainActivity.this, "��ʼ��ȡ", Toast.LENGTH_SHORT).show();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				text.setText(result);
				super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Float... values) {
				System.err.println(values[0]);
				super.onProgressUpdate(values);
			}

			@Override
			protected void onCancelled(String result) {
				// TODO Auto-generated method stub
				super.onCancelled(result);
			}  

			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
			}
		}.execute(url);
	}
}