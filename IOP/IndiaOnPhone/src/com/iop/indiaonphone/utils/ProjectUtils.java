package com.iop.indiaonphone.utils;

/**
 * 
 * @author Atish Agrawal
 *
 */
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
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

	/**
	 * 
	 * @param filePath
	 *            The path of the file to be converted to Base64
	 * @return
	 */

	public static String encodeImageToBase64FromImagePath(String filePath) {

		if (TextUtils.isEmpty(filePath)) {
			// No filepath provided. Returning null

			return null;

		} else {

			// Returning converted image from the path provided

			Bitmap bm = BitmapFactory.decodeFile(filePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
																// bitmap
																// object
			byte[] byteArrayImage = baos.toByteArray();

			return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

		}
	}

	/**
	 * 
	 * @param bitmap
	 *            The bitmap to be converted
	 * @return
	 */

	public static String encodeImageToBase64FromBitmap(Bitmap bitmap) {

		if (bitmap != null) {
			// No filepath provided. Returning null

			// Returning converted image from the path provided

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); // bm is the
			// bitmap
			// object
			byte[] byteArrayImage = baos.toByteArray();

			return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

		}
		return null;
	}

}
