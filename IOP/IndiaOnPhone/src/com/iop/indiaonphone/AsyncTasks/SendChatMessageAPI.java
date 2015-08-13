/**
 * @author Atish Agrawal
 */
package com.iop.indiaonphone.AsyncTasks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;
import com.iop.indiaonphone.utils.ProjectUtils;

/**
 * @author Atish Agrawal
 * 
 */
public class SendChatMessageAPI extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = SendChatMessageAPI.class.getSimpleName();

	private String response = "";

	private Context context;
	private String msgJSON;

	/**
	 * 
	 * @param mContext
	 *            The calling context
	 * @param mMsgJSON
	 *            The String json to be sent
	 * 
	 * <br>
	 * 
	 *            <pre>
	 * {
	 * 
	 * c_from:Atish
	 * c_to:Vinod
	 * c_msg:hi
	 * c_image:
	 * hasImage:
	 * c_timestamp:
	 * 
	 * }
	 * </pre>
	 * 
	 * 
	 */
	public SendChatMessageAPI(Context mContext, String mMsgJSON) {
		// Initializing the variables

		this.context = mContext;
		this.msgJSON = mMsgJSON;
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
						ApplicationUtils.SEND_CHAT_MSG_API);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				StringEntity entity = new StringEntity(msgJSON);
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

			// Message Delivered

		} else {

			// Message not delivered. Sending message again.

			new SendChatMessageAPI(context, msgJSON).execute();

		}

	}
}
