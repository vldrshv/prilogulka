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
import com.example.prilogulka.data.Card;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.UserIO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVGiftCardsAdapter extends RecyclerView.Adapter<RVGiftCardsAdapter.GiftCardsHolder> {
    private List<GiftCard> giftCardsList;
    private int progress;

    public RVGiftCardsAdapter(List<GiftCard> giftCardsList, Context context) {
        this.giftCardsList = giftCardsList;

        UserIO USER_IO = new UserIO(context);
        progress = (int)USER_IO.getMoney();
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
        Card giftCard = giftCardsList.get(i).getCard();
        personViewHolder.cardDescription.setText(giftCard.getDescription());
        giftCard.setPrices();

        Log.i("ADAPTER", i+"");
        int price = giftCard.getPriceBronze();
        if (price == -1) { // если не пришли прайсы
            personViewHolder.cv.setVisibility(View.GONE);
            return;
        }
        personViewHolder.cardPrice.setText(price+"");
        personViewHolder.giftCardProgressBar.setMax(price == 0 ? 1 : price);
        personViewHolder.giftCardProgressBar.setProgress(price == 0 ? 1 : progress);

        Picasso.get()
                .load("http://92.53.65.46:3000/" + giftCard.getImageUrl())
                .into(personViewHolder.cardImage);

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

