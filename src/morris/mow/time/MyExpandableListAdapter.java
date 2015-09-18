package morris.mow.time;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private final ArrayList<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public MyExpandableListAdapter(Activity act, ArrayList<Group> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		text.setText(children);

		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	public long getGroupId(int groupPosition) {
		return 0;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Group group = (Group) getGroup(groupPosition);
		if (group.date.contentEquals("")) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.today_listrow_group,
						null);
			}
			CheckedTextView ctv_name = (CheckedTextView) convertView
					.findViewById(R.id.today_group_name);
			ctv_name.setText(group.name);
			ctv_name.setChecked(isExpanded);

			if (group.children.get(0).contentEquals("Stop Timer")) {
				convertView.setBackgroundColor(Color.parseColor("#648b36"));

			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
		} else {

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listrow_group, null);
			}
			CheckedTextView ctv_name = (CheckedTextView) convertView
					.findViewById(R.id.group_name);
			CheckedTextView ctv_date = (CheckedTextView) convertView
					.findViewById(R.id.group_date);
			ctv_name.setText(group.name);
			ctv_date.setText(group.date);

			ctv_name.setChecked(isExpanded);
			ctv_date.setChecked(isExpanded);
			if (group.children.get(0).contentEquals("Stop Timer")) {
				convertView.setBackgroundColor(Color.parseColor("#648b36"));

			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}

		}
		// ((CheckedTextView) convertView).setText(group.name);

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}