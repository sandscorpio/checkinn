package com.vobileinc.tvsyncexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CheckInn extends Activity {
	private String[] shows = new String[] {"the_simpsons"};

	private Button mBtnSnap;
	private Button mBtnCheckIn;
	private String mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkinn);
		
		mBtnSnap = (Button) findViewById(R.id.btnSnap);
		mBtnCheckIn = (Button) findViewById(R.id.btnCheckIn);
		
		mBtnSnap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CheckInn.this, TVSYNCExampleActivity.class);
				startActivityForResult(intent, 1);				
			}
		});
		
		mBtnCheckIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CheckInn.this, CheckedIn.class);
				intent.putExtra("title", mTitle);
				startActivityForResult(intent, 2);		
			}
			
		});
	} 
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				String title = data.getStringExtra("result");
				mTitle = getShowName(title);
				mBtnCheckIn.setText("Check into " + mTitle);
				break;
			case RESULT_CANCELED:
				String error = data.getStringExtra("error");
				mBtnCheckIn.setText("Can't check in: " + error);
				break;
			}
			break;
		case 2:
			break;
		}
	}
	
	private String getShowName(String syncShow) {
		for (String s : shows) {
			if (syncShow.startsWith(s)) {
				return s;
			}
		}
		
		return syncShow;
	}
	
}
