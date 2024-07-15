package com.example.api.service;


import com.example.api.domain.coupon.Coupon;
import com.example.api.domain.coupon.CouponRequestDTO;
import com.example.api.domain.event.Event;
import com.example.api.repositories.CouponRepository;
import com.example.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository repository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {

        Event event = this.repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setEvent(event);
        coupon.setDiscount(couponData.discount());
        coupon.setValid(new Date(couponData.valid()));

        return this.couponRepository.save(coupon);

    }
}
