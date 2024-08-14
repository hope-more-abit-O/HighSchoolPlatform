//package com.demo.admissionportal.converter;
//
//import com.demo.admissionportal.constants.AccountStatus;
//import com.demo.admissionportal.constants.HollandCharacteristicType;
//import org.modelmapper.AbstractConverter;
//
///**
// * Converter class for mapping {@link AccountStatus} enum values to Vietnamese translations.
// *
// * @author hopeless
// * @version 1.0
// * @since 16/06/2024
// */
//public class HollandTypeConverter extends AbstractConverter<HollandCharacteristicType, String> {
//
//
//    @Override
//    protected String convert(HollandCharacteristicType hollandCharacteristicType) {
//        switch (hollandCharacteristicType) {
//            case Realistic:
//                return "Kỹ thuật";
//            case Investigative:
//                return "Nghiên cứu";
//            case Artistic:
//                return "Nghệ thuật";
//            case Social:
//                return "Xã hội";
//            case Enterprising:
//                return "Quản lý";
//            case Conventional:
//                return "Nghiệp vụ";
//            default:
//                return "Không biết";
//        }
//    }
//}