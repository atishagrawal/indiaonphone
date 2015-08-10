package com.iop.indiaonphone.AsyncTasks;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.iop.indiaonphone.ChatHomeActivity;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * 
 * @author Atish Agrawal
 * 
 */

public class MobileAppRegisterAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = MobileAppRegisterAsyncTask.class
			.getSimpleName();

	private Context context;
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> phones = new ArrayList<String>();
	private ProgressDialog progressDialog = null;
	private String fullName, phoneNumber;
	private String response = "";

	public MobileAppRegisterAsyncTask(Context callingContext, String fullname,
			String phoneNumber) {

		this.context = callingContext;
		this.fullName = fullname;
		this.phoneNumber = phoneNumber;

	}

	@Override
	protected void onPreExecute() {
		// Displaying a progressBar
		super.onPreExecute();

		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();

	}

	@SuppressWarnings("deprecation")
	@Override
	protected Boolean doInBackground(Void... arg0) {
		// Getting all contacts

		getAllContacts();

		// Preparing JSON

		String jsonString = createJsonString() + "";
		Log.d(TAG, "JSONSTRING: " + jsonString);

		// Calling Webservice

		if (ProjectUtils.isConnectedToInternet(context)) {

			// Connected to internet

			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						ApplicationUtils.CONTACT_DETAIL_API);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				StringEntity entity = new StringEntity(jsonString);
				httpPost.setEntity(entity);
				HttpResponse httpResponse = client.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					HttpEntity httpEntity = httpResponse.getEntity();
					response = EntityUtils.toString(httpEntity);

					Log.d(TAG, "Response: " + response);

					// Processing the response

					JSONObject jsonObjectResponse = new JSONObject(response);

					if (jsonObjectResponse.getInt(JSONUtils.STATUS) == 1) {
						// Server returned 1 as True

						return true;

					} else {
						// Server returned 0 as false along with message

						return false;

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// Not connected to internet

		return null;
	}

	private void getAllContacts() {

		final Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (cursor.moveToNext()) {

			final int phone_id = cursor
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			final String phone = cursor.getString(phone_id);
			final String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			names.add(name);
			phones.add(phone);

		}
	}

	private String createJsonString() {

		// Creating JSON to be sent to the server
		try {
			JSONObject object = new JSONObject();
			JSONArray contacts = new JSONArray();
			JSONArray user_data = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("name", fullName);
			obj.put("mobile", phoneNumber);

			user_data.put(obj);

			object.put("user_data", user_data);

			if (names != null && names.size() != 0) {

				// Putting all the names and phone numbers in an array

				for (int i = 0; i < names.size(); i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", names.get(i));
					jsonObject.put("phone", phones.get(i));
					contacts.put(jsonObject);
				}

				object.put("contact_details", contacts);
			}
			return object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		// Moving to new activity and removing the progress dialog
		super.onPostExecute(result);

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();

		if (result == null) {

			// User not connected to internet

			return;

		}

		if (result) {

			// Server returned true

			JSONObject jsonObjectResponse;
			try {
				jsonObjectResponse = new JSONObject(response);

				// Server returned 1 as True

				Toast.makeText(context,
						jsonObjectResponse.getString(JSONUtils.MESSAGE),
						Toast.LENGTH_SHORT).show();

				// Saving Phone number in the shared preferences
				SharedPreferences sharedPref = context.getSharedPreferences(
						ApplicationUtils.SHARED_PREFERENCES_NAME,
						Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(JSONUtils.MOBILE,
						jsonObjectResponse.getString(JSONUtils.MOBILE));
				editor.commit();

				// Starting the chat home activity

				context.startActivity(new Intent(context,
						ChatHomeActivity.class));
				((Activity) context).finish();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// // Clearing the edittexts
			//
			// EditText editTextFullName = (EditText) ((Activity) context)
			// .findViewById(R.id.edFullName);
			//
			// editTextFullName.setText("");
			// EditText editTextPhone = (EditText) ((Activity) context)
			// .findViewById(R.id.edPhone);
			//
			// editTextPhone.setText("");

		} else {

			// Record not found

		}

	}

}
