package com.example.webtrungtam.service.admin.Class;

import com.example.webtrungtam.model.ClassDetail;
import com.example.webtrungtam.repository.Class.ClassDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassDetailService {

    @Autowired
    private ClassDetailRepository classDetailRepository;

    // Lấy tất cả chi tiết lớp
    public List<ClassDetail> getAllClassDetails() {
        return classDetailRepository.findAll();
    }

    // Tìm chi tiết lớp theo ID
    public Optional<ClassDetail> getClassDetailById(int id) {
        return classDetailRepository.findById(id);
    }

    // Thêm hoặc cập nhật chi tiết lớp
    public ClassDetail saveClassDetail(ClassDetail classDetail) {
        return classDetailRepository.save(classDetail);
    }

    // Xóa chi tiết lớp
    public void deleteClassDetail(int id) {
        classDetailRepository.deleteById(id);
    }
}
