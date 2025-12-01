package com.groupa.chickendirectfarm;

import org.springframework.boot.SpringApplication;

public class TestChickenDirectFarmApplication {

    public static void main(String[] args) {
        SpringApplication.from(ChickenDirectFarmApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
