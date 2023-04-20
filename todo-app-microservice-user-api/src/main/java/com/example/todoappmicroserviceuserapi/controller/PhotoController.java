package com.example.todoappmicroserviceuserapi.controller;


import com.example.todoappmicroserviceuserapi.config.security.AuthUserDetails;
import com.example.todoappmicroserviceuserapi.model.Photo;
import com.example.todoappmicroserviceuserapi.response.UploadFileResponse;
import com.example.todoappmicroserviceuserapi.response.WebResponse;
import com.example.todoappmicroserviceuserapi.service.PhotoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("v1/profile")

public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }


    @PostMapping(value = "/uploadProfilePicture", consumes = {
            "multipart/form-data"
    })
    public ResponseEntity<WebResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) {

        AuthUserDetails userDetails = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Photo dbFile = photoService.storeFile(file, userDetails.authUser());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();
        System.out.println(fileDownloadUri);
        return ResponseEntity.ok(new WebResponse<>(new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize())));
    }


    @GetMapping(value = "/downloadFile/{fileId}", produces = "application/octet-stream")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileId) {
        try {
            Photo dbFile = photoService.getFile(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                    .body(new ByteArrayResource(dbFile.getData()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
