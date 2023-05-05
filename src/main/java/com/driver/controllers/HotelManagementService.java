package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelManagementService {

    @Autowired
    HotelManagementRepo hotelRepo;

    public boolean addHotel(Hotel hotel){
        if(hotel!=null && hotel.getHotelName()!="" && hotelRepo.checkHotel(hotel.getHotelName())==false){
            hotelRepo.addHotel(hotel);
            return true;
        }
        return false;
    }

    public boolean addUser(User user){
        hotelRepo.addUser(user);
        return true;
    }

    public int getmaxfacility(Map<String,Hotel> hotelMap){
        int max=0;
        for(String val : hotelMap.keySet()){
            Hotel h=hotelMap.get(val);
            if(h.getFacilities().size()>max){
                max=h.getFacilities().size();
            }
        }
        return max;
    }

    public void addHotelstoList(List<String> hotelList,Map<String,Hotel> hotelMap,int maxfac){
        for(String val : hotelMap.keySet()){
            Hotel h=hotelMap.get(val);
            int facsize=h.getFacilities().size();
            if(facsize==maxfac){
                hotelList.add(h.getHotelName());
            }
        }
    }

    public String sortHotels(List<String> hotelList){
        Collections.sort(hotelList);
        return hotelList.get(0);
    }
    public String getHotelWithMostFacilities(){
        Map<String,Hotel> hotelMap=hotelRepo.getHotelDB();
        int maxfac=getmaxfacility(hotelMap);
        if(maxfac<1){
            return "";
        }
        List<String> hotelList=new ArrayList<>();
        addHotelstoList(hotelList,hotelMap,maxfac);
        if(hotelList.size()==1){
            return hotelList.get(0);
        }else{
            return sortHotels(hotelList);
        }
    }

    public int getTotalBookings(Integer aadharCard){
        Map<String, Booking> bookingMap=hotelRepo.getBookingDB();
        int count=0;
        for(String val : bookingMap.keySet()){
            Booking b=bookingMap.get(val);
            int bAadharCard=b.getBookingAadharCard();
            if(bAadharCard==aadharCard.intValue()){
                count++;
            }
        }
        return count;
    }



    public Hotel updateFacilites(List<Facility> newFacilities, String hotelName) {
        Map<String,Hotel> hotelMap=hotelRepo.getHotelDB();
        Hotel h=hotelMap.get(hotelName);
        List<Facility> list1=h.getFacilities();
        List<Facility> checkDuplicate=new ArrayList<>();
        for(Facility f: newFacilities){
            if(!checkDuplicate.contains(f)){
                int f1=Collections.frequency(list1,f);
                int f2=Collections.frequency(newFacilities,f);
                if(f2>f1){
                    int count=f2-f1;
                    for(int i=0;i<count;i++){
                        list1.add(f);
                    }
                }
                checkDuplicate.add(f);
            }

        }
        h.setFacilities(list1);
        hotelMap.remove(hotelName);
        addHotel(h);
        return h;
    }

    public int bookARoom(Booking booking){
        Optional<Hotel> bookedHotelOpt = hotelRepo.getHotel(booking.getHotelName());
        Hotel bookedHotel=bookedHotelOpt.get();
        if(bookedHotel.getAvailableRooms() < booking.getNoOfRooms()) return -1;
        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);
        booking.setAmountToBePaid(booking.getNoOfRooms() * bookedHotel.getPricePerNight());
        hotelRepo.addBooking(booking);
        return booking.getAmountToBePaid();
    }
}
