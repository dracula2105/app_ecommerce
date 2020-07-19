package com.example.enamul.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.enamul.model.InforUser;
import com.example.enamul.model.ModelQrcode;
import com.example.enamul.serviceClient.APIClient;
import com.example.enamul.serviceClient.UserClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnInfo;
    Button btnScan,btnOrder,btnOrderCheck;
    EditText editText;
    String EditTextValue ;
    Thread thread ;
    public final static int QRcodeWidth = 350 ;
    Bitmap bitmap ;
    String accessToken;
    TextView tv_qr_username,tv_qr_totalPrice,tv_qr_bank,tv_qr_product,tv_qr_timeOrder,tv_idOder;
    TextView txt_name;
    Long id_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        btnInfo = (Button)findViewById(R.id.btnInfo);
        btnScan = (Button)findViewById(R.id.btnScan);
        btnOrder = (Button)findViewById(R.id.btnOrder);
        btnOrderCheck = (Button)findViewById(R.id.btnOrderCheck);
        tv_idOder = (TextView) findViewById(R.id.tv_idOder);
        tv_qr_username = (TextView) findViewById(R.id.tv_qr_username);
        tv_qr_totalPrice = (TextView) findViewById(R.id.tv_qr_totalPrice);
        tv_qr_bank = (TextView) findViewById(R.id.tv_qr_bank);
        tv_qr_product = (TextView) findViewById(R.id.tv_qr_product);
        tv_qr_timeOrder = (TextView) findViewById(R.id.tv_qr_timeOrder);
        txt_name = (TextView) findViewById(R.id.txtName);
        tv_idOder.setVisibility(View.GONE);
        tv_qr_username.setVisibility(View.GONE);
        tv_qr_totalPrice.setVisibility(View.GONE);
        tv_qr_bank.setVisibility(View.GONE);
        tv_qr_product.setVisibility(View.GONE);
        tv_qr_timeOrder.setVisibility(View.GONE);
        txt_name.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
        btnOrderCheck.setVisibility(View.GONE);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        Call<InforUser> calltargetResponse = client.getUsers("Bearer "+accessToken);
        calltargetResponse.enqueue(new Callback<InforUser>() {
            @Override
            public void onResponse(Call<InforUser> call, retrofit2.Response<InforUser> response) {
                InforUser infor = response.body();
                txt_name.setText("Hello " +infor.getUsername());

            }
            @Override
            public void onFailure(Call<InforUser> call, Throwable t) {
                //Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show();
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });
    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");
                getInforQrcode(result.getContents().toString());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void getInforQrcode(String code){
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        Call<ModelQrcode> calltargetResponse = client.getOrder("Bearer "+accessToken, code);
        calltargetResponse.enqueue(new Callback<ModelQrcode>() {
            @Override
            public void onResponse(Call<ModelQrcode> call, retrofit2.Response<ModelQrcode> response) {
                ModelQrcode modelQrcode = response.body();
                tv_qr_username.setText(modelQrcode.getUsername());
                String product = "";
                if(modelQrcode.getProduct().size()==1){
                    tv_qr_product.setText("Sản phẩm: "+modelQrcode.getProduct().get(0));
                }
                else {
                    for(String i : modelQrcode.getProduct()){
                        product+=i +" & ";
                    }
                    tv_qr_product.setText("Sản phẩm: "+product);
                }
                String timeOrder= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(modelQrcode.getTimeOrder());
                tv_qr_timeOrder.setText("Ngày đặt hàng: "+timeOrder);
                tv_qr_totalPrice.setText("Số tiền: "+modelQrcode.getTotalPrice().toString()+" đồng");
                id_order = modelQrcode.getIdOrder();
                tv_idOder.setVisibility(View.VISIBLE);
                tv_qr_username.setVisibility(View.VISIBLE);
                tv_qr_totalPrice.setVisibility(View.VISIBLE);
                tv_qr_product.setVisibility(View.VISIBLE);
                tv_qr_timeOrder.setVisibility(View.VISIBLE);
                txt_name.setVisibility(View.VISIBLE);
                if(modelQrcode.isBank() ==false){
                    btnOrder.setVisibility(View.VISIBLE);
                }
                else {
                    btnOrderCheck.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(Call<ModelQrcode> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Đã xảy ra lỗi.. Vui lòng kiểm tra lại. ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onProfile(View view) {
       Intent intent = new Intent(this, Profile.class);
       intent.putExtra("accessToken", accessToken);
       startActivity(intent);

    }
    public void getBank(View view){
        Retrofit retrofit = APIClient.getClient();
        UserClient client = retrofit.create(UserClient.class);
        Call<Boolean> calltargetResponse = client.getBank("Bearer "+accessToken, id_order);
        calltargetResponse.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {

                Toast.makeText(MainActivity.this, "Đơn hàng đã được thanh toán thành công. Vui lòng check mail để xem chi tiết.",
                        Toast.LENGTH_LONG).show();
                btnOrder.setVisibility(View.GONE);
                btnOrderCheck.setVisibility(View.VISIBLE);

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Tài khoản của bạn không đủ để thanh toán đơn hàng. Vui lòng nạp thêm tiền vào tài khoản.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
