package com.lemon.manager.friends;

import com.lemon.convertor.ConvertorResult;
import com.lemon.domain.impl.friend.FriendGroup;
import com.lemon.domain.impl.friend.Friendship;
import com.lemon.domain.impl.user.User;
import com.lemon.query.friendship.FriendshipQuery;
import com.lemon.service.IFriendGroupService;
import com.lemon.service.IFriendshipService;
import com.lemon.service.IUserService;
import com.lemon.utils.BeanLocator;
import com.lemon.utils.DateUtils;
import com.lemon.utils.StringUtils;
import com.lemon.view.friends.FriendElementView;
import com.lemon.view.friends.FriendsGroupView;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by simpletour_Jenkin on 2017/1/19.
 */
@Component
public class FriendsManager {
    @Resource
    private IFriendGroupService friendGroupService;

    @Resource
    private IFriendshipService friendshipService;

    @Resource
    private IUserService userService;


    /**
     * 返回用户的分组信息
     * @param userId
     *
     * @see com.lemon.convertor.friends.FriendshipDomainToViewConvertor
     * @return
     */
    public List<FriendsGroupView> initFriendsGroup(Long userId){
        List<Friendship> friendships = friendshipService.findFriendshipByUserId(userId);
        List<FriendGroup> friendGroups = friendGroupService.findFriendGroupByUserId(userId);
        Map<Long, List<FriendGroup>> groupIdMap = friendGroups.stream().collect(Collectors.groupingBy(FriendGroup::getId));
        Map<Long, List<Friendship>> friendshipMap = friendships.stream().collect(Collectors.groupingBy(Friendship::getFriendGroupId));
        return friendshipMap.keySet().stream()
                .map(groupId -> new FriendsGroupView(groupIdMap.get(groupId).get(0).getGroupName()
                        ,friendshipMap.get(groupId).stream().map(friendship -> {
                            Optional<User> userOptional = userService.find(friendship.getFriendId());
                            ConvertorResult convertorResult = (ConvertorResult) BeanLocator.findBeanByName("FriendshipDomainToView_Convertor");
                            return (FriendElementView)convertorResult.getResult(friendship,userOptional.get());
                        }).collect(Collectors.toList()))
                ).collect(Collectors.toList());
    }

    /**
     * 精确查找用户
     * @param user
     * @return
     */
    public FriendElementView initSearchUser(User user){
        FriendElementView elementView = new FriendElementView();
        if (StringUtils.notEmpty(user.getAvatar())) {
            elementView.setAvatar(user.getAvatar());
        }
        if (user.getBirthday()!= null) {
            elementView.setBirthday(DateUtils.yearMonthDay(user.getBirthday().atStartOfDay()));
        }
        elementView.setNickName(user.getNickName());
        if (StringUtils.notEmpty(user.getProfile())) {
            elementView.setProfile(user.getProfile());
        }
        if (user.getGender()!=null) {
            elementView.setSex(user.getGender().getValue());
        }
        elementView.setUserId(user.getId());
        return elementView;
    }

    /**
     * 添加用户
     * @param currentUser
     * @param addUserId
     * @return
     */
    public String addFriend(Long currentUser,Long addUserId){
        if (currentUser.equals(addUserId)){
            return "您不能添加您自己为好友!";
        }
        FriendshipQuery query = new FriendshipQuery();
        query.setUserId(currentUser);
        query.setFriendId(addUserId);
        Optional<Friendship> friendshipOptional = friendshipService.findOne(query);
        if (friendshipOptional.isPresent()){
            return "您已经添加此用户为好友了！";
        }
        friendshipService.addFriend(currentUser,addUserId);
        return null;
    }
}
