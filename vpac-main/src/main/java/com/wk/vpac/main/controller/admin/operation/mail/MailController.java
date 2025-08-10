package com.wk.vpac.main.controller.admin.operation.mail;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.database.jpa.util.JpaHelper;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.domain.sys.Email;
import com.wk.vpac.domain.sys.Mailbox;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import com.wk.vpac.main.service.admin.operation.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * MailController
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-04-02 15:59
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class MailController {
  @Autowired
  private MailService mailService;
  @Autowired
  private AttachmentService attachmentService;

  @GetMapping("/operation/mail/index")
  public String rechargeIndex(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
    return "operation/mail/index";
  }

  /**
   * 邮件分页查询
   * @param params
   * <p>  startDate                      - Notnull   - Str - desc
   * <p>  endDate                      - Notnull   - Str - desc
   * <p>  keywords                      - Notnull   - Str - desc
   * <p>  status                      - Notnull   - Str - desc
   * @return
   */
  @GetMapping("/operation/mail/pages")
  public ResponseEntity queryMails(@RequestParam Map<String,String> params) {
    DataPage page = mailService.queryMails(params);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  /**
   *
   * @param params
   * @return
   */
  @PostMapping("/operation/mail")
  @ResponseBody
  public ResponseEntity saveMail(@RequestParam Map<String,String> params) {
    //TokenThreadLocal.getTokenObjNonNull().objId().toString();
    String userId = "";
    mailService.saveMail(userId,params);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }

  /**
   * 获取邮件
   * @param id
   * @return
   */
  @GetMapping("/operation/mail/{id}")
  public ResponseEntity getMail(@PathVariable("id") String id) {
    Email email = JpaHelper.detach(mailService.findById(id));
    Assert.notNull(email,"未获取到数据");
    email.setContent(attachmentService.displayUeditorImg(email.getContent()));
    return new ResponseEntity<>(JsonResult.success(email), HttpStatus.OK);
  }

  @PostMapping("/operation/mail/server")
  @ResponseBody
  public ResponseEntity saveMailServer(@RequestParam Map<String,String> params) {
    mailService.saveMailServer(params);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }

  @GetMapping("/operation/mail/server")
  @ResponseBody
  public ResponseEntity getMailServer() {
    Mailbox email = mailService.getMailServer();
    return new ResponseEntity<>(JsonResult.success(email), HttpStatus.OK);
  }
  @PostMapping("/operation/mail/send")
  @ResponseBody
  public ResponseEntity sendMail(@RequestParam("id") String id) throws Exception {
    mailService.sendMail(id);
    return new ResponseEntity<>(JsonResult.success(), HttpStatus.OK);
  }
}
