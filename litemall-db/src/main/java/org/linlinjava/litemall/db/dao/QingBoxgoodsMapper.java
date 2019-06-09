package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.QingBoxgoods;
import org.linlinjava.litemall.db.domain.QingBoxgoodsExample;

public interface QingBoxgoodsMapper {
    int countByExample(QingBoxgoodsExample example);

    int deleteByExample(QingBoxgoodsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(QingBoxgoods record);

    int insertSelective(QingBoxgoods record);

    List<QingBoxgoods> selectByExample(QingBoxgoodsExample example);

    QingBoxgoods selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QingBoxgoods record, @Param("example") QingBoxgoodsExample example);

    int updateByExample(@Param("record") QingBoxgoods record, @Param("example") QingBoxgoodsExample example);

    int updateByPrimaryKeySelective(QingBoxgoods record);

    int updateByPrimaryKey(QingBoxgoods record);
}