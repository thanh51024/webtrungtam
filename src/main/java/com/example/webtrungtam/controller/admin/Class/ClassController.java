package com.example.webtrungtam.controller.admin.Class;

import com.example.webtrungtam.dto.CreateClassRequest;
import com.example.webtrungtam.dto.UpdateClassRequest;
import com.example.webtrungtam.model.ClassEntity;
import com.example.webtrungtam.service.admin.Class.ManagerClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ManagerClassService classService;

    // Lấy tất cả lớp học
    @GetMapping("/all")
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    // Lấy lớp học theo ID
    @GetMapping("/search/{id}")
    public Optional<ClassEntity> getClassById(@PathVariable int id) {
        return classService.getClassById(id);
    }


    // Tạo một lớp học mới
    @PostMapping("/create")
    public ResponseEntity<?> createClass(@Valid @RequestBody CreateClassRequest request) {
        ClassEntity savedClass = classService.createClass(request);
        if (savedClass == null) {
            return ResponseEntity.badRequest().body("Failed to create class. Subject or Teacher not found.");
        }
        return ResponseEntity.ok(savedClass);
    }

    // Cập nhật lớp học
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClass(@PathVariable int id, @Valid @RequestBody UpdateClassRequest request) {
        ClassEntity updatedClass = classService.updateClass(id, request);
        if (updatedClass == null) {
            return ResponseEntity.status(404).body("ClassEntity not found or Subject/Teacher not found.");
        }
        return ResponseEntity.ok(updatedClass);
    }
    // Xóa lớp học
    @DeleteMapping("/delete/{id}")
    public String deleteClass(@PathVariable int id) {
        classService.deleteClass(id);
        return "Deleted class with ID: " + id;
    }

    // Tìm lớp theo môn học
    @GetMapping("/search_class_by_subject/{idSubject}")
    public List<ClassEntity> findClassesBySubject(@PathVariable int idSubject) {
        return classService.findClassesBySubject(idSubject);
    }

    // Tìm lớp theo giáo viên
    @GetMapping("/search_class_by_teacher/{idTeacher}")
    public List<ClassEntity> findClassesByTeacher(@PathVariable String idTeacher) {
        return classService.findClassesByTeacher(idTeacher);
    }

    @PostMapping("/add-student/{classId}/{studentId}")
    public ResponseEntity<String> addStudentToClass(
            @PathVariable int classId,
            @PathVariable String studentId) {
        classService.addStudentToClass(classId, studentId);
        return ResponseEntity.ok("Add student success");
    }

    @DeleteMapping("/remove-student/{classId}/{studentId}")
    public ResponseEntity<String> removeStudentFromClass(
            @PathVariable int classId,
            @PathVariable String studentId) {
        classService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok("Deleted student from class");
    }
}