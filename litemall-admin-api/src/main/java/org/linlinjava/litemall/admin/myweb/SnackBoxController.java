package org.linlinjava.litemall.admin.myweb;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.util.QRCodeUtil;
import org.linlinjava.litemall.admin.vo.SnackBoxVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.QingBoxgoods;
import org.linlinjava.litemall.db.domain.QingSnackbox;
import org.linlinjava.litemall.db.myservice.BoxgoodsService;
import org.linlinjava.litemall.db.myservice.SnackBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/snackbox")
@Validated
public class SnackBoxController {
    private static final Logger logger = LoggerFactory.getLogger(SnackBoxController.class);

    @Resource
    private SnackBoxService snackBoxService;
    @Resource
    private BoxgoodsService boxgoodsService;

    @RequiresPermissions("admin:snackbox:create")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "零食盒"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody SnackBoxVo snackBoxVo){
        System.out.println(snackBoxVo.toString());
        Object error = validate(snackBoxVo);
        if (error != null) {
            return error;
        }
        // 拷贝属性,并添加零食盒
        QingSnackbox snackbox = copyProperties(snackBoxVo);
        int snackboxId = snackBoxService.save(snackbox);
        //添加盒中商品
        boxgoodsService.save(snackBoxVo.getBoxgoods(),snackboxId);
        return ResponseUtil.ok(snackBoxVo);
    }

    @RequiresPermissions("admin:snackbox:list")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="查找")
    @GetMapping("/list")
    public Object list(String school, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit){
        List<QingSnackbox> schoolList = snackBoxService.querySelective(school, name, page, limit);
        return ResponseUtil.okList(schoolList);
    }


    @RequiresPermissions("admin:snackbox:delete")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "学校管理"}, button="删除")
    @PostMapping("/delete/{id}")
    public Object deleteById(@PathVariable("id") Integer id){
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        snackBoxService.delete(id);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:snackbox:update")
    @RequiresPermissionsDesc(menu={"零食盒管理" , "零食盒"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody SnackBoxVo snackBoxVo){
        if(snackBoxVo.getId() == null){
            return ResponseUtil.badArgument();
        }

        Object error = validate(snackBoxVo);
        if (error != null) {
            return error;
        }
        QingSnackbox snackbox = copyProperties(snackBoxVo);
        if (snackBoxService.updateById(snackbox, snackBoxVo.getBoxgoods()) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(snackbox);
    }

    @RequiresPermissions("admin:snackbox:queryGoodsByName")
    @GetMapping("/queryGoodsByName")
    public Object queryGoodsByName(@RequestParam("name") String name){
        List<LitemallGoods> goodsList = snackBoxService.queryGoodsByName(name);
        return ResponseUtil.okList(goodsList);
    }

    @RequiresPermissions("admin:snackbox:get")
    @GetMapping("/get/{id}")
    public Object getById(@PathVariable("id") Integer id){
        if(id == null){
            return ResponseUtil.badArgument();
        }
        //查询零食盒信息
        QingSnackbox snackbox = snackBoxService.getBoxById(id);
        //查询盒中商品信息
        List<QingBoxgoods> boxgoodsList = boxgoodsService.getGoodsById(id);

        SnackBoxVo snackBoxVo = new SnackBoxVo();
        //赋值
        BeanUtils.copyProperties(snackbox, snackBoxVo);
        snackBoxVo.setBoxgoods(boxgoodsList);

        return ResponseUtil.ok(snackBoxVo);
    }

    @RequiresPermissions("admin:snackbox:downQRCode")
    @GetMapping("/downQRCode")
    public void downSnackBoxQRCode(HttpServletResponse response, @RequestParam("id") Integer id) {
        //查询零食盒信息
        QingSnackbox snackbox = snackBoxService.getBoxById(id);
        //查询盒中商品信息
        List<QingBoxgoods> boxgoodsList = boxgoodsService.getGoodsById(id);
        List<Integer> goodsIdList = new ArrayList<>();
        for(QingBoxgoods boxgoods : boxgoodsList){
            goodsIdList.add(boxgoods.getGoodsId());
        }

//        JSONArray goodsIdJson = JSONArray.fromObject(goodsIdList);
        JSONObject snackObject = new JSONObject();
        snackObject.put("g", goodsIdList);
        snackObject.put("s", id);
        try {
            response.setContentType("image/png");
            QRCodeUtil.encode(snackObject.toString(), response.getOutputStream());
        }catch (Exception e){
            logger.error("下载二维码出错");
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object validate(SnackBoxVo snackboxVo) {
        String address = snackboxVo.getAddress();
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.badArgument();
        }

        String school = snackboxVo.getSchool();
        if (StringUtils.isEmpty(school)) {
            return ResponseUtil.badArgument();
        }

        String name = snackboxVo.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        List<QingBoxgoods> boxgoodsList = snackboxVo.getBoxgoods();
        for(QingBoxgoods boxgoods : boxgoodsList){
            Integer goodsCount = boxgoods.getGoodsCount();
            if (goodsCount == null) {
                return ResponseUtil.badArgument();
            }
            Integer goodsId = boxgoods.getGoodsId();
            if (goodsId == null) {
                return ResponseUtil.badArgument();
            }
        }
        return null;
    }
    private QingSnackbox copyProperties(SnackBoxVo snackBoxVo){
        QingSnackbox snackbox = new QingSnackbox();
        BeanUtils.copyProperties(snackBoxVo,snackbox);
        return snackbox;
    }
}
