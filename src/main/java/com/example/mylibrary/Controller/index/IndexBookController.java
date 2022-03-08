package com.example.mylibrary.Controller.index;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private TypeService typeService;

    //搜索
    @PostMapping("/book/search")
    public String searchBook(BookQuery bookQuery, Model model, HttpSession session){
        bookQuery.setPageSize(100);
        if(bookQuery.getSearchName().equals("")){
            bookQuery.setSearchName(null);
        }
        if(bookQuery.getSearchAuthor().equals("")){
            bookQuery.setSearchAuthor(null);
        }
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        model.addAttribute("username",session.getAttribute("user"));
        model.addAttribute("message"," 搜索到结果共"+bookPageInfo.getTotal()+"条");
        return "book";
    }

    @GetMapping("/book")
    public String bookPage(Model model,HttpSession session){
       BookQuery bookQuery = new BookQuery();
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("username",session.getAttribute("user"));
        model.addAttribute("bookQuery", bookQuery);
        return "book";
    }

    @GetMapping("/book/{pageNum}")
    public String book(@PathVariable Integer pageNum, BookQuery bookQuery, Model model,HttpSession session){
        bookQuery.setPageNum(pageNum);
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        //将类别信息也返回供选择
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        model.addAttribute("username",session.getAttribute("user"));
        return "book";
    }
}
