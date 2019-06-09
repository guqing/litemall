var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({
  data: {
    schoolList: ['广东省', '广州市', '海珠区'],
    applyFormObject: {
      name: '',
      school: '选择学校',
      phone: '',
      address: '',
    },
    validateMessage: '',
    // 验证码相关属性
    time: "获取验证码",
    currentTime: 61,
    disabled: false,
    suffix: '',
    data_phone: '',
    data_code: '',
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
    // this.getAddressList();
  },
  bindInput(e){
    // 表单双向数据绑定
    var that = this;
    var dataset = e.currentTarget.dataset;
    // data-开头的是自定义属性，可以通过dataset获取到，dataset是一个json对象
    var value = e.detail.value;
    var name = dataset.name;
    // 拼接对象属性名，用于为对象属性赋值
    var attributeName = 'applyFormObject.' + name
    that.data[name] = value;
    that.setData({
      [attributeName]: that.data[name]
    });
    // console.log(that.data[name])
  },
  bindPickerChange(e) {
    var that = this
    var schoolList = that.data.schoolList
    console.log('picker发送选择改变，携带值为', schoolList[e.detail.value])
    this.setData({
      'applyFormObject.school': schoolList[e.detail.value]
    })
  },
  formSubmit(e) {
    var that = this;
    var validateFlag = that.validateForm();
    console.log(validateFlag)
    if (validateFlag === true){
      wx.showToast({
        title: '提交成功',
        icon: 'none',
        duration: 2000
      })
    }else{
      wx.showToast({
        title: that.data.validateMessage,
        icon: 'none',
        duration: 2000
      })
      // console.log('form发生了submit事件，携带数据为：', that.data.applyFormObject)
    }
    
  },
  validateForm(){
    var that = this;
    var formObj = that.data.applyFormObject;
    if (formObj.name === ''){
     that.setData({
       validateMessage: '请输入姓名'
     })
      return false;
    }
    if (formObj.smscode !== that.data.smscode) {
      that.setData({
        validateMessage: '验证码不正确'
      })
      return false;
    }
    if (formObj.school === '' || formObj.school === '选择学校') {
      that.setData({
        validateMessage: '请先择学校'
      })
      return false;
    }
    if (formObj.address === ''){
      that.setData({
        validateMessage: '请输入详细地址'
      })
      return false;
    }
    return true;
  },
  validatePhone(){
    var that = this;
    var formObj = that.data.applyFormObject;
    if (formObj.phone === '') {
      that.setData({
        validateMessage: '请输入电话号码'
      })
      return false;
    }
    if (formObj.phone.length < 11) {
      that.setData({
        validateMessage: '请输入正确的电话号码'
      })
      return false;
    } else {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!myreg.test(formObj.phone)) {
        that.setData({
          validateMessage: '请输入正确的电话号码'
        })
        return false;
      }
    }
    return true;
  },
  // 获取验证码
  getVerificationCode() {
    var that = this;
    if (!that.data.disabled) {
      that.getCode();
    }
  },
  getCode() {
    let that = this;
    if (that.validatePhone() === true) {
      //// 调用后端API，获取手机验证码
      util.request(api.AuthRegisterCaptcha, {
        mobile: that.data.applyFormObject.phone
      },'POST').then(function (res) {
        if (res.errno === 0) {
          that.setData({
            disabled: true
          })
          wx.showToast({
            title: '已发送',
            icon: 'none',
            duration: 2000
          })
        }else{
          wx.showToast({
            title: res.errmsg,
            icon: 'none',
            duration: 2000
          })
        }
        wx.hideLoading();
      });

      // 设置发送验证码按钮样式
      let interval = null;
      let currentTime = that.data.currentTime;

      interval = setInterval(function () {
        currentTime--;
        that.setData({
          time: currentTime,
          suffix: '秒后可重新获取'
        })
        if (currentTime <= 0) {
          clearInterval(interval)
          that.setData({
            time: '重新发送',
            suffix: '',
            currentTime: 61,
            disabled: false
          })
        }
      }, 1000)
    } else {
      wx.showToast({
        title: '请输入正确的电话号码',
        icon: 'none',
        duration: 2000
      })
    }
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  }
})