package com.example.prilogulka

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.prilogulka.data.managers.SharedPreferencesManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

abstract class BaseActivity : AppCompatActivity() {

    abstract fun initLayoutFields()

    fun hasInternetConnection(): Boolean {
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        return wifi.isConnected() || mobile.isConnected()
    }

    abstract fun hasEmptyField(): Boolean

    private fun isEmpty(editText: EditText): Boolean {
        return editText.text.toString() == ""
    }

    fun isEmailValid(textInputLayout: TextInputLayout): Boolean = isEmailValid(textInputLayout.editText!!.text.toString())

    private fun isEmailValid(email: String): Boolean {
        return !email.isEmpty() && isEmailCorrect(email)
    }

    fun isEmailCorrect(textInputLayout: TextInputLayout): Boolean = isEmailCorrect(textInputLayout.editText!!.text.toString())

    private fun isEmailCorrect(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun resetEmailErrors(textInputLayout: TextInputLayout) {
        textInputLayout.isErrorEnabled = false
    }

    internal fun showHint(hintText: String) { //,} currentFocus: View) {
        val snackbar = Snackbar.make(currentFocus, hintText, Snackbar.LENGTH_INDEFINITE)
        val snackbarView = snackbar.view
        val snackBarTextView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
        snackBarTextView.setSingleLine(false)
        snackbar.setAction("Понятно", object : View.OnClickListener {
            override fun onClick(v: View) {
                snackbar.dismiss()
            }
        })
        snackbar.show()
    }
}