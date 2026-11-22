package service;



import java.util.List;

import dto.StudentRequestDTO;
import dto.StudentResponseDTO;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    StudentResponseDTO getStudentById(Long id);

    List<StudentResponseDTO> getAllStudents();

    List<StudentResponseDTO> getStudentsByCourse(String course);

    List<StudentResponseDTO> getStudentsByDepartment(String department);

    List<StudentResponseDTO> searchStudentsByName(String name);

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO);

    void deleteStudent(Long id);
}