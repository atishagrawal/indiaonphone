package com.iop.indiaonphone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iop.indiaonphone.Adapters.MessagesListAdapter;
import com.iop.indiaonphone.AsyncTasks.GetAllSMSAPI;
import com.iop.indiaonphone.AsyncTasks.GetChatMessageAPI;
import com.iop.indiaonphone.AsyncTasks.SendChatMessageAPI;
import com.iop.indiaonphone.chatUtils.Message;
import com.iop.indiaonphone.interfaces.OnReceivingChatMessages;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.DateTimeUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * 
 * @author Atish Agrawal
 * 
 */

public class ChatContactsActivity extends Activity implements
		OnReceivingChatMessages {

	private static final String TAG = ChatContactsActivity.class
			.getSimpleName();

	private Handler mHandler = new Handler();

	private Button btnSend;
	private EditText inputMsg;

	// Chat messages list adapter
	private MessagesListAdapter adapter;
	private List<Message> listMessages;
	private ListView listViewMessages;

	private static String c_to, c_from, c_msg, c_image, c_timestamp;

	private static String fromName, toName;

	private boolean hasImage = false;
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_contacts);

		Intent intent = getIntent();

		toName = intent.getStringExtra(JSONUtils.CONTACT_NAME);
		c_to = intent.getStringExtra(JSONUtils.CONTACT_MOBILE);

		SharedPreferences sharedPref = getSharedPreferences(
				ApplicationUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

		c_from = sharedPref.getString(JSONUtils.MOBILE, null);
		fromName = sharedPref.getString(JSONUtils.NAME, null);

		setTitle(toName);

		Log.d(TAG, "Name:" + toName + "\nMobile:" + c_to);

		// Saving values in chatSharedPreference

		SharedPreferences sharedPref2 = getSharedPreferences(
				ApplicationUtils.CHAT_SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPref2.edit();
		editor.putString(JSONUtils.CHAT_FROM_NAME, fromName);
		editor.putString(JSONUtils.CHAT_FROM_PHONE, c_from);

		editor.putString(JSONUtils.CHAT_TO_NAME, toName);
		editor.putString(JSONUtils.CHAT_TO_PHONE, c_to);
		editor.commit();

		btnSend = (Button) findViewById(R.id.btnSend);
		inputMsg = (EditText) findViewById(R.id.inputMsg);
		listViewMessages = (ListView) findViewById(R.id.list_view_messages);

		// Adding values in the shared preferences

		// Getting the person name from previous screen

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Sending message to web server

				c_msg = inputMsg.getText().toString();

				if (TextUtils.getTrimmedLength(c_msg) > 0) {

					Message m = new Message(fromName, c_msg, null, true, false);
					appendMessage(m);

				}

				// Clearing the input filed once message was sent
				inputMsg.setText("");

			}

		});

		listMessages = new ArrayList<Message>();

		adapter = new MessagesListAdapter(this, listMessages);
		listViewMessages.setAdapter(adapter);

		getAllMessages();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mHandler == null)
			mHandler = new Handler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// Starting handler
		super.onStart();

		// starting the handler

		new Thread(new Runnable() {
			@Override
			public void run() {
				// Waiting the thread for 5 Seconds
				while (true) {
					try {
						Thread.sleep(5000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// Call getChatMessage API

								new GetChatMessageAPI(
										ChatContactsActivity.this, c_from,
										c_to, ChatContactsActivity.this)
										.execute();

							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	/**
	 * 
	 */
	protected void sendMessageToServer() {
		// Sending message to server

		new SendChatMessageAPI(ChatContactsActivity.this, generateMsgJSON())
				.execute();

	}

	private String generateMsgJSON() {
		// Creating JSON String to be sent to server
		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(JSONUtils.C_FROM, c_from);
			jsonObject.put(JSONUtils.C_TO, c_to);
			jsonObject.put(JSONUtils.C_MSG, c_msg);
			jsonObject.put(JSONUtils.C_IMAGE, c_image);
			jsonObject.put(JSONUtils.HAS_IMAGE, hasImage);
			jsonObject.put(JSONUtils.C_TIMESTAMP, DateTimeUtils.getTime());

			hasImage = false;
			c_image = "";

			return jsonObject.toString();
		} catch (JSONException e) {
			// An Exception occurred
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 */
	private void getAllMessages() {

		// Fire get all messages API

		new GetAllSMSAPI(ChatContactsActivity.this, c_from, c_to,
				ChatContactsActivity.this).execute();

	}

	/**
	 * Appending message to list view
	 * */
	private void appendMessage(final Message m) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				listMessages.add(m);

				adapter.notifyDataSetChanged();

				// Playing device's notification
				playBeep();

				// Sending message to server after adding it to the listview

				sendMessageToServer();

			}
		});
	}

	/**
	 * Plays device's default notification sound
	 * */
	public void playBeep() {

		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			hasImage = true;

			if (!TextUtils.isEmpty(picturePath)) {
				// User Added Image

				File imgFile = new File(picturePath);

				if (imgFile.exists()) {

					c_image = ProjectUtils
							.encodeImageToBase64FromImagePath(imgFile
									.getAbsolutePath());

				}

			}

			Message m = new Message(fromName, null, c_image, true, true);
			appendMessage(m);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// Clearing the handler
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		mHandler = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.iop.indiaonphone.interfaces.OnReceivingChatMessages#onTaskCompleted()
	 */
	@Override
	public void onTaskCompleted(final List<Message> messageList) {
		// Chat messages received

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				listMessages.addAll(messageList);

				adapter.notifyDataSetChanged();

				// Playing device's notification
				playBeep();

				// Sending message to server after adding it to the listview

			}
		});

	}

}
