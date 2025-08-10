package com.wk.vpac.common.service.service.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

/**
 * HtmlCleaner
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version v1.0.0
 * @date 2020-08-24 9:48
 */
public abstract class JsoupHelper {

  public static Document clean(String html, ElementConsumer elementConsumer) {
    Document dirty = Jsoup.parseBodyFragment(html, "http:");
    Cleaner cleaner = new Cleaner(elementConsumer == null
                                  ? CustomWhitelist.EMPTY_ELEMENT_CONSUMER
                                  : new CustomWhitelist(elementConsumer));
    return cleaner.clean(dirty);
  }


  public interface ElementConsumer{
    void acceptAttr(String tagName, Attribute attr);
  }

  public static class CustomWhitelist extends Safelist {
    private static final CustomWhitelist EMPTY_ELEMENT_CONSUMER = new CustomWhitelist(null);

    private ElementConsumer elementConsumer;

    public CustomWhitelist(ElementConsumer elementConsumer) {
      this.elementConsumer = elementConsumer;
    }

    @Override
    public boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
      boolean safe = super.isSafeAttribute(tagName, el, attr);
      if(safe && elementConsumer != null){
        elementConsumer.acceptAttr(tagName, attr);
      }
      return safe;
    }

    {
      addTags(
      "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
      "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
      "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong",
      "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
      "ul")

      .addAttributes("a", "href", "title","data-miniprogram-path","data-miniprogram-appid")
      .addAttributes("blockquote", "cite")
      .addAttributes("col", "span", "width")
      .addAttributes("colgroup", "span", "width")
      .addAttributes("img", "align", "alt", "height", "src", "title", "width")
      .addAttributes("ol", "start", "type")
      .addAttributes("q", "cite")
      .addAttributes("table", "summary", "width")
      .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
      .addAttributes(
        "th", "abbr", "axis", "colspan", "rowspan", "scope",
        "width")
      .addAttributes("ul", "type")

      .addProtocols("a", "href", "ftp", "http", "https", "mailto", "mini", "tel")
      .addProtocols("blockquote", "cite", "http", "https")
      .addProtocols("cite", "cite", "http", "https")
      .addProtocols("img", "src", "http", "https")
      .addProtocols("q", "cite", "http", "https")

      //自定义
      .addTags("section", "hr", "iframe")
      .addProtocols("iframe", "src", "http", "https")
      .addAttributes("iframe", "src", "height", "width", "frameborder", "scrolling", "seamless", "allowfullscreen")
      .addAttributes(":all", "style", "id","class")
      .preserveRelativeLinks(true)
      ;
    }
  }
}
