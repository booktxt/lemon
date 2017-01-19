package com.lemon.controller.friendship;

import com.lemon.annotation.UserLoginValidation;
import com.lemon.controller.BaseController;
import com.lemon.domain.impl.friend.FriendGroup;
import com.lemon.domain.impl.friend.Friendship;
import com.lemon.domain.interfaces.friend.IFriendGroup;
import com.lemon.manager.friends.FriendsManager;
import com.lemon.manager.user.HeadUserInfoManager;
import com.lemon.service.IFriendGroupService;
import com.lemon.service.IFriendshipService;
import com.lemon.view.friends.FriendsGroupView;
import com.lemon.view.user.HeadUserInfoView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by simpletour_Jenkin on 2017/1/19.
 * 好友
 */
@Controller
public class FriendShipController extends BaseController{

    @Resource
    private FriendsManager friendsManager;

    @Resource
    private HeadUserInfoManager headUserInfoManager;

    @RequestMapping(value = "/lemon/{userId:\\d+}/friends")
    @UserLoginValidation
    public String friends(@PathVariable Long userId, HttpServletRequest request, Model model){

        List<FriendsGroupView> friendsGroupViews = friendsManager.initFriendsGroup(userId);
        HeadUserInfoView userInfoView = headUserInfoManager.getUserView(request);

        model.addAttribute("headUserInfoView",userInfoView);
        return "/lemon/friendship/friends";
    }
}