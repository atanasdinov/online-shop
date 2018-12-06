package com.scalefocus.shop.enumeration;

import com.scalefocus.shop.exception.InvalidProductDataException;

/**
 * <b>This enum is being used to determine in what situation {@link InvalidProductDataException} is being thrown.</b>
 */
public enum ProductAction {
    CREATE,
    EDIT
}
