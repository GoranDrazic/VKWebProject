package com.vk.tottenham.mybatis.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.vk.tottenham.core.model.Resource;

public interface ResourceMapper {

    @Select("SELECT id as id "
            + "FROM Resources "
            + "WHERE id=#{id}")
    public Resource findById(String id);

    @Insert("INSERT INTO Resources(id) VALUES"
            + "(#{id})")
    public void save(Resource resource);

}
