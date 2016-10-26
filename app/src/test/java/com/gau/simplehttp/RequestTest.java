package com.gau.simplehttp;

import com.gau.simplehttp.http.HttpMethod;
import com.gau.simplehttp.http.Request;
import com.gau.simplehttp.http.Response;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class RequestTest {

//    private static Request.Builder builder;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        builder = new Request.Builder()
//                .url("http://10.0.3.2:8080/TestProject/TestServlet")
//                .method(HttpMethod.POST)
//                .headers("User-Agent", "guochao-1.0")
//                .params("param", "hello world")
//                .string("{\"string\":\"string\"}");
//    }

    @Test
    @PrepareForTest(Response.class)
    public void testSyncRequest() throws Exception {
//        Request request = Request.newRequest(builder);
//        Request request = PowerMockito.mock(Request.class);
//        Response response = request.execute();
        Response response = PowerMockito.mock(Response.class);
        Response.Builder builder = new Response.Builder();
        PowerMockito.whenNew(Response.class).withArguments(builder).thenReturn(response);

//        Assert.assertTrue(response.isSuccessful());
//        Assert.assertEquals(200, response.getCode());
//        Assert.assertEquals("OK", response.getMessage());
    }

    @Test
    public void testASyncRequest() throws Exception {
    }

}
