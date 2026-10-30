package com.marketplace;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.ByteArrayResource;

@RestController
@RequestMapping("/api")
class ProductImageController {
    private static final long MAX_BYTES = 12L * 1024 * 1024;
    private final Path imageRoot;

    ProductImageController(@Value("${app.product-image-dir:/data/product-images}") String imageRoot) {
        this.imageRoot = Path.of(imageRoot).toAbsolutePath().normalize();
    }

    @PostMapping(value = "/supplier/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Map<String, Object>> upload(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a product photo.");
        byte[] bytes = file.getBytes();
        String extension = validateAndGetExtension(bytes);
        var image = ImageIO.read(new ByteArrayInputStream(bytes));
        String filename = UUID.randomUUID() + "." + extension;
        Files.createDirectories(imageRoot);
        Files.write(imageRoot.resolve(filename), bytes);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "url", "/api/product-images/" + filename,
                "width", image.getWidth(), "height", image.getHeight(), "size", bytes.length));
    }

    static String validateAndGetExtension(byte[] bytes) {
        if (bytes.length == 0 || bytes.length > MAX_BYTES)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a product photo under 12 MB.");
        boolean png = bytes.length >= 8 && (bytes[0] & 0xff) == 0x89 && bytes[1] == 0x50
                && bytes[2] == 0x4e && bytes[3] == 0x47 && bytes[4] == 0x0d && bytes[5] == 0x0a
                && bytes[6] == 0x1a && bytes[7] == 0x0a;
        boolean jpeg = bytes.length >= 3 && (bytes[0] & 0xff) == 0xff && (bytes[1] & 0xff) == 0xd8
                && (bytes[2] & 0xff) == 0xff;
        String extension = png ? "png" : jpeg ? "jpg" : null;
        if (extension == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload a JPEG or PNG product photo.");
        java.awt.image.BufferedImage image;
        try { image = ImageIO.read(new ByteArrayInputStream(bytes)); }
        catch (IOException invalid) { image = null; }
        if (image == null || Math.max(image.getWidth(), image.getHeight()) < 600
                || (long) image.getWidth() * image.getHeight() > 40_000_000L)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use a clear product photo at least 600 pixels on its longest side.");
        return extension;
    }

    @GetMapping("/product-images/{filename:[a-f0-9-]+\\.(jpg|png)}")
    ResponseEntity<ByteArrayResource> read(@PathVariable String filename) throws IOException {
        Path image = imageRoot.resolve(filename).normalize();
        if (!image.startsWith(imageRoot) || !Files.isRegularFile(image))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product photo not found.");
        MediaType type = filename.endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(type).cacheControl(CacheControl.maxAge(java.time.Duration.ofDays(30)).cachePublic())
                .body(new ByteArrayResource(Files.readAllBytes(image)));
    }
}
