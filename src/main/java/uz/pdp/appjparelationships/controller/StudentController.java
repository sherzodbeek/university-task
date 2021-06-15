package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    //4.GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }


    @GetMapping("/oneStudent/{id}")
    public Student getOneStudentById(@PathVariable Integer id) {
        return studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Student not found!"));
    }

    @DeleteMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return "Student deleted!";
    }


    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Address address = addressRepository.findById(studentDto.getAddressId()).orElseThrow(() -> new IllegalStateException("Address not found!"));
        student.setAddress(address);
        Group group = groupRepository.findById(studentDto.getGroupId()).orElseThrow(() -> new IllegalStateException("Group not found!"));
        student.setGroup(group);
        List<Subject> subjects = new ArrayList<>();
        List<Integer> subjectsId = studentDto.getSubjectsId();
        for (Integer sId : subjectsId) {
            Subject subject = subjectRepository.findById(sId).orElseThrow(() -> new IllegalStateException("Subject not found!"));
            subjects.add(subject);
        }
        student.setSubjects(subjects);
        studentRepository.save(student);
        return "Student added";
    }

    @PutMapping("/editStudent/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Student editingStudent = studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Student not found"));
        editingStudent.setFirstName(studentDto.getFirstName());
        editingStudent.setLastName(studentDto.getLastName());
        Address address = addressRepository.findById(studentDto.getAddressId()).orElseThrow(() -> new IllegalStateException("Address not found!"));
        editingStudent.setAddress(address);
        Group group = groupRepository.findById(studentDto.getGroupId()).orElseThrow(() -> new IllegalStateException("Group not found!"));
        editingStudent.setGroup(group);
        List<Subject> subjects = new ArrayList<>();
        List<Integer> subjectsId = studentDto.getSubjectsId();
        for (Integer sId : subjectsId) {
            Subject subject = subjectRepository.findById(sId).orElseThrow(() -> new IllegalStateException("Subject not found!"));
            subjects.add(subject);
        }
        editingStudent.setSubjects(subjects);
        studentRepository.save(editingStudent);
        return "Student edited!";
    }


}
