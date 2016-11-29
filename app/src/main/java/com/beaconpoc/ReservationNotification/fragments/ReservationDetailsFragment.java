package com.beaconpoc.ReservationNotification.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beaconpoc.ReservationNotification.MyApplication;
import com.beaconpoc.ReservationNotification.R;
import com.beaconpoc.ReservationNotification.utils.FormatUtils;
import com.beaconpoc.ReservationNotification.webservice.model.DeviceDetailsResponse;
import com.beaconpoc.ReservationNotification.webservice.model.PushNotificationFcmModel;

/**
 * Created by Aparupa on 11/17/2016.
 */

public class ReservationDetailsFragment extends BaseFragment {

    private static String DEVICE_DETAILS_ARGUMENT = "device_details";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflateView = inflater.inflate(R.layout.fragment_reservation_details, container, false);

        setData(inflateView);
        return inflateView;
    }

    private void setData(View inflateView) {

        if (((MyApplication) getActivity().getApplication()).getPushNotificationFcmModel() != null) {

            PushNotificationFcmModel pushNotificationFcmModel = ((MyApplication) getActivity().getApplication()).getPushNotificationFcmModel();

            ((ScrollView) inflateView.findViewById(R.id.ParentScrollView)).setVisibility(View.VISIBLE);
            ((TextView) inflateView.findViewById(R.id.welcome_msg)).setVisibility(View.GONE);

            ((TextView) inflateView.findViewById(R.id.reservation_category))
                    .setText(pushNotificationFcmModel.getReservations().get(0).getCategory());
            ((TextView) inflateView.findViewById(R.id.reservation_booking_price))
                    .setText(Float.toString(pushNotificationFcmModel.getReservations().get(0).getBookingPrice()) + "$");
            ((TextView) inflateView.findViewById(R.id.reservation_number))
                    .setText("Reservation Number - " + pushNotificationFcmModel.getReservations().get(0).getReservationId());
            ((TextView) inflateView.findViewById(R.id.reservation_time_period))
                    .setText(FormatUtils.getDate((long) pushNotificationFcmModel.getReservations().get(0).getStartDateTime()) + " ~ " + FormatUtils.getDate((long) pushNotificationFcmModel.getReservations().get(0).getEndDateTime()));
            ((TextView) inflateView.findViewById(R.id.reservation_special_instructions))
                    .setText(pushNotificationFcmModel.getReservations().get(0).getSpecialInstructions());

        } else {

            ((ScrollView) inflateView.findViewById(R.id.ParentScrollView)).setVisibility(View.GONE);
            ((TextView) inflateView.findViewById(R.id.welcome_msg)).setVisibility(View.VISIBLE);
            if (getArguments() != null) {
                String deviceDetailsResponse = getArguments().getString(DEVICE_DETAILS_ARGUMENT);
                ((TextView) inflateView.findViewById(R.id.welcome_msg)).setText(deviceDetailsResponse);
            }

        }

    }

    public static ReservationDetailsFragment newInstance(String message) {
        ReservationDetailsFragment reservationDetailsFragment = new ReservationDetailsFragment();
        // Supply index input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_DETAILS_ARGUMENT, message);
        reservationDetailsFragment.setArguments(bundle);
        return reservationDetailsFragment;
    }

}
