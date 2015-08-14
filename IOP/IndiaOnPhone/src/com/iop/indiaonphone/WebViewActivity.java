package com.iop.indiaonphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iop.indiaonphone.utils.ApplicationUtils;

public class WebViewActivity extends Activity {

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
		startWebView(url);

	}

	private void startWebView(String url) {
		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// public void onLoadResource(WebView view, String url) {
			// if (progressDialog == null) {
			// progressDialog = new ProgressDialog(WebViewActivity.this);
			// progressDialog.setMessage("Loading...");
			// // progressDialog.setCancelable(false);
			// progressDialog.show();
			// }
			// }
			//
			// public void onPageFinished(WebView view, String url) {
			// try {
			// if (progressDialog.isShowing()) {
			// progressDialog.dismiss();
			// progressDialog = null;
			//
			// }
			// } catch (Exception exception) {
			// exception.printStackTrace();
			// }
			// }

		});

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}
}
