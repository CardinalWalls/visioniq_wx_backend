package com.wk.vpac.main.service.admin.operation.mail;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.sys.EmailDao;
import com.wk.vpac.database.dao.sys.MailboxDao;
import com.wk.vpac.domain.sys.Email;
import com.wk.vpac.domain.sys.Mailbox;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MailService
 *
 * @author <a href="lijian_jie@163.com">LiJian</a>
 * @version 1.0.0, 2018-04-02 16:17
 */
@Service
public class MailService extends AbstractJpaService<Email,String, EmailDao> {
  @Autowired
  private MailboxDao mailboxDao;
  @Autowired
  private AttachmentService attachmentService;

  public DataPage queryMails(Map<String, String> params) {
    return getDao().queryEmails(params);
  }

  /**
   * 邮件保存
   * @param userId 用户id
   * @param params
   * <p>  id                      - Notnull   - Str - desc
   * <p>  subject                      - Notnull   - Str - desc
   * <p>  addressee                      - Notnull   - Str - desc
   * <p>  remark                      - Notnull   - Str - desc
   * <p>  editorValue                      - Notnull   - Str - desc
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveMail(String userId, Map<String, String> params) {
    String id = ConvertUtil.convert(params.get("id"), "");
    Email email;
    if(StringUtils.isBlank(id)){
      email = new Email();
      email.setCreateTime(new Date());
      email.setStatus(Valid.FALSE.getVal());
    }else{
      Optional<Email> emailOptional = getDao().findById(id);
      Assert.isTrue(emailOptional.isPresent(),"email is not present");
      email = emailOptional.get();
    }
    String addressee = params.get("addressee");
    String[] split = addressee.split(",");
//    for(String s:split){
//      Assert.isTrue(ValidatorUtil.isEmail(s),"错误的邮箱格式："+s);
//    }
    email.setAddressee(addressee);
    email.setAuthorId(userId);
    String content = ConvertUtil.checkNotNull(params, "editorValue", "请提填写内容",String.class);
    email.setContent(attachmentService.dealUeditorImg(TokenThreadLocal.getTokenObjNonNull().objId().toString(), content));
    email.setSubject(params.get("subject"));
    email.setRemark(params.get("remark"));
    email.setCount(split.length);
    email.setMailboxId("");
    getDao().saveAndFlush(email);
  }
  @Transactional(rollbackFor = Exception.class)
  public void saveMailServer(Map<String, String> params) {
    String id = params.get("id");
    Mailbox mailbox ;
    if(StringUtils.isBlank(id)){
      mailbox = new Mailbox();
    }else{
      Optional<Mailbox> boxOptional = mailboxDao.findById(id);
      Assert.isTrue(boxOptional.isPresent(),"mailbox is not present");
      mailbox = boxOptional.get();
    }
    mailbox.setAddress(params.get("address"));
    mailbox.setCreateTime(new Date());
    mailbox.setPassword(params.get("password"));
    mailbox.setSign(params.get("sign"));
    mailbox.setStatus(Valid.TRUE.getVal());
    mailbox.setPriority(0);
    mailbox.setSmtp(params.get("smtp"));
    mailboxDao.saveAndFlush(mailbox);
  }

  public Mailbox getMailServer() {
    List<Mailbox> all = mailboxDao.findAll();
    return all.size()>0?all.get(0):null;
  }

  @Transactional(rollbackFor = Exception.class)
  public void sendMail(String id) throws Exception {
    throw new IllegalArgumentException("未配置系统邮箱");
//    Email email = getDao().findById(id).orElseThrow(() -> new IllegalArgumentException("未找到Email"));
//    Mailbox mailServer = getMailServer();
//    Assert.notNull(mailServer, "未配置系统邮箱");
//    String addressee = email.getAddressee();
//    mailUtil.sendHtmlMail(mailServer,email.getSubject(), attachmentService.displayUeditorImgWithHost(email.getContent()), addressee);
//    email.setStatus(Valid.TRUE.getVal());
//    email.setSendTime(new Date());
//    email.setMailboxId(mailServer.getId());
//    getDao().saveAndFlush(email);
  }
}
