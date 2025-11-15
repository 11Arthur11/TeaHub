package dev.parhamziaei.teahub.utils;

import dev.parhamziaei.teahub.dto.internal.ImageInternal;
import dev.parhamziaei.teahub.dto.response.DataResponse;
import dev.parhamziaei.teahub.dto.response.DetailedDataResponse;
import dev.parhamziaei.teahub.dto.response.SimpleResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {}

    public static ResponseEntity<Object> buildFailed(String type, String message, HttpStatus status) {
        SimpleResponse response = new SimpleResponse(false, type, message);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<Object> buildFailed(String type, String message, T data, HttpStatus status) {
        DetailedDataResponse<T> response = new DetailedDataResponse<>(false, type, message, data);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> buildSuccess(String type, String message, HttpStatus status) {
        SimpleResponse response = new SimpleResponse(true, type, message);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<Object> buildSuccess(String type, T data, HttpStatus status) {
        DataResponse<T> response = new DataResponse<>(true, type, data);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Resource> buildImageResponse(ImageInternal imageInternal) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageInternal.getOriginalName() + "\"")
                .contentType(MediaType.valueOf(imageInternal.getMimeType()))
                .contentLength(imageInternal.getSize())
                .body(imageInternal.getImage());
    }

}
