package com.example.webtrungtam.controller.admin.Class;

import com.example.webtrungtam.model.ClassDetail;
import com.example.webtrungtam.service.admin.Class.ClassDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/class-details")
public class ClassDetailController {

    @Autowired
    private ClassDetailService classDetailService;

    @GetMapping("/all")
    public List<ClassDetail> getClassDetails() {
        return classDetailService.getAllClassDetails();
    }

    @PostMapping("/create")
    public ClassDetail createClassDetail(@RequestBody ClassDetail classDetail) {
        return classDetailService.saveClassDetail(classDetail);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteClassDetail(@PathVariable Integer id) {
        classDetailService.deleteClassDetail(id);
        return "Class detail deleted successfully";
    }

    @GetMapping("/search/{id}")
    public Optional<ClassDetail> getClassDetailById(@PathVariable Integer id) {
        return classDetailService.getClassDetailById(id);
    }
}
