package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.OutputStream;

@Data
@AllArgsConstructor
public class File {

    private OutputStream outputStream;
    private String fileName;
}
