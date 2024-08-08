package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.payment.CreatePaymentLinkRequestBody;
import com.demo.admissionportal.dto.request.payment.PaymentRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.payment.CreateQrResponseDTO;
import com.demo.admissionportal.dto.response.payment.PaymentResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityPackage;
import com.demo.admissionportal.entity.UniversityTransaction;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.PackageRepository;
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

import java.util.Date;

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

    /**
     * Create qr object node.
     *
     * @param packageId the package id
     * @return the object node
     */
    @PostMapping("/create-QR/")
    @Transactional
    public ResponseEntity<ResponseData<CreateQrResponseDTO>> createQR(@RequestParam(name = "packageId") Integer packageId, @RequestParam(name = "postId") Integer postId) {
        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        AdsPackage adsPackage = packageRepository.findPackageById(packageId);
        UniversityTransaction universityTransaction = universityTransactionService.createTransaction(universityId, adsPackage);
        UniversityPackage universityPackage = universityPackageService.createUniPackage(adsPackage, universityTransaction, postId);
        ObjectNode result = createPaymentLink(adsPackage);
        CreateQrResponseDTO responseDTO = new CreateQrResponseDTO();
        int error = result.findValue("error").asInt();
        if (error != 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ResponseData<>(ResponseCode.C201.getCode(), "Lấy QR thất bại"));
        }
        JsonNode dataNode = result.get("data");
        String statusPayment = dataNode.get("status").asText();
        long orderCode = dataNode.get("orderCode").asLong();
        String checkoutUrl = dataNode.get("checkoutUrl").asText();

        responseDTO.setOrderCode(orderCode);
        responseDTO.setCheckoutURL(checkoutUrl);
        responseDTO.setUniversityTransactionId(universityPackage.getUniversityTransactionId());
        responseDTO.setStatusPayment(statusPayment);
        responseDTO.setPostId(postId);
        responseDTO.setPackageId(adsPackage.getId());
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(ResponseCode.C200.getCode(), "Lấy QR thành công", responseDTO));
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity<ResponseData<PaymentResponseDTO>> getResultPayment(@RequestBody @Valid PaymentRequestDTO requestDTO) {
        ObjectNode resultOfPayment = getOrderById(requestDTO.getOrderCode());
        JsonNode dataNode = resultOfPayment.get("data");
        UniversityTransaction universityTransaction;
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        String status = dataNode.get("status").asText();
        if (status.equals("PAID")) {
            universityTransaction = universityTransactionService.updateTransaction(requestDTO.getUniversityTransactionId(), status);
            universityPackageService.updateUniversityPackage(requestDTO.getUniversityTransactionId(), requestDTO.getPostId(), requestDTO.getPackageId());
        } else if (status.equals("CANCELLED")) {
            universityTransaction = universityTransactionService.updateTransaction(requestDTO.getUniversityTransactionId(), status);
        } else {
            universityTransaction = universityTransactionService.findTransaction(requestDTO.getUniversityTransactionId());
        }
        paymentResponseDTO.setPayment(universityTransaction.getStatus().name);

        ResponseData<PaymentResponseDTO> responseData = new ResponseData<>(ResponseCode.C200.getCode(), "Lấy trạng thái payment thành công", paymentResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    /**
     * Create payment link object node.
     *
     * @param adsPackage the ads package
     * @return the object node
     */
    public ObjectNode createPaymentLink(AdsPackage adsPackage) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        CreatePaymentLinkRequestBody requestBody = new CreatePaymentLinkRequestBody();
        try {
            requestBody.setProductName(adsPackage.getName());
            requestBody.setDescription("Thanh toán gói quảng cáo");
            requestBody.setPrice(adsPackage.getPrice());
            requestBody.setReturnUrl("https://your-return-url.com");
            requestBody.setCancelUrl("https://your-cancel-url.com");
            // Gen order code
            String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder()
                    .name(requestBody.getProductName())
                    .price(requestBody.getPrice())
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(requestBody.getDescription())
                    .amount(requestBody.getPrice())
                    .item(item)
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

    /**
     * Gets order by id.
     *
     * @param orderId the order id
     * @return the order by id
     */
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
