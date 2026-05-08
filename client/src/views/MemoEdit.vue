<template>
  <div class="memo-edit-page">
    <header class="edit-header">
      <el-button :icon="ArrowLeft" text @click="handleBack">返回</el-button>
      <div class="header-actions">
        <el-button :loading="saving" type="primary" @click="handleSave">保存</el-button>
      </div>
    </header>

    <div class="edit-content">
      <el-input
        v-model="form.title"
        placeholder="备忘录标题（可选）"
        class="title-input"
        maxlength="255"
        show-word-limit
      />

      <div class="editor-toolbar">
        <el-select v-model="form.groupId" placeholder="选择分组" size="default">
          <el-option
            v-for="group in groupStore.list"
            :key="group.id"
            :label="group.name"
            :value="group.id"
          />
        </el-select>
        <div class="toolbar-hint">支持 Markdown 语法</div>
      </div>

      <div class="editor-container">
        <textarea
          ref="textareaRef"
          v-model="form.content"
          class="editor-textarea"
          placeholder="开始编写你的备忘录..."
          @keydown.tab.prevent="handleTab"
        />
        <div class="editor-preview" v-html="renderedContent" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { marked } from 'marked'
import { useMemoStore } from '@/stores/memo'
import { useGroupStore } from '@/stores/group'

const route = useRoute()
const router = useRouter()
const memoStore = useMemoStore()
const groupStore = useGroupStore()

const isEdit = computed(() => !!route.params.id)
const saving = ref(false)
const textareaRef = ref()

const form = reactive({
  title: '',
  content: '',
  groupId: null
})

const renderedContent = computed(() => {
  if (!form.content) return '<p style="color: #666;">预览区域</p>'
  return marked.parse(form.content)
})

onMounted(async () => {
  if (groupStore.list.length === 0) {
    await groupStore.fetchList()
  }
  form.groupId = groupStore.selectedId || groupStore.defaultGroup?.id

  if (isEdit.value) {
    const memo = await memoStore.fetchDetail(route.params.id)
    form.title = memo.title || ''
    form.content = memo.content || ''
    form.groupId = memo.groupId
  }
})

const handleTab = (e) => {
  const start = e.target.selectionStart
  const end = e.target.selectionEnd
  form.content = form.content.substring(0, start) + '    ' + form.content.substring(end)
  setTimeout(() => {
    e.target.selectionStart = e.target.selectionEnd = start + 4
  }, 0)
}

const handleSave = async () => {
  saving.value = true
  try {
    if (isEdit.value) {
      await memoStore.update(route.params.id, {
        title: form.title,
        content: form.content,
        groupId: form.groupId
      })
      ElMessage.success('备忘录已更新')
    } else {
      await memoStore.create({
        title: form.title,
        content: form.content,
        groupId: form.groupId
      })
      ElMessage.success('备忘录已创建')
    }
    router.push('/')
  } catch (error) {
    // 错误已处理
  } finally {
    saving.value = false
  }
}

const handleBack = () => {
  router.back()
}
</script>

<style lang="scss" scoped>
.memo-edit-page {
  min-height: 100vh;
  background: #0A0A0A;
  display: flex;
  flex-direction: column;
}

.edit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #1A1A1A;
  border-bottom: 1px solid #333;
  position: sticky;
  top: 0;
  z-index: 10;
}

.edit-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.title-input {
  margin-bottom: 16px;

  :deep(.el-input__wrapper) {
    background: transparent !important;
    box-shadow: none !important;
    border-bottom: 1px solid #333;
    border-radius: 0;
  }

  :deep(.el-input__inner) {
    font-size: 24px;
    font-weight: 600;
    color: #fff;

    &::placeholder {
      color: #666;
    }
  }
}

.editor-toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #333;

  .toolbar-hint {
    color: #666;
    font-size: 13px;
  }
}

.editor-container {
  flex: 1;
  display: flex;
  gap: 24px;
  min-height: 0;
}

.editor-textarea {
  flex: 1;
  background: transparent;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 16px;
  font-size: 14px;
  line-height: 1.8;
  color: #fff;
  resize: none;
  outline: none;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;

  &::placeholder {
    color: #666;
  }

  &:focus {
    border-color: #00D4AA;
  }
}

.editor-preview {
  flex: 1;
  background: #1A1A1A;
  border: 1px solid #333;
  border-radius: 8px;
  padding: 16px;
  overflow-y: auto;
  color: #B0B0B0;
  line-height: 1.8;

  :deep(h1), :deep(h2), :deep(h3) {
    color: #fff;
    margin: 16px 0 8px;
  }

  :deep(h1) { font-size: 24px; }
  :deep(h2) { font-size: 20px; }
  :deep(h3) { font-size: 16px; }

  :deep(p) {
    margin: 8px 0;
  }

  :deep(code) {
    background: #333;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: monospace;
  }

  :deep(pre) {
    background: #333;
    padding: 12px;
    border-radius: 8px;
    overflow-x: auto;

    code {
      background: transparent;
      padding: 0;
    }
  }

  :deep(ul), :deep(ol) {
    padding-left: 24px;
    margin: 8px 0;
  }

  :deep(blockquote) {
    border-left: 3px solid #00D4AA;
    padding-left: 16px;
    margin: 16px 0;
    color: #888;
  }
}

// 响应式布局
@media (max-width: 768px) {
  .editor-container {
    flex-direction: column;
  }

  .editor-textarea,
  .editor-preview {
    min-height: 300px;
  }

  .title-input {
    :deep(.el-input__inner) {
      font-size: 18px;
    }
  }
}
</style>
