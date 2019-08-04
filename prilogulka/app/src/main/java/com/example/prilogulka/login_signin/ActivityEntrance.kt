package com.example.prilogulka.login_signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.prilogulka.BaseActivity
import com.example.prilogulka.R
import com.example.prilogulka.data.managers.SharedPreferencesManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_enterance.*

class ActivityEntrance : BaseActivity(), View.OnClickListener {
    var spManager: SharedPreferencesManager? = null

    override fun initLayoutFields() {
        setContentView(R.layout.activity_enterance)
        buttonEnter.setOnClickListener(this)
        buttonRegister.setOnClickListener(this)
    }

    override fun hasEmptyField(): Boolean {
        return email.text!!.toString() == ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spManager = SharedPreferencesManager(this)

        enterIfUserExists()
        initLayoutFields()

    }

    private fun enterIfUserExists() {
        if (!spManager!!.isFirstEnter)
            startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.buttonEnter -> checkBeforeEnter()
            R.id.buttonRegister -> startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    private fun checkBeforeEnter() {
        if (isEmailValid(email_text_input_layout)) {
            spManager!!.activeUser = email.text.toString()
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
        else
            showHint("Проверьте правильность ввода \"Email\"")
    }
}
