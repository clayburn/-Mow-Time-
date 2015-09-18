package morris.mow.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FinishLawn extends Activity {
	// Create a data manager and lawn that has been started. Only one lawn
	// should have a running timer.
	Lawn started = new Lawn();
	int row;
	long time_From_Epoch;
	DataManager manager;

	//TextView start_TextView, elapsed_TextView, lawn_TextView;
	EditText  other_Blank;
	CheckBox cut_Grass,cut_Limbs,cut_Hedges,other,pine_Straw,clean_Up;
	Button finish, cancel, pause;
	Spinner tasks_Spinner;
	//DatePicker date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_lawn_2);
		manager = new DataManager(this);
		Intent intent = getIntent();
		started.setRowID(intent.getIntExtra("id", 0));
		started.setNameLawn(intent.getStringExtra("name"));
		started.setTempTime(intent.getLongExtra("temp", 0));
		
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.setTitle(started.getNameLawn() + " started at "
				+ sdf.format(new Date(started.getTempTime())));
		cut_Grass = (CheckBox) findViewById(R.id.Check_Grass);
		cut_Limbs = (CheckBox) findViewById(R.id.Check_Limbs);
		cut_Hedges = (CheckBox) findViewById(R.id.Check_Hedges);
		other = (CheckBox) findViewById(R.id.Check_Other);
		pine_Straw = (CheckBox) findViewById(R.id.Check_Pine);
		clean_Up = (CheckBox) findViewById(R.id.Check_Clean);
		other_Blank= (EditText) findViewById(R.id.blank_Other);

		
		// Convert to standard format
		
		finish = (Button) findViewById(R.id.finish_button);
		finish.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Send all data from Text Area to export table
				started.setAccomplished(getAccomplished());
				row = started.getRowID();
				long timeWorked = manager.stopLawn(row);
				Calendar today = Calendar.getInstance();

				// Set time zone to standard
				today.setTimeZone(TimeZone.getTimeZone("GMT"));
				// Clear the hour, hour of day, minute, second, and millisecond
				today.clear(Calendar.HOUR_OF_DAY);
				today.clear(Calendar.HOUR);
				today.clear(Calendar.MINUTE);
				today.clear(Calendar.SECOND);
				today.clear(Calendar.MILLISECOND);

				// Convert today into epoch time
				time_From_Epoch = today.getTimeInMillis();
				started.setLastCut(time_From_Epoch);
				manager.recordFinish(started);
				int min = (int) ((timeWorked / (1000 * 60)) % 60);
				int hours = (int) ((timeWorked / (1000 * 60 * 60)) % 24);
				Toast.makeText(getApplicationContext(),
						"CUT TIME: " + hours + " hours " + min + " minutes",
						Toast.LENGTH_SHORT).show();

// 1296000000
				manager.setNewDate(row, manager.today() + 1209600000);

				// Set the last day cut value
				manager.close();

				// Tell data manager to end cut
				finish();

			}

			private String getAccomplished() {
				// TODO Auto-generated method stub
				String accomplished="";
				if(cut_Grass.isChecked())
					accomplished+="Cut Grass\n";
				if(cut_Limbs.isChecked())
					accomplished+="Cut Limbs\n";
				if(cut_Hedges.isChecked())
					accomplished+="Cut Hedges\n";
				if(pine_Straw.isChecked())
					accomplished+="Pine Straw\n";
				if(clean_Up.isChecked())
					accomplished+="Clean Up\n";
				if(other.isChecked())
					accomplished+="Other\n";
				if(!other_Blank.getText().toString().matches(""))
					accomplished+=other_Blank.getText().toString();
					return accomplished;
			}
		});

		cancel = (Button) findViewById(R.id.cancel_finish_button);
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


}
