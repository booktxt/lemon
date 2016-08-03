package com.lemon.controller.account;

import com.lemon.domain.User;
import com.lemon.form.AjaxResponse;
import com.lemon.form.user.UserAccountForm;
import com.lemon.query.user.UserQuery;
import com.lemon.service.IUserService;
import com.lemon.utils.Md5;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by simpletour_java on 2015/6/3.
 */
@Controller
public class RegisterController {

    @Resource //  但是@Autowired 是spring提供的，@Resource是j2ee提供的
    private IUserService userService;
//
//    @Resource
//    private ICookiesService cookiesService;

    @RequestMapping(value = "/account/register")  //默认为GET
    public String register(){
        return "lemon/account/register";
    }


    @ResponseBody
    @RequestMapping(value = "/account/register",method = RequestMethod.POST)
    public AjaxResponse register(@Valid UserAccountForm userForm, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()){
            return AjaxResponse.fail().msg("注册失败").reason("用户没有提交数据");
        }

        Optional<User> userOptional = userService.findOne(new UserQuery(userForm.getMobile()));
        if (userOptional.isPresent()) {
            return AjaxResponse.fail().msg("注册失败").reason("该手机号已经注册过了");
        }

        int salt = (int)(((Math.random()*100+1)*1000)-1); //生成salt 999--99999
        String password = Md5.messageDigest(userForm.getPassword() + salt);
        User user = new User(userForm.getMobile(),userForm.getMobile(),userForm.getPassword(),salt+"");




        return AjaxResponse.fail();
    }

//    @RequestMapping(value = "/register",method = RequestMethod.POST)
//    public String register(User user,Model model,HttpServletResponse response,HttpServletRequest request){
////        System.out.println(user.getId() + " " + user.getName() + " " + user.getPassword());
////        System.out.println("register: "+user.getPassword());
//        if(user.getId()!=null && !user.getId().equals("")
//                && user.getName()!=null && !user.getName().equals("")
//                && user.getPassword()!=null && !user.getPassword().equals("")){
//            int count = userService.findName(user.getId());
//            if(count!=0){
//                model.addAttribute("message","此用户ID已经被注册！");
//                return "register";
//            }else{
//                int salt = (int)(((Math.random()*100+1)*1000)-1); //生成salt 999--99999
//                user.setSalt(salt);
//                String pwd = user.getPassword()+salt;   //组合
//                String password = Md5.messageDigest(pwd);  //组合之后再生成MD5摘要信息
//                user.setPassword(password);
//                userService.insert(user);
//                int cookieTime = 60*30;
//                HttpSession session = request.getSession();
//                String sessionId = session.getId();
//                Cookie cookie = new Cookie("JSESSIONID",sessionId);//注意key值必须和原来一样，否则服务器无法标识用户
//                Cookie cookie1 = new Cookie("userId",user.getId());
//                Cookie cookie2 = new Cookie("password",user.getPassword());
//                cookie2.setMaxAge(cookieTime);
//                cookie2.setPath("/");
//                cookie1.setMaxAge(cookieTime);
//                cookie1.setPath("/");
//                cookie.setMaxAge(cookieTime);// 设置cookie的生命周期1800s  现在是30s
//                cookie.setPath("/");  // 设置cookie有效路径，
//                response.addCookie(cookie2);
//                response.addCookie(cookie1);
//                response.addCookie(cookie);//cookie添加完毕
//                Cookies cookies = new Cookies(); //在数据库中储存每个id对应一个sessionId
//                cookies.setId(user.getId()); //设置用户ID
//                cookies.setSessionId(sessionId);   //实体里面有一个变量叫做sessionId
//                Date now = new Date();
//                Timestamp time = new Timestamp(now.getTime());
//                cookies.setLoginTime(time);   //设置登陆时间
//                cookies.setLifeTime(cookieTime);   //设置生命周期
////                System.out.println("register:" + user.getId() + "===" + sessionId);
//                ICookiesService.insertCookies(cookies);//登陆成功后就会抛出用户ID和用户名
//                session.setAttribute("userId", user.getId());
//                session.setAttribute("userName", user.getName());
////                System.out.println("register:" + sessionId);
//                model.addAttribute("message","注册成功！");
//                return "success";
//            }
//        }else{
//            model.addAttribute("message","所有的内容都需要正确的填写！");
//            return "register";
//        }
//
//    }
//
//    @RequestMapping(value = "/login")  //默认是GET方法
//    public String login(){
//        return "login";
//    }
//
//    @RequestMapping(value = "/login",method = RequestMethod.POST)
//    public String login(User user,Model model,HttpServletRequest request,HttpServletResponse response){
////        System.out.println("login: " + user.getPassword());
//        if(user.getId()!=null && !user.getId().equals("")
//                && user.getPassword()!= null && !user.getPassword().equals("")){
//            User userTemp = userService.findUserName(user.getId());
//            if(userTemp != null){
//                int salt = userTemp.getSalt();  //从数据库获取salt
//                String pwdTemp = user.getPassword();
//                String pwd = user.getPassword() + salt;  //组合
//                String password = Md5.messageDigest(pwd);  //生成MD5摘要
//                user.setPassword(password);
//                if(userTemp.getPassword().equals(password)){
//                    int cookieTime = 60*30; //生存的时间 以秒为单位
//                    HttpSession session = request.getSession();
//                    String sessionId = session.getId();
//                    Cookie cookie = new Cookie("JSESSIONID",sessionId);//注意key值必须和原来一样，否则服务器无法标识用户
//                    Cookie cookie1 = new Cookie("userId",user.getId());
//                    Cookie cookie2 = new Cookie("password",pwdTemp);
//                    cookie.setMaxAge(cookieTime);// 设置cookie的生命周期1800s
//                    cookie1.setMaxAge(cookieTime);
//                    cookie2.setMaxAge(cookieTime);
//                    cookie.setPath("/");  // 设置cookie有效路径，
//                    cookie1.setPath("/");
//                    cookie2.setPath("/");
//                    response.addCookie(cookie2);
//                    response.addCookie(cookie1);
//                    response.addCookie(cookie);//cookie添加完毕
//                    Cookies cookies = new Cookies(); //在数据库中储存每个id对应一个sessionId
//                    cookies.setId(user.getId());  //设置用户ＩＤ
//                    cookies.setSessionId(sessionId);   //实体里面有一个变量叫做sessionId
//                    Date now = new Date();
//                    Timestamp time = new Timestamp(now.getTime());
//                    cookies.setLoginTime(time);   //设置登陆时间
//                    cookies.setLifeTime(cookieTime);   //设置生命周期
////                    System.out.println("login:"+user.getId()+"==="+sessionId);
//                    ICookiesService.updateCookies(cookies);  //更新
////                    System.out.println("login:" + sessionId);
//                    session.setAttribute("userId",user.getId());
//                    session.setAttribute("userName", userTemp.getName());
//                    return "success";
//                }else{
//                    model.addAttribute("message","用户ID或者密码错误！");
//                    return "login";
//                }
//
//            }else{
//                model.addAttribute("message","此用户ID不存在！");
//                return "login";
//            }
//
//        }else{
//            model.addAttribute("message","必须所有的都填写！");
//            return "login";
//        }
//    }

}
