package com.softedge.iopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void loginMethod(View view) {

		// Validating text boxes for not null

		if (TextUtils.isEmpty(((EditText) findViewById(R.id.edMobileNumberId))
				.getText().toString())) {

			((EditText) findViewById(R.id.edMobileNumberId))
					.setError("Please enter your mobile number id");
			((EditText) findViewById(R.id.edMobileNumberId)).requestFocus();

			return;

		}

		if (TextUtils.isEmpty(((EditText) findViewById(R.id.edPassword))
				.getText().toString())) {

			((EditText) findViewById(R.id.edPassword))
					.setError("Please enter your password");
			((EditText) findViewById(R.id.edPassword)).requestFocus();

			return;

		}

		startActivity(new Intent(MainActivity.this, Dashboard.class));

	}

	public void registerMethod(View view) {

		startActivity(new Intent(MainActivity.this, RegisterActivity.class));
		finish();
	}

}
