package com.example.mylibrary.Controller;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.BorrowService;
import com.example.mylibrary.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private BorrowService borrowService;

    @GetMapping("/admin/book/{pageNum}")
    public String book(@PathVariable Integer pageNum, BookQuery bookQuery, Model model,HttpSession session){
        bookQuery.setPageNum(pageNum);
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        //将类别信息也返回供选择
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        if(session.getAttribute("memberId")!=null){
            model.addAttribute("selectMessage","已经选择会员:"+session.getAttribute("memberName")
                    +"       "+"请选择书籍以完成借书操作...");
        }
        return "admin/book";
    }

    @GetMapping("/admin/book")
    public String bookPage(BookQuery bookQuery, Model model,HttpSession session){
        bookQuery.setSearchAuthor(null);
        bookQuery.setSearchType(null);
        bookQuery.setSearchName(null);
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        if(session.getAttribute("memberId")!=null){
            model.addAttribute("selectMessage","已经选择会员:"+session.getAttribute("memberName")
                    +"       "+"请选择书籍以完成借书操作...");
        }
        return "admin/book";
    }

    //搜索
    @PostMapping("/admin/book/search")
    public String searchBook(BookQuery bookQuery, Model model,HttpSession session){
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
        model.addAttribute("message"," 搜索到结果共"+bookPageInfo.getTotal()+"条");
        if(session.getAttribute("memberId")!=null){
            model.addAttribute("selectMessage","已经选择会员:"+session.getAttribute("memberName")
                    +"       "+"请选择书籍以完成借书操作...");
        }
        return "admin/book";
    }

    //新书入库
    @GetMapping("/admin/addBook")
    public String addBook(Model model){
        model.addAttribute("types",typeService.findAll());
        model.addAttribute("book",new Book());
        return "admin/book-input";
    }
    //提交新书
    @PostMapping("/admin/bookPost")
    public String bookPost(Book book, Model model, RedirectAttributes attributes){
        if(book.getName()==null||book.getAuthor()==null||book.getIsbn()==null
            ||book.getDescription()==null||book.getPrice()==null||book.getType()==null||
                book.getNumber()==null){
            model.addAttribute("message","书籍信息不完整！");
            model.addAttribute("book",book);
            model.addAttribute("types",typeService.findAll());
            return "admin/book-input";
        }
        //检查isbn是否重复
        Book book1 = bookService.getByIsbn(book.getIsbn());
        if(book1!=null){
            model.addAttribute("message","已存在该ISBN！");
            model.addAttribute("book",book);
            model.addAttribute("types",typeService.findAll());
            return "admin/book-input";
        }
        bookService.insertBook(book);
        attributes.addFlashAttribute("message","入库成功！");
        return "redirect:/admin/book";
    }
    //修改书籍信息
    @GetMapping("/admin/bookModify/{isbn}")
    public String bookModify(@PathVariable String isbn,Model model,RedirectAttributes attributes){
        Book book = bookService.getByIsbn(isbn);
        if(book==null){
            attributes.addFlashAttribute("message","出错！未找到该ISBN对应的书籍信息！");
            return "redirect:/admin/book";
        }
        model.addAttribute("book",book);
        model.addAttribute("types", typeService.findAll());
        return "admin/book-modify";
    }
    //修改书籍信息提交
    @PostMapping("/admin/bookModifyPost")
    public String modifyPost(Book book,Model model, RedirectAttributes attributes){
        if(book.getName()==null||book.getAuthor()==null||book.getIsbn()==null
                ||book.getDescription()==null||book.getPrice()==null||book.getType()==null||
                book.getNumber()==null){
            model.addAttribute("message","书籍信息不完整！");
            model.addAttribute("book",book);
            model.addAttribute("types",typeService.findAll());
            return "admin/book-modify";
        }
        bookService.modifyByIsbn(book);
        attributes.addFlashAttribute("message","修改成功！");
        return "redirect:/admin/book";
    }

    //删除
    @GetMapping("/admin/bookDelete/{isbn}")
    public String deleteBook(@PathVariable String isbn,RedirectAttributes attributes){
        List<Borrow> recordList = borrowService.findByIsbn(isbn);
        if(recordList.size()>0){
            attributes.addFlashAttribute("message","操作失败！该书已有外借，不能移除书库！");
        }else {
            bookService.deleteByIsbn(isbn);
            attributes.addFlashAttribute("message","操作成功！");
        }
        return "redirect:/admin/book";
    }

}
