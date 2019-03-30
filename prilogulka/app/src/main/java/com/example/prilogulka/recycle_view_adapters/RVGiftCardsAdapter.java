package com.example.prilogulka.recycle_view_adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;

import java.util.List;

public class RVGiftCardsAdapter extends RecyclerView.Adapter<RVGiftCardsAdapter.GiftCardsHolder> {
    List<GiftCard> giftCardsList;
    UserActionsDataBase userActionsDB;
    String email;
    int progress;

    public RVGiftCardsAdapter(List<GiftCard> giftCardsList, Context context) {
        this.giftCardsList = giftCardsList;

        SharedPreferencesManager spManager = new SharedPreferencesManager(context);
        email = spManager.getActiveUser();

        userActionsDB = new UserActionsDataBaseImpl(context);
        progress = (int)userActionsDB.getUserMoney(email);
    }

    public static class GiftCardsHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardDescription, cardPrice;
        ImageView cardImage;
        ProgressBar giftCardProgressBar;

        GiftCardsHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.giftCards_cv);
            cardDescription = (TextView) itemView.findViewById(R.id.giftCardDescription);
            cardPrice = (TextView) itemView.findViewById(R.id.giftCardPrice);
            cardImage = (ImageView) itemView.findViewById(R.id.giftCardImage);
            giftCardProgressBar = itemView.findViewById(R.id.giftCardProgressBar);
        }

    }

    @Override
    public void onBindViewHolder(RVGiftCardsAdapter.GiftCardsHolder personViewHolder, int i) {
        personViewHolder.cardDescription.setText(giftCardsList.get(i).getDescription());
        personViewHolder.cardPrice.setText(giftCardsList.get(i).getCoastBronze() + "");
        personViewHolder.cardImage.setImageResource(giftCardsList.get(i).getDestination());
        personViewHolder.giftCardProgressBar.setMax(giftCardsList.get(i).getCoastBronze());
        personViewHolder.giftCardProgressBar.setProgress(progress);
        /**
         * TODO: описать функцию проверки статуса карточки
         */

    }


    @Override
    public RVGiftCardsAdapter.GiftCardsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gift_cards_card_view, viewGroup, false);
        RVGiftCardsAdapter.GiftCardsHolder pvh = new RVGiftCardsAdapter.GiftCardsHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return giftCardsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

