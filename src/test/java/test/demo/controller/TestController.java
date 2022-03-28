package test.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.demo.enums.EntityStatus;
import test.demo.enums.Gender;
import test.demo.model.PageResult;
import test.demo.model.entity.User;
import test.demo.model.entity.UserDetail;
import test.demo.service.UserService;
import test.demo.task.TestTask;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


@SpringBootTest
@Slf4j(topic = "exceptionLog")
public class TestController {
    @Autowired
    UserService userService;

    @Test
    public void testThread() {
        Callable callable = new TestTask();
        for (int i = 0; i < 10; i++) {
            try {
                FutureTask task = new FutureTask<>(callable);
                new Thread(task, "子线程" + i).start();

                System.out.println("子线程返回值：" + task.get() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testInsert() throws Exception {
        Pair<User, UserDetail> userInfo   = getUser();
        Pair<Boolean, String>  saveResult = userService.saveUser(userInfo.getKey(), userInfo.getValue());
        System.out.println(saveResult);
    }

    @Test
    public void testDetail() {
        Map<String, Object> user = userService.userDetail(1L, EntityStatus.STATUS_ACTIVE.getValue());
        System.out.println(user);
    }

    @Test
    public void testList() {
        PageResult pageResult = new PageResult();
        PageResult userList   = userService.getUserList(pageResult);
        System.out.println(userList);
    }

    public Pair<User, UserDetail> getUser() {
        String name = "A·Da";
        User   user = new User();
        user.setUserName(name);
        user.setAge(30);
        user.setGender(Gender.GENDER_MALE);
        user.setEmail(StringUtils.lowerCase(user.getUserName()) + "@58.com");
        user.setCreatedBy("admin");
        user.setUpdatedBy("admin");
        user.setStatus(EntityStatus.STATUS_ACTIVE);
        //-----
        UserDetail userDetail = new UserDetail();
        userDetail.setAddress("北京市 朝阳区 酒仙桥 58同城");
        userDetail.setStatus(EntityStatus.STATUS_ACTIVE);
        userDetail.setPhone("15200000000");
        userDetail.setCreatedBy("admin");
        userDetail.setUpdatedBy("admin");
        return Pair.of(user, userDetail);
    }
}
