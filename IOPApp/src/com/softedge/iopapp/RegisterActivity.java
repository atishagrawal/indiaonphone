package com.softedge.iopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends Activity {

	Spinner spinner2, spinner1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		spinner1 = (Spinner) findViewById(R.id.spinner_register_usertype);

		ArrayAdapter adapter_spinner_user_type = ArrayAdapter
				.createFromResource(this, R.array.UserType,
						R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter_spinner_user_type);
		spinner2 = (Spinner) findViewById(R.id.spinner_register_selectlocation);

		ArrayAdapter adapter_spinner_select_location = ArrayAdapter
				.createFromResource(this, R.array.SelectLocation,
						R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter_spinner_select_location);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		startActivity(new Intent(RegisterActivity.this, MainActivity.class));
		finish();

	}

}
