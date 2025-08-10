package com.wk.vpac.database.util;

import com.base.components.common.dto.tree.NestedTree;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.Relationship;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.base.components.database.jpa.util.NestedTreeUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * BaseTreeService 抽象树型结构服务，实体类必要字段：left_val，right_val，parent_id，tree_kind
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2021-08-18 15:29
 */
public abstract class BaseTreeService<T extends NestedTree & java.io.Serializable, Dao extends GenericJpaDao<T, String>>
  extends AbstractJpaService<T, String, Dao> {

  /**
   * 保存节点
   * @param params
   * @param id
   * @param newInstance  构造函数
   * @param saveBefore   保存前的回调检查，回调参数：当前节点
   * @param saveAfter    保存后的回调检查，回调参数：当前节点，父节点
   * @param canEditField 可修改的字段
   * @return
   */
  public T saveNode(Map<String, String> params, String id, Supplier<T> newInstance, Consumer<T> saveBefore,
                    BiConsumer<T, T> saveAfter, String...canEditField){
    T current;
    if(StringUtils.isBlank(id)){
      current = ConvertUtil.populate(newInstance.get(), params);
      current.setId(null);
      saveBefore.accept(current);
    }else{
      current = findById(id);
      if(canEditField != null){
        for (String field : canEditField) {
          ConvertUtil.setPropertyIfNotBlank(current, field, params.get(field));
        }
      }
      saveBefore.accept(current);
    }
    current = saveAndFlush(current);
    if(StringUtils.isBlank(id)){
      T parent = findById(current.getParentId());
      saveAfter.accept(current, parent);
      NestedTreeUtil.addNode(parent, current, getEntityManager(), Relationship.ChildLast);
    }
    return current;
  }

  /**
   * 移动节点
   * @param src 当前节点
   * @param targetId 父节点ID；moveType = child 时必填
   * @param moveType child、before、after
   */
  public void moveNode(T src, String targetId, String moveType) {
    Relationship relationship;
    T target;
    if("child".equalsIgnoreCase(moveType)){
      relationship = Relationship.ChildLast;
      Assert.hasText(targetId, "请选择移动到的目标");
      target = findById(targetId);
      Assert.notNull(target, "未找到目标数据");
      Assert.isTrue(target.getTreeKind().equals(src.getTreeKind()), "类型不同，不能移动");
      Assert.isTrue(target.getRightVal() < src.getLeftVal() || target.getLeftVal() > src.getRightVal(), "目标不能为当前的子节点");
    }
    else if("before".equalsIgnoreCase(moveType)){
      relationship = Relationship.Before;
      target = findAdjacent(src.getTreeKind(), src.getParentId(), null,src.getLeftVal() - 1);
    }
    else {
      relationship = Relationship.After;
      target = findAdjacent(src.getTreeKind(), src.getParentId(), src.getRightVal() + 1,null);
    }
    if(target != null){
      JpaHelper.detach(src, target);
      NestedTreeUtil.updateToMove(target, src, relationship, getEntityManager());
      if(relationship.equals(Relationship.ChildLast)){
        getEntityManager().createQuery("UPDATE "+getTableName()+" m SET m.parentId = :parentId WHERE m.id = :id")
                          .setParameter("parentId", target.getId()).setParameter("id", src.getId()).executeUpdate();
        getEntityManager().flush();
        Integer srcLevel = getLevel(src);
        Integer targetLevel = getLevel(target);
        if(srcLevel != null && srcLevel != null){
          //更新层级
          List<Object[]> list = getEntityManager()
            .createQuery("SELECT m.leftVal, m.rightVal FROM "+getEntityName()+" m WHERE m.id = :id")
            .setParameter("id", src.getId()).getResultList();
          if (!list.isEmpty()) {
            Object[] values = list.get(0);
            getEntityManager().createQuery("UPDATE "+getEntityName()+" m SET m.level = m.level + :level WHERE m.treeKind = :treeKind AND m.leftVal >= :leftVal AND m.rightVal <= :rightVal")
                              .setParameter("treeKind", src.getTreeKind()).setParameter("leftVal", values[0])
                              .setParameter("rightVal", values[1]).setParameter("level", targetLevel + 1 - srcLevel).executeUpdate();
          }
        }
      }
    }
  }

  /** 查询直属子节点对象 */
  public List<T> findChildren(@NonNull String treeKind, @NonNull  String parentId, Consumer<ConditionGroup<T>> otherCondition){
    ConditionGroup<T> condition = ConditionGroup.build().addCondition("treeKind", ConditionEnum.OPERATE_EQUAL, treeKind)
                                                .addCondition("parentId", ConditionEnum.OPERATE_EQUAL, parentId);
    if(otherCondition != null){
      otherCondition.accept(condition);
    }
    return findAll(condition, Sort.by("leftVal"));
  }
  /** 查询所有子节点对象 */
  public List<T> findAllChildren(@NonNull String treeKind, String parentId, @Nullable Consumer<ConditionGroup<T>> otherCondition){
    T parent = findById(parentId);
    if(parent != null && Objects.equals(treeKind, parent.getTreeKind())){
      ConditionGroup<T> condition = ConditionGroup.build().addCondition("treeKind", ConditionEnum.OPERATE_EQUAL, treeKind)
                    .addCondition("leftVal", ConditionEnum.OPERATE_GREATER, parent.getLeftVal())
                    .addCondition("rightVal", ConditionEnum.OPERATE_LESS, parent.getRightVal());
      if(otherCondition != null){
        otherCondition.accept(condition);
      }
      return findAll(condition, Sort.by("leftVal"));
    }
    return Collections.emptyList();
  }

  /** 查询所有子节点树型对象 */
  public List<TreeNode<T>> findAllChildrenNode(@NonNull String treeKind, @NonNull String parentId,
                                               @Nullable Consumer<ConditionGroup<T>> otherCondition) {
    List<TreeNode<T>> nodes = new ArrayList<>();
    if (parentId != null) {
      List<T> list = findAllChildren(treeKind, parentId, otherCondition);
      if(!CollectionUtils.isEmpty(list)){
        ListMultimap<String, T> parentIdMap = LinkedListMultimap.create();
        Set<String> ids = Sets.newHashSet();
        for (T entity : list) {
          TreeNode<T> node = new TreeNode<>(entity);
          ids.add(entity.getId());
          parentIdMap.put(entity.getParentId(), entity);
          //先将自己的 id 一定会先放入 ids 中，
          //因为是左值排序，自己的 ParentId 一定会先放入 ids 中，
          //如果自己的ParentId不在 ids 中，则为第一级节点
          if (!ids.contains(entity.getParentId())) {
            nodes.add(node);
          }
        }
        for (TreeNode<T> node : nodes) {
          buildChildren(node, parentIdMap);
        }
      }
    }
    return nodes;
  }

  private void buildChildren(TreeNode<T> node, ListMultimap<String, T> parentIdMap) {
    List<T> children = Lists.newArrayList(parentIdMap.get(node.getId()));
    if (!CollectionUtils.isEmpty(children)) {
      Iterator<T> iterator = children.iterator();
      while (iterator.hasNext()){
        TreeNode<T> child = new TreeNode<>(iterator.next());
        buildChildren(child, parentIdMap);
        node.addChild(child);
      }
    }
  }

  /** 查找相邻 */
  private T findAdjacent(String treeKind, String parentId, Integer leftVal, Integer rightVal){
    ConditionGroup<T> condition = ConditionGroup
      .build().addCondition("parentId", ConditionEnum.OPERATE_EQUAL, parentId)
      .addCondition("leftVal", ConditionEnum.OPERATE_EQUAL, leftVal, true)
      .addCondition("rightVal", ConditionEnum.OPERATE_EQUAL, rightVal, true)
      .addCondition("treeKind", ConditionEnum.OPERATE_EQUAL, treeKind);
    List<T> list = findAll(condition);
    return CollectionUtils.isEmpty(list) ? null : list.get(0);
  }

  /** 数据库表名 */
  protected abstract String getTableName();
  /** 实体类名 */
  protected abstract String getEntityName();
  /** 获取层级，返回空时表示对象中不包含level字段 */
  protected abstract Integer getLevel(T node);




  /**
   * 构建查询
   *
   * 懒加载: 查询下级节点
   * 业务查询参数：如 name、phone 等，业务参数查询时，会查询出满足条件的节点，并查询这些节点的所有父级，以构建完整的树形结构，
   *              懒加载时不会带上这些参数；
   * 全局查询参数：如 valid 等，这些参数会直接过滤每次的查询结果，包括懒加载的查询；
   *
   * @param hasQueryField   是否含有业务查询字段，如：姓名、手机号等
   * @param params          原接口参数
   * @param table           原表名
   * @param tableAlias      原表别名
   * @param baseParamBuild  全局查询参数构建，参数：原表sqlBuilder
   * @param noneQueryField  不含有业务查询字段时执行片段，用于设置全局查询参数，回调参数：是否为懒加载，原表sqlBuilder
   * @param withQueryField  含有业务查询字段时执行片段，用于设置业务查询参数，回调参数：子查询表别名，子查询sqlBuilder
   * @return Query
   */
  public static Query buildQuery(boolean hasQueryField, Map params,
                                 String table, String tableAlias,
                                 Consumer<NativeSQLBuilder> baseParamBuild,
                                 BiConsumer<Boolean, NativeSQLBuilder> noneQueryField,
                                 BiConsumer<String, NativeSQLBuilder> withQueryField) {
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    baseParamBuild.accept(sqlBuilder);
    if (hasQueryField) {
      NativeSQLBuilder innerBuilder = NativeSQLBuilder.defaultAnd();
      innerBuilder.bindQuerySql(
        "SELECT 1 FROM " + table + " _t0_ WHERE " + tableAlias + ".left_val <= _t0_.left_val AND " + tableAlias
          + ".right_val >= _t0_.right_val AND " + tableAlias + ".tree_kind = _t0_.tree_kind");
      withQueryField.accept("_t0_", innerBuilder);
      sqlBuilder.addWhere("AND EXISTS (__EXISTS__)");
      return sqlBuilder.buildFrom(JpaHelper.getEntityManager(), ImmutableMap.of("__EXISTS__", innerBuilder));
    }
    else {
      boolean treeLazyLoad = ConvertUtil.convert(params.get("treeLazyLoad"), true);
      noneQueryField.accept(treeLazyLoad, sqlBuilder);
      return sqlBuilder.build(JpaHelper.getEntityManager());
    }
  }
}
