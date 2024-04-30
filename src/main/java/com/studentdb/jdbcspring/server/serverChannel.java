package com.studentdb.jdbcspring.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Vector;

import org.springframework.web.servlet.function.ServerRequest;

import com.studentdb.jdbcspring.controller.StudentController;
import com.studentdb.jdbcspring.entity.student;
import com.studentdb.jdbcspring.messages.MessageSerialization;
import com.studentdb.jdbcspring.messages.serverRequest;

import jakarta.websocket.RemoteEndpoint.Async;
import net.bytebuddy.asm.Advice.This;

public class serverChannel {
	SelectorProvider provider = SelectorProvider.provider();
	ServerSocketChannel serverSocketChannel;
	private static StudentController controller;
	SocketChannel socketChannel;
	SocketChannel clientSocketChannel;
	DataOutputStream outputStream;
	
	public serverChannel() {
		try {
			// Open a server socket channel
			this.serverSocketChannel = provider.openServerSocketChannel();
			// Bind the server socket channel to the given port
			serverSocketChannel.bind(new InetSocketAddress(8081));
			// Configure the server socket channel to be non-blocking
			serverSocketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startListening() throws ClassNotFoundException {
		try {
				// Accept incoming connections
				socketChannel = serverSocketChannel.accept();
//				System.out.println("Connection Received");
				if (socketChannel != null) {
					// Handle the accepted connection
					handleConnection(socketChannel);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleConnection(SocketChannel socketChannel) throws ClassNotFoundException {
		// Implement your logic to handle the incoming connection
		System.out.println("Request Received");
		try {
//			ByteBuffer buffer = ByteBuffer.allocate(1024);
//			int bytesRead = socketChannel.read(buffer);
//			if (bytesRead != -1) {
//				buffer.flip();
//				byte[] data = new byte[buffer.limit()];
//				buffer.get(data);
//				// Process received data (e.g., execute a query)
//				String requestData = new String(data);
//				System.out.println("Received: " + requestData);
				// Respond to the client
//				String responseData = "Response from server";
//				ByteBuffer responseBuffer = ByteBuffer.wrap(responseData.getBytes());
//				socketChannel.write(responseBuffer);
			
			if(socketChannel.isOpen()) {
//			ObjectInputStream inputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
//			Object obj = inputStream.readObject();
			DataInputStream inputStream = new DataInputStream(socketChannel.socket().getInputStream());
			outputStream = new DataOutputStream(socketChannel.socket().getOutputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
            while (inputStream.available() > 0) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    // Handle the case when the end of the stream is reached
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] receivedData = byteArrayOutputStream.toByteArray();
			Object obj = MessageSerialization.deserialize(receivedData);
			handleRequest(obj);
//			socketChannel.close(); // Close the channel after communication
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleRequest(Object input) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		if (input instanceof serverRequest) {
			System.out.println("of serverRequest");
			serverRequest newReq = (serverRequest) input;
			handleServerRequest(newReq);
		}
}

	private void handleServerRequest(serverRequest newReq) {
		// TODO Auto-generated method stub
		try {
		if (newReq.getKey().equals("GET_ALL_STUDENTS")){
			StudentController con = new StudentController();
			List<student> list = con.getAllStudentsAsList();
			Vector<student> v  = new Vector<>();
			for(student s : list) {
				v.add(s);
			}
			sendMessage(newReq,v);
		}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("\u001B[31m"+"Error Occured While Handling Request"+"\u001B[0m");
		}

		
	}

	private void sendMessage(serverRequest req,Vector res) throws IOException {
		try {
		serverRequest response = new serverRequest();
		response.setKey(req.getKey());
		response.setInfo(res);
//		AsynchronousByteChannel socket = new SocketChannel
//		try {
//		this.clientSocketChannel = SocketChannel.open();//For Client Side
//		clientSocketChannel.connect(new InetSocketAddress("localhost", 8090));
//		}catch(Exception ex) {
//			System.out.println("Error while creating client connection");
//		}
		if(socketChannel.isConnected()) {
//        ObjectOutputStream outputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
//		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        if (outputStream != null ) {
        	outputStream.write(MessageSerialization.serialize(response));
        	outputStream.flush();
        	outputStream.close();
            System.out.println("Object sent to Client");
        }
		}
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}


}
