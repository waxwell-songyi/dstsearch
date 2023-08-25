package com.songyi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.songyi.entity.ServerList;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class KleiApiService {

    @Value("${myrobot.token}")
    private String token;

    /**
     * 根据区域和平台查询目前所有服务器信息的方法
     *
     * @param region   区域枚举
     * @param platform 平台枚举
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<ServerList> searchServerList(String region, String platform) throws IOException, URISyntaxException {
        final String url = "https://lobby-v2-cdn.klei.com/" + region + "-" + platform + ".json.gz";
        URIBuilder builder = new URIBuilder(url);
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000)
                .build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        //转换查询到的JSON成对象
        if (StringUtils.isBlank(resultString)){
            return new ArrayList<>();
        }
        JSONObject jsonObject = JSONObject.parseObject(resultString);
        if (jsonObject.isEmpty()){
            return new ArrayList<>();
        }
        JSONArray get = jsonObject.getJSONArray("GET");
        return get.toJavaList(ServerList.class);
    }

    /**
     * 根据所在区服和rowId进行查询某一个具体服务器信息的方法
     *
     * @param region
     * @param rowId
     * @return
     * @throws IOException
     */
    public ServerList detailMessage(String region, String rowId) {
        String json = "{\n" +
                "\t\"__token\":\"" + token + "\",\n" +
                "\t\"__gameId\":\"DST\",\n" +
                "\t\"query\":{\n" +
                "\t\t\"__rowId\":\"" + rowId + "\"\n" +
                "\t}\n" +
                "}";
        final String url = "https://lobby-v2-" + region + ".klei.com/lobby/read";
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        //设置请求体
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(entity);
        //设置请求头信息
        httpPost.setHeader("Content-Type", "application/json");
        //执行请求
        ServerList serverList = null;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            //获取响应结果
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            if (200 == response.getStatusLine().getStatusCode()) {
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                JSONArray result = jsonObject.getJSONArray("GET");
                String last = JSON.toJSONString(result.get(0));
                serverList = JSON.parseObject(last, ServerList.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return serverList;
        }
        return serverList;
    }
}
