package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import pl.joboffers.domain.offer.dto.OfferRequestDto;

import java.util.List;

public class JobOffersData {
    public static List<JobOfferResponseDto> fetchAllNewJobOffers() {
        return List.of(
                new JobOfferResponseDto("Company_1", "Position_1", "Salary_1", "OfferUrl_1"),
                new JobOfferResponseDto("Company_2", "Position_2", "Salary_2", "OfferUrl_2"),
                new JobOfferResponseDto("Company_3", "Position_3", "Salary_3", "OfferUrl_3"),
                new JobOfferResponseDto("Company_4", "Position_4", "Salary_4", "OfferUrl_4"),
                new JobOfferResponseDto("Company_5", "Position_5", "Salary_5", "OfferUrl_5"),
                new JobOfferResponseDto("Company_6", "Position_6", "Salary_6", "OfferUrl_6")
        );
    }

    public static OfferRequestDto giveOfferResponseDto() {
        return OfferRequestDto.builder()
                .companyName("Company_1")
                .position("Position_1")
                .salary("Salary_1")
                .offerUrl("OfferUrl_1")
                .build();
    }

    public static List<JobOfferResponseDto> fetchTwoNewAndFourOldJobOffers() {
        return List.of(
                new JobOfferResponseDto("Company_1", "Position_1", "Salary_1", "OfferUrl_1"),
                new JobOfferResponseDto("Company_2", "Position_2", "Salary_2", "OfferUrl_2"),
                new JobOfferResponseDto("Company_3", "Position_3", "Salary_3", "OfferUrl_3"),
                new JobOfferResponseDto("Company_4", "Position_4", "Salary_4", "OfferUrl_4"),
                new JobOfferResponseDto("Company_5", "Position_5", "Salary_5", "OfferUrl_5"),
                new JobOfferResponseDto("Company_6", "Position_6", "Salary_6", "OfferUrl_6")
        );
    }
}
