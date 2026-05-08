-- ============================================================
-- Memo App 数据库初始化脚本
-- 数据库: H2
-- ============================================================

-- ----------------------------
-- 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255)    NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    nickname    VARCHAR(50)     DEFAULT NULL,
    avatar      VARCHAR(500)    DEFAULT NULL,
    is_admin    TINYINT         DEFAULT 0,
    status      TINYINT         DEFAULT 1,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- 邮箱唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS idx_email ON sys_user(email);

-- ----------------------------
-- 备忘录分组表
-- ----------------------------
CREATE TABLE IF NOT EXISTS memo_group (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT          NOT NULL,
    name        VARCHAR(100)    NOT NULL,
    is_default  TINYINT         DEFAULT 0,
    sort_order  INT             DEFAULT 0,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- 备忘录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS memo (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT          NOT NULL,
    group_id    BIGINT          DEFAULT NULL,
    title       VARCHAR(255)    NOT NULL,
    content     CLOB            DEFAULT NULL,
    is_deleted  TINYINT         DEFAULT 0,
    deleted_at  TIMESTAMP       DEFAULT NULL,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- 用户+删除状态联合索引 (用于查询某用户的未删除备忘录)
CREATE INDEX IF NOT EXISTS idx_user_deleted ON memo(user_id, is_deleted);

-- 分组+删除状态联合索引 (用于查询某分组下的未删除备忘录)
CREATE INDEX IF NOT EXISTS idx_group_deleted ON memo(group_id, is_deleted);

-- 创建时间索引 (用于按时间排序)
CREATE INDEX IF NOT EXISTS idx_created_at ON memo(created_at);
