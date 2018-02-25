package com.menglingqiang.schedule.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class MainController {
    
	//欢迎页面
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String welcome()
	{
		return "user/preLogin";//欢迎界面
	}
//	@RequestMapping(value="/error",method=RequestMethod.GET)
//	public String error()
//	{
//		return "user/login-error";
//	}
//	@RequestMapping(value="/404",method={RequestMethod.GET,RequestMethod.POST})
//	public String error404()
//	{
//		return "error/404";
//	}
//	@RequestMapping(value="/500",method={RequestMethod.GET,RequestMethod.POST})
//	public String error500()
//	{
//		return "error/500";
//	}
}

