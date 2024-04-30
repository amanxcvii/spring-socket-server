package com.studentdb.jdbcspring.repo;

import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import com.studentdb.jdbcspring.entity.student;

@Component
public class studentdata {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private final String url = "jdbc:mysql://localhost:3306/school";
    private final String username = "root";
    private final String password = "Qwerty@123";
    public ResultSet resultSet = null;
    private sendToClient send = new sendToClient();

    public void fetchDataFromMySQL() {
        try {
            connection = DriverManager.getConnection(url,username, password);
            System.out.println("Connected Successfully");
            getAllData();
			setResultSet();
//            getConnection();
        } catch (SQLException e) {
             // Capture the stack trace as a string
            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
            String stackTrace = sw.toString();

            // Show the stack trace in a dialog box
            JOptionPane.showMessageDialog(null, "An exception occurred:\n" + e.getMessage() + "\n\n" + stackTrace, "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

	private ResultSet getAllData() throws SQLException {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM students";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        System.out.println("Data fetched");
//     // Fetch data by column index (starting from 1)
//        while (resultSet.next()) {
//            // Fetch data by column index (starting from 1)
//            int employeeId = resultSet.getInt(1);
//            // Fetch data by column name
//            String employeeName = resultSet.getString("name");
//
//            // Process the fetched data for the first row
//            System.out.println("Employee ID: " + employeeId + ", Employee Name: " + employeeName);
//        }
        return resultSet;
	}

    /**
	 * @return the resultSet
	 */
	public void setResultSet() {
        Vector <student> v = new Vector<>();
        try {
        while (resultSet.next()) {
        	student stu = new student();
        	stu.setStudentId(resultSet.getInt("regdno"));
        	stu.setName(resultSet.getString("name"));
        	stu.setAge(resultSet.getInt("regdno"));
        	stu.setSex(resultSet.getString("sex"));
        	stu.setStdClass(resultSet.getString("class"));
        	stu.setFname(resultSet.getString("fname"));
        	stu.setMname(resultSet.getString("mname"));
        	stu.setAddress(resultSet.getString("address"));
        	v.add(stu);
        }
        if(send.getData() != null) {
        	send.getData().clear();
        }
        send.setData(v);
        }catch(SQLException e) {
        	e.printStackTrace();
        }
	}






	private void getConnection() {
		// Try block to check for exceptions
				try {

					// Creating an object of ServerSocket class
					// with the custom port number - 80
					ServerSocket mySsocket = new ServerSocket(8080);

					// Display commands for better readability
					System.out.println("Server started");
					System.out.println("Sending to a client ...");


					// Here it will wait for any client which wants
					// to get connected to this server

					// Establishing a connection
					// using accept() method()
					Socket socket = mySsocket.accept();

					// Display message
					System.out.println(
							"Client accepted through the port number: "
									+ mySsocket.getLocalPort());
					System.out.println("I Have Received the First Message from	 Client");
		            System.out.println("Connected to:" + socket.getInetAddress());
					// getLocalPort() function returning the port
					// number which is being used



					System.out.println("Successfully Connected with the Client");
					try {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(send.getData());
					System.out.println("Sent Data Successfully");
					oos.close();
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
//					mySsocket.close();
				}

				// Catch block to handle for exceptions
				catch (Exception e) {
					System.out.println("Fail to Connect with client");
					// Simply return/exit
					return;
				}
	}

	public void show(student stu) {

	}

}
