package me.boot.repository;

import me.boot.entity.WebSite;
import org.springframework.data.repository.CrudRepository;

public interface WebSiteDao extends CrudRepository<WebSite, String> {

}
