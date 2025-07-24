package mc.monacotelecom.tecrep.equipments.utils;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmInternalException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "incoming", name = "files.processed-directory")
public class IncomingFileResolver {

    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Value("${incoming.files.processed-directory}")
    private String incomingFilesDirectory;

    public File getImportFile(final String fileName) {
        return new File(incomingFilesDirectory, fileName);
    }

    public MultipartFile getMultipartFile(final File file) {
        MultipartFile multipartFile;
        try {
            var fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
            multipartFile = new CommonsMultipartFile(fileItem);
        } catch (IOException e) {
            throw new EqmInternalException(localizedMessageBuilder, "Failed to transform File " + file.getName() + " to MultipartFile");
        }
        return multipartFile;
    }
}
