package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.module.ResolutionException;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceUtils {
    public static Integer getId(){
        try {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getId();
        } catch (Exception e){
            throw new ResolutionException("Could not get id from user");
        }
    }

    public static User getUser(){
        try {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e){
            throw new ResolutionException("Could not get id from user");
        }
    }

    public static void checkDuplicate(List<Integer> collect, String errorName, String errorMessages) throws BadRequestException {
        Set<Integer> setCollect = new HashSet<>(collect);
        Map<String, String> error = new HashMap<>();
        collect.forEach((ele) -> {
            if (setCollect.contains(ele)) {
                error.put(errorName, ele.toString());
            }
        });
        if (!error.isEmpty()) {
            throw new BadRequestException(errorMessages, error);
        }
    }

    public static List<Integer> getListIntegerNotInList(List<Integer> packageIds,
                                                        List<Integer> needToCheckIds) {

        Set<Integer> existingProgramIds = new HashSet<>(packageIds);

        Set<Integer> missingProgramIds = needToCheckIds.stream()
                .filter(id -> !existingProgramIds.contains(id))
                .collect(Collectors.toSet());

        return missingProgramIds.stream().toList();
    }

    public static List<Integer> getListIntegerInList(List<Integer> packageIds,
                                                        List<Integer> needToCheckIds) {

        Set<Integer> existingProgramIds = new HashSet<>(packageIds);

        Set<Integer> existedProgramIds = needToCheckIds.stream()
                .filter(existingProgramIds::contains)
                .collect(Collectors.toSet());

        return existedProgramIds.stream().toList();
    }


    public static void checkListIntegerNotInList(List<Integer> packageIds,
                                                        List<Integer> needToCheckIds, String errorName, String errorMessages) throws BadRequestException {
        List<Integer> notfoundIds = getListIntegerNotInList(packageIds, needToCheckIds);

        if (!notfoundIds.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("deleteAdmissionTrainingProgramIds", notfoundIds.stream().map(Object::toString).collect(Collectors.joining(",")));
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo.", error);
        }
    }

    public static void checkListIntegerInList(List<Integer> packageIds,
                                                        List<Integer> needToCheckIds, String errorName, String errorMessages) throws BadRequestException {
        List<Integer> notfoundIds = getListIntegerInList(packageIds, needToCheckIds);

        if (!notfoundIds.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("deleteAdmissionTrainingProgramIds", notfoundIds.stream().map(Object::toString).collect(Collectors.joining(",")));
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo.", error);
        }
    }

    public static List<Integer> convertStringToIntList(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Arrays.stream(str.split(",")).map(Integer::parseInt).toList();
    }
}
