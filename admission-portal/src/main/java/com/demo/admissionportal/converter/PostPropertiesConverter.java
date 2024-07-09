package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import org.modelmapper.AbstractConverter;

/**
 * The type Post properties converter.
 */
public class PostPropertiesConverter extends AbstractConverter<PostPropertiesStatus, String> {

    @Override
    protected String convert(PostPropertiesStatus postPropertiesStatus) {
        switch (postPropertiesStatus) {
            case ACTIVE:
                return "Hoạt động";
            case INACTIVE:
                return "Không hoạt động";
            default:
                return "Không biết";
        }
    }
}
