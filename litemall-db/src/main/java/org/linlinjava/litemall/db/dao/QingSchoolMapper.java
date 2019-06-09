package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.QingSchool;
import org.linlinjava.litemall.db.domain.QingSchoolExample;

public interface QingSchoolMapper {
    int countByExample(QingSchoolExample example);

    int deleteByExample(QingSchoolExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(QingSchool record);

    int insertSelective(QingSchool record);

    List<QingSchool> selectByExample(QingSchoolExample example);

    QingSchool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QingSchool record, @Param("example") QingSchoolExample example);

    int updateByExample(@Param("record") QingSchool record, @Param("example") QingSchoolExample example);

    int updateByPrimaryKeySelective(QingSchool record);

    int updateByPrimaryKey(QingSchool record);
}