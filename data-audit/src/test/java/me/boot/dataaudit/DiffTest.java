package me.boot.dataaudit;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import me.boot.dataaudit.entity.User;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.junit.jupiter.api.Test;

/**
 * @description
 * @date 2023/07/13
 **/
public class DiffTest {

    private Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
        .build();

    @Test
    public void changeTest() {
        User user1 = new User().id(1).birth(LocalDate.now())
            .name("one")
            .online(true)
            .roles(Lists.newArrayList("admin", "system"));
        User user2 = new User().id(2).birth(LocalDate.now())
            .name("two")
            .online(false)
            .roles(Lists.newArrayList("admin", "member"));

        Diff diff = javers.compare(user1, user2);
        System.out.println(diff.prettyPrint());
        System.out.println("------");

        Changes changes = diff.getChanges();
        for (Change change : changes) {
            if ((change instanceof NewObject)) {
                System.out.println("新增改动: " + change);
            }

            if ((change instanceof ObjectRemoved)) {
                System.out.println("删除改动: " + change);
            }

            if ((change instanceof ValueChange)) {
                System.out.println("修改改动: " + change);
            }
        }
    }

}
