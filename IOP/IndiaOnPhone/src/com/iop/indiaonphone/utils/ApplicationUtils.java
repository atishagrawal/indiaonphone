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

	public static final String API_ID = "3";
	public static final String API_KEY = "06875425-b293-43af-9966-3566045f6bb95b1f7c5e-f932-4916-87f5-db0b298f2";

	public static final String SHARED_PREFERENCES_NAME = "IndiaOnPhoneSharedPreference";
	public static final String LOGIN_SHARED_PREFERENCES_NAME = "LoginSharedPreference";

	/**
	 * 
	 * Notification Id
	 * 
	 */

	public static final int NOTIFICATION_ID = 1;

	/**
	 * 
	 * 
	 * Application URLs used to make the API Call
	 */

	/**
	 * India On Phone
	 */

	public static final String CONTACT_DETAIL_API = "http://indiaonphone.com/webservice/contact_detail_test.php";

	/**
	 * 
	 * ShotmakersGolf URLs
	 * 
	 */

	public static final String LOGIN_USER_URL = "http://www.shotmakersgolf.com/server/api/user.login";
	public static final String FACEBOOK_LOGIN_USER_URL = "http://www.shotmakersgolf.com/server/api/user.login_fb";
	public static final String GET_USER_DETAILS = "http://www.shotmakersgolf.com/server/api/user.get";
	public static final String REGISTER_USER_URL = "http://www.shotmakersgolf.com/server/api/user.registration";
	public static final String FORGOT_PASSWORD_URL = "http://www.shotmakersgolf.com/server/api/user.forgot_password";
	public static final String FIND_MATCH_URL = "http://www.shotmakersgolf.com/server/api/match.list_requests";
	public static final String ACCEPT_MATCH_URL = "http://www.shotmakersgolf.com/server/api/match.accept";
	public static final String POST_SCORE_URL = "http://www.shotmakersgolf.com/server/api/match.score";
	public static final String POST_MATCH_URL = "http://www.shotmakersgolf.com/server/api/match.post";
	public static final String GET_CHAT_MESSAGES_URL = "http://www.shotmakersgolf.com/server/api/chat.messages";
	public static final String SEND_CHAT_MESSAGE_URL = "http://www.shotmakersgolf.com/server/api/chat.send";
	public static final String GET_CHAT_LIST_URL = "http://www.shotmakersgolf.com/server/api/chat.list";
	public static final String GET_CHAT_NOTIFICATIONS_URL = "http://www.shotmakersgolf.com/server/api/chat.notifications";

	public static final String GET_MATCH_LIST_URL = "http://www.shotmakersgolf.com/server/api/match.list";
	public static final String SUBMIT_SUMMARY_URL = "http://www.shotmakersgolf.com/server/api/round.save";
	public static final String SUBMIT_ASSESSMENT_SUMMARY_URL = "http://www.shotmakersgolf.com/server/api/range.shot_save";

	public static final String COURSE_STATE_LIST_URL = "http://www.shotmakersgolf.com/server/api/state.list";
	public static final String COURSE_CITY_LIST_URL = "http://www.shotmakersgolf.com/server/api/city.list";
	public static final String COURSE_COURSE_NAME_LIST_URL = "http://www.shotmakersgolf.com/server/api/course.list";
	public static final String COURSE_TEES_LIST_URL = "http://www.shotmakersgolf.com/server/api/tees.list";

	/**
	 * 
	 * Shared Preferences Names
	 */
	public static final String SHARED_PREFERENCES_FILE_NAME = "iop";
	public static final String PREFERENCES_TOKEN = "token";

	public static boolean anythingChanged = false;

}
