package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.ImageStorageProperties;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.exception.custom.service.storage.FileStorageServiceException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaSizeTooLargeException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaTypeNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @TempDir
    Path tempDirectory;

    private FileStorageService storageService;

    @BeforeEach
    void setUp() {
        storageService = new FileStorageService(new ImageStorageProperties(
                tempDirectory.toString(),
                1,
                List.of("image/png", "image/jpeg"),
                List.of("png", "jpg")
        ));
    }

    @Test
    void storesAllowedAttachmentUnderDateBasedDirectory() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                new byte[]{1, 2, 3, 4}
        );

        String storedPath = storageService.storeTicketAttachment(file);

        Path path = Path.of(storedPath);
        assertTrue(path.startsWith(tempDirectory));
        assertTrue(Files.exists(path));
        assertArrayEquals(file.getBytes(), Files.readAllBytes(path));
        assertEquals("png", storageService.getFileExtension(file.getOriginalFilename()));
    }

    @Test
    void rejectsSpoofedOrUnsupportedMediaType() {
        MockMultipartFile spoofed = new MockMultipartFile(
                "file",
                "payload.exe",
                "image/png",
                new byte[]{1}
        );

        assertThrows(MediaTypeNotAllowedException.class, () -> storageService.storeTicketAttachment(spoofed));
    }

    @Test
    void rejectsAttachmentAtOrAboveMaximumSize() {
        MockMultipartFile large = new MockMultipartFile(
                "file",
                "large.png",
                "image/png",
                new byte[1024 * 1024]
        );

        assertThrows(MediaSizeTooLargeException.class, () -> storageService.storeTicketAttachment(large));
    }

    @Test
    void loadsExistingStoredAttachment() throws Exception {
        Path stored = Files.write(tempDirectory.resolve("stored.png"), new byte[]{5, 6});
        TicketMessageAttachment attachment = TicketMessageAttachment.builder()
                .storedName("stored.png")
                .storedPath(stored.toString())
                .build();

        assertTrue(storageService.loadTicketAttachment(attachment).orElseThrow().isReadable());
    }

    @Test
    void missingStoredAttachmentRaisesDomainException() {
        TicketMessageAttachment attachment = TicketMessageAttachment.builder()
                .storedName("missing.png")
                .storedPath(tempDirectory.resolve("missing.png").toString())
                .build();

        assertThrows(FileStorageServiceException.class, () -> storageService.loadTicketAttachment(attachment));
    }
}
