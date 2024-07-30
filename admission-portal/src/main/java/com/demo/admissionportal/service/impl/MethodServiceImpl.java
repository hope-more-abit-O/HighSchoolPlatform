package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.dto.entity.method.CreateMethodDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.dto.request.method.PutMethodRequest;
import com.demo.admissionportal.dto.request.method.UpdateMethodStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.QueryException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.MethodRepository;
import com.demo.admissionportal.service.MethodService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MethodServiceImpl implements MethodService {
    private final MethodRepository methodRepository;
    private final ModelMapper modelMapper;

    private Method findById(Integer id) throws ResourceNotFoundException{
        return methodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức xét tuyển."));
    }

    public List<Method> findByIds(List<Integer> ids)
            throws ResourceNotFoundException{
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

    public List<Method> saveAll(List<Method> methods)
            throws StoreDataFailedException {
        try {
            return methodRepository.saveAll(methods);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin ngành học thất bại.");
        }
    }

    public void checkExistedNameAndCode(List<Method> Methods)
            throws DataExistedException {
        Map<String, String> errors = new HashMap<>();
        Set<String> MethodNames = Methods.stream()
                .map(Method::getName)
                .collect(Collectors.toSet());

        Set<String> MethodCodes = Methods.stream()
                .map(Method::getCode)
                .collect(Collectors.toSet());

        List<Method> existedMethodsByName = methodRepository.findByNameIn(MethodNames);
        List<Method> existedMethodsByCode = methodRepository.findByCodeIn(MethodCodes);

        if (!existedMethodsByName.isEmpty()){
            String allNames = existedMethodsByName.stream()
                    .map(Method::getName)
                    .collect(Collectors.joining(", "));
            errors.put("nameExisted", allNames);
        }

        if (!existedMethodsByCode.isEmpty()){
            String allCodes = existedMethodsByCode.stream()
                    .map(Method::getCode)
                    .collect(Collectors.joining(", "));
            errors.put("codeExisted", allCodes);
        }

        if (!errors.isEmpty()) {
            throw new DataExistedException("Có phương thức tuyển sinh đã tồn tại", errors);
        }
    }

    private boolean checkMethodName(String methodName) {
        return methodRepository.findFirstByName(methodName).isPresent();
    }

    private boolean checkMethodNameOrCodeAvailable(String methodName, String methodCode) {
        return methodRepository.findFirstByNameOrCode(methodName, methodCode).isPresent();
    }

    private Method save(Method method)
            throws StoreDataFailedException {
        try {
            return methodRepository.save(method);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin phương thức tuyển sinh thất bại.");
        }
    }

    private void checkForExistingMethodNamesAndCodes(List<CreateMethodDTO> createMethodDTOs)
            throws DataExistedException{
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

    private List<Method> createAndSaveMethods(List<CreateMethodDTO> methodDTOs, Integer createById)
            throws StoreDataFailedException {
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

    private List<Method> checkAndInsert(List<CreateMethodDTO> methods)
            throws DataExistedException, StoreDataFailedException {
        Integer createById = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        checkForExistingMethodNamesAndCodes(methods);

        return createAndSaveMethods(methods, createById);
    }

    public List<Method> insertNewMethodsAndGetExistedMethods(List<CreateMethodDTO> createMethodDTOS, List<Integer> methodIds)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException  {
        return Stream.concat(checkAndInsert(createMethodDTOS).stream(), findByIds(methodIds).stream()).toList();
    }

    public List<InfoMethodDTO> toListInfoMethodDTO(List<Method> methods){
        return methods.stream()
                .map(Method -> modelMapper.map(Method, InfoMethodDTO.class))
                .toList();
    }

    public List<Method> insertNewMethodsAndGetExistedMethods(List<CreateAdmissionQuotaRequest> quotas){
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        //CREATE A LIST OF NEW METHODS
        List<Method> newMethods = quotas.stream()
                .filter(quota -> quota.getMethodId() == null )
                .map(quota -> new Method(quota.getMethodCode(), quota.getMethodName(), consultantId))
                .toList();

        //VALIDATE METHOD'S NAME AND CODE
        checkExistedNameAndCode(newMethods);

        //SAVE ALL NEW MethodS INTO DATABASE
        List<Method> result = saveAll(newMethods);

        //GET ALL MethodS EXISTED BY IDS
        result.addAll(findByIds(quotas
                .stream()
                .map(CreateAdmissionQuotaRequest::getMethodId)
                .filter(Objects::nonNull)
                .toList()
        ));

        return result;
    }

    @Override
    public ResponseData<Page<Method>> getAllMethods(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            Date createTime,
            Integer createBy,
            Date updateTime,
            Integer updateBy,
            MethodStatus status) {
        try {
            Page<Method> methods = methodRepository.findMethodBy(pageable, id, code, name, createTime, createBy, updateTime, updateBy, status);

            if (methods.getContent().isEmpty())
                return ResponseData.ok("Không tìm thấy phương pháp.");

            return ResponseData.ok("Lấy thông tin các phương pháp thành công.", methods);
        } catch (Exception e) {
            throw new QueryException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseData<Method> createMethod(String name, String code) {
        log.info("Start Transactional!");

        Integer id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        log.info("Get staff / admin's id: {}", id);

        log.info("Received request to create method with name: '{}' and code: '{}'", name, code);

        List<Method> existedMethods = methodRepository.findByNameOrCode(name, code);

        Map<String, String> errors = new HashMap<>();

        if (!existedMethods.isEmpty()) {
            log.info("Found existing methods with the same name or code.");

            for (Method method : existedMethods) {
                if (method.getName().equals(name) && !errors.containsKey("name")) {
                    log.error("Method name '{}' already exists.", name);
                    errors.put("name", "Tên phương thức tuyển sinh đã tồn tại.");
                }
                if (method.getCode().equals(code) && !errors.containsKey("code")) {
                    log.error("Method code '{}' already exists.", code);
                    errors.put("code", "Mã phương thức tuyển sinh đã tồn tại.");
                }
            }

            log.error("Method already exists with errors: {}", errors);
            throw new DataExistedException("Tên hoặc mã phương thức tuyển sinh đã tồn tại", errors);

        }

        try {
            log.info("Creating new method with name: '{}' and code: '{}'", name, code);
            Method method = new Method(code, name, id);

            Method savedMethod = methodRepository.save(method);
            log.info("Successfully created method with id: {}", savedMethod.getId());

            return ResponseData.ok("Method created successfully.", savedMethod);
        } catch (Exception e) {
            log.error("Error occurred while creating method: {}", e.getMessage(), e);
            throw new QueryException(e.getMessage());
        }
    }

    @Override
    public ResponseData<Method> updateMethod(PutMethodRequest request) {
        try {
            Integer updaterId = ServiceUtils.getId();
            log.info("Updater ID: {}", updaterId);
            log.info("Received request to update method with ID: {}", request.getMethodId());

            Method method = this.findById(request.getMethodId());
            log.info("Found method: {}", method);

            method.update(request.getMethodName(), request.getMethodCode(), request.getNote(), updaterId);
            log.info("Updated method with new values from request.");

            Method savedMethod = methodRepository.save(method);
            log.info("Successfully saved updated method with ID: {}", savedMethod.getId());

            return ResponseData.ok("Cập nhập phương thức xét tuyển thành công.", savedMethod);
        } catch (Exception e) {
            log.error(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getCause().getCause().getStackTrace()) {
                log.warn(stackTraceElement.toString());
            }
            throw new QueryException(e.getMessage());
        }
    }

    @Override
    public ResponseData<Method> updateMethodStatus(UpdateMethodStatusRequest request) {
        Integer updaterId = ServiceUtils.getId();
        log.info("Updater ID: {}", updaterId);
        log.info("Received request to update method status with ID: {}", request.getId());

        Method method = this.findById(request.getId());
        log.info("Found method: {}", method);

        log.info("Comparing new status: {} with existing status: {}", request.getId(), method.getStatus());
        if (method.getStatus().equals(request.getStatus())) {
            log.info("Status is the same.");
            return ResponseData.ok("Trạng thái giống nhau.",method);
        }

        method.updateStatus(request.getStatus(), request.getNote(), updaterId);
        log.info("Updated method with new status from request.");

        Method savedMethod = methodRepository.save(method);
        log.info("Successfully saved updated method with ID: {}", savedMethod.getId());

        return ResponseData.ok("Cập nhập trạng thái thành công.",method);
    }


}
