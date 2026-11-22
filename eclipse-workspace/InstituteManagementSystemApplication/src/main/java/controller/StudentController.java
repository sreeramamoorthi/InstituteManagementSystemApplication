package controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dto.ApiResponse;
import dto.StudentRequestDTO;
import dto.StudentResponseDTO;
import jakarta.validation.Valid;
import service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE - POST /api/v1/students
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO created = studentService.createStudent(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", created));
    }

    // READ ONE - GET /api/v1/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    // READ ALL - GET /api/v1/students
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    // READ by course - GET /api/v1/students/course/{course}
    @GetMapping("/course/{course}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByCourse(
            @PathVariable String course) {
        List<StudentResponseDTO> students = studentService.getStudentsByCourse(course);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    // READ by department - GET /api/v1/students/department/{department}
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByDepartment(
            @PathVariable String department) {
        List<StudentResponseDTO> students = studentService.getStudentsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    // SEARCH by name - GET /api/v1/students/search?name=john
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudents(
            @RequestParam String name) {
        List<StudentResponseDTO> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", students));
    }

    // UPDATE - PUT /api/v1/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO updated = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    // DELETE - DELETE /api/v1/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully"));
    }
}