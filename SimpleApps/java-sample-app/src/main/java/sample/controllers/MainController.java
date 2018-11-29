package sample.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sample.biz.UserService;

@Controller
public class MainController {

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping("/")
	public String index(HttpSession session) throws Exception {
		// return "redirect:/login";

		Map map = new HashMap();

		if (session.getAttribute("idKey") == null) {
			session.invalidate();
			return "redirect:/login";

		}
		String idKey = session.getAttribute("idKey").toString();
		String passwordKey = session.getAttribute("passwordKey").toString();

		map.put("idKey", idKey);
		map.put("passwordKey", passwordKey);

		Map resultMap = userService.getRedisValue(map);

		if (!resultMap.containsKey("id"))
			return "redirect:/login";
		if (!resultMap.containsKey("password"))
			return "redirect:/login";

		return "manage";
	}

}
