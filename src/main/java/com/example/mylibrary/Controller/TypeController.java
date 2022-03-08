package com.example.mylibrary.Controller;

import com.example.mylibrary.entity.Book;
import com.example.mylibrary.entity.Type;
import com.example.mylibrary.entity.query.BookQuery;
import com.example.mylibrary.service.BookService;
import com.example.mylibrary.service.TypeService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BookService bookService;

    @RequestMapping("/admin/getTypeName/{id}")
    @ResponseBody
    public Map<String,String> getTypeName(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response){
        Type type = typeService.getById(id);
        Map<String,String> map = new HashMap<>();
        if(type==null){
            map.put("result", "0");
            map.put("message","查询出错！");
        }else {
            map.put("result","1");
            map.put("name",type.getName());
            map.put("description",type.getDescription());
        }
        return map;
    }

    //访问type页面
    @GetMapping("/admin/type")
    public String typePage(Model model){
        List<Type> types = typeService.findAll();
        model.addAttribute("types",types);
        return "admin/type";
    }

    //查看该分类书籍
    @GetMapping("/admin/type/{id}")
    public String searchByType(@PathVariable Integer id, BookQuery bookQuery,Model model){
        bookQuery.setSearchType(id);
        bookQuery.setPageSize(100);
        PageInfo<Book> bookPageInfo = bookService.getBookList(bookQuery);
        model.addAttribute("page",bookPageInfo);
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("bookQuery", bookQuery);
        model.addAttribute("message"," 搜索到该分类书籍结果共"+bookPageInfo.getTotal()+"条");
        return "admin/book";
    }

    //新增分类
    @GetMapping("/admin/addType")
    public String addType(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }
    //新分类提交
    @PostMapping("/admin/typePost")
    public String typePost(Type type,Model model){
        if(type.getName()==null||type.getDescription()==null){
            model.addAttribute("message","信息不能为空！");
            model.addAttribute("type",type);
            return "admin/type-input";
        }
        typeService.insertOne(type);
        model.addAttribute("message","添加成功！");
        model.addAttribute("types",typeService.findAll());
        return "admin/type";
    }
    //分类修改提交
    @PostMapping("/admin/typeModifyPost")
    public String typeModifyPost(Type type, RedirectAttributes attributes){
        if(type.getName()==null || type.getDescription() == null){
            attributes.addFlashAttribute("message","修改失败！信息不完善");
        }
        else {
            typeService.updateById(type);
            attributes.addFlashAttribute("message","修改成功！");
        }
        return "redirect:/admin/type";
    }

    @GetMapping("/admin/typeDelete/{id}")
    public String deleteType(@PathVariable Integer id, RedirectAttributes attributes)
    {
        BookQuery bookQuery = new BookQuery();
        bookQuery.setSearchType(id);
        PageInfo<Book> books = bookService.getBookList(bookQuery);
        if(books.getTotal()>0){
            attributes.addFlashAttribute("message","操作失败！该类已图书，不能删除！");
        }else {
            typeService.deleteById(id);
            attributes.addFlashAttribute("message","操作成功！");
        }
        return "redirect:/admin/type";
    }
}
