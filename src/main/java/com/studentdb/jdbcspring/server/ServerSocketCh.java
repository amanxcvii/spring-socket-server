package com.studentdb.jdbcspring.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.studentdb.jdbcspring.controller.StudentController;
import com.studentdb.jdbcspring.entity.student;
import com.studentdb.jdbcspring.messages.MessageSerialization;
import com.studentdb.jdbcspring.messages.serverRequest;

public class ServerSocketCh {
    private final static Logger LOGGER = Logger.getLogger(ServerSocketCh.class.getName());

    private SelectorProvider provider = SelectorProvider.provider();
    private ServerSocketChannel serverSocketChannel;
    private static StudentController controller;
    private DataOutputStream outputStream;
    private static final String CHANNEL_TYPE = "channelType";
    private static final String SERVER_CHANNEL = "serverChannel";
    private HashSet<SocketChannel> clients = new HashSet<>();
    private Selector selector;

    public ServerSocketCh() {
        try {
//        	serverSocketChannel = provider.openServerSocketChannel();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8081));
            serverSocketChannel.configureBlocking(false);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating server socket channel", e);
        }
    }

    public void startListening() {
        try {
            this.selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            for(SocketChannel channels : clients  ) {
            	channels.register(selector, SelectionKey.OP_READ);
            }
            while (true) {
            	if(selector.selectNow() != 0) {
                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                	if (key.isAcceptable()) {
                	    handleAccept(key);
                	}

                	if (key.isReadable()) {
                	    handleRead(key);
                	}
                }
//                todo close the client
                selector.selectedKeys().clear();
            }else {
            	return;
            }
                
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while listening for connections", e);
        }
    }

    private void handleAccept(SelectionKey key) {
        try {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
//            SelectionKey readKey = socketChannel.register(key.selector(), SelectionKey.OP_READ);
            clients.add(socketChannel);
            socketChannel.register(selector, SelectionKey.OP_READ);
            Map<String, String> properties = new HashMap<>();
            properties.put(CHANNEL_TYPE, SERVER_CHANNEL);
//            readKey.attach(properties);
            LOGGER.info("Connection Received from " + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error accepting connection", e);
        }
    }

//    private void handleRead(SelectionKey key) {
//        try {
//            SocketChannel socketChannel = (SocketChannel) key.channel();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            DataInputStream inputStream = new DataInputStream(socketChannel.socket().getInputStream());
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//            byte[] receivedData = byteArrayOutputStream.toByteArray();
//            Object obj = MessageSerialization.deserialize(receivedData);
//            handleRequest(obj, socketChannel);
//        } catch (IOException | ClassNotFoundException e) {
//            LOGGER.log(Level.SEVERE, "Error handling request", e);
//        }
//    }
    private void handleRead(SelectionKey key) {
        try {
        	if(key.channel() instanceof SocketChannel) {
        		SocketChannel socketChannel = (SocketChannel) key.channel();
        		
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            
            int bytesRead;
            if(socketChannel.isOpen()) {
            while ((bytesRead = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, bytesRead);
                buffer.clear();
            }
            byte[] receivedData = byteArrayOutputStream.toByteArray();
            if (receivedData.length > 0) {
                Object obj = MessageSerialization.deserialize(receivedData);
                handleRequest(obj, socketChannel);
            }
            
            if (bytesRead == -1) {
                // Client disconnected
                LOGGER.info("Client disconnected: " + socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }
            }
        	}
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error handling request", e);
        }
    }


    private void handleRequest(Object input, SocketChannel socketChannel) throws IOException, ClassNotFoundException {
        if (input instanceof serverRequest) {
            serverRequest newReq = (serverRequest) input;
            handleServerRequest(newReq, socketChannel);
        }
    }

    private void handleServerRequest(serverRequest newReq, SocketChannel socketChannel) throws IOException {
        try {
        	if(newReq.getKey()!= null) {
            if (newReq.getKey().equals("GET_ALL_STUDENTS")) {
            	LOGGER.log(Level.INFO, "GET_ALL_STUDENTS");;
                StudentController con = new StudentController();
                List<student> list = con.getAllStudentsAsList();
                Vector<student> v = new Vector<>();
                for (student s : list) {
                    v.add(s);
                }
                sendMessage(newReq, v, socketChannel);
            }else if(newReq.getKey().equals("ADD_STUDENT")) {
               	LOGGER.log(Level.INFO, "ADD_STUDENT");;
                StudentController con = new StudentController();
                con.addStudent((student)newReq.getInfo().elementAt(0));
                sendMessage(newReq, newReq.getInfo(), socketChannel);
            }else {
            	LOGGER.log(Level.WARNING, "Key Not Handled : "+newReq.getKey());;
            }
        	}else {
            	LOGGER.log(Level.INFO, "No Key Found");;
        	}
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling server request", e);
        }
    }

//    private void sendMessage(serverRequest req, Vector res, SocketChannel socketChannel) throws IOException {
//        try (DataOutputStream outputStream = new DataOutputStream(socketChannel.socket().getOutputStream())) {
//            serverRequest response = new serverRequest();
//            response.setKey(req.getKey());
//            response.setInfo(res);
//            outputStream.write(MessageSerialization.serialize(response));
//            outputStream.flush();
//            LOGGER.info("Response sent to Client");
//        }
//    }
    private void sendMessage(serverRequest req, Vector res, SocketChannel socketChannel) throws IOException {
    	try {
    		serverRequest response = new serverRequest();
    		response.setKey(req.getKey());
    		response.setInfo(res);
    		byte [] info = MessageSerialization.serialize(response);
    		ByteBuffer buffer = ByteBuffer.allocate(info.length+ 1024);
    		buffer.clear();
    		buffer.put(info);
    		if(socketChannel.isOpen()) {
    		buffer.flip();
    		while(buffer.hasRemaining()) {
    			socketChannel.write(buffer);
    		}
    		LOGGER.info("Response sent to Client" + "Bytes("+info.length+ 1024+")");
    		}
    	} catch (IOException e) {
            LOGGER.severe("IOException occurred while sending message: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Exception occurred while sending message: " + e.getMessage());
        }
    }

}
