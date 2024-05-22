package com.smartlink.archive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = {"com.smartlink.archive.infrastructure.db.mapper"})
@EnableScheduling
public class DataArchiveToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataArchiveToolApplication.class, args);
    }

}
