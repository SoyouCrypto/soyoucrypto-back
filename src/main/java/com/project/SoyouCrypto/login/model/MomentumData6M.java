package com.project.SoyouCrypto.login.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@AllArgsConstructor
@Entity // DB 테이블 역할을 합니다.
@Table(name = "MomentumData6M")
public class MomentumData6M {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @CsvBindByName
    @Column
    private String time;

    @CsvBindByName
    @Column
    private double momentum_algo;

    @CsvBindByName
    @Column
    private double btc_usdt;

    @Column(nullable = true)
    private double cum_return_ma;

    @Column(nullable = true)
    private double cum_return_btc;

    @Column(nullable = true)
    private double daily_return_ma;

    @Column(nullable = true)
    private double ATH;

    @Column(nullable = true)
    private double DD;

    @Column(nullable = true)
    private double anotherItem;

    public void updateTodayData(double cum_return_ma, double cum_return_btc, double daily_return_ma, double ATH, double DD) {
        this.cum_return_ma = cum_return_ma;
        this.cum_return_btc = cum_return_btc;
        this.daily_return_ma = daily_return_ma;
        this.ATH = ATH;
        this.DD = DD;
        System.out.println("업데이트하기");
    }
    }