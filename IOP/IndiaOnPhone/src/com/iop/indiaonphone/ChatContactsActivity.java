package com.iop.indiaonphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.iop.indiaonphone.utils.JSONUtils;

/**
 * 
 * @author Atish Agrawal
 * 
 */

public class ChatContactsActivity extends Activity {

	private static final String TAG = ChatContactsActivity.class
			.getSimpleName();

	private String name, mobile;
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_contacts);

		Intent intent = getIntent();

		name = intent.getStringExtra(JSONUtils.CONTACT_NAME);
		mobile = intent.getStringExtra(JSONUtils.CONTACT_MOBILE);

		setTitle(name);

		Log.d(TAG, "Name:" + name + "\nMobile:" + mobile);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
		case R.id.action_attachments:

			// User clicked add attachments menu

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
