package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    private final String avatarsDir;
    private final StudentService students;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentService students, AvatarRepository avatarRepository,
                             @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.students = students;
        this.avatarRepository = avatarRepository;
        this.avatarsDir = avatarsDir;
    }

    @Override
    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        Student student = students.read(studentId);

        Path filePath = saveToFile(student, avatarFile);

        saveToDb(filePath, avatarFile, student);
    }

    private Path saveToFile(Student student, MultipartFile avatarFile) throws IOException {
        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);

        ) {
            bis.transferTo(bos);

        }
        return filePath;

    }

    private void saveToDb(Path filePath, MultipartFile avatarFile, Student student) throws IOException {
        Avatar avatar = avatarRepository.findByStudent_id(student.getId()).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        avatarRepository.save(avatar);

    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public Avatar readFromDB(long id) {
        return avatarRepository.findByStudent_id(id)
                .orElseThrow(() -> new AvatarNotFoundException("Аватар не найден"));
    }

    @Override
    public File readFromFile(long id) throws IOException{
        Avatar avatar = readFromDB(id);
        Path path = Path.of(avatar.getFilePath());

        return new File(path.toString());

    }
    @Override
    public Page<Avatar> getAllAvatars(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return avatarRepository.findAll(paging);

    }
}