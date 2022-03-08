package com.example.mylibrary.Controller.index;

import com.example.mylibrary.entity.*;
import com.example.mylibrary.entity.query.ApplyQuery;
import com.example.mylibrary.service.ApplyService;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.BorrowService;
import com.example.mylibrary.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexApplyController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private BorrowService borrowService;

    @GetMapping("/apply")
    public String applyPage(Model model,HttpSession session){
        ApplyQuery applyQuery = new ApplyQuery();
        applyQuery.setMember_id(memberService.getByEmail((String) session.getAttribute("userEmail")).getId());
        List<Apply> applies = applyService.getByCondition(applyQuery);
        model.addAttribute("applies",applies);
        model.addAttribute("username",session.getAttribute("user"));
        return "apply";
    }

    @GetMapping("/book/borrow")
    public String borrowApply(@RequestParam("isbn")String isbn, RedirectAttributes attributes, HttpSession session){
        Book book = bookService.getByIsbn(isbn);
        if(book.getNumber()<1){
            attributes.addFlashAttribute("message","申请失败！该书已经没有库存了！");
            return "redirect:/book";
        }


        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        Apply apply = new Apply();
        apply.setIsbn(isbn);
        apply.setTime(new Date());
        apply.setMember_id(member.getId());
        apply.setBook_name(book.getName());
        apply.setType(1);
        apply.setMember_name(member.getName());
        applyService.insertOne(apply);

        attributes.addFlashAttribute("message","借书申请交成功！稍后将通过邮件的方式提醒您！您也可以在‘我的申请’查看信息");
        return "redirect:/book";
    }

    @GetMapping("/book/return/{id}")
    public String returnApply(@PathVariable Integer id,RedirectAttributes attributes,HttpSession session){
        Borrow borrow = borrowService.getById(id);
        if(borrow.getIs_overdue() && !borrow.getIs_punished())
        {
            attributes.addFlashAttribute("message","您尚未缴纳逾期金额，不能提交还书申请！");
            return "redirect:/borrow";
        }
        Member member = memberService.getByEmail((String) session.getAttribute("userEmail"));
        Book book = bookService.getByIsbn(borrow.getIsbn());
        Apply apply = new Apply();
        apply.setIsbn(borrow.getIsbn());
        apply.setTime(new Date());
        apply.setMember_id(member.getId());
        apply.setBook_name(book.getName());
        apply.setType(2);
        apply.setBorrow_id(borrow.getId());
        apply.setMember_name(member.getName());
        applyService.insertOne(apply);
        attributes.addFlashAttribute("message","还书申请交成功！稍后将通过邮件的方式提醒您！您也可以在‘我的申请’查看信息");
        return "redirect:/borrow";
    }

    @GetMapping("/apply/delete/{id}")
    public String deleteApply(@PathVariable Integer id,RedirectAttributes attributes){
        applyService.deleteById(id);
        attributes.addFlashAttribute("message","撤销申请成功！");
        return "redirect:/apply";
    }

    //续借
    @PostMapping("/apply/renew")
    public String renew(Renew renew,RedirectAttributes attributes){
        Borrow borrow = borrowService.getById(renew.getId());
        Member member = memberService.getById(borrow.getMember_id());
        Book book = bookService.getByIsbn(borrow.getIsbn());
        Apply apply = new Apply();
        apply.setIsbn(borrow.getIsbn());
        apply.setTime(new Date());
        apply.setMember_id(member.getId());
        apply.setBook_name(book.getName());
        apply.setType(3);
        apply.setBorrow_id(borrow.getId());
        apply.setMember_name(member.getName());
        apply.setDays(renew.getDays());
        applyService.insertOne(apply);
        attributes.addFlashAttribute("message","续借申请交成功！稍后将通过邮件的方式提醒您！您也可以在‘我的申请’查看信息");
        return "redirect:/borrow";
    }
}
