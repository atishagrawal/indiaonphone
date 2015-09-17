package com.softedge.iopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		startActivity(new Intent(RegisterActivity.this, MainActivity.class));
		finish();

	}

}
