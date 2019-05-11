package com.example.prilogulka.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.prilogulka.R;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.menu.HelpAppActivity;


public class HelpWithAppFragment extends Fragment implements View.OnClickListener{
    ViewGroup rootView;
    CardView cv1, cv2, cv3, cv4;
    String email;

    final String CLASS_TITLE = "Как это работает";

    /**
     * TODO: почитстить класс
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_help_with_app, container, false);
        cv1 = rootView.findViewById(R.id.cv);
        cv2 = rootView.findViewById(R.id.cv2);
        cv3 = rootView.findViewById(R.id.cv3);
        cv4 = rootView.findViewById(R.id.cv4);

        cv1.setOnClickListener(this);
        cv2.setOnClickListener(this);
        cv3.setOnClickListener(this);
        cv4.setOnClickListener(this);

        email = new SharedPreferencesManager(getContext()).getActiveUser();
        //Task myTask = new Task();
        //myTask.execute();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), HelpAppActivity.class);
        switch (v.getId()){
            case R.id.cv: // watch ad and earn money
                intent.putExtra("description", "watch ad and earn money");
                startActivity(intent);
                break;
            case R.id.cv2: // what the income depends on
                intent.putExtra("description", "what the income depends on");
                startActivity(intent);
                break;
            case R.id.cv3: // secrets of watching
                intent.putExtra("description", "secrets of watching");
                startActivity(intent);
                break;
            case R.id.cv4: // want to spend my legally earned money
                intent.putExtra("description", "want to spend my legally earned money");
                startActivity(intent);
                break;
        }
    }

    /**
     * TODO: подключение к сервачку
     */

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
                hd.showHint(getString(R.string.helpWithAppHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }
}
