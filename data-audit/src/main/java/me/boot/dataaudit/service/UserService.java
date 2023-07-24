package me.boot.dataaudit.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.dataaudit.entity.User;
import me.boot.dataaudit.repository.UserDao;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.javers.spring.annotation.JaversAuditable;
import org.springframework.stereotype.Service;

/**
 * @description
 * @date 2023/07/16
 **/
@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final Javers javers;

    @JaversAuditable
    public void saveUser(User user) {
        userDao.save(user);
    }

    public String getSnapshots(Object id, Class<?> entityClass) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, entityClass);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

    public String getUserAlterations(Integer id) {
        return getSnapshots(id, User.class);
    }

    public String getUserShadows(Object id) {
        User user = userDao.findById((Integer) id).orElse(null);
        JqlQuery jqlQuery = QueryBuilder.byInstance(user)
            .withChildValueObjects().build();
        List<Shadow<User>> shadows = javers.findShadows(jqlQuery);
        return javers.getJsonConverter().toJson(shadows.get(0));
    }

}
