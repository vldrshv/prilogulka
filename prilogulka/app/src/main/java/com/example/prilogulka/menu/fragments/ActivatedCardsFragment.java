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
import com.example.prilogulka.data.UserGiftCard;
import com.example.prilogulka.data.UserIO;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.menu.ActivatedCardActivity;
import com.example.prilogulka.recycle_view_adapters.RVActivatedCardsAdapter;
import com.example.prilogulka.recycle_view_adapters.RecyclerItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivatedCardsFragment  extends Fragment {

    RecyclerView recyclerView;
    ViewGroup rootView;
    RVActivatedCardsAdapter adapter;

    SharedPreferencesManager spM;
    String email;

    String CLASS_TAG = "ActivatedCardsFragment";
    final String CLASS_TITLE = "Активированные карты";

    GiftCardService service;
    List<UserGiftCard> cardsList;

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
        for (UserGiftCard gc : cardsList)
            Log.i(CLASS_TAG, gc.toString());

        adapter = new RVActivatedCardsAdapter(cardsList, getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ActivatedCardActivity.class);
                        intent.putExtra("cardId", cardsList.get(position).getCard().getId());
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
        UserIO USER_IO = new UserIO(getContext());
        UserInfo user = USER_IO.readUserFromLocal();
        try {
            cardsList = new ArrayList<>();
            List<UserGiftCard> giftCardList = service.getBoughtGiftCards(user.getUser().getId()).execute().body();

            for (UserGiftCard gc : giftCardList)
                Log.i(CLASS_TAG, gc.toString());

            if (giftCardList != null)
                cardsList.addAll(giftCardList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.getItem(0).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            HintDialogs hd = new HintDialogs(getContext());
            hd.showHint(getString(R.string.activatedCardsHint), CLASS_TITLE);
            return true;
        }

        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        getBoughtCards();
        adapter.updateData(cardsList);
    }
}
