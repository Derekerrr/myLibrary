package com.example.mylibrary.dao;

import com.example.mylibrary.entity.Admin;
import com.example.mylibrary.entity.Member;
import com.example.mylibrary.entity.query.MemberQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberDao {
//    @Select("select * from member")
    List<Member> findAll();

//    @Select("select * from member where id=#{id}")
    Member getById(int id);

//    @Select("select * from member where email=#{email}")
    Member getByEmail(String email);

    Admin getAdmin(String name);

    List<Member> findByCondition(MemberQuery memberQuery);

    void updateById(Member member);

    void insertOne(Member member);
}
