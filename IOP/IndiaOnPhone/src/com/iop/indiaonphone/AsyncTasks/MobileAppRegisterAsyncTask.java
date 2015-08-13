package com.iop.indiaonphone.AsyncTasks;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
	private ArrayList<String> images = new ArrayList<String>();
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

				/**
				 * 
				 * New HTTPost Method
				 * 
				 */

				HttpClient httpClient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost(
						ApplicationUtils.CONTACT_DETAIL_API);

				httpPost.setEntity(new StringEntity(jsonString, "UTF8"));
				httpPost.setHeader("Content-type", "application/json");
				HttpResponse httpResponse = httpClient.execute(httpPost);

				int statusCode = httpResponse.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					response = EntityUtils.toString(httpEntity);

					Log.d(TAG, "Response: " + response);

					// Processing the response

					JSONObject jsonObjectResponse = new JSONObject(response);

					switch (jsonObjectResponse.getInt(JSONUtils.STATUS)) {
					case 0:
						// Some error occurred

						return false;

					case 1:

						// Success

						return true;

					case 2:

						// User Already Registered

						return true;

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

			String image_uri = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

			names.add(name);
			phones.add(phone);
			images.add(image_uri);

		}
		// readContacts();
	}

	private String createJsonString() {

		// Creating JSON to be sent to the server
		try {
			JSONObject object = new JSONObject();
			JSONArray contacts = new JSONArray();
			JSONArray user_data = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put(JSONUtils.NAME, fullName);
			obj.put(JSONUtils.MOBILE, phoneNumber);

			user_data.put(obj);

			object.put(JSONUtils.USER_DATA, user_data);

			if (names != null && names.size() != 0) {

				// Putting all the names ,phone numbers and images in an JSON
				// array

				for (int i = 0; i < names.size(); i++) {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put(JSONUtils.CONTACT_NAME, names.get(i));
					jsonObject.put(JSONUtils.CONTACT_MOBILE, phones.get(i));

					if (images.get(i) != null) {

						// User has image

						// Bitmap bitmap = MediaStore.Images.Media.getBitmap(
						// context.getContentResolver(),
						// Uri.parse(images.get(i)));

						jsonObject
								.put(JSONUtils.CONTACT_IMAGE,
										ProjectUtils
												.encodeImageToBase64FromBitmap(MediaStore.Images.Media.getBitmap(
														context.getContentResolver(),
														Uri.parse(images.get(i)))));

					} else {

						jsonObject.put(JSONUtils.CONTACT_IMAGE, images.get(i));

					}

					contacts.put(jsonObject);
				}

				object.put(JSONUtils.CONTACT_DETAILS, contacts);

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

			Toast.makeText(context, "No Internet connection",
					Toast.LENGTH_SHORT).show();

			return;

		}

		JSONObject jsonObjectResponse;
		try {
			jsonObjectResponse = new JSONObject(response);

			if (result) {

				// Server returned true

				// Server returned 1/2 as True

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
				editor.putString(JSONUtils.NAME,
						jsonObjectResponse.getString(JSONUtils.NAME));
				editor.commit();

				// Starting the chat home activity

				context.startActivity(new Intent(context,
						ChatHomeActivity.class));
				((Activity) context).finish();

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

				// Some error occurred

				Toast.makeText(context,
						jsonObjectResponse.getString(JSONUtils.MESSAGE),
						Toast.LENGTH_LONG).show();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readContacts() {
		StringBuffer sb = new StringBuffer();
		sb.append("......Contact Details.....");
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		String phone = null;
		String emailContact = null;
		String emailType = null;
		String image_uri = "";
		Bitmap bitmap = null;
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				image_uri = cur
						.getString(cur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					System.out.println("name : " + name + ", ID : " + id);
					sb.append("\n Contact Name:" + name);
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						sb.append("\n Phone number:" + phone);
						System.out.println("phone" + phone);
					}
					pCur.close();
					Cursor emailCur = cr.query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (emailCur.moveToNext()) {
						emailContact = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						emailType = emailCur
								.getString(emailCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						sb.append("\nEmail:" + emailContact + "Email type:"
								+ emailType);
						System.out.println("Email " + emailContact
								+ " Email Type : " + emailType);
					}
					emailCur.close();
				}
				if (image_uri != null) {
					System.out.println(Uri.parse(image_uri));
					try {
						bitmap = MediaStore.Images.Media.getBitmap(
								context.getContentResolver(),
								Uri.parse(image_uri));
						sb.append("\n Image in Bitmap:" + bitmap);
						System.out.println(bitmap);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sb.append("\n........................................");
			}
		}
	}

}
