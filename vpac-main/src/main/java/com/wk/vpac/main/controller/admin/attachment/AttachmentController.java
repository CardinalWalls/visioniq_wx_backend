package com.wk.vpac.main.controller.admin.attachment;

import com.base.components.common.dto.sys.JsonResult;
import com.base.components.common.log.system.SystemLogger;
import com.base.components.common.util.ConvertUtil;
import com.base.components.common.util.JsonUtils;
import com.base.components.common.util.ServletContextHolder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wk.vpac.common.constants.sys.GatewayPath;
import com.wk.vpac.main.service.admin.attachment.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 后台附件
 *
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/4/4 0004 10:38
 */
@SystemLogger
@Controller
@RequestMapping(GatewayPath.ADMIN)
public class AttachmentController {

  @Autowired
  private AttachmentService attachmentService;

  /**
   * 临时上传附件返回临时地址
   *
   * @param file - file文件流
   *
   * @return
   */
  @PostMapping(value = "/attachment/uploadAtta")
  @ResponseBody
  public ResponseEntity uploadAtta(@RequestParam MultipartFile[] file, @RequestParam Map<String, String> params) {
    JsonResult res;
    try {
      if(file.length>1){
        ArrayNode arrayNode = JsonUtils.createArrayNode();
        for (MultipartFile multipartFile : file) {
          ObjectNode objectNode = upload(params, multipartFile);
          arrayNode.add(objectNode);
        }
        res = JsonResult.success(arrayNode);
      }else {
        ObjectNode objectNode = upload(params, file[0]);
        res = JsonResult.success(objectNode);
      }
    } catch (Exception e) {
      e.printStackTrace();
      res = JsonResult.fail(e.getMessage());
    }
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  /**
   * 临时上传网络附件返回临时地址
   *
   * @return
   */
  @PostMapping(value = "/attachment/uploadAttaForWebUrl")
  @ResponseBody
  public ResponseEntity uploadAttaForWebUrl(@RequestBody Map<String, String> params) {
    String url = ConvertUtil.checkNotNull(params,"url","网络文件地址不能为空",String.class);
    JsonResult res;
    try {
      MultipartFile multipartFile = createFileItem(url,System.currentTimeMillis() + ".jpg");
      ObjectNode objectNode = upload(params, multipartFile);
      res = JsonResult.success(objectNode);
    } catch (Exception e) {
      e.printStackTrace();
      res = JsonResult.fail(e.getMessage());
    }
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  /**
   * 临时上传附件返回临时地址 - kindeditor
   *
   * @param imgFile
   *
   * @return
   */
  @PostMapping(value = "/attachment/uploadAtta/kindeditor")
  @ResponseBody
  public ResponseEntity uploadAttaForKindeditor(@RequestParam MultipartFile imgFile,
                                                @RequestParam Map<String, String> params) {
    ObjectNode res = JsonUtils.createObjectNode();
    try {
      ObjectNode objectNode = attachmentService.uploadAtta(imgFile, StringUtils.isBlank(params.get("isPrivate"))
                                                                    ? false
                                                                    : Boolean.valueOf(params.get("isPrivate")));
      res.put("error", 0);
      res.putPOJO("url", objectNode.get("url"));
    } catch (Exception e) {
      e.printStackTrace();
      res.put("error", 1);
      res.putPOJO("url", "上传失败");
    }
    return new ResponseEntity<>(res.toString(), HttpStatus.OK);
  }

  private ObjectNode upload(@RequestParam Map<String, String> params, MultipartFile multipartFile) {
    return attachmentService.uploadAtta(multipartFile, StringUtils.isBlank(params.get("isPrivate"))
                                                       ? false
                                                       : Boolean.valueOf(params.get("isPrivate")));
  }

  @PostMapping(value = "/attachment/upload/no/token")
  @ResponseBody
  public ResponseEntity uploadAttaNoToken(@RequestParam MultipartFile file, @RequestParam Map<String, String> params,
                                          HttpServletRequest request) {
    JsonResult res;
    try {
      String contactPhone = request.getParameter("contactPhone");
      // 2018-12-05修改为不验证短信
      //      Assert.hasText(contactPhone,"联系电话不能为空");
      //      tokenValidate.validate(contactPhone,request.getCookies());
      ObjectNode objectNode = attachmentService.uploadAtta(file, StringUtils.isBlank(params.get("isPrivate"))
                                                                 ? false
                                                                 : Boolean.valueOf(params.get("isPrivate")));
      res = JsonResult.success(objectNode);
    } catch (Exception e) {
      e.printStackTrace();
      res = JsonResult.fail(e.getMessage());
    }
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping(value = "/attachment/redirect")
  public String redirect(@RequestParam Map<String, String> params) {
    String s = attachmentService.redirectInternals(params);
    return ServletContextHolder.sendRedirectString("redirect:" + s);
  }


  /**
   * 网络图片转成MultipartFile
   * @param url
   * @param fileName
   * @return
   * @throws Exception
   */
  private static MultipartFile createFileItem(String url, String fileName) throws Exception{
    FileItem item = null;
    try {
      HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setReadTimeout(30000);
      conn.setConnectTimeout(30000);
      //设置应用程序要从网络连接读取数据
      conn.setDoInput(true);
      conn.setRequestMethod("GET");
      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream is = conn.getInputStream();

        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "uploadfile";
        item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false, fileName);
        OutputStream os = item.getOutputStream();

        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
          os.write(buffer, 0, bytesRead);
        }
        os.close();
        is.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("文件下载失败", e);
    }

    return new AttachmentService.CommonsMultipartFile(item);
  }

}
