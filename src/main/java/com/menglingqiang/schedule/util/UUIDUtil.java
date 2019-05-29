package com.menglingqiang.schedule.util;

import com.menglingqiang.schedule.entity.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDUtil {

  public static String getUUID() {
    return UUID.randomUUID().toString() + UUID.randomUUID().toString();
  }

  //激活之后系统默认随机赠送的任务，后期可以变成指导教程
  public static Project genProject(String email) {
    Project p = new Project();
    p.setProjectName("随机赠送项目，做完送888现金大奖");
    p.setEndTime(new Date());
    Calendar curr = Calendar.getInstance();
    curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) + 7);
    Date endDate = curr.getTime();

    p.setEndTime(endDate);
    p.setModify(0);
    p.setEmail(email);
    return p;
  }

  //将2014-02-03 转换成Date格式
  public static Date string2Date(String str) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
    Date date = null;
    try {
      date = sdf.parse(str.trim());
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return date;
  }

  //两个日期的比较,前面的大返回true
  public static boolean compareDate(Date date1, Date date2) {
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);

    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);

    int day1 = cal1.get(Calendar.DATE);
    int month1 = cal1.get(Calendar.MONTH) + 1;
    int year1 = cal1.get(Calendar.YEAR);

    int day2 = cal2.get(Calendar.DATE);
    int month2 = cal2.get(Calendar.MONTH) + 1;
    int year2 = cal2.get(Calendar.YEAR);

    if (year1 > year2)
      return true;
    else if (month1 > month2)
      return true;
    else if (day1 >= day2)
      return true;
    return false;
  }

  public static String getFileType(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    String prefix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    return prefix;
  }

  public static Object parseNull2Empty(Object obj) {
    if (obj == null)
      return "";
    else
      return obj;
  }

  public static String token2Uid(String token) {
    String rex = "uid=\\d+";
    Pattern p = Pattern.compile(rex);
    Matcher m = p.matcher(token);
    if (m.find()) {
      return m.group();
    }
    return "";
  }

  public static InputStream getInputStreamByGet(String url) {
    try {
      HttpURLConnection conn = (HttpURLConnection) new URL(url)
          .openConnection();
      conn.setReadTimeout(5000);
      conn.setConnectTimeout(5000);
      conn.setRequestMethod("GET");

      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = conn.getInputStream();
        return inputStream;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  // 将服务器响应的数据流存到本地文件
  public static void saveData(InputStream is, File file) {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      bis = new BufferedInputStream(is);
      bos = new BufferedOutputStream(
          new FileOutputStream(file));
      byte[] buffer = new byte[1024];
      int len = -1;
      while ((len = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, len);
        bos.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bis != null)
          bis.close();
        if (bos != null)
          bos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void downloadPic(String url, String filePath, String fileName) {
    File file = new File(filePath, fileName);

    InputStream inputStream = UUIDUtil
        .getInputStreamByGet(url);
    UUIDUtil.saveData(inputStream, file);
  }

  public static void main(String[] args) {
    String url = "http://tva2.sinaimg.cn/crop.0.0.640.640.180/bd5b1511jw8eyutc96bf2j20hs0hstai.jpg";
    downloadPic(url, "/root", "26.jpg");
  }
}




