/**
 * 
 */
package com.iop.indiaonphone.AsyncTasks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iop.indiaonphone.MainActivity;
import com.iop.indiaonphone.R;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;

/**
 * @author Atish Agrawal
 * 
 */
public class GetChatNotifications extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetChatNotifications.class
			.getSimpleName();

	private Context context = null;
	private String response = null;
	private String notificationMessage = null;

	/**
	 * 
	 */
	public GetChatNotifications(Context serviceContext) {
		// Initializing the variable

		this.context = serviceContext;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@SuppressWarnings({ "deprecation" })
	@Override
	protected Boolean doInBackground(Void... params) {
		// Making the api call

		SharedPreferences sharedPref = context.getSharedPreferences(
				ApplicationUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

		String mobile = sharedPref.getString(JSONUtils.MOBILE, null);
		String name = sharedPref.getString(JSONUtils.NAME, null);

		if (TextUtils.isEmpty(mobile)) {

			// No token found. Do nothing

			return false;

		} else {

			try {

				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						ApplicationUtils.GET_CHAT_NOTIFICATION);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				JSONObject msgJSON = new JSONObject();
				msgJSON.put(JSONUtils.NAME, name);
				msgJSON.put(JSONUtils.MOBILE, mobile);

				StringEntity entity = new StringEntity(msgJSON.toString());
				httpPost.setEntity(entity);
				HttpResponse httpResponse = client.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					HttpEntity httpEntity = httpResponse.getEntity();
					response = EntityUtils.toString(httpEntity);

					Log.d(TAG, "Response: " + response);

					if (TextUtils.isEmpty(response)) {

						// Response is null

						return false;

					} else {

						// There is some response

						JSONObject jsonObj = new JSONObject(response);

						switch (jsonObj.getInt(JSONUtils.STATUS)) {
						case 0:

							// Some Error

							break;
						case 1:

							// Some unread messages are present. Getting the
							// names of the senders whose messages are unread.

							JSONArray jsonArraySenders = jsonObj
									.getJSONArray(JSONUtils.SENDERS);

							if (jsonArraySenders.length() > 1) {

								int numberOfSender = jsonArraySenders.length() - 1;

								JSONObject jsonObjectSenderNames = jsonArraySenders
										.getJSONObject(0);

								notificationMessage = jsonObjectSenderNames
										.getString(JSONUtils.NAME)
										+ " and "
										+ numberOfSender
										+ " others sent you message";
							} else {

								JSONObject jsonObjectSenderNames = jsonArraySenders
										.getJSONObject(0);

								notificationMessage = jsonObjectSenderNames
										.getString(JSONUtils.NAME)
										+ " sent you message";

							}

							return true;
						case 2:

							// No unread messages

							return false;

						}

					}

				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		// Displaying notification if result is true
		super.onPostExecute(result);

		if (result == null) {

			// No internet available

			Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG)
					.show();

			return;

		}

		if (result) {

			displayNotification();

		}

	}

	@SuppressLint("NewApi")
	protected void displayNotification() {

		if (!TextUtils.isEmpty(notificationMessage)) {

			// Invoking the default notification service
			Notification.Builder mBuilder = new Notification.Builder(context);
			// Define sound URI

			mBuilder.setContentTitle("India On Phone");
			mBuilder.setContentText(notificationMessage);
			mBuilder.setTicker("IOP: New unread messages");
			mBuilder.setSmallIcon(R.drawable.ic_launcher);
			mBuilder.setAutoCancel(true);

			// Increase notification number every time a new notification
			// arrives
			mBuilder.setNumber(ApplicationUtils.NOTIFICATION_ID);

			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, MainActivity.class);
			resultIntent.putExtra("notificationId",
					ApplicationUtils.NOTIFICATION_ID);

			// This ensures that navigating backward from the Activity leads out
			// of
			// the app to Home page
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent
			stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_ONE_SHOT // can only be used once
					);
			// start the activity when the user clicks the notification text
			mBuilder.setContentIntent(resultPendingIntent);

			NotificationManager myNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// pass the Notification object to the system
			myNotificationManager.notify(ApplicationUtils.NOTIFICATION_ID,
					mBuilder.build());
		}
	}

}
