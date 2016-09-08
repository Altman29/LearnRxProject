package com.csy.rx_retrofit;

import android.app.Application;

/**
 * author:Csy on 2016/9/8 13:29
 * e-mail：s1yuan_chen@163.com
 * desc: 全局的Application
 */
public class App extends Application {
    private static App INSTANCE;

    public static App getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
