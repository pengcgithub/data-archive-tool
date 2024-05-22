package com.smartlink.archive.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smartlink.archive.api.dto.ArchiveConfigDTO;
import com.smartlink.archive.api.request.ArchiveConfigListRequest;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import com.smartlink.archive.infrastructure.db.entity.ArchiveTasksEntity;
import com.smartlink.archive.infrastructure.db.service.ArchiveConfigService;
import com.smartlink.archive.infrastructure.db.service.ArchiveTasksService;
import com.smartlink.archive.infrastructure.utils.JsonResult;
import com.smartlink.archive.task.execute.ExecuteArchiveTasksService;
import com.smartlink.archive.task.generate.GenerateArchiveTasksService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 归档controller<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
@RestController
@RequestMapping("/api/archive")
public class ArchiveController {

    @Autowired
    private GenerateArchiveTasksService generateArchiveTasksService;
    @Autowired
    private ExecuteArchiveTasksService executeArchiveTasksService;
    @Autowired
    private ArchiveConfigService archiveConfigService;
    @Autowired
    private ArchiveTasksService archiveTasksService;

    /**
     * 保存归档配置
     * @param request {@link ArchiveConfigDTO}
     * @return 配置编号
     */
    @PostMapping("/config/save")
    public JsonResult<Integer> save(@RequestBody ArchiveConfigDTO request) {
        ArchiveConfigEntity entity = BeanUtil.toBean(request, ArchiveConfigEntity.class);
        archiveConfigService.saveOrUpdate(entity);
        return JsonResult.buildSuccess(entity.getId());
    }

    /**
     * 查询归档配置列表
     * @param request {@link ArchiveConfigListRequest}
     * @return {@link ArchiveConfigEntity}
     */
    @PostMapping("/config/list")
    public JsonResult<List<ArchiveConfigEntity>> list(@RequestBody ArchiveConfigListRequest request) {
        LambdaQueryWrapper<ArchiveConfigEntity> queryWrapper = Wrappers.lambdaQuery(ArchiveConfigEntity.class)
                .eq(StringUtils.isNotEmpty(request.getArchiveType()), ArchiveConfigEntity::getArchiveType, request.getArchiveType())
                .eq(ObjectUtil.isNotNull(request.getIsEnable()), ArchiveConfigEntity::getIsEnable, request.getIsEnable());
        List<ArchiveConfigEntity> list = archiveConfigService.list(queryWrapper);
        return JsonResult.buildSuccess(list);
    }

    /**
     * 归档配置详情
     * @param id 配置编号
     * @return {@link ArchiveConfigDTO}
     */
    @GetMapping("/config/get")
    public JsonResult<ArchiveConfigDTO> get(@RequestParam Integer id) {
        ArchiveConfigEntity entity = archiveConfigService.getById(id);
        ArchiveConfigDTO dto = BeanUtil.toBean(entity, ArchiveConfigDTO.class);
        return JsonResult.buildSuccess(dto);
    }

    /**
     * 生成执行任务
     * @return true
     */
    @GetMapping("/tasks/generate")
    public JsonResult<List<Integer>> generateArchiveTasks() {
        List<Integer> archiveTaskIds = generateArchiveTasksService.generateArchiveTasks();
        return JsonResult.buildSuccess(archiveTaskIds);
    }

    /**
     * 手动生成执行任务
     * @param ids 归档配置编号，多个用逗号隔开
     * @return true
     */
    @GetMapping("/tasks/manually/generate")
    public JsonResult<List<Integer>> generateArchiveTasks(@RequestParam("ids") String ids) {
        List<Integer> idList = Stream.of(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> archiveTaskIds = generateArchiveTasksService.generateArchiveTasks(idList);
        return JsonResult.buildSuccess(archiveTaskIds);
    }

    /**
     * 执行任务
     * @return true
     */
    @GetMapping("/tasks/execute")
    public JsonResult<Boolean> executeArchiveTasks() {
        executeArchiveTasksService.executeArchiveTasks();
        return JsonResult.buildSuccess();
    }

    /**
     * 查询执行归档任务的状态
     * @param taskId 任务编号
     * @return {@link ArchiveTasksEntity}
     */
    @GetMapping("/tasks/execute/status")
    public JsonResult<ArchiveTasksEntity> queryExecuteArchiveTasks(@RequestParam Integer taskId) {
        ArchiveTasksEntity entity = archiveTasksService.getById(taskId);
        return JsonResult.buildSuccess(entity);
    }

}
