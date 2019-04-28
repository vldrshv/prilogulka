package com.example.prilogulka.data.android.interraction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.prilogulka.R;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
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
        builder.setPositiveButton("Активировать", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                UserActionsDataBase userActionsDB = new UserActionsDataBaseImpl(context);
                // TODO: 27.04.2019 activated gift cards
//                ActivatedCardsDataBase activatedCardsDB = new ActivatedCardsDataBaseImpl(context);
                String code = "";

                // TODO: 27.04.2019 cards hint
//                switch (cardId) {
//                    case R.id.buyBronzeCard:
//                        userActionsDB.insertOutCome(email, -giftCard.getCoastBronze());
//                        activatedCardsDB.insertActivatedCard(giftCard, giftCard.getCoastBronze(), giftCard.getBronzeCode());
//                        code = giftCard.getBronzeCode();
//                        break;
//                    case R.id.buySilverCard:
//                        userActionsDB.insertOutCome(email, -giftCard.getCoastSilver());
//                        activatedCardsDB.insertActivatedCard(giftCard, giftCard.getCoastSilver(), giftCard.getSilverCode());
//                        code = giftCard.getSilverCode();
//                        break;
//                    case R.id.buyGoldenCard:
//                        userActionsDB.insertOutCome(email, -giftCard.getCoastGolden());
//                        activatedCardsDB.insertActivatedCard(giftCard, giftCard.getCoastGolden(), giftCard.getGoldenCode());
//                        code = giftCard.getGoldenCode();
//                        break;
//                }
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

