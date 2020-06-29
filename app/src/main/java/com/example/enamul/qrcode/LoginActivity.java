package com.example.enamul.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enamul.model.InforUser;
import com.example.enamul.model.ResponseLogin;
import com.example.enamul.model.User;
import com.example.enamul.serviceClient.APIClient;
import com.example.enamul.serviceClient.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    public EditText nameText, passText;
    public String name, password, fullResponse, username, userPassword;
    RelativeLayout relativeLayout, signUPLayout, loginLayout;
    public TextView responseText;
    public Button buttonSign, buttonLog;

    public static final String MY_PREFRENCE = "myPrefs";
    public static final String TOKEN = "myToken";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameText = findViewById(R.id.name);
        passText = findViewById(R.id.password);
        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        responseText = findViewById(R.id.response);;
        buttonSign = findViewById(R.id.button);
        buttonLog = findViewById(R.id.log);

    }

    private void makeString() {
        name = nameText.getText().toString();
        password = passText.getText().toString();
    }


    private void login()  {
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        User u = new User("tri@gmail.com","admin");
        Call<ResponseLogin> call = client.login(u);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if (response.isSuccessful()) {

                    ResponseLogin data = response.body();
                    String userToken = data.getAccessToken();
                    if (responseText.getVisibility() == View.INVISIBLE) {
                        responseText.setVisibility(View.VISIBLE);
                        responseText.setText(userToken);
                    } else {
                        responseText.setText(userToken);
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFRENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TOKEN, userToken);
                    editor.apply();
                    openGetActivity(data.getAccessToken());
                } else if(response.code() == 400) {
                    Toast.makeText(LoginActivity.this, "Invalid username or password",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.d("TAG", "error poor internet connection");

            }
        });
    }

    public void openLogin(View view) {
        if (signUPLayout.getVisibility() == View.VISIBLE) {
            signUPLayout.setVisibility(View.GONE);
            buttonSign.setVisibility(View.GONE);
            buttonLog.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.VISIBLE);

        }
    }

    public void openSignUp(View view) {

    }

    public void loginUser(View view) {
        makeUserString();
        login();

    }

    private void openGetActivity(String accessToken) {

        Intent openIntent = new Intent(this, MainActivity.class);
        openIntent.putExtra("accessToken", accessToken);
        startActivity(openIntent);
    }

    private void makeUserString() {
        username = nameText.getText().toString();
        userPassword = passText.getText().toString();

    }


}