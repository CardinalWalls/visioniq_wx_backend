package com.wk.vpac.main.service.admin.user;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.database.dao.user.GalleryDao;
import com.wk.vpac.domain.user.Gallery;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * Gallery Service
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class GalleryService extends AbstractJpaService<Gallery, String, GalleryDao> {

  @Autowired
  private AttachmentService attachmentService;

  public DataPage page(Map<String, String> params) {
    return getDao().page(params);
  }

  @Transactional(rollbackFor = Exception.class)
  public Gallery save(Map<String, String> params) {
    String id = ConvertUtil.convert(params.get("id"), "");
    Gallery gallery;
    if (StringUtils.isBlank(id)) {
      gallery = ConvertUtil.populate(new Gallery(), params);
      gallery.setCreateTime(new Date());
    } else {
      gallery = ConvertUtil.populate(findById(id), params);
    }
    Assert.hasText(gallery.getUrl(),"图片不能为空！");
    // 持久化
    try {
      if (StringUtils.isNotEmpty(gallery.getUrl()) && gallery.getUrl().contains("temp/")) {
        ArrayNode arrayNode = JsonUtils.createArrayNode();
        ObjectNode objectNode = JsonUtils.createObjectNode();
        objectNode.put("url", gallery.getUrl());
        arrayNode.add(objectNode);
        ArrayNode persistent = attachmentService.persistent(arrayNode, true, gallery.getUserId());
        gallery.setUrl(persistent != null ? persistent.get(0).get("url").asText() : "");
      }
    } catch (Exception ignore) {
      gallery.setUrl("");
    }
    return saveAndFlush(gallery);
  }

}