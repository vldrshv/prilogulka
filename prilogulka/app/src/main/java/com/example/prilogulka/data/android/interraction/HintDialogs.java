package com.example.prilogulka.data.android.interraction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Card;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.ActivatedCardsDAO;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;
import com.example.prilogulka.menu.ActivatedCardActivity;

public class HintDialogs {
    private Context context;
    String email;
    Activity activity;
    private boolean isActivated = false;

    public HintDialogs(Context context) {
        this.context = context;
        activity = (Activity)context;

        SharedPreferencesManager spManager = new SharedPreferencesManager(context);
        email = spManager.getActiveUser();
    }

    public void showHint(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean showWarning(String message, String title, final int cardId, final GiftCard giftCard) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);
        final Card card = giftCard.getCard();
        builder.setPositiveButton("Активировать", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                UserActionsDataBase userActionsDB = new UserActionsDataBaseImpl(context);
                // TODO: 27.04.2019 activated gift cards
                ActivatedCardsDAO activatedCardsDAO = new ActivatedCardsDAO(context);
                String code = "";

                // TODO: 27.04.2019 cards hint
                switch (cardId) {
                    case R.id.buyBronzeCard:
                        userActionsDB.insertOutCome(email, -card.getPriceBronze());
                        activatedCardsDAO.insert(giftCard, card.getPriceBronze());
                        code = card.getSerialNumber();
                        break;
                    case R.id.buySilverCard:
                        userActionsDB.insertOutCome(email, -card.getPriceSilver());
                        activatedCardsDAO.insert(giftCard, card.getPriceSilver());
                        code = card.getSerialNumber();
                        break;
                    case R.id.buyGoldenCard:
                        userActionsDB.insertOutCome(email, -card.getPriceGold());
                        activatedCardsDAO.insert(giftCard, card.getPriceGold());
                        code = card.getSerialNumber();
                        break;
                }
                dialog.dismiss();

                Intent intent = new Intent (activity, ActivatedCardActivity.class);
                intent.putExtra("code", code);
                activity.startActivity(intent);

                isActivated = true;

            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return isActivated;
    }

    public boolean wasActivated() {
        return isActivated;
    }
}

