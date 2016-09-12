package com.csy.rx_retrofit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import butterknife.OnClick;
import rx.Subscription;

/**
 * author:Csy on 2016/9/8 14:15
 * e-mail：s1yuan_chen@163.com
 * desc: Fragment的基类，抽象类。子类具体实现。Subscription统一在Fragment销毁时销毁。
 */
public abstract class BaseFragment extends Fragment{

    private static final String TAG = "BaseFragment";

    protected Subscription mSubscription;

    @OnClick(R.id.tipBt)
    void tip(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(),null))
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    /**
     * Fragment销毁时，同时销毁Subscriber。避免OOM?
     */
    protected void unSubscribe() {
        if (mSubscription != null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
