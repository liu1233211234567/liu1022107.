package com.fh.mamber.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.mamber.model.Mamber;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MamberMapper extends BaseMapper<Mamber> {

    // 查询账号
    @Select("select * from t_mamber where userName=#{userName}")
    Mamber userNameList(String usrName);

    // 查询号码
    @Select("select * from t_mamber where phoneNumber=#{phoneNumber}")
    Mamber phoneNumberList(String phoneNumber);
    //  添加
    @Insert("insert into t_mamber (userName,userPassword,phoneNumber) values (#{userName},#{userPassword},#{phoneNumber})")
    void addMamberList(Mamber mamber);

    // 查询
    @Select("select * from t_mamber where phoneNumber=#{phoneNumber} or userName=#{userName}")
    Mamber selectUserName(@Param("userName") String userName,@Param("phoneNumber") String phoneNumber);
}
