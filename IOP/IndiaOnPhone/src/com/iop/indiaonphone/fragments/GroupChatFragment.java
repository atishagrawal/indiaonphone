/**
 * 
 */
package com.iop.indiaonphone.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iop.indiaonphone.R;

/**
 * @author Atish Agrawal
 * 
 */
public class GroupChatFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GroupChatFragment newInstance(int sectionNumber) {
		GroupChatFragment fragment = new GroupChatFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public GroupChatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat_group,
				container, false);

		return rootView;
	}

}
