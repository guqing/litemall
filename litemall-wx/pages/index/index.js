const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../utils/user.js');

//获取应用实例
const app = getApp();

Page({
  data: {
  },
  
  onShareAppMessage: function() {
    return {
      title: 'litemall小程序商场',
      desc: '开源微信小程序商城',
      path: '/pages/index/index'
    }
  },

  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.getIndexData();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },
  scancode: function(){
    wx.scanCode({
      onlyFromCamera: false,
      scanType: ['barCode', 'qrCode', 'datamatrix', 'pdf417'],
      success: res => {
        if (res.errMsg == 'scanCode:ok') {
          var resultObject = JSON.parse(res.result);
          var goodsid = JSON.stringify(resultObject.g);
          var sid = resultObject.s;
          wx.navigateTo({
            url: '../../pages/goodslist/goodslist?goodsid=' + goodsid + '&snackboxid=' + sid
          })
        }
      },
      fail: res => {
        // 接口调用失败
        wx.showToast({
          icon: 'none',
          title: '接口调用失败！'
        })
      },
      // complete: res => {
      //   // 接口调用结束
      //   // console.log(res)
      // }
    });
  },
  onLoad: function(options) {

    // 页面初始化 options为页面跳转所带来的参数
    if (options.scene) {
     
    }

  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  },
  getCoupon(e) {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  },
})