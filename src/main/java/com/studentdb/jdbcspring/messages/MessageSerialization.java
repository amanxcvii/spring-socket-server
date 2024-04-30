package com.studentdb.jdbcspring.messages;
import java.io.*;
public class MessageSerialization {

	    // Serialize CustomObject to byte array
	    public static byte[] serialize(Object obj) throws IOException {
	        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
	        objectStream.writeObject(obj);
	        objectStream.close();
	        return byteStream.toByteArray();
	    }

	    // Deserialize byte array to CustomObject
	    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
	        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
	        Object obj = (Object) objectStream.readObject();
	        objectStream.close();
	        return obj;
	    }
	}
 
