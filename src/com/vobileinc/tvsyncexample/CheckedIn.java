package com.vobileinc.tvsyncexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckedIn extends Activity {
	private String[] shows = new String[] {"the_simpsons"};

	private TextView mTxtMsg;
	private Button mBtnShare;
	private String _show;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkedin);
		
		mTxtMsg = (TextView) findViewById(R.id.txtMsg);
		mBtnShare = (Button) findViewById(R.id.btnShare);
		
		Intent intent = getIntent();
		final String title = intent.getStringExtra("title");
		mTxtMsg.setText("Congratulations, you're now the mayor of " + title + "!");
		
		mBtnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CheckedIn.this, MainActivity.class);
				intent.putExtra("title", title);
				startActivityForResult(intent, 2);		
			}
			
		});
	} 
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case 2:
			switch (resultCode) {
			case RESULT_OK:
				break;
			case RESULT_CANCELED:
				break;
			}
			break;
		}
		
		finish();
	}
	
}
