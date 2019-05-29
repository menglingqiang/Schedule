package com.menglingqiang.schedule.dao;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface DetailProjectDao {

	public int modifyDetailProject(DetailProject detailProject);
	public int deleteDetailProject(long projectDetailId);
	public int addDetailProject(DetailProject detailProject);
	public int modifyDetailProjectForDone(long projectDetailId);
	public List<DetailProject> queryAllDetailProject();
	public List<DetailProject> queryDetailProjectByNameOrTime(Map map);
	public List<Project> queryPojectMessage(String email);
	public List<DetailProject> queryDetailPojectMessage(String email);
	
}
