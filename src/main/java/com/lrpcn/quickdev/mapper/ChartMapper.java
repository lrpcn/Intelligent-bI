package com.lrpcn.quickdev.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrpcn.quickdev.model.domain.Chart;

import java.util.List;

/**
* @author Administrator
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-02-12 10:52:59
* @Entity generator.domain.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    List<Long> selectPageIds(int current, int size,String name);
}




