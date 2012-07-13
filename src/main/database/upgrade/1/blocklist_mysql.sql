# 我们目前不需要修改时间，如果修改，只能是拖出黑名单，直接删除该记录
ALTER TABLE `ofBlocklist` DROP `modificationDate`;

UPDATE ofVersion set version = 1 where name = 'blocklist';