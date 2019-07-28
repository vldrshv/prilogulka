package com.example.prilogulka.menu;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Districts;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.QuestionnaireService;
import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.Questionnaire;
import com.example.prilogulka.data.userData.QuestionnaireInfo;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.data_base.VideoDAO;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionnaireActivity extends AppCompatActivity
        implements Button.OnClickListener {
    String CLASS_TAG = "QuestionnaireActivity";

    Spinner spinnerEducation, spinnerNationality, spinnerIncome, spinnerFamily;
    Spinner spinnerChildren, spinnerAuto, spinnerMoto, spinnerPCGames, spinnerBank;
    Spinner spinnerCinema, spinnerAlco, spinnerTabaco, spinnerVegetarian, spinnerFitness;
    Button buttonSend;

    String[] education = {"*не выбрано*", "Среднее", "Средне-профессиональное", "Высшее"};
    String[] nationality = {"*не выбрано*", "Русский/русская","Украинец/украинка","Узбек/узбечка","Белорус/ белоруска",
            "Грузин/грузинка","Литовец/литовка","Таджик/таджичка","Чуваш/чувашка","Киргиз/киргизка",
            "Дагестанец/дагестанка","Башкир/башкирка","Эстонец/эстонка","Чеченец/чеченка","Осетин/осетинка",
            "Татарин/татарка","Казах/казачка","Азербайджанец/азербайджанка","Армянин/армянка","Молдаванин/молдаванка",
            "Еврей/еврейка","Немец/немка","Туркмен/туркменка","Латыш/латышка","Мордвин/мордовка",
            "Поляк/полячка","Удмурт/удмуртка","Мариец/марийка","Коми","Кореец/кореянка", "Грек/гречанка",
            "Якут/якутка","Каракалпак/каракалпачка","Ингуш/ингушка","Болгарин/болгарка","Бурят/бурятка",
            "Кабардинец /кабардинка","Цыган/цыганка","Гагауз/гагаузка","Карел/карелка","Калмык/калмычка",
            "Тувинец/тувинка","Адыгеец/адыгейка","Финн/финка","Хакас/хакаска","Черкес/черкеска",
            "Карачаевец/карачаевка","Курд/курдка","Абхазец/абхазка","Балкарец/балкарка",
            "Народности Севера, Сибири и Дальнего Востока"};
    String[] income = {"*не выбрано*", "0-10000", "11000-20000", "21000-30000", "31000-40000", "41000-50000",
            "51000-60000", "61000-70000", "71000-80000", "81000-90000", "91000-100000", "Свыше 100000"};
    String[] family = {"*не выбрано*", "Состою в браке", "Не соcтою в браке"};
    String[] yesNo = {"*не выбрано*", "нет", "да"};
    String[] alcoTabaco = {"*не выбрано*", "Положительное", "Отрицательное", "Нейтральное"};

    SharedPreferencesManager spManager;
    Questionnaire questionnaire;
    QuestionnaireService service;

    UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        initLayoutFields();
        serializeUserRead();

        spManager = new SharedPreferencesManager(this);
        questionnaire = new Questionnaire(user.getUser().getId());
    }

    private void initLayoutFields() {
        spinnerEducation = findViewById(R.id.spinnerEducation);
        setSpinnerAdapter(education, spinnerEducation);
        spinnerEducation.setPrompt("Образование:");
        spinnerEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setEducation(education[pos]);
                else
                    questionnaire.setEducation("");
                Log.i(CLASS_TAG, "Образование: " + questionnaire.getEducation());
            }
        });


        spinnerNationality = findViewById(R.id.spinnerNationality);
        setSpinnerAdapter(nationality, spinnerNationality);
        spinnerNationality.setPrompt("Национальность:");
        spinnerNationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setNationality(nationality[pos]);
                else
                    questionnaire.setNationality("");
                Log.i(CLASS_TAG, "Национальность: " + questionnaire.getNationality());
            }
        });

        spinnerIncome = findViewById(R.id.spinnerIncome);
        setSpinnerAdapter(income, spinnerIncome);
        spinnerIncome.setPrompt("Доход:");
        spinnerIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setIncome(income[pos]);
                else
                    questionnaire.setIncome("");
                Log.i(CLASS_TAG, "Доход: " + questionnaire.getIncome());
            }
        });

        spinnerFamily = findViewById(R.id.spinnerFamily);
        setSpinnerAdapter(family, spinnerFamily);
        spinnerFamily.setPrompt("Семейное положение:");
        spinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setFamily(family[pos]);
                else
                    questionnaire.setFamily("");
                Log.i(CLASS_TAG, "Семейное положение: " + questionnaire.getFamily());
            }
        });

        spinnerChildren = findViewById(R.id.spinnerChildren);
        setSpinnerAdapter(yesNo, spinnerChildren);
        spinnerChildren.setPrompt("Есть дети?");
        spinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setChildren(pos - 1);
                else
                    questionnaire.setChildren(-1);
                Log.i(CLASS_TAG, "Есть дети: " + questionnaire.getChildren());
            }
        });

        spinnerAuto = findViewById(R.id.spinnerAuto);
        setSpinnerAdapter(yesNo, spinnerAuto);
        spinnerAuto.setPrompt("Есть втомобиль?");
        spinnerAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setCar(pos - 1);
                else
                    questionnaire.setCar(-1);
                Log.i(CLASS_TAG, "Есть втомобиль: " + questionnaire.getCar());
            }
        });

        spinnerMoto = findViewById(R.id.spinnerMoto);
        setSpinnerAdapter(yesNo, spinnerMoto);
        spinnerMoto.setPrompt("Есть мотоцикл?");
        spinnerMoto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setMotorcycle(pos - 1);
                else
                    questionnaire.setMotorcycle(-1);
                Log.i(CLASS_TAG, "Есть мотоцикл: " + questionnaire.getMotorcycle());
            }
        });

        spinnerPCGames = findViewById(R.id.spinnerPCGames);
        setSpinnerAdapter(yesNo, spinnerPCGames);
        spinnerPCGames.setPrompt("Играете в компьютерные игры?");
        spinnerPCGames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setComputer_games(pos - 1);
                else
                    questionnaire.setComputer_games(-1);
                Log.i(CLASS_TAG, "Играете в компьютерные игры: " + questionnaire.getComputer_games());
            }
        });

        spinnerBank = findViewById(R.id.spinnerBank);
        setSpinnerAdapter(yesNo, spinnerBank);
        spinnerBank.setPrompt("Есть ли банковские карты?");
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setBanks(pos - 1);
                else
                    questionnaire.setBanks(-1);
                Log.i(CLASS_TAG, "Есть ли банковские карты: " + questionnaire.getBanks());
            }
        });

        spinnerCinema = findViewById(R.id.spinnerCinema);
        setSpinnerAdapter(yesNo, spinnerCinema);
        spinnerCinema.setPrompt("Ходите в кино?");
        spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setCinema(pos - 1);
                else
                    questionnaire.setCinema(-1);
                Log.i(CLASS_TAG, "Ходите в кино: " + questionnaire.getCinema());
            }
        });

        spinnerAlco = findViewById(R.id.spinnerAlco);
        setSpinnerAdapter(alcoTabaco, spinnerAlco);
        spinnerAlco.setPrompt("Как относитесь к алкоголю?");
        spinnerAlco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setAlcohol(alcoTabaco[pos]);
                else
                    questionnaire.setAlcohol("");
                Log.i(CLASS_TAG, "Как относитесь к алкоголю: " + questionnaire.getAlcohol());
            }
        });

        spinnerTabaco = findViewById(R.id.spinnerTabaco);
        setSpinnerAdapter(alcoTabaco, spinnerTabaco);
        spinnerTabaco.setPrompt("Как относитесь к табаку?");
        spinnerTabaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setTobacco(alcoTabaco[pos]);
                else
                    questionnaire.setTobacco("");
                Log.i(CLASS_TAG, "Как относитесь к табаку: " + questionnaire.getTobacco());
            }
        });

        spinnerVegetarian = findViewById(R.id.spinnerVegetarian);
        setSpinnerAdapter(yesNo, spinnerVegetarian);
        spinnerVegetarian.setPrompt("Вы вегетарианец?");
        spinnerVegetarian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setVegeterian(pos - 1);
                else
                    questionnaire.setVegeterian(-1);
                Log.i(CLASS_TAG, "Вы вегетарианец: " + questionnaire.getVegeterian());
            }
        });

        spinnerFitness = findViewById(R.id.spinnerFitness);
        setSpinnerAdapter(yesNo, spinnerFitness);
        spinnerFitness.setPrompt("Посещаете фитнесс?");
        spinnerFitness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos!= 0)
                    questionnaire.setSport(pos - 1);
                else
                    questionnaire.setSport(-1);
                Log.i(CLASS_TAG, "Посещаете фитнесс: " + questionnaire.getSport());
            }
        });

        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
    }
    private boolean hasInternetConnection(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }
    // первый item в списке приглушенного цвета
    private void setSpinnerAdapter(String[] answers, Spinner spinner) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, answers) {
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.parseColor("#949494"));
                        } else
                            tv.setTextColor(Color.parseColor("#000000"));
                        return view;
                    }
                };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.buttonSend){
            if (!questionnaire.isEmpty()){
                boolean wasSent = sendQuestionnaire();
                if (wasSent) {
                    showHint("Спасибо, что заполнили анкету! Теперь за каждый просмотр вы получите на 20% больше!");
//                    startActivity(new Intent(this, MenuActivity.class));
//                    this.finish();
                } else {
                    showHint("Какая-то ошибка :(\nПопробуйте позже, нам важны Ваши ответы.");
                }
                // TODO: 17.02.2019 Get current balance
                // TODO: 17.02.2019 Send questionnaire to server

            } else {
                showHint("Вы ответили не на все вопросы анкеты.");
                Log.i(CLASS_TAG, questionnaire.toString());
            }
        }
    }

    private void updateUserCoefficient() {
        int user_coeff = 0;
        //  0   1
        //  нет  да
        user_coeff += questionnaire.getChildren();
        user_coeff *= 2;
        user_coeff += questionnaire.getCar();
        user_coeff *= 2;
        user_coeff += questionnaire.getMotorcycle();
        user_coeff *= 2;
        user_coeff += questionnaire.getComputer_games();
        user_coeff *= 2;
        user_coeff += questionnaire.getBanks();
        user_coeff *= 2;
        user_coeff += questionnaire.getCinema();
        user_coeff *= 2;
        user_coeff += questionnaire.getAlcohol().equals("Отрицательное") ? 0 : 1;
        user_coeff *= 2;
        user_coeff += questionnaire.getAlcohol().equals("Отрицательное") ? 0 : 1;
        user_coeff *= 2;
        user_coeff += questionnaire.getVegeterian();
        user_coeff *= 2;
        user_coeff += questionnaire.getSport();

        user.getUser().setUser_coeff(user_coeff);
        Log.i(CLASS_TAG, "user_coeff = " + user_coeff);
    }
    private void updateUserLocationCoefficient() {
        Districts d = new Districts();
        float locationCoeff = d.getLocationCoefficient(user.getUser().getLocation());
        Log.i(CLASS_TAG, "locationCoeff = " + locationCoeff);
        user.getUser().setLocation_coeff(locationCoeff);
    }
    private void updateCurrentUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);
        try {
            service.updateUserInfo(user.getUser(), user.getUser().getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean sendQuestionnaire() {
        boolean isSuccessful = false;

        if(hasInternetConnection()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://92.53.65.46:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(QuestionnaireService.class);
            Log.i(CLASS_TAG, questionnaire.toString());

            try {
                int id = user.getUser().getId();
                QuestionnaireInfo q = new QuestionnaireInfo(questionnaire);
                Log.i(CLASS_TAG, q.toString());
                isSuccessful = service.sendUser(new QuestionnaireInfo(questionnaire)).execute().isSuccessful();
                Log.i(CLASS_TAG, "Request.isSuccessful = " + isSuccessful);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showHint("Проверьте подключение интернета.");
        }

        return isSuccessful;
    }
    private void serializeUserRead() {
        SerializeObject<UserInfo> so = new SerializeObject<UserInfo>(this);
        user = new UserInfo();
        try {
            user = so.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.getStackTrace();
        }
        if (user != null)
            Log.i(CLASS_TAG, user.toString());
    }
    private void serializeUserWrite() {
        try {
            SerializeObject so = new SerializeObject(this);

            so.writeObject(user, user.getClass());
            spManager.setQuestionnaire(user.getUser().getUser_coeff() != 0);

            Log.i(CLASS_TAG, user.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
    void showHint(String hintText) {
        View currentView = findViewById(R.id.questionnaire_linear_layout);
        final Snackbar snackbar = Snackbar.make(currentView, hintText, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView snackBarTextView = snackbarView.findViewById(R.id.snackbar_text);
        snackBarTextView.setSingleLine(false);
        snackbar.setAction("Понятно", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionnaire.isEmpty())
                    updateAppData();
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void updateAppData() {
        spManager.setQuestionnaire(true);
        Log.i(CLASS_TAG, "SEND");
        updateUserCoefficient();
        updateUserLocationCoefficient();
        updateCurrentUser();
        serializeUserWrite();
        VideoDAO videoDAO = new VideoDAO(this);
        videoDAO.deleteAll();
        onBackPressed();
    }

}
