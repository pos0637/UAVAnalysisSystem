package org.jeecg.modules.uav.service.impl;

import org.jeecg.modules.uav.entity.UavTaskItem;
import org.jeecg.modules.uav.mapper.UavTaskItemMapper;
import org.jeecg.modules.uav.service.IUavTaskItemService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 测试任务项
 * @Author: jeecg-boot
 * @Date: 2020-12-22
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UavTaskItemServiceImpl extends ServiceImpl<UavTaskItemMapper, UavTaskItem> implements IUavTaskItemService {

    @Override
    public boolean save(UavTaskItem entity) {
        baseMapper.deleteByTypeAndId(entity.getType(), entity.getTaskId());
        return baseMapper.insert(entity) > 0;
    }
}
