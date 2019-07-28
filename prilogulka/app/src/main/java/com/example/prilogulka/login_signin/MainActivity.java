package com.example.prilogulka.login_signin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prilogulka.R;
import com.example.prilogulka.data.managers.CoefficientManager;
import com.example.prilogulka.data.managers.GeofenceManager;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.SerializeObject;
import com.example.prilogulka.data.userData.UserInfo;
import com.example.prilogulka.menu.MenuActivity;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements Button.OnClickListener, LocationListener {

    String CLASS_TAG = "MainActivity";

    /*  Layouts для отображения пароля и данных входа   */
    LinearLayout cannotEnterLL, pinLL, keyboardLL, buttonsLL, passwordLL;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
    Button btnCannotEnter, btnRegister, btnRepeat, btnResendPin;
    ImageButton btnDelete;
    TextInputLayout emailInput;
    EditText editEmail;
    EditText editText1, editText2, editText3, editText4, editText5;

    int position = 0;   // позиция editText# (позиция цифры пароля)
    String email = "";  // для отправки сообщения на сервер и получения юзера
    int userId = 0;     // для дальнейшего общения с сервером
    double location_coeff = 0;
    double district_coef = 0;
    String city = "";

    /*  Сервисы общения с сервером и локацией   */
    private UserService service;
    private LocationManager locationManager;
    private float lat = 0f, longt = 0f;
    private UserInfo user;

    /*  SP, куда сохраняются данные пользователя    */
    private SharedPreferencesManager spManager;

    private enum STATE {
        STATE_LOGIN, STATE_RESEND
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initServices();

        initLayoutFields();
        setListeners();

        showHint("Мы обновляем Ваше местоположение, чтобы Вы получили актуальный" +
                " коэффициент. Еще чуть-чуть и можно будет ввести пин-код.");
        checkLocationPermission(); // чтобы перерасчитать коэф и локацию
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imgButtonDelete:
                deleteText();
                break;
            case R.id.buttonRegister:
                Toast.makeText(this, "РЕГИСТРАЦИЯ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserInfoActivity.class));
                this.finish();
                break;
            case R.id.buttonCannotEnter:
                Toast.makeText(this, "НЕ МОГУ ВОЙТИ :-(", Toast.LENGTH_SHORT).show();
                switchLayouts(STATE.STATE_RESEND);
                break;
            case R.id.buttonRepeat:
                Toast.makeText(this, "Повторяем вход", Toast.LENGTH_SHORT).show();
                executeCheckingTask();
                break;
            case R.id.buttonResendPin:
                resendPin();
                break;
            default:
                printText(v.getId());
                break;
        }
    }

    private void initServices() {
        spManager = new SharedPreferencesManager(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(UserService.class);
    }
    private void initLayoutFields() {
        // поле если забыл пароль
        cannotEnterLL = findViewById(R.id.cannotEnter_linear_layout);
        cannotEnterLL.setVisibility(View.GONE);

        btnResendPin = findViewById(R.id.buttonResendPin);
        emailInput = findViewById(R.id.email_text_input_layout);
        editEmail =  findViewById(R.id.email);

        // поле отображение пин-кода
        pinLL = findViewById(R.id.pin_linear_layout);
        pinLL.setVisibility(View.INVISIBLE);

        editText1 = findViewById(R.id.editText1);
        makeEditTextUneditable(editText1);
        editText2 = findViewById(R.id.editText2);
        makeEditTextUneditable(editText2);
        editText3 = findViewById(R.id.editText3);
        makeEditTextUneditable(editText3);
        editText4 = findViewById(R.id.editText4);
        makeEditTextUneditable(editText4);
        editText5 = findViewById(R.id.editText5);
        makeEditTextUneditable(editText5);
        // поле отображения клавиатуры
        keyboardLL = findViewById(R.id.keyboard_linear_layout);
        btn0 = findViewById(R.id.button0);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);
        btnDelete = findViewById(R.id.imgButtonDelete);
        setButtonsCanBePressed(false);
        // поле отображения кнопок взаимодействия
        buttonsLL = findViewById(R.id.buttons_linear_layout);
        btnCannotEnter = findViewById(R.id.buttonCannotEnter);
        btnRegister = findViewById(R.id.buttonRegister);
        btnRepeat = findViewById(R.id.buttonRepeat);
        btnRepeat.setVisibility(View.GONE);
    }

    private void setButtonsCanBePressed(boolean isEnabled) {
        pinLL.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        btn0.setEnabled(isEnabled);
        btn1.setEnabled(isEnabled);
        btn2.setEnabled(isEnabled);
        btn3.setEnabled(isEnabled);
        btn4.setEnabled(isEnabled);
        btn5.setEnabled(isEnabled);
        btn6.setEnabled(isEnabled);
        btn7.setEnabled(isEnabled);
        btn8.setEnabled(isEnabled);
        btn9.setEnabled(isEnabled);
        btnDelete.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
    }

    private void switchLayouts(STATE state) {
        if (state.equals(STATE.STATE_RESEND)) {
            pinLL.setVisibility(View.GONE);
            keyboardLL.setVisibility(View.GONE);
            buttonsLL.setVisibility(View.GONE);
            cannotEnterLL.setVisibility(View.VISIBLE);
            cannotEnterLL.setFocusable(true);
            uploadEmail();
        } else {
            if (isGPSUpdated()) {
                setButtonsCanBePressed(true);
            }
            keyboardLL.setVisibility(View.VISIBLE);
            buttonsLL.setVisibility(View.VISIBLE);
            cannotEnterLL.setVisibility(View.GONE);
        }
    }
    private void makeEditTextUneditable(EditText editText){
        editText.setClickable(false);
        editText.setLongClickable(false);
        editText.setFocusable(false);
        editText.clearFocus();
    }
    private void setListeners() {
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnCannotEnter.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnResendPin.setOnClickListener(this);
    }

    // onClick methods
    private void setText(String text) {
        switch (position){
            case 1:
                editText1.setText(text);
                break;
            case 2:
                editText2.setText(text);
                break;
            case 3:
                editText3.setText(text);
                break;
            case 4:
                editText4.setText(text);
                break;
            case 5:
                editText5.setText(text);
                break;
        }
    }
    public void resendPin() {
        if (hasInternetConnection()) {
            if (isEmailValid()) {
                saveEmail();
                resetEmailErrors();
                switchLayouts(STATE.STATE_LOGIN);
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    try {
                        service.resendPasswordTo(email).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                showHint("Вам на почту направлено письмо с паролем!");
                hideKeyboard(this);
            }
        } else {
            showHint("Проверьте соединение с интернетом.");
        }
    }

    // editText methods
    private void printText(int id){
        if (position < 5) {
            position++;
            Button btn = findViewById(id);
            setText(btn.getText().toString());
        }
        if (position == 5) {
            btnRegister.setVisibility(View.GONE);
            btnCannotEnter.setVisibility(View.GONE);
            btnRepeat.setVisibility(View.VISIBLE);
            executeCheckingTask();
        }

    }
    private void clearText() {
        for (int i = 5; i > 0; i --)
            deleteText();
    }
    private void deleteText() {
        setText("");
        if (position == 5){

            btnRegister.setVisibility(View.VISIBLE);
            btnCannotEnter.setVisibility(View.VISIBLE);
            btnRepeat.setVisibility(View.INVISIBLE);
        }
        if (position > 0)
            position --;

    }

    // email methods
    private void saveEmail() {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        spManager.setActiveUser(email);
    }
    private void uploadEmail() {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        email = spManager.getActiveUser();
        if (email != null && !email.equals("")){
            editEmail.setText(email);
        }
    }
    private boolean isEmailValid(){
        boolean noErrors = true;

        email = editEmail.getText().toString();

        if (email.isEmpty()) {
            emailInput.setError(getString(R.string.error_field_required));
            noErrors = false;
        } else if (!isEmailCorrect(email)) {
            emailInput.setError(getString(R.string.error_invalid_email));
            noErrors = false;
        }

        if (!noErrors){
            emailInput.setErrorEnabled(true);
            showHint("Какая-то ошибка при входе в вашу учетную запись. Проверьте данные.");
        }

        return noErrors;
    }
    private boolean isEmailCorrect(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void resetEmailErrors(){
        emailInput.setErrorEnabled(false);
    }

    // send info to server
    private void executeCheckingTask() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            CheckInputTask task = new CheckInputTask();
            if (task.doInBackground()) {
                welcomeUser();
            } else {
                if (!hasInternetConnection()){
                    Toast.makeText(this, "Проверьте подключение к интернету",
                            Toast.LENGTH_SHORT).show();
                } else {
                    clearText();
                    Toast.makeText(this, "Пароль не верен", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private float makeCoefficients() {
        CoefficientManager cm = new CoefficientManager();

        city = new GeofenceManager(lat, longt).getCity();
        Log.i(CLASS_TAG, city);
        location_coeff = cm.getLocationCoefficient(city);
        district_coef = cm.getDistrictCoefficient(city);
        System.out.println("district_coef = " + district_coef);

        String date = user.getUser().getBirthday();
        double age_coef = cm.getAgeCoefficient(date);
        System.out.println("age_coef = " + age_coef);

        return (float) (district_coef * age_coef);
    }
    private String getPin() {
        return editText1.getText().toString() + editText2.getText().toString() +
                editText3.getText().toString() + editText4.getText().toString() +
                editText5.getText().toString();
    }
    private String getPinFromServer() {
        uploadEmail();
        Log.i(CLASS_TAG, email);
        if (email.equals("")) {
            showHint("Передите во вкладку *Не могу войти*, чтобы напомнить ваш e-mail");
            return null;
        }

        serializeUser();
        if (user != null && user.getUser() != null)
            return user.getUser().getPin();
        else
            showHint("Какие-то проблемы с сервером, мы работаем над этим");
        return null;

    }
    private void serializeUser() {
        try {
            user = service.getUserByEmail(email).execute().body();
            if (user.getUser() != null) {
                SerializeObject so = new SerializeObject(this);
                Log.i(CLASS_TAG, user.toString());
                so.writeObject(user, user.getClass());
                spManager.setQuestionnaire(user.getUser().getUser_coeff() != 0);

                userId = user.getUser().getId();

                Log.i(CLASS_TAG, user.toString());
            } else {
                showHint("Вы не зарегистрированны. Пожалуйста, пройдите регистрацию!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void welcomeUser() {
        clearText();
        String name = user.getUser().getName();
        Toast.makeText(this, "Здравствуйте, " + name, Toast.LENGTH_SHORT).show();

        float coefficient = makeCoefficients();
        spManager.setWPCoefficient(coefficient);

        user.getUser().setLocation(city);
        user.getUser().setLocation_coeff(location_coeff);
        user.getUser().setCurrent_video_coeff(coefficient);

        try {
            service.updateUserInfo(user.getUser(), userId).execute();
            user = service.getUserById(userId).execute().body();

            serializeUser();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        this.finish();
    }

    private void showHint(String hintText){
        View currentView = findViewById(R.id.mainActivity_linear_layout);
        final Snackbar snackbar = Snackbar.make(currentView, hintText, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView snackBarTextView = snackbarView.findViewById(R.id.snackbar_text);
        snackBarTextView.setSingleLine(false);
        snackbar.setAction("Понятно", new View.OnClickListener(){
            @Override
            public void onClick(View v){
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = findViewById(R.id.mainActivity_linear_layout);
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private boolean hasInternetConnection(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }

    class CheckInputTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... urls) {
            return checkInput();
        }

        protected void onPostExecute(Void feed) {

        }

        private boolean checkInput() {
            String localPin = getPin();
            String serverPin = getPinFromServer();
            Log.i(CLASS_TAG, "local = " + localPin + " -> serverPin = " + serverPin);
            return localPin.equals(serverPin);
        }
    }

    @Override
    public void onBackPressed(){
        if (cannotEnterLL.getVisibility() == View.VISIBLE)
            switchLayouts(STATE.STATE_LOGIN);
    }
    @Override
    public void onDestroy() {
        Log.i(CLASS_TAG, "onDestroy()");
        super.onDestroy();
        destroyLocationManager();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * ========================================================================================
     * LOCATION METHODS
     * ========================================================================================
     */

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private boolean isGPSUpdated() { return lat != 0 && longt != 0; }

    private void getLocation() {
        Log.i(CLASS_TAG, "HANDLE");
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (!isGPSUpdated()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        }
    }
    private void requestGpsPermission() {

        Log.w(CLASS_TAG, "GPS permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
        }

    }

    public void checkLocationPermission() {
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.i(CLASS_TAG, rc + "");
        if (rc == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            requestGpsPermission();
            getLocation();
        }
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (!isGPSUpdated())
                        getLocation();

                } else {

                    final Snackbar snackbar = Snackbar.make(
                            getCurrentFocus(),
                            "Мы используем геопозицию для определения коэффициентов при просмотре видео. Пожалуйста, разрешите геолокацию в настроках для перерасчета коэффициента.",
                            Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    TextView snackBarTextView = snackbarView.findViewById(R.id.snackbar_text);
                    snackBarTextView.setSingleLine(false);
                    snackbar.setAction("Понятно", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (float) location.getLatitude();
        longt = (float) location.getLongitude();

        if (cannotEnterLL.getVisibility() != View.VISIBLE) {
            setButtonsCanBePressed(true);
            Toast.makeText(this, "Можно вводить пароль", Toast.LENGTH_SHORT).show();
        }
        Log.i(CLASS_TAG, "Latitude:" + lat + ", Longitude:" + longt);
        destroyLocationManager();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Latitude", "status");
    }
    /**
     * ========================================================================================
     */
    private void destroyLocationManager() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
        }
    }
}
