package pl.joboffers.domain.offer.dto;

public record JobOfferResponseDto(
        String companyName,
        String position,
        String salary,
        String offerUrl
) {
}
