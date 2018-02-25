package com.menglingqiang.schedule.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;


@Mapper
public interface DetailProjectDao {

	public int modifyDetailProject(DetailProject detailProject);
	public int deteleDetailProject(long projectDetailId);
	public int addDetailProject(DetailProject detailProject);
	public int modifyDetailProjectForDone(long projectDetailId);
	public List<DetailProject> queryAllDetailProject();
	public List<DetailProject> queryDetailProjectByNameOrTime(Map map);
	public List<Project> queryPojectMessage(String email);
	public List<DetailProject> queryDetailPojectMessage(String email);
	
}
