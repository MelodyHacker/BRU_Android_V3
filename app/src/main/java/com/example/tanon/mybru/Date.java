package com.example.tanon.mybru;

import java.text.SimpleDateFormat;

public class Date {
    public String dateToDay() {
        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat("yyyy.MM.dd");

        return ft.format(dNow);
    }
}
