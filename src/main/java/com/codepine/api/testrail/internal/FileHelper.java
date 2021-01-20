package com.codepine.api.testrail.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {

    public static void writeFileInOStream(OutputStream outputStream, File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
                outputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    public static void createFileFromIStream(InputStream inputStream, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
