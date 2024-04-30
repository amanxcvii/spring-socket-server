package com.studentdb.jdbcspring.repo;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import com.studentdb.jdbcspring.entity.student;

@Component
public class StudentRepository {

	Connection connection = null;
    PreparedStatement preparedStatement = null;
    final String url = "jdbc:mysql://localhost:3306/school";
    final String username = "root";
    final String password = "Qwerty@123";
    ResultSet resultSet = null;
    List list = null;
    
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url,username, password);
            System.out.println("Connected Successfully");
            return this.connection;
        } catch (SQLException e) {
            StringWriter sw = new StringWriter();
            String stackTrace = sw.toString();
            JOptionPane.showMessageDialog(null, "An exception occurred:\n" + e.getMessage() + "\n\n" + stackTrace, "Exception", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

	public List<student> getAllData() throws SQLException {
		String query = "SELECT * FROM students";
        this.preparedStatement = getConnection().prepareStatement(query);
        this.resultSet = preparedStatement.executeQuery();
        System.out.println("Data fetched");
        Vector  v = new Vector<>();
        List<student> list = new ArrayList<>();
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
        if(this.list != null) {
        	list.clear();
        }
        list.addAll(v);
        return list;
        }catch(SQLException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public void save(student stu) throws SQLException {
		String query = "INSERT INTO STUDENTS VALUES "
				+ "(?,?,?,?,?,?,?);";
        this.preparedStatement = getConnection().prepareStatement(query);
        preparedStatement.setInt(1,stu.getStudentId());
        preparedStatement.setString(2,stu.getName());
        preparedStatement.setInt(3,stu.getAge());
        preparedStatement.setString(4,stu.getSex());
        preparedStatement.setString(5,stu.getFname());
        preparedStatement.setString(6,stu.getMname());
        preparedStatement.setString(7,stu.getAddress());
	}

}
