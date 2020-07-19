package com.example.enamul.qrcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enamul.model.InforUser;
import com.example.enamul.model.Information;
import com.example.enamul.serviceClient.APIClient;
import com.example.enamul.serviceClient.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Profile extends AppCompatActivity {
    String accessToken;
    TextView nameUser,tv_email,tv_money,tv_address,tv_phone;
    EditText sendMoney;
    Button btnSendMoney,btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameUser = (TextView) findViewById(R.id.nameUser);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        sendMoney =(EditText) findViewById(R.id.sendMoney);
        btnSendMoney =(Button) findViewById(R.id.btnSendMoney);
        btnSend =(Button) findViewById(R.id.btnSend);
        sendMoney.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        Call<Information> calltargetResponse = client.getInfomation("Bearer "+accessToken);
        calltargetResponse.enqueue(new Callback<Information>() {
            @Override
            public void onResponse(Call<Information> call, retrofit2.Response<Information> response) {
                Information infor = response.body();
                nameUser.setText(infor.getUsername());
                tv_email.setText("Email: "+ infor.getEmail());
                tv_money.setText("Tài khoản: "+infor.getMoney()+" đồng");
                tv_address.setText("Địa chỉ: "+infor.getAddress());
                tv_phone.setText("Số địa thoại: "+infor.getPhone());

            }
            @Override
            public void onFailure(Call<Information> call, Throwable t) {
                //Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void Send(View view){
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        Call<Boolean> calltargetResponse = client.sendMoney("Bearer "+accessToken,sendMoney.getText().toString());
        calltargetResponse.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean check = response.body();
                if(check){
                    Toast.makeText(Profile.this, "Nộp tiền thành công ", Toast.LENGTH_SHORT).show();
                    sendMoney.setText("");
                    sendMoney.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                    btnSendMoney.setVisibility(View.VISIBLE);
                    finish();
                    startActivity(getIntent());
                }
                else {
                    Toast.makeText(Profile.this, "Số thẻ cào bạn vừa nhập không chính xác. Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
                    sendMoney.setText("");
                    sendMoney.setVisibility(View.GONE);
                    btnSend.setVisibility(View.GONE);
                    btnSendMoney.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(Profile.this, "Nộp tiền thất bại ", Toast.LENGTH_SHORT).show();
                sendMoney.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
                btnSendMoney.setVisibility(View.VISIBLE);
            }
        });
    }
    public void SendMoney(View view){
        sendMoney.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        btnSendMoney.setVisibility(View.GONE);
    }
}