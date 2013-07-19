package com.droidplanner.activitys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentTabHost;
import android.util.Log;
import android.view.InputDevice;
import android.view.Menu;
import android.view.MenuItem;
import com.droidplanner.R;
import com.droidplanner.fragments.FlightMapFragment;
import com.droidplanner.fragments.HudFragment;
import com.droidplanner.fragments.RCFragment;
import com.droidplanner.fragments.FlightMapFragment.OnFlighDataListener;
import com.google.android.gms.maps.model.LatLng;

public class RCActivity extends SuperActivity implements OnFlighDataListener {
	
	private RCFragment rcFragment;
	
	static final int NUM_FRAGMENT_ITEMS = 2;
	MyAdapter mAdapter;
	ViewPager mPager;
	FragmentTabHost mTabHost;
	
	private MenuItem bTogleRC;
	MenuItem connectButton;
	
	private LatLng guidedPoint;

	@Override
	int getNavigationItem() {
		return 2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rc);
		
		rcFragment = ((RCFragment)getFragmentManager().findFragmentById(R.id.rcFragment));
		/*
		mAdapter = new MyAdapter(getFragmentManager());
		mPager = (ViewPager)findViewById(R.id.rcPager);
		if (mPager != null) {
			mPager.setAdapter(mAdapter);
		}
		*/
		
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		if (mTabHost != null) {
			mTabHost.setup(this, getFragmentManager(), R.id.tabFrameLayout);
			mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("HUD"),
                        HudFragment.class, null);
			mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Map"),
                        FlightMapFragment.class, null);
		}
		/*
		/*
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }
	    };

	    for (int i = 0; i < 2; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText("Tab " + (i + 1))
	                        .setTabListener(tabListener));
	    }
		*/
	    
	}
	
	@Override
	protected void onDestroy() {
		disableRCOverride();
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		disableRCOverride();
		super.onPause();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_rc, menu);
		connectButton = menu.findItem(R.id.menu_connect);
		bTogleRC = menu.findItem(R.id.menu_rc_enable);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_rc_enable:
			toggleRcOverride();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}


	private void toggleRcOverride() {
		if (rcFragment.isRcOverrideActive()) {
			disableRCOverride();
		} else {
			enableRCOverride();
		}
	}

	private void enableRCOverride() {
		rcFragment.setRcOverrideActive(true);
		if (rcFragment.isRcOverrideActive()) {
			bTogleRC.setTitle(R.string.disable);
		} else {
			bTogleRC.setTitle(R.string.enable);
		}
	}

	private void disableRCOverride() {
		rcFragment.setRcOverrideActive(false);
		if (rcFragment.isRcOverrideActive()) {
			bTogleRC.setTitle(R.string.disable);
		} else {
			bTogleRC.setTitle(R.string.enable);
		}
	}

	@SuppressWarnings("unused")
	private void printInputDevicesToLog() {
		int[] inputIds = InputDevice.getDeviceIds();
		Log.d("DEV", "Found " + inputIds.length);
		for (int i = 0; i < inputIds.length; i++) {
			InputDevice inputDevice = InputDevice.getDevice(inputIds[i]);
			Log.d("DEV","name:"+inputDevice.getName()+" Sources:"+inputDevice.getSources());	
		}
	}
	
	public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENT_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
        	switch (position) {
        		case 0:
        			HudFragment hud = new HudFragment();
        			return hud;
        		case 1:
        			FlightMapFragment map = new FlightMapFragment();
        			return map;
        		default:
        			return null;
        	} //ArrayListFragment.newInstance(position);
        }
        
        @Override
    	public CharSequence getPageTitle (int position) {
    		switch (position) {
    		case 0:
    			return "HUD";
    		case 1:;
    			return "Map";
    		default:
    			return "";
    		}
    	}
    }
	

	@Override
	public void onSetGuidedMode(LatLng point) {
		changeDefaultAlt();		
		guidedPoint = point;
	}

}