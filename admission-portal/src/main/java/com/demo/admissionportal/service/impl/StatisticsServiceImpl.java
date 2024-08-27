package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.StatisticsPostResponse;
import com.demo.admissionportal.dto.response.statistics.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.UserFavoriteRepository;
import com.demo.admissionportal.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Statistics service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UniversityTransactionRepository universityTransactionRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final PackageRepository packageRepository;
    private final ConsultantInfoRepository consultantInfoRepository;
    private final UserLikeRepository userLikeRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseData<?> getStatistics() {
        try {
            Role user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            switch (user) {
                case ADMIN -> calculatorStatisticsByAdmin();
                case UNIVERSITY ->
                        calculatorStatisticsByUniversity(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
                default -> throw new Exception();
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê thành công", user == Role.ADMIN ?
                    calculatorStatisticsByAdmin() :
                    calculatorStatisticsByUniversity(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        } catch (Exception e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy dữ liệu thống kê thất bại");
        }
    }

    @Override
    public ResponseData<?> getStatisticsV2(Date startDay, Date endDay, String type, String role, String status, String period) {
        try {
            Role user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<StatisticsAdminResponseV3> statistics;
            String normalizedType = type.toLowerCase();

            LocalDate now = LocalDate.now();
            switch (period) {
                case "12 months":
                    startDay = Date.from(LocalDate.of(now.getYear(), 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    endDay = Date.from(LocalDate.of(now.getYear(), 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    break;
                case "6 months":
                    startDay = Date.from(now.minusMonths(6).withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    endDay = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    break;
                case "30 days":
                    startDay = Date.from(now.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    endDay = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    break;
                case "7 days":
                    startDay = Date.from(now.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    endDay = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    break;
                default:
                    throw new IllegalArgumentException("Chu kỳ không hợp lệ: " + period);
            }

            switch (user) {
                case ADMIN -> statistics = getStatistics(startDay, endDay, type, role, status);
                case UNIVERSITY -> statistics = getStatisticsByUniversity(startDay, endDay, type, status);
                default -> throw new UnsupportedOperationException("Người dùng không được phép truy cập.");
            }

            switch (normalizedType) {
                case "revenue":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê doanh thu thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticRevenueByTime)
                            .map(revenue -> (StatisticRevenueByTime) revenue)
                            .collect(Collectors.toList()));
                case "interaction":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê tương tác thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticInteractionByTime)
                            .map(interaction -> (StatisticInteractionByTime) interaction)
                            .collect(Collectors.toList()));
                case "account":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê tài khoản thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticAccountByTime)
                            .map(account -> (StatisticAccountByTime) account)
                            .collect(Collectors.toList()));
                case "post":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê bài viết thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticPostByTime)
                            .map(post -> (StatisticPostByTime) post)
                            .collect(Collectors.toList()));
                case "like":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê lượt thích thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticLikeByTime)
                            .map(like -> (StatisticLikeByTime) like)
                            .collect(Collectors.toList()));
                case "favourite":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê yêu thích thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticFavoriteByTime)
                            .map(favorite -> (StatisticFavoriteByTime) favorite)
                            .collect(Collectors.toList()));
                case "comment":
                    return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê bình luận thành công", statistics.stream()
                            .filter(stat -> stat instanceof StatisticCommentByTime)
                            .map(comment -> (StatisticCommentByTime) comment)
                            .collect(Collectors.toList()));
                default:
                    throw new IllegalArgumentException("Loại bộ lọc không hợp lệ: " + type);
            }
        } catch (UnsupportedOperationException e) {
            log.error("Unsupported role for get statistics: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C209.getCode(), e.getMessage());
        } catch (IllegalArgumentException | ParseException e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy dữ liệu thống kê thất bại");
        }
    }

    private StatisticsUniversityResponse calculatorStatisticsByUniversity(Integer universityId) {
        return StatisticsUniversityResponse.builder()
                .totalLike(calculatorLikeResponseByUniversity(universityId))
                .totalFavorite(calculatorFavoriteResponseByUniversity(universityId))
                .totalComment(calculatorCommentResponseByUniversity(universityId))
                .totalPost(calculatorTotalPost(universityId))
                .transactionDetail(mapToUniversityTransactionDetail(universityId))
                .build();
    }

    private Integer calculatorTotalPost(Integer universityId) {
        return postRepository.totalPostWithUniId(universityId).orElse(0);
    }

    private List<StatisticsTransactionDetailResponse> mapToUniversityTransactionDetail(Integer universityId) {
        List<UniversityTransaction> universityTransactionList = universityTransactionRepository.findUniversityTransactionByUniversityId(universityId);
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityId);
        return universityTransactionList.stream()
                .map(transaction -> {
                    AdsPackage adsPackage = packageRepository.findPackageById(transaction.getPackageId());
                    return StatisticsTransactionDetailResponse.builder()
                            .createBy(universityInfo != null ? universityInfo.getName() : null)
                            .price(adsPackage != null ? adsPackage.getPrice() : 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Integer calculatorCommentResponseByUniversity(Integer universityId) {
        return commentRepository.totalComment(universityId).orElse(0);
    }

    private Integer calculatorFavoriteResponseByUniversity(Integer universityId) {
        return userFavoriteRepository.totalFavoriteCount(universityId).orElse(0);
    }

    private Integer calculatorLikeResponseByUniversity(Integer universityId) {
        return userLikeRepository.totalLikePost(universityId).orElse(0);
    }

    private StatisticsAdminResponse calculatorStatisticsByAdmin() {
        return StatisticsAdminResponse.builder()
                .transaction(calculatorTransactionByAdmin())
                .interact(calculatorInteractResponseByAdmin())
                .account(calculatorAccountResponseByAdmin())
                .post(calculatorPostResponseByAdmin())
                .activityTransaction(calculatorActivityTransaction())
                .build();
    }

    @Async
    protected List<StatisticsTransactionDetailResponse> calculatorActivityTransaction() {
        List<UniversityTransaction> list = universityTransactionRepository.findAll();
        return list.parallelStream()
                .map(this::mapToTransactionDetail)
                .collect(Collectors.toList());
    }

    private StatisticsTransactionDetailResponse mapToTransactionDetail(UniversityTransaction universityTransaction) {
        StatisticsTransactionDetailResponse responseDTO = new StatisticsTransactionDetailResponse();
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityTransaction.getCreateBy());
        if (universityInfo != null) {
            responseDTO.setCreateBy(universityInfo.getName());
        } else {
            ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(universityTransaction.getCreateBy());
            if (consultantInfo != null) {
                UniversityInfo universityInfoByConsultant = universityInfoRepository.findUniversityInfoById(consultantInfo.getUniversityId());
                if (universityInfoByConsultant != null) {
                    responseDTO.setCreateBy(universityInfoByConsultant.getName());
                }
            }
        }
        responseDTO.setPrice(
                packageRepository.findById(universityTransaction.getPackageId())
                        .map(AdsPackage::getPrice)
                        .orElse(null)
        );
        return responseDTO;
    }


    @Async
    protected StatisticsPostResponse calculatorPostResponseByAdmin() {
        Integer totalPost = postRepository.totalPost().orElse(0);
        Integer currentPost = postRepository.currentPost().orElse(0);
        Integer activePost = postRepository.activePost().orElse(0);
        Integer inactivePost = postRepository.inactivePost().orElse(0);
        return StatisticsPostResponse.builder()
                .totalPost(totalPost)
                .currentPost(currentPost)
                .activePost(activePost)
                .inactivePost(inactivePost)
                .build();
    }

    @Async
    protected StatisticsAccountResponse calculatorAccountResponseByAdmin() {
        return StatisticsAccountResponse.builder()
                .totalAccount(userRepository.totalAccount().orElse(0))
                .accountActive(userRepository.accountActive().orElse(0))
                .accountInactive(userRepository.accountInactive().orElse(0))
                .currentAccount(userRepository.currentAccount().orElse(0))
                .user(calculatorUser())
                .staff(calculatorStaff())
                .university(calculatorUniversity())
                .consultant(calculatorConsultant())
                .build();
    }

    @Async
    protected StatisticsAccountResponse.StatisticsAccountUserResponse calculatorUser() {
        return StatisticsAccountResponse.StatisticsAccountUserResponse.builder()
                .total(userRepository.totalUserAccount().orElse(0))
                .current(userRepository.currentUserAccount().orElse(0))
                .active(userRepository.totalUserAccountActive().orElse(0))
                .inactive(userRepository.totalUserAccountInactive().orElse(0))
                .build();
    }

    @Async
    protected StatisticsAccountResponse.StatisticsAccountStaffResponse calculatorStaff() {
        return StatisticsAccountResponse.StatisticsAccountStaffResponse.builder()
                .total(userRepository.totalStaffAccount().orElse(0))
                .active(userRepository.totalStaffAccountActive().orElse(0))
                .current(userRepository.currentStaffAccount().orElse(0))
                .inactive(userRepository.totalStaffAccountInactive().orElse(0))
                .build();
    }

    @Async
    protected StatisticsAccountResponse.StatisticsAccountConsultantResponse calculatorConsultant() {
        return StatisticsAccountResponse.StatisticsAccountConsultantResponse.builder()
                .total(userRepository.totalConsultantAccount().orElse(0))
                .active(userRepository.totalConsultantAccountActive().orElse(0))
                .current(userRepository.currentConsultantAccount().orElse(0))
                .inactive(userRepository.totalConsultantAccountInactive().orElse(0))
                .build();
    }

    @Async
    protected StatisticsAccountResponse.StatisticsAccountUniversityResponse calculatorUniversity() {
        return StatisticsAccountResponse.StatisticsAccountUniversityResponse.builder()
                .total(userRepository.totalUniversityAccount().orElse(0))
                .active(userRepository.totalUniversityAccountActive().orElse(0))
                .current(userRepository.currentUniversityAccount().orElse(0))
                .inactive(userRepository.totalUniversityAccountInactive().orElse(0))
                .build();
    }

    @Async
    protected StatisticsInteractResponse calculatorInteractResponseByAdmin() {
        Integer totalInteraction = userFavoriteRepository.totalInteraction().orElse(0);
        Integer currentInteraction = userFavoriteRepository.currentInteraction().orElse(0);
        return StatisticsInteractResponse.builder()
                .totalInteraction(totalInteraction)
                .currentInteraction(currentInteraction)
                .build();
    }

    @Async
    protected StatisticsTransactionResponse calculatorTransactionByAdmin() {
        Integer totalTransaction = universityTransactionRepository.calculatorTotalTransaction().orElse(0);
        Integer currentTransaction = universityTransactionRepository.calculatorCurrentTransaction().orElse(0);
        return StatisticsTransactionResponse.builder()
                .totalTransaction(totalTransaction)
                .currentTransaction(currentTransaction)
                .build();
    }

    public List<StatisticsAdminResponseV3> getStatistics(Date startDay, Date endDay, String type, String role, String status) throws Exception {
        List<StatisticsAdminResponseV3> statistics = new ArrayList<>();
        String normalizedType = type.toLowerCase();
        List<Object[]> data;
        Date date;

        switch (normalizedType) {
            case "interaction":
                try {
                    data = universityTransactionRepository.findTotalInteractionsByPeriod(startDay, endDay);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticInteractionByTime.builder()
                                    .date(date)
                                    .interactionCount((Integer) record[1])
                                    .type("Tương tác")
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê lượt tương tác: " + e.getMessage());
                }
                break;

            case "revenue":
                try {
                    data = universityTransactionRepository.findTotalRevenueByPeriod(startDay, endDay);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticRevenueByTime.builder()
                                    .date(date)
                                    .revenue((Integer) record[1])
                                    .type("Doanh Thu")
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-ddDate parsing error in revenue data: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê doanh thu: " + e.getMessage());
                }
                break;

            case "account":
                try {
                    data = universityTransactionRepository.findTotalAccountsByPeriod(startDay, endDay, role, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticAccountByTime.builder()
                                    .date(date)
                                    .type("Tài khoản")
                                    .totalAccount((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd" + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê tài khoản: " + e.getMessage());
                }
                break;

            case "post":
                try {
                    data = universityTransactionRepository.findTotalPostsByPeriod(startDay, endDay, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticPostByTime.builder()
                                    .date(date)
                                    .type("Bài viết")
                                    .totalPosts((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê bài viết: " + e.getMessage());
                }
                break;

            default:
                throw new IllegalArgumentException("Loại bộ lọc không hợp lệ: " + type);
        }

        return statistics;
    }

    public List<StatisticsAdminResponseV3> getStatisticsByUniversity(Date startDay, Date endDay, String type, String status) throws Exception {
        List<StatisticsAdminResponseV3> statistics = new ArrayList<>();
        List<Object[]> data;
        Date date;
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer universityId = authenticatedUser.getId();

        switch (type.toLowerCase()) {
            case "post":
                try {
                    data = universityTransactionRepository.findTotalPostsByPeriodAndUniversity(startDay, endDay, universityId, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticPostByTime.builder()
                                    .date(date)
                                    .type("Bài viết")
                                    .totalPosts((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê bài viết: " + e.getMessage());
                }
                break;

            case "like":
                try {
                    data = universityTransactionRepository.findTotalLikesByPeriodAndUniversity(startDay, endDay, universityId, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticLikeByTime.builder()
                                    .date(date)
                                    .type("Lượt thích")
                                    .totalLikes((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê lượt thích: " + e.getMessage());
                }
                break;

            case "favourite":
                try {
                    data = universityTransactionRepository.findTotalFavoritesByPeriodAndUniversity(startDay, endDay, universityId, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticFavoriteByTime.builder()
                                    .date(date)
                                    .type("Lượt theo dõi")
                                    .totalFavorites((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê lượt theo dõi: " + e.getMessage());
                }
                break;

            case "comment":
                try {
                    data = universityTransactionRepository.findTotalCommentsByPeriodAndUniversity(startDay, endDay, universityId, status);
                    for (Object[] record : data) {
                        try {
                            if (record[0] instanceof Date) {
                                date = (Date) record[0];
                            } else if (record[0] instanceof String dateString) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.parse(dateString);
                            } else {
                                throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd.");
                            }
                            statistics.add(StatisticCommentByTime.builder()
                                    .date(date)
                                    .type("Lượt bình luận")
                                    .totalComments((Integer) record[1])
                                    .build());
                        } catch (ParseException e) {
                            throw new Exception("Dữ liệu start day và end day phải là: yyyy-MM-dd: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("Có lỗi xảy ra khi lấy thống kê lượt bình luận: " + e.getMessage());
                }
                break;

            default:
                throw new Exception("Loại bộ lọc không hợp lệ: " + type);
        }

        return statistics;
    }

}
