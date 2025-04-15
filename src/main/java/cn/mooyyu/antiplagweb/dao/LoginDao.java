package cn.mooyyu.antiplagweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginDao {
    @Select("select password from user where name = #{name}")
    String getPwdByLoginName(@Param("name") String name);
}
