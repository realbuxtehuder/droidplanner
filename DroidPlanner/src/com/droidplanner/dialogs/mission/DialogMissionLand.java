package com.droidplanner.dialogs.mission;

import android.view.View;

import com.droidplanner.R;

public class DialogMissionLand extends DialogMission {

	@Override
	protected int getResource() {
		return R.layout.dialog_mission_land;
	}
	
	protected View buildView() {
		super.buildView();		
		return view;
	}

}
