package com.teocfish.teoc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderCancel extends Fragment {
    Button order_cancellation;


    public OrderCancel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order_cancel, container, false);
        order_cancellation=(Button)view.findViewById(R.id.order_cancellation);
        order_cancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.moveTo(getContext(),OrderCanceled.class);
            }
        });
        return view;
    }

}
