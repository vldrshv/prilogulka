
package com.example.prilogulka.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.ActivatedCardsDAO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

public class ActivatedCardActivity extends AppCompatActivity implements Button.OnClickListener{
    private String CLASS_TAG = "ActivatedCardActivity";

    // TODO: 27.04.2019 activated gift cards
    ActivatedCardsDAO activatedCardsDAO;
    GiftCard giftCard;

    SharedPreferencesManager spM;
    String email;
    Boolean wasAccepted = false;

    ImageView giftCardView, barcodeImage;
    TextView activatedCardNumber;
    Button btnActivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activated_card);

        spM = new SharedPreferencesManager(this);
        email = spM.getActiveUser();

        initUIReference();
        setToolbar();

        setValuesToLayout();
    }

    public void initUIReference(){
        giftCardView = findViewById(R.id.activatedCard);
        barcodeImage = findViewById(R.id.barcode);
        activatedCardNumber = findViewById(R.id.activatedCardNumber);
        btnActivate = findViewById(R.id.btnActivate);
        btnActivate.setOnClickListener(this);

        activatedCardsDAO = new ActivatedCardsDAO(this);
    }

    public void setValuesToLayout(){
        giftCard = findCardInDataBase();

        if (giftCard != null) {
            giftCardView.setImageResource(R.drawable.hsm);//giftCard.getDestination());
            Picasso.get().load("http://92.53.65.46:3000/" + giftCard.getCard().getImageUrl())
                    .into(giftCardView);
            activatedCardNumber.setText(giftCard.getCard().getSerialNumber() + "");
        }

    }
    public GiftCard findCardInDataBase(){
        Intent intent = getIntent();
        Log.i(CLASS_TAG, intent.getIntExtra("cardId", 0) + "");
        int cardId = intent.getIntExtra("cardId", 0);

        return activatedCardsDAO.select(cardId, email);//.findCard(code, email);
    }
    public void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void showHint(String hintText){
        View currentView = findViewById(R.id.activatedCard);
        final Snackbar snackbar = Snackbar.make(currentView, hintText, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView snackBarTextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setSingleLine(false);
        snackbar.setAction("Понятно", new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wasAccepted = true;
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnActivate) {
            if (!wasAccepted)
                showHint("Если Вы нажмете \"активировать\" еще раз, Вам будет показан QR код " +
                        "данной карточки. После этого карточка будет считаться ИСПОЛЬЗОВАННОЙ и " +
                        "удалится из списка активированных карт! Если Вы СОГЛАСНЫ, нажмите ПОНЯТНО и АКТИВИРОВАТЬ.");
            else // todo delete from activated
                barcodeImage.setImageBitmap(createQRcode(giftCard.getCard().getSerialNumber()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed(){
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }

    public Bitmap createBarCode(String codeText) {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = dm.widthPixels;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(codeText, BarcodeFormat.CODE_128, widthInDP,320);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Bitmap createQRcode(String codeText) {
        if (codeText != null) {

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            int widthInDP = dm.widthPixels;
            int heightInDP = dm.heightPixels;
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(codeText, BarcodeFormat.QR_CODE, widthInDP / 3 , heightInDP / 3);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                return barcodeEncoder.createBitmap(bitMatrix);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
