package com.sparta.bluemoon.util;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





@Configuration
public class JasyptConfig {

//    @Value("${jasypt.encryptor.password}")
//    private String password;

	@Bean("jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {
		System.out.println("------------------system env---------------");
		System.out.println(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));
		System.out.println(System.getenv("JASYPT_ENCRYPTOR_REPEAT"));
		System.out.println("------------------system env---------------");
//        String password = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD")); // 암호화할 때 사용하는 키
		config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
		config.setKeyObtentionIterations(System.getenv("JASYPT_ENCRYPTOR_REPEAT")); // 반복할 해싱 회수
		config.setPoolSize("1"); // 인스턴스 pool
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
		config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
		config.setStringOutputType("base64"); //인코딩 방식

		encryptor.setConfig(config);
		return encryptor;
	}
}
