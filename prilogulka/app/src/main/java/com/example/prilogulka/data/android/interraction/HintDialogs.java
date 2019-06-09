package com.example.prilogulka.data.android.interraction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Card;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.GiftCardService;
import com.example.prilogulka.menu.ActivatedCardActivity;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HintDialogs {
    private String CLASS_TAG = "HintDialogs";

    private Context context;
    String email;
    Activity activity;

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

    public void showWarning(String message, String title, final Card giftCard, int price, int userId) {
        AlertDialog dialog = createDialogBuilder(message, title, giftCard, price, userId).create();
        dialog.show();

    }

    private AlertDialog.Builder createDialogBuilder(String message, String title, final Card giftCard, final int price, final int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);
        builder.setPositiveButton("Активировать", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();

//            Intent intent = new Intent (activity, ActivatedCardActivity.class);
//            intent.putExtra("cardId", giftCard.getCardId());
//            activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder;
    }


}

