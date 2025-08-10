package com.wk.vpac.main.service.api.region;

import com.base.components.common.constants.Valid;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.SqlLikeHelper;
import com.base.components.database.jpa.common.condition.ConditionEnum;
import com.base.components.database.jpa.common.condition.ConditionGroup;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wk.vpac.database.dao.sys.RegionDao;
import com.wk.vpac.domain.sys.Region;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RegionService
 *
 * @author <a href="morse.jiang@foxmail.com">JiangWen</a>
 * @version 1.0.0, 2018/3/30 0030 16:23
 */
@Service
public class RegionService extends AbstractJpaService<Region, String, RegionDao> {

  private static final Set<String> REGION_SUFFIX = Collections
    .unmodifiableSet(Sets.newHashSet("区", "县", "域", "岛", "州", "市", "旗", "盟", "省", "辖"));

  /**
   * 根据parentId查询地区列表
   *
   * @param parentId
   *
   * @return
   */
  public List<Region> getRegionByParentId(String parentId) {
    List<Region> list;
    if (StringUtils.isNotEmpty(parentId)) {
      list = getDao().findByParentId(parentId, Valid.TRUE.getVal());
    } else {
      list = getDao().findRoots(Valid.TRUE.getVal());
    }
    return list;
  }

  public List<Region> getAllCity() {
    List<Region> list = findAll(ConditionGroup.build().addCondition("level", ConditionEnum.OPERATE_EQUAL, 2)
                                              .addCondition("status", ConditionEnum.OPERATE_EQUAL, 1));
    return list;
  }


  /**
   * 获取全部地区树-三级
   *
   * @return
   */
  public ArrayNode getRegionTree() {
    //一级
    List<Region> list1 = getDao().findRoots();
    ArrayNode arr1 = JsonUtils.createArrayNode();
    list1.forEach(region1 -> {
      ObjectNode objectNode1 = JsonUtils.createObjectNode();
      objectNode1.put("id", region1.getId());
      objectNode1.put("name", region1.getName());
      //二级
      List<Region> list2 = getDao().findByParentId(region1.getId());
      ArrayNode arr2 = JsonUtils.createArrayNode();
      list2.forEach(region2 -> {
        ObjectNode objectNode2 = JsonUtils.createObjectNode();
        objectNode2.put("id", region2.getId());
        objectNode2.put("name", region2.getName());
        //三级
        List<Region> list3 = getDao().findByParentId(region2.getId());
        ArrayNode arr3 = JsonUtils.createArrayNode();
        list3.forEach(region3 -> {
          ObjectNode objectNode3 = JsonUtils.createObjectNode();
          objectNode3.put("id", region3.getId());
          objectNode3.put("name", region3.getName());
          arr3.addPOJO(objectNode3);
        });
        objectNode2.putPOJO("district", arr3);
        arr2.addPOJO(objectNode2);
      });
      objectNode1.putPOJO("city", arr2);
      arr1.addPOJO(objectNode1);
    });
    return arr1;
  }


  public String getRegionId(String[] address) {
    String queryAddress = "";
    for (int i = address.length - 1; i >= 0; i--) {
      if (StringUtils.isNotBlank(address[i])) {
        queryAddress = address[i];
        break;
      }
    }
    ConditionGroup<Region> build = ConditionGroup.build()
                                                 .addCondition("name", ConditionEnum.OPERATE_RIGHT_LIKE, queryAddress);
    List<Region> regions = getDao().findAll(build, Sort.by(Sort.Direction.DESC, "orderNo"));
    if (regions.size() > 0) {
      return regions.get(0).getId();
    }
    return null;
  }

  public List<List<Map<String, Object>>> findSelected(String regionId) {
    List<Region> selectedList = getDao().findParentsAndSelf(regionId);
    List<List<Map<String, Object>>> list = Lists.newArrayList();
    for (Region region : selectedList) {
      list.add(getDao().findSelected(region.getParentId(), region.getId()));
    }
    return list;
  }


