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
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

	/**
	 * 
	 */
	public GetChatContactsAPI(Context callingContext) {
		// Initializing the variables

		context = callingContext;
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

		} else {

			// Record not found

		}

	}

}
