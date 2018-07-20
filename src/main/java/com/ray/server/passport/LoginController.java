package com.ray.server.passport;

import com.ray.server.controller.AbstractController;
import com.ray.server.data.PlayerEntity;
import com.ray.server.data.RedisUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends AbstractController {

    /**
     * 登录接口，线程安全方式访问
     */
    @RequestMapping(value="/doLogin",method = RequestMethod.GET)
    public  void doLogin(){

          String sessionId =   getSession().getId();

          String playerinfo = RedisUtil.getString(sessionId);

          if(StringUtils.isEmpty(playerinfo)){


          }else{

              //TODO indexService gan_index
              long id = 100001;
              PlayerEntity entity = new PlayerEntity();
              entity.setLevel(1);
              entity.setPassportId(id);
              entity.setRoleId(id);
              entity.setWorkers(10);

          }
    }
}
