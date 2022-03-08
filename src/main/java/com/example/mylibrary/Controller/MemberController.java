package com.example.mylibrary.Controller;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.entity.query.MemberQuery;
import com.example.mylibrary.service.MemberService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/admin/member")
    public String memberPage(Model model, MemberQuery memberQuery, HttpSession session){

        PageInfo<Member> bookPageInfo = memberService.findByCondition(memberQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("memberQuery", memberQuery);
        if(session.getAttribute("isbn")!=null){
            model.addAttribute("selectMessage","已经选择书籍:"+session.getAttribute("bookName")
                    +"       "+"请选择会员以完成借书操作...");
        }
        return "admin/member";
    }

    @GetMapping("/admin/member/{pageNum}")
    public String book(@PathVariable Integer pageNum, MemberQuery memberQuery, Model model,HttpSession session){
        memberQuery.setPageNum(pageNum);
        PageInfo<Member> bookPageInfo = memberService.findByCondition(memberQuery);
        model.addAttribute("page",bookPageInfo);
        //将类别信息也返回供选择
        model.addAttribute("memberQuery", memberQuery);
        if(session.getAttribute("isbn")!=null){
            model.addAttribute("selectMessage","已经选择书籍:"+session.getAttribute("bookName")
                    +"       "+"请选择会员以完成借书操作...");
        }
        return "admin/member";
    }

    //搜索
    @PostMapping("/admin/member/search")
    public String searchBook(MemberQuery memberQuery, Model model,HttpSession session){
        memberQuery.setPageSize(100);
        if(memberQuery.getName().equals("")){
            memberQuery.setName(null);
        }
        if(memberQuery.getEmail().equals("")){
            memberQuery.setEmail(null);
        }
        PageInfo<Member> bookPageInfo = memberService.findByCondition(memberQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("memberQuery", memberQuery);
        model.addAttribute("message"," 搜索到结果共"+bookPageInfo.getTotal()+"条");
        if(session.getAttribute("isbn")!=null){
            model.addAttribute("selectMessage","已经选择书籍:"+session.getAttribute("bookName")
                    +"       "+"请选择会员以完成借书操作...");
        }
        return "admin/member";
    }

    //金额修改
    @PostMapping("/admin/moneyModifyPost")
    public String moneyModify(Member member, RedirectAttributes attributes){
        Member member1 = memberService.getById(member.getId());
        member1.setMoney(member.getMoney());
        memberService.updateById(member1);
        attributes.addFlashAttribute("message","修改余额成功!");
        return "redirect:/admin/member";
    }
}
