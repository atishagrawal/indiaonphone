package com.softedge.iopapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends Activity {

	Spinner spinner2,spinner1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		spinner1 = (Spinner) findViewById(R.id.spinner_register_usertype);

		ArrayAdapter adapter_spinner_user_type = ArrayAdapter.createFromResource(this,
				R.array.UserType, R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter_spinner_user_type);
		spinner2 = (Spinner) findViewById(R.id.spinner_register_selectlocation);

		ArrayAdapter adapter_spinner_select_location = ArrayAdapter.createFromResource(this,
				R.array.SelectLocation, R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter_spinner_select_location);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
