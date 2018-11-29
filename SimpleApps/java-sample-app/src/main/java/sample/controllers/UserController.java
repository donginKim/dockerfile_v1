package sample.controllers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sample.biz.UserService;

/**
 * 
 * <pre>
 * &#64;desc      : 관리(/manage) API : login, logout
 * </pre>
 *
 * @package : sample.app.web.controller
 * @file_name : UserController.java
 * @date : 2015. 11. 3. 오후 2:32:28
 * @version :
 * @author : openPaaS
 */
@Controller
@RequestMapping("/manage")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    /**
     * <pre>
     * desc : 로그인 - 사용자 아이디 및 패스워드를 통해 login
     * </pre>
     * 
     * @Method Name : login
     * @date : 2015. 10. 13.
     * @author : openPaaS
     * 
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(//@RequestParam Map<String, Object> map, 
                    HttpServletRequest request, @RequestBody Map<String, Object>  body) throws Exception {
        if (!body.containsKey("id"))
            throw new IllegalArgumentException("no id founded!!!");
        if (!body.containsKey("password"))
            throw new IllegalArgumentException("no password founded!!!");

        Map returnMap = userService.login(body);
        
        if(!returnMap.containsKey("idKey")){

            request.getSession().invalidate();
            
        }else{
            request.getSession(true);
            request.getSession().setAttribute("id", body.get("id"));
            request.getSession().setAttribute("password", body.get("password"));
    
            request.getSession().setAttribute("idKey", returnMap.get("idKey"));
            request.getSession().setAttribute("passwordKey", returnMap.get("passwordKey"));
        }
        
        return returnMap;
    }

    /**
     * <pre>
     * desc : 로그인 - 사용자 아이디 및 패스워드를 통해 login
     * </pre>
     * 
     * @Method Name : login
     * @date : 2015. 10. 13.
     * @author : openPaaS
     * 
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getSession(HttpSession session) throws Exception {
        Map map = new HashMap();
        
        if(session.getAttribute("idKey") == null){
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

    /**
     * <pre>
     * desc : 로그아웃
     * </pre>
     * 
     * @Method Name : logout
     * @date : 2015. 11. 11.
     * @author : openPaaS
     * 
     * @param token
     * @param map
     * @return
     */
    @RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logout(HttpSession session){
		
		Map<String, Object> responseMap = new HashMap<String, Object>();

		responseMap.put("status", "200 OK");
		responseMap.put("error", "no error");
		
		session.removeAttribute("idKey");
		session.removeAttribute("passwordKey");
		session.invalidate();
	
		return responseMap;
	}
    
//  @RequestMapping(value = "/logout", method = RequestMethod.POST)
//  public Object logout(@RequestParam Map<String, Object> map) {
//
//      return userService.logout(map);
//  }
//}
}

