package com.beaconpoc.ReservationNotification.webservice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 472641 on 11/18/2016.
 */
public class ReservationModel implements Parcelable {
    private String reservationId;
    private double startDateTime;
    private double endDateTime;
    private String category;
    private float bookingPrice;
    private String companyName;
    private String companyWebsite;
    private PromoOffers promoOffers;
    private DriveDistance driveDistance;
    private String specialInstructions;
    private String vehicleNo;

    public ReservationModel() {

    }

    protected ReservationModel(Parcel in) {
        reservationId = in.readString();
        startDateTime = in.readDouble();
        endDateTime = in.readDouble();
        category = in.readString();
        bookingPrice = in.readFloat();
        companyName = in.readString();
        companyWebsite = in.readString();
        promoOffers = in.readParcelable(PromoOffers.class.getClassLoader());
        driveDistance = in.readParcelable(DriveDistance.class.getClassLoader());
        specialInstructions = in.readString();
        vehicleNo = in.readString();
    }

    public DriveDistance getDriveDistance() {
        return driveDistance;
    }

    public void setDriveDistance(DriveDistance driveDistance) {
        this.driveDistance = driveDistance;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public double getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(double startDateTime) {
        this.startDateTime = startDateTime;
    }

    public double getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(double endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(float bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public PromoOffers getPromoOffers() {
        return promoOffers;
    }

    public void setPromoOffers(PromoOffers promoOffers) {
        this.promoOffers = promoOffers;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reservationId);
        dest.writeDouble(startDateTime);
        dest.writeDouble(endDateTime);
        dest.writeString(category);
        dest.writeFloat(bookingPrice);
        dest.writeString(companyName);
        dest.writeString(companyWebsite);
        dest.writeParcelable(promoOffers, flags);
        dest.writeParcelable(driveDistance, flags);
        dest.writeString(specialInstructions);
        dest.writeString(vehicleNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReservationModel> CREATOR = new Creator<ReservationModel>() {
        @Override
        public ReservationModel createFromParcel(Parcel in) {
            return new ReservationModel(in);
        }

        @Override
        public ReservationModel[] newArray(int size) {
            return new ReservationModel[size];
        }
    };
}
