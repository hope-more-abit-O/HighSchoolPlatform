package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.service.NotificationService;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The type Notification service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";
    @Override
    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"highschoolvn-dev\",\n" +
                    "  \"private_key_id\": \"3e1ae34c55956ffd00b1bcff35181e85a04da1fe\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC3Yv4L8xBRkMoO\\ntuoz7lUdBCsz+zJUKgUlseDs5HslNYekzmRPH1bzQyJ3BcNmqtg0WdYVQdKz6mDC\\n9KWV4sRaod4yd3ektZ3/z0IrrAvDJzETqccF42sz9WO4Ga8Q+gsLF0h4Pl1u9ivw\\ngjYFjBeKqcWOz1jCwXNuqaY2AhX7MGxYI5JOp/8j52S/ulUXIiTDSxDWYUSA2Nrg\\nRB6PFdFdGyWAVSaik2dJpr3O9kvjqYzt7XAXdoNYMcqOw89z4Psz4zvE/owIHsoT\\nG/PPtmGU5ODkSgqBhURhzeGEdlmIEaBWzDRmUgqc0qy7dcWREjbyd3jgxst9PWI6\\nBctREh5rAgMBAAECggEAVAiBn1onHSk63dlForxQtjYl0fEGk/iZWTeUeRkNsPRB\\njuygnH0g6+HUljPG4XHiFsMfzafW9GivJEbU5cT+8wT/e6Kw6YHrv7/qc3aoASoh\\nXABNWc5nXmzQ4Bis1xutGcz2gVsal3Eom1IfjRUv6HN5OsKaysu4qHoG1vLHvc0h\\nFJKG0IC6khn+5nDaW0mewB/Rvg8YNhR2MHqvwp4z+VyqQY/fFAvGtKmC3wUefX+i\\nUSo1JI2SN3G3yt2p3TG9D/rpGipOO1t9ZQe73g41YCqIWYc4C1OF7ysfKKRD1sTS\\noY5zrd6Y1crQQEl400+saj3gnSMuw3Ep8s/VdxgYIQKBgQDZ+VmtehKGjPeBt/+N\\nhZMqsUnjayl8OqNcqzfnjMqe414YviViQkkwKpzyjkseCMjVw07jX4ggu1gSDdLa\\n7uoxWAGzxthf0/JirW94UQi7tD1cnBqwPy+S6ff3f2vbFaCdijseh1g1I/tBFIW6\\nqlEYMIKe3hMHaFWWjz9BxwUT2wKBgQDXYPuY2d96+Cm3mkBRZoKFrbv5+W8WTmdg\\nT+01ikb7I1ZDrhw5MmmCzofiWXvQANPyhBRqaMiOKhOrHX2AVNIl3dMmT0GpL0qQ\\n1sarrWAAvuxxDwPA1q6k+Une1miBvujJHnGwBph185BEFJUcSxNDyfVfcd9NMX0i\\nJgrBtT1ssQKBgGI9OIawCuNray2ioOHGvvIx3yxJIvRjdTcRz+tjD/sWXFVodW7Y\\nkyXLPtsu+Hn72ZBGKyrc5nxiEypn4o1bul8s2++Lgf+GhaqloiSm2PnIHRO9I2Yl\\nxCpBmsmIOzMNh0sZftWeiEnDAr1tI2xi0JPHYUd6rTbcdh6aRxc9PHObAoGASc/W\\n6d9hE4eQ5Hdcq4jyGlYcIeM2CvL7wqohpUOqZ88IdVFqIsAAIPAe6Ze5MuPVvzs8\\ns+sNMIuDChWlOSHYk1meT3bCDfVDHLg+hf9480h7R5PZuXDZkXhNtKzOTAjdRm/E\\njor0HRhHzM0w/O1NwOGp0ff4iz/2noljDlfKoUECgYAME66gUuDuJlYaJAFbs0nt\\nKpenWlQeDR/x7n2KRv0PPb0/DAptc/ozVo5gRsCWBs720bTi7SAMnf7YXvurTB/l\\nFwdalGkWzlH8x1bAS2m5iRpnPdZDGHSG4L/bEIR55wTjfaB1lxANC8rmmIBKXhBK\\nkdaliHf3exX4sO80T/nJsg==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-ktpxs@highschoolvn-dev.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"110253183850649138221\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ktpxs%40highschoolvn-dev.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            log.info("getAccessToken: " , e.getLocalizedMessage());
            return null;
        }
    }
}
