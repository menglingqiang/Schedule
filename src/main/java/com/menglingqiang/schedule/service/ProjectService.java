package com.menglingqiang.schedule.service;

import java.util.List;
import java.util.Map;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;
import com.menglingqiang.schedule.module.Message;



public interface ProjectService {
	
	//给指定的用户，添加一条总任务
	public int insertProjectByUser(User user,Project project);
	//给定email，添加一条总任务
	public int insertProjectByEmail(Project project);
	//给总任务添加一条分任务
	public int insertDetailProject(Project project,DetailProject detailProject);
	public List<Project> queryProjectByEmail(String email);
	public int modifyProject(Project project);
	public int deleteProject(long id);
	public List<DetailProject> queryDetailProjectById (long projectId);
	public List<Project> queryEverything(Map map);
	public float haveDone(long projectId);//该任务已经完成的百分比（截取两位小数）
	public List<Project> queryAllProject();
	public List<Project> queryProjectByNameOrTime(Map map);
	public int queryAllDetailProjectCountByEmail(String email);
	public int queryAllDoneDetailProjectCountByEmail(String email);
	public List<Message> queryMessage(String email);
}
