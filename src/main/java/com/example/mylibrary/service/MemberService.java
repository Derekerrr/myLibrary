package com.example.mylibrary.service;

import com.example.mylibrary.entity.Admin;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.MemberQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface MemberService {
    //    @Select("select * from member")
    List<Member> findAll();

    //    @Select("select * from member where id=#{id}")
    Member getById(int id);

    //    @Select("select * from member where email=#{email}")
    Member getByEmail(String email);

    Admin getAdmin(String name);

    PageInfo<Member> findByCondition(MemberQuery memberQuery);

    void updateById(Member member);

    void insertOne(Member member);
}
