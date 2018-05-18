package com.menglingqiang.schedule.webcontroller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.menglingqiang.schedule.module.ThridUser;
import com.menglingqiang.schedule.module.WeiBoUser;
import com.menglingqiang.schedule.module.WeiXinUser;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.menglingqiang.schedule.entity.Project;
import com.menglingqiang.schedule.entity.User;
import com.menglingqiang.schedule.service.ProjectService;
import com.menglingqiang.schedule.service.UserService;
import com.menglingqiang.schedule.util.Constant;
import com.menglingqiang.schedule.util.SendUtil;
import com.menglingqiang.schedule.util.UUIDUtil;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;


@Controller
@RequestMapping("/user")
public class UserMailController {

	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;

	//欢迎页面
	@RequestMapping(value="/",method={RequestMethod.GET,RequestMethod.POST})
	public String welcome()
	{
		return "user/preLogin";//欢迎界面
	}

	//进入注册页面
	@RequestMapping(value="/preRegister",method={RequestMethod.GET,RequestMethod.POST})
	public String preRegister()
	{
		return "user/preRegister";
	}
	//进入注册状态页面
	@Transactional()
	@RequestMapping(value="/register",method={RequestMethod.GET,RequestMethod.POST})
	public String register(HttpServletRequest request,Model model,User user)
	{
		if(userService.isRegister(user))//判断是否已经有注册信息
			return "user/preLogin";//用户已经注册成功，返回登录界面
		String code = UUIDUtil.getUUID();
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));//对用户的密码加密
		user.setCode(code);//生成动态激活码
		String email = user.getEmail();//得到用户的email地址
		int temp = userService.register(user);
		//0 注册失败
		if(temp==0)
			return "user/register-error";
		else
		//1 注册成功，等待验证,跳到登录界面
		{
			//发送激活邮件给用户
			SendUtil.sendString(email,
			"<h1>点击链接激活邮箱</h1><h3><a href='http://www.menglingqiang.com/schedule/user/activation?flag=true&code="+code+"'>http://www.menglingqiang.com/schedule/user/activation?code="+code+"</a></h3>");
			model.addAttribute("user",user);
			return "user/login-success";
		}
	}
	@Transactional
	@RequestMapping(value="/registerByThree",method={RequestMethod.GET,RequestMethod.POST})
	public String registerByThree(HttpServletRequest request,HttpServletResponse response,Model model)
	{
		String loginType = request.getParameter("loginType");
		String userName=null;
		String userPic =null;
		ThridUser thridUser =null;
		Map map = new HashMap<String,String>();
		String code = request.getParameter("code");
		if(loginType.equals("2"))//如果是微博登录
		{
			thridUser = (WeiBoUser)loginByWeibo(code);
			map.put("tempToken", ((WeiBoUser) thridUser).getTempToken());
		}else if(loginType.equals("1"))
			thridUser = loginByWeiXin(code);
		if(thridUser==null)
			return "user/login-error";
		map.put("userName", thridUser.getUserName());
		map.put("loginType", loginType);
		map.put("threeUserId", thridUser.getUserId());//微博的userId
		int update =0;
		if(!StringUtils.isEmpty(loginType) && !StringUtils.isEmpty(thridUser.getUserName()))//是否传入数据
		{
			User user  = userService.queryUserByThree(map);//是否已经存在登录信息
			String email=null;
			if(user==null)//没有用户
			{
				map.put("password", UUIDUtil.getUUID().substring(0, 15));//随机生成一个密码
				int init = userService.registerByThree(map);//是否插入成功
				User temp = userService.queryUserByThree(map);//查询插入信息
				Long userId = temp.getUserId();
				email = userId+"@testProject.com";
				map.put("email", email);
				map.put("userId", userId);
				map.put("userPic", userId+".jpg");
				//下载头像
				UUIDUtil.downloadPic(userPic,Constant.USERPICLOCALPATH,userId+".jpg");
				//UUIDUtil.downloadPic(userPic,Constant.SERVICELOCALPAHT,userId+".jpg");

				userService.updateUser(temp);//更新用户的头像，（用户头像名称）
				update = userService.updateUserByUserIdForThree(map);
				if(update!=1||init!=1)//注册第三方用户失败
					return "user/register-error";
				else//注册更新成功，更新用户的最新信息
				{
					user = userService.queryUserByThree(map);//查询最新用户的信息
					Project p = UUIDUtil.genProject(user.getEmail());
					projectService.insertProjectByUser(user, p);//第一次登陆自动添加一条总任务和邮箱注册逻辑保持一致
				}
			}
			//记住cookie
			Cookie[] cookies = request.getCookies();
			if(cookies!=null&&cookies.length>0)
			{
				//如果有中文，这里可以用URLEncoder放的之后编码，取得时候解码统一用utf-8
				Cookie emailCookie = new Cookie("email",user.getEmail());
				Cookie passwordCookie = new Cookie("password",user.getPassword());
				emailCookie.setMaxAge(60*60*24*10);//Cookie失效日期为十天
				passwordCookie.setMaxAge(60*60*24*10);
				response.addCookie(emailCookie);
				response.addCookie(passwordCookie);
			}

			List<Project> projectList = projectService.queryProjectByEmail(user.getEmail());
			model.addAttribute("projectList", projectList);
			model.addAttribute("user", user);
			return "fucktime/projectList";
		}
		else
			return "user/preLogin";
	}
	/*
	 * 如果是第一次注册
	 */
	//进入激活状态页面
	@Transactional
	@RequestMapping(value="/activation",method={RequestMethod.GET,RequestMethod.POST})
	public String activation(Model model ,String  code,boolean flag,HttpServletRequest requset)//true表示第一次注册
	{
		if(code==null)
			return "user/activition-error";
		User user = userService.queryByCode(code);
		if(user==null)
			return "user/activition-error";
		else
		{
			if(!flag)//不是第一次注册，激活
				SendUtil.sendString(user.getEmail(),
					"<h3><a href='http://www.menglingqiang.com/schedule/user/activation?flag=true&code="+code+"'>点击链接激活邮箱</a></h3>");
			else
			{
				userService.UpdateUserCode(code);//根据激活码更新用户的状态
				String email = requset.getParameter("email");
				String newEmail = requset.getParameter("newEmail");
				if(email!=null && newEmail!=null)
				{
					Map temp = new HashMap();
					temp.put("email", email);
					temp.put("newEmail", newEmail);
					//如果是更改邮箱激活的话,更新用户邮箱
					userService.updateUserEmail(temp);
					//更新总任务对应的邮箱
					projectService.updateProjectForEmailChange(temp);
					user.setEmail(newEmail);
				}
				Project p = UUIDUtil.genProject(user.getEmail());
				if(newEmail==null)
					projectService.insertProjectByUser(user, p);//激活用户系统自动添加一项任务
				user.setStatus(1);
				List<Project> projectList = projectService.queryProjectByEmail(user.getEmail());
				model.addAttribute("projectList", projectList);//激活成功跳转到projectList界面
				model.addAttribute("email", user.getEmail());//激活成功跳转到projectList界面
				model.addAttribute("user", user);//激活成功跳转到projectList界面
				return "fucktime/projectList";
			}
			model.addAttribute("user", user);
			//TODO跳转界面，发送成功请到邮箱激活
			return "user/register-precode";
		}
	}
	//进入登录界面
	@RequestMapping(value="/preLogin",method={RequestMethod.GET,RequestMethod.POST})
	public String preLogin(HttpServletRequest request,HttpServletResponse response)
	{
		return "user/preLogin";//登录界面
	}

	//微博登录
	public WeiBoUser loginByWeibo(String code)
	{
		Oauth oauth = new Oauth();
		WeiBoUser weiBoUser = new WeiBoUser();
		try {
			AccessToken token = oauth.getAccessTokenByCode(code);
			String accessToken = token.getAccessToken() ;
			String uidStr = UUIDUtil.token2Uid(token.toString());
			if(StringUtils.isEmpty(uidStr))
				return null;
			String[] temp = uidStr.split("=");
			if(temp.length<2)
				return null;
			weiBoUser.setUserId(temp[1]);
			Users um = new Users(accessToken);
			weibo4j.model.User user = um.showUserById(weiBoUser.getUserId());
			weiBoUser.setUserName(user.getScreenName());
			weiBoUser.setUserPicPath(user.getavatarLarge());
			weiBoUser.setTempToken(accessToken);
		} catch (WeiboException e) {
			e.printStackTrace();
			weiBoUser=null;
		}finally {
			return weiBoUser;
		}
	}
	//微信登录
	public WeiXinUser loginByWeiXin(String code)
	{

		return null;
	}
	//执行登录逻辑
	@RequestMapping(value="/login",method={RequestMethod.GET,RequestMethod.POST})
	public String logIn(User user,Model model,HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//验证验证码是否输入正确
		boolean codeFlag = false;
		boolean autoLogin = (request.getParameter("autoLogin")!=null && request.getParameter("autoLogin")!="");
		if(autoLogin==true)
			codeFlag=true;//自动登录忽略验证码判断
		else
			codeFlag= validateCode(request, response);
		if(codeFlag)
		{
			//查询用户和密码是否正确，后期可以加密
			user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));//对明文密码加密和库中的密文密码对比
			User userTemp = userService.queryByPassword(user);
			if(userTemp==null)
				return "user/login-error";//登录失败，后期可以显示是没有用户还是用户的密码错误
			else
			{
				model.addAttribute("user", userTemp);//传递用户的信息
				//判断是否自动登录
				if(autoLogin)
				{
					Cookie[] cookies = request.getCookies();
					if(cookies!=null&&cookies.length>0)
					{
						//如果有中文，这里可以用URLEncoder放的之后编码，取得时候解码统一用utf-8

						Cookie emailCookie = new Cookie("email",userTemp.getEmail());
						Cookie passwordCookie = new Cookie("password",userTemp.getPassword());
						emailCookie.setMaxAge(60*60*24*10);//Cookie失效日期为十天
						passwordCookie.setMaxAge(60*60*24*10);
						response.addCookie(emailCookie);
						response.addCookie(passwordCookie);
					}
				}else
				{
					Cookie[] cookies = request.getCookies();
					if(cookies!=null&&cookies.length>0)
					{
						for(Cookie c:cookies)
						{
							if(c.getName().equals("userName")||c.getName().equals("password"))
							{
								c.setMaxAge(0);
								response.addCookie(c);
							}
						}
					}
				}
				//判断完毕
				if(userTemp.getStatus()==0 && userTemp.getLoginType().equals("1"))//邮箱用户没有激活先激活才可以用打卡的功能
					return "user/login-success";
				else
				{
					List<Project> projectList = projectService.queryProjectByEmail(userTemp.getEmail());
					model.addAttribute("projectList", projectList);
					return "fucktime/projectList";
				}
			}
		}
		return "user/login-error";
	}
	//生成验证码
	@RequestMapping(value="/validate",method={RequestMethod.GET,RequestMethod.POST})
	public void gerValidate(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//画验证码的框
		BufferedImage bi = new BufferedImage(100, 30, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bi.getGraphics();
		Color c = new Color(200,156,255);
		g.setColor(c);
		g.fillRect(0, 0, 92, 31);
		//验证码里面的信息
		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();//
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for(int i=0;i<4;i++)
		{
			int index = random.nextInt(ch.length);
			char indexChar = ch[index];
			g.setColor(new Color(random.nextInt(88),random.nextInt(188),random.nextInt(255)));
			g.drawString(indexChar+"",i*22+5 , 25);
			sb.append(indexChar+"");
		}
		request.getSession().setAttribute("picCode", sb.toString());//将界面的piccode存入界面中
		ImageIO.write(bi, "JPG", response.getOutputStream());
	}
	//验证，验证码是否输入正确
	public boolean validateCode(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String picCode = (String)request.getSession().getAttribute("picCode");
		picCode = picCode.trim();//去空格

		String inputCode = request.getParameter("inputCode").toUpperCase().trim();//不区分大小写
		response.setContentType("text/html;charset=utf8");
		boolean flag = picCode.equals(inputCode);
		return flag;
	}

	@RequestMapping(value="/getPicCode",method={RequestMethod.GET,RequestMethod.POST})
	public  @ResponseBody String getPicCode(HttpServletRequest request)
	{
		String picCode = (String)request.getSession().getAttribute("picCode");
		return picCode;
	}
	//进入忘记密码界面
	@RequestMapping(value="/preForgetPassword",method={RequestMethod.GET,RequestMethod.POST})
	public String preForgetPassword()
	{
		return "user/preForgetPassword";
	}

	//忘记密码逻辑,通过邮箱查找用户
	@RequestMapping(value="/forgetPassword",method={RequestMethod.GET,RequestMethod.POST})
	public String forgetPassword(User user,HttpServletRequest request)//前台会自动的将email注入到user中
	{
		String email = (String)request.getParameter("email");
		String password = request.getParameter("password");
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		//TODO 修改信息，进入修改界面
		String msg = "<h3><a href='http://www.menglingqiang.com/schedule/user/modifyPassword?email="+email+"&password="+password+"'>点击重置密码</a></h3>";
		//发送邮件
		SendUtil.sendString(email,msg);//点击连接，进入修改界面传入邮箱
		request.getSession().setAttribute("msg", "邮件已经发送到您的邮箱，请查收");
		return "user/confrimForgetPassword";
	}
	@RequestMapping(value="/isRegister",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String isRegister(HttpServletRequest request)//前台会自动的将email注入到user中
	{
		String email = (String)request.getParameter("email");
		User user = new User();
		user.setEmail(email);
		//通过email，判断这个用户是否已经注册
		String flag =  Boolean.toString(userService.isRegister(user));
		return flag;
	}
	//进入用户信息修改界面
	@RequestMapping(value="/modifyPassword",method={RequestMethod.GET,RequestMethod.POST})
	public String modifyPassword(HttpServletRequest request,Model model)//修改用户的信息,就不用激活了，保持原来账户的信息
	{
		String email = (String)request.getParameter("email");
		String password = request.getParameter("password");
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setCreateTime(new Date());//更新更改时间
		int flag = userService.updateUser(user);
		//TODO  动态sql写的有问题
		if(flag!=1)
			return "user/login-error";//界面直接登录
		else
		{
			user = userService.queryByEmail(user);
			model.addAttribute("user", user);//传递用户的信息
			if(user.getStatus()==0)//没有激活
				return "user/login-success";
			else
			{
				List<Project> projectList = projectService.queryProjectByEmail(user.getEmail());
				model.addAttribute("projectList", projectList);
				return "fucktime/projectList";
			}
		}
	}
	@RequestMapping(value="/loginFlag",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String loginFlag(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//1验证成功，2这个邮箱木有注册，3有邮箱但是密码不对，4验证码不对
		//验证验证码是否输入正确
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = new User();
		user.setEmail(email);
		user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
		//查询是否有该用户
		if(userService.queryByEmail(user)==null)
			return "2";
		//查询用户和密码是否正确，后期可以加密
		if(userService.queryByPassword(user)==null)
			return "3";
		return "1";
	}
	@RequestMapping(value="/registerFlag",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String registerFlag(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//1,邮箱已经注册，0，邮箱没有注册
		String email = request.getParameter("email");
		User user = new User();
		user.setEmail(email);
		//查询是否有该用户
		if(userService.queryByEmail(user)==null)
			return "0";
		else
			return "1";
	}
	@RequestMapping(value="/showUserInfo",method={RequestMethod.GET,RequestMethod.POST})
	public  String showUserInfo(HttpServletRequest request,Model model)
	{
		String email = request.getParameter("email");

		User temp = new User();
		temp.setEmail(email);
		User user = userService.queryByEmail(temp);//查询用户的信息
		int sum = projectService.queryAllDetailProjectCountByEmail(email);
		int done = projectService.queryAllDoneDetailProjectCountByEmail(email);
		model.addAttribute("user",user);
		model.addAttribute("sum",sum);
		model.addAttribute("done",done);
		return "/user/userInfo";
	}

	@RequestMapping(value="/changeUserPic",method={RequestMethod.GET,RequestMethod.POST})//使用两次就不抽象成函数了,更改数据库的
	public  String changeUserPic(@RequestParam("file1") MultipartFile file,HttpServletRequest request,Model model)
	{
		String email = request.getParameter("email");
		//String email = "13429774945@163.com";
		User temp = new User();
		temp.setEmail(email);
		User user = userService.queryByEmail(temp);//查询用户的信息
		String userPicName = user.getUserId() +"." +UUIDUtil.getFileType(file);//用户头像名称
		temp.setUserPic(userPicName);

		if(!file.isEmpty()){
			try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(Constant.USERPICLOCALPATH,
						userPicName));//把用户头像图片复制到本地（这样clean的时候本地的图片会加载到服务器端）
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(Constant.SERVICELOCALPAHT,
						userPicName));//把用户头像图片复制到服务器(换头像的时候可以从服务器拿到用户的头像)
				userService.updateUser(temp);//更新用户的头像，（用户头像名称）
			} catch (IOException e) {
				e.printStackTrace();
				return "/user/login-error";//TODO,新建一个错误页面，返回直接historyback
			}
		}
		int sum = projectService.queryAllDetailProjectCountByEmail(email);
		int done = projectService.queryAllDoneDetailProjectCountByEmail(email);
		model.addAttribute("user",user);
		model.addAttribute("sum",sum);
		model.addAttribute("done",done);
		return "/user/userInfo";

	}
	@RequestMapping(value="/logout",method={RequestMethod.GET,RequestMethod.POST})
	public String logout(HttpServletRequest request,HttpServletResponse response)
	{
		//清除Cookie
		Cookie[] cookies = request.getCookies();
		if(cookies!=null&&cookies.length>0)
		{
			for(Cookie c:cookies)
			{
				if(c.getName().equals("password"))//只删除password的cookie，保留email
				{
					c.setValue("");
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
		//跳转到登录页面
		return "user/preLogin";
	}
	@RequestMapping(value="/changeEmail",method={RequestMethod.GET,RequestMethod.POST})//更改邮箱
	public  String changeEmail(HttpServletRequest request,Model model)
	{
 		String email = request.getParameter("email");
		String newEmail = request.getParameter("newEmail");
		String msg;
		//判断输入的邮箱是否符合规则，前台也会校验
		String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
		if(email.equals(newEmail))
			msg = "两次绑定的邮箱相同";
		if(!email.matches(regex) || !newEmail.matches(regex))
		{
			msg = "您的邮箱格式不正确请重新输入";
		}
		else//如果符合规则更新邮箱
		{
			//发送邮件到邮箱
			String code = UUIDUtil.getUUID();
			User temp = new User();
			temp.setCode(code);
			temp.setEmail(email);
			//将激活码更新到用户表中
			userService.updateUser(temp);
			SendUtil.sendString(newEmail,
					"<h3><a href='http://www.menglingqiang.com/schedule/user/activation?flag=true&code="+code+"&email="+email+"&newEmail="+newEmail+"'>点击链接激活邮箱</a></h3>");
			msg="请登录绑定邮箱邮箱激活";
		}
		//重新查询信息传给前台
		User user = new User();
		user.setEmail(email);
		user = userService.queryByEmail(user);
		int sum = projectService.queryAllDetailProjectCountByEmail(email);
		int done = projectService.queryAllDoneDetailProjectCountByEmail(email);
		model.addAttribute("sum",sum);
		model.addAttribute("done",done);
		model.addAttribute("user",user);
		model.addAttribute("msg",msg);
		return "/user/userInfo";
	}
	@RequestMapping(value="/isBindEmail",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public boolean isBindEmail(HttpServletRequest request)
	{
		String email = request.getParameter("email");
		User user = new User();
		user.setEmail(email);
		if(userService.queryByEmail(user)==null)
			return false;
		else
			return true;
	}

	@RequestMapping(value="/switchAlert",method={RequestMethod.GET,RequestMethod.POST})
	public String switchAlert(HttpServletRequest request,Model model)
	{
		boolean alertStatus = Boolean.valueOf(request.getParameter("alertStatus"));
		String email = request.getParameter("email");
		User user = new User();
		user.setEmail(email);
		user.setAlertStatus(alertStatus);
		userService.updateUser(user);
		//重新查询信息传给前台,封装一个函数?
		user = userService.queryByEmail(user);
		int sum = projectService.queryAllDetailProjectCountByEmail(email);
		int done = projectService.queryAllDoneDetailProjectCountByEmail(email);
		String msg = alertStatus ? "邮箱提醒开启成功!":"邮箱提醒关闭成功!";
		model.addAttribute("sum",sum);
		model.addAttribute("done",done);
		model.addAttribute("user",user);
		model.addAttribute("msg",msg);
		return "/user/userInfo";
	}
	@RequestMapping("/test")
	public void test()
	{
		Map map = new HashMap<String,String>();
		map.put("tempToken", "44444444444444444444444444444");
		map.put("userName", "sdfsdfdsfds");
		map.put("loginType", "1");
		User user = userService.queryUserByThree(map);
		System.out.println(user);
	}

	@RequestMapping(value="/kindle",method=RequestMethod.POST)
	public  Boolean sendToKindle(@RequestParam("files") MultipartFile[] files,HttpServletRequest request)
	{
		ArrayList<String> books = new ArrayList<String>();
		String to = request.getParameter("kindleEmail");
		if(null != files && null!= to) {
			for(MultipartFile file:files)
				books.add(file.getName());
			return SendUtil.sendAttach(to,books);
		} else
			return false;
	}
}

