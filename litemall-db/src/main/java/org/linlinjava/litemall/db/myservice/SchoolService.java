package org.linlinjava.litemall.db.myservice;
import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.QingSchoolMapper;
import org.linlinjava.litemall.db.domain.QingSchool;
import org.linlinjava.litemall.db.domain.QingSchoolExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SchoolService {
    @Resource
    private QingSchoolMapper mapper;

    public void save(QingSchool school){
        school.setCreateTime(new Date());
        school.setModifyTime(new Date());
        mapper.insertSelective(school);
    }

    public List<QingSchool> querySelective(String address, String name, Integer page, Integer limit){
        QingSchoolExample qingSchoolExample = new QingSchoolExample();
        QingSchoolExample.Criteria criteria = qingSchoolExample.createCriteria();

        if (!StringUtils.isEmpty(address)) {
            criteria.andAddressLike("%" + address + "%");
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        PageHelper.startPage(page, limit);
        return mapper.selectByExample(qingSchoolExample);
    }

    public List<QingSchool> queryAll(){
        return mapper.selectByExample(null);
    }

    public void delete(Integer id){
        mapper.deleteByPrimaryKey(id);
    }

    public QingSchool getById(Integer id){
        return mapper.selectByPrimaryKey(id);
    }

    public int updateById(QingSchool school){
        school.setModifyTime(new Date());
        return mapper.updateByPrimaryKeySelective(school);
    }
}
