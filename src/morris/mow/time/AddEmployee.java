package morris.mow.time;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmployee extends Activity {
	EditText name;
	EditText rate;
	DataManager manager = new DataManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_employee_layout);
		name = (EditText) findViewById(R.id.name_employee);
		rate = (EditText) findViewById(R.id.rate_employee);

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
			addEmployee();
			return true;
		case R.id.adding_cancel:
			close();
			return true;

		}
		return true;

	}

	private void addEmployee() {

		// TODO Auto-generated method stub
		try {
			Employee temp = new Employee();
			temp.setName(name.getText().toString());
			temp.setRate(Float.parseFloat(rate.getText().toString()));

			manager.newEntry(temp);
			manager.close();
			close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Make sure Name and Rate have a correct value.",
					Toast.LENGTH_LONG).show();

		}
			
	}

	private void close() {
		this.finish();
	}

}
