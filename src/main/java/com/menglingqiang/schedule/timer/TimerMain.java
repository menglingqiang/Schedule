package com.menglingqiang.schedule.timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*定时任务主函数*/
public class TimerMain {

  public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:main/resources/spring/spring-timer.xml");
  }

}  
