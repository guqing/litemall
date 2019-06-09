<template>
	<div class="app-container">
		<!-- 查询和其他操作 -->
		<div class="filter-container">
			<el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入零食盒名称" />
			<el-input v-model="listQuery.school" clearable class="filter-item" style="width: 200px;" placeholder="请输入学校名称" />
			<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
			<el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
		</div>

		<!-- 查询结果 -->
		<el-table v-loading="listLoading" :data="snackboxList" element-loading-text="正在查询中..." border fit
		 highlight-current-row>

			<el-table-column align="center" type="index" />

			<el-table-column align="center" min-width="100" label="零食盒名称" prop="name" />

			<el-table-column align="center" property="picUrl" label="图片">
				<template slot-scope="scope">
					<img :src="scope.row.picUrl" width="40">
				</template>
			</el-table-column>

			<el-table-column align="center" min-width="100" label="所属学校" prop="school" :filters="schoolList" :filter-method="schoolFilterHandler">
			</el-table-column>

			<el-table-column align="center" label="零食盒地址" prop="address">
			</el-table-column>

			<el-table-column align="center" label="二维码" prop="QRcode">
				<template slot-scope="scope">
					<el-button type="success" plain size="mini" @click="handleQRcodeDownload(scope.row)">下载</el-button>
				</template>
			</el-table-column>

			<el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
					<el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

		<el-tooltip placement="top" content="返回顶部">
			<back-to-top :visibility-height="100" />
		</el-tooltip>

	</div>
</template>

<script>
	import {
		listSnackbox,
		deleteSnackbox,
		downLoadQRCode
	} from '@/api/snackbox'
	import {
		MessageBox
	} from 'element-ui'
	import BackToTop from '@/components/BackToTop'
	import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

	export default {
		name: 'schoolList',
		components: {
			BackToTop,
			Pagination
		},
		data() {
			return {
				total: 0,
				listLoading: false,
				listQuery: {
					page: 1,
					limit: 20,
					name: undefined,
					school: undefined
				},
				schoolList: [],
				snackboxList: [],
				snackbox: {
					id: '',
					name: '',
					picUrl: '',
					school: '',
					address: ''
				}
			}
		},
		created() {
			this.getList()
		},
		methods: {
			getList() {
				this.listLoading = true
				listSnackbox(this.listQuery).then(response => {
					this.snackboxList = response.data.data.list
					this.total = response.data.data.total
					this.listLoading = false
				}).catch(() => {
					this.snackboxList = []
					this.total = 0
					this.listLoading = false
				})
			},
			handleFilter() {
				this.listQuery.page = 1
				this.getList()
			},
			handleCreate() {
				this.$router.push({
					path: '/snackbox/createbox'
				})
			},
			handleUpdate(row) {
				this.$router.push({
					path: '/snackbox/editbox',
					query: {
						id: row.id
					}
				})
			},
			handleDelete(row) {
				this.$confirm('你确定要删除零食盒吗？此操作不可恢复', '警告', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					deleteSnackbox(row.id).then(response => {
						this.$notify.success({
							title: '成功',
							message: '删除成功'
						})
						const index = this.snackboxList.indexOf(row)
						this.snackboxList.splice(index, 1)
					})
				}).catch(() => {})
			},
			schoolFilterHandler(value, row, column) {
				const property = column['property'];
				return row[property] === value;
			},
			handleQRcodeDownload(row) {
				// 下载零食盒二维码
				var snackBoxId = row.id
				downLoadQRCode(snackBoxId).then(response => {
					//下载成功
				}).catch(response => {
					const content = response.data
					//构造一个blob对象来处理数据
					const blob = new Blob([content])
					var filename = "零食盒二维码"+snackBoxId+".png"
					if (window.navigator.msSaveOrOpenBlob) {
						// 兼容IE10
						navigator.msSaveBlob(blob, filename)
					} else {
						//  chrome/firefox
						let aTag = document.createElement('a')
						aTag.download = filename
						aTag.href = URL.createObjectURL(blob)
						aTag.click()
						URL.revokeObjectURL(aTag.href)
					}
				})
			}
		}
	}
</script>

<style scoped="scoped">
	.table-expand {
		font-size: 0;
	}

	.table-expand label {
		width: 100px;
		color: #99a9bf;
	}

	.table-expand .el-form-item {
		margin-right: 0;
		margin-bottom: 0;
	}

	.gallery {
		width: 80px;
		margin-right: 10px;
	}
</style>
