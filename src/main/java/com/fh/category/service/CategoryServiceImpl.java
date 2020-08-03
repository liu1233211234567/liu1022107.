package com.fh.category.service;


import com.alibaba.fastjson.JSONArray;
import com.fh.category.mapper.CategoryMapper;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Resource
    private CategoryMapper categoryMapper;


    public ServerResponse queryCategoryList() {
        // 压力测试整合
        boolean exist = RedisUtil.exist(SystemConstant.REDIS_CATEGORY_KEY);
        if(exist){
            String s = RedisUtil.get(SystemConstant.REDIS_CATEGORY_KEY);
            List<Map> mapList = JSONArray.parseArray(s, Map.class);
            return ServerResponse.success(mapList);
        }
        //

        List<Map<String, Object>> allList=categoryMapper.queryCategoryList();
        List<Map<String,Object>> pareantList=new ArrayList<Map<String, Object>>();
        for(Map map: allList){
            if(map.get("pid").equals(0)){
                pareantList.add(map);
            }
        }

        selectCate(pareantList,allList);

        String toJSONString = JSONArray.toJSONString(pareantList);
        RedisUtil.set(SystemConstant.REDIS_CATEGORY_KEY,toJSONString);
        return ServerResponse.success(pareantList);
    }


    public void  selectCate(List<Map<String, Object>> pareantList ,List<Map<String,Object>> allList ){
        for (Map<String, Object> pmap : pareantList) {
            List<Map<String,Object>> chidrenList=new ArrayList<Map<String, Object>>();
            for (Map<String, Object> amap : allList) {
                if(pmap.get("id").equals(amap.get("pid"))){
                    chidrenList.add(amap);
                }
            }
            if(chidrenList!=null && chidrenList.size()>0){
                pmap.put("children",chidrenList);
                selectCate(chidrenList,allList);
            }
        }
    }


}
