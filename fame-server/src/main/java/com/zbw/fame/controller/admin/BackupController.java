package com.zbw.fame.controller.admin;

import com.zbw.fame.service.BackupService;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 备份 Controller
 *
 * @author zzzzbw
 * @since 2021/9/21 00:03
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/backup")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BackupController {
    private final BackupService backupService;

    @PostMapping("import/{articleId}")
    public RestResponse<RestResponse.Empty> importArticle(@PathVariable Integer articleId,
                                                          @RequestPart("file") MultipartFile file) {
        backupService.importArticle(file, articleId);
        return RestResponse.ok();
    }

    @PostMapping("export/{articleId}")
    public ResponseEntity<Resource> exportArticle(@PathVariable Integer articleId) throws UnsupportedEncodingException {
        Resource file = backupService.exportArticle(articleId);
        String fileName = URLEncoder.encode(file.getFilename(), "UTF-8");
        String type = new MimetypesFileTypeMap().getContentType(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, type)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(file);
    }
}
