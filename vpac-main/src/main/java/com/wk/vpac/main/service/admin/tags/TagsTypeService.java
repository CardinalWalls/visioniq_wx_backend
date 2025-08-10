package com.wk.vpac.main.service.admin.tags;

import com.base.components.common.dto.page.DataPage;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wk.vpac.database.dao.tags.TagsTypeDao;
import com.wk.vpac.domain.tags.TagsType;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * TagsType Service
 * @author : code generator
 * @version : 1.0
 * @since : 2018-06-09
 */
@Service
public class TagsTypeService extends AbstractJpaService<TagsType, String, TagsTypeDao> {
  @Autowired
  private AttachmentService attachmentService;

  public DataPage page(Map<String, String> params){
    return getDao().page(params);
  }

  @Transactional(rollbackFor = Exception.class)
  public TagsType save(Map<String, String> params){
    String id = ConvertUtil.convert(params.get("id"), "");
    TagsType tagsType;
    if(StringUtils.isBlank(id)){
      tagsType = ConvertUtil.populate(new TagsType(), params);
    }else{
      tagsType = ConvertUtil.populate(findById(id), params);
    }
    // 持久化图片
    String imgJson = tagsType.getImgJson();
    if(StringUtils.isNotEmpty(imgJson)){
      try {
        ArrayNode imgArr = JsonUtils.reader(imgJson, ArrayNode.class);
        ArrayNode persistent = attachmentService
          .persistent(imgArr, false, TokenThreadLocal.getTokenObjNonNull().objId().toString());
        tagsType.setImgJson(persistent==null?"[]":persistent.toString());
      }catch (Exception e){
        e.printStackTrace();
      }
    }else{
      tagsType.setImgJson("[]");
    }
    return saveAndFlush(tagsType);
  }

  public DataPage simplePage(Map<String, String> params){
    return getDao().simplePage(params);
  }

}