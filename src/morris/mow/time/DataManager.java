package morris.mow.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * SQLite helper used to perform all needed database functions
 *  4 Main tables: lawns/clients,employees, completed lawns/clients, payment to employees
 */

public class DataManager extends SQLiteOpenHelper {
	// Lawn Identifiers
	public static final String KEY_ROWID = "_row";
	public static final String KEY_NAME = "lawn_name";
	public static final String KEY_TOBECUT = "to_be_cut";
	public static final String KEY_LASTCUT = "last_cut";
	public static final String KEY_AVERAGECUTTIME = "average_cut";
	public static final String KEY_ACCOMPLISHED = "accomplished";
	public static final String KEY_TEMPTIME = "temp_time";
	public static final String KEY_PAUSETIME = "pause_time";
	public static final String KEY_TOTALPAUSETIME = "total_pause_time";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_ORIGINAL_ROW = "original_row";
	public static final String KEY_TOTAL_TIME = "total_cut_time";

	// Employee Identifiers
	public static final String KEY_PAYRATE = "pay_rate";
	public static final String KEY_EMPLOYEE_NAME = "name";
	public static final String KEY_EMPLOYEE_ROWID = "row_id";
	public static final String KEY_EMPLOYEE_START = "start";
	public static final String KEY_EMPLOYEE_STOP = "stop";
	public static final String KEY_EMPLOYEE_TOTAL = "total";
	public static final String KEY_EMPLOYEE_PHONE = "emp_phone";
	public static final String KEY_EMPLOYEE_DATE = "paid_date";
	public static final String KEY_EMPLOYEE_ORIGINAL_ROWID = "employee_original_row_id";

	private static final String DATABASE_NAME = "dataDB";
	private static final String DATABASE_TABLE_LAWNS = "lawnTable";
	private static final String DATABASE_TABLE_EMPLOYEE = "employeeTable";
	private static final String DATABASE_TABLE_COMPLETED = "completedTable";
	private static final String DATABASE_TABLE_EMPLOYEE_COMPLETED = "completedEmployeeTable";
	private static final int DATABASE_VERSION = 2;

	public DataManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// Create all tables
	// Update since initial release, added columns to completed tables June 28
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the lawn table
		db.execSQL("CREATE TABLE " + DATABASE_TABLE_LAWNS + " (" + KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ KEY_NAME+ " TEXT NOT NULL, " 
				+ KEY_TOBECUT + " INTEGER NOT NULL, "
				+ KEY_LASTCUT + " INTEGER, "
				+ KEY_TEMPTIME + " INTEGER, "
				+ KEY_PHONE + " TEXT, "
				+ KEY_PAUSETIME + " INTEGER, " 
				+ KEY_TOTALPAUSETIME + " INTEGER, "+ KEY_AVERAGECUTTIME
				+ " INTEGER);");

		// Create the employee table
		db.execSQL("CREATE TABLE " + DATABASE_TABLE_EMPLOYEE + " ("
				+ KEY_EMPLOYEE_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_EMPLOYEE_NAME + " TEXT NOT NULL, "
				+ KEY_EMPLOYEE_START + " INTEGER, "
				+ KEY_EMPLOYEE_PHONE + " TEXT, "
				+ KEY_EMPLOYEE_STOP + " INTEGER, "
				+ KEY_EMPLOYEE_TOTAL + " INTEGER, "
				+ KEY_PAYRATE + " REAL NOT NULL);");

		// Create the completed table
		db.execSQL("CREATE TABLE " + DATABASE_TABLE_COMPLETED + " ("
				+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_LASTCUT + " INTEGER NOT NULL, " 
				+ KEY_ORIGINAL_ROW +" INTEGER, " 
				+ KEY_TOTAL_TIME+" INTEGER, "
				+ KEY_ACCOMPLISHED+ " TEXT, " 
				+ KEY_NAME + " TEXT);");

