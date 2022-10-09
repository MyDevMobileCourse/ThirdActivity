package com.example.thirdactivity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.example.thirdactivity.api.RestApiService
import com.example.thirdactivity.api.UserInfo
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Quit");
        alertDialogBuilder.setMessage("are you sure you want to quit ?  ?");
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            super.onBackPressed()
        };
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(
                applicationContext,
                "Canceled", Toast.LENGTH_SHORT
            ).show()
        };
        alertDialogBuilder.show();
    }

}