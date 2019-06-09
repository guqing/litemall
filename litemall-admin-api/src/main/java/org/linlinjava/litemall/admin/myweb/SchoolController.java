package org.linlinjava.litemall.admin.myweb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.QingSchool;
import org.linlinjava.litemall.db.myservice.SchoolService;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/school")
@Validated
public class SchoolController {
    private static final Log logger = LogFactory.getLog(SchoolController.class);

    @Resource
    private SchoolService schoolService;

    @RequiresPermissions("admin:school:create")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody QingSchool school){
        Object error = validate(school);

        if (error != null) {
            return error;
        }
        schoolService.save(school);

        return ResponseUtil.ok(school);
    }

    @RequiresPermissions("admin:school:list")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="查找")
    @GetMapping("/list")
    public Object list(String address, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit){
        List<QingSchool> schoolList = schoolService.querySelective(address, name, page, limit);
        return ResponseUtil.okList(schoolList);
    }


    @RequiresPermissions("admin:school:delete")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="删除")
    @PostMapping("/delete/{id}")
    public Object deleteById(@PathVariable("id") Integer id){
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        schoolService.delete(id);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:school:queryAll")
    @GetMapping("/queryAll")
    public Object queryAll(){
        List<QingSchool> schoolList = schoolService.queryAll();
        return ResponseUtil.okList(schoolList);
    }


    @RequiresPermissions("admin:school:update")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody QingSchool school){
        Object error = validate(school);
        if (error != null) {
            return error;
        }
        if(school.getId() == null){
            return ResponseUtil.badArgument();
        }
        if (schoolService.updateById(school) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(school);
    }

    @RequiresPermissions("admin:school:get")
    @GetMapping("/get/{id}")
    public Object getById(@PathVariable("id") Integer id){
        if(id == null){
            return ResponseUtil.badArgument();
        }
        QingSchool school = schoolService.getById(id);
        return ResponseUtil.ok(school);
    }

    private Object validate(QingSchool school) {
        String name = school.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String address = school.getAddress();
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }
}
