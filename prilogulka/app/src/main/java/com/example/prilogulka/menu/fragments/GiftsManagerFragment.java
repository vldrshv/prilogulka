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
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.TEST_INSERTION_CLASS;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.GiftCardsDataBaseImpl;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
import com.example.prilogulka.menu.CardShop;
import com.example.prilogulka.recycle_view_adapters.RVGiftCardsAdapter;
import com.example.prilogulka.recycle_view_adapters.RecyclerItemClickListener;

import java.util.List;
// здесь должны быть реализованы подарочные карты

public class GiftsManagerFragment extends Fragment {
    RecyclerView recyclerView;
    RVGiftCardsAdapter adapter;
    ViewGroup rootView;
    TextView moneyTextView;

    SharedPreferencesManager spM;
    String email;
    Menu menu;

    String CLASS_TAG = "GiftsManagerFragment";
    final String CLASS_TITLE = "Мой счет/Вывод средств";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(CLASS_TITLE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gifts_manager, container, false);

        spM = new SharedPreferencesManager(getContext());
        email = spM.getActiveUser();

        moneyTextView = rootView.findViewById(R.id.money_giftsManager);
        moneyTextView.setText("Состояние счета: " + getMoney());

        /**
         * TODO: заменить битом синхронизации
         */
        Log.i(CLASS_TAG, "TEST_INSERTION_CLASS.resetDataBase(GiftCard.class).makeInsertion(GiftCard.class)");
        TEST_INSERTION_CLASS t = new TEST_INSERTION_CLASS(email, getContext());
        //t.resetDataBase(GiftCard.class);
        t.makeInsertion(GiftCard.class);

        initRecycleView();
        setHasOptionsMenu(true);

        Log.i(CLASS_TAG, email);
        return rootView;
    }

    private float getMoney(){
        UserActionsDataBase userActionsDB = new UserActionsDataBaseImpl(getContext());
        return userActionsDB.getUserMoney(email);
    }

    /**
     * TODO: подгрузка данных с сервера
     * TODO: кеширование данных
     * TODO: отображение у пользователя на экране
     */

    public void initRecycleView() {

        recyclerView = rootView.findViewById(R.id.giftCardsRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        final GiftCardsDataBaseImpl giftCardsDataBase = new GiftCardsDataBaseImpl(getContext());

        adapter = new RVGiftCardsAdapter(
                giftCardsDataBase.selectAll(email), getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<GiftCard> list = giftCardsDataBase.selectAll(email);

                        Context context = view.getContext();
                        Intent intent = new Intent(context, CardShop.class);
                        intent.putExtra("giftCardName", list.get(position).getName());

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                item.setTitle("Состояние счета: " + getMoney());
                return true;
            case R.id.action_help:
                HintDialogs hd = new HintDialogs(getContext());
                hd.showHint(getString(R.string.giftsManagerHint), CLASS_TITLE);
                return true;
            default:
                break;
        }

        return false;
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        if (menu != null)
            menu.getItem(0).setTitle("Состояние счета: " +
                new UserActionsDataBaseImpl(getContext()).getUserMoney(email));
        moneyTextView.setText("Состояние счета: " + getMoney());
//        тупое решение, но  notifyDataSetChanged() не работает
//        adapter.notifyDataSetChanged();
        adapter = new RVGiftCardsAdapter(
                new GiftCardsDataBaseImpl(getContext()).selectAll(email), getContext());
        recyclerView.setAdapter(adapter);

    }
}