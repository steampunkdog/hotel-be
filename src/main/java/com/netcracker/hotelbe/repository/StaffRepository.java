package com.netcracker.hotelbe.repository;

import com.netcracker.hotelbe.entity.Staff;
import com.netcracker.hotelbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("UPDATE Staff s SET s.isActive = :active WHERE s.id = :id")
    public void setStatusById(@Param("active") Boolean active, @Param("id") Long id);
}