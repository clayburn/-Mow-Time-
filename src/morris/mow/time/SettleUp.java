package morris.mow.time;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettleUp extends Activity {
	TextView placesWorked, hours_display, minutes_display;
	ListView employees;
	List<Lawn> todaysLawns;
	List<Employee> allEmployees;
	DataManager manager;
	long timeTally;
	long singleCutTime;
	int hoursWorked;
	int minutesWorked;
	float hourlyWage;

	Dialog employeeClicked;
	TextView employeeName;
	EditText hours;
	EditText minutes;
	EditText rate;
	EditText pay;
	Button refresh, all;
	Button done;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settle_up_2);
		manager = new DataManager(this);
		if (manager.getStartedLawns() != null) {
			Toast.makeText(getApplicationContext(),
					"You cannot Settle Up with a lawn timer running.",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		// placesWorked = (TextView) findViewById(R.id.todays_work);
		employees = (ListView) findViewById(R.id.employee_list);
		all = (Button) findViewById(R.id.button_breakdown);
		hours_display = (TextView) findViewById(R.id.hour_display);
		minutes_display = (TextView) findViewById(R.id.minute_display);
		todaysLawns = manager.getTodaysCut();
		// Loop through and get times for all cuts
		for (int i = 0; i < todaysLawns.size(); i++) {
			singleCutTime = todaysLawns.get(i).getTimeSpentCutting();
			// placesWorked.append(todaysLawns.get(i).getNameLawn() + ": "
			// + (int) ((singleCutTime / (1000 * 60 * 60)) % 24)
			// + " hours and "
			// + (int) ((singleCutTime / (1000 * 60)) % 60)
			// + " minutes.\n");
			timeTally += singleCutTime;
		}
		hoursWorked = (int) ((timeTally / (1000 * 60 * 60)) % 24);
		minutesWorked = (int) ((timeTally / (1000 * 60)) % 60);
		// placesWorked.append("\nTotal time worked today: " + hoursWorked
		// + " hours and " + minutesWorked + " minutes.");
		// Get all employees
		hours_display.setText(hoursWorked + "");
		minutes_display.setText(minutesWorked + "");
		allEmployees = manager.getAllEmployees();

		// create a list adapter for employees
		String[] list = new String[allEmployees.size()];
		for (int i = 0; i < allEmployees.size(); i++) {
			list[i] = allEmployees.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, list);
		employees.setAdapter(adapter);

		employees.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				employeeClicked = new Dialog(SettleUp.this);
				employeeClicked.setContentView(R.layout.employee_clicked);
				employeeClicked.setTitle("Payment Calculations");
				employeeName = (TextView) employeeClicked
						.findViewById(R.id.employeeTitle);
				hours = (EditText) employeeClicked.findViewById(R.id.editHour);
				minutes = (EditText) employeeClicked
						.findViewById(R.id.editMinute);
				rate = (EditText) employeeClicked.findViewById(R.id.editRate);
				pay = (EditText) employeeClicked.findViewById(R.id.editPay);
				refresh = (Button) employeeClicked
						.findViewById(R.id.buttonRefresh);
				done = (Button) employeeClicked.findViewById(R.id.buttonDone);

				// Set the edit text values
				final Employee temp = allEmployees.get(arg2);
				employeeName.setText(temp.getName());
				hours.setText("" + hoursWorked);
				minutes.setText("" + minutesWorked);
				hourlyWage = temp.getRate();
				rate.setText("" + hourlyWage);

				float finalPay = (hoursWorked * hourlyWage)
						+ (((float) minutesWorked / 60) * hourlyWage);
				pay.setText(String.format("%.2f", finalPay));
				done.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						temp.setPaid(pay.getText().toString());
						temp.setName(employeeName.getText().toString());
						manager.recordPayment(temp);
						Toast.makeText(
								getApplicationContext(),
								"Recorded payment of $" + temp.getPaid()
										+ " to " + temp.getName(),
								Toast.LENGTH_SHORT).show();
						manager.close();
						employeeClicked.dismiss();
					}
				});
				refresh.setOnClickListener(new View.OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						float h;
						float m;
						float p;
						String hour_string = hours.getText().toString();
						String minute_string = minutes.getText().toString();
						String hourlyWage_string = rate.getText().toString();
						if (hour_string.isEmpty())
							h = 0;
						else
							h = Float.parseFloat(hours.getText().toString());
						if (minute_string.isEmpty())
							m = 0;
						else
							m = Float.parseFloat(minutes.getText().toString());
						if (hourlyWage_string.isEmpty())
							hourlyWage = 0;
						else
							hourlyWage = Float.parseFloat(rate.getText()
									.toString());

						p = (h * hourlyWage) + ((m / 60) * hourlyWage);
						pay.setText(String.format("%.2f", p));
					}
				});
				employeeClicked.show();
			}

		});

		all.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Displays the lawns cut in a new dialog
				Dialog display_all = new Dialog(SettleUp.this);
				display_all.setTitle("Today's Work");
				display_all.setContentView(R.layout.settle_up_layout);
				TextView breakdown = (TextView) display_all
						.findViewById(R.id.breakdown_textview);
				for (int i = 0; i < todaysLawns.size(); i++) {
					singleCutTime = todaysLawns.get(i).getTimeSpentCutting();
					breakdown.append(todaysLawns.get(i).getNameLawn() + ": "
							+ (int) ((singleCutTime / (1000 * 60 * 60)) % 24)
							+ " hours and "
							+ (int) ((singleCutTime / (1000 * 60)) % 60)
							+ " minutes.\n");

				}
				display_all.show();
			}
		});
	}

}
