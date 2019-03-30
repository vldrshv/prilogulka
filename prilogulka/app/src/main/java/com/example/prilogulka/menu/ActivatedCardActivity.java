package com.example.prilogulka.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data_base.ActivatedCardsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.ActivatedCardsDataBase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ActivatedCardActivity extends AppCompatActivity {
    ActivatedCardsDataBase activatedCardsDB;
    GiftCard giftCard;

    SharedPreferencesManager spM;
    String email;


    ImageView giftCardView, barcodeImage;
    TextView activatedCardNumber;

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

        activatedCardsDB = new ActivatedCardsDataBaseImpl(this);
    }
    public void setValuesToLayout(){
        giftCard = findCardInDataBase();

        if (giftCard != null) {
            giftCardView.setImageResource(giftCard.getDestination());
            barcodeImage.setImageBitmap(createBarCode(giftCard.getBronzeCode()));
            activatedCardNumber.setText(giftCard.getBronzeCode() + "");
        }

    }
    public GiftCard findCardInDataBase(){
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");

        return activatedCardsDB.findCard(code, email);
    }
    public void setToolbar(){
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
    public void onBackPressed(){
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
    }

    public Bitmap createBarCode(String codeText){
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
}
