package me.boot.entity;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Mapper
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    @Mapping(source = "siteName", target = "name")
    WebSite siteToWebSite(Site site);

    @InheritConfiguration
    Site webSiteToSite(WebSite webSite);
}
