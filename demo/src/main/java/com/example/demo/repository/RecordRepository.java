package com.example.demo.repository;

import com.example.demo.dto.RecordScoreDTO;
import com.example.demo.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<UserRecord, Long> {
    UserRecord findByTestedByAndCreatedBy(Long testedBy, Long createdBy);

    @Query("SELECT new com.example.demo.dto.RecordScoreDTO(u.nickname, r.score) " +
            "FROM UserRecord r JOIN User u ON r.testedBy = u.id " +
            "WHERE r.createdBy = :createdBy ORDER BY r.score DESC")
    List<RecordScoreDTO> findScoresByCreatedBy(@Param("createdBy") Long createdBy);
}
