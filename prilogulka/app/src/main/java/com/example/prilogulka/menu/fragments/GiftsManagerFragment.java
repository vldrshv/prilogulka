package com.example.prilogulka.menu.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.UserIO;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.data_base.GiftCardDAO;
import com.example.prilogulka.menu.CardShop;
import com.example.prilogulka.recycle_view_adapters.RVGiftCardsAdapter;
import com.example.prilogulka.recycle_view_adapters.RecyclerItemClickListener;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// здесь должны быть реализованы подарочные карты

public class GiftsManagerFragment extends Fragment {
    RecyclerView recyclerView;
    RVGiftCardsAdapter adapter;
    ViewGroup rootView;
    TextView moneyTextView;

    SharedPreferencesManager spM;
    String email;
    Menu menu;
    GiftCardService service;
    List<GiftCard> giftCardList;

    UserIO USER_IO;

    String CLASS_TAG = "GiftsManagerFragment";
    final String CLASS_TITLE = "Мой счет/Вывод средств";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gifts_manager, container, false);

        USER_IO = new UserIO(getContext());

        spM = new SharedPreferencesManager(getContext());
        email = spM.getActiveUser();

        moneyTextView = rootView.findViewById(R.id.money_giftsManager);
        moneyTextView.setText("Состояние счета: " + getMoney());

        GiftCardsGetter task = new GiftCardsGetter();
        task.doInBackground();

        initRecycleView();
        setHasOptionsMenu(true);


        Log.i(CLASS_TAG, email);
        return rootView;
    }

    private double getMoney() {
        UserInfo user = USER_IO.readUser();
        return user != null ? user.getUser().getCurrent_balance() : 0;
    }

    public void initRecycleView() {

        recyclerView = rootView.findViewById(R.id.giftCardsRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        adapter = new RVGiftCardsAdapter(giftCardList, getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                /**
                                 * TODO: заменить битом синхронизации
                                 */

                                Context context = view.getContext();
                                Intent intent = new Intent(context, CardShop.class);
                                intent.putExtra("giftCardId", giftCardList.get(position).getCard().getCardId());
                                Log.i(CLASS_TAG, "SELECTED_CARD: " + giftCardList.get(position).getCard().getCardId());

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
        this.menu = menu;
        menu.getItem(0).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                HintDialogs hd = new HintDialogs(getContext());
                hd.showHint(getString(R.string.giftsManagerHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }

    class GiftCardsGetter extends AsyncTask<Void, Void, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://92.53.65.46:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(GiftCardService.class);
            try {
                giftCardList = service.getAllGiftCards().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(CLASS_TAG, "DOWNLOADED " + giftCardList.size());

            return null;
        }

    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        if (menu != null)
            menu.getItem(0).setTitle("Состояние счета: " + getMoney());
        moneyTextView.setText("Состояние счета: " + getMoney());

//
    }
}