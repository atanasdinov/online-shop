package com.scalefocus.shop.enumeration;


import com.scalefocus.shop.exception.InvalidCategoryNameException;

/**
 * <b>This enum is being used to determine in what situation {@link InvalidCategoryNameException} is being thrown.</b>
 */
public enum CategoryAction {
    CREATE,
    EDIT
}
