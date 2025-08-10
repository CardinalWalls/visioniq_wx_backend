package com.wk.vpac.main.service.admin.attachment;

import com.base.components.common.dto.io.FileDTO;
import com.base.components.common.exception.business.BusinessException;
import com.base.components.common.util.JsonUtils;
import com.base.components.oss.OssRedirectClient;
import com.base.components.oss.OssServiceClient;
import com.base.components.oss.constants.OssConstant;
import com.base.components.oss.util.OssTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.wk.vpac.common.constants.admin.AuthCode;
import com.wk.vpac.common.service.service.util.JsoupHelper;
import com.wk.vpac.main.util.AuthUtilHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="tecyun@foxmail.com">Huangyunyang</a>
 * @version 1.0.0, 2018/4/4 0004 10:40
 */
@Service
//@RefreshScope
public class AttachmentService {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Autowired
  private OssServiceClient ossServiceClient;
  @Autowired
  private OssRedirectClient ossRedirectClient;

//  @Value("${base.oss.impl:local}")
//  private String uploadType;

  @Value("${base.host.api}")
  private String hostApi;

  public String persistent(String adminAttachment){
    return persistent(adminAttachment, "", false);
  }
  public String persistent(String adminAttachment, String errorDefaultVal){
    return persistent(adminAttachment, errorDefaultVal, false);
  }
  public String persistent(String adminAttachment, String errorDefaultVal, boolean throwEx){
    try {
      ArrayNode arrayNode = StringUtils.isBlank(adminAttachment) ? JsonUtils.createArrayNode()
                                                                 : JsonUtils.reader(adminAttachment, ArrayNode.class);
      if(arrayNode.isEmpty()){
        return errorDefaultVal == null ? "" : errorDefaultVal;
      }
      return persistent(arrayNode, false, "").get(0).path("url").asText();
    } catch (Exception e) {
      if(throwEx){
        throw new IllegalArgumentException("附件解析出错", e);
      }else{
        logger.error("附件解析出错", e);
        return errorDefaultVal == null ? "" : errorDefaultVal;
      }
    }
  }

  public ArrayNode persistentArray(String adminAttachment){
    return persistentArray(adminAttachment, null, false);
  }
  public ArrayNode persistentArray(String adminAttachment, Supplier<ArrayNode> defaultValue){
    return persistentArray(adminAttachment, defaultValue, false);
  }
  public ArrayNode persistentArray(String adminAttachment, Supplier<ArrayNode> defaultValue, boolean throwEx){
    try {
      ArrayNode arrayNode = StringUtils.isBlank(adminAttachment) ? JsonUtils.createArrayNode()
                                                                 : JsonUtils.reader(adminAttachment, ArrayNode.class);
      if(arrayNode.isEmpty()){
        return defaultValue == null ? arrayNode : defaultValue.get();
      }
      return persistent(arrayNode, false, "");
    } catch (Exception e) {
      if(throwEx){
        throw new IllegalArgumentException("附件解析出错", e);
      }else{
        logger.error("附件解析出错", e);
        return defaultValue == null ? JsonUtils.createArrayNode() : defaultValue.get();
      }
    }
  }

  public ArrayNode persistent(ArrayNode arr) {
    return persistent(arr, false, "");
  }
  public ArrayNode persistent(ArrayNode arr, boolean notCheck, String userId) {
    if (arr == null || arr.isEmpty()) {
      return JsonUtils.createArrayNode();
    }
    // 取出已经持久化的
    ArrayNode temp = JsonUtils.createArrayNode();
    Iterator<JsonNode> iterator = arr.iterator();
    String redirectUrl = OssConstant.getRedirectUrl();
    while (iterator.hasNext()) {
      JsonNode next = iterator.next();
      String attaUrl = next.path("attaUrl").textValue();
      if (attaUrl != null) {
        if (attaUrl.startsWith(redirectUrl)) {
          Map<String, String> map = OssTool.queryStrToMap(attaUrl.substring(attaUrl.indexOf("?") + 1));
          attaUrl = map.get("file");
          ObjectNode node = JsonUtils.createObjectNode().put("url", attaUrl)
                                    .put("name", next.get("attaName").textValue());
          String duration = next.path("duration").textValue();
          if(StringUtils.isNotBlank(duration)){
            node.put("duration", duration);
          }
          temp.add(node);
          iterator.remove();
        }
      }
    }

    Map<String, String> map = Maps.newHashMap();
    map.put("userId", userId);
    //不检查字段 attUrl 或 url
    if (notCheck) {
      map.put("jsonArray", arr.toString());
    } else {
      map.put("jsonArray", getPersistentArrayNode(arr).toString());
    }
    ArrayNode persistent = ossServiceClient.persistent(map);
    temp.addAll(persistent);
    Assert.isTrue(temp.size() > 0, "持久化附件失败，请联系管理员");
    return temp;
  }

