<template>
  <div class="role-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增角色
          </el-button>
        </div>
      </template>

      <el-table :data="roleList" border stripe v-loading="loading">
        <el-table-column prop="roleId" label="ID" width="60" align="center" />
        <el-table-column prop="roleName" label="角色名称" width="100" />
        <el-table-column prop="hp" label="HP" width="60" align="center" />
        <el-table-column prop="attack" label="攻击" width="60" align="center" />
        <el-table-column prop="skillId" label="技能ID" width="80" />
        <el-table-column prop="passiveId" label="被动ID" width="80" />
        <el-table-column prop="skillDescription" label="技能描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="passiveDescription" label="被动描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column prop="portraitId" label="立绘" width="70" />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑角色' : '新增角色'"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="生命值" prop="hp">
          <el-input-number v-model="form.hp" :min="0" />
        </el-form-item>
        <el-form-item label="生命上限" prop="hpMax">
          <el-input-number v-model="form.hpMax" :min="0" />
        </el-form-item>
        <el-form-item label="攻击力" prop="attack">
          <el-input-number v-model="form.attack" :min="0" />
        </el-form-item>
        <el-form-item label="技能ID">
          <el-input v-model="form.skillId" placeholder="请输入技能ID" />
        </el-form-item>
        <el-form-item label="被动ID">
          <el-input v-model="form.passiveId" placeholder="请输入被动技能ID" />
        </el-form-item>
        <el-form-item label="技能描述">
          <el-input
            v-model="form.skillDescription"
            type="textarea"
            placeholder="请输入技能描述"
            rows="3"
          />
        </el-form-item>
        <el-form-item label="被动描述">
          <el-input
            v-model="form.passiveDescription"
            type="textarea"
            placeholder="请输入被动描述"
            rows="3"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            rows="2"
          />
        </el-form-item>
        <el-form-item label="立绘编号">
          <el-input v-model="form.portraitId" placeholder="请输入立绘编号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  getRoleList,
  createRole,
  updateRole,
  deleteRole
} from '../api/role'

const roleList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  roleId: null,
  roleName: '',
  hp: 0,
  hpMax: 0,
  attack: 0,
  skillId: '',
  passiveId: '',
  skillDescription: '',
  passiveDescription: '',
  remark: '',
  portraitId: ''
})

const rules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  hp: [
    { required: true, message: '请输入生命值', trigger: 'blur' }
  ],
  hpMax: [
    { required: true, message: '请输入生命上限', trigger: 'blur' }
  ],
  attack: [
    { required: true, message: '请输入攻击力', trigger: 'blur' }
  ]
}

// 加载角色列表
const loadRoles = async () => {
  loading.value = true
  try {
    const data = await getRoleList()
    roleList.value = data
  } catch (error) {
    console.error(error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    roleId: null,
    roleName: '',
    hp: 0,
    hpMax: 0,
    attack: 0,
    skillId: '',
    passiveId: '',
    skillDescription: '',
    passiveDescription: '',
    remark: '',
    portraitId: ''
  })
}

// 新增
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(form, row)
  isEdit.value = true
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateRole(form.roleId, form)
        ElMessage.success('更新成功')
      } else {
        await createRole(form)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadRoles()
    } catch (error) {
      console.error(error)
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确认删除角色 "${row.roleName}" 吗？此操作不可恢复。`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteRole(row.roleId)
    ElMessage.success('删除成功')
    loadRoles()
  } catch {
    // 用户取消
  }
}

onMounted(() => {
  loadRoles()
})
</script>

<style scoped>
.role-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>