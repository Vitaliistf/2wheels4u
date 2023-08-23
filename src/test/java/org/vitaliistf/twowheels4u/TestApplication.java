package org.vitaliistf.twowheels4u;

import org.springframework.boot.SpringApplication;
import org.vitaliistf.twowheels4u.repository.ContainersConfig;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
