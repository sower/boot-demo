package me.boot.datajpa.repository;

import java.util.List;
import me.boot.datajpa.base.BaseRepository;
import me.boot.datajpa.base.VersionId;
import me.boot.datajpa.entity.WebSite;
import org.springframework.data.jpa.repository.Query;

public interface WebSiteDao extends BaseRepository<WebSite, VersionId> {

//    List<WebSite> findAllByName(String name);

    List<WebSite> findAllByNameAndVersion(String name, String version);

    @Query(value = "select t.name,t.version,t.url,t.description,t.is_deleted,t.creator,t.create_time,t.id,t.update_time,t.updater from t_website t where name=?1",nativeQuery = true)
    List<WebSite> findAllByName(String name);
}
