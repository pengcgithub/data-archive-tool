package com.smartlink.archive.infrastructure.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartlink.archive.infrastructure.db.entity.ArchiveTasksEntity;
import com.smartlink.archive.infrastructure.db.service.ArchiveTasksService;
import com.smartlink.archive.infrastructure.db.mapper.ArchiveTasksMapper;
import org.springframework.stereotype.Service;

/**
 * 针对表【archive_tasks(归档任务表)】的数据库操作Service实现<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2024/4/30 3:36 PM
 * @since v1.0.0
 */
@Service
public class ArchiveTasksServiceImpl extends ServiceImpl<ArchiveTasksMapper, ArchiveTasksEntity>
    implements ArchiveTasksService {

}




