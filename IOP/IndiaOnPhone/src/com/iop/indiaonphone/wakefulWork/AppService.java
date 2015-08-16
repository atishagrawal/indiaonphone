/***
  Copyright (c) 2009-11 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.iop.indiaonphone.wakefulWork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.iop.indiaonphone.AsyncTasks.GetChatNotifications;
import com.iop.indiaonphone.utils.ApplicationUtils;
import com.iop.indiaonphone.utils.JSONUtils;

/**
 * 
 * @author Atish Agrawal
 * 
 */

public class AppService extends WakefulIntentService {
	public AppService() {
		super("ChatService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {

		// Checking for data

		SharedPreferences sharedPref = getSharedPreferences(
				ApplicationUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

		String mobile = sharedPref.getString(JSONUtils.MOBILE, null);
		if (TextUtils.isEmpty(mobile)) {
			// No Mobile number present in the shared preferences. It means user
			// has not been registered yet. Shared preferences must have a value
			// once a user submits his phone number and name to the webserver

			Log.e("AppService", "No user data. Hence no message notification");
		} else {
			// /Shared Preferences already has the phone number. It means, user
			// has already been registered on the webserver. Get unread messages
			// notifications from the webserver and display it to the user

			Log.e("AppService", "Firing GetNotifications API");

			new GetChatNotifications(AppService.this).execute();

		}

	}
}
