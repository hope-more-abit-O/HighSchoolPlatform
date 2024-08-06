package com.demo.admissionportal.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CreatePaymentLinkRequestBody implements Serializable {
  private String productName;
  private String description;
  private String returnUrl;
  private int price;
  private String cancelUrl;
}