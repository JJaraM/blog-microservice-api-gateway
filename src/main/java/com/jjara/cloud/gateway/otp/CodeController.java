/*package com.jjara.cloud.gateway.otp;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController {

    private final GoogleAuthenticator gAuth;
    private final CredentialRepository credentialRepository;

    @SneakyThrows
    @RequestMapping(value = "/generate/{username}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generate(@PathVariable String username, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(getQRCodeImage(username, 200, 200));
    }

    @SneakyThrows
    public byte[] getQRCodeImage(String username, int width, int height) {
        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("my-demo", username, key);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    @PostMapping("/validate/key")
    public Validation validateKey(@RequestBody ValidateCodeDto body) {
        return new Validation(gAuth.authorizeUser(body.getUsername(), body.getCode()));
    }

    @GetMapping("/scratches/{username}")
    public List<Integer> getScratches(@PathVariable String username) {
        return getScratchCodes(username);
    }

    private List<Integer> getScratchCodes(@PathVariable String username) {
        return credentialRepository.getUser(username).getScratchCodes();
    }

    @PostMapping("/scratches/")
    public Validation validateScratch(@RequestBody ValidateCodeDto body) {
        List<Integer> scratchCodes = getScratchCodes(body.getUsername());
        Validation validation = new Validation(scratchCodes.contains(body.getCode()));
        scratchCodes.remove(body.getCode());
        return validation;
    }
}*/
