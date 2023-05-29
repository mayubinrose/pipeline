package com.ctg.pipeline.gate.controller;


import com.ctg.pipeline.common.model.pipeline.config.StageTemplate;
import com.ctg.pipeline.config.service.ITemplateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    private ITemplateService templateService;

    @ApiOperation(value = "save a template of stage")
    @PostMapping("/saveTemplate")
    Map saveTemplate(@RequestBody StageTemplate stageTemplate) {
        return templateService.saveStageTemplate(stageTemplate);
    }

    @ApiOperation(value = "save a template of stage")
    @GetMapping("/getTemplateList")
    PageInfo<StageTemplate> pageQuery(@ApiParam("分组名称") @RequestParam(value = "groupName") String groupName,
                                      @ApiParam("模板名称，模糊查询") @RequestParam(value = "name", required = false) String name,
                                      @ApiParam("排序类型, ASC-正序 DESC-倒序") @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return templateService.pageQuery(groupName,name,sort,pageNum,pageSize);
    }

    @ApiOperation(value = "delete a template of stage")
    @PostMapping("/deleteTemplate")
    Map deleteTemplate(@RequestParam(value = "id") Long id) {
        return templateService.deleteTemplate(id);
    }

    @ApiOperation(value = "get a template of stage")
    @GetMapping("/{templateId}")
    Map getTemplate(@PathVariable(value = "templateId") Long templateId) {
        return templateService.getTemplateById(templateId);
    }

}

