package com.example.sjzs.model.bean.beanmanager

import android.text.TextUtils
import android.util.Log
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

class BeanManager {
    companion object {
        fun getArticleBean(text: String): ArticleBean {
            val document: Document = Jsoup.parse(text)
            //CONTENT中xml格式中一些字符需要转变
            val document1: Document = Jsoup.parse(
                document.toString()
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&ldquo;", "\"")
                    .replace("&rdquo;", "\"")
                    .replace("&lsquo;", "'")
                    .replace("&rsquo;", "'")
                    .replace("&hellip;", "...")
                    .replace("&mdash;", "-")
                    .replace("<br>", "\n")
                    .replace("<br/>", "")
                    .replace("<br></br>", "\n")

            );
            var videourl = ""
            //除了CONTENT的其他部分
            val title: String = document.select("TITLE").first().text().replace("<![CDATA[", "")
                .replace("]]>", "")
            val keyword: String = document.select("KEYWORD").first().text()
            val source: String = document.select("SOURCE").first().text()
            val time: String = document.select("DATE").first().text()
            val des: String = document.select("DESC").first().text()
            val editor: String = document.select("EDITOR").first().text()
            //video资源链接和List<Map>部分
            val elements: Elements = document1.select("p")
            val data: Node = Node()
            for (element in elements) {
                val img: Element? = element.select("p[style=\"text-align: center;\"]").first()
                if (img != null) {
                    if (img.select("img").first() != null) {
                        val img1: String = element.select("img").first().attr("src")
                        val widthStr = element.select("img").first().attr("width")
                        var width: Int? = null
                        if (!TextUtils.isEmpty(widthStr)) {
                            width = widthStr.toInt()
                        }
                        val imgNode = Img(img1, width)
                        data.setNexNode(imgNode)
                    } else if (element.text().contains("newPlayer")) {
                        Log.d("URL1", element.text())
                        videourl = Utils.videoUrl(element.text())

                    } else {
                        Log.d("URL14", "strong>" + img.text())
                        data.setNexNode(HeadText(img.text()))
                    }
                } else if (element.select("strong").first() != null) {
                    Log.d("URL2", "strong>" + text)
                    val strongNode = Strong(element.text())
                    data.setNexNode(strongNode)
                } else {
                    val text1: String = element.text()
                    Log.d("URL3", "TEXT_>" + text1)

                    val textNode = Text(text1)
                    data.setNexNode(textNode)

                }
            }
            return ArticleBean(title, keyword, source, time, des, videourl, editor, data)
        }

        /**
         * 获取通用的banner
         */
        fun getCommonBanners(html: String): List<CommomBanner> {
            val bannerBeanList: MutableList<CommomBanner> =
                ArrayList<CommomBanner>()
            val document = Jsoup.parse(
                html.replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&ldquo;", "\"")
                    .replace("&rdquo;", "\"")
                    .replace("&lsquo;", "'")
                    .replace("&rsquo;", "'")
                    .replace("&hellip;", "...")
                    .replace("&mdash;", "-")
                    .replace("<br>", "\n")
                    .replace("<br/>", "")
                    .replace("<br></br>", "\n")
            )

            val select = document.select("div.list_con").first()
            val selects = select.select("div.silde")

            for (element in selects) {
                var srcHref = ""
                //图片信息和连接信息
                val image = element.select("div.image").first()
                val a = image.select("a").first()
                val href = a.attr("abs:href")
                val img = a.select("img.lazy").first()
                //图片有两种Tag需要分辨
                srcHref = if (img.attr("data-echo") != null && img.attr("data-echo").length > 0) {
                    img.attr("data-echo")
                } else {
                    img.attr("data-src")
                }
                Log.d(1.toString(), srcHref)
                //标题和文本信息
                val right_text = element.select("div.right_text").first()
                val h3 =
                    right_text.select("h3").first().select("a").first()
                val title = h3.text()
                val p = right_text.select("p").first().select("a").first()
                val text = p.text()
                bannerBeanList.add(CommomBanner(href, srcHref, title, text))
            }

            return bannerBeanList
        }
    }
}