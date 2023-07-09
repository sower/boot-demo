package me.boot.web.bean;

import javax.persistence.Embeddable;
import lombok.Data;

/**
 * @description
 * @date 2022/09/28
 */
@Data
@Embeddable
public class Address {

    private String city;

    private String street;

    private String zipCode;

}
