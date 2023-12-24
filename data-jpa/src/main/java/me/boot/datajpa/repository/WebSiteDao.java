package me.boot.datajpa.repository;

import java.util.List;
import me.boot.datajpa.base.BaseRepository;
import me.boot.datajpa.base.VersionId;
import me.boot.datajpa.entity.WebSite;

public interface WebSiteDao extends BaseRepository<WebSite, VersionId> {

    List<WebSite> findAllByName(String name);

    List<WebSite> findAllByNameAndVersion(String name, String version);

}
