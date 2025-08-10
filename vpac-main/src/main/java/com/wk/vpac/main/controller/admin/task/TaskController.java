package com.wk.vpac.main.controller.admin.task;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.exception.other.ForbiddenException;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.service.task.SysTask;
import com.base.components.common.service.task.SysTaskClient;
import com.base.components.common.service.task.SysTaskLog;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.util.AuthUtilHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 调度作业
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-12-14 11:46
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN + "/task")
public class TaskController {
  @Autowired
  private SysTaskClient sysTaskClient;

  /**
   * 管理页
   */
  @GetMapping(value = {"/", "/index"})
  public String index(ModelMap model) {
    model.put("taskServers", sysTaskClient.findServerUrls());
    return "task/index";
  }

  /**
   * 查单个
   */
  @GetMapping("/find/{id}")
  @ResponseBody
  public ResponseEntity find(ModelMap model, @PathVariable("id") String id) {
    model.put("task", sysTaskClient.getTask(id));
    return new ResponseEntity<>(sysTaskClient.getTask(id), HttpStatus.OK);
  }

  /**
   * 修改
   */
  @PostMapping("/save")
  @ResponseBody
  public ResponseEntity save(@RequestParam Map<String, String> params) {
    checkAuth();
    if(StringUtils.isBlank(params.get("id"))){
      params.remove("id");
      sysTaskClient.addTask(params);
    }else{
      sysTaskClient.updateTask(params);
    }
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.CREATED);
  }

  /**
   * 启用
   */
  @PostMapping("/enable/{id}")
  @ResponseBody
  public ResponseEntity enable(@PathVariable("id") String id, @RequestParam Map<String, String> params) {
    checkAuth();
    sysTaskClient.startTask(id, true);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.CREATED);
  }

  /**
   * 禁用
   */
  @PostMapping("/disable/{id}")
  @ResponseBody
  public ResponseEntity disable(@PathVariable("id") String id, @RequestParam Map<String, String> params) {
    checkAuth();
    sysTaskClient.pauseTask(id, true);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.CREATED);
  }

  /**
   * 立即触发
   *
   * @param id 作业ID
   * @param params -
   * <p>  argumentsMap       - Nullable - Json - 触发参数
   */
  @PostMapping("/trigger/{id}")
  @ResponseBody
  public ResponseEntity trigger(@PathVariable("id") String id, @RequestParam Map<String, String> params) {
    checkAuth();
    return new ResponseEntity<>(JsonResult.success(sysTaskClient.trigger(id, false, params.get("argumentsMap"))), HttpStatus.CREATED);
  }

  /**
   * 重构同步服务
   */
  @PostMapping("/rebuild")
  @ResponseBody
  public ResponseEntity rebuild(@RequestParam Map<String, String> params) {
    checkAuth();
    Boolean result = sysTaskClient.rebuildServer(true);
    return new ResponseEntity<>(result ? JsonResult.success() : JsonResult.fail("重构同步服务失败"), HttpStatus.CREATED);
  }

  /**
   * 分页查询集合
   *
   * @param params
   *
   * @return
   */
  @GetMapping(value = {"/list"})
  @ResponseBody
  public ResponseEntity<DataPage> list(@RequestParam Map<String, String> params) {
    DataPage<SysTask> page = sysTaskClient.getTaskPage(params);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * 分页日志查询集合
   *
   * @param params
   *
   * @return
   */
  @GetMapping(value = {"/logs"})
  @ResponseBody
  public ResponseEntity<DataPage> logs(@RequestParam Map<String, String> params) {
    DataPage<SysTaskLog> page = sysTaskClient.getTaskLogPage(params);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   * 删除日志
   *
   * @param params
   *
   * @return
   */
  @PostMapping(value = {"/logs/delete"})
  @ResponseBody
  public ResponseEntity logsDelete(@RequestParam Map<String, String> params) {
    checkAuth();
    return new ResponseEntity<>(JsonResult.success(sysTaskClient.deleteSysTaskLog(params)), HttpStatus.CREATED);
  }

  private void checkAuth(){
    //TODO: 建议增加超级管理员权限
    if (!AuthUtilHelper.getAuth().checkSuperRole()) {
      throw new ForbiddenException("无权限操作");
    }
  }
}
