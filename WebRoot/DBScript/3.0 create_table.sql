--
-- 表的结构 `t_tgks_mar_passwordcard`
--
DROP TABLE IF EXISTS `t_tgks_mar_passwordcard`;
CREATE TABLE IF NOT EXISTS `t_tgks_mar_passwordcard` (
  `id` varchar(30) NOT NULL COMMENT '表唯一主键',
  `password` varchar(32) NOT NULL COMMENT '卡密号码',
  `status` varchar(3) NOT NULL COMMENT '状态（0 未使用；1 已使用）',
  `inviteCode` varchar(9) default NULL COMMENT '招待ID',
  `usedtime` timestamp NULL default NULL COMMENT '使用时间',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='招待用卡密表';