package morris.mow.time;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class AllEmployees extends Fragment implements OnPageSelectedListener{
	DataManager manager;
	Dialog editing;
	View full_view;
	Button submit_info;
	ExpandableListView listView;
	Button cancel;
	EditText employee_name;
	EditText employee_rate;
	ArrayList<Group> groups = new ArrayList<Group>();
	List<Employee> all_employees;
	View employee_View;
	ArrayAdapter<String> adapter;
	String[] employee_names;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		manager = new DataManager(getActivity());
		full_view = inflater.inflate(R.layout.main2, container, false);
		initializeData();
		createView();
		return full_view;
	}

	private void initializeData() {
		// Generates the data to be displayed
		all_employees = null;
		all_employees = manager.getAllEmployees();
		groups.clear();
		for (int i = 0; i < all_employees.size(); i++) {
			Group group = new Group(all_employees.get(i).getName(), "");
			group.children.add("Edit");
			group.children.add("Records");
			group.children.add("Remove");
			groups.add(i,group);
		}
	}
	
	private void createView() {
		// Creates the view with data from initializeData()
		listView = (ExpandableListView) full_view.findViewById(R.id.expandableList);
		MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(),
		        groups);
		
//		adapter = new ArrayAdapter<String>(getActivity(),
//				android.R.layout.simple_list_item_1, android.R.id.text1,
//				employee_names);
		listView.setAdapter(adapter);
		
		listView.setOnChildClickListener(new OnChildClickListener(){

			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				switch(childPosition){
					case 0: editEmployee(groupPosition);
						break;
					case 1: getRecords(groupPosition);
						break;
					case 2: removeEmployee(groupPosition);
						break;
				}
				
						return false;
			}

			
		
			});
		
	}
	
	protected void removeEmployee(int groupPosition) {
		// TODO Auto-generated method stub
		manager.removeEmployee(all_employees.get(groupPosition));
		initializeData();
		createView();
	}

	public void onPageSelected() {
		//full_view.refreshDrawableState();
	}
	
@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}
private void editEmployee(int groupPosition) {
	final int row = all_employees.get(groupPosition).getRowID();
	editing = new Dialog(getActivity());
	editing.setTitle("Editing Employee");
	editing.setContentView(R.layout.editing_employee);

	employee_name = (EditText) editing
			.findViewById(R.id.employee_edit_name);
	employee_rate = (EditText) editing
			.findViewById(R.id.employee_edit_rate);

	employee_name.setText(all_employees.get(groupPosition).getName());
	employee_rate.setText(Float.toString((all_employees.get(groupPosition)
			.getRate())));

	submit_info = (Button) editing.findViewById(R.id.employee_edit_submit);
	cancel = (Button) editing.findViewById(R.id.employee_edit_cancel);

	submit_info.setOnClickListener(new View.OnClickListener() {

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Employee temp = null;
			String temp_rate = employee_rate.getText().toString();
			if(temp_rate.isEmpty() || temp_rate.contentEquals(".")){
			temp = new Employee(employee_name.getText().toString(),0,
					row);
			}
			else
			{
				temp = new Employee(employee_name.getText().toString(),
						Float.parseFloat(employee_rate.getText().toString()),row);
			}
			manager.updateEmployee(temp);
			editing.dismiss();
			initializeData();
			createView();
		}
	});

	cancel.setOnClickListener(new View.OnClickListener() {

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			editing.dismiss();
		}
	});

	editing.show();
}


private void getRecords(int groupPosition){
int row =all_employees.get(groupPosition).getRowID();
Intent i = new Intent(getActivity(), RecordsActivity.class);
i.putExtra("original_row", row);
startActivity(i);
}

@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	initializeData();
	createView();
}



}
