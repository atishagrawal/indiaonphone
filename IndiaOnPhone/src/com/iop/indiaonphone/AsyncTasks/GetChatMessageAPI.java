/**
 * @author Atish Agrawal
 */
package com.iop.indiaonphone.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iop.indiaonphone.chatUtils.Message;
import com.iop.indiaonphone.interfaces.OnReceivingChatMessages;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * @author Atish Agrawal
 * 
 */
public class GetChatMessageAPI extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetChatMessageAPI.class.getSimpleName();

	private String c_from, c_to, response = "";

	private Context context;

	private OnReceivingChatMessages listener;

	public GetChatMessageAPI(Context mContext, String mC_from, String mC_to,
			OnReceivingChatMessages mlistener) {
		// Initializing the variables

		this.context = mContext;
		this.c_from = mC_from;
		this.c_to = mC_to;
		this.listener = mlistener;

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
						ApplicationUtils.GET_CHAT_MSG_API);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				JSONObject msgJSON = new JSONObject();
				msgJSON.put(JSONUtils.C_FROM, c_from);
				msgJSON.put(JSONUtils.C_TO, c_to);

				StringEntity entity = new StringEntity(msgJSON.toString());
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

			// Received messages. Process here

			try {

				SharedPreferences sharedPref = context.getSharedPreferences(
						ApplicationUtils.CHAT_SHARED_PREFERENCES_NAME,
						Context.MODE_PRIVATE);

				String toName = sharedPref.getString(JSONUtils.CHAT_TO_NAME,
						null);

				JSONObject jsonObject = new JSONObject(response);

				JSONArray chatMessagesJsonArray = new JSONArray(
						jsonObject.getString(JSONUtils.CHAT_MESSAGES));

				List<Message> listMessages = new ArrayList<Message>();

				for (int i = 0; i < chatMessagesJsonArray.length(); i++) {

					JSONObject innerJsonObject = chatMessagesJsonArray
							.getJSONObject(i);

					Message m = null;

					if (innerJsonObject.getBoolean(JSONUtils.HAS_IMAGE)) {
						// Message contains image
						m = new Message(toName, null,
								innerJsonObject.getString(JSONUtils.C_IMAGE),
								false, true);

					} else {

						// Message doesn't contains image

						m = new Message(toName,
								innerJsonObject.getString(JSONUtils.C_MSG),
								null, false, false);

					}

					if (m != null)
						listMessages.add(m);

				}

				if (listMessages.size() > 0)

					listener.onTaskCompleted(listMessages);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			// No messages

		}

	}

}
