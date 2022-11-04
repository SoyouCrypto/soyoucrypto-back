package com.project.SoyouCrypto.login.repository;



import com.project.SoyouCrypto.login.model.MomentumData1Y;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MomentumData1YRepository extends JpaRepository<MomentumData1Y, Integer> {
    Optional<MomentumData1Y> findByTime(String time);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "truncate table momentum_data1y")
    void truncateMomentumData1Y();


}