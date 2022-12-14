package com.example.thirdactivity

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.thirdactivity.api.RestApiService
import com.example.thirdactivity.api.UserInfo
import com.example.thirdactivity.databinding.FormFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class Form : Fragment() {
    private var _binding: FormFragmentBinding? = null
    private val binding get() = _binding!!

    private var root: ConstraintLayout? = null
    private var prenomLabel: TextView?  = null
    private var nomLabel: TextView? = null
    private var emailLabel: TextView? = null
    private var prenom: EditText? = null
    private var nom: EditText? = null
    private var email: EditText? = null
    private var terms: CheckBox? = null
    private var submit: Button? = null
    private var cancel: Button? = null
    private var spinner: Spinner? = null
    private var date: TextView? = null
    private var dateLabel: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {
            _binding = FormFragmentBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                this.init()
        val inputs = mutableListOf(prenom, nom, email, date)
        submit?.setOnClickListener {
            for (input in inputs) {
                if(input == date){
                    if(input?.text == "dd/mm/yyyy") {
                        this.isNotValid(dateLabel)
                    }
                }else{
                    this.validate(input as EditText)
                }
            }
            if (!terms?.isChecked!!) {
                this.isNotValid(terms)
            } else {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle("Submit")
                alertDialogBuilder.setMessage("Do you wanna submit ?")
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                this.sendData()
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(
                        context,
                        "Canceled", Toast.LENGTH_SHORT
                    ).show()
                }
                alertDialogBuilder.show()
            }
        }

        cancel?.setOnClickListener {
            this.exit()
        }

        for (input in inputs) {
            input?.addTextChangedListener {
                this.validate(input as EditText?)
            }
        }
        terms?.setOnClickListener {
            if (!terms!!.isChecked) {
                this.isNotValid(terms)
            } else {
                this.isValid(terms)
            }
        }
    }

    private fun init() {
        root = _binding?.root
        prenomLabel = _binding?.prenomLabel
        nomLabel = _binding?.nomLabel
        emailLabel = _binding?.emailLabel
        prenom = _binding?.prenom
        nom = _binding?.nom
        email = _binding?.email
        terms = _binding?.terms
        submit = _binding?.submit
        cancel = _binding?.cancel
        spinner = binding.classSpinner
        date = binding.dateNaissance
        dateLabel = binding.dateNaissanceLabel
        this.initSpinner()
        this.initDatePicker()
    }
    private fun initDatePicker(){
        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
                date!!.text = sdf.format(cal.time)

        }

        date?.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private fun initSpinner(){
        var listOfItems = arrayOf("DSI2","DSI3","RSI2","RSI3","SEM2","SEM3")
        spinner!!.setOnItemSelectedListener(null)
        val array_adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, listOfItems) }
        if (array_adapter != null) {
            array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner!!.setAdapter(array_adapter)

    }

    private fun isNotValid(label: TextView?) {
        if (label != null){
            label.setTextColor(Color.RED)
            val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
            label.startAnimation(animation)
        }
    }

    private fun isValid(label: TextView?) {
        label?.setTextColor(Color.BLACK)
    }

    private fun validate(input: EditText?) {
        if(input != null){
            val label = when (input) {
                prenom -> {
                    prenomLabel
                }
                nom -> {
                    nomLabel
                }
                else -> {
                    emailLabel
                }
            }
            if (input.text.isBlank()) {
                this.isNotValid(label)
            } else {
                this.isValid(label)
            }
            if (input == email) {
                if (!Patterns.EMAIL_ADDRESS.matcher(input.text).matches()) {
                    this.isNotValid(emailLabel)
                } else {
                    this.isValid(emailLabel)
                }
            }
        }
    }

    private fun exit(){
        if(prenom?.text?.isNotBlank() == true || nom?.text?.isNotBlank() == true || email?.text?.isNotBlank() == true){
            prenom?.setText("")
            nom?.setText("")
            email?.setText("")
            terms?.isChecked = false
            Toast.makeText(context,"Click again to go home",Toast.LENGTH_LONG).show()
        }else{
            findNavController().navigate(R.id.form_home)
        }
    }

    private fun isAllValid(): Boolean {

        return if (prenom?.text?.isBlank() == true)
            false
        else if (nom?.text?.isBlank() == true)
            false
        else if (email?.text?.isBlank() == true)
            false
        else !Patterns.EMAIL_ADDRESS.matcher(email?.text).matches()
    }

    private fun sendData() {
        val apiService = RestApiService()
        val userInfo = UserInfo(
            id = null,
            prenom = prenom?.text.toString(),
            nom = nom?.text.toString(),
            email = email?.text.toString(),
            classe = spinner?.selectedItem.toString()
        )
            val loadingDialog = AlertDialog.Builder(requireContext())
            val dialog = loadingDialog.create()
            val dialogLayout = layoutInflater.inflate(R.layout.progressdialog, null)
            dialog.setView(dialogLayout)
            dialog.setCancelable(false)
            dialog.show()
            apiService.addUser(userInfo) {
                if (it?.id != null) {
                    prenom?.setText("")
                    nom?.setText("")
                    email?.setText("")
                    terms?.isChecked = false
                    val snackBar: Snackbar = Snackbar.make(requireView(),"Creation done", Snackbar.LENGTH_LONG)
                    snackBar.setTextColor(Color.GREEN)
                    snackBar.setAction("Home"){
                        findNavController().navigate(R.id.form_home)
                    }
                    snackBar.show()
                } else {
                    val snackBar: Snackbar =
                        Snackbar.make(requireView(), "Creation Failed", Snackbar.LENGTH_INDEFINITE)
                    snackBar.setAction("Retry") {
                        sendData()
                    }
                    snackBar.setActionTextColor(Color.RED)
                    Handler(Looper.getMainLooper()).postDelayed({snackBar.dismiss()},7500)
                    snackBar.show()
                }
                dialog.dismiss()
            }
        }
    }

