package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityTransactionStatus;
import com.demo.admissionportal.dto.request.ads_package.PackageResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.payment.OrderResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UniversityTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type University transaction service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityTransactionServiceImpl implements UniversityTransactionService {
    private final UniversityTransactionRepository universityTransactionRepository;
    private final ModelMapper modelMapper;
    private final UniversityInfoRepository universityInfoRepository;
    private final PackageRepository packageRepository;
    private final ConsultantInfoRepository consultantInfoRepository;
    private final UniversityPackageRepository universityPackageRepository;
    private final PostRepository postRepository;

    public UniversityTransaction createTransaction(Integer universityId, AdsPackage adsPackage) {
        try {
            UniversityTransaction universityTransaction = new UniversityTransaction();
            universityTransaction.setUniversityId(universityId);
            universityTransaction.setPackageId(adsPackage.getId());
            universityTransaction.setCreateBy(universityId);
            universityTransaction.setCreateTime(new Date());
            universityTransaction.setStatus(UniversityTransactionStatus.PENDING);
            return universityTransactionRepository.save(universityTransaction);
        } catch (Exception ex) {
            log.error("Error when create transaction {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public UniversityTransaction updateTransaction(Integer transactionId, String status) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UniversityTransaction universityTransaction = findTransaction(transactionId);
            if (universityTransaction == null) {
                return null;
            }
            if (status.equals("CANCELLED")) {
                universityTransaction.setStatus(UniversityTransactionStatus.CANCELED);
            } else if (status.equals("PAID")) {
                universityTransaction.setStatus(UniversityTransactionStatus.PAID);
            }
            universityTransaction.setCompleteTime(new Date());
            universityTransaction.setUpdateBy(universityId);
            return universityTransactionRepository.save(universityTransaction);
        } catch (Exception ex) {
            log.error("Error when update transaction {}", ex.getMessage());
        }
        return null;
    }

    public UniversityTransaction findTransaction(Integer transactionId) {
        return universityTransactionRepository.findByTransactionId(transactionId);
    }

    @Override
    public ResponseData<Page<PackageResponseDTO>> getListPackage(String adsName, String status, String orderCode, Pageable pageable) {
        try {
            Page<UniversityTransaction> list = universityTransactionRepository.findListTransaction(adsName, status, orderCode, pageable);
            Page<PackageResponseDTO> responseDTOList = list.map(this::mapToPackageResponse);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách giao dịch thành công", responseDTOList);

        } catch (Exception ex) {
            log.error("Error when get transaction {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách giao dịch");
        }
    }

    private PackageResponseDTO mapToPackageResponse(UniversityTransaction universityTransaction) {
        PackageResponseDTO responseDTO = modelMapper.map(universityTransaction, PackageResponseDTO.class);
        responseDTO.setUrl(mapToPost(universityTransaction.getId()));
        responseDTO.setInfoUniversity(mapToInfoUniversity(universityTransaction.getUniversityId()));
        AdsPackage infoPackage = mapToInfoPackage(universityTransaction.getPackageId());
        responseDTO.setPrice(infoPackage.getPrice());
        responseDTO.setInfoPackage(modelMapper.map(infoPackage, PackageResponseDTO.InfoPackage.class));
        responseDTO.setStatus(universityTransaction.getStatus().name);
        return responseDTO;
    }

    private String mapToPost(Integer id) {
        UniversityPackage universityPackage = universityPackageRepository.findUniversityPackageByTransactionId(id);
        if (universityPackage == null) {
            return null;
        }
        Post post = postRepository.findById(universityPackage.getPostId()).orElse(null);
        return post.getUrl();
    }

    private AdsPackage mapToInfoPackage(Integer packageId) {
        return packageRepository.findPackageById(packageId);
    }

    private PackageResponseDTO.InfoUniversity mapToInfoUniversity(Integer userId) {
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(userId);
        if (universityInfo == null) {
            universityInfo = universityInfoRepository.findUniversityInfoByConsultantId(userId);
        }
        return PackageResponseDTO.InfoUniversity.builder()
                .id(universityInfo.getId())
                .name(universityInfo.getName())
                .build();
    }

    @Override
    public ResponseData<Page<OrderResponseDTO>> getOrderByUniId(String orderCode, String status, Pageable pageable) {
        Page<OrderResponseDTO> responseDTOS;
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            Role role = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            if (role == Role.CONSULTANT) {
                ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(userId);
                userId = consultantInfo.getUniversityId();
            }
            Page<UniversityTransaction> list = universityTransactionRepository.findByUniversityId(userId, orderCode, status, pageable);
            responseDTOS = list.map(this::mapToOrderResponse);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách giao dịch với university thành công", responseDTOS);
        } catch (Exception ex) {
            log.error("Error when get list transaction by University Id: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách giao dịch với universityId ");
        }
    }

    private OrderResponseDTO mapToOrderResponse(UniversityTransaction universityTransaction) {
        OrderResponseDTO responseDTO = modelMapper.map(universityTransaction, OrderResponseDTO.class);
        AdsPackage adsPackage = packageRepository.findPackageById(universityTransaction.getPackageId());
        Post post = mapToPostURLAndTitle(universityTransaction.getId());
        responseDTO.setPackageName(adsPackage.getName());
        responseDTO.setPrice(adsPackage.getPrice());
        responseDTO.setStatus(universityTransaction.getStatus().name);
        responseDTO.setTitle(post.getTitle());
        responseDTO.setUrl(post.getUrl());
        return responseDTO;
    }

    private Post mapToPostURLAndTitle(Integer id) {
        UniversityPackage universityPackage = universityPackageRepository.findUniversityPackageByTransactionId(id);
        return postRepository.findById(universityPackage.getPostId()).orElse(null);
    }
}
