package com.finalproject.chorok.myPlant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Setter
@Getter
@NoArgsConstructor
public class MyPlantUpdateRequestDto {
    private String myPlantName;
    private String myPlantPlace;
    private String myPlantImgUrl;

    public MyPlantUpdateRequestDto(String myPlantName, String myPlantPlace, String myPlantImgUrl){
        this.myPlantName = myPlantName;
        this.myPlantImgUrl = myPlantImgUrl;
        this.myPlantPlace = myPlantPlace;

    }
}
