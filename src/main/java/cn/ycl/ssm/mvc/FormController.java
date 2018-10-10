package cn.ycl.ssm.mvc;

import cn.ycl.ssm.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FormController {
    @RequestMapping("/userInfo")
    public String userInfo(Model model){
        User user = new User();
        user.setName("焦燕飞");
        user.setGender('女');
        user.setAge(24);
        model.addAttribute("user",user);
        return "user";
    }


}
