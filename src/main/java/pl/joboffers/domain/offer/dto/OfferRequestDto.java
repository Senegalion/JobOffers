package pl.joboffers.domain.offer.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record OfferRequestDto(
        @NotNull(message = "{companyName.not.null}")
        @NotEmpty(message = "{companyName.not.empty}")
        String companyName,
        @NotNull(message = "{position.not.null}")
        @NotEmpty(message = "{position.not.empty}")
        String position,
        @NotNull(message = "{salary.not.null}")
        @NotEmpty(message = "{salary.not.empty}")
        String salary,
        @NotNull(message = "{offerUrl.not.null}")
        @NotEmpty(message = "{offerUrl.not.empty}")
        String offerUrl) {
}
