package com.example.prilogulka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.recycle_view_adapters.RVActionsAdapter;

public class ListOfUserActionsFragment extends Fragment {

    /**
     * TODO: удалить класс
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_of_users_actions, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        UserActionsDataBaseImpl actionsDB = new UserActionsDataBaseImpl(getContext());

        RVActionsAdapter adapter = new RVActionsAdapter(actionsDB.selectAll());
        rv.setAdapter(adapter);
        return rootView;
    }
}