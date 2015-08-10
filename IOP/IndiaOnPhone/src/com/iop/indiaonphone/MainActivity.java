package com.iop.indiaonphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iop.indiaonphone.AsyncTasks.MobileAppRegisterAsyncTask;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * 
 * @author Atish Agrawal
 * 
 */

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPref = getSharedPreferences(
				ApplicationUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

		String mobile = sharedPref.getString(JSONUtils.MOBILE, null);
		if (TextUtils.isEmpty(mobile)) {
			// No Mobile number present in the shared preferences. It means user
			// has not been registered yet. Shared preferences must have a value
			// once a user submits his phone number and name to the webserver
		} else {
			// /Shared Preferences already has the phone number. It means, user
			// has already been registered on the webserver. Directly put him to
			// chat screen.

			startActivity(new Intent(MainActivity.this, ChatHomeActivity.class));
			finish();

		}

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

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// Animating the layout

		LinearLayout linearLayoutMainActivity = (LinearLayout) findViewById(R.id.linearLayoutMainActivity);

		Animation animFadeIn = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.fade_in);

		// set animation listener
		animFadeIn.setAnimationListener(null);

		linearLayoutMainActivity.startAnimation(animFadeIn);

	}

	public void yesMethod(View view) {
		// Checking For blank fields

		EditText editTextFullName = (EditText) findViewById(R.id.edFullName);
		String fullName = editTextFullName.getText().toString();
		EditText editTextPhone = (EditText) findViewById(R.id.edPhone);
		String phone = editTextPhone.getText().toString();

		if (TextUtils.isEmpty(fullName)) {

			// Full Name field is blank

			editTextFullName.setError("Name is mandatory");

		} else if (TextUtils.isEmpty(phone)) {

			// Phone Number field is blank

			editTextPhone.setError("Phone number is mandatory");

		} else {

			// Both the fields are entered

			// Checking for Internet Connection
			if (ProjectUtils.isConnectedToInternet(MainActivity.this)) {

				// Device is connected to Internet

				new MobileAppRegisterAsyncTask(MainActivity.this, fullName,
						phone).execute();

			} else {

				// Device is not connected to Internet

				Toast.makeText(MainActivity.this, "No Internet connection",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
