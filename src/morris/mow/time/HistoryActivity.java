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
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity {
	DataManager manager;
	ListView list_view;
	List<Lawn> client_history;
	String[] list;
	Activity a;
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		a = this;
		list_view = (ListView) findViewById(R.id.history_listView);
		Intent intent = getIntent();
		int row_id = intent.getIntExtra("original", -1);
		manager = new DataManager(this);

		client_history = manager.getClientHistory(row_id);
		
		// This is throwing errors if there is no cut.... Shouldn't get this far if there isn't a cut
		// It was because when the list is cleared in export it freaks out. it has been fixed
		setTitle(client_history.get(0).getNameLawn());
		// create a list adapter
		list = new String[client_history.size()];
		for (int i = 0; i < client_history.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			list[i] =sdf.format(new Date(client_history.get(i).getLastCut()));

		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, list);

		list_view.setAdapter(adapter);

		list_view.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				TextView  title = new TextView(HistoryActivity.this);
//				title.setText(list[arg2]);
//				title.setGravity(Gravity.CENTER);
				Dialog date_accomplishments = new Dialog(HistoryActivity.this);
				date_accomplishments.setContentView(R.layout.history_dialog);
				date_accomplishments.setTitle(list[arg2]);
				client_history.get(arg2).getLastCut();
				
				long timeWorked = client_history.get(arg2).getTimeSpentCutting();
				int min = (int) ((timeWorked / (1000 * 60)) % 60);
				int hours = (int) ((timeWorked / (1000 * 60 * 60)) % 24);
				
				TextView things = (TextView) date_accomplishments.findViewById(R.id.history_textView);
				things.setText(client_history.get(arg2).getAccomplished());
				things.append("\nHours: "+hours+" Minutes: "+min);
				date_accomplishments.show();

			}

		});
	manager.close();}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.history_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		if (item.getItemId()== R.id.send_history){
			File history_file =sendData();

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
		uris.add(Uri.fromFile(history_file));
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, "Send mail"));
		finish();
		}
		return true;

	}
	
	public File sendData(){
		File toBeSaved = createFileName();
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(toBeSaved, wbSettings);
			WritableSheet sheet = workbook.createSheet("Records", 0);
			Label label1 = new Label(0, 0, "Name");
			Label label2 = new Label(1, 0, "Date");
			Label label3 = new Label(2, 0, "Accomplishments");
			Label label4 = new Label(3,0, "Total Time in minutes");

			try {
				sheet.addCell(label1);
				sheet.addCell(label2);
				sheet.addCell(label3);
				sheet.addCell(label4);
				for (int x = 0; x < client_history.size(); x++) {
					// Loop through filling out the table
					label1 = new Label(0, x + 1, client_history.get(x).getNameLawn());
					label2 = new Label(1, x + 1, sdf.format(new Date(client_history.get(x).getLastCut())));
					label3 = new Label(2, x + 1, client_history.get(x).getAccomplished());
					label4 = new Label(3, x+1, ((client_history.get(x).getTimeSpentCutting()/60000)+""));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
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
			Toast.makeText(getApplicationContext(), "Data has been saved.",
					Toast.LENGTH_SHORT).show();
			// Clear table
			//manager.clearTable();
			return toBeSaved;
		}
	}
	
	private File createFileName() {

		//String fileName = sdf.format(new Date(today.getTimeInMillis()));
		String fileName = client_history.get(0).getNameLawn();

		File path = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"WORK");
		path.mkdirs();
		File toBeSaved = null;
			toBeSaved = new File(path.getAbsolutePath(), fileName
					+ "_history.xls");
		return toBeSaved;
	}
	
}
