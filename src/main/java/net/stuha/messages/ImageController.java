package net.stuha.messages;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public void add(HttpServletRequest request) throws IOException {
        InputStream is = request.getInputStream();

    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public Image find(@PathVariable String id) throws ImageNotFoundException, InterruptedException {
        Thread.sleep(1000);

        return imageService.find(id);
    }

    @RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    public Image thumbnail(@PathVariable String id) throws ImageNotFoundException, InterruptedException {
        Thread.sleep(1000);

        return imageService.thumbnail(id);
    }
}