  public List<Map<String, String>> getRegionByNameAndLevel(Map<String, String> params) {
    String name = ConvertUtil.checkNotNull(params, "name", "名称为空", String.class).trim();
    int level = ConvertUtil.checkNotNull(params, "level", "等级为空", Integer.class);
    Assert.isTrue(level > 0 && level < 4, "level 范围为 1 - 3");
    name = cleanSuffix(name);
    List<Region> list = getDao().findAll(
      ConditionGroup.build().addCondition("level", ConditionEnum.OPERATE_EQUAL, level)
                    .addCondition("name", ConditionEnum.OPERATE_RIGHT_LIKE, name));
    return list.stream().map(r -> {
      Map<String, String> row = Maps.newHashMap();
      row.put("regionId", r.getId());
      row.put("regionName", r.getName());
      row.put("namePath", r.getNamePath());
      return row;
    }).collect(Collectors.toList());
  }

  private String cleanSuffix(String name) {
    if (REGION_SUFFIX.contains(name.substring(name.length() - 1))) {
      return name.substring(0, name.length() - 1);
    }
    return name;
  }


  /**
   * 查询地区根据[省、市、区]名称
   */
  public Map<String, String> getRegionByName(Map<String, String> params) {
    String province = ConvertUtil.checkNotNull(params, "province", "省名称为空", String.class).trim();
    String city = ConvertUtil.checkNotNull(params, "city", "市名称为空", String.class).trim();
    String district = ConvertUtil.checkNotNull(params, "district", "区称为空", String.class).trim();
    province = cleanSuffix(province);
    Assert.hasText(province, "省名称为空");
    city = cleanSuffix(city);
    Assert.hasText(city, "市名称为空");
    district = cleanSuffix(district);
    Assert.hasText(district, "区称为空");
    String namePath = SqlLikeHelper.right(province) + "-" + SqlLikeHelper.right(city) + "-" + SqlLikeHelper
      .right(district);
    List<Region> regions = getDao()
      .findAll(ConditionGroup.build().addCondition("namePath", ConditionEnum.OPERATE_RIGHT_LIKE, namePath));
    Map<String, String> res = Maps.newHashMap();
    Region region = null;
    if(regions.isEmpty()){
      List<Region> provinceList = getDao().findAll(
        ConditionGroup.build().addCondition("name", ConditionEnum.OPERATE_RIGHT_LIKE,
                                            province.length() > 2 ? province.substring(0, 2) : province
      ).addCondition("level", ConditionEnum.OPERATE_EQUAL, 1));
      if(!provinceList.isEmpty()){
        List<Region> cityList = getDao().findAll(
          ConditionGroup.build().addCondition("name", ConditionEnum.OPERATE_RIGHT_LIKE,
                                              city.length() > 2 ? city.substring(0, 2) : city
          ).addCondition("level", ConditionEnum.OPERATE_EQUAL, 2)
                        .addCondition("parentId", ConditionEnum.OPERATE_EQUAL, provinceList.get(0).getId()));
        if(!cityList.isEmpty()){
          List<Region> districtList = getDao().findAll(
            ConditionGroup.build().addCondition("name", ConditionEnum.OPERATE_RIGHT_LIKE,
                                                district.length() > 2 ? district.substring(0, 2) : district
            ).addCondition("level", ConditionEnum.OPERATE_EQUAL, 3)
                          .addCondition("parentId", ConditionEnum.OPERATE_EQUAL, cityList.get(0).getId()));
          if(!districtList.isEmpty()){
            region = districtList.get(0);
          }
        }
      }
    } else {
      region = regions.get(0);
    }
    if(region != null){
      res.put("regionId", region.getId());
      res.put("cityId", region.getCityId());
      res.put("provinceId", region.getProvinceId());
      res.put("regionName", region.getName());
      res.put("namePath", region.getNamePath());
    }
    return res;
  }

}
