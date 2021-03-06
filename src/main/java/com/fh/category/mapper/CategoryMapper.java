package com.fh.category.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {

    @Select("select * from t_category")
    List<Map<String, Object>> queryCategoryList();
}
