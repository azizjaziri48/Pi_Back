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
        String encodedQRCode = "iVBORw0KGgoAAAANSUhEUgAAAPoAAAD6AQAAAACgl2eQAAABsElEQVR4Xu2WXWrEMAyEBTlAjuSr+0g5gMGdH3frTWH72oGIlE2lLw+DRrJrfo5e98wtHsDxAI4HcAjohTjnOPs4J9+rDf1kAfhz9bgaGcIrHwRA1Cszrzou/NvrTASYQemgZKrOBCZKeJhny/KAKctBI0ZGDJU6HwRo/PvYn/fxTwBWQCOmZlQralyRA1AmdGGDcXDkN723LIBVuc5mE6MJSgIw9WCYZLNUKp6PL5kRQOchwmH3wxIHZ19iCcDQKq51oHT1qwwnATxE7DovgSbvZQFMlqTZZuM+/hnA0kinNXWKL6CjAHqseBS6TdppF58ogAKnJsViS3prs1wCoHPEVyw1y/bjV1GAZqR/y4T9OoFNZgKgX20tbTM2TueLCymAZQJgZrhfdJ1qOQDUlW6MU1esKY275SKAyeASszT4rTQ+YQCHvdgym43nY7l3OYBDLWPXmqZm+TAH6FSllVWy2SGYeyAKmJp9MBJIwF9lAYMGYxXYT3W/w+QAmhSUGl2nUUoELG3dtd6XWAQwaTnOu6UVW5YHQCCnZlvF/OrXfvjfwKd4AMcDOB7A8TfwBcVuznTEUkmOAAAAAElFTkSuQmCC";
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