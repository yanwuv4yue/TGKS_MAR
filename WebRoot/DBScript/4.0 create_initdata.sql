--
-- 转存表中的数据 `t_tgks_common_user`
--

INSERT INTO `t_tgks_common_user` (`id`, `username`, `password`, `status`, `type`, `email`, `name`, `sex`, `groupid`, `createtime`) VALUES
('UMSX20141206012303000001', 'KRSMA', '123456', '1', '0', 'KRSMA', 'KRSMA', '0', NULL, '2014-12-05 17:23:03');


--
-- 转存表中的数据 `t_tgks_common_menu`
--

INSERT INTO `t_tgks_common_menu` (`id`, `preid`, `name`, `status`, `url`, `level`, `sort`) VALUES
('COMM20141205024116000001', '0', 'MARZ管理系统', '1', '#', '1', '3300'),
('COMM20141206020826000010', 'COMM20141205024116000001', '卡密管理', '1', '../mar/passwordCardManager.action', '2', '3300'),
('COMM20141206020826000011', 'COMM20141205024116000001', '卡牌管理', '1', '../mar/krsmaCardManager.action', '2', '3301'),
('COMM20141206020826000012', 'COMM20141205024116000001', '账号管理', '1', '../mar/accountManager.action', '2', '3302');