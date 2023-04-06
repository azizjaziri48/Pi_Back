package com.example.pi_back.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QrCodeService {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/img/QRCode.png";
    public String get_link_string(){
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            String baseip=datagramSocket.getLocalAddress().getHostAddress();
            String link = "http://"+baseip+":8083/pi_back";
            return link;

        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }
    public void generateQRCodeImage(int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(this.get_link_string(), BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
        System.out.println(path);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

}
