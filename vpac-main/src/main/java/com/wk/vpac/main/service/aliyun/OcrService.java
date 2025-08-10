package com.wk.vpac.main.service.aliyun;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeTableOcrRequest;
import com.aliyun.ocr_api20210707.models.RecognizeTableOcrResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.base.components.oss.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * OcrService
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2024-04-26 14:03
 */
@Service
public class OcrService {
  @Value("${base.ocr.aliyun.accessKeyId}")
  private String accessKeyId;

  @Value("${base.ocr.aliyun.accessKeySecret}")
  private String accessKeySecret;
  private final OssService ossService;

  private Client client;

  public OcrService(OssService ossService) {
    this.ossService = ossService;
  }

  public Client getClient() {
    if(client == null){
      client = createClient();
    }
    return client;
  }

  private Client createClient() {
    // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
    // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
    Config config = new Config()
      // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
      .setAccessKeyId(accessKeyId)
      // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
      .setAccessKeySecret(accessKeySecret);
    // Endpoint 请参考 https://api.aliyun.com/product/ocr-api
    config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
    try {
      return new Client(config);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }


  public String readExcelImage(String imageUrl)  {
    String url = ossService.getDownloadToken(null, imageUrl, false);
    try{
      return readExcelImage(new BufferedInputStream(URI.create(url).toURL().openConnection().getInputStream()));
    } catch (Exception e){
      throw new IllegalArgumentException("图片访问异常", e);
    }
  }

  public String readExcelImage(InputStream inputStream) {
    try (inputStream){
      Client client = getClient();
      RecognizeTableOcrRequest recognizeTableOcrRequest = new RecognizeTableOcrRequest();
      recognizeTableOcrRequest.setBody(inputStream);
      RuntimeOptions runtime = new RuntimeOptions();
      RecognizeTableOcrResponse response = client.recognizeTableOcrWithOptions(
        recognizeTableOcrRequest, runtime);
      return response.getBody().getData();
    } catch (TeaException e){
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
