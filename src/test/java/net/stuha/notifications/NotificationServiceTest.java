package net.stuha.notifications;

import nl.martijndwars.webpush.PushService;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.junit.Test;

public class NotificationServiceTest {

    @Test
    public void test() throws Exception {
        String endpoint = "https://updates.push.services.mozilla.com/wpush/v1/gAAAAABZjkDYTn3Ctf1nMwJS2FtewvT-LVmvc35zyNsGlZyWEAfUo5mAC8P9Qzx-6bx9kfcpttW1FWJtpfVngmRWA0jFMsUzCSEwVwxvP2ITKCCpB7AqnonIkcubWqk132G-lz8Yerdn";

        PushService pushService = new PushService();


        Request request = Request.Post(endpoint);


        request
                .addHeader("TTL", "0")
        ;
        Response r = request.execute();


        System.out.println(r);
    }
}