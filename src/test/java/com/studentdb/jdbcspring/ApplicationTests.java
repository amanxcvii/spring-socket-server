package com.studentdb.jdbcspring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studentdb.jdbcspring.entity.student;

@SpringBootTest
class ApplicationTests {

	@Autowired
//	private studentService stuServ;
	
	@Test
	void saveStudent() {
		student stu = new student();
		stu.setName("demoname");
		stu.setAge(45);
		stu.setSex("M");
		
	}

}
