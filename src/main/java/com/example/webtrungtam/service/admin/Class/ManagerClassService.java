package com.example.webtrungtam.service.admin.Class;

import com.example.webtrungtam.dto.CreateClassRequest;
import com.example.webtrungtam.dto.UpdateClassRequest;
import com.example.webtrungtam.model.*;
import com.example.webtrungtam.repository.Class.ClassRepository;
import com.example.webtrungtam.repository.Class.ClassStudentRepository;
import com.example.webtrungtam.repository.Class.SubjectRepository;
import com.example.webtrungtam.repository.StudentRepository;
import com.example.webtrungtam.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Lấy tất cả lớp học
    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    // Tìm lớp học theo ID
    public Optional<ClassEntity> getClassById(int id) {
        return classRepository.findById(id);
    }

    // Thêm hoặc cập nhật lớp học
    public ClassEntity saveClass(ClassEntity aClass) {
        return classRepository.save(aClass);
    }

    public void deleteClass(int id) {
        classRepository.deleteById(id);
    }

    public List<ClassEntity> findClassesBySubject(int idSubject) {
        Subject subject = subjectRepository.findById(idSubject).orElse(null);
        if (subject == null) {
            return Collections.emptyList();
        }
        return classRepository.findBySubject(subject);
    }

    public List<ClassEntity> findClassesByTeacher(String idTeacher) {
        Teacher teacher = teacherRepository.findById(idTeacher).orElse(null);
        if (teacher == null) {
            return Collections.emptyList();
        }
        return classRepository.findByTeacher(teacher);
    }

    public ClassEntity createClass(CreateClassRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getTeacherId()));

        if (subject == null||teacher == null) {
            return null;
        }

        ClassEntity aClass = new ClassEntity();
        aClass.setClassName(request.getClassName());
        aClass.setSubject(subject);
        aClass.setTeacher(teacher);
        aClass.setStartTime(request.getStartTime());
        aClass.setEndTime(request.getEndTime());

        return classRepository.save(aClass);
    }

    public ClassEntity updateClass(int id, UpdateClassRequest request) {
        ClassEntity existingClass = classRepository.findById(id).orElse(null);

        Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);

        Teacher teacher = teacherRepository.findById(request.getTeacherId()).orElse(null);

        if (existingClass == null||subject == null||teacher == null) {
            return null;
        }

        existingClass.setClassName(request.getClassName());
        existingClass.setSubject(subject);
        existingClass.setTeacher(teacher);
        existingClass.setStartTime(request.getStartTime());
        existingClass.setEndTime(request.getEndTime());

        return classRepository.save(existingClass);
    }

    public void addStudentToClass(int classId, String studentId) {
        ClassEntity classEntity = classRepository.findById(classId).orElse(null);

        Student student = studentRepository.findById(studentId).orElse(null);

        if (classStudentRepository.existsByIdClassAndIdStudent(classEntity, student)){
            throw new IllegalArgumentException("Sinh viên đã có trong lớp học");
        }

        ClassStudent classStudent = new ClassStudent(classEntity, student);

        classStudentRepository.save(classStudent);
    }

    public void removeStudentFromClass(int classId, String studentId) {
        ClassEntity classEntity = classRepository.findById(classId).orElse(null);

        Student student = studentRepository.findById(studentId).orElse(null);

        ClassStudent classStudent = classStudentRepository.findByIdClassAndIdStudent(classEntity, student).orElse(null);

        classStudentRepository.delete(classStudent);
    }
}