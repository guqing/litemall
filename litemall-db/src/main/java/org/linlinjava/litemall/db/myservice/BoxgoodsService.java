package org.linlinjava.litemall.db.myservice;

import org.linlinjava.litemall.db.dao.QingBoxgoodsMapper;
import org.linlinjava.litemall.db.domain.QingBoxgoods;
import org.linlinjava.litemall.db.domain.QingBoxgoodsExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class BoxgoodsService {
    @Resource
    private QingBoxgoodsMapper boxgoodsMapper;

    public void save(List<QingBoxgoods> boxgoodsList, int snackboxId){
        for(QingBoxgoods boxgoods : boxgoodsList){
            //设置零食盒id,即该商品被哪一个零食盒子引用
            boxgoods.setSnackboxId(snackboxId);
            boxgoods.setCreateTime(new Date());
            boxgoods.setModifyTime(new Date());
            boxgoodsMapper.insertSelective(boxgoods);
        }
    }

    public List<QingBoxgoods> getGoodsById(Integer snackboxId){
        QingBoxgoodsExample example = new QingBoxgoodsExample();
        QingBoxgoodsExample.Criteria criteria = example.createCriteria();
        criteria.andSnackboxIdEqualTo(snackboxId);

        return boxgoodsMapper.selectByExample(example);
    }
}
