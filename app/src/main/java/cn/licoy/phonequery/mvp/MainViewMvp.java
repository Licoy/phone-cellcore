package cn.licoy.phonequery.mvp;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface MainViewMvp extends ToastViewMvp,LoadViewMvp {
    void updateView(Object o) throws IOException;

}
