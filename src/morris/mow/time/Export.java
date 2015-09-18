package morris.mow.time;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Export extends Activity {
	List<Lawn> allLawns;
	List<Employee> allEmployees;
	Activity a;
	DataManager manager;
	TextView export_Data, export_Data_Employee;
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = new DataManager(this);
		a = this;
		allLawns = manager.getLawnsToExport();
		allEmployees = manager.getEmployeesToExport();
		if ((allLawns.size() == 0) && allEmployees.size() == 0) {
			Toast.makeText(getApplicationContext(), "Nothing Ready To Export",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		setContentView(R.layout.export);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		export_Data = (TextView) findViewById(R.id.export_info_lawn);
		export_Data_Employee = (TextView) findViewById(R.id.export_info_employee);

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		if (allLawns.size() != 0) {

			for (int i = 0; i < allLawns.size(); i++) {
				export_Data.append(allLawns.get(i).getNameLawn() + "\n");
			}
		}
		if (allEmployees.size() != 0) {
			for (int i = 0; i < allEmployees.size(); i++) {
				export_Data_Employee.append(allEmployees.get(i).getName()
						+ "\n");
			}
		}

	}

	@SuppressWarnings("finally")
	private File saveLawnFile() {
		File toBeSaved = createFileName(true);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			workbook = Workbook.createWorkbook(toBeSaved, wbSettings);
			WritableSheet sheet = workbook.createSheet("Cut Lawns", 0);
			Label label1 = new Label(0, 0, "Name");
			Label label2 = new Label(1, 0, "Date");
			Label label3 = new Label(2, 0, "Accomplishments");

			try {
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				for (int x = 0; x < allLawns.size(); x++) {
					// Loop through filling out the table
					label1 = new Label(0, x + 1, allLawns.get(x).getNameLawn());
					label2 = new Label(1, x + 1, sdf.format(new Date(allLawns.get(x).getLastCut())));
					label3 = new Label(2, x + 1, allLawns.get(x)
							.getAccomplished());
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
				}

			} catch (RowsExceededException ex) {
				ex.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Maybe a toast
//			Toast.makeText(getApplicationContext(), "Data has been saved.",
//					Toast.LENGTH_SHORT).show();
			// Clear table
			//manager.clearTable();
			return toBeSaved;

		}
	}

	@SuppressWarnings("finally")
	private File saveEmployeeFile() {

		File toBeSaved = createFileName(false);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(toBeSaved, wbSettings);
			WritableSheet sheet = workbook.createSheet("Cut Lawns", 0);
			Label label1 = new Label(0, 0, "Name");
			Label label2 = new Label(1, 0, "Date Paid");
			Label label3 = new Label(2, 0, "Payment");

			try {
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				for (int x = 0; x < allEmployees.size(); x++) {
					// Loop through filling out the table
					label1 = new Label(0, x + 1, allEmployees.get(x).getName());
					label2 = new Label(1, x + 1, allEmployees.get(x)
							.getDatePaid());
					label3 = new Label(2, x + 1, allEmployees.get(x).getPaid());
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
				}

			} catch (RowsExceededException ex) {
				ex.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Maybe a toast
			// Clear table
			//manager.clearTable(); Removed because lets just persist data
			return toBeSaved;

		}

	}

	private File createFileName(boolean lawn) {
		Calendar today = Calendar.getInstance();

		// Set timezone to standard
		today.setTimeZone(TimeZone.getTimeZone("GMT"));
		// Clear the hour, hour of day, minute, second, and millisecond
		today.clear(Calendar.HOUR_OF_DAY);
		today.clear(Calendar.HOUR);
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);

		String fileName = sdf.format(new Date(today.getTimeInMillis()));

		File path = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"WORK");
		path.mkdirs();
		File toBeSaved = null;
		if (lawn)
			toBeSaved = new File(path.getAbsolutePath(), fileName
					+ "_lawns.xls");
		else
			toBeSaved = new File(path.getAbsolutePath(), fileName
					+ "_employees.xls");

		return toBeSaved;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.history_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.send_history:
		{			
			File employee_attachment = saveEmployeeFile();
			File lawn_attachment = saveLawnFile();
				String email_address = "";
				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager.get(a).getAccounts();
				for (Account account : accounts) {
				    if (emailPattern.matcher(account.name).matches()) {
				    		email_address= account.name;
				        
				    }
				}

			
			Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			intent.putExtra(Intent.EXTRA_SUBJECT, "Mow Time Data");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_address});
			intent.putExtra(Intent.EXTRA_TEXT, "Here are your created Excel files. Don't forget to give Mow Time a good review!");
			ArrayList<Uri> uris = new ArrayList<Uri>();
			uris.add(Uri.fromFile(employee_attachment));
			uris.add(Uri.fromFile(lawn_attachment));
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			intent.setType("text/plain");
			startActivity(Intent.createChooser(intent, "Send mail"));
			
			finish();
			return true;
		}
		case R.id.adding_cancel:
			finish();
			return true;

		}
		return true;

	}
}
