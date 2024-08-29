package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.payment.CreatePaymentLinkRequestBody;
import com.demo.admissionportal.dto.request.payment.CreateQrRequestDTO;
import com.demo.admissionportal.dto.request.payment.PaymentRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.payment.CreateQrResponseDTO;
import com.demo.admissionportal.dto.response.payment.PaymentResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.PackageRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UniversityTransactionRepository;
import com.demo.admissionportal.service.UniversityPackageService;
import com.demo.admissionportal.service.UniversityTransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class OrderController {
    private final PayOS payOS;
    private final PackageRepository packageRepository;
    private final UniversityTransactionService universityTransactionService;
    private final UniversityPackageService universityPackageService;
    private final UniversityTransactionRepository universityTransactionRepository;
    private final UniversityInfoRepository universityInfoRepository;

    /**
     * Create qr object node.
     *
     * @param requestDTO the request dto
     * @return the object node
     */
    @PostMapping("/create-QR/")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseData<CreateQrResponseDTO>> createQR(@RequestBody List<CreateQrRequestDTO> requestDTO) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Role role = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
        AdsPackage adsPackage;
        List<AdsPackage> listAdsPackage = new ArrayList<>();
        UniversityTransaction universityTransaction;
        UniversityPackage universityPackage;
        List<CreateQrResponseDTO.InfoTransactionDTO> infoTransactionDTOList = new ArrayList<>();
        for (CreateQrRequestDTO request : requestDTO) {
            adsPackage = packageRepository.findPackageById(request.getPackageId());
            if (role.equals(Role.UNIVERSITY)) {
                universityTransaction = universityTransactionService.createTransaction(userId, adsPackage);
            } else {
                UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoByConsultantId(userId);
                universityTransaction = universityTransactionService.createTransaction(universityInfo.getId(), adsPackage);
            }
            universityPackage = universityPackageService.createUniPackage(adsPackage, universityTransaction, request.getPostId());
            CreateQrResponseDTO.InfoTransactionDTO transactionDTO = new CreateQrResponseDTO.InfoTransactionDTO();
            transactionDTO.setPostId(request.getPostId());
            transactionDTO.setPackageId(request.getPackageId());
            transactionDTO.setUniversityTransactionId(universityPackage.getUniversityTransactionId());
            infoTransactionDTOList.add(transactionDTO);
            listAdsPackage.add(adsPackage);
        }

        ObjectNode result = createPaymentLink(listAdsPackage, infoTransactionDTOList);
        CreateQrResponseDTO responseDTO = new CreateQrResponseDTO();
        int error = result.findValue("error").asInt();
        if (error != 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ResponseData<>(ResponseCode.C201.getCode(), "Lấy QR thất bại"));
        }
        JsonNode dataNode = result.get("data");
        String statusPayment = dataNode.get("status").asText();
        long orderCode = dataNode.get("orderCode").asLong();
        String description = dataNode.get("description").asText();
        for (CreateQrResponseDTO.InfoTransactionDTO transaction : infoTransactionDTOList) {
            UniversityTransaction serviceTransaction = universityTransactionService.findTransaction(transaction.getUniversityTransactionId());
            serviceTransaction.setOrderCode(orderCode);
            serviceTransaction.setDescription(description);
        }
        String checkoutUrl = dataNode.get("checkoutUrl").asText();
        responseDTO.setOrderCode(orderCode);
        responseDTO.setCheckoutURL(checkoutUrl);
        responseDTO.setStatusPayment(statusPayment);
        responseDTO.setDescription(description);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(ResponseCode.C200.getCode(), "Lấy QR thành công", responseDTO));
    }

    /**
     * Gets result payment.
     *
     * @param requestDTO the request dto
     * @return the result payment
     */
    @PostMapping("/")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseData<PaymentResponseDTO>> getResultPayment(@RequestBody @Valid PaymentRequestDTO requestDTO) {
        ObjectNode resultOfPayment = getOrderById(requestDTO.getOrderCode());
        JsonNode dataNode = resultOfPayment.get("data");
        UniversityTransaction universityTransaction;
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        String status = dataNode.get("status").asText();
        List<UniversityTransaction> list = universityTransactionRepository.findByOrderCode(requestDTO.getOrderCode());
        for (UniversityTransaction ut : list) {
            if (status.equals("PAID")) {
                universityTransaction = universityTransactionService.updateTransaction(ut.getId(), status);
                universityPackageService.updateUniversityPackage(ut.getId());
            } else if (status.equals("CANCELLED")) {
                universityTransaction = universityTransactionService.updateTransaction(ut.getId(), status);
            } else {
                universityTransaction = universityTransactionService.findTransaction(ut.getId());
            }
            paymentResponseDTO.setOrderCode(requestDTO.getOrderCode());
            paymentResponseDTO.setPaymentStatus(universityTransaction.getStatus().name);
        }
        ResponseData<PaymentResponseDTO> responseData = new ResponseData<>(ResponseCode.C200.getCode(), "Lấy trạng thái payment thành công", paymentResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    /**
     * Create payment link object node.
     *
     * @param adsPackage the ads package
     * @return the object node
     */
    @Transactional(rollbackOn = Exception.class)
    public ObjectNode createPaymentLink(List<AdsPackage> adsPackage, List<CreateQrResponseDTO.InfoTransactionDTO> infoTransactionDTOList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        CreatePaymentLinkRequestBody requestBody = new CreatePaymentLinkRequestBody();
        try {
            int totalAmount = 0;
            String productName = adsPackage.stream()
                    .map(AdsPackage::getName)
                    .collect(Collectors.joining(", "));
            String productTransactionId = infoTransactionDTOList.stream()
                    .map(dto -> dto.getUniversityTransactionId().toString())
                    .collect(Collectors.joining(", "));
            for (AdsPackage ads : adsPackage) {
                totalAmount += ads.getPrice();
            }
            requestBody.setProductName(productName);
            requestBody.setDescription("Ma don hang " + productTransactionId);
            requestBody.setPrice(totalAmount);
            requestBody.setReturnUrl("https://main--uap-portal.netlify.app/university/manage-campaign");
            requestBody.setCancelUrl("https://main--uap-portal.netlify.app/university/manage-campaign");

            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description("Ma don hang " + productTransactionId)
                    .amount(totalAmount)
                    .items(mapItemList(adsPackage))
                    .returnUrl(requestBody.getReturnUrl())
                    .cancelUrl(requestBody.getCancelUrl())
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;

        }
    }

    private List<ItemData> mapItemList(List<AdsPackage> adsPackage) {
        List<ItemData> items = new ArrayList<>();
        for (AdsPackage ads : adsPackage) {
            ItemData item = ItemData.builder()
                    .name(ads.getName())
                    .price(ads.getPrice())
                    .quantity(1)
                    .build();
            items.add(item);
        }
        return items;
    }

    /**
     * Gets order by id.
     *
     * @param orderId the order id
     * @return the order by id
     */
    @Transactional(rollbackOn = Exception.class)
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}
