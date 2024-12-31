package com.example.webtrungtam.repository;

import com.example.webtrungtam.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    // Tìm lịch học theo ID lớp học
    List<Schedule> findByIdClass_IdClass(int idClass);

    // Tìm lịch học của học sinh
    List<Schedule> findByStudent_IdStudent(String idStudent);

    // Tìm lịch học theo ngày cụ thể
    List<Schedule> findByDate(Date date);
}