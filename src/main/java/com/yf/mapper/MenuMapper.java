package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.domain.Menu;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(@Param("userid") Long userid);
}
