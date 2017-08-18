package cn.licoy.phonequery.mvp;

import java.io.IOException;



public interface MainViewMvp extends ToastViewMvp,LoadViewMvp {
    void updateView(Object o) throws IOException;

}
