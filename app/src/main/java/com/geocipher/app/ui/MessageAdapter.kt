package com.geocipher.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geocipher.app.R
import com.geocipher.app.models.Message

class MessageAdapter(private val onMessageClick: (Message) -> Unit) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private val messages = mutableListOf<Message>()

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageNumber: TextView = view.findViewById(R.id.message_number)
        val encrypted_text: TextView = view.findViewById(R.id.encrypted_text)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageNumber.text = "Message #${position + 1}"
        holder.encrypted_text.text = message.encryptedMessage
        holder.timestamp.text = message.timestamp.toDate().toString()
        holder.itemView.setOnClickListener {
            onMessageClick(message)
        }
    }

    override fun getItemCount() = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}