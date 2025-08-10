package com.wk.vpac.database.dao.user;

import com.base.components.database.jpa.dao.base.GenericJpaDao;
import com.wk.vpac.domain.user.UserAddress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserAddress DAO
 *
 * @author : code generator
 * @version : 1.0
 * @since : 2019-06-09
 */
@Repository
public interface UserAddressDao extends GenericJpaDao<UserAddress, String> {
  @Query("update UserAddress set valid = false where userId = :userId")
  @Modifying
  int updateDisable(@Param("userId") String userId);

  @Query("update UserAddress set valid = :valid where id = :id")
  @Modifying
  int updateValid(@Param("id") String id, @Param("valid") boolean valid);

  @Query("select a from UserAddress a where a.userId = :userId order by a.valid desc")
  List<UserAddress> findByUserId(@Param("userId") String userId);
}

