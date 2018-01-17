package com.example.ivica.popupfragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ivica on 2.10.2017..
 */

public class MyDialog extends DialogFragment implements View.OnClickListener {

    Button b;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydialog, null);
        b = (Button) view.findViewById(R.id.button3);
        b.setOnClickListener(this);
        setCancelable(false);

        view.setClickable(false);
        view.setFocusable(false);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button3){
            communicator.onDialogMessage("yesss");
            dismiss();
//            Toast.makeText(getActivity(),"Butttoon 33",Toast.LENGTH_LONG).show();
        }
    }

    interface Communicator{
        public void onDialogMessage(String message);
    }
}
