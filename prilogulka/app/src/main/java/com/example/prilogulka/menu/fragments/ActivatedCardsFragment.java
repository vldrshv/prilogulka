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
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.menu.ActivatedCardActivity;
import com.example.prilogulka.recycle_view_adapters.RVActivatedCardsAdapter;
import com.example.prilogulka.recycle_view_adapters.RecyclerItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivatedCardsFragment  extends Fragment {

    RecyclerView recyclerView;
    ViewGroup rootView;

    SharedPreferencesManager spM;
    String email;

    String CLASS_TAG = "ActivatedCardsFragment";
    final String CLASS_TITLE = "Активированные карты";

    GiftCardService service;
    List<GiftCard> cardsList;

    /**
     * TODO: удаление карточки из списка после истечения срока действия
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Активированные карты");
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_activated_card, container, false);

        spM = new SharedPreferencesManager(getContext());
        email = spM.getActiveUser();

        initService();
        getBoughtCards();
        initRecycleView();

        setHasOptionsMenu(true);

        Log.i(CLASS_TAG, email);

        return rootView;
    }

    private void initRecycleView() {
        recyclerView = rootView.findViewById(R.id.activatedCardsRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        // TODO: 27.04.2019 activated gift cards
        Log.i(CLASS_TAG, email);
        for (GiftCard gc : cardsList)
            Log.i(CLASS_TAG, gc.toString());

        RVActivatedCardsAdapter adapter = new RVActivatedCardsAdapter(cardsList, getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ActivatedCardActivity.class);
                        intent.putExtra("cardId", cardsList.get(position).getCard().getCardId());
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
    private void initService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GiftCardService.class);
    }
    /**
     * Скачиваем карточки, купленные пользователем
     */
    private void getBoughtCards() {
        UserInfo user = readUser();
        try {
            cardsList = new ArrayList<>();
            cardsList.addAll(service.getBoughtGiftCards(user.getUser().getId()).execute().body());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                hd.showHint(getString(R.string.activatedCardsHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }

    private UserInfo readUser() {
        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(getContext());
        UserInfo user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        if (user != null)
            Log.i(CLASS_TAG, user.toString());

        return user;
    }

}
