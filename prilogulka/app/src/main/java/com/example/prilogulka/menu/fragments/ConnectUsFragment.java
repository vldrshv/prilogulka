package com.example.prilogulka.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prilogulka.R;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.userData.User;
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.data_base.UserInfoDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserInfoDataBase;

import java.util.List;

public class ConnectUsFragment extends Fragment {

    SharedPreferencesManager spManager;
    UserInfoDataBase userDB;
    EditText editEmail, editName, editSurname, editMessage;
    Button send;

    ViewGroup rootView;

    String CLASS_TAG = "ConnectUsFragment";
    final String CLASS_TITLE = "Обратиться к нам";
    String email;

    /**
     * TODO: ПОЧИСТИТЬ КЛАСС
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_connect_us, container, false);
        spManager = new SharedPreferencesManager(getContext());
        userDB = new UserInfoDataBaseImpl(getContext());

        email = spManager.getActiveUser();
        List<User> userList = userDB.findUserInfo(userDB.COLUMN_EMAIL, email);
        User user;

        String info = "", info1 = "", info2 = "";
        if (userList.size() == 1) {
            user = userList.get(0);

            info = user.getName();
            info1 = user.getLastname();
            info2 = user.getEmail();
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_connect_us, container, false);
        editName = rootView.findViewById(R.id.name_connect_us);
        editName.setText(info);
        editName.setKeyListener(null);

        editSurname = rootView.findViewById(R.id.surname_connect_us);
        editSurname.setText(info1);
        editSurname.setKeyListener(null);

        editEmail = rootView.findViewById(R.id.e_mail);
        editEmail.setText(info2);
        editEmail.setKeyListener(null);

        editMessage = rootView.findViewById(R.id.message);

        send = rootView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                // Кому
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { "eva13u113@gmail.com" });
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

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                ActionsDAO actionsDAO = new ActionsDAO(getContext());
                item.setTitle("Состояние счета: " + (actionsDAO.getUserMoney(email)));
                return true;
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