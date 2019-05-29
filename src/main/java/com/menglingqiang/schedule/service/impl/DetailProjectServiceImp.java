package com.menglingqiang.schedule.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menglingqiang.schedule.dao.DetailProjectDao;
import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.service.DetailProjectService;

@Service
public class DetailProjectServiceImp implements DetailProjectService{

	@Autowired
	DetailProjectDao detailProjectDao;

	@Override
	public int modifyDetailProject(DetailProject detailProject) {
		
		return detailProjectDao.modifyDetailProject(detailProject);
	}

	@Override
	public int deteleDetailProject(long projectDetailId) {
		
		return detailProjectDao.deleteDetailProject(projectDetailId);
	}

	@Override
	public int addDetailProject(DetailProject detailProject) {
		
		return detailProjectDao.addDetailProject(detailProject);
	}

	@Override
	public int modifyDetailProjectForDone(long projectDetailId) {
		
		return detailProjectDao.modifyDetailProjectForDone(projectDetailId);
	}

	@Override
	public List<DetailProject> queryAllDetailProject() {
		
		return detailProjectDao.queryAllDetailProject();
	}

	@Override
	public List<DetailProject> queryDetailProjectByNameOrTime(Map map) {
		
		return detailProjectDao.queryDetailProjectByNameOrTime(map);
	}

	@Override
	public List<Project> queryPojectMessage(String email) {
		return detailProjectDao.queryPojectMessage(email);
	}

	@Override
	public List<DetailProject> queryDetailPojectMessage(String email) {
		// TODO Auto-generated method stub
		return detailProjectDao.queryDetailPojectMessage(email);
	}

	
	
}
