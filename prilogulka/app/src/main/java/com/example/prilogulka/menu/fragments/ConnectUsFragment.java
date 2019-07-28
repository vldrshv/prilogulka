package com.example.prilogulka.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prilogulka.R;
import com.example.prilogulka.data.UserIO;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.userData.UserInfo;

public class ConnectUsFragment extends Fragment {
    String CLASS_TAG = "ConnectUsFragment";
    final String CLASS_TITLE = "Обратиться к нам";
    String email;

    UserIO USER_IO;
    SharedPreferencesManager spManager;
    UserInfo userInfo;


    EditText editEmail, editName, editSurname, editMessage;
    Button send;
    ViewGroup rootView;

    /**
     * TODO: ПОЧИСТИТЬ КЛАСС
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_connect_us, container, false);

        initGlobalVars();
        initTextInfo();
        initSendButton();

        setHasOptionsMenu(true);

        return rootView;
    }

    private void initGlobalVars() {
        spManager = new SharedPreferencesManager(getContext());
        email = spManager.getActiveUser();
        USER_IO = new UserIO(getContext());
        userInfo = USER_IO.readUserFromLocal();
    }
    private void initTextInfo() {
        String name = "", lastname = "";
        if (userInfo != null) {
            name = userInfo.getUser().getName();
            lastname = userInfo.getUser().getLastname();
        }
        editName = rootView.findViewById(R.id.name_connect_us);
        editName.setText(name);
        editName.setKeyListener(null);

        editSurname = rootView.findViewById(R.id.surname_connect_us);
        editSurname.setText(lastname);
        editSurname.setKeyListener(null);

        editEmail = rootView.findViewById(R.id.e_mail);
        editEmail.setText(email);
        editEmail.setKeyListener(null);

        editMessage = rootView.findViewById(R.id.message);

    }
    private void initSendButton() {
        send = rootView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { "yoboboshka@yandex.ru" });
                // Зачем
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Важное сообщение");
                // О чём
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        editMessage.getText().toString());

                // Поехали!
                ConnectUsFragment.this.startActivity(Intent.createChooser(emailIntent,
                        "Отправка письма..."));
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.getItem(0).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                HintDialogs hd = new HintDialogs(getContext());
                hd.showHint(getString(R.string.connectUsHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }

}