package com.example.mylibrary.Controller;

import com.example.mylibrary.entity.Apply;
import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.ApplyQuery;
import com.example.mylibrary.service.ApplyService;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.BorrowService;
import com.example.mylibrary.service.MemberService;
import com.example.mylibrary.utils.MailNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailNotice mailNotice;

    @GetMapping("/admin/apply")
    public String applyPage(Model model){
        ApplyQuery applyQuery = new ApplyQuery();
        List<Apply> applies = applyService.getByCondition(applyQuery);
        model.addAttribute("applies",applies);
        model.addAttribute("applyQuery",applyQuery);
        return "admin/apply";
    }

    @PostMapping("/admin/apply/search")
    public String applySearch(ApplyQuery applyQuery,Model model){
        List<Apply> applies = applyService.getByCondition(applyQuery);
        model.addAttribute("applies",applies);
        model.addAttribute("applyQuery",applyQuery);
        model.addAttribute("message","共搜索到结果"+applies.size()+"条");
        return "admin/apply";
    }

    @GetMapping("/admin/agree/{id}")
    public String agree(@PathVariable Integer id, RedirectAttributes attributes){
        Apply apply = applyService.getById(id);
        Book book = bookService.getByIsbn(apply.getIsbn());
        if(apply.getType()==1){
            //借书申请
            if(book.getNumber()<1){
                //书库没存量了
                attributes.addFlashAttribute("message","失败！该书没有库存了！");
            }else {
                //插入一条借书数据
                Borrow borrow = new Borrow();
                borrow.setBorrow_time(new Date());
                borrow.setMember_id(apply.getMember_id());
                borrow.setIsbn(apply.getIsbn());
                borrow.setIs_returned(false);
                borrow.setIs_overdue(false);
                borrowService.insertOne(borrow);

                //库存-1
                book.setNumber(book.getNumber()-1);
                bookService.modifyByIsbn(book);
                attributes.addFlashAttribute("message","借书成功！");
            }
        }else if(apply.getType()==2){
            //还书申请
            Borrow borrow = borrowService.getById(apply.getBorrow_id());
            borrow.setReturn_time(new Date());
            borrow.setIs_returned(true);
            borrowService.updateOne(borrow);
            //书库+1
            book.setNumber(book.getNumber()+1);
            bookService.modifyByIsbn(book);
            attributes.addFlashAttribute("message","换书成功！");
        }else if(apply.getType()==3){
            //续借申请
            Borrow borrow = borrowService.getById(apply.getBorrow_id());
            borrow.setDays(borrow.getDays()+apply.getDays());
            //判断是否逾期
            Calendar dateTemplate = Calendar.getInstance();
            Date borrow_time = borrow.getBorrow_time();//借书日期
            dateTemplate.setTime(borrow_time);
            dateTemplate.add(Calendar.DAY_OF_YEAR,borrow.getDays());
            Date due_time = dateTemplate.getTime();//截至日期
            //超过截至日期
            borrow.setIs_overdue(due_time.before(new Date()));

            borrowService.updateOne(borrow);
            attributes.addFlashAttribute("message","续借成功！");
        }else {
            attributes.addFlashAttribute("message","失败！数据出错！");
            return "redirect:/admin/apply";
        }
        //删除申请
        applyService.deleteById(id);
        //邮件通知
        Member member = memberService.getById(apply.getMember_id());
        String type = "";
        switch (apply.getType()){
            case 1: type="借书";break;
            case 2: type="还书";break;
            case 3: type="续借";break;
        }
        String text = member.getName()+",您好!您在"+ apply.getTime()+"发起的"+type+"申请（《"+book.getName()+"》）已通过！";
        try {
            mailNotice.sendMail(member.getEmail(),text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/apply";
    }

    @GetMapping("/admin/refuse/{id}")
    public String refuse(@PathVariable Integer id, RedirectAttributes attributes){
        Apply apply = applyService.getById(id);
        Book book = bookService.getByIsbn(apply.getIsbn());
        //删除申请
        applyService.deleteById(id);
        //邮件通知
        Member member = memberService.getById(apply.getMember_id());
        String type = "";
        switch (apply.getType()){
            case 1: type="借书";break;
            case 2: type="还书";break;
            case 3: type="续借";break;
        }
        String text = member.getName()+",您好!您在"+ apply.getTime()+"发起的"+type+"申请（《"+book.getName()+"》）已被管理员拒绝！";
        try {
            mailNotice.sendMail(member.getEmail(),text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        attributes.addFlashAttribute("message","操作成功！");
        return "redirect:/admin/apply";
    }
}
