package com.groupa.chickendirectfarm.testdata;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestDataController {
    private final TestData testData;

    public TestDataController(TestData testData) {
        this.testData = testData;
    }

    @GetMapping("/init")
    public ResponseEntity<String> initTestData() {
        testData.createTestData();
        return ResponseEntity.ok("Chicken init BAWK!");
    }
}
