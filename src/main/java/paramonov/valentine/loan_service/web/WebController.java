package paramonov.valentine.loan_service.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {
    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public @ResponseBody String sayHi() {
        return "Hello";
    }
}
