package com.iop.indiaonphone.Adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iop.indiaonphone.R;
import com.iop.indiaonphone.chatUtils.Message;
import com.iop.indiaonphone.utils.ProjectUtils;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private List<Message> messagesItems;

	public MessagesListAdapter(Context context, List<Message> navDrawerItems) {
		this.context = context;
		this.messagesItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return messagesItems.size();
	}

	@Override
	public Object getItem(int position) {
		return messagesItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * The following list not implemented reusable list items as list items
		 * are showing incorrect data Add the solution if you have one
		 * */

		Message m = messagesItems.get(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Identifying the message owner
		if (messagesItems.get(position).isSelf()) {
			// message belongs to you, so load the right aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_right,
					null);
		} else {
			// message belongs to other person, load the left aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_left,
					null);
		}

		TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

		txtMsg.setText(m.getMessage());
		lblFrom.setText(m.getFromName());

		if (messagesItems.get(position).isImage()) {
			// User Added Image

			byte[] decodedString = ProjectUtils.decodeImage(messagesItems.get(
					position).getImage());

			if (decodedString.length > 0) {

				Bitmap decodedByte = BitmapFactory.decodeByteArray(
						decodedString, 0, decodedString.length);
				ImageView imageView = (ImageView) convertView
						.findViewById(R.id.imgChatMessage);
				imageView.setImageBitmap(decodedByte);
				imageView.setVisibility(View.VISIBLE);
				txtMsg.setVisibility(View.GONE);

			}

		}

		return convertView;
	}
}
