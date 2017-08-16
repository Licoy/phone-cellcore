package cn.licoy.phonequery.mvp.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.licoy.phonequery.mvp.MainViewMvp;
import cn.licoy.phonequery.unit.HttpUnit;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MainPresenter extends BasePresenter {
    private String QUERY_URL = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private MainViewMvp mainViewMvp;
    private String response;

    public MainPresenter(MainViewMvp mvp) {
        this.mainViewMvp = mvp;
    }

    public void search_phone(String phone){
        if(phone.length()!=11){
            mainViewMvp.showToast("手机号码格式有误");
            return;
        }
        mainViewMvp.showLoading();
        //处理业务逻辑
        sendHttpRequest(phone);
    }

    private void sendHttpRequest(String phone){
        Map<String,String> param = new HashMap<>();
        HttpUnit httpUnit = new HttpUnit(new HttpUnit.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                try {
                    mainViewMvp.updateView(object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainViewMvp.hideLoading();
            }

            @Override
            public void onFail(String error) {
                mainViewMvp.showToast(error);
                mainViewMvp.hideLoading();
            }
        });

        param.put("tel",phone);

        httpUnit.get(QUERY_URL,param);



    }

    public String getResponse() {
        return response;
    }
}
