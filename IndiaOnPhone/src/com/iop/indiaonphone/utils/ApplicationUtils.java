package com.iop.indiaonphone.utils;

/**
 * 
 * @author Atish Agrawal
 * 
 */
public class ApplicationUtils {

	public ApplicationUtils() {
		// Empty constructor to prevent accidentally instantiating the class
	}

	/**
	 * 
	 * 
	 * The static API_ID and API_KEY to be provided with each API call
	 */

	public static final int NOTIFICATION_ID = 1;

	public static final String SHARED_PREFERENCES_NAME = "IndiaOnPhoneSharedPreference";
	public static final String CHAT_SHARED_PREFERENCES_NAME = "ChatIndiaOnPhoneSharedPreference";

	/**
	 * India On Phone
	 */

	public static final String MAIN_WEBSITE = "http://www.indiaonphone.com/";

	public static final String MAIN_WEBSITE_WEBSERVICE = MAIN_WEBSITE
			+ "webservice/";

	public static final String CONTACT_DETAIL_API = MAIN_WEBSITE_WEBSERVICE
			+ "contact_detail_test.php";
	public static final String GET_CHAT_CONTACTS_API = MAIN_WEBSITE_WEBSERVICE
			+ "chat_api.php";

	/* Chat APIs */

	public static final String GET_ALL_SMS_API = MAIN_WEBSITE_WEBSERVICE
			+ "all_sms.php";

	public static final String SEND_CHAT_MSG_API = MAIN_WEBSITE_WEBSERVICE
			+ "send_chat_msg.php";

	public static final String GET_CHAT_MSG_API = MAIN_WEBSITE_WEBSERVICE
			+ "get_chat_msg.php";

	public static final String GET_CHAT_NOTIFICATION = MAIN_WEBSITE_WEBSERVICE
			+ "get_notification.php";

}
