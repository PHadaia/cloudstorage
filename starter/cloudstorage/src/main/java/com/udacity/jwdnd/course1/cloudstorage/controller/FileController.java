package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping()
    public String filesView(@ModelAttribute("multiPartFile") MultipartFile multipartFile) {
        return "files";
    }

    @PostMapping(params="action=save")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, @ModelAttribute("files") List<File> files, Principal principal, Model model) {
        File tempFile;
        try {
            tempFile = new File(
                    null,
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    "" + multipartFile.getSize(),
                    userService.getUserByUsername(principal.getName()).getUserId(),
                    multipartFile.getBytes()
            );
        } catch (IOException ioException) {
            model.addAttribute("uploadSuccess", false);
            return "files";
        }

        fileService.createFile(tempFile);
        files.add(tempFile);
        model.addAttribute("uploadSuccess", true);
        return "files";
    }

    @GetMapping(params="action=view")
    public String downloadFile(@ModelAttribute("file") File file, @ModelAttribute("files") List<File> files, HttpServletResponse response, Model model) {
        for(File f : files) {
            if(f.getFileId().equals(file.getFileId())) {
                File responseFile = fileService.getFile(f.getFileId());
                response.setContentType("application/octet-stream");
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename = " + responseFile.getFileName();
                response.setHeader(headerKey, headerValue);
                try {
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(f.getFileData());
                    outputStream.close();
                } catch (IOException exception) {
                    return "redirect:/error";
                }
            }
        }
        model.addAttribute("message", "Successfully downloaded file");
        return "files";
    }

    @PostMapping(params="action=delete")
    public String deleteFile(@ModelAttribute("multiPartFile") MultipartFile multipartFile, @ModelAttribute("file") File file, @ModelAttribute("files") List<File> files, Model model) {
        files.removeIf(n -> Objects.equals(n.getFileId(), file.getFileId()));
        fileService.deleteFile(file.getFileId());
        model.addAttribute("message", "Successfully deleted file");
        return "files";
    }

    @ModelAttribute("files")
    public List<File> allFiles(@ModelAttribute("multiPartFile") MultipartFile multipartFile, @ModelAttribute("file") File file, Principal principal, Model model) {
        model.addAttribute("files", fileService.getFilesByUserId(userService.getUserByUsername(principal.getName()).getUserId()));
        return fileService.getFilesByUserId(userService.getUserByUsername(principal.getName()).getUserId());
    }
}
