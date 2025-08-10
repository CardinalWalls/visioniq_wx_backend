package com.wk.vpac.main.service.api.sys;

import com.base.components.common.id.IdGenerator;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.Md5Util;
import com.base.components.database.jpa.service.AbstractJpaService;
import com.wk.vpac.database.dao.sys.SysMd5JsonDao;
import com.wk.vpac.domain.sys.SysMd5Json;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.TreeMap;

/**
 * SysMd5JsonService
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2024-10-07 22:44
 */
@Service
public class SysMd5JsonService extends AbstractJpaService<SysMd5Json, String, SysMd5JsonDao> {

  @Transactional(rollbackFor = Exception.class)
  public String toMd5(TreeMap map){
    String md5 = "";
    try {
      if(!map.isEmpty()){
        String json = JsonUtils.toString(map);
        md5 = Md5Util.md5(json);
        SysMd5Json sysMd5Json = getDao().findById(md5).orElse(null);
        //如果存在相同的MD5
        if(sysMd5Json != null) {
          //paramJson相同则直接返回
          if(json.equals(sysMd5Json.getParamJson())){
            return md5;
          }
          //paramJson不同则插入随机数重新生成
          else{
            map.put(IdGenerator.getInstance().generate().toString(), 0);
            json = JsonUtils.toString(map);
            md5 = Md5Util.md5(json);
          }
        }
        SysMd5Json j = new SysMd5Json();
        j.setId(md5);
        j.setParamJson(json);
        save(j);
      }
    } catch (Exception ignore) {
    }
    return md5;
  }


  public String getParam(String md5) {
    SysMd5Json sysMd5Json = getDao().findById(md5).orElse(null);
    String param = "{}";
    if(sysMd5Json != null){
      param = sysMd5Json.getParamJson();
    }
    return param;
  }

}
