

package com.wk.vpac.main.service.admin.member;

import com.base.components.common.constants.Valid;
import com.base.components.common.dto.tree.TreeNodeData;
import com.base.components.common.dto.tree.TreeParam;
import com.base.components.common.util.PasswordUtil;
import com.base.components.database.jpa.dao.base.NestedTreeQuery;
import com.base.components.database.jpa.util.BaseNodeBuilder;
import com.wk.vpac.common.constants.admin.MemberType;
import com.wk.vpac.database.dao.admin.SysMemberDao;
import com.wk.vpac.domain.admin.SysMember;
import com.wk.vpac.dto.sys.SysMemberCountDto;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author DLee
 */
public class MemberNodeBuilder extends BaseNodeBuilder<SysMember> {

  public MemberNodeBuilder(NestedTreeQuery<SysMember> nestedTreeQuery) {
    super(nestedTreeQuery);
  }

  @Override
  protected void custom(TreeNodeData<SysMember> node, SysMember entity, List<String> selectIds, TreeParam treeParam) {
    super.custom(node, entity, selectIds, treeParam);
    node.setIconclass("fa fa-user");
    if (MemberType.DEPT.getType() == entity.getMemberType()) {
      node.setIconclass("fa fa-group");
    }
    if (MemberType.PERSON.getType() == entity.getMemberType()
      && StringUtils.isNotBlank(entity.getPwd()) ) {
      entity.setPwd(PasswordUtil.decryptPassword(entity.getAccount(), entity.getPwd()));
    }
    if (entity.getStatus() == Valid.FALSE.getVal()) {
      node.setExtraClasses("ui-state-disabled");
    }
    if(entity instanceof SysMemberCountDto){
      SysMemberCountDto dto = (SysMemberCountDto)entity;
      if(dto.getCount() != null && dto.getCount() > 0){
        node.setTitle(entity.getMemberName() + " (" + dto.getCount() + ")");
      }
    }
  }

  @Override
  protected String getRootParentId() {
    return SysMemberDao.ROOT_PARENT;
  }

  @Override
  protected String getId(SysMember entity) {
    return entity.getId();
  }

  @Override
  protected String getTitle(SysMember entity) {
    return entity.getMemberName();
  }

}
