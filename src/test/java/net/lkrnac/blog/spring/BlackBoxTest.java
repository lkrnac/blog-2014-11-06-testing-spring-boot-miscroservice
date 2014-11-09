package net.lkrnac.blog.spring;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class BlackBoxTest {
	private static final String RESOURCE_URL = "http://localhost:8080/greeting";

	@Test
	public void contextLoads() throws InterruptedException, IOException {
		Process process = null;
		Greeting actualGreeting = null;
		try {
			process = new ProcessExecutor().execute("gs-rest-service.jar");

			RestTemplate restTemplate = new RestTemplate();
			waitForStart(restTemplate);

			actualGreeting = restTemplate.getForObject(RESOURCE_URL, Greeting.class);
		} finally {
			process.destroyForcibly();
		}
		Assert.assertEquals(new Greeting(2L, "Hello, World!"), actualGreeting);
	}

	private void waitForStart(RestTemplate restTemplate) {
		while (true) {
			try {
				Thread.sleep(500);
				restTemplate.getForObject(RESOURCE_URL, String.class);
				return;
			} catch (Throwable throwable) {
				// ignoring errors
			}
		}
	}
}
