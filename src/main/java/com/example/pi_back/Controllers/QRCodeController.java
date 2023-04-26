package com.example.pi_back.Controllers;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class QRCodeController {

    @GetMapping("/qrcode")
    public void getQRCode(HttpServletResponse response) throws IOException {
        // Récupération du code QR depuis la chaîne encodée en base64
        String encodedQRCode = "iVBORw0KGgoAAAANSUhEUgAAAH0AAAB9AQAAAACn+1GIAAABcklEQVR4Xt2UPdKEIAxAs0Nh516AGa5Bx5X0AqteAK9E5zWYyQXcjsIxXxD3r1nytctQ6CtC8kgA+lwJfgokAGd2t3VNhPwnAbRbDQ3uFvlbBmLrYFjwvuBoxcDqfkEP/wEOvYP+PWgFcKZ8plrdW+oVAGDR5/0SVAHH8i6CPa3XQYIW1JS2G3CJQhCHwBcVO4frWW0NEI2NviyG0gaNEKgpwBBwBd2VGHWAHKMPxoMpCusg6Qspb6Gz3EQyQNyhuDe4ulh8CICaieZE9wRduSgBWMFMZLzVlyAESMFwgGHZHj5qIBFXBmBWG9vDugCwP31Yh/YUVAVqhJitOzOXpqsDmgONlv3F2+FDArw1xMfyRT1Tr4B4XXDmjrO0OiloG8zWuTghyIPGww1Doqm0QxXkyWbrtAPOUkA8cGz9nq0IQX7YBlJ88rVYFwGcUuysflYrANyqmPdpvQpYhpqW2NPDeh0cb2GzXYN5CfoOPtdPgT/gPoKAnDAI1AAAAABJRU5ErkJggg==";
        byte[] decodedBytes = Base64.decodeBase64(encodedQRCode);

        // Conversion en image
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));

        // Écriture de l'image dans la réponse HTTP
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        byte[] imageBytes = bos.toByteArray();

        response.setContentType("image/png");
        response.setContentLength(imageBytes.length);
        response.getOutputStream().write(imageBytes);
    }

}