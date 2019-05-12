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
import com.example.prilogulka.data_base.ActionsDAO;
import com.example.prilogulka.data_base.ActivatedCardsDAO;
import com.example.prilogulka.menu.ActivatedCardActivity;

public class HintDialogs {
    private String CLASS_TAG = "HintDialogs";

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

                ActionsDAO actionsDAO = new ActionsDAO(context);
                ActivatedCardsDAO activatedCardsDAO = new ActivatedCardsDAO(context);

                switch (cardId) {
                    case R.id.buyBronzeCard:
                        actionsDAO.insertOutcome(email, -card.getPriceBronze());
                        activatedCardsDAO.insert(giftCard, card.getPriceBronze(), email);
                        break;
                    case R.id.buySilverCard:
                        actionsDAO.insertOutcome(email, -card.getPriceSilver());
                        activatedCardsDAO.insert(giftCard, card.getPriceSilver(), email);
                        break;
                    case R.id.buyGoldenCard:
                        actionsDAO.insertOutcome(email, -card.getPriceGold());
                        activatedCardsDAO.insert(giftCard, card.getPriceGold(), email);
                        break;
                }
                dialog.dismiss();

                Intent intent = new Intent (activity, ActivatedCardActivity.class);
                intent.putExtra("cardId", card.getCardId());
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

