

package com.wk.vpac.main.service.admin.region;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.tree.TreeNodeData;
import com.base.components.common.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.domain.sys.Region;
import com.wk.vpac.dto.sys.RegionCountDto;

import java.util.List;


/**
 * JiangWen
 */
public class RegionNodeBuilder {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static TreeNodeData<Region> buildNode(Region entity, List<String> selectIds) {
    TreeNodeData node = new TreeNodeData();
    if (entity.getLeaf() != Valid.TRUE.getVal()) {
      node.setFolder(true);
      node.setLazy(true);
      node.setExpanded(false);
      node.setUnselectable(true);
    }else{
      node.setFolder(false);
      node.setLazy(false);
      node.setExpanded(false);
      node.setUnselectable(false);
    }
    node.setIconclass("fa fa-map");
    if(entity.getLeaf() != null && entity.getLeaf() == 1){
      node.setIconclass("fa fa-map-marker");
    }
    node.setExtraClasses("");
    if (entity.getStatus() == Valid.FALSE.getVal()) {
      node.setExtraClasses("ui-state-disabled");
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
    if(entity instanceof RegionCountDto){
      RegionCountDto dto = (RegionCountDto)entity;
      if(dto.getCount() != null && dto.getCount() > 0){
        node.setTitle(entity.getName() + " (" + dto.getCount() + ")");
      }
    }
    if (selectIds != null && selectIds.contains(entity.getId())) {
      node.setSelected(true);
    }


    return node;
  }
}
