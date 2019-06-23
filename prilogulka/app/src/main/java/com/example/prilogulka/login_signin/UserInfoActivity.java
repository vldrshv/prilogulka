package com.example.prilogulka.login_signin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prilogulka.R;
import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.android.interraction.DatePicker;
import com.example.prilogulka.data.managers.CoefficientManager;
import com.example.prilogulka.data.managers.GeofenceManager;
import com.example.prilogulka.data.managers.SharedPreferencesManager;
import com.example.prilogulka.data.service.UserService;
import com.example.prilogulka.data.userData.Auth;
import com.example.prilogulka.data.userData.User;
import com.example.prilogulka.data.userData.UserInfo;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoActivity extends AppCompatActivity
        implements LocationListener {


    String CLASS_TAG = "UserInfoActivity";
    private LocationManager locationManager;

    private String city = "";
    float coefficient = 1;
    float location_coeff = 0;

    String date = "";
    String email;

    // UI references
    EditText editEmail, editName, editSurname, editBirthday;
    TextInputLayout emailInputLayout;
    TextView cityTextView;
    RadioGroup sexGroup;
    CheckBox checkBoxAgreement;

    Button buttonNext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initLayoutFields();
//        locationService = new LocationService( this, this);
        checkLocationPermission();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initLayoutFields() {
        buttonNext = findViewById(R.id.buttonNext);

        editEmail = findViewById(R.id.email);
        emailInputLayout = findViewById(R.id.email_text_input_layout);

        editName = findViewById(R.id.name);
        editSurname = findViewById(R.id.surname);
        editBirthday = findViewById(R.id.birthday);
        sexGroup = findViewById(R.id.radioGroupSex);
        checkBoxAgreement = findViewById(R.id.checkBoxAgreement);

        cityTextView = findViewById(R.id.cityTextView);
    }

    public void chooseData(View v) {
        DialogFragment dateDialog = new DatePicker();
        dateDialog.show(getSupportFragmentManager(), "datePicker");
    }
    public void getAgreement(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://docs.google.com/document/d/1KKo87rqTeV9zyCVtwLNPs0xowXIaDAymqxEQqVmfij0/edit?usp=sharing"));
        startActivity(browserIntent);
    }

    public void finishRegistration(View v) {
        resetEmailErrors();

        if (!hasInternetConnection()) {
            showHint("Проверьте подключение к интернету.");
            return;
        }

        if (hasEmptyField()) {
            showHint(getString(R.string.has_empty_fields));
            return;
        }

        email = editEmail.getText().toString();
        if (isEmailValid()) {
            emailInputLayout.setErrorEnabled(false);
            if (isAdult()) {
                saveInfo();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
        else {
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError("Можно использовать латинские буквы (a-z), цифры и точку.");
        }

    }
    private boolean isAdult() {
        return Time.isAdult(editBirthday.getText().toString());
    }
    private void saveInfo() {
        saveAppContext();
        User user = fillNewUserInfo();
        postUserToServer(user);
    }
    private void saveAppContext() {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        email = editEmail.getText().toString();
        spManager.setActiveUser(email);
        makeCoefficients();
        spManager.setWPCoefficient(coefficient);
        spManager.setQuestionnaire(false);
        Log.i(CLASS_TAG, coefficient + "");
    }
    private User fillNewUserInfo() {
        User user = new User();
        user.setName(editName.getText().toString());
        user.setLastname(editSurname.getText().toString());
        user.setBirthday(editBirthday.getText().toString());
        user.setEmail(email);
        user.setLocation(city);
        user.setSex(sexGroup.getCheckedRadioButtonId() == R.id.radioButtonMale ? 0 : 1);
        user.setCurrent_balance(0);
        user.setCurrent_video_coeff(coefficient);
        user.setLocation_coeff(location_coeff);
        user.setUser_coeff(0);

        return user;
    }
    private void postUserToServer(User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.53.65.46:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        try {
            Auth userResponse = getUserFromServer(service);

            if (userResponse == null) {
                sendUserToServer(user, service);
            } else {
                Toast.makeText(this,
                        "Такой email уже зарегистрирован. Воспользуйтесь кнопкой НЕ МОГУ ВОЙТИ",
                        Toast.LENGTH_LONG).show();
                new SharedPreferencesManager(this).setActiveUser(email);
            }
        } catch (IOException e) {
            Toast.makeText(this, "network failure :( ", Toast.LENGTH_SHORT).show();
        }
    }
    private void makeCoefficients() {
        TextView tv = findViewById(R.id.birthday);
        date = tv.getText().toString();
        CoefficientManager cm = new CoefficientManager();

        double district_coef = cm.getDistrictCoefficient(city);
        location_coeff = cm.getLocationCoefficient(city);

        System.out.println("district_coef = " + district_coef);
        double age_coef = cm.getAgeCoefficient(date);
        System.out.println("age_coef = " + age_coef);

        coefficient = (float) (district_coef * age_coef);
    }

    private Auth getUserFromServer(UserService service) throws IOException {
        Call<UserInfo> call = service.getUserByEmail(email);
        Response<UserInfo> response = call.execute();
        UserInfo userInfo = null;
        if (response.isSuccessful())
            userInfo = response.body();

        return userInfo == null ? null : userInfo.getUser();
    }
    private void sendUserToServer(User user, UserService service) throws IOException {
        Call<UserInfo> call = service.sendUser(user);
        Response<UserInfo> response = call.execute();

        if (response.isSuccessful())
            Toast.makeText(this,
                    "Вы успешно зарегистрировались.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,
                    "Что-то не так. Повторите попвтку позже.", Toast.LENGTH_SHORT).show();

    }
    public boolean hasInternetConnection() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }

    private boolean hasEmptyField() {
        Log.i(CLASS_TAG, "" + sexGroup.getCheckedRadioButtonId());
        return isEmpty(editName) || isEmpty(editSurname) || isEmpty(editBirthday) ||
                sexGroup.getCheckedRadioButtonId() == -1 || !checkBoxAgreement.isChecked();
    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().equals("");
    }
    private boolean isEmailValid() {
        boolean noErrors = true;

        if (email.isEmpty()) {
            emailInputLayout.setError(getString(R.string.error_field_required));
            noErrors = false;
        } else if (!isEmailCorrect(email)) {
            emailInputLayout.setError(getString(R.string.error_invalid_email));
            noErrors = false;
        }

        if (!noErrors) {
            emailInputLayout.setErrorEnabled(true);
            showHint("Какая-то ошибка при входе регистрации Вашей учетной записи. Проверьте данные.");
        }

        return noErrors;
    }
    private boolean isEmailCorrect(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void resetEmailErrors() {
        emailInputLayout.setErrorEnabled(false);
    }
    void showHint(String hintText) {
        final Snackbar snackbar = Snackbar.make(getCurrentFocus(), hintText, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView snackBarTextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setSingleLine(false);
        snackbar.setAction("Понятно", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    /**
     * ========================================================================================
     * LOCATION METHODS
     * ========================================================================================
     */

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

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

    private void getLocation() {
        Log.i(CLASS_TAG, "HANDLE");
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {

                    final Snackbar snackbar = Snackbar.make(
                            getCurrentFocus(),
                            "Мы используем Геопозицию для определения коэффициентов при просмотре видео. Пожалуйста, разрешите геолокацию в настроках для перерасчета коэффициента.",
                            Snackbar.LENGTH_INDEFINITE);
                    View snackbarView = snackbar.getView();
                    TextView snackBarTextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
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
        float lat = (float) location.getLatitude();
        float longt = (float) location.getLongitude();

        Log.i(CLASS_TAG, "Latitude:" + lat + ", Longitude:" + longt);

        if (city.equals("")) {
            city = new GeofenceManager(lat, longt).getCity();
        }
        cityTextView.setText(city);

        Log.i(CLASS_TAG, city);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}