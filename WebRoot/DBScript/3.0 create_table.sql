--
-- 表的结构 `t_tgks_mar_passwordcard`
--
DROP TABLE IF EXISTS `t_tgks_mar_passwordcard`;
CREATE TABLE IF NOT EXISTS `t_tgks_mar_passwordcard` (
  `id` varchar(30) NOT NULL COMMENT '表唯一主键',
  `password` varchar(32) NOT NULL COMMENT '卡密号码',
  `status` varchar(3) NOT NULL COMMENT '状态（0 未使用；1 使用中；2 已使用）',
  `inviteCode` varchar(9) default NULL COMMENT '招待ID',
  `usedtime` timestamp NULL default NULL COMMENT '使用时间',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='招待用卡密表';



--
-- 表的结构 `t_tgks_mar_krsmacard`
--
DROP TABLE IF EXISTS `t_tgks_mar_krsmacard`;
CREATE TABLE IF NOT EXISTS `t_tgks_mar_krsmacard` (
  `id` varchar(30) NOT NULL COMMENT '表唯一主键',
  `cardId` varchar(30) NOT NULL COMMENT '卡牌ID',
  `name` varchar(100) NOT NULL COMMENT '名字',
  `nickName` varchar(100) default NULL COMMENT '昵称',
  `type` varchar(3) NOT NULL COMMENT '职业（1 佣兵；2 富豪；3 盗贼；4 歌姬）',
  `rarity` varchar(10) NOT NULL COMMENT '稀有度',
  `imageUrl` varchar(500) default NULL COMMENT '卡图URL',
  `iconUrl` varchar(500) default NULL COMMENT '图标URL',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cardId` (`cardId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='乖离性MA卡牌信息表';



--
-- 表的结构 `t_tgks_mar_account`
--
DROP TABLE IF EXISTS `t_tgks_mar_account`;
CREATE TABLE IF NOT EXISTS `t_tgks_mar_account` (
  `id` varchar(30) NOT NULL COMMENT '表唯一主键',
  `uuid` varchar(50) NOT NULL COMMENT 'UUID（登录用）',
  `accountKey` varchar(200) NOT NULL COMMENT '账号加密key',
  `status` varchar(3) NOT NULL COMMENT '状态（0 新建；1 执行中；2 已完成；3 已售出）',
  `inviteCode` varchar(9) default NULL COMMENT '招待ID',
  `title` varchar(500) default NULL COMMENT '标题',
  `urNumA` int(3) NOT NULL default '0' COMMENT 'UR数量 佣兵',
  `urNumB` int(3) NOT NULL default '0' COMMENT 'UR数量 富豪',
  `urNumC` int(3) NOT NULL default '0' COMMENT 'UR数量 盗贼',
  `urNumD` int(3) NOT NULL default '0' COMMENT 'UR数量 歌姬',
  `cardIds` varchar(500) default NULL COMMENT '卡片ID（只包含UR SR）',
  `crystal` int(10) NOT NULL default '0' COMMENT '水晶数量',
  `price` int(10) NOT NULL default '0' COMMENT '价格',
  `createtime` timestamp NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(500) default NULL COMMENT '备注',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='乖离性MA账号信息表';