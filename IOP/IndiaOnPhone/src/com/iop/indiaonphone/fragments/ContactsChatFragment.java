/**
 * 
 */
package com.iop.indiaonphone.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iop.indiaonphone.R;
import com.iop.indiaonphone.AsyncTasks.GetChatContactsAPI;

/**
 * @author Atish Agrawal
 * 
 */
public class ContactsChatFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ContactsChatFragment newInstance(int sectionNumber) {
		ContactsChatFragment fragment = new ContactsChatFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ContactsChatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat_contacts,
				container, false);

		// Firing GetChatContactsAPI

		ListView listViewChatContacts=(ListView)rootView.findViewById(R.id.listChatContacts);
		
		new GetChatContactsAPI(getActivity(), listViewChatContacts).execute();

		return rootView;
	}
}
