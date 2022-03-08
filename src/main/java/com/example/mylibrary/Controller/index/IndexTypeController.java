package com.example.mylibrary.Controller.index;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Type;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexTypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BookService bookService;

    //访问type页面
    @GetMapping("/type")
    public String typePage(Model model, HttpSession session){
        List<Type> types = typeService.findAll();
        model.addAttribute("types",types);
        model.addAttribute("username",session.getAttribute("user"));
        return "type";
    }

    //查看该分类书籍
    @GetMapping("/type/{id}")
    public String searchByType(@PathVariable Integer id, BookQuery bookQuery, Model model,HttpSession session){
        bookQuery.setSearchType(id);
        bookQuery.setPageSize(100);
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        model.addAttribute("username",session.getAttribute("user"));
        model.addAttribute("message"," 搜索到该分类书籍结果共"+bookPageInfo.getTotal()+"条");
        return "book";
    }
}
