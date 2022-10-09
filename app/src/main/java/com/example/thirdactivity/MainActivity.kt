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
    lateinit var root: ConstraintLayout;
    lateinit var prenomLabel: TextView;
    lateinit var nomLabel: TextView;
    lateinit var emailLabel: TextView;
    lateinit var prenom: EditText;
    lateinit var nom: EditText;
    lateinit var email: EditText;
    lateinit var terms: CheckBox;
    lateinit var submit: Button;
    lateinit var cancel: Button;

    private fun init() {
        root = findViewById(R.id.root);
        prenomLabel = findViewById(R.id.prenomLabel);
        nomLabel = findViewById(R.id.nomLabel);
        emailLabel = findViewById(R.id.emailLabel);
        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        terms = findViewById(R.id.terms);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
    }

    private fun isNotValid(label: TextView) {
        label.setTextColor(Color.RED);
        val animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        label.startAnimation(animation);
    }

    private fun isValid(label: TextView) {
        label.setTextColor(Color.BLACK);
    }

    private fun validate(input: EditText) {
        var label = when (input) {
            prenom -> {
                prenomLabel;
            }
            nom -> {
                nomLabel;
            }
            else -> {
                emailLabel;
            }
        }
        if (input.text.isBlank()) {
            this.isNotValid(label);
        } else {
            this.isValid(label);
        }
        if (input == email) {

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input.text).matches()) {
                this.isNotValid(emailLabel);
            } else {
                this.isValid(emailLabel);
            }
        }
    }

    private fun isAllValid(): Boolean {
        var valid =
            if (prenom.text.isBlank())
                false;
            else if (nom.text.isBlank())
                false;
            else if (email.text.isBlank())
                false;
            else !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches();

        return valid;
    }

    private fun sendData() {
        val apiService = RestApiService()
        val userInfo = UserInfo(
            id = null,
            prenom = prenom.text.toString(),
            nom = nom.text.toString(),
            email = email.text.toString()
        )
        val loadingDialog = AlertDialog.Builder(this);
        val dialog = loadingDialog.create();
        val dialogLayout = layoutInflater.inflate(R.layout.progressdialog, null);
        dialog.setView(dialogLayout);
        dialog.setCancelable(false);
        dialog.show();
        apiService.addUser(userInfo) {
            if (it?.id != null) {
                prenom.setText("");
                nom.setText("");
                email.setText("");
                terms.isChecked = false;
                val snackBar: Snackbar = Snackbar.make(root,"Creation done",Snackbar.LENGTH_LONG);
                snackBar.setTextColor(Color.GREEN);
                snackBar.show();

            } else {
                val snackBar: Snackbar =
                    Snackbar.make(root, "Creation Failed", Snackbar.LENGTH_INDEFINITE);
                snackBar.setAction("Retry") {
                    sendData();
                }
                snackBar.setActionTextColor(Color.RED);
                Handler(Looper.getMainLooper()).postDelayed({snackBar.dismiss()},7500);
                snackBar.show();
            }
            dialog.dismiss();
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.init();
        val inputs = mutableListOf(prenom, nom, email);
        submit.setOnClickListener {
            for (input in inputs) {
                this.validate(input);
            }
            if (!terms.isChecked) {
                this.isNotValid(terms);
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Submit");
                alertDialogBuilder.setMessage("Do you wanna submit ?");
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                this.sendData();
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

        cancel.setOnClickListener {
            prenom.setText("");
            nom.setText("");
            email.setText("");
            terms.isChecked = false;
        }

        // validate inputs on change
        for (input in inputs) {
            input.addTextChangedListener {
                this.validate(input);
            }
        }
        terms.setOnClickListener {
            if (!terms.isChecked) {
                this.isNotValid(terms);
            } else {
                this.isValid(terms);
            }
        }

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