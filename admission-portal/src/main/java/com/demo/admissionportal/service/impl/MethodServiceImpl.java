package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.entity.method.CreateMethodDTO;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MethodServiceImpl {
    private final MethodRepository methodRepository;

    private Method findById(Integer id) throws ResourceNotFoundException{
        return methodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức xét tuyển."));
    }

    public List<Method> findByIds(List<Integer> ids) throws ResourceNotFoundException{
        List<Method> methods = methodRepository.findByIdIn(ids);

        // Check for IDs that were not found
        List<Integer> foundIds = methods.stream().map(Method::getId).toList();
        List<Integer> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

        Map<String, String> error = new HashMap<>();
        error.put("error", notFoundIds
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        if (!notFoundIds.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy ngành.", error);
        }

        return methods;
    }

    private boolean checkMethodName(String methodName) {
        return methodRepository.findFirstByName(methodName).isPresent();
    }

    private boolean checkMethodNameOrCodeAvailable(String methodName, String methodCode) {
        return methodRepository.findFirstByNameOrCode(methodName, methodCode).isPresent();
    }

    private Method save(Method method) throws StoreDataFailedException {
        try {
            return methodRepository.save(method);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin phương thức tuyển sinh thất bại.");
        }
    }

    private void checkForExistingMethodNamesAndCodes(List<CreateMethodDTO> createMethodDTOs) throws DataExistedException{
        Set<String> methodNames = createMethodDTOs.stream()
                .map(CreateMethodDTO::getName)
                .collect(Collectors.toSet());

        Set<String> methodCodes = createMethodDTOs.stream()
                .map(CreateMethodDTO::getCode)
                .collect(Collectors.toSet());

        List<Method> existingMethods = methodRepository.findByNameInOrCodeIn(methodNames, methodCodes);

        Set<String> existingMethodNames = existingMethods.stream()
                .map(Method::getName)
                .collect(Collectors.toSet());

        Set<String> existingMethodCodes = existingMethods.stream()
                .map(Method::getCode)
                .collect(Collectors.toSet());

        Map<String, String> errors = new HashMap<>();
        createMethodDTOs.forEach(method -> {
            if (existingMethodNames.contains(method.getName()) || existingMethodCodes.contains(method.getCode()))
                errors.put("methodExisted", method.getName() + "-" + method.getCode());
        });

        if (!errors.isEmpty()) {
            throw new DataExistedException("Có phương thức tuyển sinh đã tồn tại", errors);
        }
    }

    private List<Method> createAndSaveMethods(List<CreateMethodDTO> methodDTOs, Integer createById) throws StoreDataFailedException {
        List<Method> savedMethods = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();

        methodDTOs.forEach(methodDTO -> {
            try {
                Method method = methodRepository.save(new Method(methodDTO.getCode(), methodDTO.getName(), createById));
                savedMethods.add(method);
            } catch (Exception e) {
                // Log the error
                log.error("Error saving method: {} \n {}", methodDTO.getName(), e.getMessage());
                errors.put("error", methodDTO.getName() + "-" + methodDTO.getCode() + ": lưu thất bại");
            }
        });

        if (!errors.isEmpty()) {
            throw new StoreDataFailedException("Lưu thông tin phương thức tuyển sinh thất bại.", errors);
        }

        return savedMethods;
    }

    private List<Method> checkAndInsert(List<CreateMethodDTO> methods) throws DataExistedException, StoreDataFailedException {
        Integer createById = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        checkForExistingMethodNamesAndCodes(methods);

        return createAndSaveMethods(methods, createById);
    }

    public List<Method> insertNewMethodsAndGetExistedMethods(List<CreateMethodDTO> createMethodDTOS, List<Integer> methodIds) throws StoreDataFailedException {
        return Stream.concat(checkAndInsert(createMethodDTOS).stream(), findByIds(methodIds).stream()).toList();
    }
}