  /**
   * 上传临时文件
   *
   * @param file
   *
   * @return
   */
  public ObjectNode uploadAtta(MultipartFile file, boolean isPrivate) {
    String userId = "admin";
    Assert.notNull(file, "上传文件为空");
    //    Assert.isTrue(file != null && file.length > 0, "上传文件为空");
    Long fileSize = file.getSize();
    Assert.isTrue(fileSize <= 200 * 1024 * 1024, "上传文件太大");
    String fileName = file.getOriginalFilename();
    // 上传至云服务器(临时文件名)
    FileDTO fileDTO = new FileDTO();
    fileDTO.setFileName(fileName == null ? null : fileName.replaceAll("[&=?]", "_"));
    fileDTO.setUserId(userId);
    fileDTO.setTempFile(Boolean.TRUE);
    fileDTO.setPrivateFile(isPrivate);
    try {
      fileDTO.setUploadBytes(file.getBytes());
    } catch (IOException e) {
      Assert.isTrue(false, "上传文件出现异常");
    }
    ObjectNode objectNode = ossServiceClient.serverUploadFile(fileDTO);
    objectNode.put("size", Double.valueOf(fileSize) / 1000);
    return objectNode;
  }


  /**
   * 持久化ueditor里面的视频
   *
   * @param oldContent
   * @param newContent
   *
   * @return
   */
//  public void dealUeditorVedio(String oldContent, String newContent) {
//    Document oldDoc = Jsoup.parseBodyFragment(oldContent);
//    Elements oldVideos = oldDoc.select("iframe");
//    ArrayNode oldArray = JsonUtils.mapper.createArrayNode();
//    for (Element video : oldVideos) {
//      ObjectNode objectNode = JsonUtils.mapper.createObjectNode();
//      objectNode.put("name", video.attr("alt"));
//      objectNode.put("url", video.attr("src"));
//      if (!video.attr("src").contains("http://") && !video.attr("src").contains("https://")) {
//        objectNode.put("third", false);
//      } else {
//        objectNode.put("third", true);
//      }
//      oldArray.add(objectNode);
//    }
//
//    Document newDoc = Jsoup.parseBodyFragment(newContent);
//    Elements newVideos = newDoc.select("iframe");
//    ArrayNode newArray = JsonUtils.mapper.createArrayNode();
//    List<String> newArrayCoverUrl = Lists.newArrayList();
//    for (Element video : newVideos) {
//      ObjectNode objectNode = JsonUtils.mapper.createObjectNode();
//      objectNode.put("name", video.attr("alt"));
//      objectNode.put("url", video.attr("src"));
//      if (!video.attr("src").contains("http://") && !video.attr("src").contains("https://")) {
//        objectNode.put("third", false);
//      } else {
//        objectNode.put("third", true);
//      }
//      newArray.add(objectNode);
//      newArrayCoverUrl.add("");
//    }
//    aliVodService.updatePersistentAndDeleteNoDuration(oldArray.toString(), newArray.toString(),
//                                                      newArrayCoverUrl.toArray(new String[newArrayCoverUrl.size()])
//    );
//  }

