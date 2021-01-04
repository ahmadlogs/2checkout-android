package com.example.a2checkoutandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PaymentActivity extends AppCompatActivity {
    private WebView mWebView;

    private final String TWO_CO_sid         = "ENTER_MERCHANT_CODE";
    private final String TWO_CO_SecretWord  = "ENTER_SECRET_WORD";
    private static final String paymentReturnUrl="http://localhost/2checkout/index.php";

    private static String price="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mWebView = (WebView) findViewById(R.id.activity_payment_webview);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setDomStorageEnabled(true);

        Intent intentData = getIntent();
        price = intentData.getStringExtra("price");

        String sid = TWO_CO_sid;
        String mode = "2CO";
        String li_0_name = "invoice123";
        String li_0_price = price;
        String card_holder_name = "John Doe";
        String street_address = "123 Test Address";
        String street_address2 = "Suite 200";
        String city = "Lahore";
        String state = "Punjab";
        String zip = "54000";
        String country = "Pakistan";
        String email = "test@example.com";
        String phone = "03001234567";
        String demo = "Y";

        String postData = "";

        postData += "sid"+ "=" + sid + "&";
        postData += "mode" + "=" + mode + "&";
        postData += "li_0_name" + "=" + li_0_name + "&";
        postData += "li_0_price" + "=" + li_0_price + "&";
        postData += "card_holder_name" + "=" + card_holder_name + "&";
        postData += "street_address" + "=" + street_address + "&";
        postData += "street_address2" + "=" + street_address2 + "&";
        postData += "city" + "=" + city + "&";
        postData += "state" + "=" + state + "&";
        postData += "zip" + "=" + zip + "&";
        postData += "country" + "=" + country + "&";
        postData += "email" + "=" + email + "&";
        postData += "phone" + "=" + phone + "&";
        postData += "demo" + "=" + demo;

        mWebView.postUrl("https://www.2checkout.com/checkout/purchase", postData.getBytes());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            System.out.println("AhmadLogs: onPageStarted - url : " +url);

            String redirect_url = url.substring(0, paymentReturnUrl.length());
            String response = url.substring(paymentReturnUrl.length(), url.length());

            System.out.println("AhmadLogs: onPageStarted - redirect_url : " +redirect_url);
            System.out.println("AhmadLogs: onPageStarted - response : " +response);

            if(redirect_url.equals(paymentReturnUrl)) {
                System.out.println("AhmadLogs: return url cancelling");
                view.stopLoading();

                Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                String[] values = response.split("&");
                for (String pair : values) {
                    String[] nameValue = pair.split("=");
                    if (nameValue.length == 2) {
                        System.out.println("AhmadLogs: Name:" + nameValue[0] + " value:" + nameValue[1]);
                        i.putExtra(nameValue[0], nameValue[1]);
                    }
                }

                setResult(RESULT_OK, i);
                finish();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }
}