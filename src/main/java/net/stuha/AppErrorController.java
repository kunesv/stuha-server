package net.stuha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AppErrorController extends AbstractErrorController {

    public static final String PATH = "/error";

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = PATH)
    public @ResponseBody
    Map<String, Object> error(HttpServletRequest request) {
        return getErrorAttributes(request, false);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
