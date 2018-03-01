package com.menglingqiang.schedule.webcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;
import com.menglingqiang.schedule.module.Message;
import com.menglingqiang.schedule.service.DetailProjectService;
import com.menglingqiang.schedule.service.ProjectService;
import com.menglingqiang.schedule.service.UserService;
import com.menglingqiang.schedule.util.UUIDUtil;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;



@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	UserService userService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	DetailProjectService detailProjectService;
	
    @RequestMapping(value="/operation",method={RequestMethod.GET,RequestMethod.POST})
	public String operation(HttpServletRequest request,Model model)
	{
    	String type = request.getParameter("type").trim();
    	String email = request.getParameter("email");
    	if(type.equals("modify"))//需要projectId
    	{
    		String projectId = request.getParameter("projectId");
    		String projectName = request.getParameter("projectName");
    		String startTime = request.getParameter("startTime");
    		String endTime = request.getParameter("endTime");
    		
    		Project project = new Project();
    		project.setProjectId(Integer.parseInt(projectId));
    		project.setProjectName(projectName);
    		project.setStartTime(UUIDUtil.string2Date(startTime));
    		project.setEndTime(UUIDUtil.string2Date(endTime));
    		projectService.modifyProject(project);
    	}
    		
    	else if(type.equals("add"))//需要email
		{
    		String projectName = request.getParameter("projectName");
    		String startTime = request.getParameter("startTime");
    		String endTime = request.getParameter("endTime");
    		
    		Project project = new Project();
    		
    		project.setProjectName(projectName);
    		project.setEmail(email);
    		project.setStartTime(UUIDUtil.string2Date(startTime));
    		project.setEndTime(UUIDUtil.string2Date(endTime));
    		projectService.insertProjectByEmail(project);
		}
    	else if(type.equals("delete"))//
		{
    		String id = request.getParameter("projectId");
        	projectService.deleteProject(Integer.parseInt(id));
		}
    	List<Project> projectList = projectService.queryProjectByEmail(email); 
    	model.addAttribute("projectList", projectList);//重新查询list
    	//重新查一次数据放入页面中
    	return "fucktime/projectList";
	}
    @RequestMapping(value="/showDetail",method={RequestMethod.GET,RequestMethod.POST})//可以将里面的内容抽象为函数
    public String showDetail(HttpServletRequest request,Model model)
    {
    	String projectId = request.getParameter("projectId");
    	String projectName = request.getParameter("projectName");
    	return showInfo(projectId,projectName,model);
    }
    @RequestMapping(value="/detailOperation",method={RequestMethod.GET,RequestMethod.POST})
	public String detailOperation(HttpServletRequest request,Model model)
	{
    	String type = request.getParameter("type").trim();
    	String projectId = request.getParameter("projectId");
    	String name = request.getParameter("name");
    	if(type.equals("modify"))//分项目id
    	{
    		String projectDetailId = request.getParameter("projectDetailId");
    		String projectDetailName = request.getParameter("projectDetailName");
    		String detailStartTime = request.getParameter("detailStartTime");
    		String detailEndTime = request.getParameter("detailEndTime");
    		
    		DetailProject detailProject = new DetailProject();
    		detailProject.setProjectDetailId(Long.parseLong(projectDetailId));
    		detailProject.setProjectDetailName(projectDetailName);
    		detailProject.setDetailStartTime(UUIDUtil.string2Date(detailStartTime));
    		detailProject.setDetailEndTime(UUIDUtil.string2Date(detailEndTime));
    		detailProjectService.modifyDetailProject(detailProject);
    	}
    		
    	else if(type.equals("add"))//总项目id
		{
    		String projectDetailName = request.getParameter("projectDetailName");
    		String detailStartTime = request.getParameter("detailStartTime");
    		String detailEndTime = request.getParameter("detailEndTime");
    		
    		DetailProject detailProject = new DetailProject();
    		detailProject.setProjectId(Long.parseLong(projectId));
    		detailProject.setProjectDetailName(projectDetailName);
    		detailProject.setDetailStartTime(UUIDUtil.string2Date(detailStartTime));
    		detailProject.setDetailEndTime(UUIDUtil.string2Date(detailEndTime));
    		detailProjectService.addDetailProject(detailProject);
		}
    	else if(type.equals("delete"))//
		{
    		String id = request.getParameter("projectDetailId");
        	detailProjectService.deteleDetailProject(Long.parseLong(id));
		}
    	return showInfo(projectId,name,model);
	}
    @RequestMapping(value="/doneDetailProject",method={RequestMethod.GET,RequestMethod.POST})
    public String doneDetailProject(HttpServletRequest request,Model model)
    {
    	String detailProjectId = request.getParameter("detailProjectId");
    	String projectId = request.getParameter("projectId");
    	String name = request.getParameter("name");
    	detailProjectService.modifyDetailProjectForDone(Long.parseLong(detailProjectId));//更新数据库
    	return showInfo(projectId,name,model);
    }
    //通过总任务id得到每一个分任务,并跳转到明细页面
    public String showInfo(String projectId,String projectName,Model model)
    {
    	List <DetailProject> detailProjectList = 
    			projectService.queryDetailProjectById(Long.parseLong(projectId));
    	model.addAttribute("detailProjectList",detailProjectList);
    	model.addAttribute("projectId",projectId);
    	model.addAttribute("projectName",projectName);
    	return "fucktime/detailProjectList";
    }
    @RequestMapping(value="/getProjectByEmail",method={RequestMethod.GET,RequestMethod.POST})
    public String getDetailProject(HttpServletRequest request,Model model)
    {
    	String projectId = request.getParameter("projectId");
    	String email = request.getParameter("email");
    	if(email==null)
    	{
    		Map map = new HashMap();
    		map.put("projectId", projectId);
    		List<Project> temp = projectService.queryEverything(map);
    		if(temp.size()>0)
    			email = temp.get(0).getEmail();
    	}	
    	List<Project> projectList = projectService.queryProjectByEmail(email);
    	//为了和前面的页面获取额email一致
    	User temp = new User();
    	temp.setEmail(email);
    	User user = userService.queryByEmail(temp);
    	model.addAttribute("user", user);
    	model.addAttribute("projectList", projectList);//重新查询list
    	//重新查一次数据放入页面中
    	return "fucktime/projectList";
    }
    //得到完成的百分比
    @RequestMapping(value="/getHaveDone",method={RequestMethod.GET,RequestMethod.POST})
    public  @ResponseBody String getHaveDone(HttpServletRequest request,HttpServletResponse response,Model model)
    {
    	HashMap<String,Float> map = new HashMap<String,Float>();//key是项目的id,value是项目的完成百分比
    	String email = request.getParameter("email");
    	List<Project> projectList = projectService.queryProjectByEmail(email);//通过我email查到用户的所有总任务
    	
    	for(int i=0;i<projectList.size();i++)
		{
    		long tempProjectId = projectList.get(i).getProjectId();
    		float f = projectService.haveDone(tempProjectId);
    		map.put(String.valueOf(tempProjectId), f);//没有子项目会报异常
		}
    	//model.addAttribute("percentMap", map);
    	//request.getSession().setAttribute("percentMap", map); 
    	return map.toString();
    	//return "success";
    }
    @RequestMapping(value="/queryProjectByNameOrTime",method={RequestMethod.GET,RequestMethod.POST})
    public  String queryProjectByNameOrTime(HttpServletRequest request,Model model)
    {
    	String projectName = (String)UUIDUtil.parseNull2Empty(request.getParameter("queryProjectName"));
    	String queryTime = (String)UUIDUtil.parseNull2Empty(request.getParameter("queryTime"));
    	String email = (String)UUIDUtil.parseNull2Empty(request.getParameter("projectEmail"));
    	if(email==""||email==null)
    		email = (String)UUIDUtil.parseNull2Empty(request.getParameter("email"));
    	Map map = new HashMap();
    	map.put("projectName", projectName.trim());
    	map.put("queryTime", queryTime.trim());
    	map.put("email", email.trim());
    	List<Project> projectList = projectService.queryProjectByNameOrTime(map);
    	
    	//为了和前面的页面获取额email一致
    	User temp = new User();
    	temp.setEmail(email);
    	User user = userService.queryByEmail(temp);
    	model.addAttribute("user", user);
    	model.addAttribute("projectList", projectList);//重新查询list
    	//重新查一次数据放入页面中
    	return "fucktime/projectList";
    }
    @RequestMapping(value="/queryDetailProjectByNameOrTime",method={RequestMethod.GET,RequestMethod.POST})
    public  String queryDetailProjectByNameOrTime(HttpServletRequest request,Model model)
    {
    	String detailProjectName = request.getParameter("queryDetailProjectName");
    	String queryTime = request.getParameter("queryTime");
    	String projectId = request.getParameter("projectId");
    	String projectName = request.getParameter("name");
    	
    	Map map = new HashMap();
    	map.put("detailProjectName", detailProjectName.trim());
    	map.put("queryTime", queryTime.trim());
    	map.put("projectId", Long.parseLong(projectId));
    	List <DetailProject> detailProjectList = 
    			detailProjectService.queryDetailProjectByNameOrTime(map);
    	model.addAttribute("detailProjectList",detailProjectList);
    	model.addAttribute("projectId",projectId);
    	model.addAttribute("projectName",projectName);
    	return "fucktime/detailProjectList";
    }
    @RequestMapping(value="/queryUserName",method={RequestMethod.GET,RequestMethod.POST},produces={"application/text;charset=UTF-8"})
    public  @ResponseBody String queryUserName(HttpServletRequest request)
    {
    	String email = request.getParameter("email");
    	User temp = new User();
    	temp.setEmail(email);
    	User user = userService.queryByEmail(temp);
    	if(user == null)
    		return "";
    	return user.getName();
    }
    /**
     * @param request
     * @return
     */
    @RequestMapping(value="/queryPorjectNameAndTime",method={RequestMethod.GET,RequestMethod.POST},produces={"application/text;charset=UTF-8"})
    public  @ResponseBody String queryPorjectNameAndTime(HttpServletRequest request)
    {
    	
    	String projectId = request.getParameter("projectId");
    	Map map = new HashMap();
    	map.put("projectId",Long.parseLong(projectId));
    	List<Project> projectList = projectService.queryEverything(map);
    	if(projectList.size()==0)
    		return "";
    	return projectList.get(0).getProjectName()+","+projectList.get(0).getStartDate()+","+
    	projectList.get(0).getEndDate();
    }
    @RequestMapping(value="queryMessage",method={RequestMethod.GET,RequestMethod.POST})
    public String queryMessage(HttpServletRequest request,Model model)
    {
    	String email = request.getParameter("email");
    	List<Message> messageList = projectService.queryMessage(email);
    	
    	User temp = new User();
    	temp.setEmail(email);
    	
    	User user = userService.queryByEmail(temp);
    	model.addAttribute("user",user);
    	model.addAttribute("messageList", messageList);
    	return "user/message";
    }
    //微博分享
    @RequestMapping(value="/shareWeiBo",method={RequestMethod.GET,RequestMethod.POST})
    public  @ResponseBody String shareWeiBo(HttpServletRequest request)
    {
    	String email = request.getParameter("email");
    	String text = request.getParameter("text");
    	User temp = new User();
    	temp.setEmail(email);
    	User user = userService.queryByEmail(temp);
    	String access_token = user.getTempToken();
		Timeline tm = new Timeline(access_token);
		try {
			Status status = tm.updateStatus(text);
		} catch (WeiboException e) {
			e.printStackTrace();
			return "error";
		}	
    	return "success";
    }
    @RequestMapping(value="/test",method={RequestMethod.GET,RequestMethod.POST})
    public  String test(HttpServletRequest request,HttpServletResponse response,Model model)
    {
    	return "user/test";
    }
    
}



