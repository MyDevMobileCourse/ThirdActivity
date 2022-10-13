package com.example.thirdactivity

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.thirdactivity.api.RestApiService
import com.example.thirdactivity.api.UserInfo
import com.google.android.material.snackbar.Snackbar

class ListAdapter(private val context: Context, private val mUsers: MutableList<UserInfo>, private val mRowLayout: Int) : RecyclerView.Adapter<ListAdapter.UserViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mRowLayout, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = mUsers[position]
        holder.prenom.text = user.prenom
        holder.nom.text = user.nom
        holder.email.text = user.email
        holder.classe.text = user.classe
        holder.delete.setOnClickListener {
            showDeleteDialog(user)
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prenom: TextView = itemView.findViewById(R.id.prenomF)
        val nom: TextView = itemView.findViewById(R.id.nomF)
        val email: TextView = itemView.findViewById(R.id.emailF)
        val classe: TextView = itemView.findViewById(R.id.classeF)
        val delete: Button = itemView.findViewById(R.id.delete)

    }

    fun showDeleteDialog(user: UserInfo) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete user")
        builder.setMessage("Are you sure you want to delete this user?")
        builder.setPositiveButton("Yes") { dialog, which ->
            deleteUser(user)
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun deleteUser(user: UserInfo) {
        val apiService = RestApiService()
        println("user id: ${user.id}")

        apiService.deleteUser(user.id!!){
            it ->
            run {
                println("delete user: ")
                println("return value: $it")
                    refreshUsers()
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun refreshUsers() {
        val apiService = RestApiService()
        println("refreshing users")
        apiService.getUsers{
            run {
                if (it != null) {
                    mUsers.clear()
                    mUsers.addAll(it)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Error getting users", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}