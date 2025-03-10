package pl.joboffers.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import pl.joboffers.domain.offer.dto.JobOfferResponseDto;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfferFacadeTest {
    @Test
    public void shouldFindOfferById() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(JobOffersData.giveOfferResponseDto());

        OfferResponseDto offerById = offerFacade.findOfferById(offerResponseDto.id());

        assertThat(offerById).isEqualTo(OfferResponseDto.builder()
                .id(offerResponseDto.id())
                .companyName("Company_1")
                .position("Position_1")
                .salary("Salary_1")
                .offerUrl("OfferUrl_1")
                .build()
        );
    }

    @Test
    public void shouldThrowOfferNotFoundExceptionWhenOfferHasNotBeenFound() {
        String id = "1";
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();

        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById(id));

        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage(String.format("Offer with id: [%s] not found", id));
    }

    @Test
    public void shouldFetchJobOffersFromRemoteAndSaveAllOffersWhenRepositoryIsEmpty() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();
        assertThat(offerFacade.findAllOffers()).isEmpty();

        List<OfferResponseDto> result = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        assertThat(result).hasSize(JobOffersData.fetchAllNewJobOffers().size());
    }

    @Test
    public void shouldSaveOnlyTwoOffersWhenRepositoryHadAlreadyFourOutOfSixOffersAddedWithOfferUrls() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
                JobOffersData.fetchAllNewJobOffers()
        ).offerFacadeForTests();
        offerFacade.saveOffer(new OfferRequestDto("Company_1", "Position_1", "Salary_1", "OfferUrl_1"));
        offerFacade.saveOffer(new OfferRequestDto("Company_2", "Position_2", "Salary_2", "OfferUrl_2"));
        offerFacade.saveOffer(new OfferRequestDto("Company_3", "Position_3", "Salary_3", "OfferUrl_3"));
        offerFacade.saveOffer(new OfferRequestDto("Company_4", "Position_4", "Salary_4", "OfferUrl_4"));
        assertThat(offerFacade.findAllOffers()).hasSize(4);

        List<OfferResponseDto> response = offerFacade.fetchAllOffersAndSaveAllIfNotExists();

        assertThat(List.of(
                        response.get(0).offerUrl(),
                        response.get(1).offerUrl()
                )
        ).containsExactlyInAnyOrder("OfferUrl_5", "OfferUrl_6");
    }

    @Test
    public void shouldSaveFourOffersWhenThereAreNoOffersInDatabase() {
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeForTests();

        offerFacade.saveOffer(new OfferRequestDto("Company_1", "Position_1", "Salary_1", "OfferUrl_1"));
        offerFacade.saveOffer(new OfferRequestDto("Company_2", "Position_2", "Salary_2", "OfferUrl_2"));
        offerFacade.saveOffer(new OfferRequestDto("Company_3", "Position_3", "Salary_3", "OfferUrl_3"));
        offerFacade.saveOffer(new OfferRequestDto("Company_4", "Position_4", "Salary_4", "OfferUrl_4"));

        assertThat(offerFacade.findAllOffers()).hasSize(4);
    }

    @Test
    public void shouldThrowOfferSavingExceptionWhenDuplicateOffersAreFetched() {
        OfferRepository offerRepository = mock(OfferRepository.class);
        OfferFetchable offerFetcher = () -> List.of(
                new JobOfferResponseDto("Company_1", "Position_1", "Salary_1", "OfferUrl_1")
        );
        OfferService offerService = new OfferService(offerFetcher, offerRepository);

        when(offerRepository.saveAll(anyList())).thenThrow(new DuplicateKeyException("Offer with offerUrl [OfferUrl_1] already exists"));

        Throwable thrown = catchThrowable(offerService::fetchAllAndSaveAllIfNotExists);

        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Offer with offerUrl [OfferUrl_1] already exists");
    }


}