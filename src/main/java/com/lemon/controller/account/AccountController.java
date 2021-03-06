package com.lemon.controller.account;

import com.lemon.controller.BaseController;
import com.lemon.domain.impl.Cookies;
import com.lemon.domain.impl.user.User;
import com.lemon.enums.SignupType;
import com.lemon.form.AjaxResponse;
import com.lemon.form.user.UserAccountForm;
import com.lemon.manager.account.AccountManager;
import com.lemon.pojo.constants.LemonConstants;
import com.lemon.service.ICookiesService;
import com.lemon.service.IUserService;
import com.lemon.utils.Md5;
import com.lemon.utils.SequenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by simpletour_java on 2015/6/3.
 */
@Controller
public class AccountController extends BaseController{

    //// TODO: 2016/8/8 0008 需要写一个拦截器，完成用户的自动登录

    @Resource //  但是@Autowired 是spring提供的，@Resource是j2ee提供的
    private IUserService userService;

    @Resource
    private ICookiesService cookiesService;

    @Resource
    private AccountManager accountManager;

    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "/lemon/account/register")  //默认为GET
    public String register(){
        return "/lemon/account/register";
    }


    /**
     * 手机号注册
     * @param userForm
     * @param result
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/lemon/account/register",method = RequestMethod.POST)
    public AjaxResponse register(@Valid @RequestBody UserAccountForm userForm, BindingResult result, HttpServletRequest request, HttpServletResponse response){
        if (result.hasErrors()){
            return AjaxResponse.fail().msg("注册失败").reason("用户没有提交数据");
        }

        // 使用手机号注册的时候会生成两个账号
        Optional<User> userOptional = userService.findUserByAccount(userForm.getMobile());
        if (userOptional.isPresent()) {
            return AjaxResponse.fail().msg("注册失败").reason("该手机号已经注册过了");
        }

        String salt = SequenceUtils.generateAlphaNun(5); //生成salt
        String password = Md5.messageDigest(userForm.getPassword() + salt);
        // 系统默认的账号
        String account = SequenceUtils.generateAlpha(4) + userForm.getMobile();
        // 昵称默认是 account
        User user = new User(account, account, password, salt, SignupType.MOBILE);

        Optional<User> newUser = userService.createUser(user, userForm.getMobile());
        if (!newUser.isPresent()){
            return AjaxResponse.fail().msg("注册失败").reason("网络异常请稍后再试");
        }

        HttpSession session = request.getSession();
        cookiesService.insertCookies(accountManager.createCookies(session,response,newUser.get()));//登陆成功后就会抛出用户ID和用户名

//        session.setAttribute("user.nickname", user.getNickName());
        session.setAttribute(LemonConstants.USER_SEESSION_ACCOUNT, user.getAccount());
        session.setAttribute(LemonConstants.USER_SEESSION_ID, user.getId());

        // 跳转首页（朋友圈）
        return AjaxResponse.ok().url("/lemon/lemons/friends");
    }


    /**
     * 登录页面
     * @return
     */
    @RequestMapping(value = "/lemon/account/login")  //默认是GET方法
    public String login(){
        return "lemon/account/login";
    }

    /**
     * 登录页面
     * @return
     */
//    @RequestMapping(value = "/lemon/account/qq/login")  //默认是GET方法
    public String qqLogin(){
        return "lemon/account/qqlogin";
    }

    /**
     * 登录
     * @param userForm
     * @param result
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/lemon/account/login",method = RequestMethod.POST)
    public AjaxResponse login(@Valid @RequestBody UserAccountForm userForm,BindingResult result, HttpServletRequest request,HttpServletResponse response) {
        if (result.hasErrors()) {
            return AjaxResponse.fail().msg("登录失败").reason("用户没有提交任何数据");
        }

        // 手机号和默认的系统账号都可以登录，使用qq登录之后也可以利用qq号登录
        Optional<User> userOptional = userService.findUserByAccount(userForm.getMobile());
        if (!userOptional.isPresent()){
            return AjaxResponse.fail().msg("登录失败").reason("用户不存在");
        }

        // 账号是否还是可用的状态
        if (!Boolean.TRUE.toString().equals(userOptional.get().getStatus())){
            return AjaxResponse.fail().msg("登录失败").reason("用户账号不可用,请与管理员联系");
        }

        User user = userOptional.get();

        String password = Md5.messageDigest(userForm.getPassword() + user.getSalt());
        if (!user.getPassword().equals(password)){
            return AjaxResponse.fail().msg("登录失败").reason("用户名或者密码错误");
        }

        HttpSession session = request.getSession();
        Cookies cookies = accountManager.createCookies(session, response, user);
        cookiesService.updateCookies(cookies);

//        session.setAttribute("user.nickname", user.getNickName());
        session.setAttribute(LemonConstants.USER_SEESSION_ACCOUNT, user.getAccount());
        session.setAttribute(LemonConstants.USER_SEESSION_ID, user.getId());

        //首页（朋友圈）
        return AjaxResponse.ok().url("/lemon/lemons/friends");

    }

}
