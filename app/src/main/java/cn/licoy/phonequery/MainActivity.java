package cn.licoy.phonequery;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import cn.licoy.phonequery.bean.Phone;
import cn.licoy.phonequery.mvp.MainViewMvp;
import cn.licoy.phonequery.mvp.impl.MainPresenter;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MainViewMvp {

    EditText input_phone;
    Button btn_search;
    TextView result_phone;
    TextView result_province;
    TextView result_type;
    TextView result_gs_type;
    MainPresenter mainPresenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_phone = (EditText)findViewById(R.id.input_phone);
        btn_search = (Button)findViewById(R.id.btn_search);
        result_province = (TextView)findViewById(R.id.result_province);
        result_type = (TextView)findViewById(R.id.result_type);
        result_gs_type = (TextView)findViewById(R.id.result_gs_type);
        result_phone = (TextView)findViewById(R.id.result_phone);

        mainPresenter = new MainPresenter(this);
        mainPresenter.onCreate(this);

        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //btn_search.setText("查询中...");
        mainPresenter.search_phone(input_phone.getText().toString());
    }

    @Override
    public void showLoading() {
        if(progressDialog==null){
            progressDialog = ProgressDialog.show(this,"","正在加载",true,false);
        }else if(progressDialog.isShowing()){
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载");
        }
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if(progressDialog!=null&&progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateView(Object o) throws IOException {
        if(o!=null){
            Gson gson = new Gson();
            String s = o.toString();
            s = s.replace("__GetZoneResult_ = ","");
            Phone phone = gson.fromJson(s,Phone.class);
            if(phone!=null){
                result_phone.setText("手机号码："+phone.getTelString());
                result_province.setText("省份："+phone.getProvince());
                result_type.setText("运营商："+phone.getCatName());
                result_gs_type.setText("归属运营商："+phone.getCarrier());
            }else{
                Toast.makeText(this,"JSON解析出错",Toast.LENGTH_LONG).show();
            }
        }else{
            result_phone.setText("解析出错");
        }
    }
}
