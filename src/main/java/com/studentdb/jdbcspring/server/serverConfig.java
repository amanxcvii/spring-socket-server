//package com.studentdb.jdbcspring.server;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.channels.ServerSocketChannel;
//
//@Configuration
//public class serverConfig {
//	@Bean
//	public ServerSocketChannel serverSocketChannel() throws IOException {
//		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//		serverSocketChannel.bind(new InetSocketAddress(8080));
//		serverSocketChannel.configureBlocking(false);
//		return serverSocketChannel;
//	}
//}
