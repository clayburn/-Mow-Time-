package morris.mow.time;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
 * MainActivity: Launcher activity. Creates action bar and three tabs that run TodayClient.java,
 * Employees.java, and Client.java. All of which are Fragments
 * Lots of auto generated methods that can be used in the future
 */
public class MainActivity extends FragmentActivity {
	/** Called when the activity is first created. */

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
			}
		});
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub

					}

					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab,
					android.app.FragmentTransaction ft) {
				// show the given tab
				mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab,
					android.app.FragmentTransaction ft) {
				// hide the given tab
				mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabReselected(ActionBar.Tab tab,
					android.app.FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
		actionBar.addTab(actionBar.newTab().setText("Today")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Employees")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Clients")
				.setTabListener(tabListener));
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						OnPageSelectedListener frag = (OnPageSelectedListener) mSectionsPagerAdapter
								.instantiateItem(mViewPager, position);
						if (frag != null) {
							frag.onPageSelected();
						}
						getActionBar().setSelectedNavigationItem(position);
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent i;
		switch (item.getItemId()) {
		case R.id.ic:
			addQuery();
			return true;
		case R.id.download:
			i = new Intent(this, Export.class);
			startActivityForResult(i,1);
			return true;
		case R.id.money:
			i = new Intent(this, SettleUp.class);
			startActivityForResult(i,1);
			return true;
		}
		return true;

	}


	private void addQuery() {
		final Dialog query_dialog = new Dialog(this);
		query_dialog.setTitle("Create a new:");
		query_dialog.setContentView(R.layout.lawn_or_employee);
		Button lawn = (Button) query_dialog.findViewById(R.id.add_lawn_button);
		lawn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startAct();
//				addLawnDialog();
				query_dialog.dismiss();
			}
		});
		Button employee = (Button) query_dialog
				.findViewById(R.id.add_employee_button);
		employee.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Add Employee Dialog
				//addEmployeeDialog();
				startEmpAct();
				query_dialog.dismiss();
			}
		});
		query_dialog.show();
	}

	public void onSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	public void startAct(){
		Intent intent = new Intent(this, AddLawn.class);
		startActivity(intent);
	}
	
	public void startEmpAct(){
		Intent intent = new Intent(this, AddEmployee.class);
		startActivity(intent);
	}

}