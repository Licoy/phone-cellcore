package cn.licoy.phonequery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import cn.licoy.phonequery.bean.Phone;
import cn.licoy.phonequery.mvp.MainViewMvp;
import cn.licoy.phonequery.mvp.impl.MainPresenter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MainViewMvp {

    private EditText input_phone;
    private Button btn_search;
    private TextView result_phone;
    private TextView result_province;
    private TextView result_type;
    private TextView result_gs_type;
    private MainPresenter mainPresenter;
    private ProgressDialog progressDialog;
    private Button about;
    private static final String TAG = "MainActivity";

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
        about = (Button) findViewById(R.id.about);
        mainPresenter = new MainPresenter(this);
        mainPresenter.onCreate(this);

        btn_search.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search : mainPresenter.search_phone(input_phone.getText().toString());break;
            case R.id.about : about();break;
        }
        //btn_search.setText("查询中...");

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
                Toast.makeText(this,"查询成功",Toast.LENGTH_SHORT).show();
                Log.i(TAG, "updateView: 查询成功");
            }else{
                Toast.makeText(this,"JSON解析出错",Toast.LENGTH_LONG).show();
                Log.e(TAG, "updateView: ", new Throwable("JSON解析出错"));

            }
        }else{
            result_phone.setText("解析出错");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("确定要退出吗");
            builder.setPositiveButton("确定",listener);
            builder.setNegativeButton("取消",listener);
            builder.show();

        }
        return false;
    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    System.exit(0);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 关于
     */
    private void about(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("关于 / About");
        b.setMessage("作者: Licoy\n博客: https://www.licoy.cn\nGithub: https://github.com/Licoy");
        b.setPositiveButton("关闭",null);
        b.show();

    }
}
