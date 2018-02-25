package com.menglingqiang.schedule.service;

import java.util.Map;

import com.menglingqiang.schedule.entity.User;



public interface UserService {

	//注册用户
	public int register(User user);
	//根据注册码查找用户
	public User queryByCode(String code);
	//更新注册码信息
	public int UpdateUserCode(String code);
	//用户和密码是否匹配
	public User queryByPassword(User user);
	//用户和密码是否匹配
	public User queryByEmail(User user);
	//邮箱是否注册
	public boolean isRegister(User user);
	//更改用户的的信息
	public int updateUser(User user);
	//通过第三方注册用户
	public int registerByThree(Map map);
	//通过第三方更新用户
	public int updateUserByUserIdForThree(Map map);
	//查询第三方用户数据
	public User queryUserByThree(Map map);
}
