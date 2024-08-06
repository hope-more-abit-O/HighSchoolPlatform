//package com.demo.admissionportal.controller;
//
//import com.demo.admissionportal.dto.request.payment.CreatePaymentLinkRequestBody;
//import com.demo.admissionportal.entity.AdsPackage;
//import com.demo.admissionportal.entity.UniversityTransaction;
//import com.demo.admissionportal.entity.User;
//import com.demo.admissionportal.repository.PackageRepository;
//import com.demo.admissionportal.service.UniversityPackageService;
//import com.demo.admissionportal.service.UniversityTransactionService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//import vn.payos.PayOS;
//import vn.payos.type.CheckoutResponseData;
//import vn.payos.type.ItemData;
//import vn.payos.type.PaymentData;
//import vn.payos.type.PaymentLinkData;
//
//import java.util.Date;
//import java.util.Map;
//
///**
// * The type Order controller.
// */
//@RestController
//@RequestMapping("/api/v1/order")
//@RequiredArgsConstructor
//@SecurityRequirement(name = "BearerAuth")
//public class OrderController {
//    private final PayOS payOS;
//    private final PackageRepository packageRepository;
//    private final UniversityTransactionService universityTransactionService;
//    private final UniversityPackageService universityPackageService;
//
//    /**
//     * Create qr object node.
//     *
//     * @param packageId the package id
//     * @return the object node
//     */
//    @PostMapping("/create-QR/pID=")
//    public ObjectNode createQR(@RequestParam(name = "packageId") Integer packageId, @RequestParam(name = "postId") Integer postId) {
//        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//        AdsPackage adsPackage = packageRepository.findPackageById(packageId);
//        UniversityTransaction universityTransaction = universityTransactionService.createTransaction(universityId, adsPackage);
//        universityPackageService.createUniPackage(adsPackage, universityTransaction, postId);
//        return createPaymentLink(adsPackage);
//    }
//
//    @GetMapping("/pId=")
//    public ObjectNode getOrderById(@RequestParam(name = "packageId") Integer packageId) {
//        Integer
//    }
//
//    @GetMapping
//
//    /**
//     * Create payment link object node.
//     *
//     * @param adsPackage the ads package
//     * @return the object node
//     */
//    public ObjectNode createPaymentLink(AdsPackage adsPackage) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//        CreatePaymentLinkRequestBody requestBody = new CreatePaymentLinkRequestBody();
//        try {
//            requestBody.setProductName(adsPackage.getName());
//            requestBody.setDescription("Thanh toán gói quảng cáo");
//            requestBody.setPrice(adsPackage.getPrice());
//            requestBody.setReturnUrl("https://your-return-url.com");
//            requestBody.setCancelUrl("https://your-cancel-url.com");
//            // Gen order code
//            String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
//            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
//
//            ItemData item = ItemData.builder()
//                    .name(requestBody.getProductName())
//                    .price(requestBody.getPrice())
//                    .quantity(1)
//                    .build();
//
//            PaymentData paymentData = PaymentData.builder()
//                    .orderCode(orderCode)
//                    .description(requestBody.getDescription())
//                    .amount(requestBody.getPrice())
//                    .item(item)
//                    .returnUrl(requestBody.getReturnUrl())
//                    .cancelUrl(requestBody.getCancelUrl())
//                    .build();
//
//            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
//
//            response.put("error", 0);
//            response.put("message", "success");
//            response.set("data", objectMapper.valueToTree(data));
//            return response;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", "fail");
//            response.set("data", null);
//            return response;
//
//        }
//    }
//
//    /**
//     * Gets order by id.
//     *
//     * @param orderId the order id
//     * @return the order by id
//     */
//    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//
//        try {
//            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);
//
//            response.set("data", objectMapper.valueToTree(order));
//            response.put("error", 0);
//            response.put("message", "ok");
//
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//            response.set("data", null);
//            return response;
//        }
//
//    }
//}
