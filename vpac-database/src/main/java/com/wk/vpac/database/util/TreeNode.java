package com.wk.vpac.database.util;

import com.base.components.common.doc.annotation.Param;
import com.base.components.common.doc.type.ArrayType;
import com.base.components.common.doc.type.ObjectType;
import com.base.components.common.dto.tree.NestedTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TreeNode 树形结构节点
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2021-08-19 8:54
 */
public class TreeNode<T extends NestedTree> {

  @Param("当前节点ID")
  private String id;

  @Param("父节点ID")
  private String parentId;

  @Param("树的类型")
  private String treeKind;

  /** 当前节点数据 */
  private T data;

  /** 子节点集合 */
  @Param(value = "子节点集合", dataType = ArrayType.class, genericType = ObjectType.class)
  private List<TreeNode<T>> children = Collections.emptyList();

  public TreeNode(T data) {
    if(data == null){
      throw new IllegalArgumentException("tree data can not null !");
    }
    this.data = data;
    this.id = data.getId();
    this.parentId = data.getParentId();
    this.treeKind = data.getTreeKind();
  }

  public String getId() {
    return id;
  }

  public String getParentId() {
    return parentId;
  }

  public String getTreeKind() {
    return treeKind;
  }


  public T getData() {
    return data;
  }

  public List<TreeNode<T>> getChildren() {
    return children;
  }

  public boolean addChild(TreeNode<T> child) {
    if(this.children == null || Objects.equals(this.children, Collections.emptyList())){
      this.children = new ArrayList<>();
    }
    return this.children.add(child);
  }
}
