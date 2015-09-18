package com.softedge.iopapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softedge.iopapp.Dashboard;
import com.softedge.iopapp.R;
import com.softedge.iopapp.iconContextMenu.IconContextMenu;

public class DashboardHomeFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	private Context mContext;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static DashboardHomeFragment newInstance(int sectionNumber,
			Context container) {
		DashboardHomeFragment fragment = new DashboardHomeFragment(container);
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public DashboardHomeFragment(Context context) {

		this.mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dashboard,
				container, false);

		TextView textViewDashboardBottomBarContacts = (TextView) rootView
				.findViewById(R.id.txtDashboardBottomBarContacts);
		TextView textViewDashboardBottomBarCommunity = (TextView) rootView
				.findViewById(R.id.txtDashboardBottomBarCommunity);
		TextView textViewDashboardBottomBarGroup = (TextView) rootView
				.findViewById(R.id.txtDashboardBottomBarGroup);

		textViewDashboardBottomBarContacts
				.setOnClickListener(contactsClickedListener);
		textViewDashboardBottomBarCommunity
				.setOnClickListener(communityClickedListener);
		textViewDashboardBottomBarGroup
				.setOnClickListener(groupClickedListener);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((Dashboard) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	/**
	 * 
	 * Listeners for the bottom bar to show the context menu
	 * 
	 * 
	 */
	private OnClickListener contactsClickedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			IconContextMenu cm = new IconContextMenu(mContext,
					R.menu.contacts_contextmenu);

			cm.show();

		}
	};

	private OnClickListener communityClickedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			IconContextMenu cm = new IconContextMenu(mContext,
					R.menu.community_contextmenu);

			cm.show();

		}
	};

	private OnClickListener groupClickedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			IconContextMenu cm = new IconContextMenu(mContext,
					R.menu.group_contextmenu);

			cm.show();

		}
	};

}
