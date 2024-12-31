package com.example.webtrungtam.repository.Class;

import com.example.webtrungtam.model.ClassEntity;
import com.example.webtrungtam.model.Subject;
import com.example.webtrungtam.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    // Tìm lớp theo tên
    List<ClassEntity> findByClassName(String className);

    // Tìm lớp theo môn học
    List<ClassEntity> findBySubject(Subject subject);

    // Tìm lớp theo giáo viên
    List<ClassEntity> findByTeacher(Teacher teacher);

}
