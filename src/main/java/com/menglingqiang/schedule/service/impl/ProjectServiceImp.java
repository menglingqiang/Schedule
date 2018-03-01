package com.menglingqiang.schedule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menglingqiang.schedule.dao.ProjectDao;
import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;
import com.menglingqiang.schedule.module.Message;
import com.menglingqiang.schedule.service.ProjectService;

@Service
public class ProjectServiceImp implements ProjectService{

	@Autowired
	ProjectDao projectDao;

	@Override
	public int insertProjectByUser(User user, Project project) {
		
		return projectDao.insertProjectByUser(user, project);
	}

	@Override
	public int insertDetailProject(Project project, DetailProject detailProject) {
		return projectDao.insertDetailProject(project, detailProject);
	}

	public List<Project> queryProjectByEmail(String email) {
		
		return projectDao.queryProjectByEmail(email);
	}

	@Override
	public int modifyProject(Project project) {
		return projectDao.modifyProject(project);
	}

	@Override
	public int deleteProject(long id) {
		return projectDao.deleteProject(id);
	}

	@Override
	public int insertProjectByEmail(Project project) {
		
		return projectDao.insertProjectByEmail(project);
	}

	@Override
	public List<DetailProject> queryDetailProjectById(long projectId) {
		Project project = projectDao.queryDetailProjectById(projectId);
		if(project==null)
			return null;
		return project.getDetailProjects();
	}

	@Override
	public List<Project> queryEverything(Map map) {
		
		return projectDao.queryEverything(map);
	}

	@Override
	public float haveDone(long projectId) {
		
		return projectDao.haveDone(projectId);
	}

	@Override
	public List<Project> queryAllProject() {
		return projectDao.queryAllProject();
	}

	@Override
	public List<Project> queryProjectByNameOrTime(Map map) {
		
		return projectDao.queryProjectByNameOrTime(map);
	}

	@Override
	public int queryAllDetailProjectCountByEmail(String email) {
		
		return projectDao.queryAllDetailProjectCountByEmail(email);
	}

	@Override
	public int queryAllDoneDetailProjectCountByEmail(String email) {
		
		return projectDao.queryAllDoneDetailProjectCountByEmail(email);
	}

	@Override
	public List<Message> queryMessage(String email) {
		
		List<HashMap> maps = projectDao.queryMessage(email);
		List<Message> messages = new ArrayList<Message>();
		for(int i=0;i<maps.size();i++)
		{
			Message message = new Message();
			message.setDetailProjectName((String)maps.get(i).get("project_detail_name"));
			message.setProjectName((String)maps.get(i).get("project_name"));
			message.setEmail(email);
			
			messages.add(message);
		}
		return messages;
	}

	@Override
	public int updateProjectForEmailChange(Map map) {
		
		return projectDao.updateProjectForEmailChange(map);
	}
	
}
