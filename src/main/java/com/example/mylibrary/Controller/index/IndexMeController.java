package com.example.mylibrary.Controller.index;

import com.example.mylibrary.entity.Apply;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.Money;
import com.example.mylibrary.entity.Password;
import com.example.mylibrary.entity.query.ApplyQuery;
import com.example.mylibrary.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class IndexMeController {

    @Autowired
    private MemberService memberService;


    @GetMapping("/me")
    public String mePage(Model model, HttpSession session){
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        model.addAttribute("member",member);
        model.addAttribute("username",session.getAttribute("user"));
        return "me";
    }

    @GetMapping("/me/newName/{name}")
    public String newName(@PathVariable String name, RedirectAttributes attributes,HttpSession session){
        if(name==null){
            attributes.addFlashAttribute("message","修改失败！姓名不能为空！");
            return "redirect:/me";
        }
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        member.setName(name);
        memberService.updateById(member);
        attributes.addFlashAttribute("message","修改成功！");
        session.setAttribute("user",name);
        return "redirect:/me";
    }

    @GetMapping("/me/newBirthday/{birthday}")
    public String newBirthday(@PathVariable String birthday, RedirectAttributes attributes,HttpSession session){
        if(birthday==null){
            attributes.addFlashAttribute("message","修改失败！生日不能为空！");
            return "redirect:/me";
        }
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = ft.parse(birthday);
            member.setBirthday(date);
        } catch (ParseException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("message","修改失败！格式不符！");
            return "redirect:/me";
        }
        memberService.updateById(member);
        attributes.addFlashAttribute("message","修改成功！");
        return "redirect:/me";
    }

    //修改密码
    @PostMapping("/me/newPassword")
    public String newPassword(Password password,RedirectAttributes attributes,HttpSession session){
        if(password.getNewPassword1()==null||password.getNewPassword2()==null||
            password.getOldPassword()==null){
            attributes.addFlashAttribute("message","修改失败！密码为空！");
            return "redirect:/me";
        }
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        if(!Objects.equals(member.getPassword(), password.getOldPassword())){
            attributes.addFlashAttribute("message","修改失败！原密码错误！");
            return "redirect:/me";
        }
        if(!password.getNewPassword1().equals(password.getNewPassword2())){
            attributes.addFlashAttribute("message","修改失败！新密码不一致！");
            return "redirect:/me";
        }
        member.setPassword(password.getNewPassword1());
        memberService.updateById(member);
        attributes.addFlashAttribute("message","修改成功！");
        return "redirect:/me";
    }
    //充值
    //修改密码
    @PostMapping("/me/charge")
    public String newPassword(Money money, RedirectAttributes attributes, HttpSession session){
        if(money.getCharge_money()==null){
            attributes.addFlashAttribute("message","修改失败！充值金额为空！");
            return "redirect:/me";
        }
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        member.setMoney(member.getMoney()+money.getCharge_money());
        memberService.updateById(member);
        attributes.addFlashAttribute("message","充值成功！");
        return "redirect:/me";
    }
}
