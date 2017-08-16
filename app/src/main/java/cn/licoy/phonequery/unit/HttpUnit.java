package cn.licoy.phonequery.unit;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/15.
 */

/**
 * HTTP请求工具
 * @author licoy.cn
 */
public class HttpUnit {

    private String http_request_url;
    private Map<String,String> http_request_param;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private HttpResponse mResponse;
    private Handler handler = new Handler(Looper.getMainLooper());


    public interface HttpResponse {

        void onSuccess(Object object);

        void onFail(String error);
    }

    public HttpUnit(HttpResponse response) {
        mResponse = response;
    }
    /**
     * GET请求
     * @param url 请求地址
     * @param param 请求参数
     */
    public void get(String url, Map<String,String> param){
        send(url,param,false);
    }

    /**
     * POST请求
     * @param url 请求地址
     * @param param 请求参数
     */
    public void post(String url, Map<String,String> param){
        send(url,param,true);
    }

    /**
     * 发送请求
     * @param url 请求地址
     * @param param 请求参数
     * @param post 是否post请求
     */
    private void send(String url, Map<String,String> param,boolean post){
        http_request_url = url;
        http_request_param = param;
        run(post);
    }

    /**
     * 执行请求
     * @param post 是否post请求
     */
    private void run(boolean post){
        Request request = getRequest(post);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("request error","请求错误");
                if (mResponse != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mResponse.onFail("请求错误,请检查网络是否正常");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.e("request error","请求成功");
                if (mResponse == null) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()) {
                            mResponse.onFail("请求失败：code" + response);
                        } else {
                            try {
                                mResponse.onSuccess(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                mResponse.onFail("结果转换失败");
                            }
                        }
                    }
                });
            }
        });

    }


    /**
     * 获取Request请求实例
     * @param post 是否post请求
     * @return Request
     */
    private Request getRequest(boolean post){
        Request request = null;
        if(post){
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (Map.Entry<String, String> map : http_request_param.entrySet()) {
                builder.addFormDataPart(map.getKey(), map.getValue());
            }
            request = new Request.Builder().url(http_request_url)
                    .post(builder.build()).build();
        }else{
            String url = http_request_url+"?"+mapParam();
            //Log.e("####url",url);
            request = new Request.Builder().url(url).build();
        }
        return request;
    }

    /**
     * 拼接GET参数
     * @return String
     */
    private String mapParam(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> map : http_request_param.entrySet()) {
            stringBuilder.append(map.getKey()).append("=").append(map.getValue()).append("&");
        }
        String re = null;
        if(!"".equals(stringBuilder.toString())){
            re = stringBuilder.substring(0,stringBuilder.length()-1);
        }
        return  re;
    }

}
