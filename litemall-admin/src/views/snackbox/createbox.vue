<template>
	<div class="app-container">
		<el-card class="box-card">
			<h3>创建零食盒子</h3>
			<el-form ref="snackbox" :rules="rules" :model="snackbox" label-width="150px">
				<el-form-item label="零食盒名称" prop="name">
					<el-input v-model="snackbox.name" />
				</el-form-item>

				<el-form-item label="图片">
					<el-upload :action="uploadPath" :show-file-list="false" :headers="headers" :on-success="uploadPicUrl" class="avatar-uploader"
					 accept=".jpg,.jpeg,.png,.gif">
						<img v-if="snackbox.picUrl" :src="snackbox.picUrl" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon" />
					</el-upload>
				</el-form-item>

				<el-form-item label="所属学校" prop="school">
					<template>
						<el-select v-model="snackbox.school" clearable placeholder="请选择">
							<el-option v-for="item in schoolList" :key="item.id" :label="item.name" :value="item.name">
							</el-option>
						</el-select>
					</template>
				</el-form-item>

				<el-form-item label="零食盒地址" prop="address">
					<el-input v-model="snackbox.address" />
				</el-form-item>

				<el-form-item v-for="(goodsItem, index) in snackbox.boxgoods" :label="'商品' + (index+1)+'名称'" :key="goodsItem.goodsId"
				 prop="boxgoods">
					<el-select class="snackbox-goods" v-model="snackbox.boxgoods[index].name" filterable remote reserve-keyword
					 placeholder="请输入零食名" :remote-method="findGoodsByName" :loading="loading" @change="goodsSelectChange($event,index)">
						<el-option v-for="item in goodsList" :key="item.id" :label="item.name" :value="item.id+'-'+item.name">
						</el-option>
					</el-select>
					<!-- <i class="red" v-show="validateErrorFlag">*&nbsp;</i> -->
					<span class="goods-count-lable">商品{{index+1}}数量</span>
					<el-input type="number" v-model="snackbox.boxgoods[index].goodsCount" class="goods-count"/>
					<el-button @click.prevent="removeGoodsItem(goodsItem)">删除</el-button>
				</el-form-item>

				<el-form-item>
					<el-button @click="addGoods">新增商品</el-button>
				</el-form-item>
			</el-form>
		</el-card>
		<div class="op-container">
			<el-button @click="handleCancel">取消</el-button>
			<el-button type="primary" @click="handlePublish">保存</el-button>
		</div>
	</div>
</template>

<script>
	import {
		queryAllSchool
	} from '@/api/school'
	import {
		createSnackbox,
		queryGoodsByName
	} from '@/api/snackbox'
	import {
		createStorage,
		uploadPath
	} from '@/api/storage'
	import {
		MessageBox
	} from 'element-ui'
	import {
		getToken
	} from '@/utils/auth'

	export default {
		name: 'addschool',
		data() {
			return {
				uploadPath,
				loading: false,
				schoolList: [{
					id: '',
					name: ''
				}],
				goodsList: [],
				goodsName: '',
				snackbox: {
					id: '',
					name: '',
					picUrl: '',
					school: '',
					address: '',
					boxgoods: [{
						goodsId: '',
						name: '',
						goodsCount: ''
					}]
				},
				rules: {
					name: [{
						required: true,
						message: '零食盒名称不能为空',
						trigger: 'blur'
					}],
					school: [{
						required: true,
						message: '学校名称不能为空',
						trigger: 'blur'
					}],
					address: [{
						required: true,
						message: '零食盒地址不能为空',
						trigger: 'blur'
					}],
					boxgoods: [{
						required: true,
						message: '零食盒中商品不能为空',
						trigger: 'blur'
					}],
					goodsCount: [{
						required: true,
						message: '商品数量不能为空',
						trigger: 'blur'
					}],
				}
			}
		},
		computed: {
			headers() {
				return {
					'X-Litemall-Admin-Token': getToken()
				}
			}
		},
		created() {
			this.queryAllSchool()
		},
		methods: {
			goodsSelectChange($event, index) {
				//获取到选中到的商品的id-name的值
				var goodsIdAndName = $event
				var goodsIdAndNameArray = goodsIdAndName.split('-')

				//获取值
				var goodsId = goodsIdAndNameArray[0]
				var name = goodsIdAndNameArray[1]
				//当前编辑的选择框对应boxgoods中的下标
				var boxgoodsIndex = index
				//修改其中的值
				var goods = this.snackbox.boxgoods[index]
				goods['goodsId'] = goodsId
				goods['name'] = name
			},
			queryAllSchool() {
				queryAllSchool().then(response => {
					this.schoolList = response.data.data.list
				}).catch(response => {
					MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
						confirmButtonText: '确定',
						type: 'error'
					})
				})
			},
			handleCancel: function() {
				this.$router.push({
					path: '/snackbox/boxlist'
				})
			},
			handlePublish: function() {
				createSnackbox(this.snackbox).then(response => {
					this.$notify.success({
						title: '成功',
						message: '创建成功'
					})
					this.$router.push({
						path: '/snackbox/boxlist'
					})
				}).catch(response => {
					MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
						confirmButtonText: '确定',
						type: 'error'
					})
				})
			},
			uploadPicUrl: function(response) {
				this.snackbox.picUrl = response.data.url
			},
			findGoodsByName(name) {
				if (name !== '') {
					this.loading = true
					queryGoodsByName(name).then(response => {
						this.loading = false
						this.goodsList = response.data.data.list
					}).catch(response => {
						MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
							confirmButtonText: '确定',
							type: 'error'
						})
					})
				} else {
					this.goodsList = []
				}
			},

			removeGoodsItem(item) {
				var index = this.snackbox.boxgoods.indexOf(item)
				if (index !== -1) {
					this.snackbox.boxgoods.splice(index, 1)
				}
			},
			addGoods() {
				console.log(this.snackbox.boxgoods)
				var goodsArray = this.snackbox.boxgoods
				goodsArray.push({
					goodsCount: '',
					name: '',
					goodsId: ''
				})
			}
		}
	}
</script>

<style scoped="scoped">
	.el-card {
		margin-bottom: 10px;
	}

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.input-new-keyword {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}

	.avatar-uploader .el-upload {
		width: 145px;
		height: 145px;
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #20a0ff;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 120px;
		height: 120px;
		line-height: 120px;
		text-align: center;
	}

	.avatar {
		width: 145px;
		height: 145px;
		display: block;
	}

	.snackbox-goods {
		/* width: 100%; */
	}

	.goods-count-lable {
		margin-left: 15px;
	}

	.goods-count {
		width: 200px;
	}

	.red {
		color: red;
		font-style: normal;
	}
</style>
