package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.PostStatus;
import org.modelmapper.AbstractConverter;

/**
 * The type Post status converter.
 */
public class PostStatusConverter extends AbstractConverter<PostStatus, String> {
    @Override
    protected String convert(PostStatus postStatus) {
        switch (postStatus) {
            case ACTIVE:
                return "Hoạt động";
            case INACTIVE:
                return "Không hoạt động";
            case PENDING:
                return "Chờ duyệt";
            case PRIVATE:
                return "Ẩn danh";
            default:
                return "Không biết";
        }
    }
}
