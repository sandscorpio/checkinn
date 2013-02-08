package com.vobileinc.tvsyncexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CheckInn extends Activity {

	private Button mBtnSnap;
	private Button mBtnCheckIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkinn);
		
		mBtnSnap = (Button) findViewById(R.id.btnSnap);
		mBtnCheckIn = (Button) findViewById(R.id.mBtnCheckIn);
		
		mBtnSnap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mBtnCheckIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	} 

	
}
