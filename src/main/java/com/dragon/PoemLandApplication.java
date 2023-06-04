package com.dragon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.security.SecureRandom;

@SpringBootApplication
@MapperScan("com.dragon.mapper")
public class PoemLandApplication {

	public static void main(String[] args) {
//		System.setProperty("javax.net.debug", "all");
		SpringApplication.run(PoemLandApplication.class, args);
	}

}
