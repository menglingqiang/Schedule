package com.menglingqiang.schedule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;

@Mapper
public interface ProjectDao {
	public int insertProjectByUser(@Param(value = "user") User user,@Param(value = "project")Project project);//给指定的用户，添加一条总任务
	public int insertProjectByEmail(@Param(value = "project")Project project);//给指定的邮箱，添加一条总任务
	public int insertDetailProject(@Param(value = "project")Project project,@Param(value = "detailProject")DetailProject detailProject);//给总任务添加一条分任务
	public List<Project> queryProjectByEmail(@Param(value="email")String email);//通过email查询，project方法
	public int modifyProject(Project project);
	public int deleteProject(long projectId);
	public Project queryDetailProjectById(long projectId);
	public List<Project> queryEverything(Map map);
	public float haveDone(long projectId);//该任务已经完成的百分比（截取两位小数）
	public List<Project> queryAllProject();//得到所有的project
	public List<Project> queryProjectByNameOrTime(Map map);//根据项目名称和时间得到
	public int queryAllDetailProjectCountByEmail(String email);
	public int queryAllDoneDetailProjectCountByEmail(String email);
	public List<HashMap> queryMessage(String email);
}
