package com.vobileinc.tvsyncexample;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.singly.android.client.AsyncApiResponseHandler;
import com.singly.android.client.SinglyClient;
import com.singly.android.client.SinglyClient.Authentication;
import com.singly.android.component.AuthenticatedServicesActivity;
import com.singly.android.component.FriendsListActivity;

public class MainActivity
  extends Activity {

  private static final int DEFAULT_GALLERY = 1;
  private String _title;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Context context = this;
    Intent intent = getIntent();
    _title = intent.getStringExtra("title");

    // example showing all authentication services
    Button fullButton = (Button)findViewById(R.id.fullAuthButton);
    fullButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Intent authServicesActivity = new Intent(context,
          AuthenticatedServicesActivity.class);

        // by default the example doesn't use native auth because it requires
        // registering a facebook app and signing with a debug key. To get
        // started it is easier to do without this.
        authServicesActivity.putExtra("useNativeAuth", false);
        context.startActivity(authServicesActivity);
      }
    });

    // example showing only specified authentication services
    Button limitedButton = (Button)findViewById(R.id.limitedAuthButton);
    limitedButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        // example of how to pass in scope
        Bundle scopes = new Bundle();
        scopes.putCharSequence("linkedin",
          "r_basicprofile r_fullprofile r_emailaddress");

        Intent authServicesActivity = new Intent(context,
          AuthenticatedServicesActivity.class);
        String[] includedServices = {
          "twitter", "facebook"
        };
        authServicesActivity.putExtra("includes", includedServices);
        authServicesActivity.putExtra("scopes", scopes);
        authServicesActivity.putExtra("useNativeAuth", false);
        context.startActivity(authServicesActivity);
      }
    });

    // example showing only specified authentication services
    Button friendsButton = (Button)findViewById(R.id.friendsButton);
    friendsButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Intent friendsListActivity = new Intent(context,
          FriendsListActivity.class);
        context.startActivity(friendsListActivity);
      }
    });

    // example showing upload of photo to facebook
    Button photosButton = (Button)findViewById(R.id.photosButton);
    photosButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        // open the default gallery to select an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Open gallery"),
          DEFAULT_GALLERY);
      }
    });
    
    Button postStatusButton = (Button)findViewById(R.id.postStatusButton);
    postStatusButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// share a status
			SinglyClient singlyClient = SinglyClient.getInstance();
			Map post = new HashMap();
			Authentication auth = singlyClient.getAuthentication(MainActivity.this);
		    post.put("access_token", auth.accessToken);
			post.put("body", _title);
			post.put("to", "twitter");
			
			
			singlyClient.doPostApiRequest(MainActivity.this, "/types/statuses", null,
			    post, new AsyncApiResponseHandler() {
			 
			  @Override
			  public void onSuccess(String response) {
			     Log.d("singly", response);
			  }
			 
			  @Override
			  public void onFailure(Throwable error, String message) {
				 Log.d("singly error", message);
			  }
			});
			
		}
    	
    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    // when the default gallery returns
    if (resultCode == RESULT_OK && requestCode == DEFAULT_GALLERY) {

      // get the uri of the choosen image
      Uri theChoosenOne = data.getData();
      String[] projection = {
        MediaStore.Images.Media.DATA
      };
      Cursor cursor = managedQuery(theChoosenOne, projection, null, null, null);
      int column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      final String imagePath = cursor.getString(column_index);
      cursor.close();

      // read the image into a byte array, could also have just passed in the
      // file handle into the post params
      byte[] imageBytes = null;
      try {
        imageBytes = FileUtils.readFileToByteArray(new File(imagePath));
      }
      catch (IOException e) {
        Toast.makeText(MainActivity.this,
          "Error getting picture to post", Toast.LENGTH_SHORT).show();
        return;
      }

      // add the image as a file to the post request params
      SinglyClient singlyClient = SinglyClient.getInstance();
      Map<String, Object> postParams = new HashMap<String, Object>();
      Authentication auth = singlyClient.getAuthentication(this);
      postParams.put("access_token", auth.accessToken);
      postParams.put("photo", imageBytes);
      postParams.put("to", "facebook");

      // do the photo post request to facebook
      singlyClient.doPostApiRequest(this, "/types/photos", null, postParams,
        new AsyncApiResponseHandler() {

          @Override
          public void onSuccess(String response) {
            Toast.makeText(MainActivity.this, "Photo posted to facebook",
              Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onFailure(Throwable error, String message) {
            Log.e(MainActivity.class.getSimpleName(), message, error);
            Toast.makeText(MainActivity.this,
              "Error posting photo to facebook", Toast.LENGTH_SHORT).show();
          }

        });

    }
  }
}
