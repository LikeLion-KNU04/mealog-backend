package knulions.mealog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MealogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealogApplication.class, args);
	}

}
