/**
 * @author Atish Agrawal
 */
package com.iop.indiaonphone.AsyncTasks;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.iop.indiaonphone.Adapters.MessagesListAdapter;
import com.iop.indiaonphone.chatUtils.Message;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * @author Atish Agrawal
 * 
 */
public class GetAllSMSAPI extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetAllSMSAPI.class.getSimpleName();

	private String response = "";

	// Chat messages list adapter
	private Context context;
	private MessagesListAdapter adapter;
	private List<Message> listMessages;
	private ListView listViewMessages;
	private String c_to, c_from;

	/**
	 * 
	 */
	public GetAllSMSAPI(Context mContext,
			MessagesListAdapter messagesListAdapter, ListView listView,
			String mC_from, String mC_to) {
		// Initializing the variables

		this.context = mContext;
		this.adapter = messagesListAdapter;
		this.listViewMessages = listView;
		this.c_from = mC_from;
		this.c_to = mC_to;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		// Checking Internet connection

		if (ProjectUtils.isConnectedToInternet(context)) {

			// Connected to internet

			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						ApplicationUtils.GET_ALL_SMS_API);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				JSONObject jsonObjectRequest = new JSONObject();
				jsonObjectRequest.put(JSONUtils.MOBILE, c_from);
				jsonObjectRequest.put(JSONUtils.MOBILE, c_to);

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

		if (result == null) {

			// User not connected to internet

			Toast.makeText(context, "No Internet connection",
					Toast.LENGTH_SHORT).show();

			return;

		}

		if (result) {

			// Server returned true

			// Populating messages

			try {
				JSONObject jsonObjectResponse = new JSONObject(response);

			} catch (JSONException e) {
				// Printing errors
				e.printStackTrace();
			}

		} else {

			// Record not found

		}

	}
}
