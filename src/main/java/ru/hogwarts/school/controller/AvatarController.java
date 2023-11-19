package ru.hogwarts.school.controller;

import com.sun.net.httpserver.Headers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController("/avatar")
public class AvatarController {
    private final AvatarService avatarService;
    private final int maxFileSizeInB = 300 * 1024;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestParam long studentId,
                                               @RequestParam MultipartFile avatar)
            throws IOException {

        if (avatar.getSize() > maxFileSizeInB) {
            return ResponseEntity.
                    badRequest().
                    body("Изображение слишком большое");
        }

        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().body("Изображение сохранено");
    }

    @GetMapping("{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable long id) {
        Avatar avatar = avatarService.readFromDB(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping("/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.readFromDB(id);

        Path path = Path.of(avatar.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }

    }

}

