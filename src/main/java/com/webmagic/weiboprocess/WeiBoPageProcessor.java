package com.webmagic.weiboprocess;

import com.webmagic.piplineself.WeiBoMoGoPipline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class WeiBoPageProcessor implements PageProcessor{
    //正文的判断正则
    public static final String TEXTBODY="https://m\\.weibo\\.cn/api/container/getIndex\\?containerid=107603\\w+\\&page=\\d+";
    private static final String query="1345566427";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    @Override
    public void process(Page page) {
        //判断url是正文还是好友列表
        if(page.getUrl().regex(TEXTBODY).match()){
            Json json=page.getJson();
            String ok=json.jsonPath("$.ok").toString();
            if ("1".equals(ok)){
//                Selectable cardlistInfo = json.jsonPath("$.data.cardlistInfo");
                String containerid = json.jsonPath("$.data.cardlistInfo.containerid").get();
                String fid=containerid.substring(6);
                String nextpage = json.jsonPath("$.data.cardlistInfo.page").get();
                if(nextpage!=null){
                    page.addTargetRequest("https://m.weibo.cn/api/container/getIndex?containerid=" + containerid + "&page="+nextpage);
                }
                //获得当前页所有微博正文
                Selectable selectable =json.jsonPath("$.data.cards[?(@.card_type==9)]");
                List<String> all = selectable.all();
                page.putField(fid,all);
//                page.putField("mblogs",all);
                if("2".equals(nextpage)){
                    page.addTargetRequest("https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_" + fid + "&luicode=10000011&lfid=107603" + fid + "&page=1");
                }
            }
        }else {
            Json json=page.getJson();
            String ok=json.jsonPath("$.ok").toString();
            if ("1".equals(ok)){
                String containerid = json.jsonPath("$.data.cardlistInfo.containerid").get();
                String fid=containerid.substring(containerid.lastIndexOf("_")+1);
                String nextpage = json.jsonPath("$.data.cardlistInfo.page").get();
                //下页好友
                page.addTargetRequest("https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_" + fid + "&luicode=10000011&lfid=107603" + fid + "&page="+nextpage);
                //获取单页所有好友id
                List<String> ids= json.jsonPath("$.data.cards[-1:].card_group[*].user.id").all();
//                Selectable selectable = json.jsonPath("$.data.cards[?(@.card_type==11)].card_group[@.card_type==10]");
                //所有好友的微博正文页
                for (String id:ids
                     ) {
                    page.addTargetRequest("https://m.weibo.cn/api/container/getIndex?containerid=107603" + id + "&page=1");

                }
            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from
                (new Proxy("211.159.166.225",3784),new Proxy("118.24.76.45",54854),
                        new Proxy("140.143.87.241",33854)));
        String url="https://m.weibo.cn/api/container/getIndex?containerid=107603" + query + "&page=" + 1;
        Spider.create(new WeiBoPageProcessor()).setScheduler(new FileCacheQueueScheduler("D:\\mymagic\\src\\main\\resources\\static"))
                .addPipeline(new WeiBoMoGoPipline()).setDownloader(httpClientDownloader).addUrl(url).thread(5).run();
    }

}
