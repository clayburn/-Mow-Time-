package morris.mow.time;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddLawn extends Activity {
	EditText name, phone;
	DatePicker date;
	DataManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Set layout
		setContentView(R.layout.activity_add);
		name = (EditText) findViewById(R.id.adding_name_edittext);
		phone = (EditText) findViewById(R.id.adding_phone_edittext);
		date = (DatePicker) findViewById(R.id.adding_date);
		manager = new DataManager(this);
	}

	public void closeThis() {
		this.finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.adding, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.adding_save:
			addClient();
			return true;
		case R.id.adding_cancel:
			closeThis();
			return true;

		}
		return true;

	}

	public void addClient() {
		
		String tempName = "";
		String tempPhone ="";
		// Will grab all data in text fields and use them to create a
		// new Lawn instance.
		try {
			tempName = name.getText().toString();
			tempPhone = phone.getText().toString();					
			if ((tempName == "")) {
				Toast.makeText(getApplicationContext(),
						"Make sure Name has a value.",
						Toast.LENGTH_LONG).show();
			} else {
				Calendar newDate = Calendar.getInstance();
				newDate.setTimeZone(TimeZone.getTimeZone("GMT"));
				newDate.set(Calendar.YEAR, date.getYear());
				newDate.set(Calendar.MONTH, date.getMonth());
				newDate.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

				newDate.clear(Calendar.HOUR);
				newDate.clear(Calendar.HOUR_OF_DAY);
				newDate.clear(Calendar.MINUTE);
				newDate.clear(Calendar.SECOND);
				newDate.clear(Calendar.MILLISECOND);

				long epochToCutDate = newDate.getTimeInMillis();

				Lawn newLawn = new Lawn(tempName);

				newLawn.setToBeCut(epochToCutDate);
				newLawn.setPhoneNumber(tempPhone);
				newLawn.setLastCut(0);
				manager.newEntry(newLawn);
				manager.close();
				closeThis();
			}
			

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Make sure Name has a value.", Toast.LENGTH_LONG)
					.show();
		}
	}
}
