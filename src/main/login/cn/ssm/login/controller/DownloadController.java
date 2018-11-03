package cn.ssm.login.controller;

import cn.ssm.common.bean.User;
import cn.ssm.common.utils.ExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DownloadController {
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void dowloadZip(HttpServletResponse response) throws Exception {
        //3条数据，开发时从数据库或者别的地方获取
        List<Object> dataList = new ArrayList<>();
        dataList.add(new User("燕成龙",22));
        dataList.add(new User("焦燕飞",22));
        dataList.add(new User("燕依晨",7));
        //创建setting对象,具体设置请看参数详情
        ExcelUtils.Setting setting = new ExcelUtils().new Setting(true);
        //导出指定列
        setting.setFilterField(new String[]{"name","age"});//类中的属性名
        setting.setFieldName(new String[]{"姓名","年龄"});//导出时属性对应的中文名
        setting.setColumnWidth(new int[]{2000,3000});//列宽
        setting.setFileName("ZJCA");
        setting.setSheetName("sheet1");//sheet名
        setting.setTitle("测试生成excel表格");//表内第一行标题名
        try {
            //将要下载的文件流写到response的输出流中
            ExcelUtils.create(setting, dataList, User.class,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}




