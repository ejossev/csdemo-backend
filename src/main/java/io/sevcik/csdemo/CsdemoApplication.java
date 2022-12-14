package io.sevcik.csdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan("io.sevcik.csdemo.models")
public class CsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsdemoApplication.class, args);
    }

}
