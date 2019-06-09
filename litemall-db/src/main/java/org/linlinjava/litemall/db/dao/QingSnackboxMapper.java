package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.QingSnackbox;
import org.linlinjava.litemall.db.domain.QingSnackboxExample;

public interface QingSnackboxMapper {
    int countByExample(QingSnackboxExample example);

    int deleteByExample(QingSnackboxExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(QingSnackbox record);

    int insertSelective(QingSnackbox record);

    List<QingSnackbox> selectByExample(QingSnackboxExample example);

    QingSnackbox selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QingSnackbox record, @Param("example") QingSnackboxExample example);

    int updateByExample(@Param("record") QingSnackbox record, @Param("example") QingSnackboxExample example);

    int updateByPrimaryKeySelective(QingSnackbox record);

    int updateByPrimaryKey(QingSnackbox record);
}