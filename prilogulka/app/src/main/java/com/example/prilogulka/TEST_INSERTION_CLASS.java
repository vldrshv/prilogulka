package com.example.prilogulka;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.Video;
import com.example.prilogulka.data_base.GiftCardsDataBaseImpl;
import com.example.prilogulka.data_base.UserActionsDataBaseImpl;
import com.example.prilogulka.data_base.interfaces.GiftCardsDataBase;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;


public class TEST_INSERTION_CLASS {

    String email;
    GiftCardsDataBase gcDB;
    UserActionsDataBase uaDB;

    int insertions;

    /**
     * TODO: удалить класс
     */

    public TEST_INSERTION_CLASS(Intent intent, Context context) {
        this.email = intent.getStringExtra("email");
        this.gcDB = new GiftCardsDataBaseImpl(context);
        this.uaDB = new UserActionsDataBaseImpl(context);
        this.insertions = 10;

        createGiftCardsDB();
        createUserActionsDB();
    }

    public TEST_INSERTION_CLASS(String email, Context context) {
        this.email = email;
        this.gcDB = new GiftCardsDataBaseImpl(context);
        this.uaDB = new UserActionsDataBaseImpl(context);
        this.insertions = 10;

        createGiftCardsDB();
        createUserActionsDB();
    }

    private void insertGiftCards(){
//        String email = intent.getStringExtra("email");
        gcDB.insertGiftCard(
                new GiftCard(
                        "ozone", R.drawable.ozon, 500, 700, 1000,
                        "22/12/2018",
                        "4678174783841904180",
                        "3291508445519959684",
                        "6542841053281775757",
                        email));
        gcDB.insertGiftCard(
                new GiftCard(
                        "media_markt", R.drawable.mediamarkt, 500, 700, 1000,
                        "22/01/2019",
                        "8160807698461422343",
                        "9334422799732387552",
                        "4213686227523742589",
                        email));
        gcDB.insertGiftCard(
                new GiftCard("h_and_m",
                        R.drawable.hsm, 100, 200, 400,
                        "31/12/2018",
                        "1265965848659032473",
                        "7141097201419916427",
                        "0464787375529263109",
                        email));
        gcDB.insertGiftCard(
                new GiftCard(
                        "ikea", R.drawable.ikea, 500, 700, 1000,
                        "22/11/2018",
                        "5214894533776071518",
                        "0505070128677752263",
                        "3960392527058878354",
                        email));
        gcDB.insertGiftCard(
                new GiftCard(
                        "sportmaster", R.drawable.sportmaster, 500, 700, 1000,
                        "22/10/2018",
                        "9009014726979736076",
                        "5596966136817336038",
                        "4571729541021890892",
                        email));
        gcDB.insertGiftCard(
                new GiftCard(
                        "mvideo", R.drawable.mvideo, 500, 700, 1000,
                        "22/12/2018",
                        "4334780084959586907",
                        "0499066072117791898",
                        "9597037566977305735",
                        email));
        gcDB.insertGiftCard(
                new GiftCard(
                        "tsum", R.drawable.tsum, 500, 700, 1000,
                        "28/12/2018",
                        "4496445331773311938",
                        "7817434369884739096",
                        "5281419660733309923",
                        email));
    }
    private void insertUserActions(@Nullable String... args){

        for (int i = 0; i < insertions; i++) {
            uaDB.insertUserActions(new Video(
                    email,
                    (int)(Math.random() * 10000) + " ",
                    setSign()*((int)(Math.random() * 10) + 1),
                    (args != null)
                            ? Time.getTodayTime() + " " + args[0] + "." + args[1] + "."+ args[2]
                            : Time.getTodayTime() + " " + Time.getToday()
            ), true);
        }
    }

    private void createGiftCardsDB(){
        gcDB.createTable();
    }
    private void createUserActionsDB(){
        uaDB.createTable();
    }
    private int setSign(){
        return (((int)(Math.random() * 100) % 5) == 0) ? -1 : 1;
    }

    public void resetDataBase(Class c) {
        if (c.equals(GiftCard.class)) {
            gcDB.dropTable();
            gcDB.createTable();
        }
        else if (c.equals(Video.class)){
            uaDB.dropTable();
            uaDB.createTable();
        }
    }
    public void setInsertionsCounter(int yourInsertionsAmount) {
        this.insertions = yourInsertionsAmount;
    }

    /**
     *
     * @param c set Video.class to insert User_Actions, set GiftCard.class to insert Gift_Cards
     */
    public void makeInsertion(Class c){
        if (c.equals(GiftCard.class))
            insertGiftCards();
        else if (c.equals(Video.class))
            insertUserActions(null);
    }

    /**
     *
     * @param c set Video.class to insert User_Actions, set GiftCard.class to insert Gift_Cards
     */
    public void makeInsertion(Class c, String day, String month, String year){
        if (c.equals(GiftCard.class))
            insertGiftCards();
        else if (c.equals(Video.class))
            insertUserActions(day, month, year);
    }
}
