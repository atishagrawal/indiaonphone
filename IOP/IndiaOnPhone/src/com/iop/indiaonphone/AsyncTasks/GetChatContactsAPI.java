/**
 * 
 */
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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iop.indiaonphone.R;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * @author Atish Agrawal
 * 
 */
public class GetChatContactsAPI extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetChatContactsAPI.class.getSimpleName();

	private Context context;
	private ProgressDialog progressDialog = null;
	private String response = "";

	private ListView listViewChatContacts;

	/**
	 * @param listViewChatContacts
	 * 
	 */
	public GetChatContactsAPI(Context callingContext,
			ListView callingActivityListViewChatContacts) {
		// Initializing the variables

		context = callingContext;
		listViewChatContacts = callingActivityListViewChatContacts;
	}

	@Override
	protected void onPreExecute() {
		// Displaying a progressBar
		super.onPreExecute();

		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading Contacts...");
		progressDialog.setCancelable(false);
		progressDialog.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		// Getting contact number of the current user

		if (ProjectUtils.isConnectedToInternet(context)) {

			// Connected to internet

			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						ApplicationUtils.GET_CHAT_CONTACTS_API);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				SharedPreferences sharedPref = context.getSharedPreferences(
						ApplicationUtils.SHARED_PREFERENCES_NAME,
						Context.MODE_PRIVATE);

				String mobile = sharedPref.getString(JSONUtils.MOBILE, null);

				JSONObject jsonObjectRequest = new JSONObject();
				jsonObjectRequest.put(JSONUtils.MOBILE, mobile);

				StringEntity entity = new StringEntity(
						jsonObjectRequest.toString());
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

			// Adding Users in the ListView

			try {
				JSONObject jsonObjectResponse = new JSONObject(response);

				JSONArray jsonArrayContacts = jsonObjectResponse
						.getJSONArray(JSONUtils.CONTACTS);

				final ArrayList<String> contacts_name = new ArrayList<String>();
				final ArrayList<String> contacts_mobile = new ArrayList<String>();
				final ArrayList<String> contacts_image = new ArrayList<String>();
				final ArrayList<Integer> unread_msgs = new ArrayList<Integer>();

				for (int i = 0; i < jsonArrayContacts.length(); i++) {

					JSONObject innerJsonObject = jsonArrayContacts
							.getJSONObject(i);

					contacts_name.add(innerJsonObject
							.getString(JSONUtils.CONTACT_NAME));
					contacts_mobile.add(innerJsonObject
							.getString(JSONUtils.CONTACT_MOBILE));
					contacts_image.add(innerJsonObject.optString(
							JSONUtils.CONTACT_IMAGE, null));
					unread_msgs.add(innerJsonObject.optInt(
							JSONUtils.UNREAD_MSG, 0));

				}

				ArrayAdapter adapter = new ArrayAdapter(context,
						R.layout.inner_contacts_list, android.R.id.text1,
						contacts_name) {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {

						LayoutInflater inflater = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(
								R.layout.inner_contacts_list, null);

						// View view = getView(position, convertView, parent);
						TextView text1 = (TextView) view
								.findViewById(R.id.txtInnerListContactName);
						TextView text2 = (TextView) view
								.findViewById(R.id.txtInnerListContactPhone);

						text1.setText(contacts_name.get(position));
						text2.setText(contacts_mobile.get(position));

						// Setting image is available

						ImageView imageViewContactImage = (ImageView) view
								.findViewById(R.id.imgInnerListContactImage);

						byte[] decodedString = ProjectUtils
								.decodeImage(contacts_image.get(position));

						if (decodedString.length > 0) {

							Bitmap decodedByte = BitmapFactory.decodeByteArray(
									decodedString, 0, decodedString.length);
							imageViewContactImage.setImageBitmap(decodedByte);

						}

						if (unread_msgs.get(position) > 0) {
							// There are unread messages
							TextView textViewUnreadMessages = (TextView) view
									.findViewById(R.id.txtChatNumberOfUnreadMessages);

							textViewUnreadMessages.setVisibility(View.VISIBLE);
							textViewUnreadMessages.setText(unread_msgs.get(
									position).toString());

						}

						return view;
					}
				};

				listViewChatContacts.setAdapter(adapter);

			} catch (JSONException e) {
				// Printing errors
				e.printStackTrace();
			}

		} else {

			// Record not found

		}

	}
}
