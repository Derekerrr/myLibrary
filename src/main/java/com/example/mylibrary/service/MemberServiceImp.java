package com.example.mylibrary.service;

import com.example.mylibrary.dao.MemberDao;
import com.example.mylibrary.entity.Admin;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.MemberQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImp implements MemberService{

    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    @Override
    public Member getById(int id) {
        return memberDao.getById(id);
    }

    @Override
    public Member getByEmail(String email) {
        return memberDao.getByEmail(email);
    }

    @Override
    public Admin getAdmin(String name) {
        return memberDao.getAdmin(name);
    }

    @Override
    public PageInfo<Member> findByCondition(MemberQuery memberQuery) {
        PageHelper.startPage(memberQuery.getPageNum(),memberQuery.getPageSize());
        return new PageInfo<>(memberDao.findByCondition(memberQuery));
    }

    @Override
    public void updateById(Member member) {
        memberDao.updateById(member);
    }

    @Override
    public void insertOne(Member member) {
        memberDao.insertOne(member);
    }
}