		// Create the completed table for paid employees
		db.execSQL("CREATE TABLE " + DATABASE_TABLE_EMPLOYEE_COMPLETED + " ("
				+ KEY_EMPLOYEE_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_EMPLOYEE_NAME + " TEXT, " 
				+ KEY_EMPLOYEE_DATE + " TEXT, "
				+ KEY_EMPLOYEE_ORIGINAL_ROWID + " INTEGER, "
				+ KEY_EMPLOYEE_TOTAL + " TEXT);");
	
	}

	// Upgrade completed tables of initial tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
		switch(newVersion){
		case 2:
			db.execSQL("ALTER TABLE "+DATABASE_TABLE_COMPLETED+ " ADD COLUMN "+ KEY_ORIGINAL_ROW+" INTEGER DEFAULT 0");
			db.execSQL("ALTER TABLE "+DATABASE_TABLE_COMPLETED+ " ADD COLUMN "+ KEY_TOTAL_TIME+" INTEGER DEFAULT 0");
			db.execSQL("ALTER TABLE "+DATABASE_TABLE_EMPLOYEE_COMPLETED+ " ADD COLUMN "+ KEY_EMPLOYEE_ORIGINAL_ROWID+" INTEGER DEFAULT 0");
			break;
		}
	}
	// add a new client/lawn
	public void newEntry(Lawn newLawn) {
		// Create content to be added
		ContentValues content = new ContentValues();
		content.put(KEY_NAME, newLawn.getNameLawn());
		content.put(KEY_TOBECUT, newLawn.getToBeCut());
		content.put(KEY_LASTCUT, newLawn.getLastCut());
		content.put(KEY_PHONE, newLawn.getPhoneNumber());
		
		// Get an executable version of the table
		SQLiteDatabase ourDatabase = this.getWritableDatabase();

		// Add content values to the table as a new entry
		ourDatabase.insert(DATABASE_TABLE_LAWNS, null, content);
		ourDatabase.close();
	}
	// add a new employee
	public void newEntry(Employee newEmployee) {
		// Create content to be added
		ContentValues content = new ContentValues();
		content.put(KEY_EMPLOYEE_NAME, newEmployee.getName());
		content.put(KEY_PAYRATE, newEmployee.getRate());

		// Get an executable version of the table
		SQLiteDatabase ourDatabase = this.getWritableDatabase();

		// Add content values to the table as a new entry
		ourDatabase.insert(DATABASE_TABLE_EMPLOYEE, null, content);
		ourDatabase.close();
	}

	public long setStartTime(int row) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();

		Calendar today = getCurrentTime();

		// Convert today into epoch time
		long timeFromEpoch = today.getTimeInMillis();
		content.put(KEY_TEMPTIME, timeFromEpoch);
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
		return timeFromEpoch;
	}

	public void setNewDate(int row, long newDate) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();
		// Convert today into epoch time
		content.put(KEY_TOBECUT, newDate);
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
	}

	public void setNewDate(int row) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();
		// Convert today into epoch time
		content.put(KEY_TOBECUT, today());
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
	}

	public void updateEmployee(Employee new_Employee) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();
		// Convert today into epoch time
		content.put(KEY_EMPLOYEE_NAME, new_Employee.getName());
		content.put(KEY_PAYRATE, new_Employee.getRate());
		db.update(DATABASE_TABLE_EMPLOYEE, content,
				KEY_EMPLOYEE_ROWID + " = ?",
				new String[] { String.valueOf(new_Employee.getRowID()) });
		db.close();
	}

	public void updateClient(Lawn new_Lawn) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();
		// Convert today into epoch time
		content.put(KEY_NAME, new_Lawn.getNameLawn());
		content.put(KEY_PHONE, new_Lawn.getPhoneNumber());
		content.put(KEY_TOBECUT, new_Lawn.getToBeCut());
		db.update(DATABASE_TABLE_LAWNS, content,
				KEY_ROWID + " = ?",
				new String[] { String.valueOf(new_Lawn.getRowID()) });
		db.close();
	}

	public List<Lawn> getTodaysLawns(long timeFromEpoch) {
		// TODO Get all lawns within todays time frame
		List<Lawn> matchingLawns = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_TEMPTIME, KEY_PAUSETIME };
		// Loop through table and grab each lawn and check for a time that
		// matches the correct time zone
		SQLiteDatabase db = this.getWritableDatabase();
		String where = KEY_TOBECUT + "=" + timeFromEpoch;
		Cursor cursor = db.query(DATABASE_TABLE_LAWNS, result_columns, where,
				null, null, null, null);

		// All the ones that match add to list
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			temp.setTempTime(cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME)));
			temp.setPauseTime(cursor.getLong(cursor
					.getColumnIndex(KEY_PAUSETIME)));
			matchingLawns.add(temp);
		}
		db.close();
		return matchingLawns;
	}

	public List<Lawn> getTodaysLawns() {
		// TODO Get all lawns within todays time frame
		long timeFromEpoch = today();
		List<Lawn> matchingLawns = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_TEMPTIME, KEY_PAUSETIME, KEY_PHONE,KEY_LASTCUT };
		// Loop through table and grab each lawn and check for a time that
		// matches the correct time zone
		SQLiteDatabase db = this.getWritableDatabase();
		String where = KEY_TOBECUT + "=" + timeFromEpoch;
		Cursor cursor = db.query(DATABASE_TABLE_LAWNS, result_columns, where,
				null, null, null, KEY_TEMPTIME+" DESC");

		// All the ones that match add to list
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			temp.setTempTime(cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME)));
			temp.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
			temp.setPauseTime(cursor.getLong(cursor.getColumnIndex(KEY_PAUSETIME)));
			temp.setLastCut(cursor.getLong(cursor.getColumnIndex(KEY_LASTCUT)));
			matchingLawns.add(temp);
		}
		db.close();
		return matchingLawns;
	}

	public List<Lawn> getAllLawns() {
		List<Lawn> allLawns = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_TOBECUT, KEY_TEMPTIME, KEY_PAUSETIME,KEY_PHONE, KEY_LASTCUT };

		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		Cursor cursor = ourDatabase.query(DATABASE_TABLE_LAWNS, result_columns,
				null, null, null, null, KEY_TOBECUT + " ASC");
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			temp.setToBeCut(cursor.getLong(cursor.getColumnIndex(KEY_TOBECUT)));
			temp.setTempTime(cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME)));
			temp.setPauseTime(cursor.getLong(cursor.getColumnIndex(KEY_PAUSETIME)));
			temp.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
			temp.setLastCut(cursor.getLong(cursor.getColumnIndex(KEY_LASTCUT)));
			allLawns.add(temp);
		}
		ourDatabase.close();
		return allLawns;

	}

	public List<Employee> getAllEmployees() {
		List<Employee> allEmployees = new ArrayList<Employee>();
		String[] result_columns = new String[] { KEY_EMPLOYEE_ROWID,
				KEY_EMPLOYEE_NAME, KEY_PAYRATE };

		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		Cursor cursor = ourDatabase.query(DATABASE_TABLE_EMPLOYEE,
				result_columns, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Employee temp = new Employee();
			temp.setName((cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME))));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_EMPLOYEE_ROWID)));
			temp.setRate((cursor.getFloat(cursor.getColumnIndex(KEY_PAYRATE))));
			allEmployees.add(temp);
		}
		ourDatabase.close();
		return allEmployees;

	}

	public Lawn getStartedLawns() {

		SQLiteDatabase db = this.getWritableDatabase();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_TEMPTIME,KEY_PAUSETIME};
		long today = today();
		// Lawn with temptime greater than today are currently running
		String where = KEY_TEMPTIME + ">" + today;
		Cursor cursor = db.query(DATABASE_TABLE_LAWNS, result_columns, where,
				null, null, null, null);
		if (cursor.getCount() == 0) {
			return null;
		}
		Lawn temp = new Lawn();
		cursor.moveToNext();
		temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
		temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
		temp.setTempTime(cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME)));
		temp.setPaused((cursor.getLong(cursor.getColumnIndex(KEY_PAUSETIME))>0));
		Calendar now = getCurrentTime();
		long temp_Elapsed = now.getTimeInMillis() - (cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME)));
		temp.setTimeSpentCutting(temp_Elapsed);
		db.close();
		return temp;

	}

	public long stopLawn(int row) {
		// Create the database instance and the cursor instance
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();

		Calendar today = getCurrentTime();

		// Get the temp time for this row.

		Cursor cursor = db.query(DATABASE_TABLE_LAWNS, new String[] {
				KEY_TEMPTIME, KEY_TOTALPAUSETIME }, KEY_ROWID + "=" + row, null,
				null, null, null);
		cursor.moveToNext();
		long startedTime = cursor.getLong(cursor.getColumnIndex(KEY_TEMPTIME));
		long pauseTime = cursor.getLong(cursor.getColumnIndex(KEY_TOTALPAUSETIME));
		// Subtract that value from the value of timeFromEpoch to get the
		// elapsed time
		long timeFromEpoch = today.getTimeInMillis();
		timeFromEpoch = timeFromEpoch - startedTime - pauseTime;
		// Save that as the new tempTime

		content.put(KEY_TEMPTIME, timeFromEpoch);
		content.put(KEY_LASTCUT, today());
		content.put(KEY_TOTALPAUSETIME, 0);
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
		return timeFromEpoch;
	}

	private Calendar getCurrentTime() {
		// Create a calendar instance
		Calendar today = Calendar.getInstance();

		// Set timezone to standard
		today.setTimeZone(TimeZone.getTimeZone("GMT"));

		// Clear second, and millisecond
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);
		return today;
	}

	public void averageTime(int row, long epoch) {
		// Compute the average time it takes the client/lawn to be completed 
	}

	public void recordFinish(Lawn lawn) {
		ContentValues content = new ContentValues();
		content.put(KEY_NAME, lawn.getNameLawn());
		content.put(KEY_LASTCUT, lawn.getLastCut());
		content.put(KEY_ACCOMPLISHED, lawn.getAccomplished());
		content.put(KEY_ORIGINAL_ROW, lawn.getRowID());
		content.put(KEY_TOTAL_TIME, lawn.getTimeSpentCutting());
		SQLiteDatabase ourDatabase = this.getWritableDatabase();

		// Add content values to the table as a new entry
		ourDatabase.insert(DATABASE_TABLE_COMPLETED, null, content);
		ourDatabase.close();

	}

	public long today() {
		Calendar today = Calendar.getInstance();

		// Set timezone to standard
		today.setTimeZone(TimeZone.getTimeZone("GMT"));

		// Clear the hour, hour of day, minute, second, and millisecond
		today.clear(Calendar.HOUR_OF_DAY);
		today.clear(Calendar.HOUR);
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);

		// Convert today into epoch time
		return today.getTimeInMillis();
	}
	public long now() {
		Calendar today = Calendar.getInstance();

		// Set timezone to standard
		//today.setTimeZone(TimeZone.getTimeZone("GMT"));
		// Convert today into epoch time
		return today.getTimeInMillis();
	}

	public List<Lawn> getTodaysCut() {
		List<Lawn> todaysCut = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_TEMPTIME };
		SQLiteDatabase db = this.getReadableDatabase();

		String where = KEY_LASTCUT + "=" + today();

		Cursor cursor = db.query(DATABASE_TABLE_LAWNS, result_columns, where,
				null, null, null, null);
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			temp.setTimeSpentCutting(cursor.getLong(cursor
					.getColumnIndex(KEY_TEMPTIME)));
			todaysCut.add(temp);
		}
		db.close();
		return todaysCut;

	}

	public long pause(int row) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();

		Calendar today = getCurrentTime();

		// Convert today into epoch time
		long timeFromEpoch = today.getTimeInMillis();
		content.put(KEY_PAUSETIME, timeFromEpoch);
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
		return timeFromEpoch;
	}

	public long unpause(int row) {
		// Create the database instance and the cursor instance
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();

		Calendar today = getCurrentTime();

		// Get the temp time for this row.

		Cursor cursor = db.query(DATABASE_TABLE_LAWNS,
				new String[] { KEY_PAUSETIME,KEY_TOTALPAUSETIME }, KEY_ROWID + "=" + row, null,
				null, null, null);
		cursor.moveToNext();
		long startedPauseTime = cursor.getLong(cursor
				.getColumnIndex(KEY_PAUSETIME));
		long totalPauseTime = cursor.getLong(cursor
				.getColumnIndex(KEY_TOTALPAUSETIME));
		// Subtract that value from the value of timeFromEpoch to get the
		// elapsed time
		long timeFromEpoch = today.getTimeInMillis();
		timeFromEpoch = timeFromEpoch - startedPauseTime;
		totalPauseTime+=timeFromEpoch;
		content.put(KEY_TOTALPAUSETIME, totalPauseTime);
		content.put(KEY_PAUSETIME, 0);
		db.update(DATABASE_TABLE_LAWNS, content, KEY_ROWID + " = ?",
				new String[] { String.valueOf(row) });
		db.close();
		return timeFromEpoch;
	}

	public List<Lawn> getLawnsToExport() {
		List<Lawn> finishLawns = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ROWID, KEY_NAME,
				KEY_LASTCUT, KEY_ACCOMPLISHED, KEY_TOTAL_TIME };

		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		Cursor cursor = ourDatabase.query(DATABASE_TABLE_COMPLETED,
				result_columns, null, null, null, null, KEY_LASTCUT + " DESC");
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			temp.setAccomplished(cursor.getString(cursor.getColumnIndex(KEY_ACCOMPLISHED)));
			temp.setLastCut(cursor.getLong(cursor.getColumnIndex(KEY_LASTCUT)));

			finishLawns.add(temp);
		}
		ourDatabase.close();
		return finishLawns;

	}

	public void clearTable() {
		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		ourDatabase.execSQL("delete from " + DATABASE_TABLE_COMPLETED);
		ourDatabase.execSQL("delete from " + DATABASE_TABLE_EMPLOYEE_COMPLETED);
		ourDatabase.close();
	}

	
	public List<Employee> getEmployeesToExport() {
		List<Employee> paidEmployees = new ArrayList<Employee>();
		String[] result_columns = new String[] { KEY_EMPLOYEE_ROWID,
				KEY_EMPLOYEE_NAME, KEY_EMPLOYEE_TOTAL, KEY_EMPLOYEE_DATE };
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DATABASE_TABLE_EMPLOYEE_COMPLETED,
				result_columns, null, null, null, null, KEY_EMPLOYEE_DATE
				+ " DESC");
		while (cursor.moveToNext()) {
			Employee temp = new Employee();
			temp.setName(cursor.getString(cursor
					.getColumnIndex(KEY_EMPLOYEE_NAME)));
			temp.setPaid(cursor.getString(cursor
					.getColumnIndex(KEY_EMPLOYEE_TOTAL)));
			temp.setDatePaid(cursor.getString(cursor
					.getColumnIndex(KEY_EMPLOYEE_DATE)));

			paidEmployees.add(temp);
		}
		db.close();
		return paidEmployees;
	}

	public void recordPayment(Employee employee) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		ContentValues content = new ContentValues();
		content.put(KEY_EMPLOYEE_NAME, employee.getName());
		content.put(KEY_EMPLOYEE_TOTAL, employee.getPaid());
		content.put(KEY_EMPLOYEE_DATE, sdf.format(now()));
		content.put(KEY_EMPLOYEE_ORIGINAL_ROWID, employee.getRowID());

		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(DATABASE_TABLE_EMPLOYEE_COMPLETED, null, content);
		db.close();
	}

	public boolean removeEmployee(Employee employee) {
		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		return ourDatabase.delete(DATABASE_TABLE_EMPLOYEE, KEY_EMPLOYEE_ROWID
				+ "=" + employee.getRowID(), null) > 0;

	}

	public boolean removeLawn(Lawn lawn){
		SQLiteDatabase ourDatabase = this.getReadableDatabase();
		return ourDatabase.delete(DATABASE_TABLE_LAWNS, KEY_ROWID
				+ "=" + lawn.getRowID(), null) > 0;

	}

	public List<Lawn> getClientHistory(int row_id){
		List<Lawn> client_history = new ArrayList<Lawn>();
		String[] result_columns = new String[] { KEY_ORIGINAL_ROW, KEY_NAME,
				KEY_LASTCUT, KEY_ACCOMPLISHED,KEY_TOTAL_TIME };

		SQLiteDatabase db = this.getWritableDatabase();
		String where = KEY_ORIGINAL_ROW + "=" + row_id;
		Cursor cursor = db.query(DATABASE_TABLE_COMPLETED, result_columns, where,
				null, null, null, KEY_LASTCUT + " DESC");
		while (cursor.moveToNext()) {
			Lawn temp = new Lawn();
			temp.setNameLawn(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
			temp.setRowID(cursor.getInt(cursor.getColumnIndex(KEY_ORIGINAL_ROW)));
			temp.setLastCut(cursor.getLong(cursor.getColumnIndex(KEY_LASTCUT)));
			temp.setAccomplished(cursor.getString(cursor.getColumnIndex(KEY_ACCOMPLISHED)));
			temp.setTimeSpentCutting(cursor.getLong(cursor.getColumnIndex(KEY_TOTAL_TIME)));
			client_history.add(temp);
		}
		db.close();
		return client_history;


	}

	
	
	public List<Employee> getEmployeeHistory(int row_id){
	List<Employee> list = new ArrayList<Employee>();
	String[] result_columns = new String[]{KEY_EMPLOYEE_ORIGINAL_ROWID, KEY_EMPLOYEE_TOTAL,KEY_EMPLOYEE_DATE,KEY_EMPLOYEE_NAME};
	SQLiteDatabase db = this.getWritableDatabase();
	String where = KEY_EMPLOYEE_ORIGINAL_ROWID +"="+ row_id;
	
	Cursor cursor = db.query(DATABASE_TABLE_EMPLOYEE_COMPLETED, result_columns, where, null, null, null, KEY_EMPLOYEE_DATE+" ASC");
	while(cursor.moveToNext()){
		Employee employee = new Employee();
		employee.setName(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_NAME)));
		employee.setDatePaid(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_DATE)));
		employee.setPaid(cursor.getString(cursor.getColumnIndex(KEY_EMPLOYEE_TOTAL)));
		list.add(employee);
	}
	db.close();
	return list;
}
}
