package com.memo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件访问控制器
 * <p>
 * 提供上传文件的 HTTP 访问，用于头像和备忘录图片的展示。
 * </p>
 */
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 根据文件名获取上传目录中的文件
     *
     * @param filename 文件名
     * @param response HTTP 响应
     */
    @GetMapping("/{filename}")
    public void getFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        File file = new File(uploadPath, filename);

        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 根据文件扩展名设置 Content-Type
        String contentType = getContentType(filename);
        response.setContentType(contentType);
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
    }

    /**
     * 根据文件扩展名获取 Content-Type
     */
    private String getContentType(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "application/octet-stream";
        }
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            case "svg" -> "image/svg+xml";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }
}
