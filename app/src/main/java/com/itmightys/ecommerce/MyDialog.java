package com.itmightys.ecommerce;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Niknom on 3/2/2017.
 */

public class MyDialog extends DialogFragment implements View.OnClickListener{
    Communicator communicator;
    private Button confirm,cancel;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialoglayout,null);
        setCancelable(false);
        confirm = (Button)view.findViewById(R.id.confirm);
        cancel = (Button)view.findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm){
            communicator.OnDialogMessage("Your Enquiry has been placed");
            dismiss();
        }else{
            dismiss();
        }
    }
    interface Communicator{
        public void OnDialogMessage(String str);
    }
}
