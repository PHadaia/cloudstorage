package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.repository.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int createFile(File file) {
        return fileMapper.insertFile(file);
    }

    public File getFile(Integer fileId) {
        return fileMapper.findFile(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.findFilesByUserId(userId);
    }
}
