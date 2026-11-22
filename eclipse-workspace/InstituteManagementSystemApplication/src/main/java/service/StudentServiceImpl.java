package service;



import dto.StudentRequestDTO;
import dto.StudentResponseDTO;
import entity.Student;
import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import repository.StudentRepository;
import service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException(
                    "A student with email '" + requestDTO.getEmail() + "' already exists");
        }

        Student student = mapToEntity(requestDTO);
        Student saved = studentRepository.save(student);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return mapToResponseDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByCourse(String course) {
        return studentRepository.findByCourseIgnoreCase(course)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> searchStudentsByName(String name) {
        return studentRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student existingStudent = findStudentOrThrow(id);

        if (!existingStudent.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException(
                    "A student with email '" + requestDTO.getEmail() + "' already exists");
        }

        existingStudent.setFirstName(requestDTO.getFirstName());
        existingStudent.setLastName(requestDTO.getLastName());
        existingStudent.setEmail(requestDTO.getEmail());
        existingStudent.setPhoneNumber(requestDTO.getPhoneNumber());
        existingStudent.setCourse(requestDTO.getCourse());
        existingStudent.setDepartment(requestDTO.getDepartment());
        existingStudent.setDateOfBirth(requestDTO.getDateOfBirth());
        if (requestDTO.getEnrollmentDate() != null) {
            existingStudent.setEnrollmentDate(requestDTO.getEnrollmentDate());
        }

        Student updated = studentRepository.save(existingStudent);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    // ---------- helper methods ----------

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    private Student mapToEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setCourse(dto.getCourse());
        student.setDepartment(dto.getDepartment());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setEnrollmentDate(dto.getEnrollmentDate());
        return student;
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getCourse(),
                student.getDepartment(),
                student.getDateOfBirth(),
                student.getEnrollmentDate(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}