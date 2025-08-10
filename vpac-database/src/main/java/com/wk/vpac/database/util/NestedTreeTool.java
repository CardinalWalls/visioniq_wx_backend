package com.wk.vpac.database.util;

import com.base.components.common.dto.tree.NestedTree;
import com.base.components.common.util.NestedSetModel;
import com.base.components.common.util.Relationship;
import org.springframework.util.Assert;

import jakarta.persistence.EntityManager;

/**
 * NestedTreeTool
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2020-01-06 14:49
 */
public class NestedTreeTool {

  /**
   * 添加节点，重新排列整棵树的左右值并更新，
   */
  public static void insert(NestedTree target, NestedTree current, EntityManager entityManager,
                            Relationship relationship) {
    Assert.notNull(target, "目标节点不能为空！");
    Assert.notNull(current, "当前节点不能为空！");
    Assert.isTrue(target.getClass().equals(current.getClass()), "父节点和当前节点类型必须一致！");
    boolean hasTreeKind = target.getTreeKind() != null;

    int[] val = NestedSetModel
      .insert(target.getLeftVal(), target.getRightVal(), relationship);
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.leftVal = m.leftVal + 2 WHERE m.leftVal > :leftVal"
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : ""))
                 .setParameter("leftVal", val[0]).executeUpdate();
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.rightVal = m.rightVal + 2 WHERE m.rightVal > :rightVal"
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : ""))
                 .setParameter("rightVal", val[0]).executeUpdate();
    current.setLeftVal(val[1]);
    current.setRightVal(val[1] + 1);
    current.setTreeKind(target.getTreeKind());
    current.setLeaf(Boolean.TRUE);
  }

  public static void move(NestedTree target, NestedTree current, EntityManager entityManager,
                          Relationship relationship) {
    Assert.notNull(target, "目标节点不能为空！");
    Assert.notNull(current, "当前节点不能为空！");
    Assert.isTrue(target.getClass().equals(current.getClass()), "父节点和当前节点类型必须一致！");
    boolean hasTreeKind = target.getTreeKind() != null;

    int[] val = NestedSetModel
      .update(target.getLeftVal(), target.getRightVal(), current.getLeftVal(), current.getRightVal(), relationship);
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.leftVal = m.leftVal * -1, m.rightVal = m.rightVal * -1 WHERE m.leftVal >= :leftVal AND m.rightVal <= :rightVal"
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : ""))
                 .setParameter("leftVal", current.getLeftVal())
                 .setParameter("rightVal", current.getRightVal())
                 .executeUpdate();
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.leftVal = m.leftVal + "+val[3]+" WHERE m.leftVal > "+val[0]+" AND m.leftVal < " + val[1]
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : "")).executeUpdate();
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.rightVal = m.rightVal + "+val[3]+" WHERE m.rightVal > "+val[0]+" AND m.rightVal < " + val[1]
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : "")).executeUpdate();
    entityManager.createQuery(
      "UPDATE " + target.getClass().getSimpleName()
        + " m SET m.leftVal = m.leftVal * -1 + "+val[2]+", m.rightVal = m.rightVal * -1 + "+val[2]
        +" WHERE m.leftVal <= :leftVal * -1 AND m.rightVal >= :rightVal * -1 "
        + (hasTreeKind ? " AND m.treeKind = '"+target.getTreeKind()+"'" : ""))
                 .setParameter("leftVal", current.getLeftVal())
                 .setParameter("rightVal", current.getRightVal())
                 .executeUpdate();
    current.setTreeKind(target.getTreeKind());
    current.setLeaf(Boolean.TRUE);
  }

  public static void delete(NestedTree current, EntityManager entityManager) {
    Assert.notNull(current, "当前节点不能为空！");
    boolean hasTreeKind = current.getTreeKind() != null;
    int[] val = NestedSetModel.delete(current.getLeftVal(), current.getRightVal());
    entityManager.createQuery(
      "DELETE FROM " + current.getClass().getSimpleName()
        + " m WHERE m.leftVal >= :leftVal AND m.rightVal <= :rightVal"
        + (hasTreeKind ? " AND m.treeKind = '"+current.getTreeKind()+"'" : ""))
                 .setParameter("leftVal", val[0])
                 .setParameter("rightVal", val[1]).executeUpdate();
    entityManager.createQuery(
      "UPDATE " + current.getClass().getSimpleName()
        + " m SET m.leftVal = m.leftVal - :VAL1 WHERE m.leftVal > :VAL2"
        + (hasTreeKind ? " AND m.treeKind = '"+current.getTreeKind()+"'" : ""))
                 .setParameter("VAL1", val[1] - val[0] + 1)
                 .setParameter("VAL2", val[0]).executeUpdate();
    entityManager.createQuery(
      "UPDATE " + current.getClass().getSimpleName()
        + " m SET m.rightVal = m.rightVal - :VAL1 WHERE m.rightVal > :VAL2"
        + (hasTreeKind ? " AND m.treeKind = '"+current.getTreeKind()+"'" : ""))
                 .setParameter("VAL1", val[1] - val[0] + 1)
                 .setParameter("VAL2", val[1]).executeUpdate();
  }

}
