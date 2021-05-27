package com.prashant.ibtidaa;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

    private Activity mActivity;
    private AlertDialog alertDialog;

    LoadingDialog(Activity activity){
        mActivity = activity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater layoutInflater = mActivity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loading_screen_pop_up
        ,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }

}