  /**
   * 持久化ueditor里面的图片
   *
   * @param userId
   * @param content
   *
   * @return
   */
  public String dealUeditorImg(String userId, String content) {
    List<Attribute> oldImg = Lists.newArrayList();
    List<Attribute> ignoreImg = Lists.newArrayList();
    ArrayNode arrayNode = JsonUtils.mapper.createArrayNode();
    Document doc = JsoupHelper.clean(content, (tagName, attr) -> {
      if ("img".equals(tagName) && attr != null && "src".equals(attr.getKey()) && attr.getValue() != null) {
        String url = null;
        try {
          url = URLDecoder.decode(attr.getValue(), "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        if(StringUtils.isNotBlank(url)){
          if (url.contains("/temp")) {
            oldImg.add(attr);
            if(url.contains("file=")){
              url = StringUtils.substringAfter(url, "file=");
            }
            arrayNode.add(JsonUtils.createObjectNode().put("url", url));
          } else {
            ignoreImg.add(attr);
          }
        }
      }
    });
    if (arrayNode.size() > 0) {
      Map<String, String> paramMap = new HashMap<>(2);
      paramMap.put("userId", userId);
      paramMap.put("jsonArray", arrayNode.toString());
      ArrayNode array = ossServiceClient.persistent(paramMap);
      for (int i = 0; i < oldImg.size(); i++) {
        Attribute attr = oldImg.get(i);
        attr.setValue(array.get(i).get("url").asText());
      }
    }
    for (Attribute attr : ignoreImg) {
      String src = attr.getValue();
      if (src != null && src.contains("file=")) {
        src = StringUtils.substringAfter(src, "file=");
        attr.setValue(OssTool.deepDecoding(src));
      }
    }
    return compressHtml(doc.body().html());
  }

  /**
   * 获取持久化对象时所需的arraynode
   *
   * @param arr 后台附件传入的对象
   *
   * @return
   */
  public ArrayNode getPersistentArrayNode(ArrayNode arr) {
    ArrayNode result = JsonUtils.mapper.createArrayNode();
    for (JsonNode jsonNode : arr) {
      String url = jsonNode.path("attaUrl").textValue();
      String nameKey = "attaName";
      if (url == null) {
        url = jsonNode.path("url").textValue();
        nameKey = "name";
      }
      if (url != null) {
        ObjectNode node = JsonUtils.mapper.createObjectNode().put("url", url)
                                          .put("name", jsonNode.path(nameKey).textValue());
        String duration = jsonNode.path("duration").textValue();
        if (StringUtils.isNotBlank(duration)) {
          node.put("duration", duration);
        }
        result.add(node);
      }
    }
    return result;
  }

  /**
   * 展示从数据库查询出来的ueditor图片
   *
   * @param content
   *
   * @return
   */
  public String displayUeditorImg(String content) {
    return displayUeditorImg(content, OssConstant.getRedirectUrl());
  }

  public String displayUeditorImgWithHost(String content) {
    return displayUeditorImg(content, hostApi + OssConstant.getRedirectUrl());
  }

  private String displayUeditorImg(String content, String apiPath) {
    Document doc = JsoupHelper.clean(content, (tagName, attr) -> {
      if ("img".equals(tagName) && attr != null && "src".equals(attr.getKey()) && StringUtils.isNotBlank(attr.getValue())) {
        String url = attr.getValue();
        if (url.startsWith("/")) {
          try {
            attr.setValue(apiPath + "?file=" + URLEncoder.encode(url, "UTF-8"));
          } catch (Exception ignore) {
          }
        }
      }
    });
    return compressHtml(doc.body().html());
  }


  /**
   * 展示数据库查询出来的附件
   *
   * @param url
   *
   * @return
   */
  public String displayAtta(String url) {
    if (url == null) {
      return null;
    }
    try {
      url = URLEncoder.encode(url, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new BusinessException("url编码错误");
    }
    url = OssConstant.getRedirectUrl() + "?file=" + url;
    return url;
  }



  public String redirectInternals(Map<String, String> params) {
    String file = params.get("file");
    if (StringUtils.isNotBlank(file) && file.startsWith("/pub")) {
      return ossRedirectClient.redirectInternals(params);
    }
    if (AuthUtilHelper.getAuth().checkRoleCode(AuthCode.PRIVATE_ATTACHMENT_AUTH.toString())) {
      return ossRedirectClient.redirectInternals(params);
    }
    return "";
  }

  public String persistentSingleFile(String data, boolean b, String userId) throws IOException {
    if (StringUtils.isNotBlank(data)) {
      ArrayNode nodes = JsonUtils.reader(data, ArrayNode.class);
      if (nodes.size() == 0) {
        return "";
      } else {
        if (data.contains("temp")) {
          ArrayNode persistent = persistent(nodes, b, userId);
          return persistent.get(0).get("url").asText();
        } else {
          return nodes.get(0).get("attaUrl").asText();
        }
      }
    }
    return "";
  }
  public static String compressHtml(String html){
    HtmlCompressor compressor = new HtmlCompressor();
    compressor.setEnabled(true);
    compressor.setCompressCss(true);
    compressor.setYuiJsPreserveAllSemiColons(true);
    compressor.setYuiJsLineBreak(1);
    compressor.setPreserveLineBreaks(false);
    compressor.setRemoveIntertagSpaces(true);
    compressor.setRemoveComments(true);
    compressor.setRemoveMultiSpaces(true);
    //StringBuffer htmlBuf = new StringBuffer();
    //readFileToBuffer(html, htmlBuf);
    return compressor.compress(html);
  }

  public static class CommonsMultipartFile implements MultipartFile, Serializable {

    protected static final Log logger = LogFactory.getLog(CommonsMultipartFile.class);

    private final FileItem fileItem;

    private final long size;

    private boolean preserveFilename = false;


    /**
     * Create an instance wrapping the given FileItem.
     * @param fileItem the FileItem to wrap
     */
    public CommonsMultipartFile(FileItem fileItem) {
      this.fileItem = fileItem;
      this.size = this.fileItem.getSize();
    }


    /**
     * Return the underlying {@code org.apache.commons.fileupload.FileItem}
     * instance. There is hardly any need to access this.
     */
    public final FileItem getFileItem() {
      return this.fileItem;
    }

    /**
     * Set whether to preserve the filename as sent by the client, not stripping off
     * path information in {@link CommonsMultipartFile#getOriginalFilename()}.
     * <p>Default is "false", stripping off path information that may prefix the
     * actual filename e.g. from Opera. Switch this to "true" for preserving the
     * client-specified filename as-is, including potential path separators.
     * @see #getOriginalFilename()
     */
    public void setPreserveFilename(boolean preserveFilename) {
      this.preserveFilename = preserveFilename;
    }


    @Override
    public String getName() {
      return this.fileItem.getFieldName();
    }

    @Override
    public String getOriginalFilename() {
      String filename = this.fileItem.getName();
      if (filename == null) {
        // Should never happen.
        return "";
      }
      if (this.preserveFilename) {
        // Do not try to strip off a path...
        return filename;
      }

      // Check for Unix-style path
      int unixSep = filename.lastIndexOf('/');
      // Check for Windows-style path
      int winSep = filename.lastIndexOf('\\');
      // Cut off at latest possible point
      int pos = Math.max(winSep, unixSep);
      if (pos != -1)  {
        // Any sort of path separator found...
        return filename.substring(pos + 1);
      }
      else {
        // A plain name
        return filename;
      }
    }

    @Override
    public String getContentType() {
      return this.fileItem.getContentType();
    }

    @Override
    public boolean isEmpty() {
      return (this.size == 0);
    }

    @Override
    public long getSize() {
      return this.size;
    }

    @Override
    public byte[] getBytes() {
      if (!isAvailable()) {
        throw new IllegalStateException("File has been moved - cannot be read again");
      }
      byte[] bytes = this.fileItem.get();
      return (bytes != null ? bytes : new byte[0]);
    }

    @Override
    public InputStream getInputStream() throws IOException {
      if (!isAvailable()) {
        throw new IllegalStateException("File has been moved - cannot be read again");
      }
      InputStream inputStream = this.fileItem.getInputStream();
      return (inputStream != null ? inputStream : StreamUtils.emptyInput());
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
      if (!isAvailable()) {
        throw new IllegalStateException("File has already been moved - cannot be transferred again");
      }

      if (dest.exists() && !dest.delete()) {
        throw new IOException(
          "Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
      }

      try {
        this.fileItem.write(dest);
        LogFormatUtils.traceDebug(logger, traceOn -> {
          String action = "transferred";
          if (!this.fileItem.isInMemory()) {
            action = (isAvailable() ? "copied" : "moved");
          }
          return "Part '" + getName() + "',  filename '" + getOriginalFilename() + "'" +
            (traceOn ? ", stored " + getStorageDescription() : "") +
            ": " + action + " to [" + dest.getAbsolutePath() + "]";
        });
      }
      catch (FileUploadException ex) {
        throw new IllegalStateException(ex.getMessage(), ex);
      }
      catch (IllegalStateException | IOException ex) {
        // Pass through IllegalStateException when coming from FileItem directly,
        // or propagate an exception from I/O operations within FileItem.write
        throw ex;
      }
      catch (Exception ex) {
        throw new IOException("File transfer failed", ex);
      }
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
      if (!isAvailable()) {
        throw new IllegalStateException("File has already been moved - cannot be transferred again");
      }

      FileCopyUtils.copy(this.fileItem.getInputStream(), Files.newOutputStream(dest));
    }

    /**
     * Determine whether the multipart content is still available.
     * If a temporary file has been moved, the content is no longer available.
     */
    protected boolean isAvailable() {
      // If in memory, it's available.
      if (this.fileItem.isInMemory()) {
        return true;
      }
      // Check actual existence of temporary file.
      if (this.fileItem instanceof DiskFileItem) {
        return ((DiskFileItem) this.fileItem).getStoreLocation().exists();
      }
      // Check whether current file size is different than original one.
      return (this.fileItem.getSize() == this.size);
    }

    /**
     * Return a description for the storage location of the multipart content.
     * Tries to be as specific as possible: mentions the file location in case
     * of a temporary file.
     */
    public String getStorageDescription() {
      if (this.fileItem.isInMemory()) {
        return "in memory";
      }
      else if (this.fileItem instanceof DiskFileItem) {
        return "at [" + ((DiskFileItem) this.fileItem).getStoreLocation().getAbsolutePath() + "]";
      }
      else {
        return "on disk";
      }
    }

    @Override
    public String toString() {
      return "MultipartFile[field=\"" + this.fileItem.getFieldName() + "\"" +
        (this.fileItem.getName() != null ? ", filename=" + this.fileItem.getName() : "" ) +
        (this.fileItem.getContentType() != null ? ", contentType=" + this.fileItem.getContentType() : "") +
        ", size=" + this.fileItem.getSize() + "]";
    }
  }
}
