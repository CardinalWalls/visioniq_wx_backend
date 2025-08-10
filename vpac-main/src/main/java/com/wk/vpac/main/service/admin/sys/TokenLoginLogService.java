package com.wk.vpac.main.service.admin.sys;

import com.base.components.common.boot.SpringContextUtil;
import com.base.components.common.dto.page.DataPage;
import com.base.components.common.log.system.SystemLogRepository;
import com.base.components.common.service.ThreadPoolService;
import com.base.components.common.token.TokenCacheObj;
import com.base.components.common.token.TokenThreadLocal;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.IPUtils;
import com.base.components.common.util.IpLocationUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.transaction.TransactionManager;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.database.dao.sys.TokenLoginLogDao;
import com.wk.vpac.database.dao.sys.TokenRequestLogDao;
import com.wk.vpac.domain.sys.TokenLoginLog;
import com.wk.vpac.domain.sys.TokenRequestLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Map;

/**
 * TokenLoginLogService
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2020-09-22 11:03
 */
@Service
public class TokenLoginLogService implements SystemLogRepository {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Autowired
  private ThreadPoolService threadPoolService;
  @Autowired
  private TokenRequestLogDao tokenRequestLogDao;
  @Autowired
  private TokenLoginLogDao tokenLoginLogDao;

  public DataPage page(Map<String, String> params) {
    return tokenLoginLogDao.page(params);
  }

  public DataPage requestPage(Map<String, String> params) {
    return ConvertUtil.convert(params.get("doGroup"), 0) == 0
           ? tokenRequestLogDao.page(params)
           : tokenRequestLogDao.group(params);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void addLog(Object manualInvokeParam, @NonNull String invokeMethodName, @NonNull String serverHost, HttpServletRequest request) {
    if(manualInvokeParam instanceof TokenCacheObj){
      TokenCacheObj tokenCacheObj = (TokenCacheObj)manualInvokeParam;
      if (request != null) {
        String realIp = IPUtils.getRealIp(request);
        if (StringUtils.isNotBlank(realIp)) {
          TokenLoginLog log = new TokenLoginLog();
          log.setCreateTime(new Date());
          log.setLoginIp(realIp);
          log.setLoginLocation(IpLocationUtil.findToString(realIp));
          log.setTokenObjName(tokenCacheObj.objName());
          try {
            ObjectNode node = JsonUtils.convert(tokenCacheObj, ObjectNode.class);
            node.remove("id");
            node.remove("token");
            node.remove("userId");
            log.setTokenObjJson(node.toString());
          } catch (Exception ignore) {
          }
          log.setToken(tokenCacheObj.getToken());
          log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
          log.setUserId(tokenCacheObj.objId().toString());
          log.setTokenType(tokenCacheObj.getClass().getSimpleName());
          log.setServerHost(serverHost);
          tokenLoginLogDao.saveAndFlush(log);
        }
      }
    }
  }

  @Override
  public void addLog(@NonNull Map<String, Object> methodParams, @NonNull String invokeMethodName, @NonNull String serverHost,
                     HttpServletRequest request, String docName) {
    if (request != null){
      TokenCacheObj tokenObj = TokenThreadLocal.getTokenObj();
      if (tokenObj != null) {
        String realIp = IPUtils.getRealIp(request);
        String requestURI = request.getRequestURI();
        threadPoolService.run(() -> {
          TokenRequestLog log = new TokenRequestLog();
          log.setCreateTime(new Date());
          log.setIp(realIp);
          log.setIpLocation(IpLocationUtil.findToString(log.getIp()));
          log.setMethod(request.getMethod());
          log.setServerHost(serverHost);
          log.setToken(tokenObj.getToken());
          log.setApiName(docName);
          log.setUri(requestURI);
          try {
            String params = JsonUtils.toString(methodParams);
            log.setParams(params.length() > 3800 ? params.substring(0, 3800) : params);
          } catch (Exception ignore) {
          }
          tokenRequestLogDao.saveAndFlush(log);
        });
      }
    }
  }

  @Override
  public void clearLogs(Date deadline) {
    long s = System.currentTimeMillis();
    TransactionManager.openTransaction(SpringContextUtil.getBean(PlatformTransactionManager.class),
                                       TransactionDefinition.PROPAGATION_REQUIRED, () -> {
        int login = tokenLoginLogDao.deleteByLastTime(deadline);
        int request = tokenRequestLogDao.deleteWithoutLoginRecord();
        logger.info("clear > loginLogCount = {}, requestLogCount = {}, time-consuming-ms = {}", login, request,
                    System.currentTimeMillis() - s
        );
        return null;
      }, e -> {
        logger.error("clear > fail", e);
        return null;
      }
    );
  }

}