package org.fffd.l23o6.UnitTests;


import org.apache.catalina.User;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * 这个单元测试用于测试UserService接口类的功能
 * 为了验证数据是否入库，必须引入UserDao类用于拉取数据库数据
 * */
@SpringBootTest
public class UserTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void registerUser(){
        String pwd = "TestUser123";
        String name = "团队杀手";
        String idn = "320501200401235723";
        String phone = "13372778964";
        String type = "身份证";
        userService.register("testuser1", pwd, name,
                idn, phone, type);
        UserEntity getUserEntity = userDao.findByUsername("testuser1");

        assertEquals(getUserEntity.getIdn(), idn);
        assertEquals( getUserEntity.getName() ,name);
        assertEquals( getUserEntity.getPhone() , phone);
        assertEquals(getUserEntity.getType() , type);

        //这里是为了验证密码密文存储
        assertNotEquals(getUserEntity.getPassword(), pwd);

    }

    @Test
    public void editUserInfo(){
        String name = "杀手团队";
        String idn = "320501200401265743";
        String phone = "13372777964";
        String type = "身份证";
        userService.editInfo("testuser1", name, idn, phone, type);
        UserEntity getUserEntity = userDao.findByUsername("testuser1");
        assertEquals(getUserEntity.getIdn(), idn);
        assertEquals( getUserEntity.getName() ,name);
        assertEquals( getUserEntity.getPhone() , phone);
        assertEquals(getUserEntity.getType() , type);
    }

    @Test
    public void editCreditTest(){
        userService.editCredit(userService.findByUserName("testuser1").getId(), 50000);
        assertEquals(userDao.findByUsername("testuser1").getCredit(), 50000);
    }
}
