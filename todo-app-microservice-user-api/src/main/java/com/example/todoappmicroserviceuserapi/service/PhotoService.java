package com.example.todoappmicroserviceuserapi.service;


import com.example.todoappmicroserviceuserapi.exception.FileStorageException;
import com.example.todoappmicroserviceuserapi.exception.ItemNotFoundException;
import com.example.todoappmicroserviceuserapi.model.AuthUser;
import com.example.todoappmicroserviceuserapi.model.Photo;
import com.example.todoappmicroserviceuserapi.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    public Photo storeFile(MultipartFile file, AuthUser user) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                try {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                } catch (FileStorageException e) {
                    throw new RuntimeException(e);
                }
            }
            photoRepository.deleteByUser(user);
            Photo dbFile = new Photo(user,fileName, file.getContentType(), file.getBytes());

            return photoRepository.save(dbFile);
        } catch (IOException ex) {
            try {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Transactional
    public Photo getFile(String fileId) {
        try {
            return photoRepository.findByIdAndDeleted(Long.valueOf(fileId), false)
                    .orElseThrow(() -> new ItemNotFoundException("File not found with id " + fileId));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
