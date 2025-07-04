//package com.example.cake.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class FirebaseConfig {
//
//    @PostConstruct
//    public void initFirebase() throws IOException {
//        FileInputStream serviceAccount =
//                new FileInputStream("src/main/resources/serviceAccountKey.json");
//
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//            System.out.println("✅ Firebase đã được khởi tạo thành công!");
//        }
//    }
//}
package com.example.cake.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() throws IOException {
        InputStream serviceAccount;

        // đọc biến môi trường
        String externalPath = System.getenv("FIREBASE_CREDENTIAL_PATH");

        if (externalPath != null && !externalPath.isEmpty()) {
            // Nếu chạy Docker (có biến môi trường), load file ngoài container
            System.out.println("🚀 Firebase đang chạy với file bên ngoài Docker: " + externalPath);
            serviceAccount = new FileInputStream(externalPath);
        } else {
            // Nếu chạy local, load từ resources
            System.out.println("🚀 Firebase đang chạy với file trong resources.");
            serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase đã được khởi tạo thành công!");
        }
    }
}

