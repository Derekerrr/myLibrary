package com.example.mylibrary.Controller;


import com.example.mylibrary.dao.MemberDao;
import com.example.mylibrary.entity.*;

import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.CodeService;
import com.example.mylibrary.service.MemberService;
import com.example.mylibrary.service.TypeService;
import com.example.mylibrary.utils.MailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class loginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TypeService typeService;

    @GetMapping({"/","/login","/login.html"})
    public String login(Model model){
        model.addAttribute("loginUser",new LoginUser());
        return "login";
    }

    //发起登录请求
    @PostMapping("/login/post")
    public String loginPost(LoginUser loginUser,Model model,HttpSession session){
        Member member = memberService.getByEmail(loginUser.getEmail());
        if(member==null){
            model.addAttribute("message","该邮箱未注册！");
            model.addAttribute("loginUser",loginUser);
            return "login";
        }
        if(!member.getPassword().equals(loginUser.getPassword()))
        {
            model.addAttribute("message","密码错误！");
            model.addAttribute("loginUser",loginUser);
            return "login";
        }
        BookQuery bookQuery = new BookQuery();
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);;
//        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("bookQuery",bookQuery);
        //将类别信息也返回供选择
        model.addAttribute("types", typeService.findAll());
        session.setAttribute("user",member.getName());
        session.setAttribute("userEmail",member.getEmail());
        model.addAttribute("username",member.getName());
        return "book";
    }


    @GetMapping("/register")
    public String register(Model model)
    {
        model.addAttribute("applyMember",new ApplyMember());
        return "register";
    }


    @RequestMapping("/register/getCode/{email}")
    @ResponseBody
    public Map<String,String> getTypeName(@PathVariable String email)throws Exception {
        Member member = memberService.getByEmail(email);
        Map<String,String> map = new HashMap<>();
        if(member != null){
            map.put("message","该邮箱已经被注册了！");
        }else {
            Code code = codeService.getOneByEmail(email);
            String code_string = mailService.sendMail(email);
            if(code!=null){
                codeService.deleteByEmail(email);
                code.setCode_string(code_string);
            }else code = new Code(email, code_string);
            codeService.insertOne(code);
            map.put("message","验证码已经发送至你的邮箱！");
        }
        return map;
    }
    //新账号提交
    @PostMapping("/register/post")
    public String registerPost(ApplyMember applyMember, Model model) throws Exception{
        if(!applyMember.getPassword1().equals(applyMember.getPassword2())){
            model.addAttribute("message","两次密码不一致！");
            model.addAttribute("applyMember",applyMember);
        }else {
            Code code = codeService.getOneByEmail(applyMember.getEmail());
            if(!code.getCode_string().equals(applyMember.getCode())){
                model.addAttribute("message","验证码错误！");
                model.addAttribute("applyMember",applyMember);
            }else {//注册成功！
                Member member = new Member();
                member.setEmail(applyMember.getEmail());
                member.setPassword(applyMember.getPassword1());
                member.setMoney(0F);
                member.setBirthday(new Date());
                member.setName(applyMember.getEmail());
                memberService.insertOne(member);
                codeService.deleteByEmail(applyMember.getEmail());
                model.addAttribute("yes_message","注册成功！请返回登录页进行登录");
                model.addAttribute("applyMember",new ApplyMember());
            }
        }
        return "register";
    }

//    管理员账号登录
    @PostMapping("/admin/login")
    public String adminLoginPost(LoginUser loginUser, Model model, BookQuery bookQuery, HttpSession session){
        Admin admin = memberService.getAdmin(loginUser.getEmail());
        if(admin==null){
            model.addAttribute("message","不存在该管理员！");
            return "admin/login";
        }
        if(!admin.getPassword().equals(loginUser.getPassword())){
            model.addAttribute("message","密码错误！");
            return "admin/login";
        }
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);;
//        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        session.setAttribute("user",admin);

        //将类别信息也返回供选择
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("loginUser",loginUser);
        return "admin/book";
    }


    @GetMapping("/admin")
    public String adminLogin(Model model){
        model.addAttribute("loginUser",new LoginUser());
        return "admin/login";
    }


    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session,Model model){
        session.removeAttribute("user");
        model.addAttribute("loginUser",new LoginUser());
        return "admin/login"; 
    }
    @GetMapping("/logout")
    public String userLogout(Model model,HttpSession session){
        session.removeAttribute("user");
        model.addAttribute("loginUser",new LoginUser());
        return "login";
    }
}
