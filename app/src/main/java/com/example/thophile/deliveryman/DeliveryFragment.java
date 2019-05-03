package com.example.thophile.deliveryman;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryFragment extends Fragment
                                implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private ArrayList<String> mParam1;
    private Button buttonAccept;
    private Button buttonRefuse;
    private TextView TVConsumerAdress;
    private TextView TVRestaurantAdress;
    private TextView TVDeliveryTime;

    private OnFragmentInteractionListener mListener;

    public DeliveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DeliveryFragment.
     */
    public static DeliveryFragment newInstance(ArrayList<String> param1) {
        DeliveryFragment fragment = new DeliveryFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_fragment_deliveries, container, false);
        buttonAccept = (Button) view.findViewById(R.id.button_accept_delivery);
        buttonRefuse = (Button) view.findViewById(R.id.button_refuse_delivery);
        TVConsumerAdress = (TextView) view.findViewById(R.id.TV_consumer_adress);
        TVRestaurantAdress = (TextView) view.findViewById(R.id.TV_restaurant_adress);
        TVDeliveryTime = (TextView) view.findViewById(R.id.TV_delivery_time);

        buttonAccept.setOnClickListener(this);
        buttonRefuse.setOnClickListener(this);
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_accept_delivery:
                acceptDelivery();
                buttonAccept.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_refuse_delivery:
                refuseDelivery();
                break;
        }
    }

    public void newDeliveryProposal(DeliveryData deliveryData){
        TVConsumerAdress.setText(deliveryData.getDeliveryAdress());
        TVRestaurantAdress.setText(deliveryData.getRestaurantAdress());
        TVDeliveryTime.setText(deliveryData.getPickupTime());

        buttonAccept.setVisibility(View.VISIBLE);
        buttonRefuse.setVisibility(View.VISIBLE);
        //mListener.updateStatus(DeliveriesActivity.STATUSPROPOSAL);

    }

    public void acceptDelivery(){
        buttonAccept.setVisibility(View.INVISIBLE);
        buttonRefuse.setVisibility(View.INVISIBLE);
        //mListener.updateStatus(DeliveriesActivity.STATUSACCEPTED);
    }

    public void refuseDelivery(){
        mListener.updateStatus(DeliveriesActivity.STATUSWAITING);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void updateStatus(int status);
    }
}
