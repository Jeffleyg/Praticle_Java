package com.example.api.controller;

import com.example.api.domain.coupon.Coupon;
import com.example.api.domain.coupon.CouponRequestDTO;
import com.example.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;


    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data){


        Coupon coupons = this.couponService.addCouponToEvent(eventId, data);

        return ResponseEntity.ok(coupons);
    }

}
