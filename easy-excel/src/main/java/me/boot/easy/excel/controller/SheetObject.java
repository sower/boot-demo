package me.boot.easy.excel.controller;

import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description
 * @date 2023/10/14
 **/
@Data
@NoArgsConstructor
public class SheetObject {

    String sheetName;

    Class<?> headClass;

    List<List<String>> head;

    Collection<?> data;

    public SheetObject(String sheetName, List<List<String>> head, Collection<?> data) {
        this.sheetName = sheetName;
        this.head = head;
        this.data = data;
    }

    public SheetObject(String sheetName, Class<?> headClass, Collection<?> data) {
        this.sheetName = sheetName;
        this.headClass = headClass;
        this.data = data;
    }

    public SheetObject(String sheetName, Collection<?> data) {
        this.sheetName = sheetName;
        this.data = data;
    }
}
