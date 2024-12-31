package com.example.webtrungtam.repository.Class;

import com.example.webtrungtam.model.ClassDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassDetailRepository extends JpaRepository<ClassDetail, Integer> {

    // Tìm chi tiết lớp theo giáo viên
    List<ClassDetail> findByTeacher_IdTeacher(String idTeacher);

    // Tìm chi tiết lớp theo lớp học
    List<ClassDetail> findByIdClass_IdClass(int idClass);

    // Tìm chi tiết theo lịch học
    List<ClassDetail> findBySchedule_IdSchedule(int idSchedule);
}