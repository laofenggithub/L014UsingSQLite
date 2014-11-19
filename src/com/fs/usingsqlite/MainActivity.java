package com.fs.usingsqlite;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ListActivity {

	private SimpleCursorAdapter adapter;
	private EditText etName,etSex;
	private Button btnAdd;
	private Db db;
	private SQLiteDatabase dbRead,dbWrite;
	private OnClickListener btnAddListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			ContentValues cv = new ContentValues();
			cv.put("name", etName.getText().toString());
			cv.put("sex", etSex.getText().toString());	
			
			dbWrite.insert("user", null, cv);
			refreshListView();
		}
	};

	private OnItemLongClickListener listViewItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {
			
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("提醒").setMessage("您确定要删除吗？")
			.setNegativeButton("取消", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Cursor c = adapter.getCursor();
					c.moveToPosition(position);
					
					int itemId = c.getInt(c.getColumnIndex("_id"));
					
					dbWrite.delete("user", "_id=?", new String[]{itemId+""});
					
//					dbWrite.update(table, values, whereClause, whereArgs)
					refreshListView();
				}
			}).show();
			

			
			return true;
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		
		etName = (EditText) findViewById(R.id.etName);
		etSex = (EditText) findViewById(R.id.etSex);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		
		
		btnAdd.setOnClickListener(btnAddListener);
		
		db = new Db(this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		
//		Cursor c = dbRead.query("user", null, null, null, null, null, null, null);
		
		adapter = new SimpleCursorAdapter(this, R.layout.user_list_cell, null, 
				new String[]{"name","sex"}, new int[]{R.id.tvName,R.id.tvSex});
		setListAdapter(adapter);
		
		refreshListView();
//		dbRead.close();
		
		getListView().setOnItemLongClickListener(listViewItemLongClickListener);
		
		
//		Db db = new Db(this);
//		SQLiteDatabase dbWrite = db.getWritableDatabase();
//		ContentValues cv = new ContentValues();
//		cv.put("name", "小子");
//		cv.put("sex", "男");
//		dbWrite.insert("user", null, cv);
//		
//		cv = new ContentValues();
//		cv.put("name", "小美");
//		cv.put("sex", "女");
//		dbWrite.insert("user", null, cv);
//		
//		dbWrite.close();
		
		
//		SQLiteDatabase dbRead = db.getReadableDatabase();
////		new String[]{"name"}, "name=?", new String[]{"小子"}, groupBy, having, orderBy
//		Cursor c = dbRead.query("user", null, null, null, null, null, null);
//		while (c.moveToNext()) {
//			String name = c.getString(c.getColumnIndex("name"));
//			String sex = c.getString(c.getColumnIndex("sex"));
//			System.out.println(String.format("name=%s,sex=%s", name,sex));
//		}
//		
//		dbRead.close();
		
		
	}

	private void refreshListView(){
		Cursor c = dbRead.query("user", null, null, null, null, null, null, null);
		adapter.changeCursor(c);
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
}
