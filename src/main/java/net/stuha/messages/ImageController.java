package net.stuha.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public Image find(@PathVariable String id) throws FileNotFoundException {
        return imageService.find(id);
    }
}
