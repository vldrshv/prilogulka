package com.example.prilogulka.recycle_view_adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.UserCard;
import com.example.prilogulka.data.UserGiftCard;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVActivatedCardsAdapter extends RecyclerView.Adapter<RVActivatedCardsAdapter.ActivatedCardsHolder> {
    private final String CLASS_TAG = "RVActivatedCardsAdapter";

    private List<UserGiftCard> giftCardsList;

    public RVActivatedCardsAdapter(List<UserGiftCard> giftCardsList, Context context) {
        this.giftCardsList = giftCardsList;
    }

    public void updateData(List<UserGiftCard> newGiftCardsList) {
        this.giftCardsList = newGiftCardsList;
        notifyDataSetChanged();
    }

    public static class ActivatedCardsHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardDescription, cardPrice;
        ImageView cardImage;
        ProgressBar giftCardProgressBar;


        ActivatedCardsHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.giftCards_cv);
            cardDescription = (TextView) itemView.findViewById(R.id.giftCardDescription);
            cardPrice = (TextView) itemView.findViewById(R.id.giftCardPrice);
            cardImage = (ImageView) itemView.findViewById(R.id.giftCardImage);
            giftCardProgressBar = itemView.findViewById(R.id.giftCardProgressBar);
        }

    }

    @Override
    public void onBindViewHolder(RVActivatedCardsAdapter.ActivatedCardsHolder personViewHolder, int i) {
        UserCard giftCard = giftCardsList.get(i).getCard();
        personViewHolder.cardDescription.setText(giftCard.getDescription());
        personViewHolder.cardPrice.setText(giftCard.getPrice() + "");
        personViewHolder.giftCardProgressBar.setVisibility(View.GONE);
        Picasso.get()
                .load("http://92.53.65.46:3000/" + giftCard.getImageUrl())
                .into(personViewHolder.cardImage);
        Log.i(CLASS_TAG, giftCard.getImageUrl());
    }


    @Override
    public RVActivatedCardsAdapter.ActivatedCardsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gift_cards_card_view, viewGroup, false);
        RVActivatedCardsAdapter.ActivatedCardsHolder pvh = new RVActivatedCardsAdapter.ActivatedCardsHolder(v);
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

