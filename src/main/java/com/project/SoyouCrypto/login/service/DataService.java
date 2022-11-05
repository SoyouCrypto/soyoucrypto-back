package com.project.SoyouCrypto.login.service;

import com.project.SoyouCrypto.login.model.*;
import com.project.SoyouCrypto.login.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DataService {

    private final MomentumOutputIndexRepository momentumOutputIndexRepository;
    private final StableOutputIndexRepository stableOutputIndexRepository;
    private final MomentumDataAllRepository momentumDataAllRepository;
    private final MomentumData1YRepository momentumData1YRepository;
    private final MomentumData6MRepository momentumData6MRepository;
    private final MomentumData3MRepository momentumData3MRepository;
    private final StableDataAllRepository stableDataAllRepository;
    private final StableData1YRepository stableData1YRepository;
    private final StableData6MRepository stableData6MRepository;
    private final StableData3MRepository stableData3MRepository;

    // 그래프 데이터 반환
    public Object getMomentumGraphData(String type) {
        switch (type) {
            case "MOMENTUMALL":
                return momentumDataAllRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
            case "MOMENTUM1Y":
                return momentumData1YRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
            case "MOMENTUM6M":
                return momentumData6MRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
            default:
                return momentumData3MRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
        }
    }

    // 그래프 데이터 반환
    public Object getStableGraphData(String type) {
        switch (type) {
            case "STABLEALL":
                return stableDataAllRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
            case "STABLE1Y":
                return stableData1YRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
            case "STABLE6M":
                return stableData6MRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
            default:
                return stableData3MRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
        }
    }

    public MomentumOutputIndex getMomentumPortfolioIndex(String type) {
        System.out.println("Calc2 함수 호출");
        Optional<MomentumOutputIndex> momentumIndex = momentumOutputIndexRepository.findByIndexType(type);
        double cumReturn = 0;
        double dailyAvg = 0;
        double dailySharp = 0;
        double mdd = 0;
        LocalDateTime uploadTime = null;
        if (momentumIndex.isPresent()) {
            cumReturn = Math.round(momentumIndex.get().getCumReturn() * 100);
            dailyAvg = (Math.round(momentumIndex.get().getDailyAvg() * 10000) / 100.0);
            dailySharp = (Math.round(momentumIndex.get().getDailySharp() * 100) / 100.0);
            mdd = Math.round(momentumIndex.get().getMdd() * 100);
            uploadTime = momentumIndex.get().getUploadTime();
        }
        MomentumOutputIndex roundedIndex = new MomentumOutputIndex("OUTPUT", cumReturn, dailyAvg, dailySharp, mdd, uploadTime);
        return roundedIndex;
    }

    public StableOutputIndex getStablePortfolioIndex(String type) {
        System.out.println("stableIndex 함수 호출");
        System.out.println("stableIndex 함수 종류"+ type);
        Optional<StableOutputIndex> stableIndex = stableOutputIndexRepository.findByIndexType(type);
        System.out.println("값은?"+stableIndex);
        double cumReturn = 0;
        double dailyAvg = 0;
        double dailySharp = 0;
        double mdd = 0;
        LocalDateTime uploadTime = null;
        if (stableIndex.isPresent()) {
            cumReturn = Math.round(stableIndex.get().getCumReturn() * 100);
            dailyAvg = (Math.round(stableIndex.get().getDailyAvg() * 10000) / 100.0);
            dailySharp = (Math.round(stableIndex.get().getDailySharp() * 100) / 100.0);
            mdd = Math.round(stableIndex.get().getMdd() * 100);
            uploadTime = stableIndex.get().getUploadTime();
        }
        StableOutputIndex roundedIndex = new StableOutputIndex("OUTPUT", cumReturn, dailyAvg, dailySharp, mdd, uploadTime);
        System.out.println("전달은?"+ roundedIndex);
        return roundedIndex;
    }

    // MomentumData 해당 함수들
//     1Y 데이터 기간에 맞게 자르기
    public void momentumDataTrim1Y() throws ParseException {
        System.out.println("1Y trim 함수 실행");
        List<MomentumData1Y> momentumData1Y = momentumData1YRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
        String latestDay = momentumData1Y.get(0).getTime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.YEAR ,-1); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<MomentumData1Y> targetDayData = momentumData1YRepository.findByTime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            momentumData1YRepository.deleteById(i);
        }
    }

    //     6M 데이터 기간에 맞게 자르기
    public void momentumDataTrim6M() throws ParseException {
        System.out.println("6M trim 함수 실행");
        List<MomentumData6M> momentumData6M = momentumData6MRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
        String latestDay = momentumData6M.get(0).getTime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.MONTH ,-6); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<MomentumData6M> targetDayData = momentumData6MRepository.findByTime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            momentumData6MRepository.deleteById(i);
        }
    }

    //     3M 데이터 기간에 맞게 자르기
    public void momentumDataTrim3M() throws ParseException {
        System.out.println("3M trim 함수 실행");
        List<MomentumData3M> momentumData3M = momentumData3MRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
        String latestDay = momentumData3M.get(0).getTime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.MONTH ,-3); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<MomentumData3M> targetDayData = momentumData3MRepository.findByTime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            momentumData3MRepository.deleteById(i);
        }
    }

    // 인덱스 구하기 위한 data 가공
    public void momentumDataAllProcessing() {

        // DB에 저장할 다른항목들
        List<MomentumDataAll> momentumDataAll = momentumDataAllRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
        int dataSize = momentumDataAll.size();
        double initialMA = 0;
        double initialBTC = 0;
        double ATHComps = 0;
        MomentumDataAll todayData;
        MomentumDataAll yesterdayData;
        if (momentumDataAll.size() != 0) {
            initialMA = momentumDataAll.get(0).getMomentum_algo();
            initialBTC = momentumDataAll.get(0).getBtc_usdt();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = momentumDataAll.get(i);
            yesterdayData = momentumDataAll.get(i - 1);
            double today_cum_return_ma = ((todayData.getMomentum_algo() / initialMA) - 1);
            double today_cum_return_btc = (todayData.getBtc_usdt() / initialBTC) - 1;
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            System.out.println("제곱 계산?" + sumOfDailyReturnSquare);
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_cum_return_btc, today_daily_return_ma, ATHComps, today_DD);
            momentumDataAllRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((momentumDataAll.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;

        MomentumOutputIndex momentumIndexAll = new MomentumOutputIndex("MOMENTUMALL", cumReturn, dailyAvg, dailySharp, mdd, LocalDateTime.now());
        momentumOutputIndexRepository.save(momentumIndexAll);
    }

    // 1Y data 가공
    public void momentumData1YProcessing() {
        System.out.println("Calc 함수 호출2");

        // DB에 저장할 다른항목들
        List<MomentumData1Y> momentumData1Y = momentumData1YRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
        int dataSize = momentumData1Y.size();
        double initialMA = 0;
        double initialBTC = 0;
        double ATHComps = 0;
        MomentumData1Y todayData;
        MomentumData1Y yesterdayData;
        if (momentumData1Y.size() != 0) {
            initialMA = momentumData1Y.get(0).getMomentum_algo();
            initialBTC = momentumData1Y.get(0).getBtc_usdt();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = momentumData1Y.get(i);
            yesterdayData = momentumData1Y.get(i - 1);
            double today_cum_return_ma = ((todayData.getMomentum_algo() / initialMA) - 1);
            double today_cum_return_btc = (todayData.getBtc_usdt() / initialBTC) - 1;
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_cum_return_btc, today_daily_return_ma, ATHComps, today_DD);
            momentumData1YRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((momentumData1Y.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        MomentumOutputIndex momentumIndex1Y = new MomentumOutputIndex("MOMENTUM1Y", cumReturn, dailyAvg, dailySharp, mdd);
        momentumOutputIndexRepository.save(momentumIndex1Y);
    }

    // 6M data 가공
    public void momentumData6MProcessing() {
        System.out.println("Calc 함수 호출3");

        // DB에 저장할 다른항목들
        List<MomentumData6M> momentumData6M = momentumData6MRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
        int dataSize = momentumData6M.size();
        double initialMA = 0;
        double initialBTC = 0;
        double ATHComps = 0;
        MomentumData6M todayData;
        MomentumData6M yesterdayData;
        if (momentumData6M.size() != 0) {
            initialMA = momentumData6M.get(0).getMomentum_algo();
            initialBTC = momentumData6M.get(0).getBtc_usdt();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = momentumData6M.get(i);
            yesterdayData = momentumData6M.get(i - 1);
            double today_cum_return_ma = ((todayData.getMomentum_algo() / initialMA) - 1);
            double today_cum_return_btc = (todayData.getBtc_usdt() / initialBTC) - 1;
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_cum_return_btc, today_daily_return_ma, ATHComps, today_DD);
            momentumData6MRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((momentumData6M.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        MomentumOutputIndex momentumIndex6M = new MomentumOutputIndex("MOMENTUM6M", cumReturn, dailyAvg, dailySharp, mdd);
//        OutputIndex momentumIndex6M = new OutputIndex("MOMENTUM6M", cumReturn, (Math.round(dailyAvg*1000000))/1000000.0, (Math.round(dailySharp*1000000))/1000000.0, mdd);
        momentumOutputIndexRepository.save(momentumIndex6M);
    }

    // 3M data 가공
    public void momentumData3MProcessing() {
        System.out.println("Calc 함수 호출4");

        // DB에 저장할 다른항목들
        List<MomentumData3M> momentumData3M = momentumData3MRepository.findAll(Sort.by(Sort.Direction.ASC, "time"));
        int dataSize = momentumData3M.size();
        double initialMA = 0;
        double initialBTC = 0;
        double ATHComps = 0;
        MomentumData3M todayData;
        MomentumData3M yesterdayData;
        if (momentumData3M.size() != 0) {
            initialMA = momentumData3M.get(0).getMomentum_algo();
            initialBTC = momentumData3M.get(0).getBtc_usdt();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = momentumData3M.get(i);
            yesterdayData = momentumData3M.get(i - 1);
            double today_cum_return_ma = ((todayData.getMomentum_algo() / initialMA) - 1);
            double today_cum_return_btc = (todayData.getBtc_usdt() / initialBTC) - 1;
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_cum_return_btc, today_daily_return_ma, ATHComps, today_DD);
            momentumData3MRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((momentumData3M.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        MomentumOutputIndex momentumIndex3M = new MomentumOutputIndex("MOMENTUM3M", cumReturn, dailyAvg, dailySharp, mdd);
        momentumOutputIndexRepository.save(momentumIndex3M);
    }

    // StableData 해당 함수들
//     1Y 데이터 기간에 맞게 자르기
    public void stableDataTrim1Y() throws ParseException {
        List<StableData1Y> stableData1Y = stableData1YRepository.findAll(Sort.by(Sort.Direction.DESC, "datetime"));
        String latestDay = stableData1Y.get(0).getDatetime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.YEAR ,-1); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<StableData1Y> targetDayData = stableData1YRepository.findByDatetime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            stableData1YRepository.deleteById(i);
        }
    }

    //     6M 데이터 기간에 맞게 자르기
    public void stableDataTrim6M() throws ParseException {
        List<StableData6M> stableData6M = stableData6MRepository.findAll(Sort.by(Sort.Direction.DESC, "datetime"));
        String latestDay = stableData6M.get(0).getDatetime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.MONTH ,-6); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<StableData6M> targetDayData = stableData6MRepository.findByDatetime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            stableData6MRepository.deleteById(i);
        }
    }

    //     3M 데이터 기간에 맞게 자르기
    public void stableDataTrim3M() throws ParseException {
        System.out.println("3M trim 함수 실행");
        List<StableData3M> stableData3M = stableData3MRepository.findAll(Sort.by(Sort.Direction.DESC, "datetime"));
        String latestDay = stableData3M.get(0).getDatetime();
        // 기간에 맞는 날짜 범위 구하기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dt = formatter.parse(latestDay);
        cal.setTime(dt);
        cal.add(Calendar.MONTH ,-3); // 일년전 날짜 가져오기
        String targetEndDay = formatter.format(cal.getTime());
        // 기간 외 항목 삭제
        Optional<StableData3M> targetDayData = stableData3MRepository.findByDatetime(targetEndDay);
        int endDayId = targetDayData.get().getId();
        for (int i = 1; i < endDayId; i++) {
            stableData3MRepository.deleteById(i);
        }
    }

    // 인덱스 구하기 위한 data 가공
    public void stableDataAllProcessing() {

        // DB에 저장할 다른항목들
        List<StableDataAll> stableDataAll = stableDataAllRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
        int dataSize = stableDataAll.size();
        double initialMA = 0;
        double ATHComps = 0;
        StableDataAll todayData;
        StableDataAll yesterdayData;
        if (stableDataAll.size() != 0) {
            initialMA = stableDataAll.get(0).getStable_only();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = stableDataAll.get(i);
            yesterdayData = stableDataAll.get(i - 1);
            double today_cum_return_ma = ((todayData.getStable_only() / initialMA) - 1);
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
//            double today_DD = (((today_cum_return_ma + 1) / (ATHComps + 1)) - 1);
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_daily_return_ma, ATHComps, today_DD);
            stableDataAllRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((stableDataAll.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        StableOutputIndex stableIndexAll = new StableOutputIndex("STABLEALL", cumReturn, dailyAvg, dailySharp, mdd, LocalDateTime.now());
        stableOutputIndexRepository.save(stableIndexAll);
    }

    // 1Y data 가공
    public void stableData1YProcessing() {

        // DB에 저장할 다른항목들
        List<StableData1Y> stableData1Y = stableData1YRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
        int dataSize = stableData1Y.size();
        double initialMA = 0;
        double ATHComps = 0;
        StableData1Y todayData;
        StableData1Y yesterdayData;
        if (stableData1Y.size() != 0) {
            initialMA = stableData1Y.get(0).getStable_only();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = stableData1Y.get(i);
            yesterdayData = stableData1Y.get(i - 1);
            double today_cum_return_ma = ((todayData.getStable_only() / initialMA) - 1);
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_daily_return_ma, ATHComps, today_DD);
            stableData1YRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((stableData1Y.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        StableOutputIndex stableIndex1Y = new StableOutputIndex("STABLE1Y", cumReturn, dailyAvg, dailySharp, mdd);
        stableOutputIndexRepository.save(stableIndex1Y);
    }

    // 6M data 가공
    public void stableData6MProcessing() {

        // DB에 저장할 다른항목들
        List<StableData6M> stableData6M = stableData6MRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
        int dataSize = stableData6M.size();
        double initialMA = 0;
        double ATHComps = 0;
        StableData6M todayData;
        StableData6M yesterdayData;
        if (stableData6M.size() != 0) {
            initialMA = stableData6M.get(0).getStable_only();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = stableData6M.get(i);
            yesterdayData = stableData6M.get(i - 1);
            double today_cum_return_ma = ((todayData.getStable_only() / initialMA) - 1);
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_daily_return_ma, ATHComps, today_DD);
            stableData6MRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((stableData6M.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        StableOutputIndex stableIndex6M = new StableOutputIndex("STABLE6M", cumReturn, dailyAvg, dailySharp, mdd);
        stableOutputIndexRepository.save(stableIndex6M);
    }

    // 3M data 가공
    public void stableData3MProcessing() {

        // DB에 저장할 다른항목들
        List<StableData3M> stableData3M = stableData3MRepository.findAll(Sort.by(Sort.Direction.ASC, "datetime"));
        int dataSize = stableData3M.size();
        double initialMA = 0;
        double ATHComps = 0;
        StableData3M todayData;
        StableData3M yesterdayData;
        if (stableData3M.size() != 0) {
            initialMA = stableData3M.get(0).getStable_only();
        }

        // 지수 계산에 사용될 항목들
        double sumOfDailyReturn = 0;
        double sumOfDailyReturnSquare = 0;
        double mdd = 0;

        for (int i = 1; i < dataSize; i++) {
            todayData = stableData3M.get(i);
            yesterdayData = stableData3M.get(i - 1);
            double today_cum_return_ma = ((todayData.getStable_only() / initialMA) - 1);
            double today_daily_return_ma = today_cum_return_ma - yesterdayData.getCum_return_ma();

            // 화면에 띄울 인덱스
            sumOfDailyReturn += today_daily_return_ma;
            sumOfDailyReturnSquare += today_daily_return_ma * today_daily_return_ma;
            // ATH 구하기 위한 비교
            if (yesterdayData.getCum_return_ma() >= ATHComps) {
                ATHComps = yesterdayData.getCum_return_ma();
            }

            // mdd 구하기 위한 비교
            double today_DD = (today_cum_return_ma - ATHComps);
            if (mdd >= today_DD) {
                mdd = today_DD;
            }
            // DB 계산값들 저장
            todayData.updateTodayData(today_cum_return_ma, today_daily_return_ma, ATHComps, today_DD);
            stableData3MRepository.save(todayData);
        }
        // 반복문 종료

        // 화면에 띄울 인덱스 계산, 분산공식 변형
        double cumReturn = (Math.round((stableData3M.get(dataSize - 1).getCum_return_ma()) * 1000000)) / 1000000.0;
        // dataSize 에서 -1 해야 하지 않을까? 첫날은 수익이 날 수 없으니 포함 안시킨다면?
        double dailyAvg = (Math.round((sumOfDailyReturn / (dataSize)) * 1000000)) / 1000000.0;
        // 표준편차 구할때는 dataSize-1 이 됐다. 아마 표본이라 그런듯?
        double dailyAvgStd = (Math.round((Math.sqrt((sumOfDailyReturnSquare / (dataSize - 1)) - (dailyAvg * dailyAvg))) * 1000000)) / 1000000.0;
        double dailySharp = (Math.round((dailyAvg / dailyAvgStd) * 1000000)) / 1000000.0;
        StableOutputIndex stableIndex3M = new StableOutputIndex("STABLE3M", cumReturn, dailyAvg, dailySharp, mdd);
        stableOutputIndexRepository.save(stableIndex3M);
    }

    @Transactional
    public void momentumDataDelete() throws ParseException {
        System.out.println("데이터삭제 함수 실행");
        momentumDataAllRepository.truncateMomentumDataAll();
        momentumData1YRepository.truncateMomentumData1Y();
        momentumData6MRepository.truncateMomentumData6M();
        momentumData3MRepository.truncateMomentumData3M();
        momentumOutputIndexRepository.truncateMomentumOutputIndex();
    }

    @Transactional
    public void stableDataDelete() throws ParseException {
        System.out.println("데이터삭제 함수 실행");
        stableDataAllRepository.truncateStableDataAll();
        stableData1YRepository.truncateStableData1Y();
        stableData6MRepository.truncateStableData6M();
        stableData3MRepository.truncateStableData3M();
        stableOutputIndexRepository.truncateStableOutputIndex();
    }
}


