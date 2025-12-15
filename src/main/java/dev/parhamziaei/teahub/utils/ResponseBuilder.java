package dev.parhamziaei.teahub.utils;

import dev.parhamziaei.teahub.dto.internal.ImageInternal;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.DetailedDataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {}

    public static ResponseEntity<SimpleResponse> buildFailed(ResponseType responseType, String message, HttpStatus status) {
        SimpleResponse response = new SimpleResponse(false, responseType.name(), message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<SimpleResponse> buildError(String message, HttpStatus status) {
        SimpleResponse response = new SimpleResponse(false, ResponseType.ERROR.name(), message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<SimpleResponse> buildSuccess(ResponseType responseType, String message, HttpStatus status) {
        SimpleResponse response = new SimpleResponse(true, responseType.name(), message);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<DataResponse<T>> buildSuccess(ResponseType responseType, T data, HttpStatus status) {
        DataResponse<T> response = new DataResponse<>(true, responseType.name(), data);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<DetailedDataResponse<T>> buildSuccess(ResponseType responseType, String message, T data, HttpStatus status) {
        DetailedDataResponse<T> response = new DetailedDataResponse<>(true, responseType.name(), message, data);
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
