package com.beaconpoc.ReservationNotification;

import android.os.Bundle;
import android.widget.TextView;

import com.beaconpoc.ReservationNotification.constant.AppBarType;
import com.beaconpoc.ReservationNotification.webservice.model.PushNotificationFcmModel;
import com.beaconpoc.ReservationNotification.webservice.model.ReservationModel;

/**
 * Created by Aparupa on 11/29/2016.
 */

public class ArrivedVehicleDetailsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrived_vehicle_details);
        setupAppBar(getString(R.string.arrived_vehicle_header), AppBarType.NONE);

        if (((MyApplication) getApplication()).getPushNotificationFcmModel() != null) {

            ReservationModel reservationModel = ((MyApplication) getApplication()).getPushNotificationFcmModel().getReservations().get(0);

            ((TextView) findViewById(R.id.welcome_msg))
                    .setText(getString(R.string.arrived_vehicle_header_instructions));
            ((TextView) findViewById(R.id.reservation_id))
                    .setText("Reservation ID : " + reservationModel.getReservationId());
            ((TextView) findViewById(R.id.reservation_booking_price))
                    .setText(Float.toString(reservationModel.getBookingPrice()) + "$");
            ((TextView) findViewById(R.id.reservation_category))
                    .setText(reservationModel.getCategory());
            ((TextView) findViewById(R.id.reservation_vehicle_num))
                    .setText("Vehicle No. : " + reservationModel.getVehicleNo());
            ((TextView) findViewById(R.id.reservation_special_instructions))
                    .setText("Instructions :\n\n" + reservationModel.getSpecialInstructions());

        }

    }
}
