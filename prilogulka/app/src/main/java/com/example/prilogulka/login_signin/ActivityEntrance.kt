package com.example.prilogulka.login_signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.prilogulka.BaseActivity
import com.example.prilogulka.R
import com.example.prilogulka.data.UserIO
import com.example.prilogulka.data.managers.SharedPreferencesManager
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
        if (!spManager!!.isFirstEnter) {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, R.id.activity_enterance)
        when(v!!.id) {
            R.id.buttonEnter -> checkBeforeEnter()
            R.id.buttonRegister -> startActivity(Intent(this, UserInfoActivity::class.java))
        }
    }

    private fun checkBeforeEnter() {
        if (isEmailValid(email_text_input_layout)) {
            if (userExistsAtServer()) {
                spManager!!.activeUser = email.text.toString()
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            } else {
                showHint("Вы не зарегистрированы. Нажмите кнопку \"Регистрация\"")
            }
        }
        else
            showHint("Проверьте правильность ввода \"Email\"")
    }

    private fun userExistsAtServer(): Boolean {
        val USER_IO = UserIO(this)
        val user = USER_IO.getUserFromServerByEmail(email.text.toString())

        return user.user != null
    }
}
