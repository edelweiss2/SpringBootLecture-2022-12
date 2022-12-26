package com.mulcam.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.mulcam.demo.entity.User;

@Mapper
public interface UserDao {
	
	@Select("select * from users where isDeleted=0")
	List<User> getList();
	
}
