package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class HotelManagementRepo {
    Map<String, Hotel> hotelDB=new HashMap<>();
    Map<Integer, User> userDB=new HashMap<>();
    Map<String, Booking> bookingDB = new HashMap<>();


    public void addHotel(Hotel hotel){

            hotelDB.put(hotel.getHotelName(),hotel);

    }

    public boolean checkHotel(String hotelName){
        if(hotelDB.containsKey(hotelName)){
            return true;
        }else {
            return false;
        }
    }

    public void addUser(User user){
        userDB.put(user.getaadharCardNo(),user);
    }

    public void addBooking(Booking booking){
        bookingDB.put(booking.getBookingId(),booking);
    }

    public Map<String,Hotel> getHotelDB(){
        return hotelDB;
    }

    public Map<String,Booking> getBookingDB(){
        return bookingDB;
    }

    public Optional<Hotel> getHotel(String hotelName){
        Optional<Hotel> o1= Optional.ofNullable(hotelDB.get(hotelName));
        return o1;
    }

}
