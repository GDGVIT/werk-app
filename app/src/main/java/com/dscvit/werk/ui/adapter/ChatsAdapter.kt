package com.dscvit.werk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.models.chat.Message

class ChatsAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    companion object {
        const val VIEW_TYPE_MY_MESSAGE = 1
        const val VIEW_TYPE_OTHER_MESSAGE = 2
    }

    var messageList = mutableListOf<Message>()

    fun setMessages(messages: List<Message>) {
        messageList = messages as MutableList<Message>
        notifyDataSetChanged()
    }

    fun appendMessage(message: Message) {
        messageList.add(message)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.sender) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.sender_chat_item,
                    viewGroup,
                    false
                )
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.receiver_chat_item,
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: MessageViewHolder, position: Int) {
        viewHolder.bind(messageList[position])
    }

    override fun getItemCount() = messageList.size

    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name_text)
        private val messageTextView: TextView = view.findViewById(R.id.message_text)

        override fun bind(message: Message) {
            nameTextView.text = message.sentBy.name
            messageTextView.text = message.message
        }
    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name_text)
        private val messageTextView: TextView = view.findViewById(R.id.message_text)

        override fun bind(message: Message) {
            nameTextView.text = message.sentBy.name
            messageTextView.text = message.message
        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}