package cn.licoy.phonequery.mvp.impl;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/15.
 */

public class BasePresenter {
    Context context;
    public void onCreate(Context context){
        this.context = context;
    }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){
        context = null;
    }
}
