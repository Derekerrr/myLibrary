package com.example.mylibrary.service;

import com.example.mylibrary.dao.BorrowDao;
import com.example.mylibrary.entity.Borrow;
import com.example.mylibrary.entity.query.BorrowQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BorrowServiceImp implements BorrowService{

    @Autowired
    private BorrowDao borrowDao;

    @Override
    public void insertOne(Borrow borrow) {
        borrow.setBorrow_time(new Date());
        borrow.setIs_overdue(false);
        borrow.setIs_returned(false);
        borrowDao.insertOne(borrow);
    }

    @Override
    public PageInfo<Borrow> findByCondition(BorrowQuery borrowQuery) {
        if(borrowQuery.getOrder()==1){
            borrowQuery.setOrder1("11");
        }else {
            borrowQuery.setOrder2("22");
        }

        if(borrowQuery.getStatus()==2){
            borrowQuery.setStatus2("22");
        }else if (borrowQuery.getStatus()==3){
            borrowQuery.setStatus3("33");
        }else if(borrowQuery.getStatus()==4){
            borrowQuery.setStatus4("44");
        }

        //先检查更新日期
        Date lastUpdateTime = borrowDao.lastUpdateTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();//今天的日期

        if(!sdf.format(lastUpdateTime).equals(sdf.format(now))){
            Calendar dateTemplate = Calendar.getInstance();
            List<Borrow> list = borrowDao.findByCondition(new BorrowQuery());
            for(Borrow record : list){
                Date borrow_time = record.getBorrow_time();//借书日期
                dateTemplate.setTime(borrow_time);
                dateTemplate.add(Calendar.DAY_OF_YEAR,record.getDays());
                Date due_time = dateTemplate.getTime();//截至日期
                if(due_time.before(now)){
                    //超过截至日期
                    record.setIs_overdue(true);
                    borrowDao.updateOne(record);//更新
                }
            }
            //更新上次更新日期
            borrowDao.updateLastDate(now);
        }
        PageHelper.startPage(borrowQuery.getPageNum(),borrowQuery.getPageSize());
        return new PageInfo<>(borrowDao.findByCondition(borrowQuery));
    }

    @Override
    public List<Borrow> findByIsbn(String isbn) {
        return borrowDao.findByIsbn(isbn);
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowDao.getById(id);
    }

    @Override
    public void updateOne(Borrow borrow) {
        borrowDao.updateOne(borrow);
    }

    @Override
    public void deleteById(Integer id) {
        borrowDao.deleteById(id);
    }
}
