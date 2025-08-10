

package com.wk.vpac.database.dao.admin;

import com.base.components.common.dto.page.DataPage;
import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.base.components.database.jpa.dao.base.NestedTreeQuery;
import com.base.components.database.jpa.util.JpaHelper;
import com.base.components.database.jpa.util.NativeSQLBuilder;
import com.wk.vpac.domain.admin.SysMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SysMember DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2018-03-17
 */
@Repository
public interface SysMemberDao extends GenericJpaDao<SysMember, String>, NestedTreeQuery<SysMember> {

  String ADMIN_TREE_KIND = "ADMIN";

  String PROFESSION_TREE_KIND = "PROFESSION";

  /**
   * 根节点的 PARENT_ID
   */
  String ROOT_PARENT = "";


  /**
   * 查询根节点集合
   * @return roots
   */
  @Query("from SysMember where parentId = '"+ROOT_PARENT+"' and treeKind like :treeKind order by leftVal")
  List<SysMember> findRoots(@Param("treeKind") String treeKind);

  /**
   * 查询所有节点集合
   * @return roots
   */
  @Query("from SysMember order by leftVal")
  List<SysMember> findAllNode();

  @Query("from SysMember where treeKind = :treeKind order by leftVal")
  List<SysMember> findAllNode(@Param("treeKind") String treeKind);

  /**
   * 查询第一层子节点
   * @param parentId  父节点ID
   *
   * @return
   */
  List<SysMember> findByParentIdOrderByLeftVal(String parentId);

  /**
   * 更加帐号查找
   * @param account 帐号
   *
   * @return
   */
  SysMember findByAccount(String account);

  @Query(value = "UPDATE admin_sys_member SET last_login_ip = :loginIp, last_login_time = NOW() WHERE id = :id",
    nativeQuery = true)
  @Modifying
  int updateLoginInfo(@Param("id") String memberId, @Param("loginIp") String loginIp);




  @Query(value = "select id,case when parent_id is null or parent_id='' then 0 else parent_id end as parentId,member_name as name,status from admin_sys_member "
    + "where tree_kind like :regionId",
    nativeQuery = true)
  List<Map> memberTree(@Param("regionId") String regionId);

  @Query(value = "select id,case when parent_id is null or parent_id='' then 0 else parent_id end as parentId,name,status from base_region where status=1",
    nativeQuery = true)
  List<Map> regionTree();

  @Query("from SysMember where memberName like ?1")
  List<SysMember> findByMemberName(String memberName);

  default DataPage<Map<String, Object>> simplePage(Map<String, String> params){
    String querySql = "SELECT d.id,d.member_name as name FROM admin_sys_member d WHERE d.member_type=1 ";
    String countSql = "SELECT COUNT(d.id) FROM admin_sys_member d WHERE d.member_type=1 ";
    NativeSQLBuilder sqlBuilder = NativeSQLBuilder.defaultAnd();
    if (StringUtils.isNotEmpty(params.get("searchValue"))) {
      sqlBuilder.addWhereIn("d", params.get("searchKey"), Arrays.asList(params.get("searchValue").split(",")));
    }
    String name = params.get("name");
    if(StringUtils.isNotBlank(name)){
      sqlBuilder.addWhereRightLike("d", "member_name", name);
    }
    jakarta.persistence.Query count = sqlBuilder.bindQuerySql(countSql).build(JpaHelper.getEntityManager());
    jakarta.persistence.Query query = JpaHelper.setMapResult(
      sqlBuilder.bindQuerySql(querySql).orderBy("d.left_val").build(JpaHelper.getEntityManager()));
    return JpaHelper.pageByQuery(count, query, params);
  }
}

