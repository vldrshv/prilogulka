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
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.android.interraction.HintDialogs;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.GiftCardsDataBaseImpl;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.GiftCardsDataBase;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;

import java.util.List;

public class CardShop extends AppCompatActivity implements Button.OnClickListener {

    String CLASS_TAG = "CardShop";

    GiftCardsDataBase giftCardsDB;
    GiftCard giftCard;
    int userMoney;
    String email;
    SharedPreferencesManager spM;


    Button buyBronzeCard, buySilverCard, buyGoldenCard;
    ImageView giftCardView;
    TextView infoAboutCard;

    String CARD_INFO = "Акция продлиться до ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_shop);

        spM = new SharedPreferencesManager(this);
        email = spM.getActiveUser();

        initUIReference();
        setToolbar();

        getUsersMoney();
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

        giftCardsDB = new GiftCardsDataBaseImpl(this);
    }

    public void setValuesToLayout() {
        giftCard = findCardInDataBase();

        Log.i(CLASS_TAG, userMoney + " " + giftCard.getCoastBronze() + " " +
                (userMoney > giftCard.getCoastBronze()));
        if (giftCard != null) {
            giftCardView.setImageResource(giftCard.getDestination());
            infoAboutCard.setText(CARD_INFO + giftCard.getDescription());

            buyBronzeCard.setText("Активировать карту на " + giftCard.getCoastBronze());
            buySilverCard.setText("Активировать карту на " + giftCard.getCoastSilver());
            buyGoldenCard.setText("Активировать карту на " + giftCard.getCoastGolden());

            checkButtons();

        }

    }

    private void checkButtons() {
        buyBronzeCard.setEnabled(userMoney > giftCard.getCoastBronze());
        buySilverCard.setEnabled(userMoney > giftCard.getCoastSilver());
        buyGoldenCard.setEnabled(userMoney > giftCard.getCoastGolden());
    }

    public GiftCard findCardInDataBase() {
        Intent intent = getIntent();
        String cardName = intent.getStringExtra("giftCardName");
        List<GiftCard> list = giftCardsDB.selectAll(giftCardsDB.COLUMN_NAME, cardName, email);
        return (list.size() == 1) ? list.get(0) : null;
    }

    public void getUsersMoney() {
        UserActionsDataBase userActionsDB = new UserActionsDataBaseImpl(this);
        userMoney = (int) userActionsDB.getUserMoney(email);
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

    @Override
    public void onClick(View v) {
        HintDialogs hd = new HintDialogs(this);
        hd.showWarning("Вы действительно хотите активировать карту? После нажатия" +
                        " \"Активировать\" отменить активацию будет НЕВОЗМОЖНО. Она переместится в раздел активированные карты",
                "Использование карты", v.getId(), giftCard);

        Log.i(CLASS_TAG, "Active check - " + hd.wasActivated());

        /**
         * TODO: выделение строчки меню
         */

    }

    @Override
    public void onResume(){
        super.onResume();
        getUsersMoney();
        checkButtons();
    }
}