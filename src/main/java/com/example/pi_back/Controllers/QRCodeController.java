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
        String encodedQRCode = "iVBORw0KGgoAAAANSUhEUgAAAH0AAAB9AQAAAACn+1GIAAABfklEQVR4Xt3UQa6DIBAG4DEu3OkFSLwGO670vIDaC9ArsfMaJFxAdyyI834emrSbN3Rb0jT2S4rD8APx+4j0VRCJdFgNLZun/KsG+DREJqwU8FwHvtftwWmG6Q/g4VpLH0HqifoPAJX6KaaJX0oXIPfD5s9rg/6FPIKl8Xl3XYZIs/aDU802XpWKwNRgDp2mGPZaUKRpiKMlv7hKQBDa3aRl40clRF47bBTWp4arpyL4xqnBpV6rqfRDBrbGz0b9aF5NHTAS5xc0w9BPbmgFRPx7RLF719quEvhwvHfMrr37IUK7msAOG6v6MocIHI4NiRhR8qNESgZF1LKjJqa5zCFCzHt76nwZzKUfIjCfGoAEqelvcRWgGkQ1+gXfWx3EYLuEo3DicJfNFiEfNLXENDi2V6US4GQbPrBERLVMKgOfJjw3j/isVxxE8L3xZAB0XSg1oPMB3Q1z6UcVtGsX8PL9fq0E2Cg165yI5b4cJcBdyE8kCHG4GyTA+/gq+AUqwWTgmXXKXAAAAABJRU5ErkJggg==";
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