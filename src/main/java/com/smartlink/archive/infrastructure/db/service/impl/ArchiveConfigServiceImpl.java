package com.smartlink.archive.infrastructure.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import com.smartlink.archive.infrastructure.db.service.ArchiveConfigService;
import com.smartlink.archive.infrastructure.db.mapper.ArchiveConfigMapper;
import org.springframework.stereotype.Service;

/**
 * 针对表【archive_config(归档配置表)】的数据库操作Service实现<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2024/4/30 3:36 PM
 * @since v1.0.0
 */
@Service
public class ArchiveConfigServiceImpl extends ServiceImpl<ArchiveConfigMapper, ArchiveConfigEntity>
    implements ArchiveConfigService {

}




