package com.example.prilogulka.menu.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.prilogulka.R;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
import com.example.prilogulka.menu.ActivatedCardActivity;
import com.example.prilogulka.recycle_view_adapters.RecyclerItemClickListener;

public class ActivatedCardsFragment  extends Fragment {

    RecyclerView recyclerView;
    ViewGroup rootView;

    SharedPreferencesManager spM;
    String email;

    String CLASS_TAG = "ActivatedCardsFragment";
    final String CLASS_TITLE = "Активированные карты";

    /**
     * TODO: удаление карточки из списка после истечения срока действия
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Активированные карты");
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_activated_card, container, false);

        spM = new SharedPreferencesManager(getContext());
        email = spM.getActiveUser();


        initRecycleView();
        setHasOptionsMenu(true);

        Log.i(CLASS_TAG, email);

        return rootView;
    }

    public void initRecycleView() {
        recyclerView = rootView.findViewById(R.id.activatedCardsRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        // TODO: 27.04.2019 activated gift cards
//        final ActivatedCardsDataBase activatedCardsDataBase = new ActivatedCardsDataBaseImpl(getContext());

//        RVActivatedCardsAdapter adapter = new RVActivatedCardsAdapter(
//                activatedCardsDataBase.selectAll(email), getContext());
//        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        List<GiftCard> list = activatedCardsDataBase.selectAll(email);

                        Context context = view.getContext();
                        Intent intent = new Intent(context, ActivatedCardActivity.class);
//                        intent.putExtra("code", list.get(position).getBronzeCode());

                        context.startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        /**
                         * TODO: кнопка не интересно, повысить приоритет.
                         */
                    }
                })
        );

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                UserActionsDataBase userActionsDB = new UserActionsDataBaseImpl(getContext());

                item.setTitle("Состояние счета: " + (userActionsDB.getUserMoney(email)));
                return true;
            case R.id.action_help:
                HintDialogs hd = new HintDialogs(getContext());
                hd.showHint(getString(R.string.activatedCardsHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }

}
