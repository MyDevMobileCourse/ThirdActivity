package com.example.thirdactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thirdactivity.api.UserInfo

class ListAdapter(private val context: Context, private val mUsers: List<UserInfo>, private val mRowLayout: Int) : RecyclerView.Adapter<ListAdapter.UserViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mRowLayout, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = mUsers[position]
        holder.prenom.text = user.prenom
        holder.nom.text = user.nom
        holder.email.text = user.email
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prenom: TextView = itemView.findViewById(R.id.prenomF)
        val nom: TextView = itemView.findViewById(R.id.nomF)
        val email: TextView = itemView.findViewById(R.id.emailF)
    }

}