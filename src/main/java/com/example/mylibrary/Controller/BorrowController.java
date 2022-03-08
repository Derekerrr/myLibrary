package com.example.mylibrary.Controller;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.entity.query.BorrowQuery;
import com.example.mylibrary.entity.staticParam;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.BorrowService;
import com.example.mylibrary.service.MemberService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/admin/selectBook")
    public String selectBook(@RequestParam("isbn")String isbn, @RequestParam("name") String name, HttpSession session, RedirectAttributes attributes){
        //检查书库的存量
        Book book = bookService.getByIsbn(isbn);
        if(book.getNumber()<1){
            attributes.addFlashAttribute("message","操作失败！该书籍已经没有库存了！");
            return "redirect:/admin/book";
        }
        //如果已经有候选人了,则借书成功!
        if(session.getAttribute("memberId")!=null){
            Borrow borrow = new Borrow();
            borrow.setIsbn(isbn);
            borrow.setMember_id((Integer) session.getAttribute("memberId"));
            borrowService.insertOne(borrow);
            book.setNumber(book.getNumber()-1);
            bookService.modifyByIsbn(book);
            attributes.addFlashAttribute("message","借书操作成功!");
            session.removeAttribute("isbn");
            session.removeAttribute("bookName");
            session.removeAttribute("memberId");
            session.removeAttribute("memberName");
            return "redirect:/admin/book";
        }
        session.setAttribute("isbn",isbn);
        session.setAttribute("bookName",name);
        attributes.addFlashAttribute("selectMessage","已经选择书籍:"+name
                +"       "+"请选择会员以完成借书操作...");
        return "redirect:/admin/member";
    }

    @RequestMapping("/admin/selectMember")
    public String selectMember(@RequestParam("id")Integer id,@RequestParam("name")String name, HttpSession session, RedirectAttributes attributes){
        //如果已经有候选书了,则借书成功!
        if(session.getAttribute("isbn")!=null){
            Borrow borrow = new Borrow();
            borrow.setIsbn((String) session.getAttribute("isbn"));
            borrow.setMember_id(id);
            borrowService.insertOne(borrow);
            Book book = bookService.getByIsbn((String) session.getAttribute("isbn"));
            book.setNumber(book.getNumber()-1);
            bookService.modifyByIsbn(book);
            attributes.addFlashAttribute("message","借书操作成功!");
            session.removeAttribute("isbn");
            session.removeAttribute("bookName");
            session.removeAttribute("memberId");
            session.removeAttribute("memberName");
            return "redirect:/admin/member";
        }
        session.setAttribute("memberId",id);
        session.setAttribute("memberName",name);
        attributes.addFlashAttribute("selectMessage","已经选择会员:"+name
                +"      "+"请选择书籍以完成借书操作...");
        return "redirect:/admin/book";
    }

    //进入借阅详情页面
    @GetMapping("/admin/borrow")
    public String borrowPage(Model model){
        BorrowQuery borrowQuery = new BorrowQuery();
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        model.addAttribute("borrowQuery",borrowQuery);
        return "admin/borrow";
    }
    //搜索
    @PostMapping("/admin/borrow/search")
    public String searchBorrow(BorrowQuery borrowQuery, Model model)
    {
        borrowQuery.setPageSize(100);
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        model.addAttribute("borrowQuery", borrowQuery);
        model.addAttribute("message"," 搜索到结果共"+pageInfo.getTotal()+"条");
        return "admin/borrow";
    }
    //翻页
    @GetMapping("/admin/borrow/{pageNum}")
    public String book(@PathVariable Integer pageNum, BorrowQuery borrowQuery, Model model){
        borrowQuery.setPageNum(pageNum);
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        //将类别信息也返回供选择
        model.addAttribute("borrowQuery", borrowQuery);
        return "admin/borrow";
    }

    //根据member——id查询
    @GetMapping("/admin/memberBorrow")
    public String memberBorrow(@RequestParam("id")Integer id,Model model){
        BorrowQuery borrowQuery = new BorrowQuery();
        borrowQuery.setMember_id(id);
        borrowQuery.setMember_name(memberService.getById(id).getName());
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        model.addAttribute("borrowQuery",borrowQuery);
        model.addAttribute("message","共查询到该会员的借阅记录："+pageInfo.getTotal()+"条");
        return "admin/borrow";
    }


    //还书
    @GetMapping("/admin/returnBook/{id}")
    public String returnBook(@PathVariable Integer id,RedirectAttributes attributes){
        Borrow record = borrowService.getById(id);
        Book book = bookService.getByIsbn(record.getIsbn());
        book.setNumber(book.getNumber()+1);
        //先加库存
        bookService.modifyByIsbn(book);
        //在修改还书时间
        record.setIs_returned(true);
        record.setReturn_time(new Date());
        borrowService.updateOne(record);
        attributes.addFlashAttribute("message","还书操作成功！");
        return "redirect:/admin/borrow";
    }

    //罚款
    @GetMapping("/admin/punish/{id}")
    public String punish(@PathVariable Integer id,RedirectAttributes attributes){
        Borrow record = borrowService.getById(id);
        Member member = memberService.getById(record.getMember_id());
        if(member.getMoney()<staticParam.punishAmount){
            attributes.addFlashAttribute("message","操作失败！该会员余额不足！请提醒该会员尽快充值缴费！");
        }else {
            member.setMoney(member.getMoney()-staticParam.punishAmount);
            memberService.updateById(member);
            record.setIs_punished(true);
            borrowService.updateOne(record);
            attributes.addFlashAttribute("message","罚款操作成功！");
        }
        return "redirect:/admin/borrow";
    }

    //删除
    @GetMapping("/admin/borrowDelete/{id}")
    public String delete(@PathVariable Integer id,RedirectAttributes attributes){

        Borrow record = borrowService.getById(id);
        if(!record.getIs_returned()){
            attributes.addFlashAttribute("message","操作失败！该书还未归还，不能删除记录！");
        }else {
            borrowService.deleteById(id);
            attributes.addFlashAttribute("message","删除记录成功！");
        }
        return "redirect:/admin/borrow";
    }
}
