/**
 * @author Atish Agrawal
 */
package com.iop.indiaonphone.interfaces;

import java.util.List;

import com.iop.indiaonphone.chatUtils.Message;

/**
 * @author Atish Agrawal
 * 
 */
public interface OnReceivingChatMessages {

	public void onTaskCompleted(List<Message> messageList);

}
