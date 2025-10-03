1.导包

```pom
<dependency>
<groupId>org.apache.httpcomponents</groupId>
<artifactId>httpclient</artifactId>
<version>4.5.13</version>
</dependency>
```





使用方法



```java
@Test
    public void testGet() throws IOException {
        //创建http client 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        //发送请求，获取响应
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        //状态码
        int statusCode = execute.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        //响应体
        HttpEntity entity = execute.getEntity();
        //解析响应体
        String body = EntityUtils.toString(entity);
        System.out.println(body);
        //关闭资源
        execute.close();
        httpClient.close();
    }

    @Test
    public void testPost() throws Exception {
        //创建http client 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");
        StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
        //数据格式确定
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        httpPost.setEntity(stringEntity);

        //发送请求，获取响应
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        //解析返回结果
        int statusCode = execute.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        HttpEntity entity = execute.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println(body);
        //关闭资源
        execute.close();
        httpClient.close();


    }
```

