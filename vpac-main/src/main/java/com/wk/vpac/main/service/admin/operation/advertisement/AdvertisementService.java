package com.wk.vpac.main.service.admin.operation.advertisement;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.ObjectTool;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wk.vpac.database.dao.operations.AdvertisementDao;
import com.wk.vpac.domain.operations.Advertisement;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * AdvertisementService
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/4/8 0008 11:22
 */
@Service
public class AdvertisementService extends AbstractJpaService<Advertisement, String, AdvertisementDao> {


  @Autowired
  private AttachmentService attachmentService;

  /**
   * 分页查询列表
   *
   * @return
   */
  public DataPage listPage(Map<String, String> params) {
    DataPage res = getDao().list(params);
    return res;
  }

  /**
   * 新增
   *
   */
  @Transactional(rollbackFor = Exception.class)
  public void add(Map<String, String> params) {
    Advertisement advertisement = ConvertUtil.populate(new Advertisement(),params);
    advertisement.setCreateTime(new Date());
    advertisement.setClickCount(0);
    String url = saveImg(advertisement.getImg());
    advertisement.setImg(url);
    getDao().save(advertisement);
  }

  /**
   * 修改
   *
   */
  @Transactional(rollbackFor = Exception.class)
  public void update(Map<String, String> params) {
    Advertisement advertisement = ConvertUtil.populate(new Advertisement(),params);
    Optional<Advertisement> op = getDao().findById(advertisement.getId());
    Assert.isTrue(op.isPresent(), "未查到广告信息");
    Advertisement newAd = op.get();
    newAd = ObjectTool.copy(advertisement, newAd, false);
    String url = saveImg(advertisement.getImg());
    newAd.setImg(url);
    newAd.setBeginTime(advertisement.getBeginTime());
    newAd.setEndTime(advertisement.getEndTime());
    getDao().save(newAd);
  }

  /**
   * 删除
   *
   * @param params
   */
  @Transactional(rollbackFor = Exception.class)
  public void delete(Map<String, String> params) {
    String id = ConvertUtil.checkNotNull(params, "id", "id不能为空", String.class);
    Optional<Advertisement> op = getDao().findById(id);
    Assert.isTrue(op.isPresent(), "未查到广告信息");
    Advertisement advertisement = op.get();
    getDao().delete(advertisement);
  }



  /**
   * 持久化图片
   *
   * @param img
   *
   * @return
   */
  private String saveImg(String img) {
    try {
      ArrayNode license = JsonUtils.reader(img, ArrayNode.class);
      ArrayNode persistent = attachmentService
        .persistent(license, false, TokenThreadLocal.getTokenObjNonNull().objId().toString());
      return persistent == null ? "" : persistent.get(0).get("url").textValue();
    } catch (Exception ignore) {
      return "";
    }
  }

  /**
   * 批量修改链接
   *
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateUrls(Map<String, String> params) {
    String oldUrl = ConvertUtil.checkNotNull(params,"oldUrl","oldUrl不能为空",String.class);
    String newUrl = ConvertUtil.checkNotNull(params,"newUrl","newUrl不能为空",String.class);
    if (oldUrl != null && newUrl != null) {
      getDao().updateUrls(oldUrl.trim(), newUrl.trim());
    }
  }

}
