package co.edu.unimagdalena.despeganding;

import org.springframework.boot.SpringApplication;

public class TestDespegandingApplication {

    public static void main(String[] args) {
        SpringApplication.from(DespegandingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
