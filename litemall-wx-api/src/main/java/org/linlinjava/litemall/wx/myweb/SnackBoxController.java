package org.linlinjava.litemall.wx.myweb;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.QingSnackbox;
import org.linlinjava.litemall.db.myservice.SnackBoxService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户零食盒服务
 */
@RestController
@RequestMapping("/wx/snackbox")
@Validated
public class SnackBoxController {
    @Resource
    private SnackBoxService snackBoxService;

    @GetMapping("/getboxinfo")
    public Object getSnackBoxInfo(Integer id){
        QingSnackbox snackbox = snackBoxService.getBoxById(id);
        snackbox.setModifyTime(null);
        snackbox.setCreateTime(null);
        return ResponseUtil.ok(snackbox);
    }
}
