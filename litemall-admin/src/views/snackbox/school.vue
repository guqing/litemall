<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
			<el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入学校名称"/>
      <el-input v-model="listQuery.address" clearable class="filter-item" style="width: 200px;" placeholder="请输入学校地址"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中..." border fit highlight-current-row>

      <el-table-column align="center" type="index">
			</el-table-column>

      <el-table-column align="center" min-width="100" label="学校名称" prop="name"/>

      <el-table-column align="center" property="picUrl" label="图片">
        <template slot-scope="scope">
          <img :src="scope.row.picUrl" width="40">
        </template>
      </el-table-column>

      <el-table-column align="center" label="学校地址" prop="address">
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
import { listSchool, deleteSchool } from '@/api/school'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'schoolList',
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        address: undefined,
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listSchool(this.listQuery).then(response => {
        this.list = response.data.data.list
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$router.push({ path: '/snackbox/addschool' })
    },
    handleUpdate(row) {
      this.$router.push({ path: '/snackbox/editschool', query: { id: row.id }})
    },
    showDetail(detail) {
      this.goodsDetail = detail
      this.detailDialogVisible = true
    },
    handleDelete(row) {
		this.$confirm('你确定要删除学校吗？这将会导致零食盒子失去学校分类', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
			deleteSchool(row.id).then(response => {
			  this.$notify.success({
			    title: '成功',
			    message: '删除成功'
			  })
			  const index = this.list.indexOf(row)
			  this.list.splice(index, 1)
			  })
        }).catch(() => {})
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
