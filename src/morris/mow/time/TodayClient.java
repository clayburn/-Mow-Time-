package morris.mow.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class TodayClient extends Fragment implements OnPageSelectedListener{

	View fullView;
	Dialog stop_Screen;
	ExpandableListView listView;
	List<Lawn> allClients;
	Lawn started;
	DataManager manager;
	ArrayList<Group> groups = new ArrayList<Group>();
	CheckBox cut_Grass,cut_Limbs,cut_Hedges,other,pine_Straw,clean_Up;
	EditText other_Blank;
	Activity a;
	Button finish, cancel, pause;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		a = getActivity();
		manager = new DataManager(a);
	
		fullView = inflater.inflate(R.layout.main2, container, false);
		initializeData();
		
		createView();
		
		manager.close();
		return fullView;
		
	}

	private void createView() {
		//adapter = new InfoDetailsAdapter(getActivity(), client_list, tasks);
		listView = (ExpandableListView) fullView.findViewById(R.id.expandableList);
		
		MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(),
		        groups);
		
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new OnChildClickListener(){

			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				// check if lawn timer is running or not
				Lawn started = manager.getStartedLawns();
				
				if(started == null){
				// check to see which child was clicked
				switch (childPosition){
				case 0: startTimer(groupPosition);
					break;
				case 1: editClient(groupPosition);
					break;
				case 2: callClient(groupPosition);
					break;
				case 3:
					viewHistory(groupPosition);
					break;
				case 4: removeClient(groupPosition);
					break;
				}
				}
				else{
					// A lawn is being cut and it isn't paused
					if(allClients.get(groupPosition).getRowID()==started.getRowID() && !started.isPaused()){
						// different set of options
						switch (childPosition){
						case 0: stopTimer(groupPosition);
							break;
						case 1: pauseClient(groupPosition);
							break;
						case 2: callClient(groupPosition);
							break;
						case 3:
							viewHistory(groupPosition);
							break;
						}
					}
					else if(allClients.get(groupPosition).getRowID()==started.getRowID() && started.isPaused()){
						switch (childPosition){
						case 0: unpauseClient(groupPosition);
							break;
						case 1: callClient(groupPosition);
							break;
						case 2:
							viewHistory(groupPosition);
						}
					}
					else{
						switch (childPosition){
						case 0: editClient(groupPosition);
							break;
						case 1: callClient(groupPosition);
							break;
						case 2:viewHistory(groupPosition);
						break;
						case 3: removeClient(groupPosition);
							break;
						}
					}
				}
				
				return false;
			}

						
		});
	}

	public void initializeData(){
		// Get all clients from the data manager
		allClients = null;
		started = manager.getStartedLawns();
		allClients= manager.getTodaysLawns();
		// Check if any lawns are being cut
		
		groups.clear();
		if(started == null){
		// If no lawns are being cut, create the full options
		// call add info for each client
			for(int i =0; i<allClients.size(); i++){
				String cut_date = "";
				Group group = new Group(allClients.get(i).getNameLawn(), cut_date);
					group.children.add("Start Timer");
					group.children.add("Edit");
					group.children.add("Call");
					group.children.add("History");
					group.children.add("Remove");
				groups.add(i, group);
			}
		}else{
			// if a lawn is being cut, create the secondary
			for(int i =0; i<allClients.size(); i++){
				// Lawn is being cut and is paused
				if(allClients.get(i).getRowID()==started.getRowID() && started.isPaused()){
					String cut_date = "";
				Group group = new Group(allClients.get(i).getNameLawn(), cut_date);
					group.children.add("Unpause");
					group.children.add("Call");
					group.children.add("History");
				groups.add(i, group);
				}
				// Lawn is being cut and isn't paused
				else if(allClients.get(i).getRowID()==started.getRowID() && !started.isPaused()){
					String cut_date = "";
					Group group = new Group(allClients.get(i).getNameLawn(), cut_date);
					group.children.add("Stop Timer");
					group.children.add("Pause Timer");
					group.children.add("Call");
					group.children.add("History");
				groups.add(i, group);
				}
				// a lawn is being cut and it ain't this un here
				else{
					String cut_date = "";
					Group group = new Group(allClients.get(i).getNameLawn(), cut_date);
					group.children.add("Edit");
					group.children.add("Call");
					group.children.add("History");
					group.children.add("Remove");
					groups.add(i, group);
				}
			}
		}
	}
	
	private void callClient(int index) {
		// calls client
		String phone_number = allClients.get(index).getPhoneNumber();
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone_number));
		startActivity(intent);
	}

	private void removeClient(int index) {
		// removes client
		boolean worked =manager.removeLawn(allClients.get(index));
		initializeData();
		createView();
	}

	private void editClient(int index) {
		// Launches dialog to edit Client

		// Launches dialog to edit Client
		final Lawn temp = allClients.get(index);
		final Dialog add_dialog = new Dialog(getActivity());
		add_dialog.setTitle("Edit Client");
		add_dialog.setContentView(R.layout.addlawnlayout);
		Button add = (Button) add_dialog.findViewById(R.id.add_button);
		final EditText name = (EditText) add_dialog
				.findViewById(R.id.name_field);
		name.setText(temp.getNameLawn());
		final EditText phone = (EditText) add_dialog.findViewById(R.id.phone_edittext);
		phone.setText(temp.getPhoneNumber());
		final DatePicker date = (DatePicker) add_dialog
				.findViewById(R.id.date_picker);
		//date.set
		add.setText("Save");
		add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tempName = "";
				String tempPhone = "";
				// Will grab all data in text fields and use them to create a
				// new Lawn instance.
				try {
					tempName = name.getText().toString();
					tempPhone = phone.getText().toString().trim();
					if ((tempName == "")) {

						Toast.makeText(getActivity(),
								"oops",
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

						//Lawn newLawn = new Lawn(tempName, tempPhone);
						temp.setNameLawn(tempName);
						temp.setPhoneNumber(tempPhone);
						temp.setToBeCut(epochToCutDate);
						manager.updateClient(temp);
						//manager.newEntry(newLawn);
						add_dialog.dismiss();
						initializeData();
						createView();
					}

				} catch (Exception e) {
					Toast.makeText(getActivity(),"oops",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		add_dialog.show();
	
	
	}

	private void startTimer(int index) {
		// Starts the timer by taking a creating an epoch time stamp 
		long startTime = manager.setStartTime(allClients.get(index).getRowID());
		manager.setNewDate(allClients.get(index).getRowID());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Toast.makeText(getActivity(),
				"Timer started at " + sdf.format(new Date(startTime)),
				Toast.LENGTH_LONG).show();
		// Change view so start is no longer an option
		initializeData();
		createView();
	}
	private void stopTimer(int groupPosition) {

		//manager.stopLawn(allClients.get(groupPosition).getRowID());
		// Launch stop dialog
		started = allClients.get(groupPosition);
		stop_Screen = new Dialog(getActivity());
		stop_Screen.setContentView(R.layout.finish_lawn_2);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		stop_Screen.setTitle(started.getNameLawn() + " started at "
				+ sdf.format(new Date(started.getTempTime())));
		cut_Grass = (CheckBox) stop_Screen.findViewById(R.id.Check_Grass);
		cut_Limbs = (CheckBox) stop_Screen.findViewById(R.id.Check_Limbs);
		cut_Hedges = (CheckBox) stop_Screen.findViewById(R.id.Check_Hedges);
		other = (CheckBox) stop_Screen.findViewById(R.id.Check_Other);
		pine_Straw = (CheckBox) stop_Screen.findViewById(R.id.Check_Pine);
		clean_Up = (CheckBox) stop_Screen.findViewById(R.id.Check_Clean);
		other_Blank= (EditText) stop_Screen.findViewById(R.id.blank_Other);
		
		finish = (Button) stop_Screen.findViewById(R.id.finish_button);
		finish.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Send all data from Text Area to export table
				started.setAccomplished(getAccomplished());
				int row = started.getRowID();
				long timeWorked = manager.stopLawn(row);
				Calendar today = Calendar.getInstance();

				// Set time zone to standard
				today.setTimeZone(TimeZone.getTimeZone("GMT"));
				// Clear the hour, hour of day, minute, second, and millisecond
				
				long time_from_epoch = 0;
				// Convert today into epoch time
				time_from_epoch = today.getTimeInMillis();
				started.setLastCut(time_from_epoch);
				started.setTimeSpentCutting(timeWorked);
				manager.recordFinish(started);
				today.clear(Calendar.HOUR_OF_DAY);
				today.clear(Calendar.HOUR);
				today.clear(Calendar.MINUTE);
				today.clear(Calendar.SECOND);
				today.clear(Calendar.MILLISECOND);
				int min = (int) ((timeWorked / (1000 * 60)) % 60);
				int hours = (int) ((timeWorked / (1000 * 60 * 60)) % 24);
				Toast.makeText(getActivity(),
						"CUT TIME: " + hours + " hours " + min + " minutes",
						Toast.LENGTH_SHORT).show();

// 1296000000
				manager.setNewDate(row, manager.today() + 1209600000);

				// Set the last day cut value
				manager.close();

				// Tell data manager to end cut
				stop_Screen.dismiss();
				initializeData();
				createView();

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
		cancel = (Button) stop_Screen.findViewById(R.id.cancel_finish_button);
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop_Screen.dismiss();
			}
		});
//		pause = (Button) stop_Screen.findViewById(R.id.pause_button);
//
//		pause.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View arg0) {
//				
//			}
//		});
		stop_Screen.show();
		
	}
	
	private void pauseClient(int groupPosition) {
		// When pause is clicked
		long pause_time = manager.pause(allClients.get(groupPosition).getRowID());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Toast.makeText(getActivity(),
				"TIME PAUSED AT " + sdf.format(new Date(pause_time)),
				Toast.LENGTH_LONG).show();
		initializeData();
		createView();
		
	}

	protected void unpauseClient(int groupPosition) {
		// When unpause is clicked
		long pauseLength = manager.unpause(allClients.get(
				groupPosition).getRowID());
		int min = (int) ((pauseLength / (1000 * 60)) % 60);
		int hours = (int) ((pauseLength / (1000 * 60 * 60)) % 24);
		Toast.makeText(
				getActivity(),
				" Pause Time: " + hours + " hours " + min
						+ " minutes", Toast.LENGTH_SHORT)
				.show();
		initializeData();
		createView();
	}
	
@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}


	
	
	public void onPageSelected() {
		// TODO Auto-generated method stub
		//fullView.refreshDrawableState();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initializeData();
		createView();
	}

	private void viewHistory(int groupPosition) {
		// Creates new activity for displaying the history
		if(allClients.get(groupPosition).getLastCut()<=0){
			Toast.makeText(getActivity(), "Has not been cut yet", Toast.LENGTH_LONG).show();
		}
		else{
		int original_row = allClients.get(groupPosition).getRowID();
		Intent intent = new Intent(getActivity(), HistoryActivity.class);
		intent.putExtra("original", original_row);
		startActivity(intent);
		}
		
	}


}
