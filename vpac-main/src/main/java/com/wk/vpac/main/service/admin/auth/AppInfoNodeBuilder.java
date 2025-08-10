

package com.wk.vpac.main.service.admin.auth;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.tree.TreeNodeData;
import com.base.components.common.util.EnumUtil;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.database.dao.admin.SysAppInfoDao;
import com.wk.vpac.domain.admin.SysAppInfo;
import com.wk.vpac.main.constants.auth.ReadOrWrite;

import java.util.List;

/**
 * @author DLee
 */
public class AppInfoNodeBuilder {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static TreeNodeData<SysAppInfo> buildNode(SysAppInfo entity, List<String> selectIds) {
    TreeNodeData node = new TreeNodeData();
    if (SysAppInfoDao.ROOT_PARENT.equals(entity.getParentId()) || SysAppInfoDao.ROOT.equals(entity.getParentId())) {
      node.setFolder(false);
      if ((entity.getRightVal() - entity.getLeftVal() - 1) / 2 > 0) {
        node.setFolder(true);
        node.setLazy(true);
        node.setExpanded(true);
      }
    } else {
      node.setFolder(false);
      if ((entity.getRightVal() - entity.getLeftVal() - 1) / 2 > 0) {
        node.setFolder(true);
        node.setLazy(true);
      }
    }
    ReadOrWrite rw = EnumUtil.parse(ReadOrWrite.class, "value", entity.getReadWriteType());
    node.setTooltip((rw == null ? "": rw.getName()+" - ") + entity.getDescription());
    node.setIconclass(entity.getIconClass());
    node.setExtraClasses(SysAppInfoDao.ROOT_PARENT.equals(entity.getParentId()) ? "root":"");
    if (0 == entity.getType()) {
      node.setExtraClasses(node.getExtraClasses() + " directory");
    }else if (1 == entity.getType()) {
      node.setExtraClasses(node.getExtraClasses() + " view");
    }else{
      node.setExtraClasses(node.getExtraClasses() + " item");
    }
    if (entity.getStatus() == Valid.FALSE.getVal()) {
      node.setExtraClasses(node.getExtraClasses() + " ui-state-disabled");
    }
    node.setRefKey(entity.getId());
    node.setKey(entity.getId());
    String parentId = null;
    if (entity.getParentId() != null) {
      parentId = entity.getParentId();
    }
    ObjectNode data = JsonUtils.convert(entity, ObjectNode.class);
    if (entity.getParentId() != null) {
      data.put("parentId", parentId);
    }
    node.setData(data);
    node.setTitle(entity.getName());
    if (selectIds != null && selectIds.contains(entity.getId())) {
      node.setSelected(true);
    }
    return node;
  }
}
