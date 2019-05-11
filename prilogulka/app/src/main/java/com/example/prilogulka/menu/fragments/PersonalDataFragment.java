package com.example.prilogulka.menu.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.menu.QuestionnaireActivity;

import java.io.IOException;


public class PersonalDataFragment extends Fragment implements View.OnClickListener {

    private String CLASS_TAG = "PersonalDataFragment";

    private SharedPreferencesManager spManager;

    private ViewGroup rootView;
    private EditText editName, editSurname, editCity, editBirthday;
    private Spinner spinner;
    private Button buttonQuestionnaire;
    private TextView textQuestionnaire;

    private String email;
    private UserInfo user;

    private String CLASS_TITLE = "Личные данные";

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_personal_data, container, false);

        spManager = new SharedPreferencesManager(getContext());
        email = spManager.getActiveUser();

        serializeUser();
        initLayoutFields();
        showQuestionnaire();
        setTextToLayout();

        setHasOptionsMenu(true);

        return rootView;
    }

    private void serializeUser() {
        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(getContext());
        user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        if (user != null)
            Log.i(CLASS_TAG, user.toString());
    }
    private void initLayoutFields() {
        editName = rootView.findViewById(R.id.name);
        editSurname = rootView.findViewById(R.id.surname);
        editCity = rootView.findViewById(R.id.city);
        editBirthday = rootView.findViewById(R.id.birthday);
        spinner = rootView.findViewById(R.id.sex);

        textQuestionnaire = rootView.findViewById(R.id.textQuestionaire);
        buttonQuestionnaire = rootView.findViewById(R.id.buttonQuestionnaire);
    }
    private void showQuestionnaire() {
        boolean additionQuestionnaire = spManager.getQuestionnaire();
        textQuestionnaire.setVisibility(additionQuestionnaire ? View.GONE : View.VISIBLE);
        buttonQuestionnaire.setVisibility(additionQuestionnaire ? View.GONE : View.VISIBLE);
        buttonQuestionnaire.setOnClickListener(this);
    }
    private void setTextToLayout() {
        int sex = user.getUser().getSex(); // 0 - male

        editName.setText(user.getUser().getName());
        editName.setKeyListener(null);

        editSurname.setText(user.getUser().getLastname());
        editSurname.setKeyListener(null);

        editCity.setText(user.getUser().getLocation());
        editCity.setKeyListener(null);

        editBirthday.setText(user.getUser().getBirthday());
        editBirthday.setKeyListener(null);

        spinner.setSelection(sex);
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
                hd.showHint(getString(R.string.personalDataHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonQuestionnaire : {
                startActivity(new Intent(getContext(), QuestionnaireActivity.class));

                break;
            }
        }
    }
    private void updateHeader() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView userCoefHeader = headerView.findViewById(R.id.nav_header_user_coef);
        float coeff = (float) (spManager.getWPCoefficient() * (spManager.getQuestionnaire() ? 1.2 : 1));
        userCoefHeader.setText(coeff + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        updateHeader();
    }

}