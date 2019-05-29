package com.menglingqiang.schedule.timer;

import com.menglingqiang.schedule.entity.DetailProject;
import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;
import com.menglingqiang.schedule.main.App;
import com.menglingqiang.schedule.service.DetailProjectService;
import com.menglingqiang.schedule.service.ProjectService;
import com.menglingqiang.schedule.service.UserService;
import com.menglingqiang.schedule.util.SendUtil;
import com.menglingqiang.schedule.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
//@Lazy(false) 
public class TaskJob {
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  @Autowired
  ProjectService projectService;

  @Autowired
  UserService userService;

  @Autowired
  DetailProjectService detailProjectService;

  public HashMap<String, String> projectJob() {
    //得到所有user的email
    //判断哪些已经超时了，今天的日期和得到的结束日期进行比较
    //projectMap 用来存放用户的邮箱和用户今天的任务key:email,value:projectName
    //今天的日期大，发送邮件
    List<Project> projectList = projectService.queryAllProject();
    HashMap<String, String> projectMap = new HashMap<String, String>();
    Date today = new Date();
    for (int i = 0; i < projectList.size(); i++) {
      Project project = projectList.get(i);
      //todo 不改动接口的情况下怎么把它弄走
      User user = new User();
      user.setEmail(project.getEmail());
      if (checkEmail(project.getEmail()) || userService.queryByEmail(user).getAlertStatus())
        continue;
      if (UUIDUtil.compareDate(today, project.getEndTime())) {
        String email = project.getEmail();
        if (projectMap.containsKey(email))//添加任务
        {
          String str = projectMap.get(email) + "," + project.getProjectName() + ",";
          projectMap.put(email, str);
        } else//初始化
        {
          String str = "您好，你今天一共有以下总任务即将到期：" + project.getProjectName() + ",";
          projectMap.put(email, str);
        }
      }
    }
    logger.info(today + "总项目已经发送邮件");
    //System.out.println(today+"总项目已经发送邮件");
    return projectMap;
  }

  public HashMap<String, String> detailProjectJob() {
    //得到所有user的email
    //判断哪些已经超时了，今天的日期和得到的结束日期进行比较
    //今天的日期大，发送邮件
    List<DetailProject> detailProjectList = detailProjectService.queryAllDetailProject();
    HashMap<String, String> detailprojectMap = new HashMap<String, String>();
    Date today = new Date();
    User user = new User();
    for (int i = 0; i < detailProjectList.size(); i++) {
      DetailProject detailProject = detailProjectList.get(i);
      if (UUIDUtil.compareDate(today, detailProject.getDetailEndTime())
          && detailProject.getDone() == 0)//今天的日期大于结束日期并且没完成
      {
        long projectId = detailProject.getProjectId();
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("projectId", projectId);
        String email = projectService.queryEverything(temp).get(0).getEmail();
        user.setEmail(email);
        if (checkEmail(email) || userService.queryByEmail(user).getAlertStatus())
          continue;
        if (detailprojectMap.containsKey(email))//添加任务
        {
          String str = detailprojectMap.get(email) + "," + detailProject.getProjectDetailName() + ",";
          detailprojectMap.put(email, str);
        } else//初始化
        {
          String str = "您好，你今天一共有以下分任务即将到期：" + detailProject.getProjectDetailName() + ",";
          detailprojectMap.put(email, str);
        }
      }
    }
    logger.info(today + "分项目已经发送邮件");
    return detailprojectMap;
  }

  @Scheduled(cron = "0 30 20  * * ? ")
  //@Scheduled(cron="0/5 * *  * * ? ") //五秒一次
  public void sendEmail() {
    HashMap<String, String> projectMap = projectJob();
    HashMap<String, String> detailprojectmMap = detailProjectJob();
    Set<Entry<String, String>> projectSet = projectMap.entrySet();
    Set<Entry<String, String>> detailProjectSet = detailprojectmMap.entrySet();
    //发送邮件
    for (Entry<String, String> project : projectSet) {
      SendUtil.sendString((String) project.getKey(), (String) project.getValue());
    }
    for (Entry<String, String> detailProject : detailProjectSet) {
      SendUtil.sendString((String) detailProject.getKey(), (String) detailProject.getValue());
    }
  }

  public static boolean checkEmail(String email)//是否是第三方默认邮箱
  {
    String regEx = "\\d+@testProject.com";
    // 编译正则表达式
    Pattern pattern = Pattern.compile(regEx);
    // 忽略大小写的写法
    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(email);
    // 字符串是否与正则表达式相匹配
    boolean flag = matcher.matches();
    return flag;
  }

  public static void main(String[] str) {
    for (int i = 0; i < 5; i++) {
      if (i > 3)
        continue;
      System.out.println(i);
    }
  }
}  