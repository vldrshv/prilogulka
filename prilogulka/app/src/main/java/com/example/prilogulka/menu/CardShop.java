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
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.GiftCardDAO;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
import com.squareup.picasso.Picasso;

import java.util.List;

// TODO: 27.04.2019 BUY CARDS
public class CardShop extends AppCompatActivity {// implements Button.OnClickListener {

    String CLASS_TAG = "CardShop";

    // TODO: 27.04.2019 gift cards
    GiftCardDAO giftCardDAO;
    Card giftCard;
    int userMoney;
    String email;
    SharedPreferencesManager spM;


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

        initUIReference();
        setToolbar();

        getUsersMoney();
        setValuesToLayout();
    }

    public void initUIReference() {
        buyBronzeCard = findViewById(R.id.buyBronzeCard);
//        buyBronzeCard.setOnClickListener(this);

        buySilverCard = findViewById(R.id.buySilverCard);
//        buySilverCard.setOnClickListener(this);

        buyGoldenCard = findViewById(R.id.buyGoldenCard);
//        buyGoldenCard.setOnClickListener(this);

        giftCardView = findViewById(R.id.giftCardView);
        infoAboutCard = findViewById(R.id.infoAboutCard);

        // TODO: 27.04.2019 gift cards
        giftCardDAO = new GiftCardDAO(this);
    }

    public void setValuesToLayout() {
        giftCard = findCardInDataBase().getCard();
        giftCard.setPrices();

        Log.i(CLASS_TAG, userMoney + " " + giftCard.getPriceBronze() + " " +
                (userMoney > giftCard.getPriceSilver()));
        if (giftCard != null) {
//            giftCardView.setImageResource(R.drawable.hsm);//giftCard.getDestination());
            infoAboutCard.setText(CARD_INFO + giftCard.getDueDate());

            buyBronzeCard.setText("Активировать карту на " + giftCard.getPriceBronze());
            buySilverCard.setText("Активировать карту на " + giftCard.getPriceSilver());
            buyGoldenCard.setText("Активировать карту на " + giftCard.getPriceGold());
            Picasso.get().load("http://92.53.65.46:3000/" + giftCard.getImageUrl())
                    .into(giftCardView);

            checkButtons();

        }

    }

    private void checkButtons() {
        // TODO: 27.04.2019 activated gift cards
        buyBronzeCard.setEnabled(userMoney > giftCard.getPriceBronze());
        buySilverCard.setEnabled(userMoney > giftCard.getPriceSilver());
        buyGoldenCard.setEnabled(userMoney > giftCard.getPriceGold());
        buyBronzeCard.setVisibility((giftCard.getPriceBronze() == 0) ? View.GONE : View.VISIBLE);
        buySilverCard.setVisibility((giftCard.getPriceSilver() == 0) ? View.GONE : View.VISIBLE);
        buyGoldenCard.setVisibility((giftCard.getPriceGold() == 0) ? View.GONE : View.VISIBLE);
    }

    public GiftCard findCardInDataBase() {
        Intent intent = getIntent();
        int cardId = intent.getIntExtra("giftCardId", 0);
        List<GiftCard> list = giftCardDAO.select(cardId);
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

//    @Override
//    public void onClick(View v) {
//        HintDialogs hd = new HintDialogs(this);
//        hd.showWarning("Вы действительно хотите активировать карту? После нажатия" +
//                        " \"Активировать\" отменить активацию будет НЕВОЗМОЖНО. Она переместится в раздел активированные карты",
//                "Использование карты", v.getId(), giftCard);
//
//        Log.i(CLASS_TAG, "Active check - " + hd.wasActivated());
//
//        /**
//         * TODO: выделение строчки меню
//         */
//
//    }

    @Override
    public void onResume(){
        super.onResume();
        getUsersMoney();
        checkButtons();
    }
}