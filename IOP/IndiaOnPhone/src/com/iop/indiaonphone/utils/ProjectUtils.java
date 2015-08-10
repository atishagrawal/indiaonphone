package com.iop.indiaonphone.utils;

/**
 * 
 * @author Atish Agrawal
 *
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

public class ProjectUtils {

	public static boolean isConnectedToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	/**
	 * Encodes the byte array into base64 string
	 * 
	 * @param imageByteArray
	 *            - byte array
	 * @return String a {@link java.lang.String}
	 */
	public static String encodeImage(byte[] imageByteArray) {
		return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
	}

	/**
	 * Decodes the base64 string into byte array
	 * 
	 * @param imageDataString
	 *            - a {@link java.lang.String}
	 * @return byte array
	 */
	public static byte[] decodeImage(String imageDataString) {
		return Base64.decode(imageDataString, Base64.DEFAULT);
	}

	/**
	 * * function to find largest of three numbers in Java using ternary
	 * operator * @param one * @param two * @param three * @return biggest of
	 * three numbers
	 */
	public static int greatestOfThreeUsingTernaryOperator(int one, int two,
			int three) {
		return (one > two) ? (one > three ? one : three) : (two > three ? two
				: three);
	}

}
