package com.example.demo;

import com.example.demo.service.MyServiceImpl;
import com.example.demo.service.api.MyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	MyServiceImpl myService;

	@Test
	public void contextLoads() {
		assertNotNull(myService);
	}

}
