<template>
	<div class="app-container">
		<el-card class="box-card">
			<h3>编辑学校</h3>
			<el-form ref="school" :rules="rules" :model="school" label-width="150px">
				<el-form-item label="学校名称" prop="name">
					<el-input v-model="school.name" />
				</el-form-item>

				<el-form-item label="图片">
					<el-upload :action="uploadPath" :show-file-list="false" :headers="headers" :on-success="uploadPicUrl" class="avatar-uploader"
					 accept=".jpg,.jpeg,.png,.gif">
						<img v-if="school.picUrl" :src="school.picUrl" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon" />
					</el-upload>
				</el-form-item>

				<el-form-item label="学校地址">
					<el-input type="textarea" :rows="4" resize="none" v-model="school.address" />
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
		getSchool,
		updateSchool
	} from '@/api/school'
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
				school: {
					id: null,
					name: '',
					picUrl: '',
					address: ''
				},
				rules: {
					name: [{
						required: true,
						message: '学校名称不能为空',
						trigger: 'blur'
					}]
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
			this.getSchool()
		},
		methods: {
			getSchool() {
				if (this.$route.query.id == null) {
					return
				}
				var schoolId = this.$route.query.id

				getSchool(schoolId).then(response => {
					this.school = response.data.data
				}).catch(response => {
					MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
						confirmButtonText: '确定',
						type: 'error'
					})
				})
			},
			handleCancel: function() {
				this.$router.push({
					path: '/snackbox/list'
				})
			},
			handlePublish: function() {
				this.$refs['school'].validate((valid) => {
					if (valid) {
						updateSchool(this.school).then(response => {
							this.$notify.success({
								title: '成功',
								message: '编辑成功'
							})
							this.$router.push({
								path: '/snackbox/list'
							})
						}).catch(response => {
							MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
								confirmButtonText: '确定',
								type: 'error'
							})
						})
					} else {
						MessageBox.alert('请将参数表单填写完整', '警告', {
							confirmButtonText: '确定',
							type: 'error'
						})
						return false
					}
				})
			},
			uploadPicUrl: function(response) {
				this.school.picUrl = response.data.url
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
</style>
