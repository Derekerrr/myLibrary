package com.example.mylibrary.Controller.index;

import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.query.BorrowQuery;
import com.example.mylibrary.service.BorrowService;
import com.example.mylibrary.service.MemberService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexBorrowController {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private MemberService memberService;

    //进入借阅详情页面
    @GetMapping("/borrow")
    public String borrowPage(Model model, HttpSession session){
        BorrowQuery borrowQuery = new BorrowQuery();
        borrowQuery.setMember_id(memberService.getByEmail((String) session.getAttribute("userEmail")).getId());
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        model.addAttribute("borrowQuery",borrowQuery);
        model.addAttribute("username",session.getAttribute("user"));
        return "borrow";
    }

    //搜索
    @PostMapping("/borrow/search")
    public String searchBorrow(BorrowQuery borrowQuery, Model model,HttpSession session)
    {
        borrowQuery.setPageSize(100);
        borrowQuery.setMember_id(memberService.getByEmail((String) session.getAttribute("userEmail")).getId());
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        model.addAttribute("borrowQuery", borrowQuery);
        model.addAttribute("message"," 搜索到结果共"+pageInfo.getTotal()+"条");
        return "borrow";
    }

    //翻页
    @GetMapping("/borrow/{pageNum}")
    public String book(@PathVariable Integer pageNum, BorrowQuery borrowQuery, Model model,HttpSession session){
        borrowQuery.setPageNum(pageNum);
        borrowQuery.setMember_id(memberService.getByEmail((String) session.getAttribute("userEmail")).getId());
        PageInfo<Borrow> pageInfo = borrowService.findByCondition(borrowQuery);
        model.addAttribute("page",pageInfo);
        //将类别信息也返回供选择
        model.addAttribute("borrowQuery", borrowQuery);
        return "borrow";
    }
}
