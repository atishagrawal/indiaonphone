package com.iop.indiaonphone;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iop.indiaonphone.utils.ApplicationUtils;

public class WebViewActivity extends Activity {

	private static final String TAG = WebViewActivity.class.getSimpleName();

	public static final int INPUT_FILE_REQUEST_CODE = 1;
	public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";

	private ValueCallback<Uri[]> mFilePathCallback;
	private ValueCallback<Uri> mUploadMessage;
	private String mCameraPhotoPath;

	private WebView webView;
	private String url = ApplicationUtils.MAIN_WEBSITE;
	private boolean share_status = false;

	private ProgressDialog progressDialog;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		initilize();
	}

	private void initilize() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new myWebClient());

		webView.getSettings().setJavaScriptEnabled(true);

		setChromeClient();

		webView.loadUrl(url);

	}

	private void setChromeClient() {
		//

		webView.setWebChromeClient(new WebChromeClient() {

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				WebViewActivity.this.showAttachmentDialog(uploadMsg);
			}

			// For Android > 3.x
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				WebViewActivity.this.showAttachmentDialog(uploadMsg);
			}

			// For Android > 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				WebViewActivity.this.showAttachmentDialog(uploadMsg);
			}

			public boolean onShowFileChooser(WebView webView,
					ValueCallback<Uri[]> filePathCallback,
					WebChromeClient.FileChooserParams fileChooserParams) {
				if (mFilePathCallback != null) {
					mFilePathCallback.onReceiveValue(null);
				}
				mFilePathCallback = filePathCallback;

				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(WebViewActivity.this
						.getPackageManager()) != null) {
					// Create the File where the photo should go
					File photoFile = null;
					try {
						photoFile = createImageFile();
						takePictureIntent.putExtra("PhotoPath",
								mCameraPhotoPath);
					} catch (IOException ex) {
						// Error occurred while creating the File
						Log.e(TAG, "Unable to create Image File", ex);
					}

					// Continue only if the File was successfully created
					if (photoFile != null) {
						mCameraPhotoPath = "file:"
								+ photoFile.getAbsolutePath();
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(photoFile));
					} else {
						takePictureIntent = null;
					}
				}

				Intent contentSelectionIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
				contentSelectionIntent.setType("image/*");

				Intent[] intentArray;
				if (takePictureIntent != null) {
					intentArray = new Intent[] { takePictureIntent };
				} else {
					intentArray = new Intent[0];
				}

				Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
				chooserIntent.putExtra(Intent.EXTRA_INTENT,
						contentSelectionIntent);
				chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						intentArray);

				startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

				return true;
			}
		});

		// Load the local index.html file
		if (webView.getUrl() == null) {
			webView.loadUrl("file:///android_asset/www/index.html");
		}

	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			if (url.indexOf("tel:") == 0) {

				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse(url));
				startActivity(intent);

			} else if (url.indexOf("sms:") == 0) {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.setData(Uri.parse(url));
				startActivity(sendIntent);
			}

			else {

				view.loadUrl(url);
			}

			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

		}
	}

	// flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// To handle "Back" key press event for WebView to go back to previous
	// screen.
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) { web.goBack();
	 * return true; } return super.onKeyDown(keyCode, event); }
	 */

	/**
	 * More info this method can be found at
	 * http://developer.android.com/training/camera/photobasics.html
	 * 
	 * @return
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File imageFile = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		return imageFile;
	}

	/**
	 * Convenience method to set some generic defaults for a given WebView
	 * 
	 * @param webView
	 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setUpWebViewDefaults(WebView webView) {
		WebSettings settings = webView.getSettings();

		// Enable Javascript
		settings.setJavaScriptEnabled(true);

		// Use WideViewport and Zoom out if there is no viewport defined
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);

		// Enable pinch to zoom without the zoom buttons
		settings.setBuiltInZoomControls(true);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			// Hide the zoom controls for HONEYCOMB+
			settings.setDisplayZoomControls(false);
		}

		// Enable remote debugging via chrome://inspect
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		// We set the WebViewClient to ensure links are consumed by the WebView
		// rather
		// than passed to a browser if it can
		this.webView.setWebViewClient(new WebViewClient());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		Uri[] results = null;

		// Check that the response is a good one
		if (resultCode == Activity.RESULT_OK) {
			if (data == null) {
				// If there is not data, then we may have taken a photo
				if (mCameraPhotoPath != null) {
					results = new Uri[] { Uri.parse(mCameraPhotoPath) };
				}
			} else {
				String dataString = data.getDataString();
				if (dataString != null) {
					results = new Uri[] { Uri.parse(dataString) };
				}
			}
		}

		mFilePathCallback.onReceiveValue(results);
		mFilePathCallback = null;
		return;
	}

	private void showAttachmentDialog(ValueCallback<Uri> uploadMsg) {
		this.mUploadMessage = uploadMsg;

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(WebViewActivity.this
				.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
				takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
			} catch (IOException ex) {
				// Error occurred while creating the File
				Log.e(TAG, "Unable to create Image File", ex);
			}

			// Continue only if the File was successfully created
			if (photoFile != null) {
				mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
			} else {
				takePictureIntent = null;
			}
		}

		Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
		contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
		contentSelectionIntent.setType("image/*");

		Intent[] intentArray;
		if (takePictureIntent != null) {
			intentArray = new Intent[] { takePictureIntent };
		} else {
			intentArray = new Intent[0];
		}

		Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
		chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
		chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

		startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
	}

}
