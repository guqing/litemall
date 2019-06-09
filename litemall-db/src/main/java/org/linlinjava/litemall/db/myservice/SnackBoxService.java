package org.linlinjava.litemall.db.myservice;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallGoodsMapper;
import org.linlinjava.litemall.db.dao.QingBoxgoodsMapper;
import org.linlinjava.litemall.db.dao.QingSnackboxMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SnackBoxService {
    @Resource
    private QingSnackboxMapper snackboxMapper;
    @Resource
    private QingBoxgoodsMapper boxgoodsMapper;

    @Resource
    private LitemallGoodsMapper goodsMapper;


    public int save(QingSnackbox snackbox){
        snackbox.setCreateTime(new Date());
        snackbox.setModifyTime(new Date());
        snackboxMapper.insertSelective(snackbox);
        return snackbox.getId();
    }

    public List<QingSnackbox> querySelective(String school, String name, Integer page, Integer limit){
        QingSnackboxExample snackboxExample = new QingSnackboxExample();
        QingSnackboxExample.Criteria criteria = snackboxExample.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        if (!StringUtils.isEmpty(school)) {
            criteria.andSchoolLike("%" + school + "%");
        }
        PageHelper.startPage(page, limit);
        return snackboxMapper.selectByExample(snackboxExample);
    }

    public void delete(Integer id){
        snackboxMapper.deleteByPrimaryKey(id);

        //删除盒中商品引用
        QingBoxgoodsExample example = new QingBoxgoodsExample();
        QingBoxgoodsExample.Criteria criteria = example.createCriteria();
        criteria.andSnackboxIdEqualTo(id);
        boxgoodsMapper.deleteByExample(example);
    }

    public int updateById(QingSnackbox snackBox, List<QingBoxgoods> boxgoodsList){
        snackBox.setModifyTime(new Date());
        int updateRecode = snackboxMapper.updateByPrimaryKeySelective(snackBox);

        //先删除盒子商品数据在添加
        QingBoxgoodsExample example = new QingBoxgoodsExample();
        QingBoxgoodsExample.Criteria criteria = example.createCriteria();
        criteria.andSnackboxIdEqualTo(snackBox.getId());
        boxgoodsMapper.deleteByExample(example);
        //添加
        for(QingBoxgoods boxgoods : boxgoodsList){
            boxgoods.setSnackboxId(snackBox.getId());
            boxgoods.setCreateTime(new Date());
            boxgoods.setModifyTime(new Date());
            boxgoodsMapper.insertSelective(boxgoods);
        }
        return updateRecode;
    }

    public List<LitemallGoods> queryGoodsByName(String name){
        LitemallGoodsExample example = new LitemallGoodsExample();
        LitemallGoodsExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        return goodsMapper.selectByExample(example);
    }

    public QingSnackbox getBoxById(Integer id){
        return snackboxMapper.selectByPrimaryKey(id);
    }
}
