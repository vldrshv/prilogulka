package com.example.prilogulka.menu.fragments;

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
import com.example.prilogulka.data.GiftCardK;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.data_base.GiftCardDAO;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
import com.example.prilogulka.recycle_view_adapters.RVGiftCardsAdapter;

import java.io.IOException;
import java.util.ArrayList;
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

        GiftCardsGetter task = new GiftCardsGetter();
        task.doInBackground();

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
        GiftCardDAO giftCardDAO = new GiftCardDAO(getContext());

        adapter = new RVGiftCardsAdapter(giftCardDAO.selectAll(), getContext());
        recyclerView.setAdapter(adapter);

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), recyclerView,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        List<GiftCard> list = giftCardsDataBase.selectAll(email);
//
//                        Context context = view.getContext();
//                        Intent intent = new Intent(context, CardShop.class);
//                        intent.putExtra("giftCardName", list.get(position).getName());
//
//                        context.startActivity(intent);
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                        /**
//                         * TODO: кнопка не интересно, повысить приоритет.
//                         */
//                    }
//                })
//        );

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

    class GiftCardsGetter extends AsyncTask<Void, Void, Void> {

        private GiftCardDAO giftCardDAO = new GiftCardDAO(getContext());
        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://92.53.65.46:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(GiftCardService.class);
            List<GiftCardK> giftCardsList = new ArrayList<>();
            try {
                giftCardsList = service.getAllGiftCards().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!giftCardsList.isEmpty())
                giftCardDAO.insert(giftCardsList);

            giftCardsList.clear();
            giftCardsList.addAll(giftCardDAO.selectAll());

            return null;
        }

    }


    @Override
    public void onResume(){
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        if (menu != null)
            menu.getItem(0).setTitle("Состояние счета: " +
                new UserActionsDataBaseImpl(getContext()).getUserMoney(email));
        moneyTextView.setText("Состояние счета: " + getMoney());
    }
}