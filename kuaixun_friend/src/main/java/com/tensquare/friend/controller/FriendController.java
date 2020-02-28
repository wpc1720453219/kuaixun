package com.tensquare.friend.controller;


import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FriendService friendService;
   @Autowired
    private UserClient userClient;
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid , @PathVariable String type){

     Claims claims=(Claims) request.getAttribute("claims_user");

       if(claims==null){
           return  new Result(false,StatusCode.LOGINERROR,"权限不足");
       }
       String userid=claims.getId();
        if(type!=null){
              if(type.equals("1")){
             int flag= friendService.addFriend(userid,friendid);
               if(flag==0)
                    return  new Result(false,StatusCode.ERROR,"不能重复添加好友");
              if(flag==1){
                  userClient.updatefanscountandfollowcount(userid,friendid,1);
                  return new Result(true,StatusCode.OK,"添加成功");
              }

              }
              if(type.equals("2")){
                  int flag= friendService.addNoFriend(claims.getId(),friendid);
                  if(flag==0)
                      return  new Result(false,StatusCode.ERROR,"不能重复添加好友");
                  if(flag==1)
                      return new Result(true,StatusCode.OK,"添加成功");
              }
            return new Result(false,StatusCode.ERROR,"参数异常");
          }else {
              return new Result(false,StatusCode.ERROR,"参数异常");
          }
    }
    @RequestMapping(value="/{friendid}",method=RequestMethod.DELETE)
    public Result remove(@PathVariable String friendid){
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        String userid=claims.getId();
        friendService.deleteFriend(userid, friendid);
        userClient.updatefanscountandfollowcount(userid,friendid,-1);
        return new Result(true, StatusCode.OK, "删除成功");
    }
    
}
