package knulions.mealog.global.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {

        ClassPathResource resource = new ClassPathResource("glass-sight-427116-t8-aa2e289fd170.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        String projectId = "glass-sight-427116-t8";

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build().getService();
    }

}
