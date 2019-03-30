package com.example.prilogulka.data.managers;

import com.example.prilogulka.data.Coefficient;
import com.example.prilogulka.data.Districts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CoefficientManager extends Districts {

    private ArrayList<Coefficient> coefs;

    public CoefficientManager() {
        coefs = new ArrayList<>();
        parseCoefs();
    }

    public ArrayList<Coefficient> getCoefficientsList(){
        return coefs;
    }

    public double getAgeCoefficient(String date){
        try {
            if (date.equals(""))
                return 0;
            int age = parseStringDateToAge(date);

            System.out.println("age = " + age);
            return getAgeCoefficient(age);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getAgeCoefficient(int age){
        for (Coefficient c : coefs){
            if (contains(age, c.getCategoriesList())) {
                System.out.println(c.getCategoriesList().toString());
                System.out.println("c.getCoef() = " + c.getCoef());
                return c.getCoef();
            }
        }

        return 1;
    }

    /*  ?????????????? ? ???????? ????. ?? ????? json    */
    private void parseCoefs() {
        System.out.println("PARSE COEFFS");
        for (int i = 1; i <= 3; i++){
            Coefficient c = new Coefficient();
            c.setType(i);

            int[] arr = new int[]{};
            if (i == 1) {
                arr = new int[]{18, 25, 36, 45};
                c.setCoef(1.0);
            } else if (i == 2) {
                arr = new int[]{26, 35};
                c.setCoef(1.1);
            } else if (i == 3) {
                arr = new int[]{46, 100};
                c.setCoef(1.2);
            }

            c.setCategoriesList(arr);
            coefs.add(c);
        }

        System.out.println(coefs);
    }

    /**
     * TODO: ??????? ??????? ? ???????
     */
    /*  ??????????? ????. ?? ????? json    */
    private void parseCoefsFromJson(String s) throws JSONException {
        JSONObject j = new JSONObject(s);
        JSONArray categories = j.getJSONArray("categories");
        for (int i = 0; i < categories.length(); i++){
            Coefficient c = new Coefficient();
            c.setType(categories.getJSONObject(i).getInt("name"));
            c.setCoef(categories.getJSONObject(i).getDouble("coefficient"));

            JSONArray duration = categories.getJSONObject(i).getJSONArray("duration");
            ArrayList<Integer> arr = new ArrayList<>();
            for (int k = 0; k < duration.length(); k++){
                arr.add(duration.getJSONObject(k).getInt("upper"));
                arr.add(duration.getJSONObject(k).getInt("down"));
            }
            c.setCategoriesList(arr);

            coefs.add(c);
        }
    }
    /*  ?????? ????????? ????????????? ???????? ? ???????? "19.07.1995" -> age    */
    private int parseStringDateToAge(String date) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date d = sdf.parse(date);

        int age = (int) ((System.currentTimeMillis() - d.getTime()) / (365*24*60) / (60*1000));

        return age;
    }
    /*  ?????????? ?????????? ?????? ? ??????? ?????. ?? ????? ??????? ????.????????    */
    private boolean contains(int age, ArrayList<Coefficient.Category> list) {
        for (Coefficient.Category aList : list) {
            if (age >= aList.getDOWN_category() && age <= aList.getUP_category())
                return true;
        }

        return false;
    }
}
