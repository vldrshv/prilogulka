package com.example.prilogulka.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Card;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.UserIO;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.data.userData.UserInfo;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO: 27.04.2019 BUY CARDS
public class CardShop extends AppCompatActivity implements Button.OnClickListener {

    String CLASS_TAG = "CardShop";

    // TODO: 27.04.2019 gift cards
    //GiftCardDAO giftCardDAO;
    Card giftCard;
    UserInfo user;
    double userMoney = 0;
    String email;
    SharedPreferencesManager spM;
    GiftCardService giftCardService;
    UserIO USER_IO;

    Button buyBronzeCard, buySilverCard, buyGoldenCard;
    ImageView giftCardView;
    TextView infoAboutCard;

    String CARD_INFO = "Акция продлится до ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_shop);

        spM = new SharedPreferencesManager(this);
        email = spM.getActiveUser();

        initServices();
        initUIReference();
        setToolbar();

        readAndUpdateUser();
        userMoney = user.getUser().getCurrent_balance();
        setValuesToLayout();
    }

    public void initUIReference() {
        buyBronzeCard = findViewById(R.id.buyBronzeCard);
        buyBronzeCard.setOnClickListener(this);

        buySilverCard = findViewById(R.id.buySilverCard);
        buySilverCard.setOnClickListener(this);

        buyGoldenCard = findViewById(R.id.buyGoldenCard);
        buyGoldenCard.setOnClickListener(this);

        giftCardView = findViewById(R.id.giftCardView);
        infoAboutCard = findViewById(R.id.infoAboutCard);
    }
    private void initServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        giftCardService = retrofit.create(GiftCardService.class);
        USER_IO = new UserIO(this);
    }
    private void readAndUpdateUser() {
        user = USER_IO.readUserFromLocal();
        user = USER_IO.getUserFromServerById(user.getUser().getId());
        USER_IO.writeUserToLocal(user);
    }
    public void setValuesToLayout() {
        getGiftCard();
        giftCard.setPrices();

        Log.i(CLASS_TAG, giftCard.toString());

        if (giftCard != null) {
            infoAboutCard.setText(CARD_INFO + Time.parseServerTime(giftCard.getDueDate()));

            buyBronzeCard.setText("Активировать карту на " + giftCard.getPriceBronze());
            buySilverCard.setText("Активировать карту на " + giftCard.getPriceSilver());
            buyGoldenCard.setText("Активировать карту на " + giftCard.getPriceGold());
            Picasso.get().load("http://92.53.65.46:3000/" + giftCard.getImageUrl())
                    .into(giftCardView);

            checkButtons();
        }

    }

    private void checkButtons() {
        buyBronzeCard.setEnabled(userMoney >= giftCard.getPriceBronze());
        buySilverCard.setEnabled(userMoney >= giftCard.getPriceSilver());
        buyGoldenCard.setEnabled(userMoney >= giftCard.getPriceGold());
        buyBronzeCard.setVisibility((giftCard.getPriceBronze() < 0) ? View.GONE : View.VISIBLE);
        buySilverCard.setVisibility((giftCard.getPriceSilver() < 0) ? View.GONE : View.VISIBLE);
        buyGoldenCard.setVisibility((giftCard.getPriceGold() < 0) ? View.GONE : View.VISIBLE);

        if (userMoney < giftCard.getPriceGold())
            buyGoldenCard.setBackgroundColor(getResources().getColor(R.color.buttonDisable));

        if (userMoney < giftCard.getPriceSilver())
            buySilverCard.setBackgroundColor(getResources().getColor(R.color.buttonDisable));

        if (userMoney < giftCard.getPriceBronze())
            buyBronzeCard.setBackgroundColor(getResources().getColor(R.color.buttonDisable));

    }

    public void getGiftCard() {
        Intent intent = getIntent();
        int cardId = intent.getIntExtra("giftCardId", 0);
        try {
            GiftCard gC = giftCardService.getGiftCard(cardId).execute().body();
            if (gC != null)
                giftCard = gC.getCard();
            Log.i(CLASS_TAG, giftCard.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    // TODO: 27.04.2019 activated gift cards
    @Override
    public void onClick(View v) {
        HintDialogs hd = new HintDialogs(this);
        GiftCard gcard = new GiftCard();
        gcard.setCard(giftCard);
        hd.showWarning("Вы действительно хотите активировать карту? После нажатия" +
                        " \"Активировать\" отменить активацию будет НЕВОЗМОЖНО. Она переместится в раздел активированные карты",
                "Использование карты", v.getId(), gcard);

        Log.i(CLASS_TAG, "Active check - " + hd.wasActivated());
    }

    @Override
    public void onResume(){
        super.onResume();
        readAndUpdateUser();
        checkButtons();
    }
}